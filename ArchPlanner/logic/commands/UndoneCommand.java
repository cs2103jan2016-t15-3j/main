package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Logic.COMMAND_TYPE;
import logic.RollbackItem;
import logic.Task;
import storage.Storage;

/**
 * This class is used to execute undone command.
 * 
 * @@author A0140021J
 *
 */
public class UndoneCommand implements CommandInterface {

	//This is the first index of task in the list to be undone.
	private int _firstIndex;
	
	//This is the last index of task in the list to be undone.
	private int _lastIndex;
	
	//This is the message of an UndoneCommand object to be displayed.
	private String _message;

	//These constant string variables are the standard format for display message when tasks are set to undone successfully.
	private final String MESSAGE_UNDONE_COMMAND = "undone \"%1$s\"";
	private final String MESSAGE_MULTIPLE_UNDONE_COMMAND = "undone multiple tasks";
	
	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";

	//This is constructor of the class.
	public UndoneCommand(int index) {
		_firstIndex = index - 1;
		_lastIndex = index - 1;
		_message = STRING_EMPTY;
	}

	//This is constructor of the class.
	public UndoneCommand(int firstIndex, int lastIndex) {
		_firstIndex = firstIndex - 1;
		_lastIndex = lastIndex - 1;
		_message = STRING_EMPTY;
	}

	/**
	 * This is setter method for UndoneCommand's firstIndex.
	 * 
	 * @param index This will be the firstIndex of the UndoneCommand.
	 */
	public void setFirstIndex(int index) {
		_firstIndex = index;
	}
	
	/**
	 * This is setter method for UndoneCommand's lastIndex.
	 * 
	 * @param index This will be the lastIndex of the UndoneCommand.
	 */
	public void setLastIndex(int index) {
		_lastIndex = index;
	}
	
	/**
	 * This is setter method for UndoneCommand's message.
	 * 
	 * @param message This will be the message of the UndoneCommand.
	 */
	public void setMessage(String message) {
        _message = message;
    }

	/**
	 * This is getter method for UndoneCommand's firstIndex.
	 * 
	 * @return firstIndex.
	 */
	public int getFirstIndex() {
		return _firstIndex;
	}

	/**
	 * This is getter method for UndoneCommand's lastIndex.
	 * 
	 * @return lastIndex.
	 */
	public int getLastIndex() {
		return _lastIndex;
	}
	
	/**
	 * This is getter command for UndoneCommand's message.
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
	 * This method is used to execute undone command.
	 */
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		assert(getFirstIndex() >= 0 && getFirstIndex() < listsManager.getViewList().size());
		assert(getLastIndex() >= 0 && getLastIndex() < listsManager.getViewList().size());
		
		clearIndexList(listsManager);
		
		int numOfUndone = getLastIndex() - getFirstIndex() + 1;
		Task oldTask = null;
		ArrayList<Task> undoneTasksList = new ArrayList<Task>();
		for (int i = 0; i < numOfUndone; i++) {
			oldTask = listsManager.getViewList().get(_lastIndex);
			listsManager.getMainList().remove(oldTask);
			Task newTask = new Task();
			initializeNewTask(oldTask, newTask);
			undoneTasksList.add(newTask);
			updateManagers(listsManager, historyManager, numOfUndone, oldTask, newTask);
		}
		for (int i = 0; i < undoneTasksList.size(); i++) {
			Task undoneTask = undoneTasksList.get(i);
			listsManager.updateIndexList(undoneTask);
		}
		updateMessage(numOfUndone, oldTask);
		return null;
	}

	/**
	 * This method is used to update UndoneCommand's message.
	 * 
	 * @param numOfUndone This is the number of tasks to be set to undone.
	 * 
	 * @param oldTask This is the task before been set to undone.
	 */
	private void updateMessage(int numOfUndone, Task oldTask) {
		if (numOfUndone == 1) {
			_message = String.format(MESSAGE_UNDONE_COMMAND, oldTask.getDescription());
		} else {
			_message = MESSAGE_MULTIPLE_UNDONE_COMMAND;
		}
	}

	/**
	 * This method is used to update ListsManager and HistoryManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param historyManager This is the HistoryManager.
	 * 
	 * @param numOfUndone This is the number of tasks to be set to undone.
	 * 
	 * @param oldTask This is the task before been set to undone.
	 * 
	 * @param newTask This is the task after been set to undone.
	 */
	private void updateManagers(ListsManager listsManager, HistoryManager historyManager, int numOfUndone, Task oldTask,
			Task newTask) {
		updateListsManager(listsManager, newTask);
		updateHistoryManager(historyManager, oldTask, newTask, numOfUndone);
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
	 * This method is used to update HistoryManager.
	 * 
	 * @param historyManager This is the HistoryManager.
	 * 
	 * @param oldTask This is the task before been set to undone.
	 * 
	 * @param newTask This is the task after been set to undone.
	 * 
	 * @param numOfUndone This is the number of tasks to be set to undone.
	 */
	private void updateHistoryManager(HistoryManager historyManager, Task oldTask, Task newTask, int numOfUndone) {
		RollbackItem rollbackItem = new RollbackItem(COMMAND_TYPE.UNDONE, oldTask, newTask, numOfUndone);
		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
	}

	/**
	 * This method is used to update ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param newTask This is the task after been set to undone.
	 */
	private void updateListsManager(ListsManager listsManager, Task newTask) {
		listsManager.getMainList().add(newTask);
		listsManager.updateLists();
	}

	/**
	 * This method is used to initialize the new task with the old task properties but done status is set to false.
	 * 
	 * @param oldTask This is the task before been set to undone.
	 * 
	 * @param newTask This is the task after been set to undone.
	 */
	private void initializeNewTask(Task oldTask, Task newTask) {
		newTask.setDescription(oldTask.getDescription());
		newTask.setTagsList(oldTask.getTagsList());
		newTask.setStartDate(oldTask.getStartDate());
		newTask.setStartTime(oldTask.getStartTime());
		newTask.setEndDate(oldTask.getEndDate());
		newTask.setEndTime(oldTask.getEndTime());
		newTask.setIsOverdue(oldTask.getIsOverdue());
		newTask.setIsDone(false);
	}
}
