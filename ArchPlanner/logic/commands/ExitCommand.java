package logic.commands;


import logic.HistoryManager;
import logic.ListsManager;

public class ExitCommand implements Command {

	public boolean execute() {
		System.exit(0);
		return true;
	}
	
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		return false;
	}
}
