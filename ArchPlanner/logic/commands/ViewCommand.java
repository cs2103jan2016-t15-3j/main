package logic.commands;

import java.util.logging.Logger;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Logic;
import logic.Tag;
import logic.Task;
import logic.TaskParameters;
import storage.Storage;

/**
 * This class is used to execute view command.
 * 
 * @@author A0140021J
 *
 */
public class ViewCommand implements CommandInterface {

	//These are enum variables of view type.
	public enum VIEW_TYPE {
		ALL, DONE, UNDONE, OVERDUE
	}

	//these are enum variables of category type.
	public enum CATEGORY_TYPE {
		ALL, EVENTS, DEADLINES, TASKS
	}

	//This is the logger used to log and observe the changes when program runs.
	static Logger log = Logger.getLogger(Logic.class.getName());

	//This is the view type of the ViewCommand object.
	private VIEW_TYPE _viewType;

	//This is the category type of the ViewCommand object.
	public CATEGORY_TYPE _categoryType;

	//This is the taskParameters of the ViewCommand object.
	private TaskParameters _taskParameters;

	//These are constant string variables for logging.
	private final String  LOGGER_MESSAGE_EXECUTING_VIEW_COMMAND = "Executing view command...";
	private final String  LOGGER_MESSAGE_COMPLETED_VIEW_COMMAND = "Completed view command.";

	//These constant string variables are used to append messages for readability.
	private final String STRING_EMPTY = "";
	private final String STRING_DOUBLE_QUOTE = "\"";
	private final String STRING_SINGLE_SPACE = " ";
	private final String STRING_TO = "to";

	//This is constructor of the class.
	public ViewCommand(VIEW_TYPE viewType, CATEGORY_TYPE categoryType, TaskParameters taskParameters) {
		_viewType = viewType;
		_categoryType = categoryType;
		_taskParameters = taskParameters;
	}

	/**
	 * This is setter method of the ViewCommand's viewType.
	 * 
	 * @param viewType	This will be the viewType of the ViewCommand.
	 */
	public void setViewType(VIEW_TYPE viewType) {
		_viewType = viewType;
	}

	/**
	 * This is setter method of the ViewCommand's categoryType.
	 * 
	 * @param categoryType	This will be the categoryType of the ViewCommand.
	 */
	public void setCategoryType(CATEGORY_TYPE categoryType) {
		_categoryType = categoryType;
	}

	/**
	 * This is setter method of the ViewCommand's taskParameters.
	 * 
	 * @param taskParameters	This will be the taskParameters of the ViewCommand.
	 */
	public void setTaskParameters(TaskParameters taskParameters) {
		_taskParameters = taskParameters;
	}

	/**
	 * This is getter method of the ViewCommand's viewType.
	 * 
	 * @return	viewType.
	 */
	public VIEW_TYPE getViewType() {
		return _viewType;
	}

	/**
	 * This is getter method of the ViewCommand's categoryType.
	 * 
	 * @return	categoryType.
	 */
	public CATEGORY_TYPE getCategoryType() {
		return _categoryType;
	}

	/**
	 * This is getter method of the ViewCommand's taskParameters.
	 * 
	 * @return	taskParameters.
	 */
	public TaskParameters getTaskParameters() {
		return _taskParameters;
	}

	/**
	 * This is getter method of the ViewCommand's message.
	 * 
	 * @return	message with empty string.
	 */
	public String getMessage() {
		return STRING_EMPTY;
	}

	/**
	 * This method will not be called.
	 */
	public CommandInterface execute() {
		return null;
	}

	/**
	 * This method will not be called.
	 */
	public CommandInterface execute(ListsManager listsManager, Storage storage) {
		return null;
	}

	/**
	 * This method is used to set the view state as specified by the user.
	 */
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {

		log.info(LOGGER_MESSAGE_EXECUTING_VIEW_COMMAND);
		String currentViewType = STRING_EMPTY;
		listsManager.setViewType(VIEW_TYPE.ALL);
		setViewIfViewTypeIsNotNull(listsManager);
		setViewIfCategoryTypeIsNotNull(listsManager);
		updateSelectedTagsList(listsManager);

		clearIndexList(listsManager);
		listsManager.updateLists();

		currentViewType = updateViewListWithDescriptionOnly(listsManager, currentViewType);
		currentViewType = updateCurrentViewTypeWithTagsList(currentViewType);
		currentViewType = updateViewListWithStartDateOnly(listsManager, currentViewType);
		currentViewType = updateViewListWithStartTimeOnly(listsManager, currentViewType);
		currentViewType = updateViewListWithEndDateOnly(listsManager, currentViewType);
		currentViewType = updateViewListWithEndTimeOnly(listsManager, currentViewType);
		currentViewType = updateViewListWithStartDateEndDateOnly(listsManager, currentViewType);

		listsManager.setCurrentViewType(currentViewType);

		log.info(currentViewType);
		log.info(LOGGER_MESSAGE_COMPLETED_VIEW_COMMAND);
		return null;
	}

	/**
	 * This method clear the indexList in the ListsManager.
	 * 
	 * @param listsManager 	his is the ListsManager.
	 */
	private void clearIndexList(ListsManager listsManager) {
		listsManager.getIndexList().clear();
	}

	/**
	 * This method update the selectedTagsList in ListsManger.
	 * 
	 * @param listsManager	This is the ListsManger
	 */
	private void updateSelectedTagsList(ListsManager listsManager) {
		listsManager.getSelectedTagsList().clear();
		if (getTaskParameters().getTagsList() != null && !getTaskParameters().getTagsList().isEmpty()) {
			listsManager.getSelectedTagsList().addAll(getTaskParameters().getTagsList());
		}
	}

	/**
	 * This method is used to update view list in ListsManager with list of tasks with start date and end date that intercept
	 * taskParameters' start date and end date time frame.
	 * 
	 * @param listsManager		This is the ListsManager.
	 * @param currentViewType	This is the current view type.
	 * @return					current view type
	 */
	private String updateViewListWithStartDateEndDateOnly(ListsManager listsManager, String currentViewType) {
		if (getTaskParameters().getStartDate() != null && getTaskParameters().getEndDate() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!(hasTaskStartDateInterceptsTaskParametersTimeFrame(task) || hasTaskEndDateInterceptsTaskParametersTimeFrame(task) 
						|| hasTaskStartDateEndDateInterceptsTaskParametersTimeFrame(task))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType += appendCurrentViewType(currentViewType, getTaskParameters().getStartDateString()) + STRING_TO 
					+ STRING_SINGLE_SPACE + appendCurrentViewType(currentViewType, getTaskParameters().getEndDateString());
		}
		return currentViewType;
	}

	/**
	 * This method is used to check if the task's start date and end date intercepts taskParameters' time frame.
	 * 
	 * @param task	This is the task in the view list to be checked.
	 * @return		true if task's start date and end date intercepts taskParameters' time frame or else return false.
	 */
	private boolean hasTaskStartDateEndDateInterceptsTaskParametersTimeFrame(Task task) {
		return task.getStartDate() != null && task.getEndDate() != null 
				&& (((task.getStartDate().isAfter(getTaskParameters().getStartDate()) && task.getStartDate().isBefore(getTaskParameters().getEndDate())) 
						|| (task.getEndDate().isAfter(getTaskParameters().getStartDate()) && task.getEndDate().isBefore(getTaskParameters().getEndDate()))) 
						|| task.getStartDate().isEqual(getTaskParameters().getStartDate()) || task.getStartDate().isEqual(getTaskParameters().getEndDate()) 
						|| task.getEndDate().isEqual(getTaskParameters().getStartDate()) || task.getEndDate().isEqual(getTaskParameters().getEndDate()));
	}

	/**
	 * This method is used to check if the task's end date intercepts taskParameters' time frame.
	 * 
	 * @param task	This is the task in the view list to be checked.
	 * @return		true if task's end date intercepts taskParameters' time frame or else return false.
	 */
	private boolean hasTaskEndDateInterceptsTaskParametersTimeFrame(Task task) {
		return task.getStartDate() == null && task.getEndDate() != null
				&& ((task.getEndDate().isAfter(getTaskParameters().getStartDate()) && task.getEndDate().isBefore(getTaskParameters().getEndDate())) 
						|| task.getEndDate().isEqual(getTaskParameters().getStartDate()) || task.getEndDate().isEqual(getTaskParameters().getEndDate()));
	}

	/**
	 * This method is used to check if the task's start date intercepts taskParameters' time frame.
	 * 
	 * @param task	This is the task in the view list to be checked.
	 * @return		true if task's start date intercepts taskParameters' time frame or else return false.
	 */
	private boolean hasTaskStartDateInterceptsTaskParametersTimeFrame(Task task) {
		return task.getStartDate() != null && task.getEndDate() == null 
				&& ((task.getStartDate().isAfter(getTaskParameters().getStartDate()) && task.getStartDate().isBefore(getTaskParameters().getEndDate())) 
						|| task.getStartDate().isEqual(getTaskParameters().getStartDate()) || task.getStartDate().isEqual(getTaskParameters().getEndDate()));
	}

	/**
	 * This method is used to update view list with tasks' end time matches taskParameters' end time
	 * if taskParameters' start time is null and end time is not null.
	 * 
	 * @param listsManager		This is the ListsManager.
	 * @param currentViewType	Thisis the current view type.
	 * @return					current view type.
	 */
	private String updateViewListWithEndTimeOnly(ListsManager listsManager, String currentViewType) {
		if (getTaskParameters().getStartTime() == null && getTaskParameters().getEndTime() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!(task.getEndTime() != null && task.getEndTime().equals(getTaskParameters().getEndTime()))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, getTaskParameters().getEndTimeString());
		}
		return currentViewType;
	}

	/**
	 * This method is used to append the current view type.
	 * 
	 * @param currentViewType	This is the current view type.
	 * @param detail			This is the String to be appended to current view type.
	 * @return					current view type.
	 */
	private String appendCurrentViewType(String currentViewType, String detail) {
		currentViewType += STRING_DOUBLE_QUOTE + detail + STRING_DOUBLE_QUOTE + STRING_SINGLE_SPACE;
		return currentViewType;
	}

	/**
	 * This method is used to update view list with tasks' end date matches taskParameters' end date
	 * if taskParameters' start date is null and end date is not null.
	 * 
	 * @param listsManager		This is the ListsManager.
	 * @param currentViewType	This is the current view type.
	 * @return					current view type.
	 */
	private String updateViewListWithEndDateOnly(ListsManager listsManager, String currentViewType) {
		if (getTaskParameters().getStartDate() == null && getTaskParameters().getEndDate() != null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getEndDate() != null) && 
						(task.getEndDate().equals(getTaskParameters().getEndDate())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, getTaskParameters().getEndDateString());
		}
		return currentViewType;
	}

	/**
	 * This method is used to update view list with tasks' start time matches taskParameters' start time
	 * if taskParameters' start time is not null and end time is null.
	 * 
	 * @param listsManager		This is the ListsManager.
	 * @param currentViewType	This is the current view type.
	 * @return					current view type.
	 */
	private String updateViewListWithStartTimeOnly(ListsManager listsManager, String currentViewType) {
		if (getTaskParameters().getStartTime() != null && getTaskParameters().getEndTime() == null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getStartTime() != null) && 
						(task.getStartTime().equals(getTaskParameters().getStartTime())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, getTaskParameters().getStartTimeString());
		}
		return currentViewType;
	}

	/**
	 * This method is used to update view list with tasks' start date matches taskParameters' start date
	 * if taskParameters' start date is not null and end date is null.
	 * 
	 * @param listsManager		This is the ListsManager.
	 * @param currentViewType	This is the current view type.
	 * @return					current view type.
	 */
	private String updateViewListWithStartDateOnly(ListsManager listsManager, String currentViewType) {
		if (getTaskParameters().getStartDate() != null && getTaskParameters().getEndDate() == null) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (!((listsManager.getViewList().get(i).getStartDate() != null) && 
						(task.getStartDate().equals(getTaskParameters().getStartDate())))) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, _taskParameters.getStartDateString());
		}
		return currentViewType;
	}

	/**
	 * This method is used to update and append current view type with tags' names.
	 * 
	 * @param currentViewType	This is the current view type.
	 * @return					current view type.
	 */
	private String updateCurrentViewTypeWithTagsList(String currentViewType) {
		if (getTaskParameters().getTagsList() != null && !getTaskParameters().getTagsList().isEmpty()) {
			for (int i = 0; i < getTaskParameters().getTagsList().size(); i++) {
				currentViewType = appendCurrentViewType(currentViewType, getTaskParameters().getTagsList().get(i));
			}
		}
		return currentViewType;
	}

	/**
	 * This method is used to update view list with tasks with description that contains the taskParameters' description.
	 * 
	 * @param listsManager		This is the ListsManager.
	 * @param currentViewType	This is the current view type.
	 * @return					current view type.
	 */
	private String updateViewListWithDescriptionOnly(ListsManager listsManager, String currentViewType) {
		if (getTaskParameters().getDescription() != null && !getTaskParameters().getDescription().isEmpty()) {
			for (int i = 0; i < listsManager.getViewList().size(); i++) {
				Task task = listsManager.getViewList().get(i);
				if (listsManager.getViewList().get(i).getDescription() != null && 
						!task.getDescription().toLowerCase().contains(getTaskParameters().getDescription().toLowerCase())) {
					listsManager.getViewList().remove(i);
					i--;
				}
			}
			currentViewType = appendCurrentViewType(currentViewType, getTaskParameters().getDescription());
		}
		return currentViewType;
	}

	/**
	 * This method is used to set view type if category type is not null.
	 * 
	 * @param listsManager	This is the ListsManager.
	 */
	private void setViewIfCategoryTypeIsNotNull(ListsManager listsManager) {
		if (getCategoryType() != null) {
			if (getViewType() == null) {
				listsManager.setViewType(VIEW_TYPE.ALL);
			}
			listsManager.setCategoryType(getCategoryType());
		}
	}

	/**
	 * This method is used to set category type if view type is not null.
	 * 
	 * @param listsManager	This is the ListsManager.
	 */
	private void setViewIfViewTypeIsNotNull(ListsManager listsManager) {
		if (getViewType() != null) {
			if (getViewType().equals(VIEW_TYPE.ALL)) {
				listsManager.setCategoryType(CATEGORY_TYPE.ALL);
				deselectAllTags(listsManager);
			}
			listsManager.setViewType(_viewType);
		}
	}

	/**
	 * This method is used to set the select status of all the tags in tagsList to false, and clear selectedTagsList.
	 *  
	 * @param listsManager	This is the ListsManager.
	 */
	private void deselectAllTags(ListsManager listsManager) {
		for (int i = 0; i < listsManager.getTagsList().size(); i++) {
			Tag tag = listsManager.getTagsList().get(i);
			tag.setIsSelected(false);
		}
		listsManager.getSelectedTagsList().clear();
	}
}