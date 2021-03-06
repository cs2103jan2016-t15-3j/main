package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

/**
 * This class is an interface that will be implemented by all the Command objects.
 * 
 * @@author A0140021J
 *
 */
public interface CommandInterface {
	CommandInterface execute();
	CommandInterface execute(ListsManager listsManager, Storage storage);
	CommandInterface execute(ListsManager listsManager, HistoryManager historyManager);
	String getMessage();
}