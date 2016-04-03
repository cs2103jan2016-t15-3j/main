package logic;

public class RollbackItem {
	private String _commandType;
	private int _times;
	private Task _oldTask;
	private Task _newTask;
	
	public RollbackItem(String commandType, Task oldTask, Task newTask) {
		_commandType = commandType;
		_times = 1;
		_oldTask = oldTask;
		_newTask = newTask;
	}
	
	public RollbackItem(String commandType, Task oldTask, Task newTask, int times) {
		_commandType = commandType;
		_times = times;
		_oldTask = oldTask;
		_newTask = newTask;
	}
	
	public void setCommandType(String commandType) {
		_commandType = commandType;
	}
	
	public void setTimes(int times) {
		_times = times;
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
	
	public int getTimes() {
		return _times;
	}
	
	public Task getOldTask() {
		return _oldTask;
	}
	
	public Task getNewTask() {
		return _newTask;
	}
}
