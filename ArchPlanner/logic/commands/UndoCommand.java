package logic.commands;

import logic.Logic;

public class UndoCommand implements Command {

	private int _times;

	private final int DEFAULT_NUMBER_OF_TIMES = 1;

	@Override
	public void execute(Logic logic) {

	}

	public UndoCommand() {
		this._times = DEFAULT_NUMBER_OF_TIMES;
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
