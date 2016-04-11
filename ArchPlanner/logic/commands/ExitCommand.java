package logic.commands;

import java.util.logging.Logger;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Logic;
import storage.Storage;

/**
 * This class is used to execute exit command.
 * 
 * @@author A0140021J
 *
 */
public class ExitCommand implements CommandInterface {

	//This is the logger used to log and observe the changes when program runs.
	static Logger log = Logger.getLogger(Logic.class.getName());

	//These are constant string variables for logging.
	private final String  LOGGER_MESSAGE_EXECUTING_EXIT_COMMAND = "Executing exit command...";
	private final String  LOGGER_MESSAGE_COMPLETED_EXIT_COMMAND = "Program has ended.";

	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";

	/**
	 * This is getter method for ExitCommand's message.
	 * 
	 * @return	empty string.
	 */
	public String getMessage() {
		return STRING_EMPTY;
	}

	/**
	 * This method is used to execute exit command and exit from the program.
	 */
	public CommandInterface execute() {
		log.info(LOGGER_MESSAGE_EXECUTING_EXIT_COMMAND);
		log.info(LOGGER_MESSAGE_COMPLETED_EXIT_COMMAND);
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
