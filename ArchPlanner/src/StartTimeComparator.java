import java.util.Comparator;

public class StartTimeComparator implements Comparator<Task> {
	
	public int compare(Task task1, Task task2) {
		return task1.getStartTime().compareTo(task2.getStartTime());
	}
	
	public boolean equals(Object obj) {
		return this == obj;
	}
}