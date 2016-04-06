package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import storage.Storage;

public class DoneCommand implements CommandInterface {

	private int _firstIndex;
	private int _lastIndex;
	private String _message;
	
	private static final String MESSAGE_DONE_COMMAND = "done \"%1$s\"";
	private static final String MESSAGE_MULTIPLE_DONE_COMMAND = "done multiple tasks";

	public DoneCommand(int index) {
		_firstIndex = index - 1;
		_lastIndex = index - 1;
		_message = "";
	}
	
	public DoneCommand(int firstIndex, int lastIndex) {
		_firstIndex = firstIndex - 1;
		_lastIndex = lastIndex - 1;
		_message = "";
	}

	public int getfirstIndex() {
		return _firstIndex;
	}

	public int getLastIndex() {
		return _lastIndex;
	}

	public CommandInterface execute() {
		return null;
	}
	
	public CommandInterface execute(ListsManager listsManager, Storage storage) {
		return null;
	}

	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((_firstIndex >= 0) && (_firstIndex < listsManager.getViewList().size()));
		assert((_lastIndex >= 0) && (_lastIndex < listsManager.getViewList().size()));
		listsManager.getIndexList().clear();
		int numOfDone = _lastIndex - _firstIndex + 1;
		Task oldTask = null;
		ArrayList<Task> doneTasksList = new ArrayList<Task>();
		for (int i = 0; i < numOfDone; i++) {
			oldTask = listsManager.getViewList().get(_firstIndex);
			listsManager.getMainList().remove(oldTask);
			Task newTask = new Task();
			initializeNewTask(oldTask, newTask);
			doneTasksList.add(newTask);
			updateListsManager(listsManager, newTask);
			updateHistoryManager(historyManager, oldTask, newTask, numOfDone);
		}
		for (int i = 0; i < doneTasksList.size(); i++) {
			Task doneTask = doneTasksList.get(i);
			listsManager.updateIndexList(doneTask);
		}
		if (numOfDone == 1) {
			_message = String.format(MESSAGE_DONE_COMMAND, oldTask.getDescription());
		} else {
			_message = MESSAGE_MULTIPLE_DONE_COMMAND;
		}
		return null;

	}

	private void updateHistoryManager(HistoryManager historyManager, Task oldTask, Task newTask, int numOfDone) {
		RollbackItem rollbackItem = new RollbackItem("done", oldTask, newTask, numOfDone);
		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
	}

	private void updateListsManager(ListsManager listsManager, Task newTask) {
		listsManager.getMainList().add(newTask);
		listsManager.updateLists();
	}

	private void initializeNewTask(Task oldTask, Task newTask) {
		newTask.setDescription(oldTask.getDescription());
		newTask.setTagsList(oldTask.getTagsList());
		newTask.setStartDate(oldTask.getStartDate());
		newTask.setStartTime(oldTask.getStartTime());
		newTask.setEndDate(oldTask.getEndDate());
		newTask.setEndTime(oldTask.getEndTime());
		newTask.setIsOverdue(oldTask.getIsOverdue());
		newTask.setIsDone(true);
	}
	
	public String getMessage() {
        return _message;
    }
	
	public void getMessage(String message) {
        _message = message;
    }
}
