//package test;
//
//import static org.junit.Assert.*;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import feedback.Feedback;
//import logic.Logic;
//import logic.Task;
//import logic.commands.AddCommand;
//import logic.commands.Command;
//import logic.commands.InvalidCommand;
//import parser.Parser;
//import java.util.ArrayList;
//
//public class AddActionTest {
//
//    private Feedback feedbackTest;
//    private Logic logicTest;
//    private Parser parserTest;
//
//    private String userInputTest;
//    private ArrayList<String> promptTest;
//    private Command commandTest;
//    private Task taskTest;
//
//
//    @Before
//    public void setUp() throws Exception {
//        feedbackTest = new Feedback();
//        parserTest = new Parser();
//        logicTest = new Logic();
//
//        promptTest = new ArrayList<String>();
//        taskTest = new Task();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        logicTest.getMainList().clear();
//        logicTest.getViewList().clear();
//    }
//
//    @Test
//    public void testAddTask() {
//        userInputTest = "add transfer task to harddisk";
//
//        promptTest = feedbackTest.getPrompts(userInputTest);
//        assertEquals(1, promptTest.size());
//        assertEquals("add <Description>", promptTest.get(0));
//
//        commandTest = parserTest.parseCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//
//        commandTest = logicTest.executeCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//        taskTest = logicTest.getViewList().get(0);
//        assertEquals("transfer task to harddisk", taskTest.getDescription());
//        assertEquals("", taskTest.getStartDateString());
//        assertEquals("", taskTest.getStartTimeString());
//        assertEquals("", taskTest.getEndDateString());
//        assertEquals("", taskTest.getEndTimeString());
//        assertEquals(false , taskTest.getIsDone());
//        assertEquals(true, taskTest.getTagsList().isEmpty());
//    }
//
//    @Test
//    public void testAddDeadlineWithDate() {
//        userInputTest = "add transfer task to harddisk by 5/10/17";
//
//        promptTest = feedbackTest.getPrompts(userInputTest);
//        assertEquals(3, promptTest.size());
//        assertEquals("add <Description> by <Start Date>", promptTest.get(0));
//        assertEquals("add <Description> by <Start Date> #<Tag>", promptTest.get(1));
//        assertEquals("add <Description> by <Start Date> <Start Time>", promptTest.get(2));
//
//        commandTest = parserTest.parseCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//
//        commandTest = logicTest.executeCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//        taskTest = logicTest.getViewList().get(0);
//        assertEquals("transfer task to harddisk", taskTest.getDescription());
//        assertEquals("", taskTest.getStartDateString());
//        assertEquals("", taskTest.getStartTimeString());
//        assertEquals("10 May 2017", taskTest.getEndDateString());
//        assertEquals("", taskTest.getEndTimeString());
//        assertEquals(false , taskTest.getIsDone());
//        assertEquals(true, taskTest.getTagsList().isEmpty());
//    }
//
//    @Test
//    public void testAddDeadlineWithDateTime() {
//        userInputTest = "add transfer task to harddisk by 5/10/17 10am";
//
//        promptTest = feedbackTest.getPrompts(userInputTest);
//        assertEquals(2, promptTest.size());
//        assertEquals("add <Description> by <Start Date> <Start Time>", promptTest.get(0));
//        assertEquals("add <Description> by <Start Date> <Start Time> #<Tag>", promptTest.get(1));
//
//        commandTest = parserTest.parseCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//
//        commandTest = logicTest.executeCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//        taskTest = logicTest.getViewList().get(0);
//        assertEquals("transfer task to harddisk", taskTest.getDescription());
//        assertEquals("", taskTest.getStartDateString());
//        assertEquals("", taskTest.getStartTimeString());
//        assertEquals("10 May 2017", taskTest.getEndDateString());
//        assertEquals("10:00AM", taskTest.getEndTimeString());
//        assertEquals(false , taskTest.getIsDone());
//        assertEquals(true, taskTest.getTagsList().isEmpty());
//    }
//
//    @Test
//    public void testAddEventWithStartDate() {
//        userInputTest = "add transfer task to harddisk on 5/10/17";
//
//        promptTest = feedbackTest.getPrompts(userInputTest);
//        assertEquals(3, promptTest.size());
//        assertEquals("add <Description> on <Start Date>", promptTest.get(0));
//        assertEquals("add <Description> on <Start Date> #<Tag>", promptTest.get(1));
//        assertEquals("add <Description> on <Start Date> <Start Time>", promptTest.get(2));
//
//        commandTest = parserTest.parseCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//
//        commandTest = logicTest.executeCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//        taskTest = logicTest.getViewList().get(0);
//        assertEquals("transfer task to harddisk", taskTest.getDescription());
//        assertEquals("10 May 2017", taskTest.getStartDateString());
//        assertEquals("", taskTest.getStartTimeString());
//        assertEquals("", taskTest.getEndDateString());
//        assertEquals("", taskTest.getEndTimeString());
//        assertEquals(false , taskTest.getIsDone());
//        assertEquals(true, taskTest.getTagsList().isEmpty());
//    }
//
//    @Test
//    public void testAddEventWithStartDateTime() {
//        userInputTest = "add transfer task to harddisk on 5/10/17 10am";
//
//        promptTest = feedbackTest.getPrompts(userInputTest);
//        assertEquals(2, promptTest.size());
//        assertEquals("add <Description> on <Start Date> <Start Time>", promptTest.get(0));
//        assertEquals("add <Description> on <Start Date> <Start Time> #<Tag>", promptTest.get(1));
//
//        commandTest = parserTest.parseCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//
//        commandTest = logicTest.executeCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//        taskTest = logicTest.getViewList().get(0);
//        assertEquals("transfer task to harddisk", taskTest.getDescription());
//        assertEquals("10 May 2017", taskTest.getStartDateString());
//        assertEquals("10:00AM", taskTest.getStartTimeString());
//        assertEquals("", taskTest.getEndDateString());
//        assertEquals("", taskTest.getEndTimeString());
//        assertEquals(false , taskTest.getIsDone());
//        assertEquals(true, taskTest.getTagsList().isEmpty());
//    }
//
//    @Test
//    public void testAddEventWithStartDateEndDate() {
//        userInputTest = "add transfer task to harddisk from 5/10/17 to 5/20/17";
//
//        promptTest = feedbackTest.getPrompts(userInputTest);
//        assertEquals(3, promptTest.size());
//        assertEquals("add <Description> from <Start Date> to <End Date>", promptTest.get(0));
//        assertEquals("add <Description> from <Start Date> to <End Date> #<Tag>", promptTest.get(1));
//        assertEquals("add <Description> from <Start Date> to <End Date> <End Time>", promptTest.get(2));
//
//        commandTest = parserTest.parseCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//
//        commandTest = logicTest.executeCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//        taskTest = logicTest.getViewList().get(0);
//        assertEquals("transfer task to harddisk", taskTest.getDescription());
//        assertEquals("10 May 2017", taskTest.getStartDateString());
//        assertEquals("", taskTest.getStartTimeString());
//        assertEquals("20 May 2017", taskTest.getEndDateString());
//        assertEquals("", taskTest.getEndTimeString());
//        assertEquals(false , taskTest.getIsDone());
//        assertEquals(true, taskTest.getTagsList().isEmpty());
//    }
//
//    @Test
//    public void testAddEventWithStartDateTimeEndDateTime() {
//        userInputTest = "add transfer task to harddisk from 5/10/17 10am to 5/20/17 10pm";
//
//        promptTest = feedbackTest.getPrompts(userInputTest);
//        assertEquals(2, promptTest.size());
//        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> <End Time>", promptTest.get(0));
//        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> <End Time> #<Tag>", promptTest.get(1));
//
//        commandTest = parserTest.parseCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//
//        commandTest = logicTest.executeCommand(userInputTest);
//        assertTrue(commandTest instanceof AddCommand);
//        taskTest = logicTest.getViewList().get(0);
//        assertEquals("transfer task to harddisk", taskTest.getDescription());
//        assertEquals("10 May 2017", taskTest.getStartDateString());
//        assertEquals("10:00AM", taskTest.getStartTimeString());
//        assertEquals("20 May 2017", taskTest.getEndDateString());
//        assertEquals("10:00PM", taskTest.getEndTimeString());
//        assertEquals(false , taskTest.getIsDone());
//        assertEquals(true, taskTest.getTagsList().isEmpty());
//    }
//
//    @Test
//    public void testAddInvalidTag() {
//        userInputTest = "add transfer task to harddisk #tag # ";
//
//        promptTest = feedbackTest.getPrompts(userInputTest);
//        assertEquals(1, promptTest.size());
//        assertEquals("Invalid Tag: add <Description> #<Tag> #<Tag>", promptTest.get(0));
//
//        commandTest = parserTest.parseCommand(userInputTest);
//        assertTrue(commandTest instanceof InvalidCommand);
//
//        commandTest = logicTest.executeCommand(userInputTest);
//        assertTrue(commandTest instanceof InvalidCommand);
//        assertEquals("Empty tag is not allowed", ((InvalidCommand) commandTest).get_error_message());
//    }
//}
