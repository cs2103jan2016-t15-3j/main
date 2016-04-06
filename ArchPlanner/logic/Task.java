//@@author A0140021J
package logic;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This class contains the attributes of each task in ArchPlanner
 * @author riyang
 *
 */

public class Task extends TaskParameters implements Serializable {

	/**
	 * This is a generated serial UID for serializing
	 */
	private static final long serialVersionUID = 4512516990029160337L;

	public Task() {
		super();
	}

	public Task(String description, ArrayList<String> tagsList, LocalDate startDate, LocalTime startTime, 
			LocalDate endDate, LocalTime endTime) {
		super(description, tagsList, startDate, startTime, endDate, endTime);
	}
}