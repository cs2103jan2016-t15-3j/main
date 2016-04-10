package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

/**
 * This class is used for InvalidCommand
 * 
 * @@author A0140021J
 *
 */
public class InvalidCommand implements CommandInterface {

	//This is the message of an InvalidCommand object to be displayed.
    private String _message;

  //This is constructor of the class.
    public InvalidCommand(String message) {
    	_message = message;
    }
    /**
	 * This is setter method for InvalidCommand's message.
	 * 
	 * @param message This will be the message of the InvalidCommand.
	 */
	public void setMessage(String message) {
        _message = message;
    }
	
	/**
	 * This is getter method for InvalidCommand's message.
	 * 
	 * @return message.
	 */
	public String getMessage() {
        return _message;
    }
    
	/**
	 * This method will not be called.
	 */
    public CommandInterface execute() {
		return null;
	}
    
    /**
	 * This method will not be called.
	 */
    public CommandInterface execute(ListsManager listsManager, Storage storage) {
		return null;
	}
    
    /**
	 * This method will not be called.
	 */
	public CommandInterface execute(ListsManager listsmanager, HistoryManager historyManager) {
		return null;
	}
}
