package logic.commands;

import java.util.ArrayList;
import java.util.Calendar;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;

public class AddCommand implements Command {
	
	TaskParameters taskParameters;
	
	public AddCommand(String description, ArrayList<String> tagsList, Calendar startDateTime, Calendar endDateTime) {
		taskParameters = new TaskParameters(description, tagsList, startDateTime, endDateTime);
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		Task newTask = new Task(taskParameters.getDescription(), taskParameters.getTagsList(), taskParameters.getStartDateTime(), 
				taskParameters.getEndDateTime());
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		mainList.add(newTask);
		listsManager.updateLists(mainList);

		RollbackItem rollbackItem = new RollbackItem("add", null, newTask);
		ArrayList<RollbackItem> undoList = new ArrayList<RollbackItem>();

			undoList.addAll(historyManager.getUndoList());
		
		undoList.add(rollbackItem);
		historyManager.setUndoList(undoList);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
		System.out.println("undolist size: " + undoList.size());
		return true;
	}
}