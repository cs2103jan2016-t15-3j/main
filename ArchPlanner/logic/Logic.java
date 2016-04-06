package logic;

import java.util.ArrayList;

import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

import parser.Parser;

/**
 * This class interact with the UI and process the operation, 
 * followed by updating the changes to storage
 * 
 * @author riyang
 *
 */
public class Logic {
	enum COMMAND_TYPE {
		ADD, DELETE, EDIT, EXIT, UNDO, REDO, DONE, UNDONE, VIEW, SET, INVALID
	};

	private Storage _storage;
	private ListsManager _listsManager;
	private HistoryManager _historyManager;
	private Parser _parser;

	private final String DEADLINES = "Deadlines";
	private final String EVENTS = "Events";
	private final String TASKS = "Tasks";
	private final String DONE = "Done";
	private final String UNDONE = "Undone";
	private final String OVERDUE = "Overdue";
	private final String EMPTY_STRING = "";

	public Logic() {
		_storage = new Storage();
		_listsManager = new ListsManager();
		_historyManager = new HistoryManager();
		_parser = new Parser();
	}

	public void loadFile() {
		_storage.loadStorageFile();
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList = _storage.getMasterList();
		_listsManager.setUpLists(mainList);
	}

	public void updateLists() {
		_listsManager.updateLists();
	}

	public CommandInterface executeCommand(String userInput) {
		ArrayList<Tag> tagsListClone = getTagsListClone();

		CommandInterface commandInput = _parser.parseCommand(userInput, _listsManager.getViewList().size(), 
				_historyManager.getUndoList().size(), _historyManager.getRedoList().size(), 
				tagsListClone);
		CommandInterface commandReturn = runCommand(commandInput);

		return save(userInput, commandInput, commandReturn);
	}

	public void setSelectedCategory(CATEGORY_TYPE selectedCategory) {
		System.out.print(selectedCategory);
		_listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		_listsManager.setCategoryType(selectedCategory);
		_listsManager.updateLists();
	}

	public void setSelectedTag(String tagName, boolean isSelected) {
		if (isSelected) {
			_listsManager.updateSelectedTagsList(tagName, true);
		} else {
			_listsManager.updateSelectedTagsList(tagName, false);
			updateTagsList(tagName);
		}
		_listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		_listsManager.updateLists();
	}

	public String getPreviousUserInput() {
		_historyManager.setPreviousUserInputCounter(_historyManager.getPreviousUserInputCounter() + 1);
		int previousUserInputListIndex = _historyManager.getPreviousUserInputList().size() - 
				_historyManager.getPreviousUserInputCounter() - 1;
		if ((previousUserInputListIndex >= 0) 
				&& (_historyManager.getPreviousUserInputList().size() > previousUserInputListIndex)) {
			String previousUserInput = _historyManager.getPreviousUserInputList().get(previousUserInputListIndex);
			return previousUserInput;
		}
		_historyManager.setPreviousUserInputCounter(_historyManager.getPreviousUserInputList().size() - 1);
		return "";
	}

	public String getNextUserInput() {
		_historyManager.setPreviousUserInputCounter(_historyManager.getPreviousUserInputCounter() - 1);
		int previousUserInputListIndex = _historyManager.getPreviousUserInputList().size() 
				- _historyManager.getPreviousUserInputCounter() - 1;
		if ((previousUserInputListIndex >= 0) && 
				(_historyManager.getPreviousUserInputList().size() > previousUserInputListIndex)) {
			String previousUserInput = _historyManager.getPreviousUserInputList().get(previousUserInputListIndex);
			return previousUserInput;
		}
		_historyManager.setPreviousUserInputCounter(-1);
		return "";
	}

	public CATEGORY_TYPE getSelectedCategory() {
		return _listsManager.getCategoryType();
	}

	public String getCurrentViewType() {
		String currentViewType = getSelectedCategory() + " " + getSelectedView() + " " 
				+ _listsManager.getCurrentViewType();
		return currentViewType;
	}

	public ArrayList<Task> getViewList() {
		return _listsManager.getViewList();
	}

	public ArrayList<Task> getMainList() {
		return _listsManager.getMainList();
	}

	public ArrayList<Tag> getTagsList() {
		return _listsManager.getTagsList();
	}

	public ArrayList<Integer> getIndexList() {
		return _listsManager.getIndexList();
	}

	private ArrayList<Tag> getTagsListClone() {
		ArrayList<Tag> tagsListClone = new ArrayList<Tag>();
		for (int i = 0; i < _listsManager.getTagsList().size(); i++) {
			String tagName = _listsManager.getTagsList().get(i).getName();
			boolean tagIsSelected = _listsManager.getTagsList().get(i).getIsSelected();
			Tag tag = new Tag(tagName, tagIsSelected);
			tagsListClone.add(tag);
		}
		return tagsListClone;
	}

	private void updateTagsList(String tagName) {
		for (int i = 0; i < _listsManager.getTagsList().size(); i++) {
			Tag tag = _listsManager.getTagsList().get(i);
			if (tag.getName().equals(tagName)) {
				tag.setIsSelected(false);
			}
		}
	}

	private CommandInterface runCommand(CommandInterface commandInput) {
		String strCommandType = commandInput.getClass().getSimpleName();

		COMMAND_TYPE commandType = getCommandType(strCommandType);
		if (commandType.equals(COMMAND_TYPE.EXIT)) {
			return commandInput.execute();
		} else if (commandType.equals(COMMAND_TYPE.SET)) {
			return commandInput.execute(_listsManager, _storage);
		} else {
			return commandInput.execute(_listsManager, _historyManager);
		}
	}

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
		} else if (strCommandType.equals("SetCommand")) {
			return COMMAND_TYPE.SET;
		}else {
			return COMMAND_TYPE.INVALID;
		}
	}

	private CommandInterface save(String userInput, CommandInterface commandInput, CommandInterface commandReturn) {
		if ((commandReturn != null) && (commandReturn instanceof InvalidCommand)) {
			return commandReturn;
		} else if ((commandReturn == null) && (commandInput instanceof InvalidCommand)) {
			return commandInput;
		}
		updateUserInputList(userInput);
		_storage.writeStorageFile(_listsManager.getMainList());
		return commandInput;
	}

	private void updateUserInputList(String userInput) {
		_historyManager.getPreviousUserInputList().add(userInput);
		_historyManager.setPreviousUserInputCounter(-1);
	}

	public CATEGORY_TYPE getCategoryType(String selectedCategory) {
		switch (selectedCategory) {
		case DEADLINES : 
			return CATEGORY_TYPE.CATEGORY_DEADLINES;
		case EVENTS : 
			return CATEGORY_TYPE.CATEGORY_EVENTS;
		case TASKS : 
			return CATEGORY_TYPE.CATEGORY_TASKS;
		default : 
			return CATEGORY_TYPE.CATEGORY_ALL;
		}
	}

	private String getSelectedView() {
		VIEW_TYPE viewType = _listsManager.getViewType();
		switch (viewType) {

		case VIEW_ALL : 
			return EMPTY_STRING;
		case VIEW_DONE: 
			return DONE;
		case VIEW_UNDONE : 
			return UNDONE;
		default : 
			return OVERDUE;
		}
	}
}