package test.prompt;

import org.junit.Before;
import org.junit.Test;
import prompt.Prompt;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lifengshuang on 4/11/16.
 */
public class IdOnlyPromptTest {

    Prompt prompt;

    @Before
    public void setUp() throws Exception {
        prompt = new Prompt();
    }

    /**
     * Test ID only prompt with one id only
     */
    @Test
    public void testOneId() throws Exception {
        ArrayList<String> oneId = prompt.getPrompts("delete 3");
        assertEquals("delete <Task ID>", oneId.get(0));
        assertEquals("delete <Task ID> to <Task ID>", oneId.get(1));

        ArrayList<String> oneInvalidId = prompt.getPrompts("done -3");
        assertEquals("Invalid ID: done <Task ID>", oneInvalidId.get(0));
    }

    /**
     * Test ID only prompt with two id
     */
    @Test
    public void testTwoId() throws Exception {
        ArrayList<String> twoId = prompt.getPrompts("delete 3 to 10");
        assertEquals("delete <Task ID> to <Task ID>", twoId.get(0));

        ArrayList<String> twoIdInvalid = prompt.getPrompts("undone 30 to 10");
        assertEquals("Invalid command", twoIdInvalid.get(0));
    }
}
