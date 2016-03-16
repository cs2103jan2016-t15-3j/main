package logic.commands;

import java.util.ArrayList;
import java.util.Stack;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;

public class DeleteCommand extends Command {

	private int _index;

	public DeleteCommand(int index) {
		_index = index - 1;
	}

	public int getIndex() {
		return _index;
	}


	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {

		ArrayList<Task> viewList = new ArrayList<Task>();
		viewList.addAll(listsManager.getViewList());

		if (!isWithinList(viewList, _index)) {
			return false;
		}

		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		System.out.println(_index);
		Task oldTask = viewList.get(_index);
		mainList.remove(oldTask);

		if (listsManager.getViewType().equals("VIEW_SEARCH_RESULT")) {
			ArrayList<Task> searchResultList = new ArrayList<Task>();
			searchResultList.addAll(listsManager.getSearchResultList());
			searchResultList.remove(oldTask);
			listsManager.setSearchList(searchResultList);
		}
		listsManager.updateLists(mainList);

		RollbackItem rollbackItem = new RollbackItem("delete", oldTask, null);
		ArrayList<RollbackItem> undoList = new ArrayList<RollbackItem>();
		undoList.addAll(historyManager.getUndoList());

		undoList.add(rollbackItem);
		historyManager.setUndoList(undoList);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
		System.out.println("undoList size: " + undoList.size());
		return true;
	}

	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
}
