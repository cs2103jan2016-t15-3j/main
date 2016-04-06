//@@author A0140034B
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import prompt.Prompt;

public class AddPromptTest {

    @Test
    public void test() {
        ArrayList<String> prompts = new ArrayList<String>();
        Prompt feedback = new Prompt();
        /*
        //partition 1 - command
        //valid
        //boundary 1
        prompts = feedback.getPrompts("a");     
        assertEquals(1, prompts.size());
        assertEquals("add", prompts.get(0));
        //boundary 2
        prompts = feedback.getPrompts("add");  
        assertEquals(1, prompts.size());
        assertEquals("add", prompts.get(0));
        
        //invalid
        //boundary 1
        prompts = feedback.getPrompts(" ");     
        assertEquals(1, prompts.size());
        assertEquals("Invalid command", prompts.get(0));
        
        prompts = feedback.getPrompts("qwe");  
        assertEquals(1, prompts.size());
        assertEquals("Invalid command: add | delete | edit | view | done | undone | undo | redo | exit", prompts.get(0));
        
        
        //partition 2 - description
        prompts = feedback.getPrompts("add ");
        assertEquals(1, prompts.size());
        assertEquals("add <Description>", prompts.get(0));
        
        prompts = feedback.getPrompts("add d ");
        assertEquals(4, prompts.size());
        assertEquals("add <Description> #<Tag>", prompts.get(0));
        assertEquals("add <Description> on <Start Date>", prompts.get(1));
        assertEquals("add <Description> by <End Date>", prompts.get(2));
        assertEquals("add <Description> from <Start Date> to <End Date>", prompts.get(3));
        
        //parititon 3 - date region
        //boundary 1
        prompts = feedback.getPrompts("add description on ");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> on <Start Date>", prompts.get(0));
        assertEquals("add <Description> on <Start Time>", prompts.get(1));
        //boundary 2
        prompts = feedback.getPrompts("add description on today 1pm");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> on <Start Date> <Start Time>", prompts.get(0));
        assertEquals("add <Description> on <Start Date> <Start Time> #<Tag>", prompts.get(1));
        
        //partition 4 - tag region
        //boundary 1
        //valid
        prompts = feedback.getPrompts("add description #");
        assertEquals(1, prompts.size());
        assertEquals("add <Description> #<Tag>", prompts.get(0));

        prompts = feedback.getPrompts("add description #tag1 #tag2");
        assertEquals(1, prompts.size());
        assertEquals("add <Description> #<Tag> #<Tag>", prompts.get(0));
        
        //invalid
        prompts = feedback.getPrompts("add description # ");
        assertEquals(1, prompts.size());
        assertEquals("Invalid Tag: add <Description> #<Tag>", prompts.get(0));
        */
    }
}
