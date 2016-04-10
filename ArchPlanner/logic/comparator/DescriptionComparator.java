package logic.comparator;

import java.util.Comparator;

import logic.Task;

/**
 * This class is used to compare description of two Task objects to support sorting of list based on description.
 * 
 *@@author A0140021J
 *
 */
public class DescriptionComparator implements Comparator<Task> {

	/**
	 * This method is used to compare description of two Task objects.
	 * It returns 0 if task1's description and task2's description are both null.
	 * It returns -1 if task1's description is null and task2's description is not null.
	 * It returns 1 if task1's description is not null and task2's description is null.
	 */
	public int compare(Task task1, Task task2) {
		
		if (task1.getDescription() == null && task2.getDescription() == null) {
			return 0;
		}
		
		if (task1.getDescription() == null && task2.getDescription() != null) {
			return -1;
		}
		
		if (task1.getDescription() != null && task2.getDescription() == null) {
			return 1;
		}
		
		String description1 =  task1.getDescription();
		String description2 =  task2.getDescription();
		
		return description1.compareTo(description2);
	}
}