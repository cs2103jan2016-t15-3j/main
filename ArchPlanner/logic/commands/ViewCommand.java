package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;

import java.util.ArrayList;

public class ViewCommand extends Command {

	private ArrayList<String> _tagList;
	private boolean _viewAll;
	private boolean _viewDone;
	private boolean _viewUndone;
	private boolean _viewOverdue;


	public ViewCommand(ArrayList<String> tagList, boolean viewAll, boolean viewDone, boolean viewUndone, boolean viewOverdue) {
		this._tagList = tagList;
		this._viewAll = viewAll;
		this._viewDone = viewDone;
		this._viewUndone = viewUndone;
		this._viewOverdue = viewOverdue;
	}

	//view show the result of CURRENT list. Except for view all.
	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
//		if ((_description != null) && (_description.equals("all"))) {
//			listsManager.setViewList("VIEW_ALL");
//			return true;
//		} else if ((_description != null) && (_description.equals("done"))) {
//			listsManager.setViewList("VIEW_DONE");
//			return true;
//		}
		return false;
	}

	public ArrayList<String> getTagList() {
		return _tagList;
	}

	public void setTagList(ArrayList<String> tagList) {
		this._tagList = tagList;
	}

	public boolean isViewAll() {
		return _viewAll;
	}

	public void setViewAll(boolean viewAll) {
		this._viewAll = viewAll;
	}

	public boolean isViewDone() {
		return _viewDone;
	}

	public void setViewDone(boolean viewDone) {
		this._viewDone = viewDone;
	}

	public boolean isViewUndone() {
		return _viewUndone;
	}

	public void setViewUndone(boolean viewUndone) {
		this._viewUndone = viewUndone;
	}

	public boolean isViewOverdue() {
		return _viewOverdue;
	}

	public void setViewOverdue(boolean viewOverdue) {
		this._viewOverdue = viewOverdue;
	}
}