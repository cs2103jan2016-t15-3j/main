//@@author A0140021J
package logic;

import java.util.ArrayList;

public class HistoryManager {
	private int _previousUserInputCounter;
	private ArrayList<String> _previousUserInputList;
	private ArrayList<RollbackItem> _undoList;
	private ArrayList<RollbackItem> _redoList;
	
	public HistoryManager() {
		_previousUserInputCounter = 0;
		_previousUserInputList = new ArrayList<String>();
		_undoList = new ArrayList<RollbackItem>();
		_redoList = new ArrayList<RollbackItem>();
	}
	
	public void setPreviousUserInputCounter(int previousUserInputCounter) {
		_previousUserInputCounter = previousUserInputCounter;
	}
	
	public void setPreviousUserInputList(ArrayList<String> previousUserInputList) {
		_previousUserInputList = new ArrayList<String>();
		_previousUserInputList.addAll(previousUserInputList);
	}

	public void setUndoList(ArrayList<RollbackItem> undoList) {
		_undoList = new ArrayList<RollbackItem>();
		_undoList.addAll(undoList);
	}

	public void setRedoList(ArrayList<RollbackItem> redoList) {
		_redoList = new ArrayList<RollbackItem>();
		_redoList.addAll(redoList);
	}
	
	public int getPreviousUserInputCounter() {
		return _previousUserInputCounter;
	}
	
	public ArrayList<String> getPreviousUserInputList() {
		return _previousUserInputList;
	}

	public ArrayList<RollbackItem> getUndoList() {
		return _undoList;
	}

	public ArrayList<RollbackItem> getRedoList() {
		return _redoList;
	}
}
