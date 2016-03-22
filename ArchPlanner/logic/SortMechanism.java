package logic;

import java.util.ArrayList;
import java.util.Collections;

import comparator.DescriptionComparator;


public class SortMechanism {

	public void sortTagsList(ArrayList<String> tagsList) {
		Collections.sort(tagsList);
	}

	public ArrayList<Task> sortListByDateTime(ArrayList<Task> list) {
		ArrayList<Task> top = new ArrayList<Task>();
		ArrayList<Task> bottom = new ArrayList<Task>();
		int mid;

		if (list.size() <= 1) {
			return list;
		}
		mid = list.size() / 2;
		// copy the left half of whole into the left.
		for (int i=0; i<mid; i++) {
			top.add(list.get(i));
		}

		for (int i=mid; i<list.size(); i++) {
			bottom.add(list.get(i));
		}

		top  = sortListByDateTime(top);
		bottom = sortListByDateTime(bottom);


		// Merge the results back together.
		merge(list, top, bottom);
		//for (int i = 0; i < list.size(); i++)
			//System.out.println("merge" + i + "\t" + list.get(i).getDescription());
		return list;
	}

	private void merge(ArrayList<Task> list, ArrayList<Task> top, ArrayList<Task> bottom) {

		int listIndex = 0;
		int topIndex = 0;
		int bottomIndex = 0;
		
		while ((topIndex < top.size()) && (bottomIndex < bottom.size())) {
			Task topTask = top.get(topIndex);
			Task bottomTask = bottom.get(bottomIndex);

			if ((((topTask.getStartDate() == null)) && (topTask.getEndDate() == null))
					&& (((bottomTask.getStartDate() != null)) || (bottomTask.getEndDate() != null))) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
				//System.out.println("test1 - (task1_start = task2_start == null) (task2_start = task2_end != null)");
			} else if ((((topTask.getStartDate() != null)) && (bottomTask.getStartDate() != null))
					&& (topTask.getStartDate().isAfter(bottomTask.getStartDate()))) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
				//System.out.println("test2 - (task1_start = task2_start != null) (task1_start compare task2_start)");
			} else if ((((topTask.getStartDate() == null)) && (topTask.getEndDate() != null))
					&& (((bottomTask.getStartDate() == null)) && (bottomTask.getEndDate() != null))
					&& (topTask.getEndDate().isAfter(bottomTask.getEndDate()))) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
				//System.out.println("test3 - (task1_start = task2_start = null) (task1_end = task2_end != null) (task1_end compare task2_end)");
			} else if (((topTask.getStartDate() != null))
					&& (((bottomTask.getStartDate() == null)) && (bottomTask.getEndDate() != null))
					&& (topTask.getStartDate().isAfter(bottomTask.getEndDate()))) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
				//System.out.println("test4 - (task1_start = task2_end != null) (task2_start = null) (task1_start compare task2_end)");
			} else if ((((topTask.getStartDate() == null)) && (topTask.getEndDate() != null))
					&& ((bottomTask.getStartDate() != null))
					&& (topTask.getEndDate().isAfter(bottomTask.getStartDate()))) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
				//System.out.println("test5 - (task1_start = null) (task1_end = task2_start != null) (task1_end compare task2_start)");
			} else if ((((topTask.getStartDate() == null)) && (topTask.getEndDate() != null))
					&& (((bottomTask.getStartDate() != null)) && (bottomTask.getEndDate() == null))
					&& (topTask.getEndDate().equals(bottomTask.getStartDate()))) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
				//System.out.println("test6 - (task1_start=task2_end = null) (task1_end = task2_start != null) (task1_end = task2_start)");
			} else if ((((topTask.getStartDate() != null)) && (topTask.getEndDate() != null))
					&& (((bottomTask.getStartDate() != null)) && (bottomTask.getEndDate() == null))
					&& (((topTask.getStartDate().equals(bottomTask.getStartDate()))))) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
				//System.out.println("test7 - (task1_start = task1_end = task2_start != null task_2 == null) (task1_start = task2_start)");
			} else if ((((topTask.getStartDate() != null)) && (topTask.getEndDate() != null))
					&& (((bottomTask.getStartDate() != null)) && (bottomTask.getEndDate() != null))
					&& (((topTask.getStartDate().equals(bottomTask.getStartDate())))) 
					&& (topTask.getEndDate().isAfter(bottomTask.getEndDate()))) {
				list.set(listIndex, bottom.get(bottomIndex));
				bottomIndex++;
				//System.out.println("test8 - (task1_start = task1_end = task2_start = task2_end != null) (task1_start = task2_start) (task1_end compare task2_end)");
			} else {
				list.set(listIndex, top.get(topIndex));
				topIndex++;
				//System.out.println("test9");
			}
			listIndex++;
		}

		ArrayList<Task> leftOver;
		int leftOverIndex;
		if (topIndex >= top.size()) {
			leftOver = bottom;
			leftOverIndex = bottomIndex;
		}
		else {
			leftOver = top;
			leftOverIndex = topIndex;
		}

		for (int i = leftOverIndex; i < leftOver.size(); i++) {
			list.set(listIndex, leftOver.get(i));
			listIndex++;
		}
	}
	public void sortListByDescription(ArrayList<Task> list) {
		DescriptionComparator descriptionComp = new DescriptionComparator();
		Collections.sort(list, descriptionComp);
		//for(int i = 0; i < list.size(); i++)
			//System.out.println("sort" + i + "\t" + list.get(i).getDescription());
	}
}
