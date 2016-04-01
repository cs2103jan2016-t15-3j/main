package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;

public class UndoCommand implements Command {

	private int _times;

	public UndoCommand() {
		_times = 1;
	}

	public UndoCommand(int times) {
		_times = times;
	}

	public int getTimes() {
		return _times;
	}

	public Command execute() {
		return null;
	}

	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((_times > 0) && (_times <= historyManager.getUndoList().size()));
		/*
		if (!isWithinList(historyManager.getUndoList(), _times)) {
			return false;
		}
		 */

		for (int i = 0; i < _times; i++) {
			RollbackItem rollbackItem = new RollbackItem(null, null, null);
			rollbackItem = historyManager.getUndoList().remove(historyManager.getUndoList().size() - 1);
			System.out.println("helo:" + rollbackItem.getCommandType());
			if (rollbackItem.getCommandType().equals("add")) {
				listsManager.getMainList().remove(rollbackItem.getNewTask());
				rollbackItem.setCommandType("delete");
				rollbackItem.setOldTask(rollbackItem.getNewTask());
				rollbackItem.setNewTask(null);
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
			historyManager.getRedoList().add(rollbackItem);
		}
		listsManager.getSelectedTagsList().clear();
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
		listsManager.updateLists();
		System.out.println("undostack size 1 1 1 1: " + historyManager.getUndoList().size());
		System.out.println("redostack size 1 1 1 1: " + historyManager.getRedoList().size());
		return null;
	}
	/*
	private boolean isWithinList(ArrayList<RollbackItem> list, int times) {
		boolean isWithinStack = false;
		isWithinStack = ((times <= list.size()) && (times > 0));
		return isWithinStack;
	}
	 */
}
