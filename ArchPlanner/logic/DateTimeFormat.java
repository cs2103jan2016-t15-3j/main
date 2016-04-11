package logic;

public class DateTimeFormat {

	//These constant string variables are used for display if the date is today, yesterday or tomorrow.
	private final String STRING_TODAY = "today";
	private final String STRING_YESTERDAY = "yesterday";
	private final String STRING_TOMORROW = "tomorrow";

	//These constant string variables are used to format date and time for display.
	private final String STRING_DATE_FORMAT = "dd MMM uuuu";
	private final String STRING_TIME_FORMAT = "h:mma";

	//This constant string variable is used if date or time is null.
	private final String STRING_EMPTY = "";
	
	/**
	 * This is getter method for string today.
	 * 
	 * @return string today.
	 */
	public String getStringToday() {
		return STRING_TODAY;
	}
	
	/**
	 * This is getter method for string yesterday.
	 * 
	 * @return string yesterday.
	 */
	public String getStringYesterday() {
		return STRING_YESTERDAY;
	}
	
	/**
	 * This is getter method for string tomorrow.
	 * 
	 * @return string tomorrow.
	 */
	public String getStringTomorrow() {
		return STRING_TOMORROW;
	}
	
	/**
	 * This is getter method for string date format.
	 * 
	 * @return string date format.
	 */
	public String getStringDateFormat() {
		return STRING_DATE_FORMAT;
	}
	
	/**
	 * This is getter method for string time format.
	 * 
	 * @return string time format.
	 */
	public String getStringTimeFormat() {
		return STRING_TIME_FORMAT;
	}
	
	/**
	 * This is getter method for string empty.
	 * 
	 * @return string empty.
	 */
	public String getStringEmpty() {
		return STRING_EMPTY;
	}
}
