package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

public class UndoCommand implements CommandInterface {

	private int _times;
	private String _message;

	private final String ADD = "add";
	private final String DELETE = "delete";
	private final String EDIT = "edit";
	private final String DONE = "done";
	private final String UNDONE = "undone";

	private static final String MESSAGE_UNDO_ADD_COMMAND = "deleted \"%1$s\"";
	private static final String MESSAGE_UNDO_DELETE_COMMAND = "added \"%1$s\"";
	private static final String MESSAGE_UNDO_EDIT_COMMAND = "edited \"%1$s\"";
	private static final String MESSAGE_UNDO_DONE_COMMAND = "undone \"%1$s\"";
	private static final String MESSAGE_UNDO_UNDONE_COMMAND = "done \"%1$s\"";

	private static final String MESSAGE_UNDO_MULTIPLE_ADD_COMMAND = "deleted multiple tasks";
	private static final String MESSAGE_UNDO_MULTIPLE_DELETE_COMMAND = "added multiple tasks";
	private static final String MESSAGE_UNDO_MULTIPLE_EDIT_COMMAND = "edited multiple tasks";
	private static final String MESSAGE_UNDO_MULTIPLE_DONE_COMMAND = "undone multiple tasks";
	private static final String MESSAGE_UNDO_MULTIPLE_UNDONE_COMMAND = "done multiple tasks";

	enum UNDO_TYPE {
		ADD, DELETE, EDIT, DONE, UNDONE, INVALID
	};

	public UndoCommand() {
		_times = 1;
		_message = "";
	}

	public UndoCommand(int times) {
		_times = times;
		_message = "";
	}

	public int getTimes() {
		return _times;
	}

	public CommandInterface execute() {
		return null;
	}

	public CommandInterface execute(ListsManager listsManager, Storage storage) {
		return null;
	}

	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((_times > 0) && (_times <= historyManager.getUndoList().size()));
		listsManager.getIndexList().clear();
		for (int i = 0; i < _times; i++) {
			executeUndoCommand(listsManager, historyManager);
		}
		return null;
	}

	private void executeUndoCommand(ListsManager listsManager, HistoryManager historyManager) {
		RollbackItem rollbackItem = new RollbackItem(null, null, null);
		rollbackItem = historyManager.getUndoList().remove(historyManager.getUndoList().size() - 1);
		int undoNumOfTimes = rollbackItem.getTimes();
		ArrayList<Task> undoTasksList = new ArrayList<Task>();
		for (int i = 0; i < undoNumOfTimes; i++) {
			UNDO_TYPE undoType = getUndoType(rollbackItem.getCommandType());
			undoAddCommand(listsManager, rollbackItem, undoType, undoNumOfTimes);
			undoDeleteCommand(listsManager, rollbackItem, undoType, undoNumOfTimes);
			undoEditCommand(listsManager, rollbackItem, undoType, undoNumOfTimes);
			undoDoneCommand(listsManager, rollbackItem, undoType, undoNumOfTimes);
			undoUndoneCommand(listsManager, rollbackItem, undoType, undoNumOfTimes);
			undoTasksList.add(rollbackItem.getNewTask());
			historyManager.getRedoList().add(rollbackItem);
			updateListsManager(listsManager, rollbackItem.getNewTask());
			if (i < rollbackItem.getTimes() - 1) {
				rollbackItem = historyManager.getUndoList().remove(historyManager.getUndoList().size() - 1);
			}
		}
		for (int i = 0; i < undoTasksList.size(); i++) {
			Task doneTask = undoTasksList.get(i);
			listsManager.updateIndexList(doneTask);
		}
	}

	private void updateListsManager(ListsManager listsManager, Task task) {
		listsManager.getSelectedTagsList().clear();
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
		listsManager.updateLists();
	}

	private void undoUndoneCommand(ListsManager listsManager, RollbackItem rollbackItem,
			UNDO_TYPE undoType, int undoNumOfTimes) {
		if (undoType.equals(UNDO_TYPE.UNDONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(DONE);
			Task tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_UNDO_UNDONE_COMMAND, rollbackItem.getNewTask().getDescription());
			} else {
				_message = MESSAGE_UNDO_MULTIPLE_UNDONE_COMMAND;
			}
		}
	}

	private void undoDoneCommand(ListsManager listsManager, RollbackItem rollbackItem,
			UNDO_TYPE undoType, int undoNumOfTimes) {
		if (undoType.equals(UNDO_TYPE.DONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(UNDONE);
			Task tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_UNDO_DONE_COMMAND, rollbackItem.getNewTask().getDescription());
			} else {
				_message = MESSAGE_UNDO_MULTIPLE_DONE_COMMAND;
			}
		}
	}

	private void undoEditCommand(ListsManager listsManager, RollbackItem rollbackItem,
			UNDO_TYPE undoType, int undoNumOfTimes) {
		if (undoType.equals(UNDO_TYPE.EDIT)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(EDIT);
			Task tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_UNDO_EDIT_COMMAND, rollbackItem.getNewTask().getDescription());
			} else {
				_message = MESSAGE_UNDO_MULTIPLE_EDIT_COMMAND;
			}
		}
	}

	private void undoDeleteCommand(ListsManager listsManager, RollbackItem rollbackItem,
			UNDO_TYPE undoType, int undoNumOfTimes) {
		if (undoType.equals(UNDO_TYPE.DELETE)) {
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(ADD);
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(null);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_UNDO_DELETE_COMMAND, rollbackItem.getNewTask().getDescription());
			} else {
				_message = MESSAGE_UNDO_MULTIPLE_DELETE_COMMAND;
			}
		}
	}

	private void undoAddCommand(ListsManager listsManager, RollbackItem rollbackItem, 
			UNDO_TYPE undoType, int undoNumOfTimes) {
		if (undoType.equals(UNDO_TYPE.ADD)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			rollbackItem.setCommandType(DELETE);
			rollbackItem.setOldTask(rollbackItem.getNewTask());
			rollbackItem.setNewTask(null);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_UNDO_ADD_COMMAND, rollbackItem.getOldTask().getDescription());
			} else {
				_message = MESSAGE_UNDO_MULTIPLE_ADD_COMMAND;
			}
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

	public String getMessage() {
		return _message;
	}

	public void setMessage(String message) {
		_message = message;
	}
}
