package logic;

import java.util.Comparator;

public class TagComparator implements Comparator<Task> {
	
	public int compare(Task task1, Task task2) {
		if (task1.getTag() == null) {
			if (task2.getTag() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (task2.getTag() == null) {
			return 1;
		}
		String tag1 =  task1.getTag();
		String tag2 =  task2.getTag();
		
		return tag1.compareTo(tag2);
	}
}
