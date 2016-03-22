package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;

public class AddCommand implements Command {

	TaskParameters _task;

	public AddCommand(TaskParameters newTaskParameters) {
		_task = new TaskParameters();
		_task = newTaskParameters;
	}

	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		Task newTask = new Task(_task.getDescription(), _task.getTagsList(), _task.getStartDate(), _task.getStartTime(), 
				_task.getEndDate(), _task.getEndTime());
		listsManager.getMainList().add(newTask);
		listsManager.updateLists();

		RollbackItem rollbackItem = new RollbackItem("add", null, newTask);

		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
		System.out.println("undolist size: " + historyManager.getUndoList().size());
		return true;
	}
}