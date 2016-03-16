package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;

public class UndoCommand implements Command {

	private int _times;

	public UndoCommand(int times) {
		_times = times;
	}

	public int getTimes() {
		return _times;
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		ArrayList<RollbackItem> undoList = new ArrayList<RollbackItem>();
		ArrayList<RollbackItem> redoList = new ArrayList<RollbackItem>();

		undoList.addAll(historyManager.getUndoList());
		redoList.addAll(historyManager.getRedoList());

		System.out.println("undostack size 1 1 1 1: " + undoList.size());
		System.out.println("redostack size 1 1 1 1: " + redoList.size());

		if (!isWithinList(undoList, _times)) {
			return false;
		}

		//System.out.println("test");

		for (int i = 0; i < _times; i++) {
			System.out.println("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
			RollbackItem rollbackItem = new RollbackItem(null, null, null);
			rollbackItem = undoList.remove(undoList.size() - 1);
			System.out.println("helo:" + rollbackItem.getCommandType());

			if (rollbackItem.getCommandType().equals("add")) {
				mainList.remove(rollbackItem.getNewTask());
				rollbackItem.setCommandType("delete");
				rollbackItem.setOldTask(rollbackItem.getNewTask());
				rollbackItem.setNewTask(null);
			} else if (rollbackItem.getCommandType().equals("delete")) {
				mainList.add(rollbackItem.getOldTask());
				rollbackItem.setCommandType("add");
				rollbackItem.setNewTask(rollbackItem.getOldTask());
				rollbackItem.setOldTask(null);
			} else if (rollbackItem.getCommandType().equals("edit")) {
				mainList.remove(rollbackItem.getNewTask());
				mainList.add(rollbackItem.getOldTask());
				rollbackItem.setCommandType("edit");
				Task tempTask = new Task(null, null, null, null);
				tempTask = rollbackItem.getNewTask();
				rollbackItem.setNewTask(rollbackItem.getOldTask());
				rollbackItem.setOldTask(tempTask);
			} else if (rollbackItem.getCommandType().equals("done")) {
				mainList.remove(rollbackItem.getNewTask());
				mainList.add(rollbackItem.getOldTask());
				rollbackItem.setCommandType("undone");
				Task tempTask = new Task(null, null, null, null);
				tempTask = rollbackItem.getNewTask();
				rollbackItem.setNewTask(rollbackItem.getOldTask());
				rollbackItem.setOldTask(tempTask);
			} else if (rollbackItem.getCommandType().equals("undone")) {
				mainList.remove(rollbackItem.getNewTask());
				mainList.add(rollbackItem.getOldTask());
				rollbackItem.setCommandType("done");
				Task tempTask = new Task(null, null, null, null);
				tempTask = rollbackItem.getNewTask();
				rollbackItem.setNewTask(rollbackItem.getOldTask());
				rollbackItem.setOldTask(tempTask);
			}
			redoList.add(rollbackItem);
		}
		historyManager.setUndoList(undoList);
		historyManager.setRedoList(redoList);
		listsManager.getUndoneList();
		listsManager.updateLists(mainList);
		return true;
	}

	private boolean isWithinList(ArrayList<RollbackItem> list, int times) {
		boolean isWithinStack = false;
		isWithinStack = ((times <= list.size()) && (times > 0));
		return isWithinStack;
	}
}
