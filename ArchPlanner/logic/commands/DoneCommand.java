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
 * This class is used to execute done command.
 * 
 * @@author A0140021J
 *
 */
public class DoneCommand implements CommandInterface {

	//This is the logger used to log and observe the changes when program runs.
	static Logger log = Logger.getLogger(Logic.class.getName());

	//This is the first index of task in the list to be done.
	private int _firstIndex;

	//This is the last index of task in the list to be done.
	private int _lastIndex;

	//This is the message of an DoneCommand object to be displayed.
	private String _message;

	//These constant string variables are the standard format for display message when tasks are set to done successfully.
	private final String MESSAGE_DONE_COMMAND = "done \"%1$s\"";
	private final String MESSAGE_MULTIPLE_DONE_COMMAND = "done multiple tasks";

	//These are constant string variables for logging.
	private final String  LOGGER_MESSAGE_EXECUTING_DONE_COMMAND = "Executing done command...";
	private final String  LOGGER_MESSAGE_COMPLETED_DONE_COMMAND = "Completed done command.";

	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";

	//This is constructor of the class.
	public DoneCommand(int index) {
		_firstIndex = index - 1;
		_lastIndex = index - 1;
		_message = STRING_EMPTY;
	}

	//This is constructor of the class.
	public DoneCommand(int firstIndex, int lastIndex) {
		_firstIndex = firstIndex - 1;
		_lastIndex = lastIndex - 1;
		_message = STRING_EMPTY;
	}

	/**
	 * This is setter method for DoneCommand's firstIndex.
	 * 
	 * @param index This will be the firstIndex of the DoneCommand.
	 */
	public void setFirstIndex(int index) {
		_firstIndex = index;
	}

	/**
	 * This is setter method for DoneCommand's lastIndex.
	 * 
	 * @param index This will be the lastIndex of the DoneCommand.
	 */
	public void setLastIndex(int index) {
		_lastIndex = index;
	}

	/**
	 * This is setter method for DoneCommand's message.
	 * 
	 * @param message This will be the message of the DoneCommand.
	 */
	public void setMessage(String message) {
		_message = message;
	}

	/**
	 * This is getter method for DoneCommand's firstIndex.
	 * 
	 * @return firstIndex.
	 */
	public int getFirstIndex() {
		return _firstIndex;
	}

	/**
	 * This is getter command for DoneCommand's lastIndex.
	 * 
	 * @return lastIndex.
	 */
	public int getLastIndex() {
		return _lastIndex;
	}

	/**
	 * This is getter command for DoneCommand's message.
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
	 * This method is used to execute done command and the task will be set to done, followed by updating the relevant lists.
	 */
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		assert(getFirstIndex() >= 0 && getFirstIndex() < listsManager.getViewList().size());
		assert(getLastIndex() >= 0 && getLastIndex() < listsManager.getViewList().size());

		log.info(LOGGER_MESSAGE_EXECUTING_DONE_COMMAND);
		clearIndexList(listsManager);

		int numOfDone = getLastIndex() - getFirstIndex() + 1;
		Task oldTask = null;
		ArrayList<Task> doneTasksList = new ArrayList<Task>();
		for (int i = 0; i < numOfDone; i++) {
			oldTask = listsManager.getViewList().get(_firstIndex);
			listsManager.getMainList().remove(oldTask);
			Task newTask = new Task();
			initializeNewTask(oldTask, newTask);
			doneTasksList.add(newTask);
			updateManagers(listsManager, historyManager, numOfDone, oldTask, newTask);
		}
		updateIndexList(listsManager, doneTasksList);
		updateMessage(numOfDone, oldTask);
		log.info(getMessage());
		log.info(LOGGER_MESSAGE_COMPLETED_DONE_COMMAND);
		return null;

	}

	/**
	 * This method is used to update DoneCommand's message.
	 * 
	 * @param numOfDone This is the number of tasks to be set to done.
	 * 
	 * @param oldTask This is the task before been set to done.
	 */
	private void updateMessage(int numOfDone, Task oldTask) {
		if (numOfDone == 1) {
			setMessage(String.format(MESSAGE_DONE_COMMAND, oldTask.getDescription()));
		} else {
			setMessage(MESSAGE_MULTIPLE_DONE_COMMAND);
		}
	}

	/**
	 * This method is used to update indexList in ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param doneTasksList This is the list of tasks set to done.
	 */
	private void updateIndexList(ListsManager listsManager, ArrayList<Task> doneTasksList) {
		for (int i = 0; i < doneTasksList.size(); i++) {
			Task doneTask = doneTasksList.get(i);
			listsManager.updateIndexList(doneTask);
		}
	}

	/**
	 * This method is used to update ListsManager and HistoryManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param historyManager This is the HistoryManager.
	 * 
	 * @param numOfDone This is the number of tasks to be set to done.
	 * 
	 * @param oldTask This is the task before been set to done.
	 * 
	 * @param newTask This is the task after been set to done.
	 */
	private void updateManagers(ListsManager listsManager, HistoryManager historyManager, int numOfDone, Task oldTask,
			Task newTask) {
		updateListsManager(listsManager, newTask);
		updateHistoryManager(historyManager, oldTask, newTask, numOfDone);
	}

	/**
	 * This method is used to clear indexList in ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 */
	private void clearIndexList(ListsManager listsManager) {
		listsManager.getIndexList().clear();
	}

	/**
	 * This method is used to update the HistoryManager.
	 * 
	 * @param historyManager This is the HistoryManager.
	 * 
	 * @param oldTask This is the task before been set to done.
	 * 
	 * @param newTask This is the task after been set to done.
	 * 
	 * @param numOfDone This is the number of tasks to be set to done.
	 */
	private void updateHistoryManager(HistoryManager historyManager, Task oldTask, Task newTask, int numOfDone) {
		RollbackItem rollbackItem = new RollbackItem(COMMAND_TYPE.DONE, oldTask, newTask, numOfDone);
		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
	}

	/**
	 * This method is used to update ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param newTask This is the task to be set to done.
	 */
	private void updateListsManager(ListsManager listsManager, Task newTask) {
		listsManager.getMainList().add(newTask);
		listsManager.updateLists();
	}

	/**
	 * This method is used to initialize the new task with the properties of old task but done status is set to true.
	 * 
	 * @param oldTask This is the task before been set to done.
	 * 
	 * @param newTask This is the task after been set to done.
	 */
	private void initializeNewTask(Task oldTask, Task newTask) {
		newTask.setDescription(oldTask.getDescription());
		newTask.setTagsList(oldTask.getTagsList());
		newTask.setStartDate(oldTask.getStartDate());
		newTask.setStartTime(oldTask.getStartTime());
		newTask.setEndDate(oldTask.getEndDate());
		newTask.setEndTime(oldTask.getEndTime());
		newTask.setIsOverdue(oldTask.getIsOverdue());
		newTask.setIsDone(true);
	}
}
