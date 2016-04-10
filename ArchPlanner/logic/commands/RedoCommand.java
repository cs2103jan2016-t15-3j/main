package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Logic.COMMAND_TYPE;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

/**
 * This class is used to execute redo command.
 * 
 * @@author A0140021J
 *
 */
public class RedoCommand implements CommandInterface {

	//This is the number of times of redo.
	private int _times;

	//This is the message of an RedoCommand object to be displayed.
	private String _message;

	//These constant string variables are the standard format for display message when tasks are redo-ed successfully.
	private final String MESSAGE_REDO_ADD_COMMAND = "deleted \"%1$s\"";
	private final String MESSAGE_REDO_DELETE_COMMAND = "added \"%1$s\"";
	private final String MESSAGE_REDO_EDIT_COMMAND = "edited \"%1$s\"";
	private final String MESSAGE_REDO_DONE_COMMAND = "undone \"%1$s\"";
	private final String MESSAGE_REDO_UNDONE_COMMAND = "done \"%1$s\"";
	private final String MESSAGE_REDO_MULTIPLE_ADD_COMMAND = "deleted multiple tasks";
	private final String MESSAGE_REDO_MULTIPLE_DELETE_COMMAND = "added multiple tasks";
	private final String MESSAGE_REDO_MULTIPLE_EDIT_COMMAND = "edited multiple tasks";
	private final String MESSAGE_REDO_MULTIPLE_DONE_COMMAND = "undone multiple tasks";
	private final String MESSAGE_REDO_MULTIPLE_UNDONE_COMMAND = "done multiple tasks";

	//This constant string variable is used to append messages for readability.
	private final String EMPTY_STRING = "";

	//This is constructor of the class.
	public RedoCommand() {
		_times = 1;
		_message = EMPTY_STRING;
	}

	//This is constructor of the class.
	public RedoCommand(int times) {
		_times = times;
		_message = EMPTY_STRING;
	}

	/**
	 * This is setter method for RedoCommand's times.
	 * 
	 * @param times This will be the times of the RedoCommand.
	 */
	public void setTimes(int times) {
		_times = times;
	}

	/**
	 * This is setter method for RedoCommand's message.
	 * 
	 * @param message This will be the message of the RedoCommand.
	 */
	public void setMessage(String message) {
		_message = message;
	}

	/**
	 * This is getter method for RedoCommand's times.
	 * 
	 * @return the number of times of redo.
	 */
	public int getTimes() {
		return _times;
	}

	/**
	 * This is getter method for RedoCommand's message.
	 * 
	 * @return message.
	 */
	public String getMessage() {
		return _message;
	}

	/**
	 * This method will not be called.
	 */
	public CommandInterface execute() {
		return null;
	}


	/**
	 * This method will not be called.
	 */
	public CommandInterface execute(ListsManager listsManager, Storage storage) {
		return null;
	}

	/**
	 * This method is used to execute redo command.
	 */
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		assert(getTimes() > 0 && getTimes() <= historyManager.getRedoList().size());

		clearIndexList(listsManager);

		for (int i = 0; i < getTimes(); i++) {
			executeRedoCommand(listsManager, historyManager);
		}
		return null;
	}

	/**
	 * This method is used to clear the indexList in the ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 */
	private void clearIndexList(ListsManager listsManager) {
		listsManager.getIndexList().clear();
	}

	/**
	 * This method differentiate the command type and execute the redo command based on the command type, followed by
	 * updating the relevant lists.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param historyManager This is the HistoryManager.
	 */
	private void executeRedoCommand(ListsManager listsManager, HistoryManager historyManager) {
		RollbackItem rollbackItem = new RollbackItem(null, null, null);
		rollbackItem = historyManager.getRedoList().remove(historyManager.getRedoList().size() - 1);
		int redoNumOfTimes = rollbackItem.getTimes();
		ArrayList<Task> redoTasksList = new ArrayList<Task>();

		for (int i = 0; i < rollbackItem.getTimes(); i++) {
			COMMAND_TYPE commandType = rollbackItem.getCommandType();

			switch (commandType) {

			case ADD : 
				redoAddCommand(listsManager, rollbackItem, redoNumOfTimes);
				break;
			case DELETE : 
				redoDeleteCommand(listsManager, rollbackItem, redoNumOfTimes);
				break;
			case EDIT : 
				redoEditCommand(listsManager, rollbackItem, redoNumOfTimes);
				break;
			case DONE : 
				redoDoneCommand(listsManager, rollbackItem, redoNumOfTimes);
				break;
			case UNDONE : 
				redoUndoneCommand(listsManager, rollbackItem, redoNumOfTimes);
				break;
			default : 
				break;
			}
			redoTasksList.add(rollbackItem.getNewTask());
			historyManager.getUndoList().add(rollbackItem);
			updateListsManager(listsManager);
			if (i < rollbackItem.getTimes() - 1) {
				rollbackItem = historyManager.getRedoList().remove(historyManager.getRedoList().size() - 1);
			}
		}
		updateIndexList(listsManager, redoTasksList);
	}

	/**
	 * This method is used to update the indexList in ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param redoTasksList This is the list of tasks that are redo-ed.
	 */
	private void updateIndexList(ListsManager listsManager, ArrayList<Task> redoTasksList) {
		for (int i = 0; i < redoTasksList.size(); i++) {
			Task doneTask = redoTasksList.get(i);
			listsManager.updateIndexList(doneTask);
		}
	}

	/**
	 * This method is used to update ListsManager.
	 * 
	 * @param listsManager this is the ListsManager.
	 */
	private void updateListsManager(ListsManager listsManager) {
		listsManager.getSelectedTagsList().clear();
		listsManager.setViewType(VIEW_TYPE.ALL);
		listsManager.setCategoryType(CATEGORY_TYPE.ALL);
		listsManager.updateLists();
	}

	/**
	 * This method is used to redo an undone command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for redo command.
	 * 
	 * @param redoNumOfTimes This is the number of times of redo.
	 */
	private void redoUndoneCommand(ListsManager listsManager, RollbackItem rollbackItem, int redoNumOfTimes) {
		listsManager.getMainList().remove(rollbackItem.getNewTask());
		listsManager.getMainList().add(rollbackItem.getOldTask());
		rollbackItem.setCommandType(COMMAND_TYPE.DONE);
		Task tempTask = rollbackItem.getNewTask();
		rollbackItem.setNewTask(rollbackItem.getOldTask());
		rollbackItem.setOldTask(tempTask);
		String redoUndoneCommandMessage = String.format(MESSAGE_REDO_UNDONE_COMMAND, rollbackItem.getNewTask().getDescription());
		updateMessage(redoUndoneCommandMessage, MESSAGE_REDO_MULTIPLE_UNDONE_COMMAND, rollbackItem, redoNumOfTimes);
	}

	/**
	 * This method is used to redo a done command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for redo command.
	 * 
	 * @param redoNumOfTimes This is the number of times of redo.
	 */
	private void redoDoneCommand(ListsManager listsManager, RollbackItem rollbackItem, int redoNumOfTimes) {
		listsManager.getMainList().remove(rollbackItem.getNewTask());
		listsManager.getMainList().add(rollbackItem.getOldTask());
		rollbackItem.setCommandType(COMMAND_TYPE.UNDONE);
		Task tempTask = rollbackItem.getNewTask();
		rollbackItem.setNewTask(rollbackItem.getOldTask());
		rollbackItem.setOldTask(tempTask);
		String redoDoneCommandMessage = String.format(MESSAGE_REDO_DONE_COMMAND, rollbackItem.getNewTask().getDescription());
		updateMessage(redoDoneCommandMessage, MESSAGE_REDO_MULTIPLE_DONE_COMMAND, rollbackItem, redoNumOfTimes);
	}

	/**
	 * This method is used to redo a edit command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for redo command.
	 * 
	 * @param redoNumOfTimes This is the number of times of redo.
	 */
	private void redoEditCommand(ListsManager listsManager, RollbackItem rollbackItem, int redoNumOfTimes) {
		listsManager.getMainList().remove(rollbackItem.getNewTask());
		listsManager.getMainList().add(rollbackItem.getOldTask());
		rollbackItem.setCommandType(COMMAND_TYPE.EDIT);
		Task tempTask = rollbackItem.getNewTask();
		rollbackItem.setNewTask(rollbackItem.getOldTask());
		rollbackItem.setOldTask(tempTask);
		String redoEditCommandMessage = String.format(MESSAGE_REDO_EDIT_COMMAND, rollbackItem.getNewTask().getDescription());
		updateMessage(redoEditCommandMessage, MESSAGE_REDO_MULTIPLE_EDIT_COMMAND, rollbackItem, redoNumOfTimes);
	}

	/**
	 * This method is used to redo a delete command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for redo command.
	 * 
	 * @param redoNumOfTimes This is the number of times of redo.
	 */
	private void redoDeleteCommand(ListsManager listsManager, RollbackItem rollbackItem, int redoNumOfTimes) {
		listsManager.getMainList().add(rollbackItem.getOldTask());
		rollbackItem.setCommandType(COMMAND_TYPE.ADD);
		rollbackItem.setNewTask(rollbackItem.getOldTask());
		rollbackItem.setOldTask(null);
		String redoDeleteCommandMessage = String.format(MESSAGE_REDO_DELETE_COMMAND, rollbackItem.getNewTask().getDescription());
		updateMessage(redoDeleteCommandMessage, MESSAGE_REDO_MULTIPLE_DELETE_COMMAND, rollbackItem, redoNumOfTimes);
	}

	/**
	 * This method is used to redo a add command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for redo command.
	 * 
	 * @param redoNumOfTimes This is the number of times of redo.
	 */
	private void redoAddCommand(ListsManager listsManager, RollbackItem rollbackItem, int redoNumOfTimes) {
		listsManager.getMainList().remove(rollbackItem.getNewTask());
		rollbackItem.setCommandType(COMMAND_TYPE.DELETE);
		rollbackItem.setOldTask(rollbackItem.getNewTask());
		rollbackItem.setNewTask(null);
		String redoAddCommandMessage = String.format(MESSAGE_REDO_ADD_COMMAND, rollbackItem.getOldTask().getDescription());
		updateMessage(redoAddCommandMessage, MESSAGE_REDO_MULTIPLE_ADD_COMMAND, rollbackItem, redoNumOfTimes);
	}

	/**
	 * This method is used to update and set the message of RedoCommand.
	 * 
	 * @param redoOneTaskMessage This will be the message of one task been redo-ed.
	 * 
	 * @param redoMultipleTasksMessage This will be the message of multiple tasks been redo-ed.
	 * 
	 * @param rollbackItem This is the RollbackItem used for redo command.
	 * 
	 * @param redoNumOfTimes This is the number of times of redo.
	 */
	private void updateMessage(String redoOneTaskMessage, String redoMultipleTasksMessage, RollbackItem rollbackItem, int redoNumOfTimes) {
		if (redoNumOfTimes == 1) {
			setMessage(redoOneTaskMessage);
		} else {
			setMessage(redoMultipleTasksMessage);
		}
	}
}
