package logic.comparator;
import java.util.Comparator;

import logic.Task;

public class DescriptionComparator implements Comparator<Task> {

	public int compare(Task task1, Task task2) {
		if (task1.getDescription() == null) {
			if (task2.getDescription() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (task2.getDescription() == null) {
			return 1;
		}
		String description1 =  task1.getDescription();
		String description2 =  task2.getDescription();
		
		return description1.compareTo(description2);
	}
}