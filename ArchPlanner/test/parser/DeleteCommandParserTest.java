package test.parser;

import logic.commands.CommandInterface;
import logic.commands.DeleteCommand;
import logic.commands.InvalidCommand;
import org.junit.Test;
import interpreter.parser.DeleteCommandParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @@author A0149647N
 * Test DeleteCommandParser
 */
public class DeleteCommandParserTest {

    /**
     * Test delete command parser
     */
    @Test
    public void testDeleteCommandParser() throws Exception {
        CommandInterface command1 = new DeleteCommandParser().parse("delete 1", 2);
        assertEquals(0, ((DeleteCommand)command1).getFirstIndex());

        CommandInterface command2 = new DeleteCommandParser().parse("delete 1343234", 100);//invalid
        assertTrue(command2 instanceof InvalidCommand);
        assertEquals("Delete index out of range!", command2.getMessage());

        CommandInterface command3 = new DeleteCommandParser().parse("delete gg", 100);//invalid
        assertTrue(command3 instanceof InvalidCommand);
        assertEquals("Invalid: ID not found", command3.getMessage());

        CommandInterface command4 = new DeleteCommandParser().parse("delete 2 to 40", 100);
        assertEquals(1, ((DeleteCommand)command4).getFirstIndex());
        assertEquals(39, ((DeleteCommand)command4).getLastIndex());

        CommandInterface command5 = new DeleteCommandParser().parse("delete 1 to 400", 100);//invalid
        assertTrue(command5 instanceof InvalidCommand);
        assertEquals("Delete index out of range!", command5.getMessage());

    }

}
