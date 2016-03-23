package comparator;

import java.util.Comparator;

import logic.Tag;

public class TagComparator implements Comparator<Tag> {

	public int compare(Tag tag1, Tag tag2) {
		if (tag1.getName() == null) {
			if (tag2.getName() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (tag2.getName() == null) {
			return 1;
		}
		String tagName1 =  tag1.getName();
		String tagName2 =  tag2.getName();
		
		return tagName1.compareTo(tagName2);
	}
}