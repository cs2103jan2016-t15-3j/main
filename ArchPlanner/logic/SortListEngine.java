package logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class SortListEngine {

	private ArrayList<Task> _timelineList = new ArrayList<Task>();
	private ArrayList<Task> _eventList = new ArrayList<Task>();
	private ArrayList<Task> _floatingList = new ArrayList<Task>();
	private ArrayList<Task> _doneList = new ArrayList<Task>();
	private ArrayList<Task> _undoneList = new ArrayList<Task>();

	public ArrayList<Task> getSortedList(ArrayList<Task> list) {
		populateLists(list);
		ArrayList<Task> sortedList = new ArrayList<Task>();
		sortedList.addAll(_timelineList);
		sortedList.addAll(_eventList);
		sortedList = getSortedListWithEndDateTime(sortedList);
		sortedList = getSortedListWithStartDateTime(sortedList);
		sortedList.addAll(_floatingList);
		return sortedList;
	}
	
	public ArrayList<Task> getSortedIsDoneList(ArrayList<Task> list) {
		populateLists(list);
		ArrayList<Task> doneList = new ArrayList<Task>();
		doneList.addAll(_doneList);
		return doneList;
	}
	
	public ArrayList<Task> getSortedIsUndoneList(ArrayList<Task> list) {
		populateLists(list);
		ArrayList<Task> undoneList = new ArrayList<Task>();
		undoneList.addAll(_undoneList);
		return undoneList;
	}

	private void populateLists(ArrayList<Task> list) {
		_floatingList = new ArrayList<Task>();
		_eventList = new ArrayList<Task>();
		_timelineList = new ArrayList<Task>();
		_doneList = new ArrayList<Task>();
		_undoneList = new ArrayList<Task>();

		for (int i = 0; i < list.size(); i++) {
			if ((getTaskStartDateTime(list, i) == null) && (getTaskEndDateTime(list, i) == null)) {
				_floatingList.add(list.get(i));
			} else if ((getTaskStartDateTime(list, i) != null) && (getTaskEndDateTime(list, i) == null) || 
					(getTaskStartDateTime(list, i) == null) && (getTaskEndDateTime(list, i) != null)) {
				_eventList.add(list.get(i));
			} else if ((getTaskStartDateTime(list, i) != null) && (getTaskEndDateTime(list, i) != null)) {
				_timelineList.add(list.get(i));
			}
			if (getTaskIsDone(list, i) == true) {
				_doneList.add(list.get(i));
			} else {
				_undoneList.add(list.get(i));
			}
			
			_timelineList = getSortedListWithEndDateTime(_timelineList);
			_timelineList = getSortedListWithStartDateTime(_timelineList);
			_eventList = getSortedListWithEndDateTime(_eventList);
			_eventList = getSortedListWithStartDateTime(_eventList);
			_floatingList = getSortedListWithDescription(_floatingList);
		}
	}
	
	private ArrayList<Task> getSortedFloatingList(ArrayList<Task> list) {
		populateLists(list);
		ArrayList<Task> floatingList = new ArrayList<Task>();
		floatingList.addAll(_floatingList);
		return floatingList;
	}

	private ArrayList<Task> getSortedTimelineList(ArrayList<Task> list) {
		populateLists(list);
		ArrayList<Task> timelineList = new ArrayList<Task>();
		timelineList.addAll(_timelineList);
		return timelineList;
	}

	private ArrayList<Task> getSortedEventList(ArrayList<Task> list) {
		populateLists(list);
		ArrayList<Task> eventList = new ArrayList<Task>();
		eventList.addAll(_eventList);
		return eventList;
	}

	private ArrayList<Task> getSortedListWithDescription(ArrayList<Task> list) {
		ArrayList<Task> sortedList = new ArrayList<Task>();
		sortedList.addAll(list);
		DescriptionComparator descriptionComp = new DescriptionComparator();
		Collections.sort(sortedList, descriptionComp);
		return sortedList;
	}

	private ArrayList<Task> getSortedListWithTag(ArrayList<Task> list) {
		ArrayList<Task> sortedList = new ArrayList<Task>();
		sortedList.addAll(list);
		TagComparator tagComp = new TagComparator();
		Collections.sort(sortedList, tagComp);
		return sortedList;
	}

	private ArrayList<Task> getSortedListWithStartDateTime(ArrayList<Task> list) {
		ArrayList<Task> sortedList = new ArrayList<Task>();
		sortedList.addAll(list);
		StartDateTimeComparator startDateTimeComp = new StartDateTimeComparator();
		Collections.sort(sortedList, startDateTimeComp);
		return sortedList;
	}

	private ArrayList<Task> getSortedListWithEndDateTime(ArrayList<Task> list) {
		ArrayList<Task> sortedList = new ArrayList<Task>();
		sortedList.addAll(list);
		EndDateTimeComparator endTimeComp = new EndDateTimeComparator();
		Collections.sort(sortedList, endTimeComp);
		return sortedList;
	}

	private Task getTask(ArrayList<Task> list, int taskIndex) {
		Task task = list.get(taskIndex);
		return task;
	}

	private String getTaskDescription(ArrayList<Task> list, int taskIndex) {
		String taskDescription = list.get(taskIndex).getDescription();
		return taskDescription;
	}

	private Calendar getTaskStartDateTime(ArrayList<Task> list, int taskIndex) {
		Calendar taskStartDateTime = list.get(taskIndex).getStartDateTime();
		return taskStartDateTime;
	}

	private Calendar getTaskEndDateTime(ArrayList<Task> list, int taskIndex) {
		Calendar taskEndTime = list.get(taskIndex).getEndDateTime();
		return taskEndTime;
	}

	private String getTaskTag(ArrayList<Task> list, int taskIndex) {
		String taskTag = list.get(taskIndex).getTag();
		return taskTag;
	}

	private boolean getTaskIsDone(ArrayList<Task> list, int taskIndex) {
		boolean taskIsDone = list.get(taskIndex).getIsDone();
		return taskIsDone;
	}
}
