package test.parser;

import logic.commands.CommandInterface;
import logic.commands.EditCommand;
import logic.commands.InvalidCommand;
import org.junit.Test;
import parser.EditCommandParser;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @@author A0149647N
 * Test EditCommandParser
 */
public class EditCommandParserTest {

    /**
     * Test edit description
     */
    @Test
    public void testDescription() {
        //edit <id> description ...
        CommandInterface command1 = new EditCommandParser().parse("edit 3 description lalala", 10);
        assertEquals("lalala", ((EditCommand) command1).getTaskParameters().getDescription());

        CommandInterface command2 = new EditCommandParser().parse("edit 8 description", 10);
        assertTrue(command2 instanceof InvalidCommand);
        assertEquals("Argument Missing", command2.getMessage());

        CommandInterface command3 = new EditCommandParser().parse("edit 43 description bbb", 10);//invalid
        assertTrue(command3 instanceof InvalidCommand);
        assertEquals("Index out of range", command3.getMessage());

    }

    /**
     * Test edit start date and time
     */
    @Test
    public void testStart() throws Exception {

        //edit <id> start ...
        CommandInterface command4 = new EditCommandParser().parse("edit 1 start May 5", 10);
        LocalDate dateNow = LocalDate.now();
        LocalDate may5 = LocalDate.of(dateNow.getYear(), 5, 5);
        assertEquals(may5, ((EditCommand) command4).getTaskParameters().getStartDate());

        CommandInterface command5 = new EditCommandParser().parse("edit -1 start this friday 3pm", 10);//invalid
        assertTrue(command5 instanceof InvalidCommand);
        assertEquals("Index out of range", command5.getMessage());

        CommandInterface command6 = new EditCommandParser().parse("edit 3 start time 3pm", 10);
        assertTrue(command6 instanceof InvalidCommand);
        assertEquals("Invalid start date or time", command6.getMessage());

        CommandInterface command7 = new EditCommandParser().parse("edit 3 start 23:23:23", 10);//invalid
        LocalTime command7Time = LocalTime.of(23, 23, 23);
        assertEquals(command7Time, ((EditCommand) command7).getTaskParameters().getStartTime());
    }

    /**
     * Test edit end date and time
     */
    @Test
    public void testEnd() throws Exception {

        //edit <id> end ...
        CommandInterface command9 = new EditCommandParser().parse("edit 5 end tomorrow 1pm", 10);
        LocalDate dateNow = LocalDate.now();
        assertEquals(dateNow.plusDays(1), ((EditCommand) command9).getTaskParameters().getEndDate());
        LocalTime onePm = LocalTime.of(13, 0, 0);
        assertEquals(onePm, ((EditCommand) command9).getTaskParameters().getEndTime());

        CommandInterface command10 = new EditCommandParser().parse("edit 3 end 3pm", 10);
        LocalTime threePm = LocalTime.of(15, 0, 0);
        assertEquals(threePm, ((EditCommand) command10).getTaskParameters().getEndTime());

    }

    /**
     * Test edit tag of a task
     */
    @Test
    public void testTag() throws Exception {
        //edit <id> #tags
        CommandInterface command11 = new EditCommandParser().parse("edit 3 #a #assignments #233", 10);
        assertEquals("#a", ((EditCommand) command11).getTaskParameters().getTagsList().get(0));
        assertEquals("#assignments", ((EditCommand) command11).getTaskParameters().getTagsList().get(1));
        assertEquals("#233", ((EditCommand) command11).getTaskParameters().getTagsList().get(2));

        CommandInterface command12 = new EditCommandParser().parse("edit 3 #123 fdsf", 10);//invalid
        assertTrue(command12 instanceof InvalidCommand);

    }
}
