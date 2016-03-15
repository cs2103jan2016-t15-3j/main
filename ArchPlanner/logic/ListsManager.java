package logic;

import java.util.ArrayList;

public class ListsManager {

	private ArrayList<Task> _mainList = new ArrayList<Task>();
	private ArrayList<Task> _viewList = new ArrayList<Task>();
	private ArrayList<Task> _searchResultList = new ArrayList<Task>();
	private ArrayList<String> _tagsList = new ArrayList<String>();

	private ArrayList<Task> _deadlineList = new ArrayList<Task>();
	private ArrayList<Task> _eventList = new ArrayList<Task>();
	private ArrayList<Task> _tasksList = new ArrayList<Task>();
	private ArrayList<Task> _doneList = new ArrayList<Task>();
	private ArrayList<Task> _undoneList = new ArrayList<Task>();

	private String _viewType = "VIEW_ALL";

	public void updateLists(ArrayList<Task> list) {

		clearAllLists();
		_mainList.addAll(list);

		SortMechanism sort = new SortMechanism();
		sort.sortListByDescription(_mainList);
		_mainList = sort.sortListByDateTime(_mainList);

		for (int i = 0; i < _mainList.size(); i++) {
			Task task = _mainList.get(i);
			//System.out.println("update: " + task.getDescription());
			if ((task.getStartDateTime() == null) && (task.getEndDateTime() == null)) {
				_tasksList.add(task);
			} else if ((task.getStartDateTime() != null) && (task.getEndDateTime() == null) || 
					(task.getStartDateTime() == null) && (task.getEndDateTime() != null)) {
				_eventList.add(task);
			} else if ((task.getStartDateTime() != null) && (task.getEndDateTime() != null)) {
				_deadlineList.add(task);
			}

			if (task.getIsDone() == true) {
				_doneList.add(task);
			} else {
				_undoneList.add(task);
			}

			for (int j = 0; j < task.getTagsList().size() ; j++) {
				String tag = task.getTagsList().get(j);
				if ((tag != null) && (!_tagsList.contains(tag))) {
					_tagsList.add(tag);
				}
			}
		}
		/*
		for(int i = 0; i < _undoneList.size(); i++)
			System.out.println("main" + i + "\t" + _mainList.get(i).getDescription());

		for(int i = 0; i < _undoneList.size(); i++)
			System.out.println("undone" + i + "\t" + _undoneList.get(i).getDescription());
		 */
		sort.sortTagsList(_tagsList);
		setViewList(_viewType);

		_tagsList.add(0, "Deadlines");
		_tagsList.add(1 ,"Events");
		_tagsList.add(2, "Tasks");
	}

	private void clearAllLists() {
		_mainList.clear();
		_viewList.clear();
		_tagsList.clear();
		_deadlineList.clear();
		_eventList.clear();
		_tasksList.clear();
		_doneList.clear();
		_undoneList.clear();
	}

	public void setViewList(String viewType) {
		_viewType = viewType;
		_viewList.clear();

		System.out.println(_viewType);

		if (_viewType.equalsIgnoreCase("VIEW_ALL")) {
			_viewList.addAll(_undoneList);
		} else if (_viewType.equals("VIEW_DONE")) {
			_viewList.addAll(_doneList);
		} else if (_viewType.equals("VIEW_SEARCH_RESULT")) {
			_viewList.addAll(_searchResultList);
		}
	}

	public void setSearchList(ArrayList<Task> searchResultList) {
		_searchResultList.clear();
		_searchResultList.addAll(searchResultList);
	}

	public String getViewType() {
		return _viewType;
	}

	public ArrayList<Task> getMainList() {
		return _mainList;
	}

	public ArrayList<Task> getUndoneList() {
		return _undoneList;
	}

	public ArrayList<Task> getViewList() {
		return _viewList;
	}

	public ArrayList<String> getTagsList() {
		return _tagsList;
	}

	public ArrayList<Task> getSearchResultList() {
		return _searchResultList;
	}
}
