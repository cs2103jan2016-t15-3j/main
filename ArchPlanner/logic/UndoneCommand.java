package logic;

public class UndoneCommand implements Command {

	private int _id;

	public void setId(int id) {
		assert(id >= 1); //id can only be 1 or greater
		_id = id;
	}

	public int getId() {
		return _id;
	}
}
