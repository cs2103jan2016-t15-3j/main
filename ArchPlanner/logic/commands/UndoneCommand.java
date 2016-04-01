package logic.commands;

import java.util.ArrayList;

import logic.HistoryManager;
import logic.ListsManager;
import logic.RollbackItem;
import logic.Task;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import logic.commands.ViewCommand.VIEW_TYPE;

public class UndoneCommand implements Command {

	private int _firstIndex;
	private int _lastIndex;


	public UndoneCommand(int index) {
		_firstIndex = index - 1;
		_lastIndex = index - 1;
	}

	public UndoneCommand(int firstIndex, int lastIndex) {
		_firstIndex = firstIndex - 1;
		_lastIndex = lastIndex - 1;
	}

	public int getfirstIndex() {
		return _firstIndex;
	}

	public int getLastIndex() {
		return _lastIndex;
	}

	public Command execute() {
		return null;
	}

	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		assert((_firstIndex >= 0) && (_firstIndex < listsManager.getViewList().size()));
		assert((_lastIndex >= 0) && (_lastIndex < listsManager.getViewList().size()));
		/*
		if (!isWithinList(listsManager.getViewList(), _index)) {
			return false;
		}
		 */
		int numOfUndone = _lastIndex - _firstIndex + 1;
		for (int i = 0; i < numOfUndone; i++) {
			Task oldTask = listsManager.getViewList().get(_firstIndex);
			listsManager.getMainList().remove(oldTask);
			Task newTask = new Task();
			newTask.setDescription(oldTask.getDescription());
			newTask.setTagsList(oldTask.getTagsList());
			newTask.setStartDate(oldTask.getStartDate());
			newTask.setStartTime(oldTask.getStartTime());
			newTask.setEndDate(oldTask.getEndDate());
			newTask.setEndTime(oldTask.getEndTime());
			newTask.setIsOverdue(oldTask.getIsOverdue());
			newTask.setIsDone(false);
			listsManager.getMainList().add(newTask);
			listsManager.getSelectedTagsList().clear();
			listsManager.setViewType(VIEW_TYPE.VIEW_ALL);
			listsManager.setCategoryType(CATEGORY_TYPE.CATEGORY_ALL);
			listsManager.updateLists();

			RollbackItem rollbackItem = new RollbackItem("done", oldTask, newTask);

			historyManager.getUndoList().add(rollbackItem);
			historyManager.setRedoList(new ArrayList<RollbackItem>());
		}
		System.out.println("undolist size: " + historyManager.getUndoList().size());
		return null;
	}
	/*
	private boolean isWithinList(ArrayList<Task> list, int index) {
		boolean isWithinList = false;
		isWithinList = ((index < list.size()) && (index >= 0));
		return isWithinList;
	}
	 */
}
