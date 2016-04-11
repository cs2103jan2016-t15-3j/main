package logic.commands;

import java.util.ArrayList;
import java.util.logging.Logger;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Logic;
import logic.Logic.COMMAND_TYPE;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

/**
 * This class is used to execute undo command.
 * 
 * @@author A0140021J
 *
 */
public class UndoCommand implements CommandInterface {

	//This is the logger used to log and observe the changes when program runs.
	static Logger log = Logger.getLogger(Logic.class.getName());

	//This is the number of times of undo.
	private int _times;

	//This is the message of an UndoCommand object to be displayed.
	private String _message;

	//These constant string variables are the standard format for display message when tasks are undo-ed successfully.
	private final String MESSAGE_UNDO_ADD_COMMAND = "deleted \"%1$s\"";
	private final String MESSAGE_UNDO_DELETE_COMMAND = "added \"%1$s\"";
	private final String MESSAGE_UNDO_EDIT_COMMAND = "edited \"%1$s\"";
	private final String MESSAGE_UNDO_DONE_COMMAND = "undone \"%1$s\"";
	private final String MESSAGE_UNDO_UNDONE_COMMAND = "done \"%1$s\"";
	private final String MESSAGE_UNDO_MULTIPLE_ADD_COMMAND = "deleted multiple tasks";
	private final String MESSAGE_UNDO_MULTIPLE_DELETE_COMMAND = "added multiple tasks";
	private final String MESSAGE_UNDO_MULTIPLE_EDIT_COMMAND = "edited multiple tasks";
	private final String MESSAGE_UNDO_MULTIPLE_DONE_COMMAND = "undone multiple tasks";
	private final String MESSAGE_UNDO_MULTIPLE_UNDONE_COMMAND = "done multiple tasks";

	//These are constant string variables for logging.
	private final String  LOGGER_MESSAGE_EXECUTING_UNDO_COMMAND = "Executing undo command...";
	private final String  LOGGER_MESSAGE_COMPLETED_UNDO_COMMAND = "Completed undo command.";

	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";

	//This is constructor of the class.
	public UndoCommand() {
		_times = 1;
		_message = STRING_EMPTY;
	}

	//This is constructor of the class.
	public UndoCommand(int times) {
		_times = times;
		_message = STRING_EMPTY;
	}

	/**
	 * This is setter method for UndoCommand's times.
	 * 
	 * @param times This will be the times of the UndoCommand.
	 */
	public void setTimes(int times) {
		_times = times;
	}

	/**
	 * This is setter method for UndoCommand's message.
	 * 
	 * @param message This will be the message of the UndoCommand.
	 */
	public void setMessage(String message) {
		_message = message;
	}

	/**
	 * This is getter method for UndoCommand's times.
	 * 
	 * @return the number of times of undo.
	 */
	public int getTimes() {
		return _times;
	}

	/**
	 * This is getter method for UndoCommand's message.
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
	 * This method is used to execute undo command.
	 */
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		assert(getTimes() > 0 && getTimes() <= historyManager.getUndoList().size());
		
		log.info(LOGGER_MESSAGE_EXECUTING_UNDO_COMMAND);
		clearIndexList(listsManager);
		for (int i = 0; i < getTimes(); i++) {
			executeUndoCommand(listsManager, historyManager);
		}
		log.info(getMessage());
		log.info(LOGGER_MESSAGE_COMPLETED_UNDO_COMMAND);
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
	 * This method differentiate the command type and execute the undo command based on the command type, followed by
	 * updating the relevant lists.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param historyManager This is the HistoryManager.
	 */
	private void executeUndoCommand(ListsManager listsManager, HistoryManager historyManager) {
		RollbackItem rollbackItem = new RollbackItem(null, null, null);
		rollbackItem = historyManager.getUndoList().remove(historyManager.getUndoList().size() - 1);
		int undoNumOfTimes = rollbackItem.getTimes();
		ArrayList<Task> undoTasksList = new ArrayList<Task>();
		for (int i = 0; i < undoNumOfTimes; i++) {
			COMMAND_TYPE commandType = rollbackItem.getCommandType();
			switch (commandType) {

			case ADD : 
				undoAddCommand(listsManager, rollbackItem, undoNumOfTimes);
				break;
			case DELETE : 
				undoDeleteCommand(listsManager, rollbackItem, undoNumOfTimes);
				break;
			case EDIT : 
				undoEditCommand(listsManager, rollbackItem, undoNumOfTimes);
				break;
			case DONE : 
				undoDoneCommand(listsManager, rollbackItem, undoNumOfTimes);
				break;
			case UNDONE : 
				undoUndoneCommand(listsManager, rollbackItem, undoNumOfTimes);
				break;
			default : 
				break;
			}
			undoTasksList.add(rollbackItem.getNewTask());
			historyManager.getRedoList().add(rollbackItem);
			updateListsManager(listsManager);
			if (i < rollbackItem.getTimes() - 1) {
				rollbackItem = historyManager.getUndoList().remove(historyManager.getUndoList().size() - 1);
			}
		}
		updateIndexList(listsManager, undoTasksList);
	}

	/**
	 * This method is used to update the indexList in ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param undoTasksList This is the list of tasks that are undo-ed.
	 */
	private void updateIndexList(ListsManager listsManager, ArrayList<Task> undoTasksList) {
		for (int i = 0; i < undoTasksList.size(); i++) {
			Task doneTask = undoTasksList.get(i);
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
	 * This method is used to undo an undone command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for undo command.
	 * 
	 * @param undoNumOfTimes This is the number of times of undo.
	 */
	private void undoUndoneCommand(ListsManager listsManager, RollbackItem rollbackItem, int undoNumOfTimes) {
		listsManager.getMainList().remove(rollbackItem.getNewTask());
		listsManager.getMainList().add(rollbackItem.getOldTask());
		rollbackItem.setCommandType(COMMAND_TYPE.DONE);
		Task tempTask = rollbackItem.getNewTask();
		rollbackItem.setNewTask(rollbackItem.getOldTask());
		rollbackItem.setOldTask(tempTask);
		String undoUndoneCommandMesage = String.format(MESSAGE_UNDO_UNDONE_COMMAND, rollbackItem.getNewTask().getDescription());
		updateMessage(undoUndoneCommandMesage, MESSAGE_UNDO_MULTIPLE_UNDONE_COMMAND, rollbackItem, undoNumOfTimes);
	}

	/**
	 * This method is used to undo a done command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for undo command.
	 * 
	 * @param undoNumOfTimes This is the number of times of undo.
	 */
	private void undoDoneCommand(ListsManager listsManager, RollbackItem rollbackItem, int undoNumOfTimes) {
		listsManager.getMainList().remove(rollbackItem.getNewTask());
		listsManager.getMainList().add(rollbackItem.getOldTask());
		rollbackItem.setCommandType(COMMAND_TYPE.UNDONE);
		Task tempTask = rollbackItem.getNewTask();
		rollbackItem.setNewTask(rollbackItem.getOldTask());
		rollbackItem.setOldTask(tempTask);
		String undoDoneCommandMesage = String.format(MESSAGE_UNDO_DONE_COMMAND, rollbackItem.getNewTask().getDescription());
		updateMessage(undoDoneCommandMesage, MESSAGE_UNDO_MULTIPLE_DONE_COMMAND, rollbackItem, undoNumOfTimes);
	}

	/**
	 * This method is used to undo a edit command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for undo command.
	 * 
	 * @param undoNumOfTimes This is the number of times of undo.
	 */
	private void undoEditCommand(ListsManager listsManager, RollbackItem rollbackItem, int undoNumOfTimes) {
		listsManager.getMainList().remove(rollbackItem.getNewTask());
		listsManager.getMainList().add(rollbackItem.getOldTask());
		rollbackItem.setCommandType(COMMAND_TYPE.EDIT);
		Task tempTask = rollbackItem.getNewTask();
		rollbackItem.setNewTask(rollbackItem.getOldTask());
		rollbackItem.setOldTask(tempTask);
		String undoEditCommandMesage = String.format(MESSAGE_UNDO_EDIT_COMMAND, rollbackItem.getNewTask().getDescription());
		updateMessage(undoEditCommandMesage, MESSAGE_UNDO_MULTIPLE_EDIT_COMMAND, rollbackItem, undoNumOfTimes);
	}

	/**
	 * This method is used to undo a delete command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for undo command.
	 * 
	 * @param undoNumOfTimes This is the number of times of undo.
	 */
	private void undoDeleteCommand(ListsManager listsManager, RollbackItem rollbackItem, int undoNumOfTimes) {
		listsManager.getMainList().add(rollbackItem.getOldTask());
		rollbackItem.setCommandType(COMMAND_TYPE.ADD);
		rollbackItem.setNewTask(rollbackItem.getOldTask());
		rollbackItem.setOldTask(null);
		String undoDeleteCommandMesage = String.format(MESSAGE_UNDO_DELETE_COMMAND, rollbackItem.getNewTask().getDescription());
		updateMessage(undoDeleteCommandMesage, MESSAGE_UNDO_MULTIPLE_DELETE_COMMAND, rollbackItem, undoNumOfTimes);
	}

	/**
	 * This method is used to undo a add command.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param rollbackItem This is the RollbackItem used for undo command.
	 * 
	 * @param undoNumOfTimes This is the number of times of undo.
	 */
	private void undoAddCommand(ListsManager listsManager, RollbackItem rollbackItem, int undoNumOfTimes) {
		listsManager.getMainList().remove(rollbackItem.getNewTask());
		rollbackItem.setCommandType(COMMAND_TYPE.DELETE);
		rollbackItem.setOldTask(rollbackItem.getNewTask());
		rollbackItem.setNewTask(null);
		String undoAddCommandMesage = String.format(MESSAGE_UNDO_ADD_COMMAND, rollbackItem.getOldTask().getDescription());
		updateMessage(undoAddCommandMesage, MESSAGE_UNDO_MULTIPLE_ADD_COMMAND, rollbackItem, undoNumOfTimes);
	}

	/**
	 * This method is used to update and set the message of UndoCommand.
	 * 
	 * @param undoOneTaskMessage This will be the message of one task been undo-ed.
	 * 
	 * @param undoMultipleTasksMessage This will be the message of multiple tasks been undo-ed.
	 * 
	 * @param rollbackItem This is the RollbackItem used for redo command.
	 * 
	 * @param undoNumOfTimes This is the number of times of undo.
	 */
	private void updateMessage(String undoOneTaskMessage, String undoMultipleTasksMessage, RollbackItem rollbackItem, int undoNumOfTimes) {
		if (undoNumOfTimes == 1) {
			setMessage(undoOneTaskMessage);
		} else {
			setMessage(undoMultipleTasksMessage);
		}
	}
}
