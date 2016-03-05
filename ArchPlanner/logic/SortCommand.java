package logic;

import java.util.ArrayList;

public class SortCommand implements Command {

	public final String _AVAILABLE_SORT_TYPE[] = {"DESCRIPTION", "TAG", "START_DATE_TIME", "END_DATE_TIME", "COMPLETION", "OVERDUE"};

	public ArrayList<String> _sortTypeList;

	public void setSortType(ArrayList<String> sortType) {
		//sortype must be one of them
		assert(sortType.equals("DESCRIPTION") || sortType.equals("TAG") || sortType.equals("START_DATE_TIME")
				|| sortType.equals("END_DATE_TIME") || sortType.equals("COMPLETION") || sortType.equals("OVERDUE"));

		//if (!isValidSortType(sortType)) {
			_sortTypeList = sortType;
		//}
	}

	public ArrayList<String> getSortType() {
		return _sortTypeList;
	}
	/*
	private boolean isValidSortType(ArrayList<String> sortTypeList) {
		
		int count = 0;
		
		boolean isValidSortTypeList = false;
		
		for (int i = 0; i < sortTypeList.size(); i++) {
			for (int j = 0; j < _AVAILABLE_SORT_TYPE.length; j++) {
				if (sortTypeList.get(i).equals(_AVAILABLE_SORT_TYPE[j])) {
					count++;
				}
			}
		}
		
		isValidSortTypeList = (count == sortTypeList.size()) ? true : false;
		
		return isValidSortTypeList;
	}
	*/
}
