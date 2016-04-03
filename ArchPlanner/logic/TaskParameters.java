package logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TaskParameters {
	
	private String _description;
	private ArrayList<String> _tagsList;
	private LocalDate _startDate;
	private LocalDate _endDate;
	private LocalTime _startTime;
	private LocalTime _endTime;
	private boolean _isDone;
	private boolean _isOverdue;

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

	public void setDescription(String description) {
		_description = description;
	}

	public void setTagsList(ArrayList<String> tagsList) {
		_tagsList = tagsList;
	}

	public void setStartDate(LocalDate startDate) {
		_startDate = startDate;
	}
	
	public void setStartTime(LocalTime startTime) {
		_startTime = startTime;
	}
	
	public void setEndDate(LocalDate endDate) {
		_endDate = endDate;
	}
	
	public void setEndTime(LocalTime endTime) {
		_endTime = endTime;
	}

	public void setIsDone(boolean isDone) {
		_isDone = isDone;
	}

	public void setIsOverdue(boolean isOverdue) {
		_isOverdue = isOverdue;
	}

	public String getDescription() {
		return _description;
	}

	public ArrayList<String> getTagsList() {
		return _tagsList;
	}

	public LocalDate getStartDate() {
		return _startDate;
	}
	
	public LocalTime getStartTime() {
		return _startTime;
	}
	
	public LocalDate getEndDate() {
		return _endDate;
	}
	
	public LocalTime getEndTime() {
		return _endTime;
	}

	public String getStartDateString() {
		String startDateString = getDateString(_startDate);
		return startDateString;
	}

	public String getStartTimeString() {
		String startTime = getTimeString(_startTime);
		return startTime;
	}

	public String getEndDateString() {
		String endDateString = getDateString(_endDate);
		return endDateString;
	}

	public String getEndTimeString() {
		String endTime = getTimeString(_endTime);
		return endTime;
	}

	public boolean getIsDone() {
		return _isDone;
	}

	public boolean getIsOverdue() {
		return _isOverdue;
	}

	private String getDateString(LocalDate date) {

		LocalDate today = LocalDate.now();
		
		if (date == null) {
			return "";
		} else if (date.isEqual(today)) {
			return "today";
		} else if (date.compareTo(today) == -1) {
			return "yesterday";
		} else if (date.compareTo(today) == 1) {
			return "tomorrow";
		}
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM uuuu");
		return dateTimeFormatter.format(date);
	}

	private String getTimeString(LocalTime time) {
		if (time == null) {
			return "";
		}

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("h:mma");
		return dateTimeFormatter.format(time);
	}
}