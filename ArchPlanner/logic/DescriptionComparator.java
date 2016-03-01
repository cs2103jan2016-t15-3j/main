package logic;
import java.util.Comparator;

public class DescriptionComparator implements Comparator<Task> {
	
	public int compare(Task task1, Task task2) {
		return task1.getDescription().compareTo(task2.getDescription());
	}
	
	public boolean equals(Object obj) {
		return this == obj;
	}
}