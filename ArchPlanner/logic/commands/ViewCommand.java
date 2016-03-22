package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Task;
import logic.TaskParameters;

public class ViewCommand implements Command {
	
	VIEW_TYPE _viewType;
	public enum VIEW_TYPE {VIEW_ALL, VIEW_DONE, VIEW_UNDONE, VIEW_OVERDUE}
	public enum CATEGORY_TYPE {CATEGORY_FLOATING, CATEGORY_EVENT, CATEGORY_DEADLINE, CATEGORY_TASKS}
	CATEGORY_TYPE _categoryType;
	TaskParameters _task;

	public ViewCommand(VIEW_TYPE viewType, CATEGORY_TYPE categoryType, TaskParameters newTaskParameters) {
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