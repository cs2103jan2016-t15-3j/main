package logic.commands;

import java.util.ArrayList;
import java.util.Calendar;

import logic.ListsManager;
import logic.Task;
import logic.UserTaskParameters;

public class SearchCommand implements Command {
	
	UserTaskParameters userTaskParameters;

	public SearchCommand(String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		userTaskParameters.setDescription(description);
		userTaskParameters.setTag(tag);
		userTaskParameters.setStartDateTime(startDateTime);
		userTaskParameters.setEndDateTime(endDateTime);
	}

	public String getUserTaskParameters() {
		return getUserTaskParameters();
	}
	
	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager) {
		ArrayList<Task> searchResultList = new ArrayList<Task>();
		ArrayList<Task> viewList = new ArrayList<Task>();
		viewList.addAll(listsManager.getUndoneList());
		
		searchResultList = searchResult(viewList);
		listsManager.setSearchList(searchResultList);
		listsManager.setViewList("VIEW_SEARCH_RESULT");
		return true;
	}

	private ArrayList<Task> searchResult(ArrayList<Task> viewList) {
		ArrayList<Task> searchResultList = new ArrayList<>();
		for (int i = 0; i < viewList.size(); i++) {
			String taskDescription = getTaskDescription(viewList, i);
			if (taskDescription.contains(userTaskParameters.getDescription())) {
				searchResultList.add(viewList.get(i));
			}
		}
		return searchResultList;
	}
	
	private String getTaskDescription(ArrayList<Task> list, int taskIndex) {
		String taskDescription = list.get(taskIndex).getDescription();
		return taskDescription;
	}
}
