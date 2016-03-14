package logic;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskParameters {
	
	private String _description;
	private ArrayList<String> _tagsList;
	private Calendar _startDateTime;
	private Calendar _endDateTime;
	private boolean _isDone;
	private boolean _isOverdue;

	public TaskParameters(String description, ArrayList<String> tagsList, Calendar startDateTime, Calendar endDateTime) {
		_description = description;
		_tagsList = tagsList;
		_startDateTime = startDateTime;
		_endDateTime = endDateTime;
		_isDone = false;
		_isOverdue = false;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setTag(ArrayList<String> tagsList) {
		_tagsList = tagsList;
	}

	public void setStartDateTime(Calendar startDateTime) {
		_startDateTime = startDateTime;
	}

	public void setEndDateTime(Calendar endDateTime) {
		_endDateTime = endDateTime;
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

	public Calendar getStartDateTime() {
		return _startDateTime;
	}

	public Calendar getEndDateTime() {
		return _endDateTime;
	}

	public String getStartDate() {
		String startDate = getDate(getStartDateTime());
		return startDate;
	}

	public String getStartTime() {
		String startTime = getTime(getStartDateTime());
		return startTime;
	}

	public String getEndDate() {
		String endDate = getDate(getEndDateTime());
		return endDate;
	}

	public String getEndTime() {
		String endTime = getTime(getEndDateTime());
		return endTime;
	}

	public boolean getIsDone() {
		return _isDone;
	}

	public boolean getIsOverdue() {
		return _isOverdue;
	}

	private String getDate(Calendar date) {
		if (date == null) {
			return "";
		}

		String day = String.format("%02d", date.get(Calendar.DATE));
		String month = String.format("%02d", date.get(Calendar.MONTH));
		String year = String.format("%02d", date.get(Calendar.YEAR));

		String strDate  = day + "/" +  month + "/" + year;

		return strDate;
	}

	private String getTime(Calendar date) {
		if (date == null) {
			return "";
		}

		String minute = String.format("%02d", date.get(Calendar.MINUTE));
		String hour = String.format("%02d", date.get(Calendar.HOUR));

		String strTime = hour + ":" + minute;

		return strTime;
	}
}