package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Task;
import logic.TaskParameters;

public class ViewCommand implements Command {
	
	String _viewType;
	String _categoryType;
	TaskParameters _task;

	public ViewCommand(String viewType, String categoryType, TaskParameters newTaskParameters) {
		_viewType = viewType;
		_categoryType = categoryType;
		_task = new TaskParameters();
		_task = newTaskParameters;
	}

	public ViewCommand(ListsManager listsManager, HistoryManager historyManager) {
		Task newTask = new Task(_task.getDescription(), _task.getTagsList(), _task.getStartDate(), _task.getStartTime(), 
				_task.getEndDate(), _task.getEndTime());
	}

	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		return false;
	}
}