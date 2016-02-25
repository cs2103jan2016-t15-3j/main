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

	public int numberOfTags;

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
		_mainList.remove(taskIndex);
		saveTask();
	}

	public void editTask(int id, String newDescription, Date newStartTime, Date newEndTime, String newTag) throws IOException {
		int taskIndex = getTaskIndex(id);

		if (newDescription.equals(getTaskDescription(_mainList, taskIndex))) {
			setTaskDescription(_mainList, taskIndex);
		}

		if (newStartTime.equals(getTaskStartTime(_mainList, taskIndex))) {
			setTaskStartTime(_mainList, taskIndex);
		}

		if (newEndTime.equals(getTaskEndTime(_mainList, taskIndex))) {
			setTaskEndTime(_mainList, taskIndex);
		}

		if (newTag.equals(getTaskTag(_mainList, taskIndex))) {
			setTaskTag(_mainList, taskIndex);
		}
		saveTask();
	}

	public void done(int id) throws IOException {
		int taskIndex = getTaskIndex(id);

		setTaskIsDone(_mainList, taskIndex, true);
		saveTask();
	}

	public void unDone(int id) throws IOException {
		int taskIndex = getTaskIndex(id);

		setTaskIsDone(_mainList, taskIndex, false);
		saveTask();
	}

	public ArrayList<Task> viewAll() {
		return getViewList();
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
		floatingTasksList = getSortedListWithDescription(floatingTasksList);
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

	public void setTaskDescription(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).getDescription();
	}

	public void setTaskStartTime(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).getStartTime();
	}

	public void setTaskEndTime(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).getEndTime();
	}

	public void setTaskTag(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).getTag();
	}

	public void setTaskIsDone(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).getIsDone();
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