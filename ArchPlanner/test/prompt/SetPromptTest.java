package test.prompt;

import org.junit.Before;
import org.junit.Test;
import interpreter.prompt.Prompt;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @@author A0149647N
 * Test SetPrompt
 */
public class SetPromptTest {
    Prompt prompt;

    @Before
    public void setUp() throws Exception {
        prompt = new Prompt();
    }

    /**
     * Test set prompt
     */
    @Test
    public void testSetPrompt() throws Exception {
        ArrayList<String> emptySet = prompt.getPrompts("set");
        assertEquals("set filepath <New File Path>", emptySet.get(0));
        assertEquals("set filepath default", emptySet.get(1));

        ArrayList<String> halfDefaultSet = prompt.getPrompts("set filepath def");
        assertEquals("set filepath default", halfDefaultSet.get(0));
        assertEquals("set filepath <New File Path>", halfDefaultSet.get(1));

        ArrayList<String> pathSet = prompt.getPrompts("set filepath C:\\\\path");
        assertEquals("set filepath <New File Path>", pathSet.get(0));
    }

    @Test
    public void testAutoComplete() throws Exception {

        prompt.getPrompts("set f");
        String filepath = prompt.getAutoComplete("set f");
        assertEquals("set filepath", filepath);

        prompt.getPrompts("set filepath de");
        String defaultPath = prompt.getAutoComplete("set filepath de");
        assertEquals("set filepath default", defaultPath);
    }
}
