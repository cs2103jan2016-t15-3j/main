package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;

public interface Command {

	abstract boolean execute(ListsManager listsManager, HistoryManager historyManager);
	abstract boolean execute();
}
