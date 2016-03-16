package logic.commands;

import java.util.ArrayList;
import java.util.Stack;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;

public class UndoneCommand implements Command {

	private int _index;

	public UndoneCommand(int index) {
		_index = index - 1;
	}

	public int getIndex() {
		return _index;
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
		newTask.setIsDone(false);
		mainList.add(newTask);
		listsManager.updateLists(mainList);
		
		RollbackItem rollbackItem = new RollbackItem("done", oldTask, newTask);
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
