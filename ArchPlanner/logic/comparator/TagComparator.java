package logic.comparator;

import java.util.Comparator;

import logic.Tag;

/**
 * This class is used to compare tag's name of two Tag objects to support sorting of list based on tag's name.
 * 
 *@@author A0140021J
 *
 */
public class TagComparator implements Comparator<Tag> {

	/**
	 * This method is used to compare tag's name of two Tag objects.
	 * It returns 0 if tag1's name and tag2's name are both null.
	 * It returns -1 if tag1's name is null and tag2's name is not null.
	 * It returns 1 if tag1's name is not null and tag2's name is null.
	 */
	public int compare(Tag tag1, Tag tag2) {
		
		if (tag1.getName() == null && tag2.getName() == null) {
			return 0;
		}
		
		if (tag1.getName() == null && tag2.getName() != null) {
			return -1;
		}
		
		if (tag1.getName() != null && tag2.getName() == null) {
			return 1;
		}
		
		String tagName1 =  tag1.getName();
		String tagName2 =  tag2.getName();
		
		return tagName1.compareTo(tagName2);
	}
}