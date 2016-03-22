package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;

public interface Command {

	public boolean execute(ListsManager listsManager, HistoryManager historyManager);
}
