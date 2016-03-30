package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.VIEW_TYPE;

public class RedoCommand implements Command {

	private int _times;

	public RedoCommand() {
		_times = 1;
	}

	public RedoCommand(int times) {
		_times = times;
	}

	public int getTimes() {
		return _times;
	}

	public boolean execute() {
		return false;
	}
	
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		
		System.out.println("undostack size 2 2 2 2: " + historyManager.getUndoList().size());
		System.out.println("redostack size 2 2 2 2: " + historyManager.getRedoList().size());

		if (!isWithinList(historyManager.getRedoList(), _times)) {
			return false;
		}

		//System.out.println("test");

		for (int i = 0; i < _times; i++) {
			System.out.println("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
			RollbackItem rollbackItem = new RollbackItem(null, null, null);
			rollbackItem = historyManager.getRedoList().remove(historyManager.getRedoList().size() - 1);
			System.out.println("helo:" + rollbackItem.getCommandType());
			if (rollbackItem.getCommandType().equals("add")) {
				listsManager.getMainList().remove(rollbackItem.getNewTask());
				rollbackItem.setCommandType("delete");
				rollbackItem.setOldTask(rollbackItem.getNewTask());
				rollbackItem.setNewTask(null);
				int j = 0;
				System.out.println("DSSSSSSSSSSSSSSSSSSSS");
				while (!historyManager.getUndoList().isEmpty() && j < historyManager.getUndoList().size()) {
					System.out.println("undo: " + historyManager.getUndoList().get(i).getNewTask().getDescription());
					j++;
				}
			} else if (rollbackItem.getCommandType().equals("delete")) {
				listsManager.getMainList().add(rollbackItem.getOldTask());
				rollbackItem.setCommandType("add");
				rollbackItem.setNewTask(rollbackItem.getOldTask());
				rollbackItem.setOldTask(null);
			} else if (rollbackItem.getCommandType().equals("edit")) {
				listsManager.getMainList().remove(rollbackItem.getNewTask());
				listsManager.getMainList().add(rollbackItem.getOldTask());
				rollbackItem.setCommandType("edit");
				Task tempTask = new Task();
				tempTask = rollbackItem.getNewTask();
				rollbackItem.setNewTask(rollbackItem.getOldTask());
				rollbackItem.setOldTask(tempTask);
			} else if (rollbackItem.getCommandType().equals("done")) {
				listsManager.getMainList().remove(rollbackItem.getNewTask());
				listsManager.getMainList().add(rollbackItem.getOldTask());
				rollbackItem.setCommandType("undone");
				Task tempTask = new Task();
				tempTask = rollbackItem.getNewTask();
				rollbackItem.setNewTask(rollbackItem.getOldTask());
				rollbackItem.setOldTask(tempTask);
			} else if (rollbackItem.getCommandType().equals("undone")) {
				listsManager.getMainList().remove(rollbackItem.getNewTask());
				listsManager.getMainList().add(rollbackItem.getOldTask());
				rollbackItem.setCommandType("done");
				Task tempTask = new Task();
				tempTask = rollbackItem.getNewTask();
				rollbackItem.setNewTask(rollbackItem.getOldTask());
				rollbackItem.setOldTask(tempTask);
			}
			historyManager.getUndoList().add(rollbackItem);
		}
		//listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.updateLists();
		return true;
	}
	
	private boolean isWithinList(ArrayList<RollbackItem> list, int times) {
		boolean isWithinStack = false;
		isWithinStack = ((times <= list.size()) && (times > 0));
		return isWithinStack;
	}
}
