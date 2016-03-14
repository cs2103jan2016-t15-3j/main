package logic.commands;

import logic.ListsManager;
import logic.Task;
import logic.UserTaskParameters;

import java.util.ArrayList;
import java.util.Calendar;

public class EditCommand implements Command {

	UserTaskParameters userTaskParameters;
	private int _index;

	public EditCommand(int index, String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		assert(index >= 1);
		userTaskParameters = new UserTaskParameters(description, tag, startDateTime, endDateTime);
		_index = index - 1;
	}

	public EditCommand(int id, String description, String tag, Calendar startDateTime, Calendar endDateTime, boolean isDone) {
		this(id, description, tag, startDateTime, endDateTime);
		userTaskParameters.setIsDone(isDone);
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
		
		if ((userTaskParameters.getDescription() != null) && (!userTaskParameters.getDescription().equals(task.getDescription()))) {
			task.setDescription(userTaskParameters.getDescription());
		}

		if ((userTaskParameters.getTag() != null) && (!userTaskParameters.getTag().equals(task.getTag()))) {
			task.setTag(userTaskParameters.getTag());
		}

		if ((userTaskParameters.getStartDateTime() != null) && (!userTaskParameters.getStartDateTime().equals(task.getStartDateTime()))) {
			task.setStartDateTime(userTaskParameters.getStartDateTime());
		}

		if ((userTaskParameters.getEndDateTime() != null) && (!userTaskParameters.getEndDateTime().equals(task.getEndDateTime()))) {
			task.setEndDateTime(userTaskParameters.getEndDateTime());
		}

		if (!userTaskParameters.getIsDone() == task.getIsDone()) {
			task.setIsDone(userTaskParameters.getIsDone());
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
