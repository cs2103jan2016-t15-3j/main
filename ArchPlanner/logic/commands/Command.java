package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

public interface Command {
	public Command execute();
	public Command execute(Storage storage);
	public Command execute(ListsManager listsManager, HistoryManager historyManager);
}
