package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;

public interface Command {
	public Command execute();
	public Command execute(ListsManager listsManager, HistoryManager historyManager);
}
