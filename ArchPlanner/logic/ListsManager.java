package logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;

public class ListsManager {

	private ArrayList<Task> _mainList;
	private ArrayList<Task> _viewList;

	private ArrayList<Task> _deadlineList;
	private ArrayList<Task> _eventList;
	private ArrayList<Task> _floatingList;
	private ArrayList<Task> _doneList;
	private ArrayList<Task> _undoneList;
	private ArrayList<Task> _overdueList;

	private ArrayList<Tag> _tagsList;
	private ArrayList<String> _selectedTagsList;
	private VIEW_TYPE _viewType;
	private CATEGORY_TYPE _categoryType;
	private String _currentViewType;	
	private int _index;

	public ListsManager() {
		_mainList = new ArrayList<Task>();
		_viewList = new ArrayList<Task>();
		_deadlineList = new ArrayList<Task>();
		_eventList = new ArrayList<Task>();
		_floatingList = new ArrayList<Task>();
		_doneList = new ArrayList<Task>();
		_undoneList = new ArrayList<Task>();
		_overdueList = new ArrayList<Task>();

		_tagsList = new ArrayList<Tag>();
		_selectedTagsList = new ArrayList<String>();
		_viewType = VIEW_TYPE.VIEW_ALL;
		_categoryType = CATEGORY_TYPE.CATEGORY_ALL;
		_currentViewType = "";
		_index = 0;
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
		sortMainList();
		populateAllLists();
		setSelectedTag();
		updateViewList();
	}

	public void updateSelectedTagsList(String tagName, boolean isSelected) {
		if (!_selectedTagsList.contains(tagName) && isSelected) {
			_selectedTagsList.add(tagName);
		} else if(_selectedTagsList.contains(tagName) && !isSelected) {
			_selectedTagsList.remove(tagName);
		}
	}
	
	public void setIndex(Task task) {
		for (int i = _viewList.size() - 1; i >= 0; i--) {
			if (_viewList.get(i).equals(task)) {
				_index = i;
			}
		}
	}

	public void setViewType(VIEW_TYPE viewType) {
		_viewType = viewType;
	}

	public void setCategoryType(CATEGORY_TYPE categoryType) {
		_categoryType = categoryType;
	}

	public void updateViewList() {
		boolean hasNoTagSlected = true;
		ArrayList<Task> list = new ArrayList<Task>();
		_viewList.clear();
		updateViewListWithViewType(list);
		updateViewListWithCategoryType(list);
		hasNoTagSlected = hasNoTagSelected(hasNoTagSlected);
		updateViewListWithSelectedTags(list, hasNoTagSlected);
		setViewList(list);
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
	
	public ArrayList<Task> getMainList() {
		return _mainList;
	}

	public ArrayList<Task> getViewList() {
		return _viewList;
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
	
	public String getCurrentViewType() {
		return _currentViewType;
	}
	
	public VIEW_TYPE getViewType() {
		return _viewType;
	}

	public CATEGORY_TYPE getCategoryType() {
		return _categoryType;
	}

	public ArrayList<String> getSelectedTagsList() {
		return _selectedTagsList;
	}
	
	public int getIndex() {
		return _index;
	}

	private void populateAllLists() {
		for (int i = 0; i < _mainList.size(); i++) {
			Task task = _mainList.get(i);

			populateCategoryLists(task);
			populateDoneList(task);
			populateUndoneList(task);
			populateOverdueList(task);
			populateTagsList(task);
		}
	}

	private void populateOverdueList(Task task) {
		if (task.getIsOverdue() == true) {
			_overdueList.add(task);
		}
	}

	private void populateUndoneList(Task task) {
		if (task.getIsDone() == false){
			_undoneList.add(task);
		}
	}

	private void populateDoneList(Task task) {
		if (task.getIsDone() == true) {
			_doneList.add(task);
		}
	}

	private void populateCategoryLists(Task task) {
		if ((task.getStartDate() == null) && (task.getEndDate() == null)) {
			_floatingList.add(task);
		} else if ((task.getStartDate() != null) && (task.getEndDate() == null) || 
				(task.getStartDate() != null) && (task.getEndDate() != null)) {
			_eventList.add(task);
		} else if ((task.getStartDate() == null) && (task.getEndDate() != null)) {
			_deadlineList.add(task);
		}
	}

	private void populateTagsList(Task task) {
		for (int i = 0; i < task.getTagsList().size(); i++) {
			String taskTagName = task.getTagsList().get(i);
			Tag tag = new Tag(taskTagName, false);
			boolean hasSameTag = false;
			for (int j = 0; j < _tagsList.size() && !hasSameTag; j++) {
				if (taskTagName.equals(_tagsList.get(j).getName())) {
					hasSameTag = true;
					System.out.println(tag.getName() + "\t" + tag.getIsSelected());
				}
			}
			if (!hasSameTag) {
				_tagsList.add(tag);
			}
		}
	}
	
	private void updateTaskOverdueStatus() {

		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();

		for (int i = 0; i < _mainList.size(); i++) {
			Task task = _mainList.get(i);
			task.setIsOverdue(false);
			if (((task.getStartDate() != null) && (task.getEndDate() != null)) || 
					((task.getStartDate() != null) && (task.getEndDate() == null))) {
				if (task.getStartTime() != null) {
					if (task.getStartDate().isBefore(currentDate) || 
							(task.getStartDate().equals(currentDate) 
									&& task.getStartTime().isBefore(currentTime))) {
						task.setIsOverdue(true);
					}
				} else if (task.getStartDate().isBefore(currentDate)) {
					task.setIsOverdue(true);
				}
			} else if ((task.getStartDate() == null) && (task.getEndDate() != null)) {
				if (task.getEndTime() != null) {
					if (task.getEndDate().isBefore(currentDate) || 
							(task.getEndDate().equals(currentDate) 
									&& task.getEndTime().isBefore(currentTime))) {
						task.setIsOverdue(true);
					}
				} else if (task.getEndDate().isBefore(currentDate)) {
					task.setIsOverdue(true);
				}
			}
		}
	}
	
	private void setSelectedTag() {
		SortMechanism sort = new SortMechanism();
		for (int j = 0; j < _selectedTagsList.size(); j++) {
			_currentViewType += "\"" + _selectedTagsList.get(j) + "\" ";
			boolean hasSameTag = false;
			for (int k = 0; k < _tagsList.size() && !hasSameTag; k++) {
				if (_selectedTagsList.get(j).equals(_tagsList.get(k).getName())) {
					_tagsList.get(k).setIsSelected(true);
					hasSameTag = true;
				}
			}
		}
		sort.sortTagsList(_tagsList);
	}
	
	private void sortMainList() {
		SortMechanism sort = new SortMechanism();
		sort.sortListByDescription(_mainList);
		sort.sortListByDateTime(_mainList);
		sort.sortListByOverdue(_mainList);
		sort.sortListByDone(_mainList);
	}
	
	private void clearAllLists() {
		_tagsList.clear();
		_viewList.clear();
		_deadlineList.clear();
		_eventList.clear();
		_floatingList.clear();
		_doneList.clear();
		_undoneList.clear();
		_overdueList.clear();
		_currentViewType = "";
		_index = -1;
	}

	private boolean hasNoTagSelected(boolean hasNoTagSlected) {
		for (int i = 0; i < _tagsList.size() && hasNoTagSlected; i++) {
			if (_tagsList.get(i).getIsSelected()) {
				hasNoTagSlected = false;
			}
		}
		return hasNoTagSlected;
	}
	
	private void updateViewListWithSelectedTags(ArrayList<Task> list, boolean hasNoTagSlected) {
		if (!hasNoTagSlected) {
			for (int i = 0; i < list.size(); i++) {
				boolean hasSameTag = false;
				Task task = list.get(i);
				for (int j = 0; j < _tagsList.size() && !hasSameTag; j++) {
					String tagName = _tagsList.get(j).getName();
					if (task.getTagsList().contains(tagName) && _tagsList.get(j).getIsSelected()) {
						hasSameTag = true;
					}
				}
				if (hasSameTag == false) {
					list.remove(i);
					i--;
				}
			}
		}
	}

	private void updateViewListWithCategoryType(ArrayList<Task> list) {
		updateViewListWithCategoryTasks(list);
		updateViewListWithCategoryEvents(list);
		updateViewListWithCategoryDeadlines(list);
	}

	private void updateViewListWithCategoryDeadlines(ArrayList<Task> list) {
		if (_categoryType.equals(CATEGORY_TYPE.CATEGORY_DEADLINES)) {
			for (int i = 0; i < list.size(); i++) {
				Task task = list.get(i);
				if (!((task.getStartDate() == null) && (task.getEndDate() != null))) {
					list.remove(i);
					i--;
				}
			}
		}
	}

	private void updateViewListWithCategoryEvents(ArrayList<Task> list) {
		if (_categoryType.equals(CATEGORY_TYPE.CATEGORY_EVENTS)) {
			for (int i = 0; i < list.size(); i++) {
				Task task = list.get(i);
				if (!((task.getStartDate() != null) && (task.getEndDate() == null)) && 
						!((task.getStartDate() != null) && (task.getEndDate() != null))) {
					list.remove(i);
					i--;
				}
			}
		}
	}

	private void updateViewListWithCategoryTasks(ArrayList<Task> list) {
		if (_categoryType.equals(CATEGORY_TYPE.CATEGORY_TASKS)) {
			for (int i = 0; i < list.size(); i++) {
				Task task = list.get(i);
				if (!((task.getStartDate() == null) && (task.getEndDate() == null))) {
					list.remove(i);
					i--;
				}
			}
		}
	}

	private void updateViewListWithViewType(ArrayList<Task> list) {
		updateViewListWithViewAll(list);
		updateViewListWithViewDone(list);
		updateViewListWithViewUndone(list);
		updateViewListWithViewOverdue(list);
	}

	private void updateViewListWithViewOverdue(ArrayList<Task> list) {
		if (_viewType.equals(VIEW_TYPE.VIEW_OVERDUE)) {
			list.addAll(_overdueList);
		}
	}

	private void updateViewListWithViewUndone(ArrayList<Task> list) {
		if (_viewType.equals(VIEW_TYPE.VIEW_UNDONE)) {
			list.addAll(_undoneList);
		}
	}

	private void updateViewListWithViewDone(ArrayList<Task> list) {
		if (_viewType.equals(VIEW_TYPE.VIEW_DONE)) {
			list.addAll(_doneList);
		}
	}

	private void updateViewListWithViewAll(ArrayList<Task> list) {
		if (_viewType.equals(VIEW_TYPE.VIEW_ALL)) {
			list.addAll(_mainList);
		}
	}
}
