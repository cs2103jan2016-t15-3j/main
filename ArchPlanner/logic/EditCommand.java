package logic;

import java.util.Calendar;

public class EditCommand implements Command {
	
	private int _id;
	private String _description;
	private String _tag;
	private Calendar _startDateTime;
	private Calendar _endDateTime;
	private boolean _isDone;
	private boolean _isOverDue;

	public EditCommand(int id, String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		assert(id >= 1); //id can only be 1 or greater
		_id = id;
		_description = description;
		_tag = tag;
		_startDateTime = startDateTime;
		_endDateTime = endDateTime;
		_isDone = false;
		_isOverDue = false;
	}
	
	public EditCommand(int id, String description, String tag, Calendar startDateTime, Calendar endDateTime, boolean isDone) {
		this(id, description, tag, startDateTime, endDateTime);
		_isDone = isDone;
	}
	
	public void setId(int id) {
		assert(id >= 1); //id can only be 1 or greater
		_id = id;
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
	
	public int getId() {
		return _id;
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
