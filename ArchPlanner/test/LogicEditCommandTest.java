package test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import logic.Logic;
import logic.Task;
import logic.TaskParameters;
import logic.commands.AddCommand;
import logic.commands.CommandInterface;
import logic.commands.EditCommand;
import logic.commands.EditCommand.REMOVE_TYPE;

/**
 * This class is used to conduct unit testing on logic component for edit command.
 * 
 * @@author A0140021J
 *
 */
public class LogicEditCommandTest {

	private Logic logic;

	private CommandInterface editCommandTest;


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
	public void testEditCommandTaskDescription() {

		addEventTaskWithStartDateTimeEndDateTimeAndTag();

		TaskParameters taskParameters = new TaskParameters();
		taskParameters.setDescription("work on proposal");
		editCommandTest = new EditCommand(1, taskParameters);

		logic.logicTestEnvironment(editCommandTest);

		assertEquals("work on proposal", logic.getViewList().get(0).getDescription());
	}

	@Test
	public void testEditCommandTaskStartDateTime() {

		LocalDate startDate = LocalDate.of(2016, 1, 10);
		LocalTime startTime = LocalTime.of(9, 00);
		addEventTaskWithStartDateTimeEndDateTimeAndTag();

		TaskParameters taskParameters = new TaskParameters();
		taskParameters.setStartDate(startDate);
		taskParameters.setStartTime(startTime);
		editCommandTest = new EditCommand(1, taskParameters);

		logic.logicTestEnvironment(editCommandTest);

		Task task = logic.getViewList().get(0);

		assertEquals("transfer task to harddisk", task.getDescription());
		assertEquals("10 Jan 2016", task.getStartDateString());
		assertEquals("9:00AM", task.getStartTimeString());
		assertEquals("02 Feb 2016", task.getEndDateString());
		assertEquals("11:00AM", task.getEndTimeString());
		assertEquals(false , task.getIsDone());
		assertEquals("#project", task.getTagsList().get(0));
	}

	@Test
	public void testEditCommandTaskEndDateTime() {

		LocalDate endDate = LocalDate.of(2016, 1, 10);
		LocalTime endTime = LocalTime.of(9, 00);
		addEventTaskWithStartDateTimeEndDateTimeAndTag();

		TaskParameters taskParameters = new TaskParameters();
		taskParameters.setEndDate(endDate);
		taskParameters.setEndTime(endTime);
		editCommandTest = new EditCommand(1, taskParameters);

		logic.logicTestEnvironment(editCommandTest);

		Task task = logic.getViewList().get(0);

		assertEquals("transfer task to harddisk", task.getDescription());
		assertEquals("01 Jan 2016", task.getStartDateString());
		assertEquals("10:00AM", task.getStartTimeString());
		assertEquals("10 Jan 2016", task.getEndDateString());
		assertEquals("9:00AM", task.getEndTimeString());
		assertEquals(false , task.getIsDone());
		assertEquals("#project", task.getTagsList().get(0));
	}
	
	@Test
	public void testEditCommandTaskTags() {

		addEventTaskWithStartDateTimeEndDateTimeAndTag();
		
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("#work");
		
		TaskParameters taskParameters = new TaskParameters();
		taskParameters.setTagsList(tags);
		editCommandTest = new EditCommand(1, taskParameters);

		logic.logicTestEnvironment(editCommandTest);

		Task task = logic.getViewList().get(0);

		assertEquals("transfer task to harddisk", task.getDescription());
		assertEquals("01 Jan 2016", task.getStartDateString());
		assertEquals("10:00AM", task.getStartTimeString());
		assertEquals("02 Feb 2016", task.getEndDateString());
		assertEquals("11:00AM", task.getEndTimeString());
		assertEquals(false , task.getIsDone());
		assertEquals("#work", task.getTagsList().get(0));
	}
	
	@Test
	public void testEditCommandRemoveStartDateTime() {

		addEventTaskWithStartDateTimeEndDateTimeAndTag();
		
		TaskParameters taskParameters = new TaskParameters();
		editCommandTest = new EditCommand(1, taskParameters, REMOVE_TYPE.START);

		logic.logicTestEnvironment(editCommandTest);

		Task task = logic.getViewList().get(0);

		assertEquals("transfer task to harddisk", task.getDescription());
		assertEquals("", task.getStartDateString());
		assertEquals("", task.getStartTimeString());
		assertEquals("02 Feb 2016", task.getEndDateString());
		assertEquals("11:00AM", task.getEndTimeString());
		assertEquals(false , task.getIsDone());
		assertEquals("#project", task.getTagsList().get(0));
	}

	@Test
	public void testEditCommandRemoveEndDateTime() {

		addEventTaskWithStartDateTimeEndDateTimeAndTag();
		
		TaskParameters taskParameters = new TaskParameters();
		editCommandTest = new EditCommand(1, taskParameters, REMOVE_TYPE.END);

		logic.logicTestEnvironment(editCommandTest);

		Task task = logic.getViewList().get(0);

		assertEquals("transfer task to harddisk", task.getDescription());
		assertEquals("01 Jan 2016", task.getStartDateString());
		assertEquals("10:00AM", task.getStartTimeString());
		assertEquals("", task.getEndDateString());
		assertEquals("", task.getEndTimeString());
		assertEquals(false , task.getIsDone());
		assertEquals("#project", task.getTagsList().get(0));
	}
	
	@Test
	public void testEditCommandRemoveTag() {

		addEventTaskWithStartDateTimeEndDateTimeAndTag();
		
		TaskParameters taskParameters = new TaskParameters();
		editCommandTest = new EditCommand(1, taskParameters, REMOVE_TYPE.TAG);

		logic.logicTestEnvironment(editCommandTest);

		Task task = logic.getViewList().get(0);

		assertEquals("transfer task to harddisk", task.getDescription());
		assertEquals("01 Jan 2016", task.getStartDateString());
		assertEquals("10:00AM", task.getStartTimeString());
		assertEquals("02 Feb 2016", task.getEndDateString());
		assertEquals("11:00AM", task.getEndTimeString());
		assertEquals(false , task.getIsDone());
		assertEquals(0, task.getTagsList().size());
	}

	@Test
	public void testEditCommandInvalidStartDate() {
		LocalDate startDate = LocalDate.of(2016, 3, 1);
		addEventTaskWithStartDateTimeEndDateTimeAndTag();
		
		TaskParameters taskParameters = new TaskParameters();
		taskParameters.setStartDate(startDate);
		editCommandTest = new EditCommand(1, taskParameters, REMOVE_TYPE.END);

		CommandInterface commandExecuted = logic.logicTestEnvironment(editCommandTest);
		String invalidMessage = commandExecuted.getMessage();

		assertEquals("Invalid start date", invalidMessage);
	}

	public void addEventTaskWithStartDateTimeEndDateTimeAndTag() {
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("#project");
		LocalDate startDate = LocalDate.of(2016, 1, 1);
		LocalTime startTime = LocalTime.of(10, 00);
		LocalDate endDate = LocalDate.of(2016, 2, 2);
		LocalTime endTime = LocalTime.of(11, 00);
		TaskParameters task = new TaskParameters("transfer task to harddisk", tags, startDate, startTime, endDate, endTime);
		AddCommand addCommandTest = new AddCommand(task);

		logic.logicTestEnvironment(addCommandTest);
	}
}
