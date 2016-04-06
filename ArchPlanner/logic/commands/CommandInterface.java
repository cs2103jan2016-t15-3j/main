package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

public interface CommandInterface {
	public CommandInterface execute();
	public CommandInterface execute(ListsManager listsManager, Storage storage);
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager);
	public String getMessage();
}