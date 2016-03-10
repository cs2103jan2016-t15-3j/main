package logic.commands;

import java.util.ArrayList;

import logic.Task;

public class UndoneCommand extends Command {

	private int _id;

	public UndoneCommand(int _id) {
		this._id = _id;
	}

	public void setId(int id) {
		assert (id >= 1); //id can only be 1 or greater
		_id = id;
	}

	public int getId() {
		return _id;
	}

	@Override
	public boolean execute(ArrayList<Task> mainList, ArrayList<Task> viewList, ArrayList<String> tagsList) {
		if (isValidUndoneCommand(viewList)) {
			int taskIndex = getTaskIndex(_id);
			Task oldTask = getTask(viewList, taskIndex);
			setTaskIsDone(viewList, taskIndex);
			mainList.remove(oldTask);
			Task newTask = getTask(viewList, taskIndex);
			mainList.add(newTask);
			viewList.remove(newTask);
			return true;
		}
		return false;
	}

	private boolean isValidUndoneCommand(ArrayList<Task> viewList) {
		boolean isValidUndoneCommand;
		isValidUndoneCommand = ((_id <= viewList.size()) && (_id > 0));
		return isValidUndoneCommand;
	}

	private void setTaskIsDone(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).setIsDone(false);
	}

	private int getTaskIndex(int id) {
		int taskIndex = id - 1;
		return taskIndex;
	}

	private Task getTask(ArrayList<Task> list, int taskIndex) {
		Task task = list.get(taskIndex);
		return task;
	}
}
