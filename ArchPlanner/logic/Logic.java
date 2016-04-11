package logic;

import java.util.ArrayList;
import java.util.logging.Logger;

import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

import parser.Parser;

/**
 * This class interact with the UI and process the operation, 
 * followed by updating the changes to storage.
 * 
 * @@author A0140021J
 *
 */
public class Logic {
	
	//These are enum variables of command type.
	public enum COMMAND_TYPE {
		ADD, DELETE, EDIT, EXIT, UNDO, REDO, DONE, UNDONE, VIEW, SET, INVALID
	}

	//This is the logger used to log and observe the changes when program runs.
	static Logger log = Logger.getLogger(Logic.class.getName());

	//This is the Storage object used for accessing and saving data.
	private Storage _storage;

	//This is the ListsManager object used for accessing managing the lists of tasks and viewing state.
	private ListsManager _listsManager;

	//This is the HistoryManager object used for accessing and managing the history of successful commands executed by users.
	private HistoryManager _historyManager;

	//This is the Parser object used to translate string commands entered by user to command object.
	private Parser _parser;

	//These are the constant string variables of the category type.
	private final String STRING_DEADLINES = "Deadlines";
	private final String STRING_EVENTS = "Events";
	private final String STRING_TASKS = "Tasks";

	//These are the constant string variables of the view type.
	private final String STRING_DONE = "Done";
	private final String STRING_UNDONE = "Undone";
	private final String STRING_OVERDUE = "Overdue";

	//These are constant string variables for logging.
	private final String  LOGGER_MESSAGE_ANALYZING_COMMAND_INPUT = "Analyzing command input...";
	private final String  LOGGER_MESSAGE_CONFIRMED = "confirmed.";
	
	//These constant string used to append messages for readability.
	private final String STRING_EMPTY = "";
	private final String STRING_SINGLE_SPACE = " ";

	//This is constructor of the class.
	public Logic() {
		_storage = new Storage();
		_listsManager = new ListsManager();
		_historyManager = new HistoryManager();
		_parser = new Parser();
		loadFile();
	}

	/**
	 * This method is used to load the content in the storage file to the main list
	 * and populate the lists in ListsManager
	 */
	public void loadFile() {
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList = _storage.getMasterList();
		_listsManager.setUpLists(mainList);
	}

	/**
	 * This method is used to refresh the view list for any overdue task
	 */
	public void updateViewList() {
		_listsManager.refreshViewList();
	}

	/**
	 * This method is used to execute command after passing the string command to parser and got command object.
	 * 
	 * @param userInput This is the string command entered by user.
	 * 
	 * @return command object.
	 */
	public CommandInterface executeCommand(String userInput) {
		ArrayList<Tag> tagsListClone = getTagsListClone();

		CommandInterface commandInput = _parser.parseCommand(userInput, _listsManager.getViewList().size(), 
				_historyManager.getUndoList().size(), _historyManager.getRedoList().size(), tagsListClone);

		CommandInterface commandReturn = runCommand(commandInput);

		return save(userInput, commandInput, commandReturn);
	}

	/**
	 * This method is used to update ListsManager the selected category type and reset view type and update all the lists.
	 * 
	 * @param selectedCategory This is the category type.
	 */
	public void setSelectedCategory(CATEGORY_TYPE selectedCategory) {
		_listsManager.setViewType(VIEW_TYPE.ALL);
		_listsManager.setCategoryType(selectedCategory);
		_listsManager.updateLists();
	}

	/**
	 * This method is used to update the selected tags list in ListsManager.
	 * 
	 * @param tagName This is the name of the selected tag
	 * 
	 * @param isSelected This is the variable indicates whether the tag is selected.
	 */
	public void setSelectedTag(String tagName, boolean isSelected) {
		if (!isSelected) {
			updateTagsList(tagName);
		}
		_listsManager.updateSelectedTagsList(tagName, isSelected);
		_listsManager.setViewType(VIEW_TYPE.ALL);
		_listsManager.updateLists();
	}

	/**
	 * This method is used to retrieve the string of the previously executed command entered by user in HistoryManager.
	 * 
	 * @return string of the previously executed command by user.
	 */
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
		return STRING_EMPTY;
	}

	/**
	 * This method is used to retrieve the string of the next executed command entered by user in HistoryManager.
	 * 
	 * @return string of the next executed command by user.
	 */
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
		return STRING_EMPTY;
	}

	/**
	 * This method is used to differentiate the category type.
	 * 
	 * @param selectedCategory This is the category selected.
	 * 
	 * @return category type.
	 */
	public CATEGORY_TYPE getCategoryType(String selectedCategory) {
		switch (selectedCategory) {
		case STRING_DEADLINES : 
			return CATEGORY_TYPE.DEADLINES;
		case STRING_EVENTS : 
			return CATEGORY_TYPE.EVENTS;
		case STRING_TASKS : 
			return CATEGORY_TYPE.TASKS;
		default : 
			return CATEGORY_TYPE.ALL;
		}
	}

	/**
	 * This method is used to retrieve the selected view type.
	 * 
	 * @return string view type.
	 */
	public String getSelectedView() {
		VIEW_TYPE viewType = _listsManager.getViewType();
		switch (viewType) {

		case ALL : 
			return STRING_EMPTY;
		case DONE: 
			return STRING_DONE;
		case UNDONE : 
			return STRING_UNDONE;
		default : 
			return STRING_OVERDUE;
		}
	}

	/**
	 * This method is used to retrieve the selected category in ListsManager.
	 * 
	 * @return selected category.
	 */
	public CATEGORY_TYPE getSelectedCategory() {
		return _listsManager.getCategoryType();
	}

	/**
	 * This method is used to retrieve current view type.
	 * 
	 * @return current view type.
	 */
	public String getCurrentViewType() {
		String selectedCategory = getSelectedCategory().toString();
		String currentViewType = selectedCategory + STRING_SINGLE_SPACE + getSelectedView() + STRING_SINGLE_SPACE 
				+ _listsManager.getCurrentViewType();
		return currentViewType;
	}

	/**
	 * This method is used to retrieve view list in ListsManager.
	 * 
	 * @return view list.
	 */
	public ArrayList<Task> getViewList() {
		return _listsManager.getViewList();
	}

	/**
	 * This method is used to retrieve main list in ListsManager.
	 * 
	 * @return main list
	 */
	public ArrayList<Task> getMainList() {
		return _listsManager.getMainList();
	}

	/**
	 * This method is used to retrieve tags list in ListsManager.
	 * 
	 * @return tags list.
	 */
	public ArrayList<Tag> getTagsList() {
		return _listsManager.getTagsList();
	}

	/**
	 * This method is used to retrieve index list in ListsManager.
	 * 
	 * @return index lists.
	 */
	public ArrayList<Integer> getIndexList() {
		return _listsManager.getIndexList();
	}

	/**
	 * This method is used to create a clone list of the tags list in ListsManager.
	 * 
	 * @return tags list clone
	 */
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

	/**
	 * This method is used to set tag's isSelected to false if the tag is no selected.
	 * 
	 * @param tagName This is the name of the tag.
	 */
	private void updateTagsList(String tagName) {
		for (int i = 0; i < _listsManager.getTagsList().size(); i++) {
			Tag tag = _listsManager.getTagsList().get(i);
			if (tag.getName().equals(tagName)) {
				tag.setIsSelected(false);
			}
		}
	}

	/**
	 * This method is used to differentiate the type of command and call the relevant execute method of the command.
	 * 
	 * @param commandInput This is the command object that the user is executing.
	 * 
	 * @return command object.
	 */
	private CommandInterface runCommand(CommandInterface commandInput) {

		log.info(LOGGER_MESSAGE_ANALYZING_COMMAND_INPUT);
		String strCommandType = commandInput.getClass().getSimpleName();

		COMMAND_TYPE commandType = getCommandType(strCommandType);
		log.info(commandType.toString() + STRING_SINGLE_SPACE + LOGGER_MESSAGE_CONFIRMED);

		if (commandType.equals(COMMAND_TYPE.EXIT)) {
			return commandInput.execute();
		} else if (commandType.equals(COMMAND_TYPE.SET)) {
			return commandInput.execute(_listsManager, _storage);
		} else {
			return commandInput.execute(_listsManager, _historyManager);
		}
	}

	/**
	 * This method is used to differentiate the command type.
	 * 
	 * @param strCommandType This is the string command type.
	 * 
	 * @return command type.
	 */
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

	/**
	 * This method is used to save the updated main list in ListsManager to the file through Storage.
	 * 
	 * @param userInput This is the string command entered by user.
	 * 
	 * @param commandInput This is the command object the user is executing.
	 * 
	 * @param commandReturn This is the command object returned after the been executed.
	 * 
	 * @return the command object the user is executing.
	 */
	private CommandInterface save(String userInput, CommandInterface commandInput, CommandInterface commandReturn) {
		if (commandReturn != null && commandReturn instanceof InvalidCommand) {
			return commandReturn;
		} else if (commandReturn == null && commandInput instanceof InvalidCommand) {
			return commandInput;
		}
		updateUserInputList(userInput);
		_storage.writeStorageFile(_listsManager.getMainList());
		return commandInput;
	}

	/**
	 * This method is used to update the user input list in HistoryManager.
	 * 
	 * @param userInput This is the string command entered by user.
	 */
	private void updateUserInputList(String userInput) {
		_historyManager.getPreviousUserInputList().add(userInput);
		_historyManager.setPreviousUserInputCounter(-1);
	}

	/**
	 * This method is used to test commands executed on logic component
	 * 
	 * @param commandInput This is the command object to be executed for testing.
	 * 
	 * @return The command object.
	 */
	public CommandInterface logicTestEnvironment(CommandInterface commandInput) {
		return runCommand(commandInput);
	}
}