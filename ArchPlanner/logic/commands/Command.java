package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;

import java.util.logging.Logger;

public interface Command {
	public boolean execute();
	public boolean execute(ListsManager listsManager, HistoryManager historyManager);
}
