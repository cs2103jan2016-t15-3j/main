package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import storage.Storage;

public class DeleteCommand implements Command {

	private int _firstIndex;
	private int _lastIndex;

	public DeleteCommand(int index) {
		_firstIndex = index - 1;
		_lastIndex = index - 1;
	}

	public DeleteCommand(int firstIndex, int lastIndex) {
		_firstIndex = firstIndex - 1;
		_lastIndex = lastIndex - 1;
	}

	public int getfirstIndex() {
		return _firstIndex;
	}

	public int getLastIndex() {
		return _lastIndex;
	}

	public Command execute() {
		return null;
	}
	
	public Command execute(Storage storage) {
		return null;
	}
	

	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((_firstIndex >= 0) && (_firstIndex < listsManager.getViewList().size()));
		assert((_lastIndex >= 0) && (_lastIndex < listsManager.getViewList().size()));

		int numOfDeletion = _lastIndex - _firstIndex + 1;
		for (int i = 0; i < numOfDeletion; i++) {
			Task oldTask = listsManager.getViewList().get(_firstIndex);
			updateListsManager(listsManager, oldTask);
			updateHistoryManager(historyManager, oldTask, numOfDeletion);
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
}
