package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.VIEW_TYPE;

public class DoneCommand implements Command {

	private int _index;
	private int _toIndex;

	public DoneCommand(int index) {
		_index = index - 1;
	}

	public DoneCommand(int index, int toIndex) {
		_index = index - 1;
		_toIndex = toIndex - 1;
	}

	public int getIndex() {
		return _index;
	}

	public int getToIndex() {
		return _toIndex;
	}

	public boolean execute() {
		return false;
	}
	
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
		newTask.setIsOverdue(oldTask.getIsOverdue());
		newTask.setIsDone(true);
		listsManager.getMainList().add(newTask);
		//listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.updateLists();
		
		RollbackItem rollbackItem = new RollbackItem("done", oldTask, newTask);
			
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
