package test.prompt;

import org.junit.Before;
import org.junit.Test;
import prompt.Prompt;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @@author A0149647N
 * Test ViewPrompt
 */
public class ViewPromptTest {

    Prompt prompt;

    @Before
    public void setUp() throws Exception {
        prompt = new Prompt();
    }

    /**
     * Test view prompt without keyword
     */
    @Test
    public void testNoKeyword() throws Exception {
        ArrayList<String> empty = prompt.getPrompts("view");
        assertEquals("view description <Partial Description>", empty.get(0));
        assertEquals("view events | deadlines | tasks", empty.get(1));
        assertEquals("view all | done | undone | overdue", empty.get(2));
        assertEquals("view start | end | from", empty.get(3));

        ArrayList<String> view_d = prompt.getPrompts("view d");
        assertEquals("view description <Partial Description>", view_d.get(0));
        assertEquals("view deadlines", view_d.get(1));
        assertEquals("view done", view_d.get(2));

        ArrayList<String> halfStart = prompt.getPrompts("view st");
        assertEquals("view start date <Start Date>", halfStart.get(0));
        assertEquals("view start time <Start Time>", halfStart.get(1));

        ArrayList<String> tag = prompt.getPrompts("view #t");
        assertEquals("view #<Tag>", tag.get(0));
    }

    /**
     * Test view description
     */
    @Test
    public void testDescription() throws Exception {
        ArrayList<String> description = prompt.getPrompts("view description abc");
        assertEquals("view description <Partial Description>", description.get(0));

        ArrayList<String> partialDescription = prompt.getPrompts("view abc");
        assertEquals("view <Partial Description>", partialDescription.get(0));
    }

    /**
     * Test view start date
     */
    @Test
    public void testStartDate() throws Exception {
        ArrayList<String> startDate = prompt.getPrompts("view start date today");
        assertEquals("view start date <Start Date>", startDate.get(0));

        ArrayList<String> startDateInvalid = prompt.getPrompts("view start date hdfdf");
        assertEquals("Invalid date: view start date <Start Date>", startDateInvalid.get(0));
    }

    /**
     * Test view end time
     */
    @Test
    public void testEndTime() throws Exception {
        ArrayList<String> endTime = prompt.getPrompts("view end time 1pm");
        assertEquals("view end time <End Time>", endTime.get(0));

        ArrayList<String> endTimeInvalid = prompt.getPrompts("view end time today");
        assertEquals("Invalid time: view end time <End Time>", endTimeInvalid.get(0));
    }

    /**
     * Test view types and category types
     */
    @Test
    public void testTypes() throws Exception {
        ArrayList<String> events = prompt.getPrompts("view events");
        assertEquals("view events", events.get(0));


        ArrayList<String> all = prompt.getPrompts("view all");
        assertEquals("view all", all.get(0));
    }

    /**
     * Test auto complete
     */
    @Test
    public void testAutoComplete() throws Exception {
        prompt.getPrompts("view d");
        String description = prompt.getAutoComplete("view d");
        assertEquals("view description", description);

        prompt.getPrompts("view st");
        String startDate1 = prompt.getAutoComplete("view st");
        assertEquals("view start", startDate1);

        prompt.getPrompts("view start d");
        String startDate2 = prompt.getAutoComplete("view start d");
        assertEquals("view start date", startDate2);

        prompt.getPrompts("view e");
        String events = prompt.getAutoComplete("view e");
        assertEquals("view events", events);
    }
}
