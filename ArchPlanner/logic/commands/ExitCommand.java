package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

public class ExitCommand implements CommandInterface {

	public CommandInterface execute() {
		System.exit(0);
		return null;
	}
	
	public CommandInterface execute(ListsManager listsManager, Storage storage) {
		return null;
	}
	
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		return null;
	}

	public String getMessage() {
		return "";
	}
}
