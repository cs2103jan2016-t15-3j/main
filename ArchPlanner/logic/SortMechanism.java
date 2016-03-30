package logic;

import java.util.ArrayList;
import java.util.Collections;

import comparator.DescriptionComparator;
import comparator.DoneComparator;
import comparator.OverdueComparator;
import comparator.TagComparator;


public class SortMechanism {

	public ArrayList<Task> sortListByDateTime(ArrayList<Task> list) {
		ArrayList<Task> top = new ArrayList<Task>();
		ArrayList<Task> bottom = new ArrayList<Task>();
		int mid;

		if (list.size() <= 1) {
			return list;
		}
		mid = list.size() / 2;
		for (int i=0; i<mid; i++) {
			top.add(list.get(i));
		}

		for (int i=mid; i<list.size(); i++) {
			bottom.add(list.get(i));
		}

		top  = sortListByDateTime(top);
		bottom = sortListByDateTime(bottom);

		merge(list, top, bottom);
		//for (int i = 0; i < list.size(); i++)
		//System.out.println("merge" + i + "\t" + list.get(i).getDescription());
		return list;
	}

	private void merge(ArrayList<Task> list, ArrayList<Task> top, ArrayList<Task> bottom) {

		int listIndex = 0;
		int topIndex = 0;
		int bottomIndex = 0;

		while ((topIndex < top.size()) && (bottomIndex < bottom.size())) {
			Task topTask = top.get(topIndex);
			Task bottomTask = bottom.get(bottomIndex);

			if ( isTopTaskFloatingAndBottomTaskWithDate(topTask, bottomTask) 
					|| hasTopTaskEarlierThanBottomTask(topTask, bottomTask)) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
			} else {
				list.set(listIndex, top.get(topIndex));
				topIndex++;
				//System.out.println("test9");
			}
			listIndex++;
		}

		ArrayList<Task> leftOver;
		int leftOverIndex;
		if (topIndex >= top.size()) {
			leftOver = bottom;
			leftOverIndex = bottomIndex;
		} else {
			leftOver = top;
			leftOverIndex = topIndex;
		}

		for (int i = leftOverIndex; i < leftOver.size(); i++) {
			list.set(listIndex, leftOver.get(i));
			listIndex++;
		}
	}

	private boolean isTopTaskFloatingAndBottomTaskWithDate(Task topTask, Task bottomTask) {
		return ((((topTask.getStartDate() == null)) && (topTask.getEndDate() == null))
				&& (((bottomTask.getStartDate() != null)) || (bottomTask.getEndDate() != null)));
	}

	private boolean hasTopTaskEarlierThanBottomTask(Task topTask, Task bottomTask) {

		return (hasTopTaskStartDateEarlierThanBottomTaskStartDate(topTask, bottomTask) 
				|| hasTopTaskStartTimeEarlierThanBottomTaskStartTime(topTask, bottomTask) 
				|| hasTopTaskStartDateEarlierThanBottomTaskEndDate(topTask, bottomTask) 
				|| hasTopTaskEndDateEarlierThanBottomTaskStartDate(topTask, bottomTask)
				|| hasTopTaskStartTimeEarlierThanBottomTaskEndTime(topTask, bottomTask) 
				|| hasTopTaskEndTimeEarlierThanBottomTaskstartTime(topTask, bottomTask)
				|| hasTopTaskEndDateEarlierThanBottomTaskEndDate(topTask, bottomTask))
				|| hasTopTaskEndTimeEarlierThanBottomTaskEndTime(topTask, bottomTask);
	}

	private boolean hasTopTaskStartDateEarlierThanBottomTaskStartDate(Task topTask, Task bottomTask) {

		return (((topTask.getStartDate() != null) && (bottomTask.getStartDate() != null)) 
				&& topTask.getStartDate().isBefore(bottomTask.getStartDate()));
	}

	private boolean hasTopTaskStartTimeEarlierThanBottomTaskStartTime(Task topTask, Task bottomTask) {

		return (((topTask.getStartDate() != null) && (bottomTask.getStartDate() != null))
				&& ((topTask.getStartTime() != null) && (bottomTask.getStartTime() != null)) 
				&& (topTask.getStartDate().equals(bottomTask.getStartDate()))
				&& topTask.getStartTime().isBefore(bottomTask.getStartTime()));
	}
	
	private boolean hasTopTaskStartDateEarlierThanBottomTaskEndDate(Task topTask, Task bottomTask) {

		return (((topTask.getStartDate() != null) && (bottomTask.getStartDate() == null) && (bottomTask.getEndDate() != null))
				&& topTask.getStartDate().isBefore(bottomTask.getEndDate()));
	}
	
	private boolean hasTopTaskStartTimeEarlierThanBottomTaskEndTime(Task topTask, Task bottomTask) {

		return (((topTask.getStartDate() != null) && (bottomTask.getStartDate() == null) && (bottomTask.getEndDate() != null))
				&& ((topTask.getStartTime() != null) && (bottomTask.getEndTime() != null)) 
				&& (topTask.getStartDate().equals(bottomTask.getEndDate()))
				&& topTask.getStartTime().isBefore(bottomTask.getEndTime()));
	}
	
	private boolean hasTopTaskEndDateEarlierThanBottomTaskStartDate(Task topTask, Task bottomTask) {

		return (((bottomTask.getStartDate() != null) && (topTask.getStartDate() == null) && (topTask.getEndDate() != null))
				&& topTask.getEndDate().isBefore(bottomTask.getStartDate()));
	}
	
	private boolean hasTopTaskEndTimeEarlierThanBottomTaskstartTime(Task topTask, Task bottomTask) {

		return (((bottomTask.getStartDate() != null) && (topTask.getStartDate() == null) && (topTask.getEndDate() != null))
				&& ((topTask.getEndTime() != null) && (bottomTask.getStartTime() != null)) 
				&& (topTask.getEndDate().equals(bottomTask.getStartDate()))
				&& topTask.getEndTime().isBefore(bottomTask.getStartTime()));
	}
	private boolean hasTopTaskEndDateEarlierThanBottomTaskEndDate(Task topTask, Task bottomTask) {

		return (((topTask.getStartDate() == null) && (bottomTask.getStartDate() == null)) 
				&& ((topTask.getEndDate() != null) && (bottomTask.getEndDate() != null)) 
				&& topTask.getEndDate().isBefore(bottomTask.getEndDate()));
	}
	
	private boolean hasTopTaskEndTimeEarlierThanBottomTaskEndTime(Task topTask, Task bottomTask) {

		return (((topTask.getStartDate() == null) && (bottomTask.getStartDate() == null)) 
				&& ((topTask.getEndDate() != null) && (bottomTask.getEndDate() != null)) 
				&& ((topTask.getEndTime() != null) && (bottomTask.getEndTime() != null)) 
				&& (topTask.getEndDate().isEqual(bottomTask.getEndDate())) 
				&& (topTask.getEndTime().isBefore(bottomTask.getEndTime())));
	}

	public void sortListByDescription(ArrayList<Task> list) {
		DescriptionComparator descriptionComp = new DescriptionComparator();
		Collections.sort(list, descriptionComp);
	}

	public void sortListByOverdue(ArrayList<Task> list) {
		OverdueComparator overdueComp = new OverdueComparator();
		Collections.sort(list, overdueComp);
	}

	public void sortListByDone(ArrayList<Task> list) {
		DoneComparator doneComp = new DoneComparator();
		Collections.sort(list, doneComp);
	}

	public void sortTagsList(ArrayList<Tag> list) {
		TagComparator tagComp = new TagComparator();
		Collections.sort(list, tagComp);
	}
}
