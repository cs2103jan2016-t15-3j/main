//@@author A0140021J
package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Tag;
import logic.Task;
import logic.TaskParameters;
import storage.Storage;

public class ViewCommand implements CommandInterface {
	public enum VIEW_TYPE {VIEW_ALL, VIEW_DONE, VIEW_UNDONE, VIEW_OVERDUE};
	public enum CATEGORY_TYPE {CATEGORY_ALL, CATEGORY_EVENTS, CATEGORY_DEADLINES, CATEGORY_TASKS};

	public VIEW_TYPE _viewType;
	public CATEGORY_TYPE _categoryType;
	private TaskParameters _task;

	public ViewCommand(VIEW_TYPE viewType, CATEGORY_TYPE categoryType, TaskParameters taskParameters) {
		_viewType = viewType;
		_categoryType = categoryType;
		_task = taskParameters;
	}

	public CommandInterface execute() {
		return null;
	}

	public CommandInterface execute(ListsManager listsManager, Storage storage) {
		return null;
	}

	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		String currentViewType = "";
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		setViewIfViewTypeIsNotNull(listsManager);
		setViewIfCategoryTypeIsNotNull(listsManager);
		updateSelectedTagsList(listsManager);

		listsManager.getIndexList().clear();
		listsManager.updateLists();

		currentViewType = updateViewListWithDescriptionOnly(listsManager, currentViewType);
		currentViewType = updateCurrentViewTypeWithTagsList(currentViewType);
		currentViewType = updateViewListWithStartDateOnly(listsManager, currentViewType);
		currentViewType = updateViewListWithStartTimeOnly(listsManager, currentViewType);
		currentViewType = updateViewListWithEndDateOnly(listsManager, currentViewType);
		currentViewType = updateViewListWithEndTimeOnly(listsManager, currentViewType);
		currentViewType = updateViewListWithStartDateEndDateOnly(listsManager, currentViewType);

		listsManager.setCurrentViewType(currentViewType);
		return null;
	}

	private void updateSelectedTagsList(ListsManager listsManager) {
		listsManager.getSelectedTagsList().clear();
		if (_task.getTagsList() != null && !_task.getTagsList().isEmpty()) {
			listsManager.getSelectedTagsList().addAll(_task.getTagsList());
		}
	}

	private String updateViewListWithStartDateEndDateOnly(ListsManager listsManager, String currentViewType) {
		if (_task.getStartDate() != null && _task.getEndDate() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!(((task.getStartDate() != null) && (task.getEndDate() == null) 
						&& ((task.getStartDate().isAfter(_task.getStartDate()) && task.getStartDate().isBefore(_task.getEndDate())) 
								|| task.getStartDate().isEqual(_task.getStartDate()) || task.getStartDate().isEqual(_task.getEndDate()))) 
						|| ((task.getStartDate() == null) && (task.getEndDate() != null)
								&& ((task.getEndDate().isAfter(_task.getStartDate()) && task.getEndDate().isBefore(_task.getEndDate())) 
										|| task.getEndDate().isEqual(_task.getStartDate()) || task.getEndDate().isEqual(_task.getEndDate()))) 
						|| ((task.getStartDate() != null) && (task.getEndDate() != null) 
								&& (((task.getStartDate().isAfter(_task.getStartDate()) && task.getStartDate().isBefore(_task.getEndDate())) 
										|| (task.getEndDate().isAfter(_task.getStartDate()) && task.getEndDate().isBefore(_task.getEndDate()))) 
										|| task.getStartDate().isEqual(_task.getStartDate()) || task.getStartDate().isEqual(_task.getEndDate()) 
										|| task.getEndDate().isEqual(_task.getStartDate()) || task.getEndDate().isEqual(_task.getEndDate()))))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType += appendCurrentViewType(currentViewType, _task.getStartDateString()) + "to " 
					+ appendCurrentViewType(currentViewType, _task.getEndDateString());;
		}
		return currentViewType;
	}

	private String updateViewListWithEndTimeOnly(ListsManager listsManager, String currentViewType) {
		if (_task.getStartTime() == null && _task.getEndTime() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getEndTime() != null) && 
						(task.getEndTime().equals(_task.getEndTime())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, _task.getEndTimeString());
		}
		return currentViewType;
	}

	private String appendCurrentViewType(String currentViewType, String detail) {
		currentViewType += "\"" + detail + "\" ";
		return currentViewType;
	}

	private String updateViewListWithEndDateOnly(ListsManager listsManager, String currentViewType) {
		if (_task.getStartDate() == null && _task.getEndDate() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getEndDate() != null) && 
						(task.getEndDate().equals(_task.getEndDate())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, _task.getEndDateString());
		}
		return currentViewType;
	}

	private String updateViewListWithStartTimeOnly(ListsManager listsManager, String currentViewType) {
		if (_task.getStartTime() != null && _task.getEndTime() == null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getStartTime() != null) && 
						(task.getStartTime().equals(_task.getStartTime())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, _task.getStartTimeString());
		}
		return currentViewType;
	}

	private String updateViewListWithStartDateOnly(ListsManager listsManager, String currentViewType) {
		if (_task.getStartDate() != null && _task.getEndDate() == null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getStartDate() != null) && 
						(task.getStartDate().equals(_task.getStartDate())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, _task.getStartDateString());
		}
		return currentViewType;
	}

	private String updateCurrentViewTypeWithTagsList(String currentViewType) {
		if (_task.getTagsList() != null && !_task.getTagsList().isEmpty()) {
			for (int i = 0; i < _task.getTagsList().size(); i++) {
				currentViewType = appendCurrentViewType(currentViewType, _task.getTagsList().get(i));
			}
		}
		return currentViewType;
	}

	private String updateViewListWithDescriptionOnly(ListsManager listsManager, String currentViewType) {
		if (_task.getDescription() != null && !_task.getDescription().isEmpty()) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (listsManager.getViewList().get(i).getDescription() != null && 
						!task.getDescription().toLowerCase().contains(_task.getDescription().toLowerCase())) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, _task.getDescription());
		}
		return currentViewType;
	}

	private void setViewIfCategoryTypeIsNotNull(ListsManager listsManager) {
		if (_categoryType != null) {
			if (_viewType == null) {
				listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
			}
			listsManager.setCategoryType(_categoryType);
		}
	}

	private void setViewIfViewTypeIsNotNull(ListsManager listsManager) {
		if (_viewType != null) {
			if (_viewType.equals(VIEW_TYPE.VIEW_ALL)) {
				listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
				for (int i = 0; i < listsManager.getTagsList().size(); i++) {
					Tag tag = listsManager.getTagsList().get(i);
					tag.setIsSelected(false);
				}
				listsManager.getSelectedTagsList().clear();
			}
			listsManager.setViewType(_viewType);
		}
	}

	public String getMessage() {
		return "";
	}

	public TaskParameters getTask() {
		return _task;
	}
}