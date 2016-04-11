package logic;

import java.util.ArrayList;

/**
 * This class keeps track of the successful commands typed by users
 * to allow and support undo and redo commands.
 * 
 * @@author A0140021J
 *
 */
public class HistoryManager {
	
	//This ArrayList stores the successful string commands input by users.
	private ArrayList<String> _previousUserInputList;
	
	//This ArrayList stores the old and new task after a user successfully entered a command.
	private ArrayList<RollbackItem> _undoList;
	
	//This ArrayList stores the old and new task after a user successfully undo a command.
	private ArrayList<RollbackItem> _redoList;
	
	//This variable is used to keep track of the previous and next string command previously entered by user.
	private int _previousUserInputCounter;
	
	//This is constructor of the class.
	public HistoryManager() {
		_previousUserInputList = new ArrayList<String>();
		_undoList = new ArrayList<RollbackItem>();
		_redoList = new ArrayList<RollbackItem>();
		_previousUserInputCounter = 0;
	}
	
	/**
	 * This is setter method for previous user input counter.
	 * 
	 * @param previousUserInputCounter	This number represents the distance from current point to the history command executed previously.
	 */
	public void setPreviousUserInputCounter(int previousUserInputCounter) {
		_previousUserInputCounter = previousUserInputCounter;
	}

	/**
	 * This is setter method for previous user input list.
	 * 
	 * @param previousUserInputList	This is the list of successful commands executed by user.
	 */
	public void setPreviousUserInputList(ArrayList<String> previousUserInputList) {
		_previousUserInputList = new ArrayList<String>();
		_previousUserInputList.addAll(previousUserInputList);
	}
	
	/**
	 *  This is setter method for undo list.
	 *  
	 * @param undoList	This is the list RollbackItem to support undo command.
	 */
	public void setUndoList(ArrayList<RollbackItem> undoList) {
		_undoList = new ArrayList<RollbackItem>();
		_undoList.addAll(undoList);
	}

	/**
	 *  This is setter method for redo list.
	 *  
	 * @param undoList	This is the list RollbackItem to support redo command.
	 */
	public void setRedoList(ArrayList<RollbackItem> redoList) {
		_redoList = new ArrayList<RollbackItem>();
		_redoList.addAll(redoList);
	}

	/**
	 * This is getter method for previous user input counter.
	 * 
	 * @return	previous user input counter.
	 */
	public int getPreviousUserInputCounter() {
		return _previousUserInputCounter;
	}
	
	/**
	 * This is getter method for previous user input list.
	 * 
	 * @return	previous user input counter.
	 */
	public ArrayList<String> getPreviousUserInputList() {
		return _previousUserInputList;
	}

	/**
	 * This is getter method for undo list.
	 * 
	 * @return	undo list
	 */
	public ArrayList<RollbackItem> getUndoList() {
		return _undoList;
	}

	/**
	 * This is getter method for redo list
	 * 
	 * @return	redo list
	 */
	public ArrayList<RollbackItem> getRedoList() {
		return _redoList;
	}
}
