package logic.commands;

import logic.ListsManager;
import logic.Task;
import logic.TaskParameters;

import java.util.ArrayList;
import java.util.Calendar;

public class EditCommand implements Command {

	TaskParameters taskParameters;
	private int _index;

	public EditCommand(int index, String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		assert(index >= 1);
		taskParameters = new TaskParameters(description, tag, startDateTime, endDateTime);
		_index = index - 1;
	}

	public EditCommand(int id, String description, String tag, Calendar startDateTime, Calendar endDateTime, boolean isDone) {
		this(id, description, tag, startDateTime, endDateTime);
		taskParameters.setIsDone(isDone);
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager) {

		ArrayList<Task> viewList = new ArrayList<Task>();
		viewList.addAll(listsManager.getViewList());
		
		if (!isWithinList(viewList, _index)) {
			return false;
		}
		
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		
		Task task = viewList.get(_index);
		mainList.remove(task);
		
		if ((taskParameters.getDescription() != null) && (!taskParameters.getDescription().equals(task.getDescription()))) {
			task.setDescription(taskParameters.getDescription());
		}

		if ((taskParameters.getTag() != null) && (!taskParameters.getTag().equals(task.getTag()))) {
			task.setTag(taskParameters.getTag());
		}

		if ((taskParameters.getStartDateTime() != null) && (!taskParameters.getStartDateTime().equals(task.getStartDateTime()))) {
			task.setStartDateTime(taskParameters.getStartDateTime());
		}

		if ((taskParameters.getEndDateTime() != null) && (!taskParameters.getEndDateTime().equals(task.getEndDateTime()))) {
			task.setEndDateTime(taskParameters.getEndDateTime());
		}

		if (!taskParameters.getIsDone() == task.getIsDone()) {
			task.setIsDone(taskParameters.getIsDone());
		}

		mainList.add(task);
		listsManager.updateLists(mainList);
		return true;
	}
	
	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
}
