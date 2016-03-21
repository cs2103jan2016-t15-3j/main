package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;

import java.util.ArrayList;

public class EditCommand implements Command {

	TaskParameters _task;
	private int _index;

	public EditCommand(int index, TaskParameters newTaskParameters) {
		assert(index >= 1);
		_task = new TaskParameters();
		_task = newTaskParameters;
		_index = index - 1;
	}

	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		
		if (!isWithinList(listsManager.getViewList(), _index)) {
			return false;
		}
		
		Task oldTask = listsManager.getViewList().get(_index);
		listsManager.getMainList().remove(oldTask);
		
		Task newTask = new Task();
		newTask.setDescription(oldTask.getDescription());
		newTask.setTagsList(oldTask.getTagsList());
		newTask.setStartDate(oldTask.getStartDate());
		newTask.setStartTime(oldTask.getStartTime());
		newTask.setEndDate(oldTask.getEndDate());
		newTask.setEndTime(oldTask.getEndTime());
		newTask.setIsDone(oldTask.getIsDone());
		newTask.setIsOverdue(oldTask.getIsOverdue());
		
		if ((_task.getDescription() != null) && (!_task.getDescription().equals(oldTask.getDescription()))) {
			newTask.setDescription(_task.getDescription());
		}

		if ((_task.getTagsList() != null) && (!_task.getTagsList().equals(oldTask.getTagsList()))) {
			newTask.setTagsList(_task.getTagsList());
		}

		if ((_task.getStartDate() != null) && (!_task.getStartDate().equals(oldTask.getStartDate()))) {
			newTask.setStartDate(_task.getStartDate());
		}
		
		if ((_task.getStartTime() != null) && (!_task.getStartTime().equals(oldTask.getStartTime()))) {
			newTask.setStartTime(_task.getStartTime());
		}
		
		if ((_task.getEndDate() != null) && (!_task.getEndDate().equals(oldTask.getEndDate()))) {
			newTask.setEndDate(_task.getEndDate());
		}
		
		if ((_task.getEndTime() != null) && (!_task.getEndTime().equals(oldTask.getEndTime()))) {
			newTask.setEndTime(_task.getEndTime());
		}
		
		if (!_task.getIsDone() == oldTask.getIsDone()) {
			newTask.setIsDone(_task.getIsDone());
		}
		
		listsManager.getMainList().add(newTask);
		
		listsManager.updateLists();
		
		RollbackItem rollbackItem = new RollbackItem("edit", oldTask, newTask);
		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
		System.out.println("undolist size: " + historyManager.getUndoList().size());
		return true;
	}
	
	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
}
