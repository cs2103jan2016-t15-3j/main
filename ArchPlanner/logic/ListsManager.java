package logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;

public class ListsManager {

	private ArrayList<Task> _mainList;
	private ArrayList<Task> _viewList;
	private ArrayList<Tag> _tagsList;

	private ArrayList<Task> _deadlineList;
	private ArrayList<Task> _eventList;
	private ArrayList<Task> _floatingList;
	private ArrayList<Task> _doneList;
	private ArrayList<Task> _undoneList;
	private ArrayList<Task> _overdueList;

	private VIEW_TYPE _viewType;
	private CATEGORY_TYPE _categoryType;

	private String _currentViewType;

	public ListsManager() {
		_mainList = new ArrayList<Task>();
		_viewList = new ArrayList<Task>();
		_tagsList = new ArrayList<Tag>();

		_deadlineList = new ArrayList<Task>();
		_eventList = new ArrayList<Task>();
		_floatingList = new ArrayList<Task>();
		_doneList = new ArrayList<Task>();
		_undoneList = new ArrayList<Task>();
		_overdueList = new ArrayList<Task>();
		_viewType = VIEW_TYPE.VIEW_ALL;
		_categoryType = CATEGORY_TYPE.CATEGORY_ALL;
		_currentViewType = "";
	}

	public void setUpLists(ArrayList<Task> list) {
		if (list == null) {
			return;
		}
		_mainList.addAll(list);
		updateLists();
	}

	public void updateLists() {
		clearAllLists();
		updateTaskOverdueStatus();
		SortMechanism sort = new SortMechanism();
		sort.sortListByDescription(_mainList);
		//sort.sortListByDateTime(_mainList);
		sort.sortListByOverdue(_mainList);
		sort.sortListByDone(_mainList);
		for (int i = 0; i < _mainList.size(); i++) {
			Task task = _mainList.get(i);
			if ((task.getStartDate() == null) && (task.getEndDate() == null)) {
				_floatingList.add(task);
			} else if ((task.getStartDate() != null) && (task.getEndDate() == null) || 
					(task.getStartDate() != null) && (task.getEndDate() != null)) {
				_eventList.add(task);
			} else if ((task.getStartDate() == null) && (task.getEndDate() != null)) {
				_deadlineList.add(task);
			}
			if (task.getIsDone() == true) {
				_doneList.add(task);
			} else {
				_undoneList.add(task);
			}
			if (task.getIsOverdue() == true) {
				_overdueList.add(task);
			}

			for (int j = 0; j < task.getTagsList().size(); j++) {
				String taskTagName = task.getTagsList().get(j);
				Tag tag = new Tag(taskTagName, false);
				boolean hasSameTag = false;
				for (int k = 0; k < _tagsList.size() && !hasSameTag; k++) {
					if (taskTagName.equals(_tagsList.get(k).getName())) {
						hasSameTag = true;
						System.out.println(tag.getName() + "\t" + tag.getIsSelected());
					}
				}
				if (!hasSameTag) {
					_tagsList.add(tag);
				}
			}
			//System.out.println("test");
		}
		//System.out.println("testpassed");
		sort.sortTagsList(_tagsList);
		updateViewList();
		System.out.println(_categoryType);
	}

	public void updateTaskOverdueStatus() {

		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();

		for (int i = 0; i < _mainList.size(); i++) {
			Task task = _mainList.get(i);
			task.setIsOverdue(false);
			if (((task.getStartDate() != null) && (task.getEndDate() != null)) || 
					((task.getStartDate() != null) && (task.getEndDate() == null))) {
				if (task.getStartTime() != null) {
					if (task.getStartDate().isBefore(currentDate) || 
							(task.getStartDate().equals(currentDate) && task.getStartTime().isBefore(currentTime))) {
						task.setIsOverdue(true);
					}
				} else if (task.getStartDate().isBefore(currentDate)) {
					task.setIsOverdue(true);
				}
			} else if ((task.getStartDate() == null) && (task.getEndDate() != null)) {
				if (task.getEndTime() != null) {
					if (task.getEndDate().isBefore(currentDate) || 
							(task.getEndDate().equals(currentDate) && task.getEndTime().isBefore(currentTime))) {
						task.setIsOverdue(true);
					}
				} else if (task.getEndDate().isBefore(currentDate)) {
					task.setIsOverdue(true);
				}
			}
		}
	}

	private void clearAllLists() {
		_viewList.clear();
		_tagsList.clear();
		_deadlineList.clear();
		_eventList.clear();
		_floatingList.clear();
		_doneList.clear();
		_undoneList.clear();
		_overdueList.clear();
	}

	public void setViewType(VIEW_TYPE viewType) {
		_viewType = viewType;
	}

	public void setCategoryType(CATEGORY_TYPE categoryType) {
		_categoryType = categoryType;
	}

	public VIEW_TYPE getViewType() {
		return _viewType;
	}

	public CATEGORY_TYPE getCategoryType() {
		return _categoryType;
	}

	public void updateViewList() {

		ArrayList<Task> list = new ArrayList<Task>();
		_viewList.clear();
		if (_viewType.equals(VIEW_TYPE.VIEW_ALL)) {
			list.addAll(_mainList);
		} else if (_viewType.equals(VIEW_TYPE.VIEW_DONE)) {
			list.addAll(_doneList);
		} else if (_viewType.equals(VIEW_TYPE.VIEW_UNDONE)) {
			list.addAll(_undoneList);
		} else if (_viewType.equals(VIEW_TYPE.VIEW_OVERDUE)) {
			list.addAll(_overdueList);
		}

		if (_categoryType.equals(CATEGORY_TYPE.CATEGORY_TASK)) {
			for (int i = 0; i < list.size(); i++) {
				Task task = list.get(i);
				if (!((task.getStartDate() == null) && (task.getEndDate() == null))) {
					list.remove(i);
					i--;
				}
			}
		} else if (_categoryType.equals(CATEGORY_TYPE.CATEGORY_EVENT)) {
			for (int i = 0; i < list.size(); i++) {
				Task task = list.get(i);
				if (!((task.getStartDate() != null) && (task.getEndDate() == null)) && 
						!((task.getStartDate() != null) && (task.getEndDate() != null))) {
					list.remove(i);
					i--;
				}
			}
		} else if (_categoryType.equals(CATEGORY_TYPE.CATEGORY_DEADLINE)) {
			for (int i = 0; i < list.size(); i++) {
				Task task = list.get(i);
				if (!((task.getStartDate() == null) && (task.getEndDate() != null))) {
					list.remove(i);
					i--;
				}
			}
		}
		System.out.println("start");

		boolean isShowAll = true;

		for (int i = 0; i < _tagsList.size() && isShowAll; i++) {
			if (_tagsList.get(i).getIsSelected()) {
				isShowAll = false;
			}
		}

		if (!isShowAll) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println("test21");
				boolean hasSameTag = true;
				Task task = list.get(i);
				System.out.println("test22");
				for (int j = 0; j < _tagsList.size() && hasSameTag; j++) {
					String tagName = _tagsList.get(j).getName();
					System.out.println("test24");
					if (!(task.getTagsList().contains(tagName) && _tagsList.get(j).getIsSelected())) {
						list.remove(i);
						System.out.println(task.getDescription());
						hasSameTag = false;
						i--;
					}
					System.out.println("test2");
				}
				System.out.println("test2passed");
			}
		}
		setViewList(list);
	}

	public ArrayList<Task> getMainList() {
		return _mainList;
	}

	public ArrayList<Task> getViewList() {
		return _viewList;
	}

	public String getCurrentViewType() {
		return _currentViewType;
	}

	public void setCurrentViewType(String currentViewType) {
		_currentViewType = currentViewType;
	}

	public void setViewList(ArrayList<Task> list) {
		_viewList.clear();
		_viewList.addAll(list);
	}

	public void setTagsList(ArrayList<Tag> list) {
		_tagsList.clear();
		_tagsList.addAll(list);
	}

	public ArrayList<Tag> getTagsList() {
		return _tagsList;
	}

	public ArrayList<Task> getDeadlineList() {
		return _deadlineList;
	}

	public ArrayList<Task> getEventList() {
		return _eventList;
	}

	public ArrayList<Task> getFloatingList() {
		return _floatingList;
	}
}
