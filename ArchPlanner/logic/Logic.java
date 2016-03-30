package logic;

import java.util.ArrayList;

import logic.commands.Command;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;
import storage.Storage;

import paser.Parser;

/**
 * This class interact with the UI and process the operation, 
 * followed by updating the changes to storage
 * 
 * @author riyang
 *
 */
public class Logic {
	enum COMMAND_TYPE {
		ADD, DELETE, EDIT, EXIT, UNDO, REDO, DONE, UNDONE, VIEW, INVALID
	};

	private Storage storage;
	private ListsManager listsManager;
	private HistoryManager historyManager;

	public Logic() {
		storage = new Storage();
		listsManager = new ListsManager();
		historyManager = new HistoryManager();
	}

	public void loadFile() {
		storage.loadStorageFile();
		ArrayList<Task> mainList = new ArrayList<Task>();
		mainList = storage.getMasterList();
		listsManager.setUpLists(mainList);
	}

	public Command executeCommand(String userInput) {
		Parser parser = new Parser();
		
		Command commandObj = parser.parseCommand(userInput, listsManager.getViewList().size(), 
				historyManager.getUndoList().size(), historyManager.getRedoList().size(), listsManager.getTagsList().clone());
				
		
		//Command commandObj = parser.parseCommand(userInput);
		
		boolean isSuccessful;
		//if (commandObj instanceof InvalidCommand) {
		//	return false;
		//}

		isSuccessful = runCommand(commandObj);

		if (isSuccessful) {
			historyManager.getPreviousUserInputList().add(userInput);
		}
		save(commandObj);
		return commandObj;
	}

	private boolean runCommand(Command commandObj) {
		String strCommandType = commandObj.getClass().getSimpleName();

		COMMAND_TYPE commandType = getCommandType(strCommandType);
		if (commandType.equals(COMMAND_TYPE.EXIT)) {
			return commandObj.execute();
		} else {
			return commandObj.execute(listsManager, historyManager);
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
		}else {
			return COMMAND_TYPE.INVALID;
		}
	}

	private void save(Command commandObj) {
		String strCommandType = commandObj.getClass().getSimpleName();

		COMMAND_TYPE commandType = getCommandType(strCommandType);
		if (commandObj.equals(COMMAND_TYPE.VIEW) || commandType.equals(COMMAND_TYPE.EXIT)) {
			return;
		}
		storage.writeStorageFile(listsManager.getMainList());
	}

	public void setSelectedCategory(String selectedCategory) {
		System.out.print(selectedCategory);
		CATEGORY_TYPE categoryType = getCategoryType(selectedCategory);
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.setCategoryType(categoryType);
		System.out.println(categoryType.toString());
		listsManager.updateLists();
	}

	public String getSelectedView() {
		VIEW_TYPE viewType = listsManager.getViewType();
		switch (viewType) {

		case VIEW_ALL : 
			return "";
		case VIEW_DONE: 
			return "Done";
		case VIEW_UNDONE : 
			return "Undone";
		default : 
			return "Overdue";
		}
	}

	public String getSelectedCategory() {
		CATEGORY_TYPE categoryType = listsManager.getCategoryType();
		switch (categoryType) {

		case CATEGORY_DEADLINE : 
			return "Deadlines";
		case CATEGORY_EVENT: 
			return "Events";
		case CATEGORY_TASK : 
			return "Tasks";
		default : 
			return "All";
		}
	}

	private CATEGORY_TYPE getCategoryType(String selectedCategory) {
		switch (selectedCategory) {
		case "Deadlines" : 
			return CATEGORY_TYPE.CATEGORY_DEADLINE;
		case "Events" : 
			return CATEGORY_TYPE.CATEGORY_EVENT;
		case "Tasks" : 
			return CATEGORY_TYPE.CATEGORY_TASK;
		default : 
			return CATEGORY_TYPE.CATEGORY_ALL;
		}
	}

	public ArrayList<Task> getViewList() {
		return listsManager.getViewList();
	}

	public ArrayList<Task> getMainList() {
		return listsManager.getMainList();
	}

	public ArrayList<Tag> getTagsList() {
		return listsManager.getTagsList();
	}

	public void setSelectedTag(String tagName, boolean isSelected) {
		System.out.println("running setselected");

		if (isSelected) {
			listsManager.updateSelectedTagsList(tagName, true);
		} else {
			listsManager.updateSelectedTagsList(tagName, false);
			for (int i = 0; i < listsManager.getTagsList().size(); i++) {
				Tag tag = listsManager.getTagsList().get(i);
				if (tag.getName().equals(tagName)) {
					tag.setIsSelected(false);
				}
			}
		}
		listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
		listsManager.updateLists();
	}

	public String getCurrentViewType() {
		String currentViewType = getSelectedCategory() + " " + getSelectedView() + " " + listsManager.getCurrentViewType();
		return currentViewType;
	}

	public String getPreviousUserInput() {
		if (historyManager.getPreviousUserInputList().size() > 0) {
			int previousUserInputListLastIndex = historyManager.getPreviousUserInputList().size() - 1;
			String previousUserInput = historyManager.getPreviousUserInputList().remove(previousUserInputListLastIndex);
			historyManager.getNextUserInputList().add(previousUserInput);
			return previousUserInput;
		}
			return "";
	}

	public String getNextUserInput() {
		if (historyManager.getNextUserInputList().size() > 0) {
			int nextUserInputListLastIndex = historyManager.getNextUserInputList().size() - 1;
			String nextUserInput = historyManager.getNextUserInputList().remove(nextUserInputListLastIndex);
			historyManager.getNextUserInputList().add(nextUserInput);
			return nextUserInput;
		}
		return "";
	}

	public boolean testLogicFramework(Command commandObj) {
		boolean isSuccessful;
		if (commandObj instanceof InvalidCommand) {
			return false;
		}
		isSuccessful = runCommand(commandObj);
		return isSuccessful;
	}
}