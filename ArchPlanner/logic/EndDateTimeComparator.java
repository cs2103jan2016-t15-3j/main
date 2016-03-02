package logic;
import java.util.Comparator;

public class EndDateTimeComparator implements Comparator<Task> {
	
	public int compare(Task task1, Task task2) {
		return task1.getEndTime().compareTo(task2.getEndTime());
	}
	
	public boolean equals(Object obj) {
		return this == obj;
	}
}