package logic;

public class RollbackItem {
	private String _commandType;
	private Task _oldTask;
	private Task _newTask;
	
	public RollbackItem(String commandType, Task oldTask, Task newTask) {
		_commandType = commandType;
		_oldTask = oldTask;
		_newTask = newTask;
	}
	
	public void setCommandType(String commandType) {
		_commandType = commandType;
	}
	
	public void setOldTask(Task oldTask) {
		_oldTask = oldTask;
	}
	
	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}
	
	public String getCommandType() {
		return _commandType;
	}
	
	public Task getOldTask() {
		return _oldTask;
	}
	
	public Task getNewTask() {
		return _newTask;
	}
}
