package logic.commands;

import java.util.ArrayList;
import java.util.Calendar;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Task;
import logic.TaskParameters;

public class SearchCommand implements Command {
	
	TaskParameters taskParameters;

	public SearchCommand(String description, ArrayList<String> tagsList, Calendar startDateTime, Calendar endDateTime) {
		taskParameters = new TaskParameters(description, tagsList, startDateTime, endDateTime);
	}
	
	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
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
			if (taskDescription.contains(taskParameters.getDescription())) {
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
