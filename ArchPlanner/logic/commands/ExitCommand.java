package logic.commands;


import logic.HistoryManager;
import logic.ListsManager;

public class ExitCommand extends Command {


	@Override
	public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
		exit();
		return true;
	}

	private void exit() {
		System.exit(0);
	}
}
