package logic;

import java.util.Calendar;

public class UserTaskParameters extends Task{

	public UserTaskParameters(String description, String tag, Calendar startDateTime, Calendar endDateTime) {
		super(description, tag, startDateTime, endDateTime);
	}
}