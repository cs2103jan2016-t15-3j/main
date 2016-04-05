package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

public class RedoCommand implements Command {

	private int _times;
	private final String ADD = "add";
	private final String DELETE = "delete";
	private final String EDIT = "edit";
	private final String DONE = "done";
	private final String UNDONE = "undone";

	enum REDO_TYPE {
		ADD, DELETE, EDIT, DONE, UNDONE, INVALID
	};

	public RedoCommand() {
		_times = 1;
	}

	public RedoCommand(int times) {
		_times = times;
	}

	public int getTimes() {
		return _times;
	}

	public Command execute() {
		return null;
	}

	public Command execute(Storage storage) {
		return null;
	}

	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((_times > 0) && (_times <= historyManager.getRedoList().size()));
		for (int i = 0; i < _times; i++) {
			executeRedoCommand(listsManager, historyManager);
		}
		return null;
	}

	private void executeRedoCommand(ListsManager listsManager, HistoryManager historyManager) {
		RollbackItem rollbackItem = new RollbackItem(null, null, null);
		rollbackItem = historyManager.getRedoList().remove(historyManager.getRedoList().size() - 1);
		Task oldTask = rollbackItem.getOldTask();
		for (int i = 0; i < rollbackItem.getTimes(); i++) {
			REDO_TYPE redoType = getRedoType(rollbackItem.getCommandType());
			redoAddCommand(listsManager, rollbackItem, redoType);
			redoDeleteCommand(listsManager, rollbackItem, redoType);
			redoEditCommand(listsManager, rollbackItem, redoType);
			redoDoneCommand(listsManager, rollbackItem, redoType);
			redoUndoneCommand(listsManager, rollbackItem, redoType);
			historyManager.getUndoList().add(rollbackItem);
			if (i < rollbackItem.getTimes() - 1) {
				rollbackItem = historyManager.getRedoList().remove(historyManager.getRedoList().size() - 1);
			}
		}
		updateListsManager(listsManager, oldTask);
	}

	private void updateListsManager(ListsManager listsManager, Task newTask) {
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
		listsManager.updateLists();
		listsManager.setIndex(newTask);
	}

	private void redoUndoneCommand(ListsManager listsManager, RollbackItem rollbackItem,
			REDO_TYPE undoType) {
		if (undoType.equals(REDO_TYPE.UNDONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(DONE);
			Task tempTask = new Task();
			tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
		}
	}

	private void redoDoneCommand(ListsManager listsManager, RollbackItem rollbackItem,
			REDO_TYPE redoType) {
		if (redoType.equals(REDO_TYPE.DONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(UNDONE);
			Task tempTask = new Task();
			tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
		}
	}

	private void redoEditCommand(ListsManager listsManager, RollbackItem rollbackItem,
			REDO_TYPE redoType) {
		if (redoType.equals(REDO_TYPE.EDIT)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(EDIT);
			Task tempTask = new Task();
			tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
		}
	}

	private void redoDeleteCommand(ListsManager listsManager, RollbackItem rollbackItem,
			REDO_TYPE redoType) {
		if (redoType.equals(REDO_TYPE.DELETE)) {
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(ADD);
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(null);
		}
	}

	private void redoAddCommand(ListsManager listsManager, RollbackItem rollbackItem, 
			REDO_TYPE redoType) {
		if (redoType.equals(REDO_TYPE.ADD)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			rollbackItem.setCommandType(DELETE);
			rollbackItem.setOldTask(rollbackItem.getNewTask());
			rollbackItem.setNewTask(null);
		}
	}

	private REDO_TYPE getRedoType(String strRedoType) {
		if (strRedoType.equals(ADD)) {
			return REDO_TYPE.ADD;
		} else if (strRedoType.equals(DELETE)) {
			return REDO_TYPE.DELETE;
		} else if (strRedoType.equals(EDIT)) {
			return REDO_TYPE.EDIT;
		} else if (strRedoType.equals(DONE)) {
			return REDO_TYPE.DONE;
		} else if (strRedoType.equals(UNDONE)) {
			return REDO_TYPE.UNDONE;
		}else {
			return REDO_TYPE.INVALID;
		}
	}
}
