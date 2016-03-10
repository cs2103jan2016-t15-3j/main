package logic.commands;

import java.util.ArrayList;

import logic.Task;

public class InvalidCommand extends Command {

    private String _error_message;

    public InvalidCommand() {
    }

    public InvalidCommand(String _error_message) {
        this._error_message = _error_message;
    }

    public String get_error_message() {
        return _error_message;
    }

    public void set_error_message(String _error_message) {
        this._error_message = _error_message;
    }

	@Override
	public boolean execute(ArrayList<Task> mainList, ArrayList<Task> viewList, ArrayList<String> tagsList) {
		return false;
	}
}
