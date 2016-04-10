package logic.comparator;

import java.util.Comparator;

import logic.Task;

/**
 * This class is used to compare done status of two Task objects to support sorting of list based on done status.
 * 
 *@@author A0140021J
 *
 */
public class DoneComparator implements Comparator<Task> {

	/**
	 * This method is used to compare done status of two Task objects.
	 * It returns 0 if task1's isDone and task2's isDone are true.
	 * It returns -1 if task1's isDone is false and task2's isDone is true.
	 * It returns 1 if task1's isDone and task2's isDone are both false or true.
	 */
	public int compare(Task task1, Task task2) {
		if (task1.getIsDone() == true && task2.getIsDone() == false){
			return 1;
		} else if (task1.getIsDone() == false && task2.getIsDone() == true){
			return -1;
		} else {
			return 0;
		}
	}
}