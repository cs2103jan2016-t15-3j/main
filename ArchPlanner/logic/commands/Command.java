package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;

public abstract class Command {

	public abstract boolean execute(ListsManager listsManager, HistoryManager historyManager);
}
