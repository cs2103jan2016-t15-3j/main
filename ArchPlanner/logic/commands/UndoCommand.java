package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

public class UndoCommand implements Command {

	private int _times;
	private final String ADD = "add";
	private final String DELETE = "delete";
	private final String EDIT = "edit";
	private final String DONE = "done";
	private final String UNDONE = "undone";

	enum UNDO_TYPE {
		ADD, DELETE, EDIT, DONE, UNDONE, INVALID
	};

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

	public Command execute(Storage storage) {
		return null;
	}

	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((_times > 0) && (_times <= historyManager.getUndoList().size()));

		for (int i = 0; i < _times; i++) {
			executeUndoCommand(listsManager, historyManager);
		}
		return null;
	}

	private void executeUndoCommand(ListsManager listsManager, HistoryManager historyManager) {
		RollbackItem rollbackItem = new RollbackItem(null, null, null);
		rollbackItem = historyManager.getUndoList().remove(historyManager.getUndoList().size() - 1);
		Task oldTask = rollbackItem.getOldTask();
		for (int i = 0; i < rollbackItem.getTimes(); i++) {
			UNDO_TYPE undoType = getUndoType(rollbackItem.getCommandType());
			undoAddCommand(listsManager, rollbackItem, undoType);
			undoDeleteCommand(listsManager, rollbackItem, undoType);
			undoEditCommand(listsManager, rollbackItem, undoType);
			undoDoneCommand(listsManager, rollbackItem, undoType);
			undoUndoneCommand(listsManager, rollbackItem, undoType);
			historyManager.getRedoList().add(rollbackItem);
			if (i < rollbackItem.getTimes() - 1) {
				rollbackItem = historyManager.getUndoList().remove(historyManager.getUndoList().size() - 1);
			}
		}
		updateListsManager(listsManager, oldTask);
	}

	private void updateListsManager(ListsManager listsManager, Task task) {
		listsManager.getSelectedTagsList().clear();
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
		listsManager.updateLists();
		listsManager.setIndex(task);
	}

	private void undoUndoneCommand(ListsManager listsManager, RollbackItem rollbackItem,
			UNDO_TYPE undoType) {
		if (undoType.equals(UNDO_TYPE.UNDONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(DONE);
			Task tempTask = new Task();
			tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
		}
	}

	private void undoDoneCommand(ListsManager listsManager, RollbackItem rollbackItem,
			UNDO_TYPE undoType) {
		if (undoType.equals(UNDO_TYPE.DONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(UNDONE);
			Task tempTask = new Task();
			tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
		}
	}

	private void undoEditCommand(ListsManager listsManager, RollbackItem rollbackItem,
			UNDO_TYPE undoType) {
		if (undoType.equals(UNDO_TYPE.EDIT)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(EDIT);
			Task tempTask = new Task();
			tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
		}
	}

	private void undoDeleteCommand(ListsManager listsManager, RollbackItem rollbackItem,
			UNDO_TYPE undoType) {
		if (undoType.equals(UNDO_TYPE.DELETE)) {
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(ADD);
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(null);
		}
	}

	private void undoAddCommand(ListsManager listsManager, RollbackItem rollbackItem, 
			UNDO_TYPE undoType) {
		if (undoType.equals(UNDO_TYPE.ADD)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			rollbackItem.setCommandType(DELETE);
			rollbackItem.setOldTask(rollbackItem.getNewTask());
			rollbackItem.setNewTask(null);
		}
	}

	private UNDO_TYPE getUndoType(String strUndoType) {
		if (strUndoType.equals(ADD)) {
			return UNDO_TYPE.ADD;
		} else if (strUndoType.equals(DELETE)) {
			return UNDO_TYPE.DELETE;
		} else if (strUndoType.equals(EDIT)) {
			return UNDO_TYPE.EDIT;
		} else if (strUndoType.equals(DONE)) {
			return UNDO_TYPE.DONE;
		} else if (strUndoType.equals(UNDONE)) {
			return UNDO_TYPE.UNDONE;
		}else {
			return UNDO_TYPE.INVALID;
		}
	}
}
