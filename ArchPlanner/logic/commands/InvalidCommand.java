//@@author A0140021J
package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

public class InvalidCommand implements CommandInterface {

    private String _errorMessage;

    public CommandInterface execute() {
		return null;
	}
    
    public CommandInterface execute(ListsManager listsManager, Storage storage) {
		return null;
	}
    
	public CommandInterface execute(ListsManager listsmanager, HistoryManager historyManager) {
		return null;
	}

    public InvalidCommand(String errorMessage) {
        _errorMessage = errorMessage;
    }

    public String getMessage() {
        return _errorMessage;
    }

    public void setMessage(String errorMessage) {
        _errorMessage = errorMessage;
    }
}
