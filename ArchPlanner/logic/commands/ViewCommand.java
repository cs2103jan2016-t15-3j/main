package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Task;
import logic.TaskParameters;

public class ViewCommand implements Command {
	public enum VIEW_TYPE {VIEW_ALL, VIEW_DONE, VIEW_UNDONE, VIEW_OVERDUE};
	public enum CATEGORY_TYPE {CATEGORY_ALL, CATEGORY_EVENT, CATEGORY_DEADLINE, CATEGORY_TASK};

	public VIEW_TYPE _viewType;
	public CATEGORY_TYPE _categoryType;
	private TaskParameters _task;

	public ViewCommand(VIEW_TYPE viewType, CATEGORY_TYPE categoryType, TaskParameters taskParameters) {
		_viewType = viewType;
		_categoryType = categoryType;
		_task = taskParameters;
	}
	public boolean execute() {
		return false;
	}

	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		if (_viewType != null) {
			if (_viewType.equals(VIEW_TYPE.VIEW_ALL)) {
				listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
			}
			listsManager.setViewType(_viewType);
		}

		if (_categoryType != null) {
			if (_viewType == null) {
				listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
			}
			listsManager.setCategoryType(_categoryType);
		}

		listsManager.updateLists();
		String currentViewType = "";

		if (_task.getDescription() != null && !_task.getDescription().isEmpty()) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (listsManager.getViewList().get(i).getDescription() != null && 
						!task.getDescription().contains(_task.getDescription())) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType += "\"" + _task.getDescription() + "\" ";
		}

		if (_task.getTagsList() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				boolean hasSameTag = true;
				Task task = listsManager.getViewList().get(i);
				for (int j = 0; j < _task.getTagsList().size() && hasSameTag; j++) {
					if (!task.getTagsList().contains(_task.getTagsList().get(j))) {
						listsManager.getViewList().remove(i);
						hasSameTag = false;
						i--;
					}
				}
			}
			for (int i = 0; i < _task.getTagsList().size(); i++) {
				currentViewType +=  "\"" + _task.getTagsList().get(i) + "\" ";
			}
		}

		if (_task.getStartDate() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getStartDate() != null) && 
						(task.getStartDate().equals(_task.getStartDate())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType += "\"" + _task.getStartDateString() + "\" ";
		}

		if (_task.getStartTime() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getStartTime() != null) && 
						(task.getStartTime().equals(_task.getStartTime())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType += "\"" + _task.getStartTimeString() + "\" ";
		}

		if (_task.getEndDate() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getEndDate() != null) && 
						(task.getEndDate().equals(_task.getEndDate())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType += "\"" + _task.getEndDateString() + "\" ";
		}

		if (_task.getEndTime() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getEndTime() != null) && 
						(task.getEndTime().equals(_task.getEndTime())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType += "\"" + _task.getEndTimeString() + "\" ";
		}

		listsManager.setCurrentViewType(currentViewType);
		return true;
	}
}