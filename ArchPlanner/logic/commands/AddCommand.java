package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;

public class AddCommand extends Command {

	TaskParameters _task = new TaskParameters(null, null, null, null);

	public AddCommand(TaskParameters newTaskParameters) {
		_task = newTaskParameters;
	}


	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		Task newTask = new Task(_task.getDescription(), _task.getTagsList(), _task.getStartDateTime(),
				_task.getEndDateTime());
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		mainList.add(newTask);
		listsManager.updateLists(mainList);

		RollbackItem rollbackItem = new RollbackItem("add", null, newTask);
		ArrayList<RollbackItem> undoList = new ArrayList<RollbackItem>();

			undoList.addAll(historyManager.getUndoList());

		undoList.add(rollbackItem);
		historyManager.setUndoList(undoList);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
		System.out.println("undolist size: " + undoList.size());
		return true;
	}
}