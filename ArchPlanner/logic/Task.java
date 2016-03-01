import java.io.Serializable;
import java.util.Date;

/**
 * This class contains the attributes of each task in ArchPlanner
 * @author riyang
 *
 */

public class Task implements Serializable {
	
	//These are the data members of Task object
	public String _description;
	public String _tag;
	public Date _startTime;
	public Date _endTime;
	public boolean _isDone;
	public boolean _isOverDue;
	
	public void setDescription(String description) {
		_description = description;
	}
	
	public void setTag(String tag) {
		_tag = tag;
	}
	
	public void setStartTime(Date startTime) {
		_startTime = startTime;
	}
	
	public void setEndTime(Date endTime) {
		_endTime = endTime;
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
	
	public Date getStartTime() {
		return _startTime;
	}
	
	public Date getEndTime() {
		return _endTime;
	}
	
	public boolean getIsDone() {
		return _isDone;
	}
	
	public boolean getIsOverDue() {
		return _isOverDue;
	}
}