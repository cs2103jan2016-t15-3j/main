package logic;

import java.util.Calendar;

public class AddCommand implements Command {

	private String _description;
	private String _tag;
	private Calendar _startDateTime;
	private Calendar _endDateTime;
	private boolean _isDone;
	private boolean _isOverDue;

	public AddCommand(String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		_description = description;
		_tag = tag;
		_startDateTime = startDateTime;
		_endDateTime = endDateTime;
		_isDone = false;
		_isOverDue = false;
	}

	public AddCommand(String description, String tag, Calendar startDateTime, Calendar endDateTime, boolean isDone) {
		this(description, tag, startDateTime, endDateTime);
		_isDone = isDone;
	}

	public void setDescription(String description) {
		assert(description != null); //description cannot be null
		_description = description;
	}

	public void setTag(String tag) {
		_tag = tag;
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

	public void setIsOverDue(boolean isOverDue) {
		_isOverDue = isOverDue;
	}

	public String getDescription() {
		return _description;
	}

	public String getTag() {
		return _tag;
	}

	public Calendar getStartDateTime() {
		return _startDateTime;
	}

	public Calendar getEndDateTime() {
		return _endDateTime;
	}

	public boolean getIsDone() {
		return _isDone;
	}

	public boolean getIsOverDue() {
		return _isOverDue;
	}
}
