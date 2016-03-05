package logic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;;

//import application.Ui;

/**
 * This class interact with the UI and process the operation, 
 * followed by updating the changes to storage
 * 
 * @author riyang
 *
 */
public class Logic {

	private ArrayList<Task> _mainList = new ArrayList<Task>();
	private ArrayList<Task> _viewList = new ArrayList<Task>();
	private ArrayList<String> _tagsList = new ArrayList<String>();
	
	Storage storage = new Storage();
	
	public Logic() {
	    storage.loadStorageFile();
        _mainList = storage.getTasksList();
        setViewList(_mainList);
	}

	
	private ArrayList<Task> updateViewFloatingTasksList() {
		ArrayList<Task> floatingTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartDateTime(_mainList, i) == null) && (getTaskEndDateTime(_mainList, i) == null)) {
				floatingTasksList.add(_mainList.get(i));
			}
		}
		return floatingTasksList;
	}

	private ArrayList<Task> updateViewDeadlineTasksList() {
		ArrayList<Task> deadlineTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartDateTime(_mainList, i) == null) && (getTaskEndDateTime(_mainList, i) != null)) {
				deadlineTasksList.add(_mainList.get(i));
			}
		}
		deadlineTasksList = getSortedListWithEndDateTime(deadlineTasksList);
		deadlineTasksList = getSortedListWithDescription(deadlineTasksList);
		return deadlineTasksList;
	}

	private ArrayList<Task> updateViewTagTasksList(String tag) {
		ArrayList<Task> tagTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if (getTaskTag(_mainList, i).equals(tag)) {
				tagTasksList.add(_mainList.get(i));
			}
		}
		return tagTasksList;
	}

	private ArrayList<Task> updateViewTimeLineTasksList() {
		ArrayList<Task> timeLineTasksList= new ArrayList<>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartDateTime(_mainList, i) != null) && (getTaskEndDateTime(_mainList, i) != null)) {
				timeLineTasksList.add(_mainList.get(i));
			}
		}
		timeLineTasksList = getSortedListWithStartDateTime(timeLineTasksList);
		timeLineTasksList = getSortedListWithEndDateTime(timeLineTasksList);
		timeLineTasksList = getSortedListWithDescription(timeLineTasksList);
		return timeLineTasksList;
	}

	private void saveTask() {
		storage.writeStorageFile(_mainList);
	}

	public void exitProgram() {
		System.exit(0);
	}

	public void addTask(String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		Task task = new Task(description, tag, startDateTime, endDateTime);
		System.out.println(task.getDescription());
		_mainList.add(task);
		
		if (tag != null && !_tagsList.contains(tag)) {
			_tagsList.add(tag);
			//Ui.getInstance().updateTagDisplay(_tagsList);
		}
		setViewList(_mainList);
		//Ui.getInstance().updateTaskDisplay(_viewList);
		
		saveTask();
	}

	public void deleteTask(int id) throws IOException {
		int taskIndex= getTaskIndex(id);
		Task task = getTask(_viewList, taskIndex);
		_mainList.remove(task);

		for (int i = 0; i < _mainList.size(); i++) {
			String tag = getTaskTag(_mainList, i);
			if (!_tagsList.contains(tag)) {
				_tagsList.add(tag);
			}
		}
		Collections.sort(_tagsList);
		saveTask();
	}

	public void editTask(int id, String newDescription, String newTag, Calendar newStartDateTime, Calendar newEndDateTime) {
		int taskIndex = getTaskIndex(id);
		Task oldTask = getTask(_viewList, taskIndex);

		if (newDescription.equals(getTaskDescription(_viewList, taskIndex))) {
			setTaskDescription(_viewList, taskIndex, newDescription);
		}

		if (newStartDateTime.equals(getTaskStartDateTime(_viewList, taskIndex))) {
			setTaskStartDateTime(_viewList, taskIndex, newStartDateTime);
		}

		if (newEndDateTime.equals(getTaskEndDateTime(_viewList, taskIndex))) {
			setTaskEndDateTime(_viewList, taskIndex, newEndDateTime);
		}

		if (newTag.equals(getTaskTag(_viewList, taskIndex))) {
			setTaskTag(_viewList, taskIndex, newTag);
		}
		_mainList.remove(oldTask);
		_mainList.add(getTask(_viewList, taskIndex));
		saveTask();
	}

	public void done(int id) {
		int taskIndex = getTaskIndex(id);
		Task task = getTask(_viewList, taskIndex);

		setTaskIsDone(_viewList, taskIndex, true);
		_mainList.remove(task);
		_mainList.add(getTask(_viewList, taskIndex));
		saveTask();
	}

	public void undone(int id) throws IOException {
		int taskIndex = getTaskIndex(id);
		Task task = getTask(_viewList, taskIndex);

		setTaskIsDone(_viewList, taskIndex, false);
		_mainList.remove(task);
		_mainList.add(getTask(_viewList, taskIndex));
		saveTask();
	}

	public ArrayList<Task> view() {
		return _mainList;
	}

	public void undo(int number) {
	}

	public void redo(int number) {
	}
	
	public ArrayList<Task> getViewList() {
		return _viewList;
	}

	public ArrayList<Task> getMainList() {
		return _mainList;
	}

	public ArrayList<String> getTagsList() {
		return _tagsList;
	}
	
	private void setTaskDescription(ArrayList<Task> list, int taskIndex, String description) {
		list.get(taskIndex).setDescription(description);
	}

	private void setTaskStartDateTime(ArrayList<Task> list, int taskIndex, Calendar startDateTime) {
		list.get(taskIndex).setStartDateTime(startDateTime);
	}

	private void setTaskEndDateTime(ArrayList<Task> list, int taskIndex, Calendar endDateTime) {
		list.get(taskIndex).setEndDateTime(endDateTime);
	}

	private void setTaskTag(ArrayList<Task> list, int taskIndex, String tag) {
		list.get(taskIndex).setTag(tag);
	}

	private void setTaskIsDone(ArrayList<Task> list, int taskIndex, boolean isDone) { //throws IOException {
		list.get(taskIndex).setIsDone(isDone);
		saveTask();
	}

	private void setViewList(ArrayList<Task> list) {
		_viewList = _mainList;
	}
	
	private int getTaskIndex(int id) {
		int taskIndex = id - 1;
		return taskIndex;
	}

	private Task getTask(ArrayList<Task> list, int taskIndex) {
		Task task = list.get(taskIndex);
		return task;
	}

	private String getTaskDescription(ArrayList<Task> list, int taskIndex) {
		String taskDescription = list.get(taskIndex).getDescription();
		return taskDescription;
	}
	
//	private ArrayList<Task> getSortedListWithTag(ArrayList<Task> list) {
//		TagComparator tagComp = new TagComparator();
//		Collections.sort(list, tagComp);
//		return list;
//	}

	private Calendar getTaskStartDateTime(ArrayList<Task> list, int taskIndex) {
		Calendar taskStartDateTime = list.get(taskIndex).getStartDateTime();
		return taskStartDateTime;
	}

	private Calendar getTaskEndDateTime(ArrayList<Task> list, int taskIndex) {
		Calendar taskEndTime = list.get(taskIndex).getEndDateTime();
		return taskEndTime;
	}

	private String getTaskTag(ArrayList<Task> list, int taskIndex) {
		String taskTag = list.get(taskIndex).getTag();
		return taskTag;
	}

	private boolean getTaskIsDone(ArrayList<Task> list, int taskIndex) {
		boolean taskIsDone = list.get(taskIndex).getIsDone();
		return taskIsDone;
	}

	private ArrayList<Task> getSortedListWithDescription(ArrayList<Task> list) {
		DescriptionComparator descriptionComp = new DescriptionComparator();
		Collections.sort(list, descriptionComp);
		return list;
	}

	private ArrayList<Task> getSortedListWithStartDateTime(ArrayList<Task> list) {
		StartDateTimeComparator startDateTimeComp = new StartDateTimeComparator();
		Collections.sort(list, startDateTimeComp);
		return list;
	}

	private ArrayList<Task> getSortedListWithEndDateTime(ArrayList<Task> list) {
		EndDateTimeComparator endTimeComp = new EndDateTimeComparator();
		Collections.sort(list, endTimeComp);
		return list;
	}

}