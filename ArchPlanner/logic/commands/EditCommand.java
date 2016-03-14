package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.TaskParameters;

import java.util.ArrayList;
import java.util.Calendar;

public class EditCommand implements Command {

	TaskParameters taskParameters;
	private int _index;

	public EditCommand(int index, String description, ArrayList<String> tagsList, Calendar startDateTime, Calendar endDateTime) {
		assert(index >= 1);
		taskParameters = new TaskParameters(description, tagsList, startDateTime, endDateTime);
		_index = index - 1;
	}

	public EditCommand(int id, String description, ArrayList<String> tagsList, Calendar startDateTime, Calendar endDateTime, boolean isDone) {
		this(id, description, tagsList, startDateTime, endDateTime);
		taskParameters.setIsDone(isDone);
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		
		ArrayList<Task> viewList = new ArrayList<Task>();
		viewList.addAll(listsManager.getViewList());
		
		if (!isWithinList(viewList, _index)) {
			return false;
		}
		
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		
		Task oldTask = viewList.get(_index);
		mainList.remove(oldTask);
		
		Task newTask = new Task(null, null, null, null);
		newTask = oldTask;
		
		if ((taskParameters.getDescription() != null) && (!taskParameters.getDescription().equals(oldTask.getDescription()))) {
			newTask.setDescription(taskParameters.getDescription());
		}

		if ((taskParameters.getTagsList() != null) && (!taskParameters.getTagsList().equals(oldTask.getTagsList()))) {
			newTask.setTag(taskParameters.getTagsList());
		}

		if ((taskParameters.getStartDateTime() != null) && (!taskParameters.getStartDateTime().equals(oldTask.getStartDateTime()))) {
			newTask.setStartDateTime(taskParameters.getStartDateTime());
		}

		if ((taskParameters.getEndDateTime() != null) && (!taskParameters.getEndDateTime().equals(oldTask.getEndDateTime()))) {
			newTask.setEndDateTime(taskParameters.getEndDateTime());
		}

		if (!taskParameters.getIsDone() == oldTask.getIsDone()) {
			newTask.setIsDone(taskParameters.getIsDone());
		}

		mainList.add(newTask);
		listsManager.updateLists(mainList);
		
		RollbackItem rollbackItem = new RollbackItem("edit", oldTask, newTask);
		ArrayList<RollbackItem> undoList = new ArrayList<RollbackItem>();
		undoList.addAll(historyManager.getUndoList());
		undoList.add(rollbackItem);
		historyManager.setUndoList(undoList);
		historyManager.setRedoList(new ArrayList<RollbackItem>());
		System.out.println("undolist size: " + undoList.size());
		return true;
	}
	
	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
}
