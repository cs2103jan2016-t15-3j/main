package logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * This class defines the the properties of a TaskParameter object.
 * 
 * @@author A0140021J
 *
 */
public class TaskParameters {
	
	//This variable is the description of the TaskParameters object
	private String _description;
	
	//This is the list of tags of the TaskParameters object
	private ArrayList<String> _tagsList;
	
	//This variable is the start date of the TaskParameters object
	private LocalDate _startDate;
	
	//This variable is the end date of the TaskParameters object
	private LocalDate _endDate;
	
	//This variable is the start time of the TaskParameters object
	private LocalTime _startTime;
	
	//This variable is the end time of the TaskParameters object
	private LocalTime _endTime;
	
	//This variable indicates whether the task is done.
	private boolean _isDone;
	
	//This variable indicates whether the task is overdue.
	private boolean _isOverdue;
	
	//These constant string variables are used for display if the date is today, yesterday or tomorrow.
	private final String STRING_TODAY = "today";
	private final String STRING_YESTERDAY = "yesterday";
	private final String STRING_TOMORROW = "tomorrow";
	
	//These constant string variables are used to format date and time for display.
	private final String STRING_DATE_FORMAT = "dd MMM uuuu";
	private final String STRING_TIME_FORMAT = "h:mma";
	
	//This constant string variable is used if date or time is null.
	private final String STRING_EMPTY = "";

	//This is constructor of the class.
	public TaskParameters() {
		_description = null;
		_tagsList = null;
		_startDate = null;
		_startTime = null;
		_endDate = null;
		_endTime = null;
		_isDone = false;
		_isOverdue = false;
	}
	
	//This is constructor of the class.
	public TaskParameters(String description, ArrayList<String> tagsList, LocalDate startDate, LocalTime startTime, 
			LocalDate endDate, LocalTime endTime) {
		_description = description;
		_tagsList = tagsList;
		_startDate = startDate;
		_startTime = startTime;
		_endDate = endDate;
		_endTime = endTime;
		_isDone = false;
		_isOverdue = false;
	}

	/**
	 * This is setter method for TaskParameters' description.
	 * 
	 * @param description This will be the description of the TaskParamters.
	 */
	public void setDescription(String description) {
		_description = description;
	}

	/**
	 * This is setter method for TaskParameters' tags list.
	 * 
	 * @param tagsList This will be the tags list of the TaskParamters.
	 */
	public void setTagsList(ArrayList<String> tagsList) {
		_tagsList = tagsList;
	}

	/**
	 * This is setter method for TaskParameters' start date.
	 * 
	 * @param startDate This will be the start date of the TaskParameters.
	 */
	public void setStartDate(LocalDate startDate) {
		_startDate = startDate;
	}
	

	/**
	 * This is setter method for TaskParameters' start time.
	 * 
	 * @param startTime This will be the start time of the TaskParameters.
	 */
	public void setStartTime(LocalTime startTime) {
		_startTime = startTime;
	}
	

	/**
	 * This is setter method for TaskParameters' end date.
	 * 
	 * @param endDate This will be the end date of the TaskParameters.
	 */
	public void setEndDate(LocalDate endDate) {
		_endDate = endDate;
	}
	

	/**
	 * This is setter method for TaskParameters' end time.
	 * 
	 * @param endTime This will be the end time of the TaskParameters.
	 */
	public void setEndTime(LocalTime endTime) {
		_endTime = endTime;
	}


	/**
	 * This is setter method for TaskParameters' isDone.
	 * 
	 * @param isDone This is the done status of the TaskParameters.
	 */
	public void setIsDone(boolean isDone) {
		_isDone = isDone;
	}

	/**
	 * This is setter method for TaskParameters' isOverdue.
	 * 
	 * @param isOverdue This is the overdue status of the TaskParameters.
	 */
	public void setIsOverdue(boolean isOverdue) {
		_isOverdue = isOverdue;
	}

	/**
	 * This is getter method for TaskParameters' description.
	 * 
	 * @return description of TaskParameters.
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * This is getter method for TaskParameters' tags list.
	 * 
	 * @return tags list.
	 */
	public ArrayList<String> getTagsList() {
		return _tagsList;
	}

	/**
	 * This is getter method for TaskParameters' start date.
	 * 
	 * @return start date.
	 */
	public LocalDate getStartDate() {
		return _startDate;
	}
	
	/**
	 * This is getter method for TaskParameters' start time.
	 * 
	 * @return start time.
	 */
	public LocalTime getStartTime() {
		return _startTime;
	}
	
	/**
	 * This is getter method for TaskParameters' end date.
	 * 
	 * @return end date.
	 */
	public LocalDate getEndDate() {
		return _endDate;
	}
	
	/**
	 * This is getter method for TaskParameters' end time.
	 * 
	 * @return end time.
	 */
	public LocalTime getEndTime() {
		return _endTime;
	}

	/**
	 * This is getter method for TaskParameters' start date in string format.
	 * 
	 * @return start date in string format.
	 */
	public String getStartDateString() {
		String startDateString = getDateString(_startDate);
		return startDateString;
	}

	/**
	 * This is getter method for TaskParameters' start time in string format.
	 * 
	 * @return start time in string format.
	 */
	public String getStartTimeString() {
		String startTime = getTimeString(_startTime);
		return startTime;
	}

	/**
	 * This is getter method for TaskParameters' end date in string format.
	 * 
	 * @return end date in string format.
	 */
	public String getEndDateString() {
		String endDateString = getDateString(_endDate);
		return endDateString;
	}

	/**
	 * This is getter method for TaskParameters' end time in string format.
	 * 
	 * @return end time string format.
	 */
	public String getEndTimeString() {
		String endTime = getTimeString(_endTime);
		return endTime;
	}

	/**
	 * This is getter method for TaskParameters' isDone.
	 * 
	 * @return done status.
	 */
	public boolean getIsDone() {
		return _isDone;
	}

	/**
	 * This is getter method for TaskParameters' isOverdue.
	 * 
	 * @return overdue status.
	 */
	public boolean getIsOverdue() {
		return _isOverdue;
	}

	/**
	 * This method is used to convert date from localDate to string format.
	 * 
	 * @param date This is the date of TaskParameters.
	 * 
	 * @return date in string format.
	 */
	private String getDateString(LocalDate date) {

		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		LocalDate tomorrow = today.plusDays(1);
		
		if (date == null) {
			return STRING_EMPTY;
		} else if (date.isEqual(today)) {
			return STRING_TODAY;
		} else if (date.isEqual(yesterday)) {
			return STRING_YESTERDAY;
		} else if (date.isEqual(tomorrow)) {
			return STRING_TOMORROW;
		}
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STRING_DATE_FORMAT);
		return dateTimeFormatter.format(date);
	}

	/**
	 * This method is used to convert time from localTime to string format.
	 * 
	 * @param time This is the time of TaskParameters.
	 * 
	 * @return time in string format.
	 */
	private String getTimeString(LocalTime time) {
		if (time == null) {
			return STRING_EMPTY;
		}

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STRING_TIME_FORMAT);
		return dateTimeFormatter.format(time);
	}
}