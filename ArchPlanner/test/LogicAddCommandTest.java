package test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import logic.Logic;
import logic.TaskParameters;
import logic.commands.AddCommand;
import logic.commands.CommandInterface;

/**
 * This class is used to conduct unit testing on logic component for add command.
 * 
 * @@author A0140021J
 *
 */
public class LogicAddCommandTest {

    private Logic logic;

    private CommandInterface addCommandTest;


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
    public void testAddTask() {
    	
    	TaskParameters task = new TaskParameters("transfer task to harddisk", new ArrayList<String>(), null, null, null, null);
        addCommandTest = new AddCommand(task);
        
        logic.logicTestEnvironment(addCommandTest);

        task = logic.getViewList().get(0);
        assertEquals("transfer task to harddisk", task.getDescription());
        assertEquals("", task.getStartDateString());
        assertEquals("", task.getStartTimeString());
        assertEquals("", task.getEndDateString());
        assertEquals("", task.getEndTimeString());
        assertEquals(false , task.getIsDone());
        assertEquals(true, task.getTagsList().isEmpty());
    }

    @Test
    public void testAddDeadlineWithDate() {
    	LocalDate endDate = LocalDate.of(2016, 1, 1);
    	TaskParameters task = new TaskParameters("transfer task to harddisk", new ArrayList<String>(), null, null, endDate, null);
        addCommandTest = new AddCommand(task);
        
        logic.logicTestEnvironment(addCommandTest);

        task = logic.getViewList().get(0);
        assertEquals("transfer task to harddisk", task.getDescription());
        assertEquals("", task.getStartDateString());
        assertEquals("", task.getStartTimeString());
        assertEquals("01 Jan 2016", task.getEndDateString());
        assertEquals("", task.getEndTimeString());
        assertEquals(false , task.getIsDone());
        assertEquals(true, task.getTagsList().isEmpty());
    }

    @Test
    public void testAddDeadlineWithDateTime() {
    	LocalDate endDate = LocalDate.of(2016, 1, 1);
    	LocalTime endTime = LocalTime.of(10, 00);
    	TaskParameters task = new TaskParameters("transfer task to harddisk", new ArrayList<String>(), null, null, endDate, endTime);
        addCommandTest = new AddCommand(task);
        
        logic.logicTestEnvironment(addCommandTest);

        task = logic.getViewList().get(0);
        assertEquals("transfer task to harddisk", task.getDescription());
        assertEquals("", task.getStartDateString());
        assertEquals("", task.getStartTimeString());
        assertEquals("01 Jan 2016", task.getEndDateString());
        assertEquals("10:00AM", task.getEndTimeString());
        assertEquals(false , task.getIsDone());
        assertEquals(true, task.getTagsList().isEmpty());
    }

    @Test
    public void testAddEventWithStartDate() {
    	LocalDate startDate = LocalDate.of(2016, 1, 1);
    	TaskParameters task = new TaskParameters("transfer task to harddisk", new ArrayList<String>(), startDate, null, null, null);
        addCommandTest = new AddCommand(task);
        
        logic.logicTestEnvironment(addCommandTest);

        task = logic.getViewList().get(0);
        assertEquals("transfer task to harddisk", task.getDescription());
        assertEquals("01 Jan 2016", task.getStartDateString());
        assertEquals("", task.getStartTimeString());
        assertEquals("", task.getEndDateString());
        assertEquals("", task.getEndTimeString());
        assertEquals(false , task.getIsDone());
        assertEquals(true, task.getTagsList().isEmpty());
    }

    @Test
    public void testAddEventWithStartDateTime() {
    	LocalDate startDate = LocalDate.of(2016, 1, 1);
    	LocalTime startTime = LocalTime.of(10, 00);
    	TaskParameters task = new TaskParameters("transfer task to harddisk", new ArrayList<String>(), startDate, startTime, null, null);
        addCommandTest = new AddCommand(task);
        
        logic.logicTestEnvironment(addCommandTest);

        task = logic.getViewList().get(0);
        assertEquals("transfer task to harddisk", task.getDescription());
        assertEquals("01 Jan 2016", task.getStartDateString());
        assertEquals("10:00AM", task.getStartTimeString());
        assertEquals("", task.getEndDateString());
        assertEquals("", task.getEndTimeString());
        assertEquals(false , task.getIsDone());
        assertEquals(true, task.getTagsList().isEmpty());
    }

    @Test
    public void testAddEventWithStartDateEndDate() {
    	LocalDate startDate = LocalDate.of(2016, 1, 1);
    	LocalDate endDate = LocalDate.of(2016, 2, 2);
    	TaskParameters task = new TaskParameters("transfer task to harddisk", new ArrayList<String>(), startDate, null, endDate, null);
        addCommandTest = new AddCommand(task);
        
        logic.logicTestEnvironment(addCommandTest);

        task = logic.getViewList().get(0);
        assertEquals("transfer task to harddisk", task.getDescription());
        assertEquals("01 Jan 2016", task.getStartDateString());
        assertEquals("", task.getStartTimeString());
        assertEquals("02 Feb 2016", task.getEndDateString());
        assertEquals("", task.getEndTimeString());
        assertEquals(false , task.getIsDone());
        assertEquals(true, task.getTagsList().isEmpty());
    }

    @Test
    public void testAddEventWithStartDateTimeEndDateTime() {
    	LocalDate startDate = LocalDate.of(2016, 1, 1);
    	LocalTime startTime = LocalTime.of(10, 00);
    	LocalDate endDate = LocalDate.of(2016, 2, 2);
    	LocalTime endTime = LocalTime.of(11, 00);
    	TaskParameters task = new TaskParameters("transfer task to harddisk", new ArrayList<String>(), startDate, startTime, endDate, endTime);
        addCommandTest = new AddCommand(task);
        
        logic.logicTestEnvironment(addCommandTest);

        task = logic.getViewList().get(0);
        assertEquals("transfer task to harddisk", task.getDescription());
        assertEquals("01 Jan 2016", task.getStartDateString());
        assertEquals("10:00AM", task.getStartTimeString());
        assertEquals("02 Feb 2016", task.getEndDateString());
        assertEquals("11:00AM", task.getEndTimeString());
        assertEquals(false , task.getIsDone());
        assertEquals(true, task.getTagsList().isEmpty());
    }

    @Test
    public void testAddTaskWithTag() {
    	ArrayList<String> tags = new ArrayList<>();
    	tags.add("#project");
    	TaskParameters task = new TaskParameters("transfer task to harddisk", tags, null, null, null, null);
        addCommandTest = new AddCommand(task);
        
        logic.logicTestEnvironment(addCommandTest);

        task = logic.getViewList().get(0);
        assertEquals("transfer task to harddisk", task.getDescription());
        assertEquals("", task.getStartDateString());
        assertEquals("", task.getStartTimeString());
        assertEquals("", task.getEndDateString());
        assertEquals("", task.getEndTimeString());
        assertEquals(false , task.getIsDone());
        assertEquals(false, task.getTagsList().isEmpty());
        assertEquals("#project", task.getTagsList().get(0));
    }
}