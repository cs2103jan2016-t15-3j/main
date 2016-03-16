package logic;

import java.util.ArrayList;

import logic.commands.Command;
import logic.commands.InvalidCommand;
import storage.Storage;

/**
 * This class interact with the UI and process the operation, 
 * followed by updating the changes to storage
 * 
 * @author riyang
 *
 */
public class Logic {
/*
	enum COMMAND_TYPE {
		ADD, DELETE, EDIT, EXIT, UNDO, REDO, DONE, UNDONE, VIEW, SEARCH, SORT, INVALID
	};
*/
	//private ArrayList<Task> _mainList = new ArrayList<Task>();
	//private ArrayList<Task> _viewList = new ArrayList<Task>();
	//private ArrayList<String> _tagsList = new ArrayList<String>();
	
	Storage storage = new Storage();
	ListsManager listsManager = new ListsManager();
	HistoryManager historyManager = new HistoryManager();

	public void loadFile() {
		storage.loadStorageFile();
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList = storage.getMasterList();
		listsManager.updateLists(mainList);
	}

	public boolean executeCommand(Command commandObj) {
		if (commandObj instanceof InvalidCommand) {
			return false;
		}
		commandObj.execute(listsManager, historyManager);
		save();
		return true;
	}
	
	/*
	String strCommandType = commandObj.getClass().getSimpleName();

	COMMAND_TYPE commandType = getCommandType(strCommandType);

	switch (commandType) {
	case ADD :
		return executeAddCommand(commandObj);
	case DELETE :
		return executeDeleteCommand(commandObj);
	case EDIT :
		return executeEditCommand(commandObj);
	case EXIT :
		return executeExitCommand(commandObj);
	case UNDO :
		break;
	case REDO :
		break;
	case DONE :
		return executeDoneCommand(commandObj);
	case UNDONE :
		return executeUndoneCommand(commandObj);
	case VIEW :
		break;
	case SEARCH :
		return executeSearchCommand(commandObj);
	case SORT :
		break;
	case INVALID :
		return executeInvalidCommand(commandObj);
	default:
		throw new Error("Invalid command");
	}
	return false;
	*/
/*
private COMMAND_TYPE getCommandType(String strCommandType) {
	if (strCommandType.equals("AddCommand")) {
		return COMMAND_TYPE.ADD;
	} else if (strCommandType.equals("DeleteCommand")) {
		return COMMAND_TYPE.DELETE;
	} else if (strCommandType.equals("EditCommand")) {
		return COMMAND_TYPE.EDIT;
	} else if (strCommandType.equals("ExitCommand")) {
		return COMMAND_TYPE.EXIT;
	} else if (strCommandType.equals("UndoCommand")) {
		return COMMAND_TYPE.UNDO;
	} else if (strCommandType.equals("RedoCommand")) {
		return COMMAND_TYPE.REDO;
	} else if (strCommandType.equals("DoneCommand")) {
		return COMMAND_TYPE.DONE;
	} else if (strCommandType.equals("UndoneCommand")) {
		return COMMAND_TYPE.UNDONE;
	} else if (strCommandType.equals("ViewCommand")) {
		return COMMAND_TYPE.VIEW;
	} else if (strCommandType.equals("SearchCommand")) {
		return COMMAND_TYPE.SEARCH;
	} else if (strCommandType.equals("SortCommand")) {
		return COMMAND_TYPE.SORT;
	} else {
		return COMMAND_TYPE.INVALID;
	}
}
*/

	public ArrayList<Task> getViewList() {
		return listsManager.getViewList();
	}

	public ArrayList<Task> getMainList() {
		return listsManager.getMainList();
	}

	public ArrayList<String> getTagsList() {
		return listsManager.getTagsList();
	}
	
	private void save() {
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList.addAll(listsManager.getMainList());
		storage.writeStorageFile(mainList);
	}
}