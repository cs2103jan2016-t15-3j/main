package logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;

/**
 * This class keeps track and manages the lists of tasks belonging to different category and the state of the view.
 * 
 * @@author A0140021J
 *
 */
public class ListsManager {
	
	//This is the list that will be accessed used to populate other list.
	private ArrayList<Task> _mainList;
	
	//This is the list that will be used to display to the user.
	private ArrayList<Task> _viewList;

	//These are the lists that will be populated from main list based on category type.
	private ArrayList<Task> _deadlineList;
	private ArrayList<Task> _eventList;
	private ArrayList<Task> _floatingList;
	
	//These are the lists that will be populated from main list based on view type.
	private ArrayList<Task> _doneList;
	private ArrayList<Task> _undoneList;
	private ArrayList<Task> _overdueList;
	
	//This list of indexes that point to the tasks that are changed on added to the viewing list.
	private ArrayList<Integer> _indexList;
	
	//This list is the contains all the tags of all the tasks in the main list.
	private ArrayList<Tag> _tagsList;
	
	//This list is the contains all the tags that were selected.
	private ArrayList<String> _selectedTagsList;

	//This variable specifies the state of the view.
	private VIEW_TYPE _viewType;
	
	//This variable specifies the the category that the user is viewing.
	private CATEGORY_TYPE _categoryType;
	
	//This variable is the header to be displayed to user which specifies the current view state.
	private String _currentViewType;

	//This object variable is used to sort the list.
	private SortMechanism sort;
	
	//These constant string variables are used to append messages for readability.
	private final String STRING_DOUBLE_QUOTE = "\"";
	private final String STRING_WHITE_SPACE = " ";
	private final String STRING_EMPTY = "";

	//This is constructor of the class.
	public ListsManager() {
		_mainList = new ArrayList<Task>();
		_viewList = new ArrayList<Task>();
		_deadlineList = new ArrayList<Task>();
		_eventList = new ArrayList<Task>();
		_floatingList = new ArrayList<Task>();
		_doneList = new ArrayList<Task>();
		_undoneList = new ArrayList<Task>();
		_overdueList = new ArrayList<Task>();
		_indexList = new ArrayList<Integer>();

		_tagsList = new ArrayList<Tag>();
		_selectedTagsList = new ArrayList<String>();
		_viewType = VIEW_TYPE.ALL;
		_categoryType = CATEGORY_TYPE.ALL;
		_currentViewType = STRING_EMPTY;

		sort = new SortMechanism();
	}

	/**
	 * This is setter method for current view type.
	 * 
	 * @param currentViewType This is the current view type.
	 */
	public void setCurrentViewType(String currentViewType) {
		_currentViewType = currentViewType;
	}

	/**
	 * This is setter method for main list.
	 * 
	 * @param list This is the list that replaces current main list.
	 */
	public void setMainList(ArrayList<Task> list) {
		_mainList.clear();
		_mainList.addAll(list);
	}

	/**
	 * This is setter method for view list.
	 * 
	 * @param list This is the list that replaces current view list.
	 */
	public void setViewList(ArrayList<Task> list) {
		_viewList.clear();
		_viewList.addAll(list);
	}

	/**
	 * This is setter method for tags list.
	 * 
	 * @param list This is the list that replaces current tags list.
	 */
	public void setTagsList(ArrayList<Tag> list) {
		_tagsList.clear();
		_tagsList.addAll(list);
	}

	/**
	 * This is setter method for view type.
	 * 
	 * @param viewType This is the view type to replace the current view type.
	 */
	public void setViewType(VIEW_TYPE viewType) {
		_viewType = viewType;
	}

	/**
	 * This is setter method for category type.
	 * 
	 * @param categoryType This is the category type that replaces current category type.
	 */
	public void setCategoryType(CATEGORY_TYPE categoryType) {
		_categoryType = categoryType;
	}

	/**
	 * This is getter method for main list.
	 * 
	 * @return main list.
	 */
	public ArrayList<Task> getMainList() {
		return _mainList;
	}

	/**
	 * This is getter method for view list.
	 * 
	 * @return view list.
	 */
	public ArrayList<Task> getViewList() {
		return _viewList;
	}

	/**
	 * This is getter method for tags list.
	 * 
	 * @return tags list.
	 */
	public ArrayList<Tag> getTagsList() {
		return _tagsList;
	}

	/**
	 * This is getter method for _deadlineList arrayList.
	 * 
	 * @return deadline list.
	 */
	public ArrayList<Task> getDeadlineList() {
		return _deadlineList;
	}

	/**
	 * This is getter method for event list.
	 * 
	 * @return event list.
	 */
	public ArrayList<Task> getEventList() {
		return _eventList;
	}

	/**
	 * This is getter method for floating list.
	 * 
	 * @return floating list.
	 */
	public ArrayList<Task> getFloatingList() {
		return _floatingList;
	}

	/**
	 * This is getter method for overdue list.
	 * 
	 * @return overdue list.
	 */
	public ArrayList<Task> getOverdueList() {
		return _overdueList;
	}

	/**
	 * This is getter method for done list.
	 * 
	 * @return done list.
	 */
	public ArrayList<Task> getDoneList() {
		return _doneList;
	}

	/**
	 * This is getter method for undone list.
	 * 
	 * @return undone list.
	 */
	public ArrayList<Task> getUndoneList() {
		return _undoneList;
	}

	/**
	 * This is getter method for current view type.
	 * 
	 * @return current view type.
	 */
	public String getCurrentViewType() {
		return _currentViewType;
	}

	/**
	 * This is getter method for view type.
	 * 
	 * @return view type.
	 */
	public VIEW_TYPE getViewType() {
		return _viewType;
	}

	/**
	 * This is getter method for category type.
	 * 
	 * @return category type.
	 */
	public CATEGORY_TYPE getCategoryType() {
		return _categoryType;
	}

	/**
	 * This is getter method for selected tags list.
	 * 
	 * @return selected tags list.
	 */
	public ArrayList<String> getSelectedTagsList() {
		return _selectedTagsList;
	}

	/**
	 * This is getter method for index list.
	 * 
	 * @return index list.
	 */
	public ArrayList<Integer> getIndexList() {
		return _indexList;
	}

	/**
	 * This method is used to set up all the lists when the system just starts up.
	 * 
	 * @param list This is the list used to set up main list.
	 */
	public void setUpLists(ArrayList<Task> list) {
		if (list == null) {
			return;
		}
		setMainList(list);
		updateLists();
	}

	/**
	 * The method is used to update all the lists when user add or modify tasks after executing a successful command.
	 */
	public void updateLists() {
		clearAllLists();
		updateTaskIsOverdueStatus(getMainList());
		sortMainList();
		populateAllLists();
		setSelectedTag();
		updateViewList();
	}

	/**
	 * This method is used to add or remove the tag name in selected tags list 
	 * when a user selected or unselected a tag button displayed on UI, respectively.
	 * 
	 * @param tagName This is the name of the tag.
	 * 
	 * @param isSelected This is the status of whether the tag is selected.
	 */
	public void updateSelectedTagsList(String tagName, boolean isSelected) {
		if (!getSelectedTagsList().contains(tagName) && isSelected) {
			getSelectedTagsList().add(tagName);
		} else if(getSelectedTagsList().contains(tagName) && !isSelected) {
			getSelectedTagsList().remove(tagName);
		}
	}

	/**
	 * This method add the index of the task that was modified or added to the  index list
	 * follow by sorting it in ascending order.
	 * 
	 * @param task This is the task that is added or modified to the list.
	 */
	public void updateIndexList(Task task) {
		for (int i = getViewList().size() - 1; i >= 0; i--) {
			if (getViewList().get(i).equals(task)) {
				getIndexList().add(i);
				Collections.sort(getIndexList());
				return;
			}
		}
	}

	/**
	 * This method is used to update the view list based on view type, category type and the selected tags.
	 */
	public void updateViewList() {
		boolean hasTagSelected = false;
		ArrayList<Task> list = new ArrayList<Task>();
		getViewList().clear();
		updateViewListWithViewType(list);
		updateViewListWithCategoryType(list);
		hasTagSelected = hasTagSelected(hasTagSelected);
		updateViewListWithSelectedTags(list, hasTagSelected);
		setViewList(list);
	}

	/**
	 * This method is used to refresh the view list and update the list after checking whether the tasks are overdue.
	 */
	public void refreshViewList() {
		updateTaskIsOverdueStatus(getViewList());
	}
	
	/**
	 * This method is used to update view list based on selected category type.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateViewListWithCategoryType(ArrayList<Task> list) {
		switch (getCategoryType()) {
		case TASKS :
			updateViewListWithCategoryTasks(list);
			break;
		case EVENTS : 
			updateViewListWithCategoryEvents(list);
			break;
		case DEADLINES : 
			updateViewListWithCategoryDeadlines(list);
			break;
		default : 
			break;
		}
	}

	/**
	 * This method is used to update view list based on selected view type.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateViewListWithViewType(ArrayList<Task> list) {
		switch (getViewType()) {
		case ALL : 
			updateViewListWithViewAll(list);
			break;
		case DONE :
			updateViewListWithViewDone(list);
			break;
		case UNDONE : 
			updateViewListWithViewUndone(list);
			break;
		case OVERDUE : 
			updateViewListWithViewOverdue(list);
			break;
		}
	}

	/**
	 * This method is used to clear all the lists.
	 */
	private void clearAllLists() {
		_tagsList.clear();
		_viewList.clear();
		_deadlineList.clear();
		_eventList.clear();
		_floatingList.clear();
		_doneList.clear();
		_undoneList.clear();
		_overdueList.clear();
		_currentViewType = STRING_EMPTY;
	}

	/**
	 * This method is used to populate all the lists based the category type and view type.
	 */
	private void populateAllLists() {
		for (int i = 0; i < getMainList().size(); i++) {
			Task task = getMainList().get(i);

			populateCategoryLists(task);
			populateDoneList(task);
			populateUndoneList(task);
			populateOverdueList(task);
			populateTagsList(task);
		}
	}
	
	/**
	 * This method is used to populate overdue list with tasks from main list that are overdue.
	 * 
	 * @param task This is the task that will be checked whether it is overdue.
	 */
	private void populateOverdueList(Task task) {
		if ((task.getIsOverdue() == true) && (!task.getIsDone())) {
			getOverdueList().add(task);
		}
	}

	/**
	 * This method is used to populate undone list with tasks from main list that are undone.
	 * 
	 * @param task This is the task that will be checked whether it is undone.
	 */
	private void populateUndoneList(Task task) {
		if (task.getIsDone() == false){
			getUndoneList().add(task);
		}
	}

	/**
	 * This method is used to populate done list with tasks from main list that are done.
	 * 
	 * @param task This is the task that will be checked whether it is done.
	 */
	private void populateDoneList(Task task) {
		if (task.getIsDone() == true) {
			getDoneList().add(task);
		}
	}

	/**
	 * This method is used to populate category lists based on category type.
	 * 
	 * @param task This is the task that will be classified based on category type.
	 */
	private void populateCategoryLists(Task task) {
		if (hasNoStartDateAndNoEndDate(task)) {
			getFloatingList().add(task);
		} else if (hasStartDateAndNoEndDate(task) || hasStartDateAndEndDate(task)) {
			getEventList().add(task);
		} else if (hasEndDateAndNoStartDate(task)) {
			getDeadlineList().add(task);
		}
	}

	/**
	 * This method is used to populate tags list from checking the tasks in the main list.
	 * 
	 * @param task This is the task that will be checked whether it has same tag name in tags list.
	 */
	private void populateTagsList(Task task) {
		for (int i = 0; i < task.getTagsList().size(); i++) {
			String taskTagName = task.getTagsList().get(i);
			Tag tag = new Tag(taskTagName, false);
			boolean hasSameTag = false;
			updateTagsList(taskTagName, tag, hasSameTag);
		}
	}

	/**
	 * This method is used to check if tags list contains the name of the tag of a task
	 * and update the update the tags list.
	 * 
	 * @param taskTagName This is the task's tag name to be checked.
	 * 
	 * @param tag This is the task' tag with isSelected set to false.
	 * 
	 * @param hasSameTag This is the variable to check if the task's tag name contains in tags list.
	 */
	private void updateTagsList(String taskTagName, Tag tag, boolean hasSameTag) {
		for (int j = 0; j < getTagsList().size() && !hasSameTag; j++) {
			String tagsListTagName = getTagsList().get(j).getName();
			if (taskTagName.equals(tagsListTagName)) {
				hasSameTag = true;
			}
		}
		addTagIfNotFoundInTagsList(tag, hasSameTag);
	}
	
	/**
	 * This method is used to add the tag to the tags list if not found in tags list.
	 * 
	 * @param tag This is the task' tag with isSelected set to false.
	 * 
	 * @param hasSameTag This is the variable to check if the task's tag name contains in tags list.
	 */
	private void addTagIfNotFoundInTagsList(Tag tag, boolean hasSameTag) {
		if (!hasSameTag) {
			getTagsList().add(tag);
		}
	}

	/**
	 * This method is used to check and update the list after checking for tasks that are overdue.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateTaskIsOverdueStatus(ArrayList<Task> list) {

		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();

		for (int i = 0; i < list.size(); i++) {
			Task task = list.get(i);
			task.setIsOverdue(false);
			setTaskIsOverdueIfBeforeCurrentTime(currentDate, currentTime, task);
		}
	}

	/**
	 * This method is used to set task to overdue if the allocated date and time is before current date and time.
	 * 
	 * @param currentDate This is the current date.
	 * 
	 * @param currentTime This is the current time.
	 * 
	 * @param task This is the task to be checked if it is overdue.
	 */
	private void setTaskIsOverdueIfBeforeCurrentTime(LocalDate currentDate, LocalTime currentTime, Task task) {
		if (hasStartDateAndEndDate(task) || hasStartDateAndNoEndDate(task)) {
			setTaskIsOverdueIfStartIsBeforeCurrentDateTime(currentDate, currentTime, task);
		} else if (hasEndDateAndNoStartDate(task)) {
			setTaskIsOverdueIfEndIsBeforeCurrentDateTime(currentDate, currentTime, task);
		}
	}

	/**
	 * This method is used to set task to overdue if the end is before current.
	 * 
	 * @param currentDate This is the current date.
	 * 
	 * @param currentTime This is the current time.
	 * 
	 * @param task This is the task to be checked if it is overdue.
	 */
	private void setTaskIsOverdueIfEndIsBeforeCurrentDateTime(LocalDate currentDate, LocalTime currentTime, Task task) {
		if (task.getEndTime() != null) {
			setTaskIsOverdueIfEndDateTimeBeforeCurrentDateTime(currentDate, currentTime, task);
		} else {
			setTaskIsOverdueIfEndDateBeforeCurrentDate(currentDate, task);
		}
	}

	/**
	 * This method is used to set task to overdue if the end date is before current date.
	 * 
	 * @param currentDate This is the current date.
	 * 
	 * @param task This is the task to be checked if it is overdue.
	 */
	private void setTaskIsOverdueIfEndDateBeforeCurrentDate(LocalDate currentDate, Task task) {
		if (task.getEndDate().isBefore(currentDate)) {
			task.setIsOverdue(true);
		}
	}
	
	/**
	 * This method is used to set task to overdue if the end date and time is before current date and time.
	 * 
	 * @param currentDate This is the current date.
	 * 
	 * @param currentTime This is the current time.
	 * 
	 * @param task This is the task to be checked if it is overdue.
	 */
	private void setTaskIsOverdueIfEndDateTimeBeforeCurrentDateTime(LocalDate currentDate, LocalTime currentTime,
			Task task) {
		if (hasEndDateTimeBeforeCurrentDateTime(currentDate, currentTime, task)) {
			task.setIsOverdue(true);
		}
	}

	/**
	 * This method is used to check if task end date and time is before current date and time.
	 * 
	 * @param currentDate This is the current date.
	 * 
	 * @param currentTime This is the current time.
	 * 
	 * @param task This is the task to be checked if it is overdue.
	 * 
	 * @return true if task end date and time is before current date and time or else return false
	 */
	private boolean hasEndDateTimeBeforeCurrentDateTime(LocalDate currentDate, LocalTime currentTime, Task task) {
		return task.getEndDate().isBefore(currentDate) || (task.getEndDate().equals(currentDate) 
				&& task.getEndTime().isBefore(currentTime));
	}

	/**
	 * This method is used to set task to overdue if the start is before current.
	 * 
	 * @param currentDate This is the current date.
	 * 
	 * @param currentTime This is the current time.
	 * 
	 * @param task This is the task to be checked if it is overdue.
	 */
	private void setTaskIsOverdueIfStartIsBeforeCurrentDateTime(LocalDate currentDate, LocalTime currentTime, Task task) {
		if (task.getStartTime() != null) {
			setTaskIsOverdueStartDateTimeBeforeCurrentDateTime(currentDate, currentTime, task);
		} else {
			setTaskIsOverdueStartDateBeforeCurrentDate(currentDate, task);
		}
	}

	/**
	 * This method is used to set task to overdue if the start date is before current date
	 * 
	 * @param currentDate This is the current date.
	 * 
	 * @param task This is the task to be checked if it is overdue.
	 */
	private void setTaskIsOverdueStartDateBeforeCurrentDate(LocalDate currentDate, Task task) {
		if (task.getStartDate().isBefore(currentDate)) {
			task.setIsOverdue(true);
		}
	}

	/**
	 * This method is used to set task to overdue if the start date and time is before current date and time.
	 * 
	 * @param currentDate This is the current date.
	 * 
	 * @param currentTime This is the current time.
	 * 
	 * @param task This is the task to be checked if it is overdue.
	 */
	private void setTaskIsOverdueStartDateTimeBeforeCurrentDateTime(LocalDate currentDate, LocalTime currentTime,
			Task task) {
		if (hasStartDateTimeBeforeCurrentDateTime(currentDate, currentTime, task)) {
			task.setIsOverdue(true);
		}
	}

	/**
	 * This method is used to check if task start date and time is before current date and time.
	 * 
	 * @param currentDate This is the current date.
	 * 
	 * @param currentTime This is the current time.
	 * 
	 * @param task This is the task to be checked if it is overdue.
	 * 
	 * @return true if task start date and time is before current date and time or else return false
	 */
	private boolean hasStartDateTimeBeforeCurrentDateTime(LocalDate currentDate, LocalTime currentTime, Task task) {
		return task.getStartDate().isBefore(currentDate) || (task.getStartDate().equals(currentDate) 
				&& task.getStartTime().isBefore(currentTime));
	}

	/**
	 * This method  is used to check if task has start date and no end date.
	 * 
	 * @param task This is the task that is checked for start and end date.
	 * 
	 * @return true if task has start date and no end date or else return false.
	 */
	private boolean hasStartDateAndNoEndDate(Task task) {
		return task.getStartDate() != null && task.getEndDate() == null;
	}

	/**
	 * This method  is used to check if task has start date and end date.
	 * 
	 * @param task This is the task that is checked for start and end date.
	 * 
	 * @return true if task has start date and end date or else return false.
	 */
	private boolean hasStartDateAndEndDate(Task task) {
		return task.getStartDate() != null && task.getEndDate() != null;
	}

	/**
	 * This method  is used to check if task has no start date and has end date.
	 * 
	 * @param task This is the task that is checked for start and end date.
	 * 
	 * @return true if task has no start date and has end date or else return false.
	 */
	private boolean hasEndDateAndNoStartDate(Task task) {
		return task.getStartDate() == null && task.getEndDate() != null;
	}

	/**
	 * This method  is used to check if task has no start date and no end date.
	 * 
	 * @param task This is the task that is checked for start and no end date.
	 * 
	 * @return true if task has no start date and no end date or else return false.
	 */
	private boolean hasNoStartDateAndNoEndDate(Task task) {
		return task.getStartDate() == null && task.getEndDate() == null;
	}

	/**
	 * This method is used to update all the tags in the tags list with selected tags list.
	 */
	private void setSelectedTag() {
		for (int i = 0; i < getSelectedTagsList().size(); i++) {
			_currentViewType += STRING_DOUBLE_QUOTE + getSelectedTagsList().get(i) + STRING_DOUBLE_QUOTE + STRING_WHITE_SPACE;
			boolean hasSameTag = false;
			String selectedTagName = getSelectedTagsList().get(i);
			setTagIsSelectedStatusIfExistsInSelectedTagsList(selectedTagName, hasSameTag);
		}
		sort.sortTagsList(_tagsList);
	}

	/**
	 * This method is used to set the tag to selected if the tag name contains in the _selectedTagsList.
	 * 
	 * @param selectedTagName This is the name of the selected tag.
	 * 
	 * @param hasSameTag This is the variable to check if the task's tag name contains in tags list.
	 */
	private void setTagIsSelectedStatusIfExistsInSelectedTagsList(String selectedTagName, boolean hasSameTag) {
		for (int j = 0; j < getTagsList().size() && !hasSameTag; j++) {
			Tag tag = getTagsList().get(j);
			if (selectedTagName.equals(tag.getName())) {
				tag.setIsSelected(true);
				hasSameTag = true;
			}
		}
	}

	/**
	 * This method is used to sort the _mainList in the order of description, date and time, overdue and done status.
	 */
	private void sortMainList() {
		sort.sortListByDescription(getMainList());
		sort.sortListByDateTime(getMainList());
		sort.sortListByOverdue(getMainList());
		sort.sortListByDone(getMainList());
	}

	/**
	 * This method is used to check if any of the tag is selected.
	 * 
	 * @param hasTagSelected This variable indicates if any of the tags is selected.
	 * 
	 * @return true if any of the tag is selected or else return false.
	 */
	private boolean hasTagSelected(boolean hasTagSelected) {
		for (int i = 0; i < getTagsList().size() && !hasTagSelected; i++) {
			Tag tag = getTagsList().get(i);
			hasTagSelected = hasTagSelected(hasTagSelected, tag);
		}
		return hasTagSelected;
	}

	/**
	 * This method is used check if the tag is selected.
	 * 
	 * @param hasTagSelected This variable indicates if any of the tags is selected.
	 * 
	 * @param tag This is the tag to be checked if it is selected
	 * 
	 * @return true if any of the tag is selected or else return false.
	 */
	private boolean hasTagSelected(boolean hasTagSelected, Tag tag) {
		if (tag.getIsSelected()) {
			hasTagSelected = true;
		}
		return hasTagSelected;
	}
	
	/**
	 * This method is used to update view list if at least one tag is selected.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 * 
	 * @param hasTagSelected This variable indicates whether any tag is selected.
	 */
	private void updateViewListWithSelectedTags(ArrayList<Task> list, boolean hasTagSelected) {
		if (!hasTagSelected) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			boolean hasSameTag = false;
			Task task = list.get(i);
			hasSameTag = hasSameTag(hasSameTag, task);
			i = removeTaskFromViewListIfNoSameTag(list, i, hasSameTag);
		}
	}

	/**
	 * This is used to remove task from view list if it does not contain any of the tags that are selected.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 * 
	 * @param i This is the index of the task to be checked whether it has the tag that is selected.
	 * 
	 * @param hasSameTag This variable indicates whether any tag is selected.
	 * 
	 * @return the index
	 */
	private int removeTaskFromViewListIfNoSameTag(ArrayList<Task> list, int i, boolean hasSameTag) {
		if (hasSameTag == false) {
			list.remove(i);
			i--;
		}
		return i;
	}

	/**
	 * This method checks if task has tag with the same name as the tag in the selected tags list.
	 * 
	 * @param hasSameTag This variable indicates whether any tag is selected.
	 * 
	 * @param task This is the task to be checked whether it has any tag that is selected.
	 * 
	 * @return true if task has tag with the same name as the tag in the _selectedTagsList or else return false.
	 */
	private boolean hasSameTag(boolean hasSameTag, Task task) {
		for (int j = 0; j < getTagsList().size() && !hasSameTag; j++) {
			Tag tag = getTagsList().get(j);
			if (task.getTagsList().contains(tag.getName()) && tag.getIsSelected()) {
				hasSameTag = true;
			}
		}
		return hasSameTag;
	}

	/**
	 * This method is used to update view list with tasks that are deadline task.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateViewListWithCategoryDeadlines(ArrayList<Task> list) {
		for (int i = 0; i < list.size(); i++) {
			Task task = list.get(i);
			i = removeTaskFromViewListIfIsNotCategoryDeadlines(list, i, task);
		}
	}

	/**
	 * This method is used to remove task from view list if it is not deadline task.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.

	 * @param i This is the index of the task to be checked whether it is deadline task.
	 * 
	 * @param task This is the task to be checked whether it is deadline task.
	 * 
	 * @return the index of the previous checked task.
	 */
	private int removeTaskFromViewListIfIsNotCategoryDeadlines(ArrayList<Task> list, int i, Task task) {
		if (!hasEndDateAndNoStartDate(task)) {
			list.remove(i);
			i--;
		}
		return i;
	}

	/**
	 * This method is used to update view list with tasks that are event task.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateViewListWithCategoryEvents(ArrayList<Task> list) {
		for (int i = 0; i < list.size(); i++) {
			Task task = list.get(i);
			i = removeTaskFromViewListIfIsNotCategoryEvents(list, i, task);
		}
	}

	/**
	 * This method is used to remove task from view list if it is not event task.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.

	 * @param i This is the index of the task to be checked whether it is event task.
	 * 
	 * @param task This is the task to be checked whether it is event task.
	 * 
	 * @return the index of the previous checked task.
	 */
	private int removeTaskFromViewListIfIsNotCategoryEvents(ArrayList<Task> list, int i, Task task) {
		if (!hasStartDateAndNoEndDate(task) && !hasStartDateAndEndDate(task)) {
			list.remove(i);
			i--;
		}
		return i;
	}

	/**
	 * This method is used to update view list with tasks that are floating task.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateViewListWithCategoryTasks(ArrayList<Task> list) {
		for (int i = 0; i < list.size(); i++) {
			Task task = list.get(i);
			i = removeTaskFromViewListIfIsNotCategoryTasks(list, i, task);
		}
	}

	/**
	 * This method is used to remove task from view list if it is not floating task.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.

	 * @param i This is the index of the task to be checked whether it is floating task.
	 * 
	 * @param task This is the task to be checked whether it is floating task.
	 * 
	 * @return the index of the previous checked task.
	 */
	private int removeTaskFromViewListIfIsNotCategoryTasks(ArrayList<Task> list, int i, Task task) {
		if (!hasNoStartDateAndNoEndDate(task)) {
			list.remove(i);
			i--;
		}
		return i;
	}

	/**
	 * This method is used to update view list with overdue tasks.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateViewListWithViewOverdue(ArrayList<Task> list) {
		list.addAll(getOverdueList());
	}
	
	/**
	 * This method is used to update view list with undone tasks.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateViewListWithViewUndone(ArrayList<Task> list) {
		list.addAll(getUndoneList());
	}

	/**
	 * This method is used to update view list with done tasks.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateViewListWithViewDone(ArrayList<Task> list) {
		list.addAll(getDoneList());
	}

	/**
	 * This method is used to update view list with all tasks.
	 * 
	 * @param list This is the list of tasks after meeting previous conditions.
	 */
	private void updateViewListWithViewAll(ArrayList<Task> list) {
		list.addAll(getMainList());
	}
}