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
	
	public static ArrayList<Task> _mainList;
	
	public static Storage storage = new Storage();
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		storage.loadStorageFile();
		storage.readStorageFile();
		_mainList = storage.getTasksList();
		Date date = new Date();
		addTask("task 1", date, date, "tag 1");
		addTask("task 2", date, date, "tag 2");
		for(int i = 0; i < _mainList.size(); i++) {
			String output = _mainList.get(i).getStartTime().toString();
			System.out.println(output);
		}
		storage.writeStorageFile();
	}
	
	public static void addTask(String description, Date startTime, Date endTime, String tag) throws IOException {
		Task task = new Task();
		task.setDescription(description);
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setTag(tag);
		_mainList.add(task);
		storage.setTasksList(_mainList);
		storage.writeStorageFile();
	}
	
	public static void deleteTask(int id) throws IOException {
		int taskIndex= getTaskIndex(id);
		_mainList.remove(taskIndex);
		storage.setTasksList(_mainList);
		storage.writeStorageFile();
	}
	
	public static void editTask(int id, String description, Date startTime, Date endTime, String tag) throws IOException {
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
		
		storage.setTasksList(_mainList);
		storage.writeStorageFile();
	}
	
	public static void done(int id) throws IOException {
		markCompletion(id, true);
	}
	
	public static void unDone(int id) throws IOException {
		markCompletion(id, false);
	}
	
	public static void markCompletion(int id, boolean isDone) throws IOException {
		int taskIndex = getTaskIndex(id);
		
		_mainList.get(taskIndex).setIsDone(isDone);
		storage.setTasksList(_mainList);
		storage.writeStorageFile();
	}
	
	public static ArrayList<Task> viewMainList() {
		return _mainList;
	}
	
	public static void undo(int number) {
	}
	
	public static void redo(int number) {
	}
	
	public static int getTaskIndex(int id) {
		int taskIndex = id - 1;
		return taskIndex;
	}
}