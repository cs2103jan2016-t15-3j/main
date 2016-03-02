package logic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This class interact with the UI and process the operation, 
 * followed by updating the changes to Storage
 * 
 * @author riyang
 *
 */
public class Logic {

	private ArrayList<Task> _mainList;
	private ArrayList<Task> _viewList;
	private ArrayList<String> _tagsList;

	public Storage storage = new Storage();

	public Logic() throws ClassNotFoundException, IOException {
		storage.loadStorageFile();
		_mainList = storage.getTasksList();
		_tagsList = new ArrayList();
		setViewList(_mainList);
	}

	public void addTask(String description, String tag, Date startDateTime, Date endDateTime) throws IOException {
		Task task = new Task(description, tag, startDateTime, endDateTime);
		_mainList.add(task);
		if (!_tagsList.contains(tag)) {
			_tagsList.add(tag);
		}
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

	public void editTask(int id, String newDescription, String newTag, Date newStartDateTime, Date newEndDateTime) throws IOException {
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

	public void done(int id) throws IOException {
		int taskIndex = getTaskIndex(id);
		Task task = getTask(_viewList, taskIndex);

		setTaskIsDone(_viewList, taskIndex, true);
		_mainList.remove(task);
		_mainList.add(getTask(_viewList, taskIndex));
		saveTask();
	}

	public void unDone(int id) throws IOException {
		int taskIndex = getTaskIndex(id);
		Task task = getTask(_viewList, taskIndex);

		setTaskIsDone(_viewList, taskIndex, false);
		_mainList.remove(task);
		_mainList.add(getTask(_viewList, taskIndex));
		saveTask();
	}

	public ArrayList<Task> viewAll() {
		return _mainList;
	}

	public void undo(int number) {
	}

	public void redo(int number) {
	}

	public ArrayList<Task> updateViewFloatingTasksList() {
		ArrayList<Task> floatingTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartDateTime(_mainList, i) == null) && (getTaskEndDateTime(_mainList, i) == null)) {
				floatingTasksList.add(_mainList.get(i));
			}
		}
		return floatingTasksList;
	}

	public ArrayList<Task> updateViewDeadLineTasksList() {
		ArrayList<Task> deadLineTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartDateTime(_mainList, i) == null) && (getTaskEndDateTime(_mainList, i) != null)) {
				deadLineTasksList.add(_mainList.get(i));
			}
		}
		deadLineTasksList = getSortedListWithEndDateTime(deadLineTasksList);
		deadLineTasksList = getSortedListWithDescription(deadLineTasksList);
		return deadLineTasksList;
	}

	public ArrayList<Task> updateViewTagTasksList(String tag) {
		ArrayList<Task> tagTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if (getTaskTag(_mainList, i).equals(tag)) {
				tagTasksList.add(_mainList.get(i));
			}
		}
		return tagTasksList;
	}

	public ArrayList<Task> updateViewTimeLineTasksList() {
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

	public void saveTask() throws IOException {
		storage.writeStorageFile(_mainList);
	}

	public void exitProgram() {
		System.exit(0);
	}

	public int getTaskIndex(int id) {
		int taskIndex = id - 1;
		return taskIndex;
	}

	public Task getTask(ArrayList<Task> list, int taskIndex) {
		Task task = list.get(taskIndex);
		return task;
	}

	public String getTaskDescription(ArrayList<Task> list, int taskIndex) {
		String taskDescription = list.get(taskIndex).getDescription();
		return taskDescription;
	}

	public Date getTaskStartDateTime(ArrayList<Task> list, int taskIndex) {
		Date taskStartDateTime = list.get(taskIndex).getStartDateTime();
		return taskStartDateTime;
	}

	public Date getTaskEndDateTime(ArrayList<Task> list, int taskIndex) {
		Date taskEndTime = list.get(taskIndex).getEndDateTime();
		return taskEndTime;
	}

	public String getTaskTag(ArrayList<Task> list, int taskIndex) {
		String taskTag = list.get(taskIndex).getTag();
		return taskTag;
	}

	public boolean getTaskIsDone(ArrayList<Task> list, int taskIndex) {
		boolean taskIsDone = list.get(taskIndex).getIsDone();
		return taskIsDone;
	}

	public ArrayList<Task> getSortedListWithDescription(ArrayList<Task> list) {
		DescriptionComparator descriptionComp = new DescriptionComparator();
		Collections.sort(list, descriptionComp);
		return list;
	}

	public ArrayList<Task> getSortedListWithStartDateTime(ArrayList<Task> list) {
		StartDateTimeComparator startDateTimeComp = new StartDateTimeComparator();
		Collections.sort(list, startDateTimeComp);
		return list;
	}

	public ArrayList<Task> getSortedListWithEndDateTime(ArrayList<Task> list) {
		EndDateTimeComparator endTimeComp = new EndDateTimeComparator();
		Collections.sort(list, endTimeComp);
		return list;
	}

	public void setTaskDescription(ArrayList<Task> list, int taskIndex, String description) {
		list.get(taskIndex).setDescription(description);
	}

	public void setTaskStartDateTime(ArrayList<Task> list, int taskIndex, Date startDateTime) {
		list.get(taskIndex).setStartDateTime(startDateTime);
	}

	public void setTaskEndDateTime(ArrayList<Task> list, int taskIndex, Date endDateTime) {
		list.get(taskIndex).setEndDateTime(endDateTime);
	}

	public void setTaskTag(ArrayList<Task> list, int taskIndex, String tag) {
		list.get(taskIndex).setTag(tag);
	}

	public void setTaskIsDone(ArrayList<Task> list, int taskIndex, boolean isDone) throws IOException {
		list.get(taskIndex).setIsDone(isDone);
		saveTask();
	}

	public void setViewList(ArrayList<Task> list) {
		_viewList = _mainList;
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
}