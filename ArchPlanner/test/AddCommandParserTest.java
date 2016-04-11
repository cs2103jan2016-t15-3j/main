package test;

import logic.commands.AddCommand;
import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import org.junit.Before;
import org.junit.Test;
import parser.AddCommandParser;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lifengshuang on 4/10/16.
 */
public class AddCommandParserTest {


    LocalDate dateNow;
    LocalDate tomorrow;

    @Before
    public void setUp() throws Exception {
        dateNow = LocalDate.now();
        tomorrow = dateNow.plusDays(1);
    }

    /**
     * This method test keyword on with different time type
     */
    @Test
    public void testKeywordOn() throws Exception {
        //add ... on <date>
        CommandInterface onlyDateKeywordOn = new AddCommandParser().parse("add sth123 on tomorrow");
        assertEquals("sth123", ((AddCommand) onlyDateKeywordOn).getTaskParameters().getDescription());
        assertEquals(tomorrow, ((AddCommand) onlyDateKeywordOn).getTaskParameters().getStartDate());

        //add ... on <date> <time>
        CommandInterface dateTimeKeywordOn = new AddCommandParser().parse("add assignments on this Thursday 3pm #assign #oh-my-god #help-me!");
        LocalTime threePm = LocalTime.of(15, 0);
        assertEquals(threePm, ((AddCommand) dateTimeKeywordOn).getTaskParameters().getStartTime());
        assertEquals("#assign", ((AddCommand) dateTimeKeywordOn).getTaskParameters().getTagsList().get(0));
        assertEquals("#oh-my-god", ((AddCommand) dateTimeKeywordOn).getTaskParameters().getTagsList().get(1));
        assertEquals("#help-me!", ((AddCommand) dateTimeKeywordOn).getTaskParameters().getTagsList().get(2));

        //add ... on <time>
        CommandInterface timeKeywordOn = new AddCommandParser().parse("add sth on 3pm");
        assertEquals(dateNow, ((AddCommand) timeKeywordOn).getTaskParameters().getStartDate());
        assertEquals(threePm, ((AddCommand) timeKeywordOn).getTaskParameters().getStartTime());
    }

    /**
     * This method test keyword by with different time type
     */
    @Test
    public void testKeywordBy() throws Exception {
        //add ... by <date>
        CommandInterface onlyDateKeywordBy = new AddCommandParser().parse("add miao by Mar 7 #a #b");
        LocalDate mar7 = LocalDate.of(dateNow.getYear(), 3, 7);
        assertEquals(mar7, ((AddCommand) onlyDateKeywordBy).getTaskParameters().getEndDate());
        assertEquals("#a", ((AddCommand) onlyDateKeywordBy).getTaskParameters().getTagsList().get(0));
        assertEquals("#b", ((AddCommand) onlyDateKeywordBy).getTaskParameters().getTagsList().get(1));

        //add ... by <date><time>
        CommandInterface dateTimeKeywordBy = new AddCommandParser().parse("add manual V0.2 by Nov 2 23:59:59");
        LocalDate nov2 = LocalDate.of(dateNow.getYear(), 11, 2);
        assertEquals(nov2, ((AddCommand) dateTimeKeywordBy).getTaskParameters().getEndDate());
        LocalTime endOfDay = LocalTime.of(23, 59, 59);
        assertEquals(endOfDay, ((AddCommand) dateTimeKeywordBy).getTaskParameters().getEndTime());
        assertEquals(null, ((AddCommand) dateTimeKeywordBy).getTaskParameters().getStartDate());
        assertEquals(null, ((AddCommand) dateTimeKeywordBy).getTaskParameters().getStartTime());

        //add ... by <time>
        CommandInterface timeKeywordBy = new AddCommandParser().parse("add sth by 11pm");
        assertEquals(dateNow, ((AddCommand) timeKeywordBy).getTaskParameters().getEndDate());
        LocalTime elevenPm = LocalTime.of(23, 0, 0);
        assertEquals(elevenPm, ((AddCommand) timeKeywordBy).getTaskParameters().getEndTime());
    }

    /**
     * This method test keyword from with different time type
     */
    @Test
    public void testKeywordFrom() throws Exception {
        //add ... from <date> to <date>
        CommandInterface onlyDateKeywordFrom = new AddCommandParser().parse("add lalala from today to tomorrow #a");
        assertEquals("lalala", ((AddCommand) onlyDateKeywordFrom).getTaskParameters().getDescription());
        assertEquals(dateNow, ((AddCommand) onlyDateKeywordFrom).getTaskParameters().getStartDate());
        assertEquals(tomorrow, ((AddCommand) onlyDateKeywordFrom).getTaskParameters().getEndDate());

        //add ... from <date><time> to <date><time>
        CommandInterface dateTimeDateTime = new AddCommandParser().parse("add a lot of things from March 22 0:00:00 to May 3 8:00");
        assertEquals("a lot of things", ((AddCommand) dateTimeDateTime).getTaskParameters().getDescription());
        LocalDate mar22 = LocalDate.of(dateNow.getYear(), 3, 22);
        LocalDate may3 = LocalDate.of(dateNow.getYear(), 5, 3);
        assertEquals(mar22, ((AddCommand) dateTimeDateTime).getTaskParameters().getStartDate());
        assertEquals(may3, ((AddCommand) dateTimeDateTime).getTaskParameters().getEndDate());
        LocalTime zero = LocalTime.of(0, 0, 0);
        LocalTime eight = LocalTime.of(8, 0, 0);
        assertEquals(zero, ((AddCommand) dateTimeDateTime).getTaskParameters().getStartTime());
        assertEquals(eight, ((AddCommand) dateTimeDateTime).getTaskParameters().getEndTime());

        //add ... from <date><time> to <time>
        CommandInterface dateTimeToTime = new AddCommandParser().parse("add movie from May 9 13:00 to 15:00");
        LocalDate may9 = LocalDate.of(dateNow.getYear(), 5, 9);
        assertEquals(may9, ((AddCommand) dateTimeToTime).getTaskParameters().getStartDate());
        assertEquals(may9, ((AddCommand) dateTimeToTime).getTaskParameters().getEndDate());

        //add ... from <time> to <time>
        CommandInterface timeToTimeKeywordFrom = new AddCommandParser().parse("add movie from 13:00 to 3pm");
        LocalTime onePm = LocalTime.of(13, 0, 0);
        LocalTime threePm = LocalTime.of(15, 0);
        assertEquals(dateNow, ((AddCommand) timeToTimeKeywordFrom).getTaskParameters().getStartDate());
        assertEquals(dateNow, ((AddCommand) timeToTimeKeywordFrom).getTaskParameters().getEndDate());
        assertEquals(onePm, ((AddCommand) timeToTimeKeywordFrom).getTaskParameters().getStartTime());
        assertEquals(threePm, ((AddCommand) timeToTimeKeywordFrom).getTaskParameters().getEndTime());
    }

    /**
     * This method test description only command without time
     */
    @Test
    public void testDescriptionOnly() throws Exception {
        //add ...
        CommandInterface onlyDescription = new AddCommandParser().parse("add assignments 2");
        assertEquals("assignments 2", ((AddCommand) onlyDescription).getTaskParameters().getDescription());
    }

    /**
     * This method test misusing the keyword and wrong time
     * All the misused time string will be recognized as description
     */
    @Test
    public void testKeywordWrongFormat() throws Exception {
        CommandInterface wrongKeyword = new AddCommandParser().parse("add movie at Friday 13:00 to 15:00");
        assertEquals("movie at Friday 13:00 to 15:00", ((AddCommand) wrongKeyword).getTaskParameters().getDescription());

        CommandInterface unmatchedKeyword = new AddCommandParser().parse("add movie from Friday 13:00");
//        assertEquals("movie from Friday 13:00", ((AddCommand) unmatchedKeyword).getTaskParameters().getDescription());
        assertTrue(unmatchedKeyword instanceof InvalidCommand);
        assertEquals("Invalid Time!", unmatchedKeyword.getMessage());

        CommandInterface wrongTime = new AddCommandParser().parse("add movie from Friday 23:61");
        assertEquals("movie from Friday 23:61", ((AddCommand) wrongTime).getTaskParameters().getDescription());
    }

    /**
     * This method test invalid command which has empty tag
     */
    @Test
    public void testInvalidInput() throws Exception {
        CommandInterface invalidTag = new AddCommandParser().parse("add assignments 2 on today #tag #");
        assertTrue(invalidTag instanceof InvalidCommand);
    }
}
