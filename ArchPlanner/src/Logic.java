import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class interact with the UI and process the operation, 
 * followed by updating the changes to Storage
 * 
 * @author riyang
 *
 */
public class Logic {
	
	public ArrayList<Task> _mainList;
	
	public Storage storage = new Storage();
	
	public Logic() throws ClassNotFoundException, IOException {
		storage.loadStorageFile();
		_mainList = storage.getTasksList();
	}
	
	public void addTask(String description, Date startTime, Date endTime, String tag) throws IOException {
		Task task = new Task();
		task.setDescription(description);
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setTag(tag);
		_mainList.add(task);
		saveTask();
	}
	
	public void deleteTask(int id) throws IOException {
		int taskIndex= getTaskIndex(id);
		_mainList.remove(taskIndex);
		saveTask();
	}
	
	public void editTask(int id, String description, Date startTime, Date endTime, String tag) throws IOException {
		int taskIndex = getTaskIndex(id);
		
		if(description !=_mainList.get(taskIndex).getDescription()) {
			_mainList.get(taskIndex).setDescription(description);
		}
		
		if(startTime != _mainList.get(taskIndex).getStartTime()) {
			_mainList.get(taskIndex).setStartTime(startTime);
		}
		
		if(endTime != _mainList.get(taskIndex).getEndTime()) {
			_mainList.get(taskIndex).setEndTime(endTime);
		}
		
		if(tag != _mainList.get(taskIndex).getTag()) {
			_mainList.get(taskIndex).setTag(tag);
		}
		
		saveTask();
	}
	
	public void done(int id) throws IOException {
		markCompletion(id, true);
	}
	
	public void unDone(int id) throws IOException {
		markCompletion(id, false);
	}
	
	public void markCompletion(int id, boolean isDone) throws IOException {
		int taskIndex = getTaskIndex(id);
		
		_mainList.get(taskIndex).setIsDone(isDone);
		saveTask();
	}
	
	public ArrayList<Task> viewMainList() {
		return _mainList;
	}
	
	public void undo(int number) {
	}
	
	public void redo(int number) {
	}
	
	public int getTaskIndex(int id) {
		int taskIndex = id - 1;
		return taskIndex;
	}
	
	public void saveTask() throws IOException {
		storage.writeStorageFile(_mainList);
	}
}