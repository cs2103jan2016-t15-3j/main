package logic;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This class defines the the properties of a Task object
 * which inherit from TaskParameters class.
 * 
 * @@author A0140021J
 *
 */
public class Task extends TaskParameters implements Serializable {

	/**
	 * This is a generated serial UID for serializing
	 */
	private static final long serialVersionUID = 4512516990029160337L;

	//This is constructor of the class.
	public Task() {
		super();
	}

	//This is constructor of the class.
	public Task(String description, ArrayList<String> tagsList, LocalDate startDate, LocalTime startTime, 
			LocalDate endDate, LocalTime endTime) {
		super(description, tagsList, startDate, startTime, endDate, endTime);
	}
}