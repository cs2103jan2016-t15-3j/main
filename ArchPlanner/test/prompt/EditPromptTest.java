package test.prompt;

import org.junit.Before;
import org.junit.Test;
import prompt.Prompt;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lifengshuang on 4/11/16.
 */
public class EditPromptTest {

    Prompt prompt;

    @Before
    public void setUp() throws Exception {
        prompt = new Prompt();
    }

    /**
     * Test edit command without keyword
     */
    @Test
    public void testNoKeyword() throws Exception {
        ArrayList<String> emptyKeywordPrompt = prompt.getPrompts("edit");
        assertEquals("edit <Task ID>", emptyKeywordPrompt.get(0));

        ArrayList<String> idOnlyPrompt = prompt.getPrompts("edit 3");
        assertEquals("edit <Task ID> description", idOnlyPrompt.get(0));
        assertEquals("edit <Task ID> start | end | from", idOnlyPrompt.get(1));
        assertEquals("edit <Task ID> #<Tag>", idOnlyPrompt.get(2));

        ArrayList<String> halfStartPrompt = prompt.getPrompts("edit 3 st");
        assertEquals("edit <Task ID> start", halfStartPrompt.get(0));
        assertEquals("edit <Task ID> start remove", halfStartPrompt.get(1));

        ArrayList<String> tagPrompt = prompt.getPrompts("edit 3 #tag1 #tag2 #");
        assertEquals("edit <Task ID> #<Tag> #<Tag> #<Tag>", tagPrompt.get(0));
    }

    /**
     * Test edit command with keyword description
     */
    @Test
    public void testDescription() throws Exception {
        ArrayList<String> descriptionPrompt = prompt.getPrompts("edit 3 description assignment 3");
        assertEquals("edit <Task ID> description <Description>", descriptionPrompt.get(0));

        ArrayList<String> descriptionPromptWithoutKeyword = prompt.getPrompts("edit 4 oh my god");
        assertEquals("Invalid Edit Command", descriptionPromptWithoutKeyword.get(0));
    }

    /**
     * Test edit command with keyword start
     */
    @Test
    public void testStart() throws Exception {
        ArrayList<String> startPrompt = prompt.getPrompts("edit 4 start");
        assertEquals("edit <Task ID> start <Date>", startPrompt.get(0));
        assertEquals("edit <Task ID> start <Time>", startPrompt.get(1));
        assertEquals("edit <Task ID> start <Date> <Time>", startPrompt.get(2));
        assertEquals("edit <Task ID> start remove", startPrompt.get(3));

        ArrayList<String> startDatePrompt = prompt.getPrompts("edit 4 start Mar 3");
        assertEquals("edit <Task ID> start <Date>", startDatePrompt.get(0));
        assertEquals("edit <Task ID> start <Date> <Time>", startDatePrompt.get(1));
    }

    /**
     * Test edit command with keyword end
     */
    @Test
    public void testEnd() throws Exception {
        ArrayList<String> endTime = prompt.getPrompts("edit 4 end 1pm");
        assertEquals("edit <Task ID> end <Time>", endTime.get(0));

        ArrayList<String> endRemove = prompt.getPrompts("edit 4 end rem");
        assertEquals("edit <Task ID> end remove", endRemove.get(0));
    }

    /**
     * Test edit command with keyword from
     */
    @Test
    public void testFrom() throws Exception {
        ArrayList<String> from = prompt.getPrompts("edit 6 from");
        assertEquals("edit <Task ID> from <Start Date> to <End Date>", from.get(0));
        assertEquals("edit <Task ID> from <Start Time> to <End Time>", from.get(1));
        assertEquals("edit <Task ID> from <Start Date> <Start Time> to <End Date> <End Time>", from.get(2));

        ArrayList<String> fromDateTime = prompt.getPrompts("edit 3 from May 5 1pm");
        assertEquals("edit <Task ID> from <Start Date> <Start Time> to <End Time>", fromDateTime.get(0));
        assertEquals("edit <Task ID> from <Start Date> <Start Time> to <End Date> <End Time>", fromDateTime.get(1));
    }

    /**
     * Test invalid cases
     */
    @Test
    public void testInvalid() throws Exception {
        ArrayList<String> invalidId = prompt.getPrompts("edit from May 5 1pm");
        assertEquals("Invalid ID: edit <Task ID>", invalidId.get(0));

        ArrayList<String> invalidTime = prompt.getPrompts("edit 3 start some day");
        assertEquals("Invalid Time", invalidTime.get(0));

        ArrayList<String> invalidTag = prompt.getPrompts("edit 3 #tag dfd");
        assertEquals("Invalid tag: edit <Task ID> #<Tag>", invalidTag.get(0));
    }

    /**
     * Test auto complete
     */
    @Test
    public void testAutoComplete() throws Exception {

        prompt.getPrompts("edit 3 d");
        String description = prompt.getAutoComplete("edit 3 d");
        assertEquals("edit 3 description", description);

        prompt.getPrompts("edit 3 st");
        String start = prompt.getAutoComplete("edit 3 st");
        assertEquals("edit 3 start", start);

        prompt.getPrompts("edit 3 start r");
        String startRemove = prompt.getAutoComplete("edit 3 start r");
        assertEquals("edit 3 start remove", startRemove);
    }
}
