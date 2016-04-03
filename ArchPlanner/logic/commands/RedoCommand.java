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
		Task newTask = new Task();
		for (int i = 0; i < _times; i++) {
			executeRedoCommand(listsManager, historyManager, newTask);
		}
		updateListsManager(listsManager, newTask);
		return null;
	}

	private void executeRedoCommand(ListsManager listsManager, HistoryManager historyManager, Task newTask) {
		RollbackItem rollbackItem = new RollbackItem(null, null, null);
		rollbackItem = historyManager.getRedoList().remove(historyManager.getRedoList().size() - 1);
		for (int i = 0; i < rollbackItem.getTimes(); i++) {
			REDO_TYPE redoType = getRedoType(rollbackItem.getCommandType());
			redoAddCommand(listsManager, newTask, rollbackItem, redoType);
			redoDeleteCommand(listsManager, newTask, rollbackItem, redoType);
			redoEditCommand(listsManager, newTask, rollbackItem, redoType);
			redoDoneCommand(listsManager, newTask, rollbackItem, redoType);
			redoUndoneCommand(listsManager, newTask, rollbackItem, redoType);
			historyManager.getUndoList().add(rollbackItem);
			if (i < rollbackItem.getTimes() - 1) {
				rollbackItem = historyManager.getRedoList().remove(historyManager.getRedoList().size() - 1);
			}
		}
	}

	private void updateListsManager(ListsManager listsManager, Task newTask) {
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
		listsManager.updateLists();
		if (_times == 1) {
			listsManager.setIndex(newTask);
		}
	}

	private void redoUndoneCommand(ListsManager listsManager, Task newTask, RollbackItem rollbackItem,
			REDO_TYPE undoType) {
		if (undoType.equals(REDO_TYPE.UNDONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(DONE);
			Task tempTask = new Task();
			tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
			newTask = rollbackItem.getOldTask();
		}
	}

	private void redoDoneCommand(ListsManager listsManager, Task newTask, RollbackItem rollbackItem,
			REDO_TYPE redoType) {
		if (redoType.equals(REDO_TYPE.DONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(UNDONE);
			Task tempTask = new Task();
			tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
			newTask = rollbackItem.getOldTask();
		}
	}

	private void redoEditCommand(ListsManager listsManager, Task newTask, RollbackItem rollbackItem,
			REDO_TYPE redoType) {
		if (redoType.equals(REDO_TYPE.EDIT)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(EDIT);
			Task tempTask = new Task();
			tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
			newTask = rollbackItem.getOldTask();
		}
	}

	private void redoDeleteCommand(ListsManager listsManager, Task newTask, RollbackItem rollbackItem,
			REDO_TYPE redoType) {
		if (redoType.equals(REDO_TYPE.DELETE)) {
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(ADD);
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(null);
			newTask = rollbackItem.getOldTask();
		}
	}

	private Task redoAddCommand(ListsManager listsManager, Task newTask, RollbackItem rollbackItem, 
			REDO_TYPE redoType) {
		if (redoType.equals(REDO_TYPE.ADD)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			rollbackItem.setCommandType(DELETE);
			rollbackItem.setOldTask(rollbackItem.getNewTask());
			rollbackItem.setNewTask(null);
			newTask = new Task();
		}
		return newTask;
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
