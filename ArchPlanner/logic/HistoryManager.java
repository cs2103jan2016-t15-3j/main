package logic;

import java.util.ArrayList;

public class HistoryManager {
	private ArrayList<RollbackItem> _undoList = new ArrayList<RollbackItem>();
	private ArrayList<RollbackItem> _redoList = new ArrayList<RollbackItem>();

	public void setUndoList(ArrayList<RollbackItem> undoList) {
		_undoList = new ArrayList<RollbackItem>();
		_undoList.addAll(undoList);
	}

	public void setRedoList(ArrayList<RollbackItem> redoList) {
		_redoList = new ArrayList<RollbackItem>();
		_redoList.addAll(redoList);
	}

	public ArrayList<RollbackItem> getUndoList() {
		return _undoList;
	}

	public ArrayList<RollbackItem> getRedoList() {
		return _redoList;
	}
}
