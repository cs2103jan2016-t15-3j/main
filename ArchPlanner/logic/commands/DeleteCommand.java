package logic.commands;

import java.util.ArrayList;
import java.util.logging.Logger;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Logic;
import logic.Logic.COMMAND_TYPE;
import logic.RollbackItem;
import logic.Task;
import storage.Storage;

/**
 * This class is used to execute delete command.
 * 
 * @@author A0140021J
 *
 */
public class DeleteCommand implements CommandInterface {

	//This is the logger used to log and observe the changes when program runs.
	static Logger log = Logger.getLogger(Logic.class.getName());

	//This is the first index of task in the list to be deleted.
	private int _firstIndex;

	//This is the last index of task in the list to be deleted.
	private int _lastIndex;

	//This is the message of an DeleteCommand object to be displayed.
	private String _message;

	//These constant string variables are the standard format for display message upon deleting of tasks successfully.
	private final String MESSAGE_DELETE_COMMAND = "deleted \"%1$s\"";
	private final String MESSAGE_MULTIPLE_DELETE_COMMAND = "deleted multiple tasks";

	//These are constant string variables for logging.
	private final String  LOGGER_MESSAGE_EXECUTING_DELETE_COMMAND = "Executing delete command...";
	private final String  LOGGER_MESSAGE_COMPLETED_DELETE_COMMAND = "Completed delete command.";

	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";

	//This is constructor of the class.
	public DeleteCommand(int index) {
		_firstIndex = index - 1;
		_lastIndex = index - 1;
		_message = STRING_EMPTY;
	}

	//This is constructor of the class.
	public DeleteCommand(int firstIndex, int lastIndex) {
		_firstIndex = firstIndex - 1;
		_lastIndex = lastIndex - 1;
		_message = "";
	}

	/**
	 * This is setter method for DeleteCommand's firstIndex.
	 * 
	 * @param index	This will be firstIndex of the DeleteCommand.
	 */
	public void setFirstIndex(int index) {
		_firstIndex = index;
	}

	/**
	 *  This is setter method for DeleteCommand's lastIndex.
	 *  
	 * @param index	This will be lastIndex of the DeleteCommand.
	 */
	public void setLastIndex(int index) {
		_lastIndex = index;
	}

	/**
	 * This is setter method for DeleteCommand's message.
	 * 
	 * @param message	This will be the message of the DeleteCommand.
	 */
	public void setMessage(String message) {
		_message = message;
	}

	/**
	 * This is getter method for DeleteCommand's firstIndex.
	 * 
	 * @return	firstIndex.
	 */
	public int getFirstIndex() {
		return _firstIndex;
	}

	/**
	 * This is getter method for DeleteCommand's lastIndex.
	 * 
	 * @return	lastIndex.
	 */
	public int getLastIndex() {
		return _lastIndex;
	}

	/**
	 * This is getter method for DeleteCommand's message.
	 * 
	 * @return	message.
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
	 * This method is used to execute delete command and delete the task followed by updating the relevant lists.
	 */
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		assert(getFirstIndex() >= 0 && getFirstIndex() < listsManager.getViewList().size());
		assert(getLastIndex() >= 0 && getLastIndex() < listsManager.getViewList().size());

		log.info(LOGGER_MESSAGE_EXECUTING_DELETE_COMMAND);
		clearIndexList(listsManager);

		Task oldTask = null;
		int numOfDeletion = getLastIndex() - getFirstIndex() + 1;

		for (int i = 0; i < numOfDeletion; i++) {
			oldTask = listsManager.getViewList().get(getFirstIndex());
			updateManagers(listsManager, historyManager, oldTask, numOfDeletion);
		}
		updateMessage(oldTask, numOfDeletion);
		log.info(getMessage());
		log.info(LOGGER_MESSAGE_COMPLETED_DELETE_COMMAND);
		return null;
	}

	/**
	 * This method is used to update message of the DeleteCommand upon deleting of tasks.
	 * 
	 * @param oldTask		This is the task to be deleted.
	 * @param numOfDeletion	This is the number of tasks to be deleted.
	 */
	private void updateMessage(Task oldTask, int numOfDeletion) {
		if (numOfDeletion == 1) {
			setMessage(String.format(MESSAGE_DELETE_COMMAND, oldTask.getDescription()));
		} else {
			setMessage(MESSAGE_MULTIPLE_DELETE_COMMAND);
		}
	}

	/**
	 * This method is used to update the ListsManager and HistoryManager.
	 * 
	 * @param listsManager		This is the ListsManager.
	 * @param historyManager	This is the HistoryManager.
	 * @param oldTask			This is the task to be deleted.
	 * @param numOfDeletion		This is the number of tasks to be deleted.
	 */
	private void updateManagers(ListsManager listsManager, HistoryManager historyManager, Task oldTask,
			int numOfDeletion) {
		updateListsManager(listsManager, oldTask);
		updateHistoryManager(historyManager, oldTask, numOfDeletion);
	}

	/**
	 * This method is used to clear indexList in ListsManager.
	 * 
	 * @param listsManager	This is the ListsManager.
	 */
	private void clearIndexList(ListsManager listsManager) {
		listsManager.getIndexList().clear();
	}

	/**
	 * This method is used to update HistoryManager.
	 * 
	 * @param historyManager	This is the HistoryManager.
	 * @param oldTask			This is the task to be deleted.
	 * @param numOfDeletion		This is the number of tasks to be deleted.
	 */
	private void updateHistoryManager(HistoryManager historyManager, Task oldTask, int numOfDeletion) {
		RollbackItem rollbackItem = new RollbackItem(COMMAND_TYPE.DELETE, oldTask, null, numOfDeletion);
		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
	}

	/**
	 * This method is used to update ListsManager.
	 * 
	 * @param listsManager	This is the ListsManager.
	 * @param oldTask		This is the task to be deleted.
	 */
	private void updateListsManager(ListsManager listsManager, Task oldTask) {
		listsManager.getMainList().remove(oldTask);
		listsManager.updateLists();
	}
}
