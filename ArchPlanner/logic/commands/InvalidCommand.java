package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

public class InvalidCommand implements Command {

    private String _errorMessage;

    public Command execute() {
		return null;
	}
    
    public Command execute(Storage storage) {
		return null;
    }
    
	public Command execute(ListsManager listsmanager, HistoryManager historyManager) {
		return null;
	}

    public InvalidCommand(String _error_message) {
        _errorMessage = _error_message;
    }

    public String get_error_message() {
        return _errorMessage;
    }

    public void set_error_message(String errorMessage) {
        _errorMessage = errorMessage;
    }
}
