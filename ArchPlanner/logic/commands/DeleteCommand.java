package logic.commands;

import logic.Logic;

public class DeleteCommand implements Command {
	
	private int _id;

	public DeleteCommand(int _id) {
		this._id = _id;
	}

	@Override
	public void execute(Logic logic) {

	}
	public void setId(int id) {
		assert(id >= 1); //id can only be 1 or greater
		_id = id;
	}


	public int getId() {
		return _id;
	}
}
