package logic;

import java.util.ArrayList;
import java.util.Collections;

import logic.commands.Command;
import logic.commands.InvalidCommand;

/**
 * This class interact with the UI and process the operation, 
 * followed by updating the changes to storage
 * 
 * @author riyang
 *
 */
public class Logic {
	
	enum COMMAND_TYPE {
		ADD, DELETE, EDIT, EXIT, UNDO, REDO, DONE, UNDONE, VIEW, SEARCH, SORT, INVALID
	};

	private ArrayList<Task> _mainList = new ArrayList<Task>();
	private ArrayList<Task> _viewList = new ArrayList<Task>();
	private ArrayList<String> _tagsList = new ArrayList<String>();
	
	Storage storage = new Storage();

	public void loadFile() {
		storage.loadStorageFile();
		_mainList = storage.getMasterList();
		SortListEngine sort = new SortListEngine();
		setViewList(sort.getSortedList(_mainList));
		setTagsList();
	}

	private void setTagsList() {
		for (int i = 0; i < _mainList.size(); i++) {
			String tag = getTaskTag(_mainList, i);
			if ((tag != null) && (!_tagsList.contains(tag))) {
				_tagsList.add(tag);
			}
		}
		Collections.sort(_tagsList);
		_tagsList.add(0, "TimeLine");
		_tagsList.add(1 ,"Event");
		_tagsList.add(2, "Tasks");
	}

	public boolean executeCommand(Command commandObj) {
		if (commandObj instanceof InvalidCommand) {
			return false;
		}
		commandObj.execute(_mainList, _viewList, _tagsList);
		save();
		SortListEngine sort = new SortListEngine();
		setViewList(sort.getSortedList(_viewList));
		Collections.sort(_tagsList);
		_mainList = sort.getSortedList(_mainList);
		for (int i = 0; i < _mainList.size(); i++)
			System.out.println(_mainList.get(i).getDescription());
		System.out.println();
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
		return _viewList;
	}

	public ArrayList<Task> getMainList() {
		return _mainList;
	}

	public ArrayList<String> getTagsList() {
		return _tagsList;
	}

	private void setViewList(ArrayList<Task> list) {
		_viewList = new ArrayList<Task>(list);
	}

	private String getTaskTag(ArrayList<Task> list, int taskIndex) {
		String taskTag = list.get(taskIndex).getTag();
		return taskTag;
	}

	private void save() {
		storage.writeStorageFile(_mainList);
	}
}