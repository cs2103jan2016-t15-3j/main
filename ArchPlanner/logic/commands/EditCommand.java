package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Logic;
import logic.Logic.COMMAND_TYPE;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;
import storage.Storage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class is used to execute edit command.
 * 
 * @@author A0140021J
 *
 */
public class EditCommand implements CommandInterface {

	//This is the logger used to log and observe the changes when program runs.
	static Logger log = Logger.getLogger(Logic.class.getName());

	//This is the taskParameters of an EditCommand object.
	private TaskParameters _taskParameters;

	//This is the invalidCommand to be initialized if edit command is invalid.
	private InvalidCommand _invalidCommand;

	//This is the index of the task to be edited.
	private int _index;

	//This variable indicates which parameter of the task to be removed.
	private REMOVE_TYPE _removeType;

	//This is the message of an EditCommand object to be displayed.
	private String _message;

	//These are constant string variables of error messages to be displayed if edit command is invalid.
	private final String ERROR_MESSAGE_INVALID_START_DATE = "Invalid start date";
	private final String ERROR_MESSAGE_INVALID_START_DATE_TIME = "Invalid start date and time";
	private final String ERROR_MESSAGE_INVALID_START_TIME = "Invalid start time";
	private final String ERROR_MESSAGE_INVALID_END_DATE = "Invalid end date";
	private final String ERROR_MESSAGE_INVALID_END_DATE_TIME = "Invalid end date and time";
	private final String ERROR_MESSAGE_INVALID_END_TIME = "Invalid end time";
	private final String ERROR_MESSAGE_NO_START_DATE = "The task has no start date";
	private final String ERROR_MESSAGE_NO_END_DATE = "The task has no end date";

	//This constant string variable is the standard format for display message upon editing of task successfully.
	private final String MESSAGE_EDIT_COMMAND = "edited \"%1$s\"";

	//These are constant string variables for logging.
	private final String  LOGGER_MESSAGE_EXECUTING_EDIT_COMMAND = "Executing edit command...";
	private final String  LOGGER_MESSAGE_COMPLETED_EDIT_COMMAND = "Completed edit command.";

	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";

	public enum REMOVE_TYPE {START, START_TIME, END, END_TIME, TAG, NONE}

	//This is constructor of the class.
	public EditCommand(int index, TaskParameters newTaskParameters) {
		assert(index >= 1);
		_taskParameters = newTaskParameters;
		_index = index - 1;
		_removeType= REMOVE_TYPE.NONE;
		_message = STRING_EMPTY;
	}

	//This is constructor of the class.
	public EditCommand(int index, TaskParameters newTaskParameters, REMOVE_TYPE removeType) {
		assert(index >= 1);
		_taskParameters = newTaskParameters;
		_index = index - 1;
		_removeType = removeType;
		_message = STRING_EMPTY;
	}

	/**
	 * This is setter method for EditCommand's taskParameters.
	 * 
	 * @param taskParameters This will be the taskParameters of the EditCommand.
	 */
	public void setTaskParameters(Task taskParameters) {
		_taskParameters = taskParameters;
	}

	/**
	 * This is setter method for EditCommand's invalidCommand.
	 * 
	 * @param invalidCommand This will be the invalidCommand of the EditCommand.
	 */
	public void setInvalidCommand(InvalidCommand invalidCommand) {
		_invalidCommand = invalidCommand;
	}

	/**
	 * This is setter method for EditCommand's index.
	 * 
	 * @param index This will be the index of the EditCommand.
	 */
	public void setIndex(int index) {
		_index = index;
	}

	/**
	 * This is setter method for EditCommand's removeType.
	 * 
	 * @param removeType This will be the removeType of the EditCommand.
	 */
	public void setRemoveType(REMOVE_TYPE removeType) {
		_removeType = removeType;
	}

	/**
	 * This is setter method for EditCommand's message.
	 * 
	 * @param message This will be the message of EditCommand.
	 */
	public void setMessage(String message) {
		_message = message;
	}

	/**
	 * This is getter method for EditCommand's taskParameters.
	 * 
	 * @return taskParameters.
	 */
	public TaskParameters getTaskParameters() {
		return _taskParameters;
	}

	/**
	 * This is getter method for EditCommand's invalidCommand.
	 * 
	 * @return invalidCommand.
	 */
	public InvalidCommand getInvalidCommand() {
		return _invalidCommand;
	}

	/**
	 * This is getter method for EditCommand's index.
	 * 
	 * @return index.
	 */
	public int getIndex() {
		return _index;
	}

	/**
	 * This is getter method for EditCommand's removeType.
	 * 
	 * @return removeType.
	 */
	public REMOVE_TYPE getRemoveType() {
		return _removeType;
	}

	/**
	 * This is getter method for EditCommand's message.
	 * 
	 * @return message.
	 */
	public String getMessage() {
		return _message;
	}

	/**
	 * This method will not be called.
	 */
	public CommandInterface execute() {
		return null;
	}

	/**
	 * This method will not be called.
	 */
	public CommandInterface execute(ListsManager listsManager, Storage storage) {
		return null;
	}

	/**
	 * This method is used to execute edit command and edit the task followed by updating the relevant lists.
	 */
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((getIndex() >= 0) && (getIndex() < listsManager.getViewList().size()));

		log.info(LOGGER_MESSAGE_EXECUTING_EDIT_COMMAND);
		clearIndexList(listsManager);
		Task oldTask = listsManager.getViewList().get(getIndex());

		Task newTask = new Task();
		initializeNewTask(oldTask, newTask);

		LocalTime startTime = LocalTime.of(00, 00);
		LocalTime endTime = LocalTime.of(23, 59);

		editTask(oldTask, newTask, startTime, endTime);

		if (getInvalidCommand() != null) {
			log.info(getInvalidCommand().getMessage());
			return getInvalidCommand();
		}
		setMessage(String.format(MESSAGE_EDIT_COMMAND, newTask.getDescription()));
		updateListsManager(listsManager, oldTask, newTask);
		updateHistoryManager(historyManager, oldTask, newTask);
		log.info(getMessage());
		log.info(LOGGER_MESSAGE_COMPLETED_EDIT_COMMAND);
		return null;
	}

	/**
	 * This method is used to clear indexList in ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 */
	private void clearIndexList(ListsManager listsManager) {
		listsManager.getIndexList().clear();
	}

	/**
	 * This method is used to update HistoryManager.
	 * 
	 * @param historyManager This is the HistoryManager.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask this is the task after been edited.
	 */
	private void updateHistoryManager(HistoryManager historyManager, Task oldTask, Task newTask) {
		RollbackItem rollbackItem = new RollbackItem(COMMAND_TYPE.EDIT, oldTask, newTask);
		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
	}

	/**
	 * This method is used to update ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 */
	private void updateListsManager(ListsManager listsManager, Task oldTask, Task newTask) {
		listsManager.getMainList().remove(oldTask);
		listsManager.getMainList().add(newTask);
		listsManager.updateLists();
		listsManager.updateIndexList(newTask);
	}

	/**
	 * This method is used to initialize the new task with old task properties.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
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

	/**
	 * This method is used to edit the tasks based on edit command attributes.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 * 
	 * @param startTime This is the start time at 12:00am.
	 * 
	 * @param endTime This is the end time at 11:59pm.
	 */
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
		editTaskStartDateEndDate(oldTask, newTask, endTime);
		removeTaskParameters(oldTask, newTask);
	}

	/**
	 * This method is used to differentiate the removeType and execute the respective method for removing taskParameters.
	 * @param oldTask
	 * @param newTask
	 */
	private void removeTaskParameters(Task oldTask, Task newTask) {
		switch (_removeType) {
		case START : 
			removeTaskStartDate(newTask);
			break;
		case START_TIME :
			removeTaskStartTime(oldTask, newTask);
			break;
		case END :
			removeTaskEndDate(oldTask, newTask);
			break;
		case END_TIME :
			removeTaskEndTime(oldTask, newTask);
			break;
		case TAG :
			removeTaskTagsList(newTask);
			break;
		default :
			break;
		}
	}

	/**
	 * This method is used to clear tagsList of the task.
	 * 
	 * @param newTask This is the task after been edited.
	 */
	private void removeTaskTagsList(Task newTask) {
		newTask.getTagsList().clear();
	}

	/**
	 * This method is used to remove end time of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 */
	private void removeTaskEndTime(Task oldTask, Task newTask) {
		if (oldTask.getStartDate() != null && oldTask.getStartTime() != null && oldTask.getEndDate() != null 
				&& oldTask.getEndTime() != null) {
			newTask.setStartTime(null);
		}
		newTask.setEndTime(null);
	}

	/**
	 * This method is used to remove end time of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 */
	private void removeTaskEndDate(Task oldTask, Task newTask) {
		newTask.setEndDate(null);
		newTask.setEndTime(null);
	}

	/**
	 *This method is used to remove start time of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 */
	private void removeTaskStartTime(Task oldTask, Task newTask) {
		if ((oldTask.getStartDate() != null) && (oldTask.getStartTime() != null) && (oldTask.getEndDate() != null)
				&& (oldTask.getEndTime() != null)) {
			newTask.setEndTime(null);
		}
		newTask.setStartTime(null);
	}

	/**
	 * This method is used to remove start date of the task.
	 * 
	 * @param newTask This is the task after been edited.
	 */
	private void removeTaskStartDate(Task newTask) {
		newTask.setStartDate(null);
		newTask.setStartTime(null);
	}

	/**
	 * This method is used to edit start date and end date of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 * 
	 * @param endTime This is the end time at 11:59pm.
	 */
	private void editTaskStartDateEndDate(Task oldTask, Task newTask, LocalTime endTime) {
		if (hasTaskParametersStartDateNotEqualsNullAndStartTimeEqualsNull() 
				&& hasTaskParametersEndDateNotEqualsNullAndEndTimeEqualsNull()) {
			newTask.setStartDate(getTaskParameters().getStartDate());
			newTask.setEndDate(getTaskParameters().getEndDate());
			newTask.setStartTime(null);
			newTask.setEndTime(null);
		}
	}

	/**
	 * This method is used to edit start date time and end date time of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 * 
	 * @param endTime This is the end time at 11:59pm.
	 */
	private void editTaskStartDateTimeEndDateTime(Task oldTask, Task newTask, LocalTime endTime) {
		if (hasTaskParametersStartDateNotEqualsNullAndStartTimeNotEqualsNull() 
				&& hasTaskParametersEndDateNotEqualsNullAndEndTimeNotEqualsNull()) {
			newTask.setStartDate(getTaskParameters().getStartDate());
			newTask.setStartTime(getTaskParameters().getStartTime());
			newTask.setEndDate(getTaskParameters().getEndDate());
			newTask.setEndTime(getTaskParameters().getEndTime());
		}
	}

	/**
	 * This method is used to edit end date time of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 * 
	 * @param startTime This is the start time at 12:00am.
	 */
	private void editTaskEndDateTime(Task oldTask, Task newTask, LocalTime startTime) {
		if (hasTaskParametersStartDateEqualsNullAndStartTimeEqualsNull() 
				&& hasTaskParametersEndDateNotEqualsNullAndEndTimeNotEqualsNull()) {
			if (oldTask.getStartDate() != null && oldTask.getStartDate().compareTo(getTaskParameters().getEndDate()) > 0) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_END_DATE);
			}		

			if (oldTask.getStartDate() != null && oldTask.getStartTime() == null) {
				newTask.setStartTime(startTime);
			}

			if (oldTask.getStartDate() != null && oldTask.getStartTime() != null 
					&& getTaskParameters().getEndDate().isEqual(oldTask.getStartDate())
					&& getTaskParameters().getEndTime().compareTo(oldTask.getStartTime()) < 0) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_END_DATE_TIME);
			}
			newTask.setEndDate(getTaskParameters().getEndDate());
			newTask.setEndTime(getTaskParameters().getEndTime());
		}
	}

	/**
	 * This method is used to edit start date time of the task
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 * 
	 * @param endTime This is the end time at 11:59pm.
	 */
	private void editTaskStartDateTime(Task oldTask, Task newTask, LocalTime endTime) {
		if (hasTaskParametersStartDateNotEqualsNullAndStartTimeNotEqualsNull() 
				&& hasTaskParametersEndDateEqualsNullAndEndTimeEqualsNull()) {
			if (oldTask.getEndDate() != null && 
					getTaskParameters().getStartDate().compareTo(oldTask.getEndDate()) > 0) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_START_DATE);
			}

			if (oldTask.getEndDate() != null && oldTask.getEndTime() == null) {
				newTask.setEndTime(endTime);
			}

			if (oldTask.getEndDate() != null && oldTask.getEndTime() != null 
					&& getTaskParameters().getStartDate().isEqual(oldTask.getEndDate())
					&& oldTask.getEndTime().compareTo(getTaskParameters().getStartTime()) < 0) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_START_DATE_TIME);
			}
			newTask.setStartDate(getTaskParameters().getStartDate());
			newTask.setStartTime(getTaskParameters().getStartTime());
		}
	}

	/**
	 * This method is used to edit end time of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 * 
	 * @param startTime This is the start time at 12:00am.
	 */
	private void editTaskEndTimeOnly(Task oldTask, Task newTask, LocalTime startTime) {
		if (hasTaskParametersStartDateEqualsNullAndStartTimeEqualsNull() 
				&& hasTaskParametersEndDateEqualsNullAndEndTimeNotEqualsNull()) {
			if (oldTask.getEndDate() == null) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_NO_END_DATE);
			}

			if (oldTask.getStartDate() != null && oldTask.getEndDate() != null && oldTask.getStartTime() == null) {
				newTask.setStartTime(startTime);
			}

			if (oldTask.getStartDate() != null && oldTask.getEndDate() != null 
					&& oldTask.getStartTime() != null 
					&& oldTask.getStartDate().isEqual(oldTask.getEndDate())
					&& getTaskParameters().getEndTime().compareTo(oldTask.getStartTime()) < 0) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_END_TIME);
			}
			newTask.setEndTime(_taskParameters.getEndTime());
		}
	}

	/**
	 * This method is used to edit end date of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 * 
	 * @param endTime This is the end time at 11:59pm.
	 */
	private void editTaskEndDateOnly(Task oldTask, Task newTask, LocalTime endTime) {
		if (hasTaskParametersStartDateEqualsNullAndStartTimeEqualsNull() 
				&& hasTaskParametersEndDateNotEqualsNullAndEndTimeEqualsNull()) {
			if (oldTask.getStartDate() != null && getTaskParameters().getEndDate().compareTo(oldTask.getStartDate()) < 0) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_END_DATE);
			}
			if ((oldTask.getStartDate() != null && oldTask.getStartTime() != null && oldTask.getEndDate() == null 
					&& oldTask.getEndTime() == null) || (oldTask.getStartDate() != null && oldTask.getStartTime() != null 
					&& oldTask.getEndDate() != null && oldTask.getEndTime() != null 
					&& getTaskParameters().getEndDate().isEqual(oldTask.getStartDate())
					&& oldTask.getStartTime().isAfter(oldTask.getEndTime()))) {
				newTask.setEndTime(endTime);
			}
			newTask.setEndDate(getTaskParameters().getEndDate());
		}
	}

	/**
	 * This method is used to edit start time of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 * 
	 * @param endTime This is the end time at 11:59pm.
	 */
	private void editTaskStartTimeOnly(Task oldTask, Task newTask, LocalTime endTime) {
		if (hasTaskParametersStartDateEqualsNullAndStartTimeNotEqualsNull() 
				&& hasTaskParametersEndDateEqualsNullAndEndTimeEqualsNull()) {
			if (oldTask.getStartDate() == null) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_NO_START_DATE);
			}

			if (oldTask.getStartDate() != null && oldTask.getEndDate() != null && oldTask.getEndTime() == null) {
				newTask.setEndTime(endTime);
			}

			if (oldTask.getStartDate() != null && oldTask.getEndDate() != null && oldTask.getEndTime() != null 
					&& oldTask.getStartDate().isEqual(oldTask.getEndDate()) 
					&& getTaskParameters().getStartTime().compareTo(oldTask.getEndTime()) > 0) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_START_TIME);
			}
			newTask.setStartTime(getTaskParameters().getStartTime());
		}
	}

	/**
	 * This method is used to edit start date of the task.
	 * 
	 * @param oldTask This is the task before been edited.
	 * 
	 * @param newTask This is the task after been edited.
	 * 
	 * @param startTime This is the start time at 12:00am.
	 */
	private void editTaskStartDateOnly(Task oldTask, Task newTask, LocalTime startTime) {
		if (getTaskParameters().getStartDate() != null && getTaskParameters().getStartTime() == null 
				&& getTaskParameters().getEndDate() == null && getTaskParameters().getEndTime() == null) {
			if (oldTask.getEndDate() != null && 
					getTaskParameters().getStartDate().compareTo(oldTask.getEndDate()) > 0) {
				_invalidCommand = new InvalidCommand(ERROR_MESSAGE_INVALID_START_DATE);
			}
			if ((oldTask.getStartDate() == null && oldTask.getStartTime() == null 
					&& oldTask.getEndDate() != null && oldTask.getEndTime() != null) 
					|| (oldTask.getStartDate() != null && oldTask.getStartTime() != null 
					&& oldTask.getEndDate() != null && oldTask.getEndTime() != null 
					&& oldTask.getEndDate().isEqual(getTaskParameters().getStartDate())
					&& oldTask.getStartTime().isAfter(oldTask.getEndTime()))) {
				newTask.setStartTime(startTime);
			}
			newTask.setStartDate(getTaskParameters().getStartDate());
		}
	}

	/**
	 * This method is used to edit the tags list of the task.
	 * 
	 * @param newTask This is the task after been edited.
	 */
	private void editTaskTagsList(Task newTask) {
		if (getTaskParameters().getTagsList() != null) {
			newTask.setTagsList(getTaskParameters().getTagsList());
		}
	}

	/**
	 * This method is used to edit the description of the task.
	 * 
	 * @param newTask This is the task after been edited.
	 */
	private void editTaskDescription(Task newTask) {
		if (getTaskParameters().getDescription() != null) {
			newTask.setDescription(getTaskParameters().getDescription());
		}
	}

	/**
	 * This method is used to check whether taskParameters' end date is not null and taskParameters' end time is null.
	 * 
	 * @return true if taskParameters' end date is not null and taskParameters' end time is null or else return false.
	 */
	private boolean hasTaskParametersEndDateNotEqualsNullAndEndTimeEqualsNull() {
		return getTaskParameters().getEndDate() != null && getTaskParameters().getEndTime() == null;
	}

	/**
	 * This method is used to check whether taskParameters' start date is not null and taskParameters' start time is null.
	 * 
	 * @return true if taskParameters' start date is not null and taskParameters' start time is null or else return false.
	 */
	private boolean hasTaskParametersStartDateNotEqualsNullAndStartTimeEqualsNull() {
		return getTaskParameters().getStartDate() != null && getTaskParameters().getStartTime() == null;
	}

	/**
	 * This method is used to check whether taskParameters' end date is not null and taskParameters' end time is not null.
	 * 
	 * @return true if taskParameters' end date is not null and taskParameters' end time is not null or else return false.
	 */
	private boolean hasTaskParametersEndDateNotEqualsNullAndEndTimeNotEqualsNull() {
		return getTaskParameters().getEndDate() != null && getTaskParameters().getEndTime() != null;
	}

	/**
	 * This method is used to check whether taskParameters' start date is not null and taskParameters' start time is not null.
	 * 
	 * @return true if taskParameters' start date is not null and taskParameters' start time is not null or else return false.
	 */
	private boolean hasTaskParametersStartDateNotEqualsNullAndStartTimeNotEqualsNull() {
		return getTaskParameters().getStartDate() != null && getTaskParameters().getStartTime() != null;
	}

	/**
	 * This method is used to check whether taskParameters' start date is null and taskParameters' start time is null.
	 * 
	 * @return true if taskParameters' start date is null and taskParameters' start time is null or else return false.
	 */
	private boolean hasTaskParametersStartDateEqualsNullAndStartTimeEqualsNull() {
		return getTaskParameters().getStartDate() == null && getTaskParameters().getStartTime() == null;
	}

	/**
	 * This method is used to check whether taskParameters' end date is null and taskParameters' end time is null.
	 * 
	 * @return true if taskParameters' end date is null and taskParameters' end time is null or else return false.
	 */
	private boolean hasTaskParametersEndDateEqualsNullAndEndTimeEqualsNull() {
		return getTaskParameters().getEndDate() == null && getTaskParameters().getEndTime() == null;
	}

	/**
	 * This method is used to check whether taskParameters' end date is null and taskParameters' end time is not null.
	 * 
	 * @return true if taskParameters' end date is null and taskParameters' end time is not null or else return false.
	 */
	private boolean hasTaskParametersEndDateEqualsNullAndEndTimeNotEqualsNull() {
		return getTaskParameters().getEndDate() == null && getTaskParameters().getEndTime() != null;
	}

	/**
	 * This method is used to check whether taskParameters' start date is null and taskParameters' start time is not null.
	 * 
	 * @return true if taskParameters' start date is null and taskParameters' start time is not null or else return false.
	 */
	private boolean hasTaskParametersStartDateEqualsNullAndStartTimeNotEqualsNull() {
		return getTaskParameters().getStartDate() == null && getTaskParameters().getStartTime() != null;
	}
}
