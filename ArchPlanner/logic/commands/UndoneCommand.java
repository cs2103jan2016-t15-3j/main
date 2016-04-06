package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import storage.Storage;

public class UndoneCommand implements CommandInterface {

	private int _firstIndex;
	private int _lastIndex;
	private String _message;

	private static final String MESSAGE_UNDONE_COMMAND = "undone \"%1$s\"";
	private static final String MESSAGE_MULTIPLE_UNDONE_COMMAND = "undone multiple tasks";

	public UndoneCommand(int index) {
		_firstIndex = index - 1;
		_lastIndex = index - 1;
		_message = "";
	}

	public UndoneCommand(int firstIndex, int lastIndex) {
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
		int numOfUndone = _lastIndex - _firstIndex + 1;
		Task oldTask = null;
		ArrayList<Task> undoneTasksList = new ArrayList<Task>();
		for (int i = 0; i < numOfUndone; i++) {
			oldTask = listsManager.getViewList().get(_lastIndex);
			listsManager.getMainList().remove(oldTask);
			Task newTask = new Task();
			initializeNewTask(oldTask, newTask);
			undoneTasksList.add(newTask);
			updateListsManager(listsManager, newTask);
			updateHistoryManager(historyManager, oldTask, newTask, numOfUndone);
		}
		for (int i = 0; i < undoneTasksList.size(); i++) {
			Task undoneTask = undoneTasksList.get(i);
			listsManager.updateIndexList(undoneTask);
		}
		if (numOfUndone == 1) {
			_message = String.format(MESSAGE_UNDONE_COMMAND, oldTask.getDescription());
		} else {
			_message = MESSAGE_MULTIPLE_UNDONE_COMMAND;
		}
		return null;
	}

	private void updateHistoryManager(HistoryManager historyManager, Task oldTask, Task newTask, int numOfUndone) {
		RollbackItem rollbackItem = new RollbackItem("undone", oldTask, newTask, numOfUndone);
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
		newTask.setIsDone(false);
	}
	
	public String getMessage() {
        return _message;
    }
	
	public void getMessage(String message) {
        _message = message;
    }
}
