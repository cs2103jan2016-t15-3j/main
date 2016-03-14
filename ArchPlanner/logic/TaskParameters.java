package logic;

import java.util.Calendar;

public class TaskParameters extends Task{

	public TaskParameters(String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		super(description, tag, startDateTime, endDateTime);
	}
}