package logic.commands;

import logic.Task;

import java.util.ArrayList;
import java.util.Calendar;

public class EditCommand extends Command {

	private int _id;
	private String _description;
	private String _tag;
	private Calendar _startDateTime;
	private Calendar _endDateTime;
	private boolean _isDone;

	public EditCommand(int id, String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		assert(id >= 1); //id can only be 1 or greater
		_id = id;
		_description = description;
		_tag = tag;
		_startDateTime = startDateTime;
		_endDateTime = endDateTime;
		_isDone = false;
	}

	public EditCommand(int id, String description, String tag, Calendar startDateTime, Calendar endDateTime, boolean isDone) {
		this(id, description, tag, startDateTime, endDateTime);
		_isDone = isDone;
	}

	public void setId(int id) {
		assert(id >= 1); //id can only be 1 or greater
		_id = id;
	}

	public void setDescription(String description) {
		assert(description != null); //description cannot be null
		_description = description;
	}

	public void setTag(String tag) {
		_tag = tag;
	}

	public void setStartDateTime(Calendar startDateTime) {
		_startDateTime = startDateTime;
	}

	public void setEndDateTime(Calendar endDateTime) {
		_endDateTime = endDateTime;
	}

	public void setIsDone(boolean isDone) {
		_isDone = isDone;
	}

	public int getId() {
		return _id;
	}

	public String getDescription() {
		return _description;
	}

	public String getTag() {
		return _tag;
	}

	public Calendar getStartDateTime() {
		return _startDateTime;
	}

	public Calendar getEndDateTime() {
		return _endDateTime;
	}

	public boolean getIsDone() {
		return _isDone;
	}

	@Override
	public boolean execute(ArrayList<Task> mainList, ArrayList<Task> viewList, ArrayList<String> tagsList) {

		int taskIndex = getTaskIndex();
		Task oldTask = getTask(viewList, taskIndex);
		
		if ((_description != null) && (!_description.equals(getTaskDescription(viewList, taskIndex)))) {
			setTaskDescription(viewList, taskIndex);
		}

		if ((_tag != null) && (!_tag.equals(getTaskTag(viewList, taskIndex)))) {
			setTaskTag(viewList, taskIndex);
		}

		if ((_startDateTime != null) && (!_startDateTime.equals(getTaskStartDateTime(viewList, taskIndex)))) {
			setTaskStartDateTime(viewList, taskIndex);
		}

		if ((_endDateTime != null) && (!_endDateTime.equals(getTaskEndDateTime(viewList, taskIndex)))) {
			setTaskEndDateTime(viewList, taskIndex);
		}

		if (!_isDone == getTaskIsDone(viewList, taskIndex)) {
			setTaskIsDone(viewList, taskIndex);
		}
		
		mainList.remove(oldTask);
		Task newTask = getTask(viewList, taskIndex);	
		mainList.add(newTask);
		updateTagsList(mainList, tagsList);
		return true;
	}

	private void setTaskDescription(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).setDescription(_description);
	}

	private void setTaskTag(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).setTag(_tag);
	}

	private void setTaskStartDateTime(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).setStartDateTime(_startDateTime);
	}

	private void setTaskEndDateTime(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).setEndDateTime(_endDateTime);
	}

	private void setTaskIsDone(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).setIsDone(_isDone);
	}

	private int getTaskIndex() {
		int taskIndex = _id - 1;
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

	private String getTaskTag(ArrayList<Task> list, int taskIndex) {
		String taskTag = list.get(taskIndex).getTag();
		return taskTag;
	}

	private Calendar getTaskStartDateTime(ArrayList<Task> list, int taskIndex) {
		Calendar taskStartDateTime = list.get(taskIndex).getStartDateTime();
		return taskStartDateTime;
	}

	private Calendar getTaskEndDateTime(ArrayList<Task> list, int taskIndex) {
		Calendar taskEndTime = list.get(taskIndex).getEndDateTime();
		return taskEndTime;
	}

	private boolean getTaskIsDone(ArrayList<Task> list, int taskIndex) {
		boolean taskIsDone = list.get(taskIndex).getIsDone();
		return taskIsDone;
	}

	private void updateTagsList(ArrayList<Task> mainList, ArrayList<String> tagsList) {
		tagsList.clear();
		for (int i = 0; i < mainList.size(); i++) {
			String tag = getTaskTag(mainList, i);
			if ((tag != null) && (!tagsList.contains(tag))) {
				tagsList.add(tag);
			}
		}
		tagsList.add(0, "TimeLine");
		tagsList.add(1 ,"Event");
		tagsList.add(2, "Tasks");
	}
}
