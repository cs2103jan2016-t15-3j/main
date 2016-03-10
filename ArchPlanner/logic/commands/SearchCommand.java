package logic.commands;

import java.util.ArrayList;

import logic.Task;

public class SearchCommand extends Command {
	
	private String _partialDescription;

	public SearchCommand(String _partialDescription) {
		this._partialDescription = _partialDescription;
	}

	public void setPartialDescription(String partialDescription) {
		_partialDescription = partialDescription;
	}

	public String getPartialDescription() {
		return _partialDescription;
	}

	@Override
	public boolean execute(ArrayList<Task> mainList, ArrayList<Task> viewList, ArrayList<String> tagsList) {
		updateViewList(mainList, viewList);
		return true;
	}

	private void updateViewList(ArrayList<Task> mainList, ArrayList<Task> viewList) {
		viewList.removeAll(viewList);
		for (int i = 0; i < mainList.size(); i++) {
			String taskDescription = getTaskDescription(mainList, i);
			if (taskDescription.contains(_partialDescription)) {
				viewList.add(mainList.get(i));
			}
		}
	}
	
	private String getTaskDescription(ArrayList<Task> list, int taskIndex) {
		String taskDescription = list.get(taskIndex).getDescription();
		return taskDescription;
	}
}
