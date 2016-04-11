package test.prompt;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import interpreter.prompt.Prompt;

/**
 * This class is used to conduct unit testing on CommandPrompt class which provide prompt the available command
 * 
 * @@author A0140034B
 */
public class CommandPromptTest {

    ArrayList<String> prompts;
    Prompt promptObj;
    
    @Before
    public void setUp() throws Exception {
        prompts = new ArrayList<String>();
        promptObj = new Prompt();
    }
    
    @Test
    public void testCommandPrompts() {
        prompts = promptObj.getPrompts("a");
        assertEquals(1, prompts.size());
        assertEquals("add", prompts.get(0));
     
        prompts = promptObj.getPrompts("s");
        assertEquals(1, prompts.size());
        assertEquals("set", prompts.get(0));
        
        prompts = promptObj.getPrompts("r");
        assertEquals(1, prompts.size());
        assertEquals("redo", prompts.get(0));
   
        prompts = promptObj.getPrompts("v");
        assertEquals(1, prompts.size());
        assertEquals("view", prompts.get(0));
        
        prompts = promptObj.getPrompts("u");
        assertEquals(2, prompts.size());
        assertEquals("undone", prompts.get(0));
        assertEquals("undo", prompts.get(1));
        
        prompts = promptObj.getPrompts("undon");
        assertEquals(1, prompts.size());
        assertEquals("undone", prompts.get(0));
        
        prompts = promptObj.getPrompts("d");
        assertEquals(2, prompts.size());
        assertEquals("delete", prompts.get(0));
        assertEquals("done", prompts.get(1));
        
        prompts = promptObj.getPrompts("de");
        assertEquals(1, prompts.size());
        assertEquals("delete", prompts.get(0));
        
        prompts = promptObj.getPrompts("do");
        assertEquals(1, prompts.size());
        assertEquals("done", prompts.get(0));
        
        prompts = promptObj.getPrompts("e");
        assertEquals(2, prompts.size());
        assertEquals("edit", prompts.get(0));
        assertEquals("exit", prompts.get(1));
        
        prompts = promptObj.getPrompts("ed");
        assertEquals(1, prompts.size());
        assertEquals("edit", prompts.get(0));
        
        prompts = promptObj.getPrompts("ex");
        assertEquals(1, prompts.size());
        assertEquals("exit", prompts.get(0));
    }

}
