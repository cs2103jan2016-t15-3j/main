package logic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class contains the attributes of each task in ArchPlanner
 * @author riyang
 *
 */

public class Task extends TaskParameters implements Serializable {
	
	public Task(String description, ArrayList<String> tagsList, Calendar startDateTime, Calendar endDateTime) {
		super(description, tagsList, startDateTime, endDateTime);
	}
}