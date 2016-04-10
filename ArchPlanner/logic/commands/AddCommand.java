package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Logic.COMMAND_TYPE;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

/**
 * This class is used to execute add command.
 * 
 * @@author A0140021J
 *
 */
public class AddCommand implements CommandInterface {

	//This is the taskParameters of an AddCommand object.
	private TaskParameters _taskParameters;
	
	//This is the message of an AddCommand object to be displayed.
	private String _message;
	
	//This constant string variable is the standard format for display message upon adding of task successfully.
	private final String MESSAGE_ADD_COMMAND = "added \"%1$s\"";
	
	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";

	//This is constructor of the class.
	public AddCommand(TaskParameters newTaskParameters) {
		_taskParameters = new TaskParameters();
		_taskParameters = newTaskParameters;
		_message = STRING_EMPTY;
	}
	
	/**
	 * This is setter method for AddCommand's taskParameters.
	 * 
	 * @param taskParameters This will be the taskParameters of the AddCommand.
	 */
	public void setTaskParameters(Task taskParameters) {
        _taskParameters = taskParameters;
    }
	
	/**
	 * This is setter method for AddCommand's message.
	 * 
	 * @param message This will be the message of the AddCommand.
	 */
	public void setMessage(String message) {
        _message = message;
    }
	
	/**
	 * This is getter method for AddCommand's message.
	 * 
	 * @return message.
	 */
	public String getMessage() {
        return _message;
    }
	
	/**
	 * This is getter method for AddCommand's taskParameters.
	 * 
	 * @return taskParameters.
	 */
	public TaskParameters getTaskParameters() {
        return _taskParameters;
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
	 * This method is used to execute add command and add the task followed by updating the relevant lists.
	 */
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		clearIndexList(listsManager);
		Task newTask = new Task(getTaskParameters().getDescription(), getTaskParameters().getTagsList(), 
				getTaskParameters().getStartDate(), getTaskParameters().getStartTime(), 
				getTaskParameters().getEndDate(), getTaskParameters().getEndTime());
		setMessage(String.format(MESSAGE_ADD_COMMAND, getTaskParameters().getDescription()));
		updateManagers(listsManager, historyManager, newTask);
		return null;
	}

	/**
	 * This method is used to update ListsManager and HistoryManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param historyManager This is the HistoryManager.
	 * 
	 * @param newTask This is the task to be added.
	 */
	private void updateManagers(ListsManager listsManager, HistoryManager historyManager, Task newTask) {
		updateListsManager(listsManager, newTask);
		updateHistoryManager(historyManager, newTask);
	}

	/**
	 * This method is used to clear the index list in ListsManager.
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
	 * @param newTask This is the task to be added.
	 */
	private void updateHistoryManager(HistoryManager historyManager, Task newTask) {
		RollbackItem rollbackItem = new RollbackItem(COMMAND_TYPE.ADD, null, newTask);
		historyManager.getUndoList().add(rollbackItem);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
	}

	/**
	 * This method is used to update ListsManager.
	 * 
	 * @param listsManager This is the ListsManager.
	 * 
	 * @param newTask This is the task to be added.
	 */
	private void updateListsManager(ListsManager listsManager, Task newTask) {
		listsManager.getMainList().add(newTask);
		listsManager.getSelectedTagsList().clear();
		listsManager.setViewType(VIEW_TYPE.ALL);
		listsManager.setCategoryType(CATEGORY_TYPE.ALL);
		listsManager.updateLists();
		listsManager.updateIndexList(newTask);
	}

	public TaskParameters getTask() {
		return _taskParameters;
	}
}