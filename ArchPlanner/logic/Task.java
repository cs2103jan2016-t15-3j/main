package logic;
import java.io.Serializable;
import java.util.Date;

/**
 * This class contains the attributes of each task in ArchPlanner
 * @author riyang
 *
 */

public class Task implements Serializable {
	
	//These are the data members of Task object
	private String _description;
	private String _tag;
	private Date _startDateTime;
	private Date _endDateTime;
	private boolean _isDone;
	private boolean _isOverDue;
	
	public Task(String description, String tag, Date startDateTime, Date endDateTime) {
		_description = description;
		_tag = tag;
		_startDateTime = startDateTime;
		_endDateTime = endDateTime;
		_isDone = false;
		_isOverDue = false;
	}
	
	public void setDescription(String description) {
		_description = description;
	}
	
	public void setTag(String tag) {
		_tag = tag;
	}
	
	public void setStartDateTime(Date startDateTime) {
		_startDateTime = startDateTime;
	}
	
	public void setEndDateTime(Date endDateTime) {
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
	
	public Date getStartDateTime() {
		return _startDateTime;
	}
	
	public Date getEndDateTime() {
		return _endDateTime;
	}
	
	public String getStartDate() {
		String startDate = String.valueOf(getStartDateTime().getDate()) + "/"
				+  String.valueOf(getStartDateTime().getMonth()) + "/" + String.valueOf(getStartDateTime().getYear());
		return startDate;
	}
	
	public String getStartTime() {
		String startTime = String.valueOf(getStartDateTime().getTime());
		return startTime;
	}
	
	public String getEndDate() {
		String endDate = String.valueOf(getEndDateTime().getDate()) + "/"
				+  String.valueOf(getEndDateTime().getMonth()) + "/" + String.valueOf(getEndDateTime().getYear());
		return endDate;
	}
	
	public String getEndTime() {
		String endTime = String.valueOf(getStartDateTime().getTime());
		return endTime;
	}
	
	public boolean getIsDone() {
		return _isDone;
	}
	
	public boolean getIsOverDue() {
		return _isOverDue;
	}
}