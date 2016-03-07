package logic.commands;

import java.util.ArrayList;

import logic.Task;

public interface Command {
	public boolean execute(ArrayList<Task> mainList, ArrayList<Task> viewList, ArrayList<String> tagsList);
}
