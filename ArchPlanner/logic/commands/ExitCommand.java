package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

public class ExitCommand implements Command {

	public Command execute() {
		System.exit(0);
		return null;
	}
	
	public Command execute(Storage storage) {
		return null;
	}
	
	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		return null;
	}
}
