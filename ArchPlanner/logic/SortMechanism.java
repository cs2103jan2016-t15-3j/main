package logic;

import java.util.ArrayList;
import java.util.Collections;

import logic.comparator.DescriptionComparator;
import logic.comparator.DoneComparator;
import logic.comparator.OverdueComparator;
import logic.comparator.TagComparator;

/**
 * This class is to support sorting mechanism to sort the tasks in a list.
 * 
 * @@author A0140021J
 *
 */
public class SortMechanism {

	/**
	 * This method sort the list by description in ascending order.
	 * 
	 * @param list this is the list to be sorted.
	 */
	public void sortListByDescription(ArrayList<Task> list) {
		DescriptionComparator descriptionComp = new DescriptionComparator();
		Collections.sort(list, descriptionComp);
	}

	/**
	 * This method sort the list by overdue status with overdue tasks on the top of the list.
	 * 
	 * @param list this is the list to be sorted.
	 */
	public void sortListByOverdue(ArrayList<Task> list) {
		OverdueComparator overdueComp = new OverdueComparator();
		Collections.sort(list, overdueComp);
	}

	/**
	 * This method sort the list by done status with done tasks on the bottom of the list.
	 * 
	 * @param list this is the list to be sorted.
	 */
	public void sortListByDone(ArrayList<Task> list) {
		DoneComparator doneComp = new DoneComparator();
		Collections.sort(list, doneComp);
	}

	/**
	 * This method sort the list by name in ascending order.
	 * 
	 * @param list this is the list to be sorted.
	 */
	public void sortTagsList(ArrayList<Tag> list) {
		TagComparator tagComp = new TagComparator();
		Collections.sort(list, tagComp);
	}
	
	/**
	 * This method is used to sort the list based on date and time from earlier task to later task.
	 * 
	 * @param list This is the list to be sorted.
	 * 
	 * @return sorted list.
	 */
	public ArrayList<Task> sortListByDateTime(ArrayList<Task> list) {

		//This list contains tasks of the top part of the list.
		ArrayList<Task> top = new ArrayList<Task>();

		//This list contains tasks of the bottom part of the list.
		ArrayList<Task> bottom = new ArrayList<Task>();

		//return if list has less than 2 Task objects
		if (list.size() <= 1) {
			return list;
		}

		int mid = list.size() / 2;
		updateTopList(list, top, mid);
		updateBottomList(list, bottom, mid);

		top  = sortListByDateTime(top);
		bottom = sortListByDateTime(bottom);
		merge(list, top, bottom);

		return list;
	}

	/**
	 * This method is used to add task to bottom list.
	 * 
	 * @param list This is the list to be sorted.
	 * 
	 * @param bottom This is the bottom list.
	 * 
	 * @param mid This is the index of the middle of the list.
	 */
	private void updateBottomList(ArrayList<Task> list, ArrayList<Task> bottom, int mid) {
		for (int i=mid; i<list.size(); i++) {
			bottom.add(list.get(i));
		}
	}

	/**
	 * This method is used to add task to top list.
	 * 
	 * @param list This is the list to be sorted.
	 * 
	 * @param top This is the top list.
	 * 
	 * @param mid This is the index of the middle of the list.
	 */
	private void updateTopList(ArrayList<Task> list, ArrayList<Task> top, int mid) {
		for (int i=0; i<mid; i++) {
			top.add(list.get(i));
		}
	}

	/**
	 * This method is used to merge top and bottom list.
	 * 
	 * @param list This is the list to be sorted.
	 * 
	 * @param top This is the top list.
	 * 
	 * @param bottom This is the bottom list.
	 */
	private void merge(ArrayList<Task> list, ArrayList<Task> top, ArrayList<Task> bottom) {

		int listIndex = 0;
		int topIndex = 0;
		int bottomIndex = 0;

		//Loop and add tasks from top and bottom lists while topIndex and bottomIndex 
		//is less than top list's size and bottom list's size
		while (topIndex < top.size() && bottomIndex < bottom.size()) {
			Task topTask = top.get(topIndex);
			Task bottomTask = bottom.get(bottomIndex);

			if ( isTopTaskFloatingAndBottomTaskWithDate(topTask, bottomTask) || hasTopTaskIsAfterBottomTask(topTask, bottomTask)) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
			} else {
				list.set(listIndex, top.get(topIndex));
				topIndex++;
			}
			listIndex++;
		}

		int leftOverIndex = setLeftOverIndex(top, topIndex, bottomIndex);
		ArrayList<Task> leftOver = setLeftOverList(top, bottom, topIndex);
		listIndex = setListIndex(list, listIndex, leftOverIndex, leftOver);
	}

	/**
	 * This method is used to set the list index.
	 * 
	 * @param list This is the list to be sorted.
	 * 
	 * @param listIndex This is the index of the sorted list.
	 * 
	 * @param leftOverIndex This is the index of the leftOver list
	 * 
	 * @param leftOver This is the list of leftover tasks after merging top and bottom lists with indexes within lists' size.
	 * 
	 * @return lists index.
	 */
	private int setListIndex(ArrayList<Task> list, int listIndex, int leftOverIndex, ArrayList<Task> leftOver) {
		for (int i = leftOverIndex; i < leftOver.size(); i++) {
			list.set(listIndex, leftOver.get(i));
			listIndex++;
		}
		return listIndex;
	}
	
	/**
	 * This method is used to set the leftOver list with the remaining tasks after merging top and bottom lists.
	 * 
	 * @param top This is the top list.
	 * 
	 * @param bottom This is the bottom list.
	 * 
	 * @param topIndex This is the top list index.
	 * 
	 * @return leftOver list.
	 */
	private ArrayList<Task> setLeftOverList(ArrayList<Task> top, ArrayList<Task> bottom, int topIndex) {
		ArrayList<Task> leftOver;
		if (topIndex >= top.size()) {
			leftOver = bottom;
		} else {
			leftOver = top;
		}
		return leftOver;
	}

	/**
	 * This method set leftOver list index with the number of remaining tasks after merging top and bottom lists.
	 * 
	 * @param top This is the top list.
	 * 
	 * @param topIndex This is the top list index.
	 * 
	 * @param bottomIndex This is the bottom list index.
	 * 
	 * @return leftOver index.
	 */
	private int setLeftOverIndex(ArrayList<Task> top, int topIndex, int bottomIndex) {
		int leftOverIndex;
		if (topIndex >= top.size()) {
			leftOverIndex = bottomIndex;
		} else {
			leftOverIndex = topIndex;
		}
		return leftOverIndex;
	}

	/**
	 * This method is used to check top task is floating task and bottom task has start or end date.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if top task is floating task and bottom task has start or end date, or else return false.
	 */
	private boolean isTopTaskFloatingAndBottomTaskWithDate(Task topTask, Task bottomTask) {
		return ((topTask.getStartDate() == null && topTask.getEndDate() == null)
				&& (((bottomTask.getStartDate() != null)) || (bottomTask.getEndDate() != null)));
	}

	/**
	 * This method is used to check whether top task is earlier than bottom task.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if top task is earlier than bottom task or else return false.
	 */
	private boolean hasTopTaskIsAfterBottomTask(Task topTask, Task bottomTask) {

		return (hasTopTaskStartDateIsAfterBottomTaskStartDate(topTask, bottomTask) 
				|| hasTopTaskStartTimeIsAfterBottomTaskStartTime(topTask, bottomTask) 
				|| hasTopTaskStartDateIsAfterBottomTaskEndDate(topTask, bottomTask) 
				|| hasTopTaskEndDateIsAfterBottomTaskStartDate(topTask, bottomTask)
				|| hasTopTaskStartTimeIsAfterBottomTaskEndTime(topTask, bottomTask) 
				|| hasTopTaskEndTimeIsAfterBottomTaskstartTime(topTask, bottomTask)
				|| hasTopTaskEndDateIsAfterBottomTaskEndDate(topTask, bottomTask))
				|| hasTopTaskEndTimeIsAfterBottomTaskEndTime(topTask, bottomTask);
	}

	/**
	 * This method is used to check whether both tasks have start date and whether 
	 * top task's start date is after bottom task's start date.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if both tasks have start date and top task's start date is after bottom task's start date.
	 */
	private boolean hasTopTaskStartDateIsAfterBottomTaskStartDate(Task topTask, Task bottomTask) {

		return ((topTask.getStartDate() != null && bottomTask.getStartDate() != null) 
				&& topTask.getStartDate().isAfter(bottomTask.getStartDate()));
	}

	/**
	 * This method is used to check whether both tasks have start date and time with same date, 
	 * and whether top task's start time is after bottom task's start time.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if both tasks have start date and time with same date, and top task's start time is after 
	 * 		   bottom task's start time.
	 */
	private boolean hasTopTaskStartTimeIsAfterBottomTaskStartTime(Task topTask, Task bottomTask) {

		return ((topTask.getStartDate() != null && bottomTask.getStartDate() != null)
				&& (topTask.getStartTime() != null && bottomTask.getStartTime() != null) 
				&& topTask.getStartDate().equals(bottomTask.getStartDate())
				&& topTask.getStartTime().isAfter(bottomTask.getStartTime()));
	}

	/**
	 * This method is used to check whether top task has start date and bottom task has no start date but has end date, 
	 * and whether top task's start date is after bottom task's end date.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if top task has start date and bottom task has no start date but has end date, 
	 * 		   and top task's start date is after than bottom task's end date.
	 */
	private boolean hasTopTaskStartDateIsAfterBottomTaskEndDate(Task topTask, Task bottomTask) {

		return ((topTask.getStartDate() != null && bottomTask.getStartDate() == null 
				&& bottomTask.getEndDate() != null) && topTask.getStartDate().isAfter(bottomTask.getEndDate()));
	}

	/**
	 * This method is used to check whether top task has start date and time and bottom task has no start date 
	 * but has end date and end time, whether top task's start date is same as bottom task's end date,
	 * and whether top task's start time is after bottom task's end time.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if top task has start date and time and bottom task has no start date but has end date and end time, 
	 * 		   with top task's start date is same as bottom task's end date, and top task's start time is after
	 *         bottom task's end time.
	 */
	private boolean hasTopTaskStartTimeIsAfterBottomTaskEndTime(Task topTask, Task bottomTask) {

		return ((topTask.getStartDate() != null && bottomTask.getStartDate() == null && bottomTask.getEndDate() != null)
				&& (topTask.getStartTime() != null && bottomTask.getEndTime() != null) 
				&& topTask.getStartDate().equals(bottomTask.getEndDate())
				&& topTask.getStartTime().isAfter(bottomTask.getEndTime()));
	}

	/**
	 * This method is used to check whether top task has start date and end date and bottom task has start date,
	 * and whether top task's end date is after bottom task's start date.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if top task has start date and end date and bottom task has start date,
	 * 		   and top task's end date is after bottom task's start date.
	 */
	private boolean hasTopTaskEndDateIsAfterBottomTaskStartDate(Task topTask, Task bottomTask) {

		return ((bottomTask.getStartDate() != null && topTask.getStartDate() == null && topTask.getEndDate() != null)
				&& topTask.getEndDate().isAfter(bottomTask.getStartDate()));
	}

	/**
	 * This method is used to check whether top task has no start date, has end date and time, and bottom task has start date and time,
	 * whether top task's end date is same as bottom task's start date,
	 * and whether top task's end time is after bottom task's start time.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if top task has no start date, has end date and time, and bottom task has start date and time,
	 *         with top task's end date is same as bottom task's start date,
	 *         and top task's end time is after bottom task's start time.
	 */
	private boolean hasTopTaskEndTimeIsAfterBottomTaskstartTime(Task topTask, Task bottomTask) {

		return ((bottomTask.getStartDate() != null && topTask.getStartDate() == null && topTask.getEndDate() != null)
				&& (topTask.getEndTime() != null && bottomTask.getStartTime() != null) 
				&& topTask.getEndDate().equals(bottomTask.getStartDate())
				&& topTask.getEndTime().isAfter(bottomTask.getStartTime()));
	}
	
	/**
	 * This method is used to check whether top task has no start date but has end date, and bottom task has no start date 
	 * but has end date, and whether top task's end date is after bottom task's end date.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if top task has no start date but has end date, and bottom task has no start date but has end date, 
	 * 		   and top task's end date is after bottom task's end date.
	 */
	private boolean hasTopTaskEndDateIsAfterBottomTaskEndDate(Task topTask, Task bottomTask) {

		return ((topTask.getStartDate() == null && bottomTask.getStartDate() == null) 
				&& (topTask.getEndDate() != null && bottomTask.getEndDate() != null) 
				&& topTask.getEndDate().isAfter(bottomTask.getEndDate()));
	}

	/**
	 * This method is used to check whether top task has no start date but has end date and time, and bottom task has no start date 
	 * but has end date and time, whether top task's end date is same as bottom task's end date,
	 * and whether top task's end date is after bottom task's end date.
	 * 
	 * @param topTask This is the top task.
	 * 
	 * @param bottomTask This is the bottom task.
	 * 
	 * @return true if top task has no start date but has end date and time, and bottom task has no start date 
	 *         but has end date and time, with top task's end date is same as bottom task's end date,
	 *         and top task's end date is after bottom task's end date.
	 */
	private boolean hasTopTaskEndTimeIsAfterBottomTaskEndTime(Task topTask, Task bottomTask) {

		return ((topTask.getStartDate() == null && bottomTask.getStartDate() == null) 
				&& (topTask.getEndDate() != null && bottomTask.getEndDate() != null) 
				&& (topTask.getEndTime() != null && bottomTask.getEndTime() != null) 
				&& topTask.getEndDate().isEqual(bottomTask.getEndDate()) 
				&& topTask.getEndTime().isAfter(bottomTask.getEndTime()));
	}
}
