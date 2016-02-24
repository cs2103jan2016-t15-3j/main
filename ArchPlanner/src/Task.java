import java.io.Serializable;
import java.sql.Date;

/**
 * This class contains the attributes of each task in ArchPlanner
 * @author riyang
 *
 */

public class Task implements Serializable {
	
	//These are the data members of Task object
	public static String _description;
	public static String _tag;
	public static Date _startTime;
	public static Date _endTime;
	public static boolean _isDone;
	public static boolean _isOverDue;
	
	public static void setDescription(String description) {
		_description = description;
	}
	
	public static void setTag(String tag) {
		_tag = tag;
	}
	
	public static void setStartTime(Date startTime) {
		_startTime = startTime;
	}
	
	public static void setEndTime(Date endTime) {
		_endTime = endTime;
	}
	
	public static void setIsDone(boolean isDone) {
		_isDone = isDone;
	}
	
	public static void setIsOverDue(boolean isOverDue) {
		_isOverDue = isOverDue;
	}
	
	public static String getName() {
		return _name;
	}
	
	public static String getDescription() {
		return _description;
	}
	
	public static String getTag() {
		return _tag;
	}
	
	public static Date getStartTime() {
		return _startTime;
	}
	
	public static Date getEndTime() {
		return _endTime;
	}
	
	public static boolean getIsDone() {
		return _isDone;
	}
	
	public static boolean getIsOverDue() {
		return _isOverDue;
	}
}