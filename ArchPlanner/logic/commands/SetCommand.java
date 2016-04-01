package logic.commands;

import logic.HistoryManager;
import logic.ListsManager;

/**
 * Created by lifengshuang on 4/1/16.
 */
public class SetCommand implements Command {

    private String _filePath;

    public SetCommand(String filePath) {
        _filePath = filePath;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public boolean execute(ListsManager listsManager, HistoryManager historyManager) {
        return false;
    }
}
