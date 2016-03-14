package logic.commands;

import java.util.ArrayList;

import logic.ListsManager;
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
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager) {

		ArrayList<Task> viewList = new ArrayList<Task>();
		viewList.addAll(listsManager.getViewList());

		if (!isWithinList(viewList, _index)) {
			return false;
		}

		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		System.out.println(_index);
		Task task = viewList.get(_index);
		mainList.remove(task);

		if (listsManager.getViewType().equals("VIEW_SEARCH_RESULT")) {
			ArrayList<Task> searchResultList = new ArrayList<Task>();
			searchResultList.addAll(listsManager.getSearchResultList());
			searchResultList.remove(task);
			listsManager.setSearchList(searchResultList);
		}
		listsManager.updateLists(mainList);
		return true;
	}

	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
}
