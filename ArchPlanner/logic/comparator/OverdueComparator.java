package logic.comparator;

import java.util.Comparator;

import logic.Task;


/**
 * This class is used to compare overdue status of two Task objects to support sorting of list based on overdue status.
 * 
 *@@author A0140021J
 *
 */
public class OverdueComparator implements Comparator<Task> {

	/**
	 * This method is used to compare overdue status of two Task objects.
	 * It returns 0 if task1's isOverdue and task2's isOverdue are both false or true.
	 * It returns -1 if task1's isOverdue is true and task2's isOverdue is false.
	 * It returns 1 if task1's isOverdue is false and task2's isOverdue is true.
	 */
	public int compare(Task task1, Task task2) {
		if (task1.getIsOverdue() == false && task2.getIsOverdue() == true){
			return 1;
		} else if (task1.getIsOverdue() == true && task2.getIsOverdue() == false){
			return -1;
		} else {
			return 0;
		}
	}
}