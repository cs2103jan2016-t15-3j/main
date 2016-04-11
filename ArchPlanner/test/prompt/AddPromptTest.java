package test.prompt;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import interpreter.prompt.Prompt;

/**
 * This class is used to conduct unit testing on AddPrompt class which provide prompts for add command format
 * 
 * @@author A0140034B
 */
public class AddPromptTest {
    ArrayList<String> prompts;
    Prompt promptObj;
    
    @Before
    public void setUp() throws Exception {
        prompts = new ArrayList<String>();
        promptObj = new Prompt();
    }
    
    @Test
    public void testDescriptionRegion() {
        prompts = promptObj.getPrompts("add ");
        assertEquals(1, prompts.size());
        assertEquals("add <Description>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description ");
        assertEquals(4, prompts.size());
        assertEquals("add <Description> #<Tag>", prompts.get(0));
        assertEquals("add <Description> on <Start Date>", prompts.get(1));
        assertEquals("add <Description> by <End Date>", prompts.get(2));
        assertEquals("add <Description> from <Start Date> to <End Date>", prompts.get(3));
    }
    
    @Test
    public void testKeyWordRegion() {
        prompts = promptObj.getPrompts("add description on ");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> on <Start Date>", prompts.get(0));
        assertEquals("add <Description> on <Start Time>", prompts.get(1));
        
        prompts = promptObj.getPrompts("add description by ");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> by <End Date>", prompts.get(0));
        assertEquals("add <Description> by <End Time>", prompts.get(1));
        
        prompts = promptObj.getPrompts("add description from ");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> from <Start Date>", prompts.get(0));
        assertEquals("add <Description> from <Start Time>", prompts.get(1));
    }
    
    @Test
    public void testKeyWordOnFirstDateRegion() {
        prompts = promptObj.getPrompts("add description on today");
        assertEquals(3, prompts.size());
        assertEquals("add <Description> on <Start Date>", prompts.get(0));
        assertEquals("add <Description> on <Start Date> #<Tag>", prompts.get(1));
        assertEquals("add <Description> on <Start Date> <Start Time>", prompts.get(2));
        
        prompts = promptObj.getPrompts("add description on 1pm");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> on <Start Time>", prompts.get(0));
        assertEquals("add <Description> on <Start Time> #<Tag>", prompts.get(1));
        
        prompts = promptObj.getPrompts("add description on today 1pm");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> on <Start Date> <Start Time>", prompts.get(0));
        assertEquals("add <Description> on <Start Date> <Start Time> #<Tag>", prompts.get(1));
    }
    
    @Test
    public void testKeyWordByFirstDateRegion() {
        prompts = promptObj.getPrompts("add description by today");
        assertEquals(3, prompts.size());
        assertEquals("add <Description> by <End Date>", prompts.get(0));
        assertEquals("add <Description> by <End Date> #<Tag>", prompts.get(1));
        assertEquals("add <Description> by <End Date> <End Time>", prompts.get(2));
        
        prompts = promptObj.getPrompts("add description by 1pm");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> by <End Time>", prompts.get(0));
        assertEquals("add <Description> by <End Time> #<Tag>", prompts.get(1));
        
        prompts = promptObj.getPrompts("add description by today 1pm");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> by <End Date> <End Time>", prompts.get(0));
        assertEquals("add <Description> by <End Date> <End Time> #<Tag>", prompts.get(1));
    }
    
    @Test
    public void testKeyWordFromFirstDateRegion() {
        prompts = promptObj.getPrompts("add description from today");
        assertEquals(3, prompts.size());
        assertEquals("add <Description> from <Start Date> to <End Date>", prompts.get(0));
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Time>", prompts.get(1));
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> <End Time>", prompts.get(2));
        
        prompts = promptObj.getPrompts("add description from 1pm");
        assertEquals(3, prompts.size());
        assertEquals("add <Description> from <Start Time> to <End Time>", prompts.get(0));
        assertEquals("add <Description> from <Start Time> to <End Date>", prompts.get(1));
        assertEquals("add <Description> from <Start Time> to <End Date> <End Time>", prompts.get(2));
        
        prompts = promptObj.getPrompts("add description from today 1pm");
        assertEquals(3, prompts.size());
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Time>", prompts.get(0));
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date>", prompts.get(1));
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> <End Time>", prompts.get(2));
    }
    
    @Test
    public void testKeyWordFromSecondDateRegion() {
        prompts = promptObj.getPrompts("add description from today 1pm to tomorrow");
        assertEquals(3, prompts.size());
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date>", prompts.get(0));
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> #<Tag>", prompts.get(1));
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> <End Time>", prompts.get(2));
        
        prompts = promptObj.getPrompts("add description from today 1pm to 2pm");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Time>", prompts.get(0));
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Time> #<Tag>", prompts.get(1));
        
        prompts = promptObj.getPrompts("add description from today 1pm to tomorrow 2pm");
        assertEquals(2, prompts.size());
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> <End Time>", prompts.get(0));
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> <End Time> #<Tag>", prompts.get(1));
    }
    
    @Test
    public void testTagRegion() {
        prompts = promptObj.getPrompts("add description #tag");
        assertEquals(1, prompts.size());
        assertEquals("add <Description> #<Tag>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description on today #tag");
        assertEquals(1, prompts.size());
        assertEquals("add <Description> on <Start Date> #<Tag>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description by today 1pm #tag");
        assertEquals(1, prompts.size());
        assertEquals("add <Description> by <End Date> <End Time> #<Tag>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description from today 1pm to tomorrow #tag");
        assertEquals(1, prompts.size());
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> #<Tag>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description from today 1pm to tomorrow 2pm #tag");
        assertEquals(1, prompts.size());
        assertEquals("add <Description> from <Start Date> <Start Time> to <End Date> <End Time> #<Tag>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description #tag #tag2");
        assertEquals(1, prompts.size());
        assertEquals("add <Description> #<Tag> #<Tag>", prompts.get(0));
    }
    
    @Test
    public void testInvalidTag() {
        prompts = promptObj.getPrompts("add description # ");
        assertEquals(1, prompts.size());
        assertEquals("Invalid Tag: add <Description> #<Tag>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description #tag invalid");
        assertEquals(1, prompts.size());
        assertEquals("Invalid Tag: add <Description> #<Tag> #<Tag>", prompts.get(0));
    }
    
    @Test
    public void testInvalidDateRange() {
        prompts = promptObj.getPrompts("add description from tomorrow to today");
        assertEquals(1, prompts.size());
        assertEquals("Invalid Date Range: add <Description> from <Start Date> to <End Date>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description from tomorrow 1pm to 12pm");
        assertEquals(1, prompts.size());
        assertEquals("Invalid Date Range: add <Description> from <Start Date> <Start Time> to <End Time>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description from 1pm to today 12pm");
        assertEquals(1, prompts.size());
        assertEquals("Invalid Date Range: add <Description> from <Start Time> to <End Date> <End Time>", prompts.get(0));
        
        prompts = promptObj.getPrompts("add description from 1pm to 12pm");
        assertEquals(1, prompts.size());
        assertEquals("Invalid Date Range: add <Description> from <Start Time> to <End Time>", prompts.get(0));
    }
    
    @Test
    public void testInvalidDateRangeAndTag() {
        prompts = promptObj.getPrompts("add description from tomorrow to today # ");
        assertEquals(1, prompts.size());
        assertEquals("Invalid Date Range and Tag: add <Description> from <Start Date> to <End Date> #<Tag>", prompts.get(0));
    }
}
