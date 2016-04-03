package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

public class AddCommand implements Command {

	private TaskParameters _task;

	public AddCommand(TaskParameters newTaskParameters) {
		_task = new TaskParameters();
		_task = newTaskParameters;
	}

	public Command execute() {
		return null;
	}
	
	public Command execute(Storage storage) {
		return null;
	}
	
	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		Task newTask = new Task(_task.getDescription(), _task.getTagsList(), _task.getStartDate(), _task.getStartTime(), 
				_task.getEndDate(), _task.getEndTime());
		updateListsManager(listsManager, newTask);
		updateHistoryManager(historyManager, newTask);
		return null;
	}

	private void updateHistoryManager(HistoryManager historyManager, Task newTask) {
		RollbackItem rollbackItem = new RollbackItem("add", null, newTask);
		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
	}

	private void updateListsManager(ListsManager listsManager, Task newTask) {
		listsManager.getMainList().add(newTask);
		listsManager.getSelectedTagsList().clear();
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
		listsManager.updateLists();
		listsManager.setIndex(newTask);
	}
}