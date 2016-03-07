package logic.commands;

import java.util.ArrayList;

import logic.Task;

public class ExitCommand implements Command {

	@Override
	public boolean execute(ArrayList<Task> mainList, ArrayList<Task> viewList, ArrayList<String> tagsList) {
		return true;
	}
}
