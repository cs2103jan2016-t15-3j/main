package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;

public class DeleteCommand implements Command {

	private int _index;

	public DeleteCommand(int index) {
		_index = index - 1;
	}

	public int getIndex() {
		return _index;
	}


	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {

		if (!isWithinList(listsManager.getViewList(), _index)) {
			return false;
		}

		//System.out.println(_index);
		Task oldTask = listsManager.getViewList().get(_index);
		listsManager.getMainList().remove(oldTask);

		if (listsManager.getViewType().equals("VIEW_SEARCH_RESULT")) {
			listsManager.getSearchResultList().remove(oldTask);
		}
		listsManager.updateLists();

		RollbackItem rollbackItem = new RollbackItem("delete", oldTask, null);

		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
		System.out.println("undoList size: " + historyManager.getUndoList().size());
		return true;
	}

	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
}
