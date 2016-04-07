//@@author A0140021J
package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

public interface CommandInterface {
	CommandInterface execute();
	CommandInterface execute(ListsManager listsManager, Storage storage);
	CommandInterface execute(ListsManager listsManager, HistoryManager historyManager);
	String getMessage();
}