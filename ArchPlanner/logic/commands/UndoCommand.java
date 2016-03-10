package logic.commands;

import logic.Task;

import java.util.ArrayList;

public class UndoCommand extends Command {

	private int _times;

	private final int DEFAULT_NUMBER_OF_TIMES = 1;


	public UndoCommand() {
		this._times = DEFAULT_NUMBER_OF_TIMES;
	}

	@Override
	public boolean execute(ArrayList<Task> mainList, ArrayList<Task> viewList, ArrayList<String> tagsList) {
		return false;
	}

	public UndoCommand(int _times) {
		this._times = _times;
	}

	public void setTimes(int times) {
		assert(times >= 1); //times can only be 1 or greater
		_times = times;
	}


	public void setTimes() {
		_times = DEFAULT_NUMBER_OF_TIMES;
	}

	public int getTimes() {
		return _times;
	}
}
