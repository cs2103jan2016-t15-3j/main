package logic.comparator;
import java.util.Comparator;

import logic.Task;

public class DoneComparator implements Comparator<Task> {

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