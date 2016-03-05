package logic.commands;

import logic.Logic;

import java.util.Calendar;

public class ViewCommand implements Command {
	
	private String _description;
	private String _tag;
	private Calendar _startDateTime;
	private Calendar _endDateTime;
	private boolean _isDone;

	public ViewCommand(String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		_description = description;
		_tag = tag;
		_startDateTime = startDateTime;
		_endDateTime = endDateTime;
		_isDone = false;
	}

	public ViewCommand(String description, String tag, Calendar startDateTime, Calendar endDateTime, boolean isDone) {
		this(description, tag, startDateTime, endDateTime);
		_isDone = isDone;
	}

	public void setDescription(String description) {
		assert(description != null); //description cannot be null
		_description = description;
	}

	@Override
	public void execute(Logic logic) {

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

}