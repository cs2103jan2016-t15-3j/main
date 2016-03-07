package logic.commands;

import java.util.ArrayList;

import logic.Task;

public class DoneCommand implements Command {

    private int _id;

    public DoneCommand(int _id) {
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
		boolean isSuccessful;

		int taskIndex = getTaskIndex(_id);
		Task oldTask = getTask(viewList, taskIndex);
		
		setTaskIsDone(viewList, taskIndex);
		
		mainList.remove(oldTask);
		
		Task newTask = getTask(viewList, taskIndex);
		
		isSuccessful = mainList.add(newTask);

		return isSuccessful;
	}
	
	private void setTaskIsDone(ArrayList<Task> list, int taskIndex) {
		list.get(taskIndex).setIsDone(true);
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
