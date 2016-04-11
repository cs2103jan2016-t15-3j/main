package test.parser;

import logic.commands.CommandInterface;
import logic.commands.DoneCommand;
import logic.commands.InvalidCommand;
import org.junit.Test;
import parser.DoneCommandParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lifengshuang on 4/11/16.
 */
public class DoneCommandParserTest {
    /**
     * Test done command parser
     */
    @Test
    public void testDoneCommandParser() throws Exception {
        CommandInterface command1 = new DoneCommandParser().parse("done 1", 2);
        assertEquals(0, ((DoneCommand)command1).getFirstIndex());

        CommandInterface command2 = new DoneCommandParser().parse("done 1343234", 100);//invalid
        assertTrue(command2 instanceof InvalidCommand);
        assertEquals("Done index out of range!", command2.getMessage());

        CommandInterface command3 = new DoneCommandParser().parse("dedonelete gg", 100);//invalid
        assertTrue(command3 instanceof InvalidCommand);
        assertEquals("Invalid: ID not found", command3.getMessage());

        CommandInterface command4 = new DoneCommandParser().parse("done 2 to 40", 100);
        assertEquals(1, ((DoneCommand)command4).getFirstIndex());
        assertEquals(39, ((DoneCommand)command4).getLastIndex());

        CommandInterface command5 = new DoneCommandParser().parse("done 1 to 400", 100);//invalid
        assertTrue(command5 instanceof InvalidCommand);
        assertEquals("Done index out of range!", command5.getMessage());

    }
}
