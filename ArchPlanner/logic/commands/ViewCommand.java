package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;

import java.util.Calendar;

public class ViewCommand extends Command {
	
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

	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		if ((_description != null) && (_description.equals("all"))) {
			listsManager.setViewList("VIEW_ALL");
			return true;
		} else if ((_description != null) && (_description.equals("done"))) {
			listsManager.setViewList("VIEW_DONE");
			return true;
		}
		return false;
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