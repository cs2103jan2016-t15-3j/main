package logic;

import logic.Logic.COMMAND_TYPE;

/**
 * This class defines the the properties of a RollbackItem object
 * to support undo and redo commands.
 * 
 * @@author A0140021J
 *
 */
public class RollbackItem {
	
	//This variable is used to specify the command type of the RollbackItem object.
	private COMMAND_TYPE _commandType;
	
	//This is the number of times of rollback.
	private int _times;
	
	//This is the Task object of an old task before a command is executed.
	private Task _oldTask;
	
	//This is the Task object of a new task after a command is executed.
	private Task _newTask;
	
	//This is constructor of the class.
	public RollbackItem(COMMAND_TYPE commandType, Task oldTask, Task newTask) {
		_commandType = commandType;
		_times = 1;
		_oldTask = oldTask;
		_newTask = newTask;
	}
	
	//This is constructor of the class.
	public RollbackItem(COMMAND_TYPE commandType, Task oldTask, Task newTask, int times) {
		_commandType = commandType;
		_times = times;
		_oldTask = oldTask;
		_newTask = newTask;
	}
	
	/**
	 * This is setter method for command type.
	 * 
	 * @param commandType	This will be the command Type of the RollbackItem.
	 */
	public void setCommandType(COMMAND_TYPE commandType) {
		_commandType = commandType;
	}
	
	/**
	 * This is setter method for times of rollback.
	 * 
	 * @param times	This will be the number of times of rollback of the RollbackItem.
	 */
	public void setTimes(int times) {
		_times = times;
	}
	
	/**
	 * This is setter method for old task.
	 * 
	 * @param oldTask	This will be the oldTask of the RollbackItem.
	 */
	public void setOldTask(Task oldTask) {
		_oldTask = oldTask;
	}
	
	/**
	 * This is setter method for new task.
	 * 
	 * @param newTask	This will be newTask of the RollbackItem.
	 */
	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}
	
	/**
	 * This is getter method for command type.
	 * 
	 * @return	command type.
	 */
	public COMMAND_TYPE getCommandType() {
		return _commandType;
	}
	
	/**
	 * This is getter method for times of rollback.
	 * 
	 * @return	number of times of rollback.
	 */
	public int getTimes() {
		return _times;
	}
	
	/**
	 * This is getter method for old task.
	 * 
	 * @return	old task.
	 */
	public Task getOldTask() {
		return _oldTask;
	}
	
	/**
	 * This is getter method for new task.
	 * 
	 * @return	new task.
	 */
	public Task getNewTask() {
		return _newTask;
	}
}
