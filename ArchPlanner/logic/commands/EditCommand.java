package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;

import java.util.ArrayList;
import java.util.Calendar;

public class EditCommand implements Command {

	TaskParameters _task = new TaskParameters(null, null, null, null);
	private int _index;

	public EditCommand(int index, TaskParameters newTaskParameters) {
		assert(index >= 1);
		_task = newTaskParameters;
		_index = index - 1;
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		
		ArrayList<Task> viewList = new ArrayList<Task>();
		viewList.addAll(listsManager.getViewList());
		
		if (!isWithinList(viewList, _index)) {
			return false;
		}
		
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		
		Task oldTask = viewList.get(_index);
		mainList.remove(oldTask);
		
		Task newTask = new Task(null, null, null, null);
		newTask = oldTask;
		
		if ((_task.getDescription() != null) && (!_task.getDescription().equals(oldTask.getDescription()))) {
			newTask.setDescription(_task.getDescription());
		}

		if ((_task.getTagsList() != null) && (!_task.getTagsList().equals(oldTask.getTagsList()))) {
			newTask.setTag(_task.getTagsList());
		}

		if ((_task.getStartDateTime() != null) && (!_task.getStartDateTime().equals(oldTask.getStartDateTime()))) {
			newTask.setStartDateTime(_task.getStartDateTime());
		}

		if ((_task.getEndDateTime() != null) && (!_task.getEndDateTime().equals(oldTask.getEndDateTime()))) {
			newTask.setEndDateTime(_task.getEndDateTime());
		}

		if (!_task.getIsDone() == oldTask.getIsDone()) {
			newTask.setIsDone(_task.getIsDone());
		}

		mainList.add(newTask);
		listsManager.updateLists(mainList);
		
		RollbackItem rollbackItem = new RollbackItem("edit", oldTask, newTask);
		ArrayList<RollbackItem> undoList = new ArrayList<RollbackItem>();
		undoList.addAll(historyManager.getUndoList());
		undoList.add(rollbackItem);
		historyManager.setUndoList(undoList);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
		System.out.println("undolist size: " + undoList.size());
		return true;
	}
	
	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
}
