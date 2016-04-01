package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;

import java.time.LocalTime;
import java.util.ArrayList;

public class EditCommand implements Command {

	private TaskParameters _task;
	private InvalidCommand _invalidCommand;
	private int _index;
	private REMOVE_TYPE _removeType;

	private String ERROR_MESSAGE_INVALID_START_DATE = "Invalid start date";
	private String ERROR_MESSAGE_INVALID_START_DATE_TIME = "Invalid start date and time";
	private String ERROR_MESSAGE_INVALID_START_TIME = "Invalid start time";
	private String ERROR_MESSAGE_INVALID_END_DATE = "Invalid end date";
	private String ERROR_MESSAGE_INVALID_END_DATE_TIME = "Invalid end date and time";
	private String ERROR_MESSAGE_INVALID_END_TIME = "Invalid end time";
	private String ERROR_MESSAGE_NO_START_DATE = "The task do not have start date";
	private String ERROR_MESSAGE_NO_END_DATE = "The task do not have end date";

	public enum REMOVE_TYPE {START, START_TIME, START_DATE, END, END_TIME, END_DATE, TAG}

	public EditCommand(int index, TaskParameters newTaskParameters) {
		assert(index >= 1);
		_task = newTaskParameters;
		_index = index - 1;
	}

	public EditCommand(int index, TaskParameters newTaskParameters, REMOVE_TYPE removeType) {
		assert(index >= 1);
		_task = newTaskParameters;
		_index = index - 1;
		_removeType = removeType;
	}

	public void setRemoveType(REMOVE_TYPE removeType) {
		_removeType = removeType;
	}

	public Command execute() {
		return null;
	}

	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((_index >= 0) && (_index < listsManager.getViewList().size()));
		/*
		if (!isWithinList(listsManager.getViewList(), _index)) {
			return false;
		}
		 */
		Task oldTask = listsManager.getViewList().get(_index);
		listsManager.getMainList().remove(oldTask);

		Task newTask = new Task();
		initializeNewTask(oldTask, newTask);

		LocalTime startTime = LocalTime.of(00, 00);
		LocalTime endTime = LocalTime.of(23, 59);

		editTask(oldTask, newTask, startTime, endTime);

		if (_invalidCommand == null) {
			listsManager.getMainList().add(newTask);
			listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
			listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
			listsManager.updateLists();
			RollbackItem rollbackItem = new RollbackItem("edit", oldTask, newTask);
			historyManager.getUndoList().add(rollbackItem);
			historyManager.setRedoList(new ArrayList<RollbackItem>());
		}
		System.out.println("undolist size: " + historyManager.getUndoList().size());
		return _invalidCommand;
	}
	/*
	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
	 */

	private void initializeNewTask(Task oldTask, Task newTask) {
		newTask.setDescription(oldTask.getDescription());
		newTask.setTagsList(oldTask.getTagsList());
		newTask.setStartDate(oldTask.getStartDate());
		newTask.setStartTime(oldTask.getStartTime());
		newTask.setEndDate(oldTask.getEndDate());
		newTask.setEndTime(oldTask.getEndTime());
		newTask.setIsDone(oldTask.getIsDone());
		newTask.setIsOverdue(oldTask.getIsOverdue());
	}

	private void editTask(Task oldTask, Task newTask, LocalTime startTime, LocalTime endTime) {
		editTaskDescription(newTask);
		editTaskTagsList(newTask);
		editTaskStartDateOnly(oldTask, newTask, startTime);
		editTaskStartTimeOnly(oldTask, newTask, endTime);
		editTaskStartDateTime(oldTask, newTask, endTime);
		editTaskEndDateOnly(oldTask, newTask, endTime);
		editTaskEndTimeOnly(oldTask, newTask, startTime);
		editTaskEndDateTime(oldTask, newTask, startTime);
		editTaskStartDateTimeEndDateTime(oldTask, newTask, endTime);
		removeTaskParameters(oldTask, newTask);
	}

	private void removeTaskParameters(Task oldTask, Task newTask) {
		switch (_removeType) {
		case START : 
			removeTaskStartDateTime(newTask);
			break;
		case START_DATE : 
			removeTaskStartDate(newTask);
			break;
		case START_TIME :
			removeTaskStartTime(oldTask, newTask);
			break;
		case END :
			removeTaskEndDateTime(newTask);
			break;
		case END_DATE :
			removeTaskEndDate(oldTask, newTask);
			break;
		case END_TIME :
			removeTaskEndTime(oldTask, newTask);
			break;
		case TAG :
			removeTaskTagsList(newTask);
			break;
		}
	}

	private void removeTaskTagsList(Task newTask) {
		newTask.getTagsList().clear();
	}

	private void removeTaskEndTime(Task oldTask, Task newTask) {
		if ((oldTask.getStartDate() != null) && (oldTask.getStartTime() != null) 
				&& (oldTask.getEndDate() != null) && (oldTask.getEndTime() != null)) {
			newTask.setStartTime(null);
		}
		newTask.setEndTime(null);
	}

	private void removeTaskEndDate(Task oldTask, Task newTask) {
		newTask.setEndDate(null);
		newTask.setEndTime(null);
	}

	private void removeTaskEndDateTime(Task newTask) {
		newTask.setStartTime(null);
		newTask.setEndDate(null);
		newTask.setEndTime(null);
	}

	private void removeTaskStartTime(Task oldTask, Task newTask) {
		if ((oldTask.getStartDate() != null) && (oldTask.getStartTime() != null) 
				&& (oldTask.getEndDate() != null) && (oldTask.getEndTime() != null)) {
			newTask.setEndTime(null);
		}
		newTask.setStartTime(null);
	}

	private void removeTaskStartDate(Task newTask) {
		newTask.setStartDate(null);
		newTask.setStartTime(null);
	}

	private void removeTaskStartDateTime(Task newTask) {
		newTask.setStartDate(null);
		newTask.setStartTime(null);
		newTask.setEndTime(null);
	}

	private void editTaskStartDateTimeEndDateTime(Task oldTask, Task newTask, LocalTime endTime) {
		if ((_task.getStartDate() != null) && (_task.getStartTime() != null) 
				&& (_task.getEndDate() != null) && (_task.getEndTime() != null)) {
			newTask.setStartDate(_task.getStartDate());
			newTask.setStartTime(_task.getStartTime());
			newTask.setEndDate(_task.getEndDate());
			newTask.setEndTime(_task.getEndTime());
		}
	}

	private void editTaskEndDateTime(Task oldTask, Task newTask, LocalTime startTime) {
		if ((_task.getStartDate() == null) && (_task.getStartTime() == null) 
				&& (_task.getEndDate() != null) && (_task.getEndTime() != null)) {
			if ((oldTask.getStartDate() != null) && 
					(oldTask.getStartDate().compareTo(_task.getEndDate()) > 0)) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_END_DATE);
			}

			if ((oldTask.getStartDate() != null) && (oldTask.getStartTime() == null)) {
				newTask.setEndTime(startTime);
			}

			if ((oldTask.getStartDate() != null) && (oldTask.getStartTime() != null) 
					&& (_task.getEndDate().isEqual(oldTask.getStartDate()))
					&& ((_task.getEndTime().compareTo(oldTask.getStartTime())) < 0)) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_END_DATE_TIME);
			}
			newTask.setStartDate(_task.getStartDate());
		}
	}

	private void editTaskStartDateTime(Task oldTask, Task newTask, LocalTime endTime) {
		if ((_task.getStartDate() != null) && (_task.getStartTime() != null) 
				&& (_task.getEndDate() == null) && (_task.getEndTime() == null)) {
			if ((oldTask.getEndDate() != null) && 
					(_task.getStartDate().compareTo(oldTask.getEndDate()) > 0)) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_START_DATE);
			}

			if ((oldTask.getEndDate() != null) && (oldTask.getEndTime() == null)) {
				newTask.setEndTime(endTime);
			}

			if ((oldTask.getEndDate() != null) && (oldTask.getEndTime() != null) 
					&& (_task.getStartDate().isEqual(oldTask.getEndDate()))
					&& ((oldTask.getEndTime().compareTo(_task.getStartTime())) < 0)) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_START_DATE_TIME);
			}
			newTask.setStartDate(_task.getStartDate());
		}
	}

	private void editTaskEndTimeOnly(Task oldTask, Task newTask, LocalTime startTime) {
		if ((_task.getStartDate() == null) && (_task.getStartTime() == null) 
				&& (_task.getEndDate() == null) && (_task.getEndTime() != null)) {
			if (oldTask.getEndDate() == null) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_NO_END_DATE);
			}

			if ((oldTask.getStartDate() != null) && (oldTask.getEndDate() != null) 
					&& (oldTask.getStartTime() == null)) {
				newTask.setStartTime(startTime);
			}

			if ((oldTask.getStartDate() != null) && (oldTask.getEndDate() != null) 
					&& (oldTask.getStartTime() != null) 
					&& (oldTask.getStartDate().isEqual(oldTask.getEndDate()))
					&& ((_task.getEndTime().compareTo(oldTask.getStartTime())) < 0)) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_END_TIME);
			}
			newTask.setEndTime(_task.getEndTime());
		}
	}

	private void editTaskEndDateOnly(Task oldTask, Task newTask, LocalTime endTime) {
		if ((_task.getStartDate() == null) && (_task.getStartTime() == null) 
				&& (_task.getEndDate() != null) && (_task.getEndTime() == null)) {
			if ((oldTask.getStartDate() != null) && 
					(_task.getEndDate().compareTo(oldTask.getStartDate()) < 0)) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_END_DATE);
			}
			if ((oldTask.getStartDate() != null) && (oldTask.getStartTime() != null)) {
				newTask.setEndTime(endTime);
			}
			newTask.setEndDate(_task.getEndDate());
		}
	}

	private void editTaskStartTimeOnly(Task oldTask, Task newTask, LocalTime endTime) {
		if ((_task.getStartDate() == null) && (_task.getStartTime() != null) 
				&& (_task.getEndDate() == null) && (_task.getEndTime() == null)) {
			if (oldTask.getStartDate() == null) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_NO_START_DATE);
			}

			if ((oldTask.getStartDate() != null) && (oldTask.getEndDate() != null) 
					&& (oldTask.getEndTime() == null)) {
				newTask.setEndTime(endTime);
			}

			if ((oldTask.getStartDate() != null) && (oldTask.getEndDate() != null) 
					&& (oldTask.getEndTime() != null) 
					&& (oldTask.getStartDate().isEqual(oldTask.getEndDate()))
					&& ((_task.getStartTime().compareTo(oldTask.getEndTime())) > 0)) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_START_TIME);
			}
			newTask.setStartTime(_task.getStartTime());
		}
	}

	private void editTaskStartDateOnly(Task oldTask, Task newTask, LocalTime startTime) {
		if ((_task.getStartDate() != null) && (_task.getStartTime() == null) 
				&& (_task.getEndDate() == null) && (_task.getEndTime() == null)) {
			if ((oldTask.getEndDate() != null) && 
					(_task.getStartDate().compareTo(oldTask.getEndDate()) > 0)) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_START_DATE);
			}
			if ((oldTask.getEndDate() != null) && (oldTask.getEndTime() != null)) {
				newTask.setStartTime(startTime);
			}
			newTask.setStartDate(_task.getStartDate());
		}
	}

	private void editTaskTagsList(Task newTask) {
		if (_task.getTagsList() != null) {
			newTask.setTagsList(_task.getTagsList());
		}
	}

	private void editTaskDescription(Task newTask) {
		if (_task.getDescription() != null) {
			newTask.setDescription(_task.getDescription());
		}
	}
}
