package test;

import logic.commands.CommandInterface;
import logic.commands.UndoneCommand;
import logic.commands.InvalidCommand;
import org.junit.Test;
import parser.UndoneCommandParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lifengshuang on 4/11/16.
 */
public class UndoneCommandParserTest {

    /**
     * Test unUndone command parser
     */
    @Test
    public void testUnUndoneCommandParser() throws Exception {
        CommandInterface command1 = new UndoneCommandParser().parse("unUndone 1", 2);
        assertEquals(0, ((UndoneCommand) command1).getFirstIndex());

        CommandInterface command2 = new UndoneCommandParser().parse("unUndone 1343234", 100);//invalid
        assertTrue(command2 instanceof InvalidCommand);
        assertEquals("Undone index out of range!", command2.getMessage());

        CommandInterface command3 = new UndoneCommandParser().parse("undeUndonelete gg", 100);//invalid
        assertTrue(command3 instanceof InvalidCommand);
        assertEquals("Invalid: ID not found", command3.getMessage());

        CommandInterface command4 = new UndoneCommandParser().parse("unUndone 2 to 40", 100);
        assertEquals(1, ((UndoneCommand) command4).getFirstIndex());
        assertEquals(39, ((UndoneCommand) command4).getLastIndex());

        CommandInterface command5 = new UndoneCommandParser().parse("unUndone 1 to 400", 100);//invalid
        assertTrue(command5 instanceof InvalidCommand);
        assertEquals("Undone index out of range!", command5.getMessage());

    }

}
