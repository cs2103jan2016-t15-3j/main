package logic.commands;

import java.util.ArrayList;
import java.util.Collections;

import logic.Task;

public class DeleteCommand implements Command {

	private int _id;

	public DeleteCommand(int _id) {
		this._id = _id;
	}

	public void setId(int id) {
		assert(id >= 1); //id can only be 1 or greater
		_id = id;
	}


	public int getId() {
		return _id;
	}

	@Override
	public boolean execute(ArrayList<Task> mainList, ArrayList<Task> viewList, ArrayList<String> tagsList) {

		if (isValidDeleteCommand(viewList, _id)) {
			int taskIndex= getTaskIndex(_id);
			Task task = getTask(viewList, taskIndex);
			viewList.remove(taskIndex);
			mainList.remove(task);
			updateTagsList(mainList, tagsList);
			Collections.sort(tagsList);
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidDeleteCommand(ArrayList<Task> viewList, int _id) {
		boolean isValidDeleteCommand;
		isValidDeleteCommand = (_id <= viewList.size() && _id > 0);
		return isValidDeleteCommand;

	}

	private int getTaskIndex(int id) {
		int taskIndex = id - 1;
		return taskIndex;
	}

	private Task getTask(ArrayList<Task> list, int taskIndex) {
		Task task = list.get(taskIndex);
		return task;
	}

	private String getTaskTag(ArrayList<Task> list, int taskIndex) {
		String taskTag = list.get(taskIndex).getTag();
		return taskTag;
	}

	private void updateTagsList(ArrayList<Task> mainList, ArrayList<String> tagsList) {
		tagsList.removeAll(tagsList);
		for (int i = 0; i < mainList.size(); i++) {
			String tag = getTaskTag(mainList, i);
			if (!tagsList.contains(tag)) {
				tagsList.add(tag);
			}
		}
	}
}
