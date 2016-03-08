package logic;
import java.util.Calendar;
import java.util.Comparator;

public class StartDateTimeComparator implements Comparator<Task> {

	public int compare(Task task1, Task task2) {
		if (task1.getStartDateTime() == null) {
			if (task2.getStartDateTime() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (task2.getStartDateTime() == null) {
			return 1;
		}
		Calendar startDateTime1 =  task1.getStartDateTime();
		Calendar startDateTime2 =  task2.getStartDateTime();

		return startDateTime1.compareTo(startDateTime2);
	}
}