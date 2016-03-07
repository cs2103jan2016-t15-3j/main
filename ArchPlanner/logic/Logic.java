package logic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import logic.commands.AddCommand;
import logic.commands.Command;
import logic.commands.DeleteCommand;
import logic.commands.DoneCommand;
import logic.commands.EditCommand;
import logic.commands.ExitCommand;
import logic.commands.SearchCommand;
import logic.commands.UndoneCommand;

import java.util.Calendar;;

//import application.Ui;

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
	private Stack<Command> undoStack = new Stack<Command>();
	private Stack<Command> redoStack = new Stack<Command>();

	Storage storage = new Storage();

	public static void main(String args[]) {
		Logic logic = new Logic();

		Calendar startDateTime1 = Calendar.getInstance();
		Calendar endDateTime1 = Calendar.getInstance();
		Calendar startDateTime2 = Calendar.getInstance();
		Calendar endDateTime2 = Calendar.getInstance();
		Calendar startDateTime3 = Calendar.getInstance();
		Calendar endDateTime3 = Calendar.getInstance();

		startDateTime1 = configureDateTime(2001, 1, 1, 1, 1);
		endDateTime1 = configureDateTime(2002, 2, 2, 2, 2);
		startDateTime2 = configureDateTime(2003, 3, 3, 3, 3);
		endDateTime2 = configureDateTime(2004, 4, 4, 4, 4);
		startDateTime3 = configureDateTime(2005, 5, 5, 5, 5);
		endDateTime3 = configureDateTime(2006, 6, 6, 6, 6);

		AddCommand addCommand1 = new AddCommand("task 1", "tag 1", startDateTime1, endDateTime1);
		AddCommand addCommand2 = new AddCommand("task 2", "tag 2", startDateTime2, endDateTime2);
		AddCommand addCommand3 = new AddCommand("task 3", "tag 3", startDateTime3, endDateTime3);

		Command commandObj1 = (Command) addCommand1;
		Command commandObj2 = (Command) addCommand2;
		Command commandObj3 = (Command) addCommand3;

		logic.executeCommand(commandObj1);
		logic.executeCommand(commandObj2);
		logic.executeCommand(commandObj3);

		Storage storage = new Storage();
		storage.loadStorageFile();

		assertEquals("task 1", logic.getViewList().get(0).getDescription().toString());
		assertEquals("task 2",logic.getViewList().get(1).getDescription().toString());
		assertEquals("task 3", logic.getViewList().get(2).getDescription().toString());
		assertEquals("tag 1", logic.getViewList().get(0).getTag().toString());
		assertEquals("tag 2", logic.getViewList().get(1).getTag().toString());
		assertEquals("tag 3", logic.getViewList().get(2).getTag().toString());
		assertEquals("01/01/2001", logic.getViewList().get(0).getStartDate());
		assertEquals("01:01", logic.getViewList().get(0).getStartTime());
		assertEquals("02/02/2002", logic.getViewList().get(0).getEndDate());
		assertEquals("02:02", logic.getViewList().get(0).getEndTime());
		assertEquals("03/03/2003", logic.getViewList().get(1).getStartDate());
		assertEquals("03:03", logic.getViewList().get(1).getStartTime());
		assertEquals("04/04/2004", logic.getViewList().get(1).getEndDate());
		assertEquals("04:04", logic.getViewList().get(1).getEndTime());
		assertEquals("05/05/2005", logic.getViewList().get(2).getStartDate());
		assertEquals("05:05", logic.getViewList().get(2).getStartTime());
		assertEquals("06/06/2006", logic.getViewList().get(2).getEndDate());
		assertEquals("06:06", logic.getViewList().get(2).getEndTime());
		assertEquals("tag 1", logic.getViewList().get(0).getTag());
		assertEquals("tag 2",logic.getViewList().get(1).getTag());
		assertEquals("tag 3", logic.getViewList().get(2).getTag());

		DeleteCommand command4 = new DeleteCommand(2);
		Command commandObj4 = (Command) command4;
		logic.executeCommand(commandObj4);

		assertEquals("task 1",logic.getViewList().get(0).getDescription());
		assertEquals("task 3", logic.getViewList().get(1).getDescription());

		assertEquals("tag 1", logic.getTagsList().get(0));
		assertEquals("tag 3", logic.getTagsList().get(1));

		EditCommand command5 = new EditCommand(2, "task two", "tag two", startDateTime2, endDateTime2);
		Command commandObj5 = (Command) command5;
		logic.executeEditCommand(commandObj5);

		assertEquals("task two", logic.getViewList().get(1).getDescription());
		assertEquals("tag two", logic.getViewList().get(1).getTag());
		assertEquals("tag 1", logic.getTagsList().get(0));
		assertEquals("tag two", logic.getTagsList().get(1));

		DoneCommand command6 = new DoneCommand(1);
		Command commandObj6 = (Command) command6;
		logic.executeDoneCommand(commandObj6);

		assertEquals(true, logic.getViewList().get(0).getIsDone());
		
		UndoneCommand command7 = new UndoneCommand(1);
		Command commandObj7 = (Command) command7;
		logic.executeUndoneCommand(commandObj7);

		assertEquals(false, logic.getViewList().get(0).getIsDone());

		SearchCommand command8 = new SearchCommand("two");
		Command commandObj8 = (Command) command8;
		logic.executeSearchCommand(commandObj8);

		assertEquals("task two",logic.getViewList().get(0).getDescription());
	}

	public static Calendar configureDateTime(int year, int month, int day, int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute);
		return cal;
	}

	private void loadFile() {
		storage.loadStorageFile();
		_mainList = storage.getMasterList();
		setViewList(_mainList);
		updateTagsList();
	}

	private ArrayList<Task> updateViewFloatingTasksList() {
		ArrayList<Task> floatingTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartDateTime(_mainList, i) == null) && (getTaskEndDateTime(_mainList, i) == null)) {
				floatingTasksList.add(_mainList.get(i));
			}
		}
		return floatingTasksList;
	}

	private ArrayList<Task> updateViewDeadlineTasksList() {
		ArrayList<Task> deadlineTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartDateTime(_mainList, i) == null) && (getTaskEndDateTime(_mainList, i) != null)) {
				deadlineTasksList.add(_mainList.get(i));
			}
		}
		deadlineTasksList = getSortedListWithEndDateTime(deadlineTasksList);
		deadlineTasksList = getSortedListWithDescription(deadlineTasksList);
		return deadlineTasksList;
	}

	private ArrayList<Task> updateViewTagTasksList(String tag) {
		ArrayList<Task> tagTasksList = new ArrayList<Task>();

		for (int i = 0; i < _mainList.size(); i++) {
			if (getTaskTag(_mainList, i).equals(tag)) {
				tagTasksList.add(_mainList.get(i));
			}
		}
		return tagTasksList;
	}

	private ArrayList<Task> updateViewTimeLineTasksList() {
		ArrayList<Task> timeLineTasksList= new ArrayList<>();

		for (int i = 0; i < _mainList.size(); i++) {
			if ((getTaskStartDateTime(_mainList, i) != null) && (getTaskEndDateTime(_mainList, i) != null)) {
				timeLineTasksList.add(_mainList.get(i));
			}
		}
		timeLineTasksList = getSortedListWithStartDateTime(timeLineTasksList);
		timeLineTasksList = getSortedListWithEndDateTime(timeLineTasksList);
		timeLineTasksList = getSortedListWithDescription(timeLineTasksList);
		return timeLineTasksList;
	}

	private void updateTagsList() {
		ArrayList<String> tagsList = new ArrayList<String>();
		tagsList.removeAll(tagsList);
		for (int i = 0; i < _mainList.size(); i++) {
			String tag = getTaskTag(_mainList, i);
			if (!tagsList.contains(tag)) {
				tagsList.add(tag);
			}
		}
	}

	public void exitProgram() {
		System.exit(0);
	}

	public boolean executeCommand(Command commandObj) {

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
		} else if (strCommandType.equals("SearchCommand")) {
			return COMMAND_TYPE.SEARCH;
		} else if (strCommandType.equals("SortCommand")) {
			return COMMAND_TYPE.SORT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}


	private boolean executeAddCommand(Command commandObj) {
		AddCommand command = (AddCommand) commandObj;
		boolean isSuccessful = command.execute(_mainList, _viewList, _tagsList);
		save();
		_viewList = getSortedList(_viewList);
		return isSuccessful;
	}

	private boolean executeDeleteCommand(Command commandObj) {
		DeleteCommand command = (DeleteCommand) commandObj;
		boolean isSuccessful = command.execute(_mainList, _viewList, _tagsList);
		save();
		return isSuccessful;
	}

	private boolean executeEditCommand(Command commandObj) {
		EditCommand command = (EditCommand) commandObj;
		save();
		boolean isSuccessful = command.execute(_mainList, _viewList, _tagsList);
		_viewList = getSortedList(_viewList);
		return isSuccessful;
	}

	private boolean executeExitCommand(Command commandObj) {
		exit();
		return false;
	}

	private boolean executeDoneCommand(Command commandObj) {
		DoneCommand command = (DoneCommand) commandObj;
		boolean isSuccessful = command.execute(_mainList, _viewList, _tagsList);
		save();
		return isSuccessful;
	}

	private boolean executeUndoneCommand(Command commandObj) {
		UndoneCommand command = (UndoneCommand) commandObj;
		boolean isSuccessful = command.execute(_mainList, _viewList, _tagsList);
		save();
		return isSuccessful;
	}

	private boolean executeSearchCommand(Command commandObj) {
		SearchCommand command = (SearchCommand) commandObj;
		boolean isSuccessful = command.execute(_mainList, _viewList, _tagsList);
		save();
		return isSuccessful;
	}

	private boolean executeInvalidCommand(Command commandObj) {
		return false;
	}

	public void editTask(int id, String newDescription, String newTag, Calendar newStartDateTime, Calendar newEndDateTime) {
		int taskIndex = getTaskIndex(id);
		Task oldTask = getTask(_viewList, taskIndex);

		if (newDescription.equals(getTaskDescription(_viewList, taskIndex))) {
			setTaskDescription(_viewList, taskIndex, newDescription);
		}

		if (newStartDateTime.equals(getTaskStartDateTime(_viewList, taskIndex))) {
			setTaskStartDateTime(_viewList, taskIndex, newStartDateTime);
		}

		if (newEndDateTime.equals(getTaskEndDateTime(_viewList, taskIndex))) {
			setTaskEndDateTime(_viewList, taskIndex, newEndDateTime);
		}

		if (newTag.equals(getTaskTag(_viewList, taskIndex))) {
			setTaskTag(_viewList, taskIndex, newTag);
		}
		_mainList.remove(oldTask);
		_mainList.add(getTask(_viewList, taskIndex));
		//saveTask();
	}

	public void done(int id) {
		int taskIndex = getTaskIndex(id);
		Task task = getTask(_viewList, taskIndex);

		setTaskIsDone(_viewList, taskIndex, true);
		_mainList.remove(task);
		_mainList.add(getTask(_viewList, taskIndex));
		//saveTask();
	}

	public void undone(int id) throws IOException {
		int taskIndex = getTaskIndex(id);
		Task task = getTask(_viewList, taskIndex);

		setTaskIsDone(_viewList, taskIndex, false);
		_mainList.remove(task);
		_mainList.add(getTask(_viewList, taskIndex));
		//saveTask();
	}

	public ArrayList<Task> view() {
		return _mainList;
	}

	public void undo(int number) {
	}

	public void redo(int number) {
	}


	public ArrayList<Task> getViewList() {
		return _viewList;
	}

	public ArrayList<Task> getMainList() {
		return _mainList;
	}

	public ArrayList<String> getTagsList() {
		return _tagsList;
	}

	private void setTaskDescription(ArrayList<Task> list, int taskIndex, String description) {
		list.get(taskIndex).setDescription(description);
	}

	private void setTaskStartDateTime(ArrayList<Task> list, int taskIndex, Calendar startDateTime) {
		list.get(taskIndex).setStartDateTime(startDateTime);
	}

	private void setTaskEndDateTime(ArrayList<Task> list, int taskIndex, Calendar endDateTime) {
		list.get(taskIndex).setEndDateTime(endDateTime);
	}

	private void setTaskTag(ArrayList<Task> list, int taskIndex, String tag) {
		list.get(taskIndex).setTag(tag);
	}

	private void setTaskIsDone(ArrayList<Task> list, int taskIndex, boolean isDone) { //throws IOException {
		list.get(taskIndex).setIsDone(isDone);
		//saveTask();
	}

	private void setViewList(ArrayList<Task> list) {
		_viewList = _mainList;
	}

	private int getTaskIndex(int id) {
		int taskIndex = id - 1;
		return taskIndex;
	}

	private Task getTask(ArrayList<Task> list, int taskIndex) {
		Task task = list.get(taskIndex);
		return task;
	}

	private String getTaskDescription(ArrayList<Task> list, int taskIndex) {
		String taskDescription = list.get(taskIndex).getDescription();
		return taskDescription;
	}

	private Calendar getTaskStartDateTime(ArrayList<Task> list, int taskIndex) {
		Calendar taskStartDateTime = list.get(taskIndex).getStartDateTime();
		return taskStartDateTime;
	}

	private Calendar getTaskEndDateTime(ArrayList<Task> list, int taskIndex) {
		Calendar taskEndTime = list.get(taskIndex).getEndDateTime();
		return taskEndTime;
	}

	private String getTaskTag(ArrayList<Task> list, int taskIndex) {
		String taskTag = list.get(taskIndex).getTag();
		return taskTag;
	}

	private boolean getTaskIsDone(ArrayList<Task> list, int taskIndex) {
		boolean taskIsDone = list.get(taskIndex).getIsDone();
		return taskIsDone;
	}
	
	private ArrayList<Task> getSortedList(ArrayList<Task> list) {
		list = getSortedListWithStartDateTime(_viewList);
		list = getSortedListWithEndDateTime(_viewList);
		list = getSortedListWithTag(_viewList);
		list = getSortedListWithDescription(_viewList);
		return list;
	}

	private ArrayList<Task> getSortedListWithDescription(ArrayList<Task> list) {
		DescriptionComparator descriptionComp = new DescriptionComparator();
		Collections.sort(list, descriptionComp);
		return list;
	}
	
	private ArrayList<Task> getSortedListWithTag(ArrayList<Task> list) {
		TagComparator tagComp = new TagComparator();
		Collections.sort(list, tagComp);
		return list;
	}

	private ArrayList<Task> getSortedListWithStartDateTime(ArrayList<Task> list) {
		StartDateTimeComparator startDateTimeComp = new StartDateTimeComparator();
		Collections.sort(list, startDateTimeComp);
		return list;
	}

	private ArrayList<Task> getSortedListWithEndDateTime(ArrayList<Task> list) {
		EndDateTimeComparator endTimeComp = new EndDateTimeComparator();
		Collections.sort(list, endTimeComp);
		return list;
	}

	private void save() {
		storage.writeStorageFile(_mainList);
	}

	private void exit() {
		System.exit(0);
	}
}