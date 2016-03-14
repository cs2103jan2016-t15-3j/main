package logic.commands;

import java.util.ArrayList;
import java.util.Calendar;

import logic.ListsManager;
import logic.Task;
import logic.TaskParameters;

public class AddCommand implements Command {
	
	TaskParameters taskParameters;
	
	public AddCommand(String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		taskParameters = new taskParameters(description, tag, startDateTime, endDateTime);
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager) {
		Task task = new Task(taskParameters.getDescription(), taskParameters.getTag(), taskParameters.getStartDateTime(), 
				taskParameters.getEndDateTime());
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		mainList.add(task);
		listsManager.updateLists(mainList);
		return true;
	}
}