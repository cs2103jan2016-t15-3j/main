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

	public ArrayList<Task> _mainList;
	public ArrayList<Task> _viewList;

	public Storage storage = new Storage();

	public Logic() throws ClassNotFoundException, IOException {
		storage.loadStorageFile();
		_mainList = storage.getTasksList();
		setViewList(_mainList);
	}

	public void addTask(String description, Date startTime, Date endTime, String tag) throws IOException {
		Task task = new Task();
		task.setDescription(description);
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setTag(tag);
		_mainList.add(task);
		saveTask();
	}

	public void deleteTask(int id) throws IOException {
		int taskIndex= getTaskIndex(id);
		Task task = getTask(_viewList, taskIndex);
		
		_mainList.remove(task);
		saveTask();
	}

	public void editTask(int id, String newDescription, Date newStartTime, Date newEndTime, String newTag) throws IOException {
		int taskIndex = getTaskIndex(id);
		Task oldTask = getTask(_viewList, taskIndex);
		
		if (newDescription.equals(getTaskDescription(_viewList, taskIndex))) {
			setTaskDescription(_viewList, taskIndex, newDescription);
		}

		if (newStartTime.equals(getTaskStartTime(_viewList, taskIndex))) {
			setTaskStartTime(_viewList, taskIndex, newStartTime);
		}

		if (newEndTime.equals(getTaskEndTime(_viewList, taskIndex))) {
			setTaskEndTime(_viewList, taskIndex, newEndTime);
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

	public ArrayList<String> updateTagsList() {
		ArrayList<String> tagsList = new ArrayList<String>();

		for (int i = 0; i < _mainList.size(); i++) {
			String tag = getTaskTag(_mainList, i);
			if (!tagsList.contains(tag)) {
				tagsList.add(tag);
			}
		}
		Collections.sort(tagsList);
		return tagsList;
	}

	public ArrayList<Task> updateViewFloatingTasksList() {
		ArrayList<Task> floatingTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartTime(_mainList, i) == null) && (getTaskEndTime(_mainList, i) == null)) {
				floatingTasksList.add(_mainList.get(i));
			}
		}
		return floatingTasksList;
	}

	public ArrayList<Task> updateViewDeadLineTasksList() {
		ArrayList<Task> deadLineTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartTime(_mainList, i) == null) && (getTaskEndTime(_mainList, i) != null)) {
				deadLineTasksList.add(_mainList.get(i));
			}
		}
		deadLineTasksList = getSortedListWithEndTime(deadLineTasksList);
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
			if ((getTaskStartTime(_mainList, i) != null) && (getTaskEndTime(_mainList, i) != null)) {
				timeLineTasksList.add(_mainList.get(i));
			}
		}
		timeLineTasksList = getSortedListWithStartTime(timeLineTasksList);
		timeLineTasksList = getSortedListWithEndTime(timeLineTasksList);
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

	public Date getTaskStartTime(ArrayList<Task> list, int taskIndex) {
		Date taskStartTime = list.get(taskIndex).getStartTime();
		return taskStartTime;
	}

	public Date getTaskEndTime(ArrayList<Task> list, int taskIndex) {
		Date taskEndTime = list.get(taskIndex).getEndTime();
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

	public ArrayList<Task> getSortedListWithStartTime(ArrayList<Task> list) {
		StartTimeComparator startTimeComp = new StartTimeComparator();
		Collections.sort(list, startTimeComp);
		return list;
	}

	public ArrayList<Task> getSortedListWithEndTime(ArrayList<Task> list) {
		EndTimeComparator endTimeComp = new EndTimeComparator();
		Collections.sort(list, endTimeComp);
		return list;
	}

	public void setTaskDescription(ArrayList<Task> list, int taskIndex, String description) {
		list.get(taskIndex).setDescription(description);
	}

	public void setTaskStartTime(ArrayList<Task> list, int taskIndex, Date startTime) {
		list.get(taskIndex).setStartTime(startTime);
	}

	public void setTaskEndTime(ArrayList<Task> list, int taskIndex, Date endTime) {
		list.get(taskIndex).setEndTime(endTime);
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
}