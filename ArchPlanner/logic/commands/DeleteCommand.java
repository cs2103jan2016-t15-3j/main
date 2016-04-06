package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import storage.Storage;

public class DeleteCommand implements CommandInterface {

	private int _firstIndex;
	private int _lastIndex;
	private String _message;

	private static final String MESSAGE_DELETE_COMMAND = "deleted \"%1$s\"";
	private static final String MESSAGE_MULTIPLE_DELETE_COMMAND = "deleted multiple tasks";

	public DeleteCommand(int index) {
		_firstIndex = index - 1;
		_lastIndex = index - 1;
		_message = "";
	}

	public DeleteCommand(int firstIndex, int lastIndex) {
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
		Task oldTask = null;
		int numOfDeletion = _lastIndex - _firstIndex + 1;
		for (int i = 0; i < numOfDeletion; i++) {
			oldTask = listsManager.getViewList().get(_firstIndex);
			updateListsManager(listsManager, oldTask);
			updateHistoryManager(historyManager, oldTask, numOfDeletion);
		}
		if (numOfDeletion == 1) {
			_message = String.format(MESSAGE_DELETE_COMMAND, oldTask.getDescription());
		} else {
			_message = MESSAGE_MULTIPLE_DELETE_COMMAND;
		}
		return null;
	}

	private void updateHistoryManager(HistoryManager historyManager, Task oldTask, int numOfDeletion) {
		RollbackItem rollbackItem = new RollbackItem("delete", oldTask, null, numOfDeletion);
		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
	}

	private void updateListsManager(ListsManager listsManager, Task oldTask) {
		listsManager.getMainList().remove(oldTask);
		listsManager.updateLists();
	}

	public String getMessage() {
		return _message;
	}

	public void getMessage(String message) {
		_message = message;
	}
}
