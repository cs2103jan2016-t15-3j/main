package test.parser;

import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import logic.commands.SetCommand;
import org.junit.Test;
import parser.SetCommandParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @@author A0149647N
 * Test SetCommandParser
 */
public class SetCommandParserTest {

    /**
     * Test set command parser
     */
    @Test
    public void testSetCommandParser() throws Exception {

        CommandInterface command1 = new SetCommandParser().parse("set filepath /usr/bin/storage");
        assertEquals("/usr/bin/storage.txt", ((SetCommand) command1).getFilePath());

        CommandInterface command2 = new SetCommandParser().parse("set filepath default");
        assertEquals(null, ((SetCommand) command2).getFilePath());

        CommandInterface command3 = new SetCommandParser().parse("set filepath default/bin/a");
        assertEquals("default/bin/a.txt", ((SetCommand) command3).getFilePath());

        CommandInterface command4 = new SetCommandParser().parse("set filepath path/to/file");
        assertEquals("path/to/file.txt", ((SetCommand) command4).getFilePath());

        CommandInterface command5 = new SetCommandParser().parse("set filep");
        assertTrue(command5 instanceof InvalidCommand);
    }
}
