package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;

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

	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((_firstIndex >= 0) && (_firstIndex < listsManager.getViewList().size()));
		assert((_lastIndex >= 0) && (_lastIndex < listsManager.getViewList().size()));
		/*
		if (!isWithinList(listsManager.getViewList(), _index)) {
			return false;
		}
		 */
		int numOfDeletion = _lastIndex - _firstIndex + 1;
		for (int i = 0; i < numOfDeletion; i++) {
			Task oldTask = listsManager.getViewList().get(_firstIndex);
			listsManager.getMainList().remove(oldTask);

			listsManager.getViewList().remove(oldTask);
			listsManager.updateLists();

			RollbackItem rollbackItem = new RollbackItem("delete", oldTask, null);

			historyManager.getUndoList().add(rollbackItem);
			historyManager.setRedoList(new ArrayList<RollbackItem>());
		}
		System.out.println("undoList size: " + historyManager.getUndoList().size());
		return null;
	}
	/*
	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
	 */
}
