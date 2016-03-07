package logic;
import java.util.Calendar;
import java.util.Comparator;

public class EndDateTimeComparator implements Comparator<Task> {
	
	public int compare(Task task1, Task task2) {
		if (task1 == null) {
			if (task2 == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (task2 == null) {
			return 1;
		}
		Calendar endDateTime1 =  task1.getEndDateTime();
		Calendar endDateTime2 =  task2.getEndDateTime();
		
		return endDateTime1.compareTo(endDateTime2);
	}
}