package logic.commands;

import logic.Logic;

import java.util.ArrayList;

public class SortCommand implements Command {

//    public final String _VALID_SORT_TYPE[] = {"DESCRIPTION", "TAG", "START_DATE_TIME", "END_DATE_TIME", "COMPLETION", "OVERDUE"};

    private ArrayList<String> _sortTypeList;
    private String _sortType;

    public SortCommand(String _sortType) {
        this._sortType = _sortType;
    }

    public void setSortType(ArrayList<String> sortTypeList) {
        //Fengshuang: I removed assertion because feedback will check it.
        _sortTypeList = sortTypeList;
    }

    public String get_sortType() {
        return _sortType;
    }

    public void set_sortType(String _sortType) {
        this._sortType = _sortType;
    }

    public ArrayList<String> getSortType() {
        return _sortTypeList;
    }

    @Override
    public void execute(Logic logic) {

    }
}
