package test.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import logic.Logic;
import logic.Task;
import logic.TaskParameters;
import logic.commands.AddCommand;
import logic.commands.CommandInterface;
import logic.commands.DoneCommand;


/**
 * This class is used to conduct unit testing on logic component for done command.
 * 
 * @@author A0140021J
 *
 */
public class LogicDoneCommandTest {

	private Logic logic;

	private CommandInterface doneCommandTest;


	@Before
	public void setUp() throws Exception {
		logic = new Logic();
		tearDown();
	}

	@After
	public void tearDown() throws Exception {
		logic.getMainList().clear();
		logic.getViewList().clear();
	}

	@Test
	public void testDoneOneTask() {

		addOneTask();
		
		assertEquals(1, logic.getViewList().size());
		Task oldTask1 = logic.getViewList().get(0);
		assertEquals(false, oldTask1.getIsDone());

		doneCommandTest = new DoneCommand(1);
		logic.logicTestEnvironment(doneCommandTest);

		Task newTask1 = logic.getViewList().get(0);
		assertEquals(true, newTask1.getIsDone());
	}

	@Test
	public void testDoneMultipleTask() {

		addOneTask();
		addOneTask();
		addOneTask();
		
		assertEquals(3, logic.getViewList().size());
		Task oldTask1 = logic.getViewList().get(0);
		Task oldTask2 = logic.getViewList().get(1);
		Task oldTask3 = logic.getViewList().get(2);
		assertEquals(false, oldTask1.getIsDone());
		assertEquals(false, oldTask2.getIsDone());
		assertEquals(false, oldTask3.getIsDone());

		doneCommandTest = new DoneCommand(1, 3);
		logic.logicTestEnvironment(doneCommandTest);

		Task newTask1 = logic.getViewList().get(0);
		Task newTask2 = logic.getViewList().get(1);
		Task newTask3 = logic.getViewList().get(2);
		assertEquals(true, newTask1.getIsDone());
		assertEquals(true, newTask2.getIsDone());
		assertEquals(true, newTask3.getIsDone());
	}

	public void addOneTask() {

		TaskParameters task = new TaskParameters("transfer task to harddisk", new ArrayList<String>(), null, null, null, null);
		AddCommand addCommand = new AddCommand(task);

		logic.logicTestEnvironment(addCommand);
	}

}
