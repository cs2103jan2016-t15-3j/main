package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;

public class InvalidCommand implements Command {

    private String _error_message;

    public Command execute() {
		return null;
	}
    
	public Command execute(ListsManager listsmanager, HistoryManager historyManager) {
		return null;
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
}
