package comparator;
import java.util.Comparator;

import logic.Task;

public class OverdueComparator implements Comparator<Task> {

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