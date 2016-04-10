package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

/**
 * This class is used to execute exit command.
 * 
 * @@author A0140021J
 *
 */
public class ExitCommand implements CommandInterface {
	
	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";
	
	/**
	 * This is getter method for ExitCommand's message.
	 * 
	 * @return empty string.
	 */
	public String getMessage() {
        return STRING_EMPTY;
    }
	
	/**
	 * This method is used to execute exit command and exit from the program.
	 */
	public CommandInterface execute() {
		System.exit(0);
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
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		return null;
	}
}
