package logic.commands;

import java.util.ArrayList;

import logic.Task;

public class ExitCommand implements Command {

	@Override
	public boolean execute(ArrayList<Task> mainList, ArrayList<Task> viewList, ArrayList<String> tagsList) {
		exit();
		return true;
	}

	private void exit() {
		System.exit(0);
	}
}
