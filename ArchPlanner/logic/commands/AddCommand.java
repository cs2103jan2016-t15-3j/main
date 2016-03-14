package logic.commands;

import java.util.ArrayList;
import java.util.Calendar;

import logic.ListsManager;
import logic.Task;
import logic.UserTaskParameters;

public class AddCommand implements Command {
	
	UserTaskParameters userTaskParameters;
	
	public AddCommand(String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		userTaskParameters = new UserTaskParameters(description, tag, startDateTime, endDateTime);
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager) {
		Task task = new Task(userTaskParameters.getDescription(), userTaskParameters.getTag(), userTaskParameters.getStartDateTime(), 
				userTaskParameters.getEndDateTime());
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		mainList.add(task);
		listsManager.updateLists(mainList);
		return true;
	}
}