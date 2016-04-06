//@@author A0140021J
package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

public class RedoCommand implements CommandInterface {

	private int _times;
	private String _message;

	private final String ADD = "add";
	private final String DELETE = "delete";
	private final String EDIT = "edit";
	private final String DONE = "done";
	private final String UNDONE = "undone";

	private static final String MESSAGE_REDO_ADD_COMMAND = "deleted \"%1$s\"";
	private static final String MESSAGE_REDO_DELETE_COMMAND = "added \"%1$s\"";
	private static final String MESSAGE_REDO_EDIT_COMMAND = "edited \"%1$s\"";
	private static final String MESSAGE_REDO_DONE_COMMAND = "undone \"%1$s\"";
	private static final String MESSAGE_REDO_UNDONE_COMMAND = "done \"%1$s\"";

	private static final String MESSAGE_REDO_MULTIPLE_ADD_COMMAND = "deleted multiple tasks";
	private static final String MESSAGE_REDO_MULTIPLE_DELETE_COMMAND = "added multiple tasks";
	private static final String MESSAGE_REDO_MULTIPLE_EDIT_COMMAND = "edited multiple tasks";
	private static final String MESSAGE_REDO_MULTIPLE_DONE_COMMAND = "undone multiple tasks";
	private static final String MESSAGE_REDO_MULTIPLE_UNDONE_COMMAND = "done multiple tasks";

	enum REDO_TYPE {
		ADD, DELETE, EDIT, DONE, UNDONE, INVALID
	};

	public RedoCommand() {
		_times = 1;
		_message = "";
	}

	public RedoCommand(int times) {
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
		assert((_times > 0) && (_times <= historyManager.getRedoList().size()));
		listsManager.getIndexList().clear();
		for (int i = 0; i < _times; i++) {
			executeRedoCommand(listsManager, historyManager);
		}
		return null;
	}

	private void executeRedoCommand(ListsManager listsManager, HistoryManager historyManager) {
		RollbackItem rollbackItem = new RollbackItem(null, null, null);
		rollbackItem = historyManager.getRedoList().remove(historyManager.getRedoList().size() - 1);
		int undoNumOfTimes = rollbackItem.getTimes();
		ArrayList<Task> redoTasksList = new ArrayList<Task>();
		for (int i = 0; i < rollbackItem.getTimes(); i++) {
			REDO_TYPE redoType = getRedoType(rollbackItem.getCommandType());
			redoAddCommand(listsManager, rollbackItem, redoType, undoNumOfTimes);
			redoDeleteCommand(listsManager, rollbackItem, redoType, undoNumOfTimes);
			redoEditCommand(listsManager, rollbackItem, redoType, undoNumOfTimes);
			redoDoneCommand(listsManager, rollbackItem, redoType, undoNumOfTimes);
			redoUndoneCommand(listsManager, rollbackItem, redoType, undoNumOfTimes);
			redoTasksList.add(rollbackItem.getNewTask());
			historyManager.getUndoList().add(rollbackItem);
			updateListsManager(listsManager, rollbackItem.getNewTask());
			if (i < rollbackItem.getTimes() - 1) {
				rollbackItem = historyManager.getRedoList().remove(historyManager.getRedoList().size() - 1);
			}
		}
		for (int i = 0; i < redoTasksList.size(); i++) {
			Task doneTask = redoTasksList.get(i);
			listsManager.updateIndexList(doneTask);
		}
	}

	private void updateListsManager(ListsManager listsManager, Task newTask) {
		listsManager.getSelectedTagsList().clear();
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
		listsManager.updateLists();
	}

	private void redoUndoneCommand(ListsManager listsManager, RollbackItem rollbackItem,
			REDO_TYPE undoType, int undoNumOfTimes) {
		if (undoType.equals(REDO_TYPE.UNDONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(DONE);
			Task tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_REDO_UNDONE_COMMAND, rollbackItem.getNewTask().getDescription());
			} else {
				_message = MESSAGE_REDO_MULTIPLE_UNDONE_COMMAND;
			}
		}
	}

	private void redoDoneCommand(ListsManager listsManager, RollbackItem rollbackItem,
			REDO_TYPE redoType, int undoNumOfTimes) {
		if (redoType.equals(REDO_TYPE.DONE)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(UNDONE);
			Task tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_REDO_DONE_COMMAND, rollbackItem.getNewTask().getDescription());
			} else {
				_message = MESSAGE_REDO_MULTIPLE_DONE_COMMAND;
			}
		}
	}

	private void redoEditCommand(ListsManager listsManager, RollbackItem rollbackItem,
			REDO_TYPE redoType, int undoNumOfTimes) {
		if (redoType.equals(REDO_TYPE.EDIT)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(EDIT);
			Task tempTask = rollbackItem.getNewTask();
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(tempTask);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_REDO_EDIT_COMMAND, rollbackItem.getNewTask().getDescription());
			} else {
				_message = MESSAGE_REDO_MULTIPLE_EDIT_COMMAND;
			}
		}
	}

	private void redoDeleteCommand(ListsManager listsManager, RollbackItem rollbackItem,
			REDO_TYPE redoType, int undoNumOfTimes) {
		if (redoType.equals(REDO_TYPE.DELETE)) {
			listsManager.getMainList().add(rollbackItem.getOldTask());
			rollbackItem.setCommandType(ADD);
			rollbackItem.setNewTask(rollbackItem.getOldTask());
			rollbackItem.setOldTask(null);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_REDO_DELETE_COMMAND, rollbackItem.getNewTask().getDescription());
			} else {
				_message = MESSAGE_REDO_MULTIPLE_DELETE_COMMAND;
			}
		}
	}

	private void redoAddCommand(ListsManager listsManager, RollbackItem rollbackItem, 
			REDO_TYPE redoType, int undoNumOfTimes) {
		if (redoType.equals(REDO_TYPE.ADD)) {
			listsManager.getMainList().remove(rollbackItem.getNewTask());
			rollbackItem.setCommandType(DELETE);
			rollbackItem.setOldTask(rollbackItem.getNewTask());
			rollbackItem.setNewTask(null);
			if (undoNumOfTimes == 1) {
				_message = String.format(MESSAGE_REDO_ADD_COMMAND, rollbackItem.getOldTask().getDescription());
			} else {
				_message = MESSAGE_REDO_MULTIPLE_ADD_COMMAND;
			}
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

	public String getMessage() {
		return _message;
	}

	public void setMessage(String message) {
		_message = message;
	}
}
