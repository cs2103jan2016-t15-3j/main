package test;

import logic.Tag;
import logic.commands.*;
import org.junit.Test;
import parser.*;
import parser.time.TimeParser;
import parser.time.TimeParserResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by lifengshuang on 3/8/16.
 */
public class ParserTest {
    @Test
    public void testAddCommandParser() throws Exception {

        LocalDate dateNow = LocalDate.now();
        LocalDate tomorrow = dateNow.plusDays(1);

        /* Only date */
        //add ... on <date>
        CommandInterface onlyDateKeywordOn = new AddCommandParser().parse("add sth123 on tomorrow");
        assertEquals("sth123", ((AddCommand) onlyDateKeywordOn).getTask().getDescription());
        assertEquals(tomorrow, ((AddCommand) onlyDateKeywordOn).getTask().getStartDate());

        //add ... from <date> to <date>
        CommandInterface onlyDateKeywordFrom = new AddCommandParser().parse("add lalala from today to tomorrow #a");
        assertEquals("lalala", ((AddCommand) onlyDateKeywordFrom).getTask().getDescription());
        assertEquals(dateNow, ((AddCommand) onlyDateKeywordFrom).getTask().getStartDate());
        assertEquals(tomorrow, ((AddCommand) onlyDateKeywordFrom).getTask().getEndDate());

        //add ... by <date>
        CommandInterface onlyDateKeywordBy = new AddCommandParser().parse("add miao by Mar 7 #a #b");
        LocalDate mar7 = LocalDate.of(dateNow.getYear(), 3, 7);
        assertEquals(mar7, ((AddCommand) onlyDateKeywordBy).getTask().getEndDate());
        assertEquals("#a", ((AddCommand) onlyDateKeywordBy).getTask().getTagsList().get(0));
        assertEquals("#b", ((AddCommand) onlyDateKeywordBy).getTask().getTagsList().get(1));

        /* Date and time */
        //add ... on <date> <time>
        CommandInterface dateTimeKeywordOn = new AddCommandParser().parse("add assignments on this Thursday 3pm #assign #oh-my-god #help-me!");
        LocalTime threePm = LocalTime.of(15, 0);
        assertEquals(threePm, ((AddCommand) dateTimeKeywordOn).getTask().getStartTime());
        assertEquals("#assign", ((AddCommand) dateTimeKeywordOn).getTask().getTagsList().get(0));
        assertEquals("#oh-my-god", ((AddCommand) dateTimeKeywordOn).getTask().getTagsList().get(1));
        assertEquals("#help-me!", ((AddCommand) dateTimeKeywordOn).getTask().getTagsList().get(2));

        //add ... from <date><time> to <date><time>
        CommandInterface dateTimeDateTime = new AddCommandParser().parse("add a lot of things from March 22 0:00:00 to May 3 8:00");
        assertEquals("a lot of things", ((AddCommand) dateTimeDateTime).getTask().getDescription());
        LocalDate mar22 = LocalDate.of(dateNow.getYear(), 3, 22);
        LocalDate may3 = LocalDate.of(dateNow.getYear(), 5, 3);
        assertEquals(mar22, ((AddCommand) dateTimeDateTime).getTask().getStartDate());
        assertEquals(may3, ((AddCommand) dateTimeDateTime).getTask().getEndDate());
        LocalTime zero = LocalTime.of(0, 0, 0);
        LocalTime eight = LocalTime.of(8, 0, 0);
        assertEquals(zero, ((AddCommand) dateTimeDateTime).getTask().getStartTime());
        assertEquals(eight, ((AddCommand) dateTimeDateTime).getTask().getEndTime());

        //add ... from <date><time> to <time>
        CommandInterface dateTimeToTime = new AddCommandParser().parse("add movie from May 9 13:00 to 15:00");
        LocalDate may9 = LocalDate.of(dateNow.getYear(), 5, 9);
        assertEquals(may9, ((AddCommand) dateTimeToTime).getTask().getStartDate());
        assertEquals(may9, ((AddCommand) dateTimeToTime).getTask().getEndDate());

        //add ... by <date><time>
        CommandInterface dateTimeKeywordBy = new AddCommandParser().parse("add manual V0.2 by next Sunday 23:59:59");
        LocalDate nextSunday = dateNow.plusDays(2 * 7 - dateNow.getDayOfWeek().getValue());
        assertEquals(nextSunday, ((AddCommand) dateTimeKeywordBy).getTask().getEndDate());
        LocalTime endOfDay = LocalTime.of(23, 59, 59);
        assertEquals(endOfDay, ((AddCommand) dateTimeKeywordBy).getTask().getEndTime());
        assertEquals(null, ((AddCommand) dateTimeKeywordBy).getTask().getStartDate());
        assertEquals(null, ((AddCommand) dateTimeKeywordBy).getTask().getStartTime());

        /* Only time */
        //add ... on <time>
        CommandInterface timeKeywordOn = new AddCommandParser().parse("add sth on 3pm");
        assertEquals(dateNow, ((AddCommand) timeKeywordOn).getTask().getStartDate());
        assertEquals(threePm, ((AddCommand) timeKeywordOn).getTask().getStartTime());


        //add ... from <time> to <time>
        CommandInterface timeToTimeKeywordFrom = new AddCommandParser().parse("add movie from 13:00 to 3pm");
        LocalTime onePm = LocalTime.of(13, 0, 0);
        assertEquals(dateNow, ((AddCommand) timeToTimeKeywordFrom).getTask().getStartDate());
        assertEquals(dateNow, ((AddCommand) timeToTimeKeywordFrom).getTask().getEndDate());
        assertEquals(onePm, ((AddCommand) timeToTimeKeywordFrom).getTask().getStartTime());
        assertEquals(threePm, ((AddCommand) timeToTimeKeywordFrom).getTask().getEndTime());

        //add ... by <time>
        CommandInterface timeKeywordBy = new AddCommandParser().parse("add sth by 11pm");
        assertEquals(dateNow, ((AddCommand) timeKeywordBy).getTask().getEndDate());
        LocalTime elevenPm = LocalTime.of(23, 0, 0);
        assertEquals(elevenPm, ((AddCommand) timeKeywordBy).getTask().getEndTime());

        /* No date or time */
        //add ...
        CommandInterface onlyDescription = new AddCommandParser().parse("add assignments 2");
        assertEquals("assignments 2", ((AddCommand) onlyDescription).getTask().getDescription());

        //add ... <without keyword> <date>/<time>
        CommandInterface manyKeywords = new AddCommandParser().parse("add on from by assignments 13:00 #3");
        assertEquals("on from by assignments 13:00", ((AddCommand) manyKeywords).getTask().getDescription());
        assertEquals("#3", ((AddCommand) manyKeywords).getTask().getTagsList().get(0));

        //add ... #... ... on <date> #...
        CommandInterface mixedTagNotation = new AddCommandParser().parse("add remove #task tag on today #remove ##");
        assertEquals("remove #task tag", ((AddCommand) mixedTagNotation).getTask().getDescription());
        assertEquals(dateNow, ((AddCommand) mixedTagNotation).getTask().getStartDate());
        assertEquals("#remove", ((AddCommand) mixedTagNotation).getTask().getTagsList().get(0));
        assertEquals("##", ((AddCommand) mixedTagNotation).getTask().getTagsList().get(1));

        /* wrong or invalid */
        CommandInterface wrongKeyword = new AddCommandParser().parse("add movie at Friday 13:00 to 15:00");
        assertEquals("movie at Friday 13:00 to 15:00", ((AddCommand) wrongKeyword).getTask().getDescription());

        CommandInterface unmatchedKeyword = new AddCommandParser().parse("add movie from Friday 13:00");
        assertEquals("movie from Friday 13:00", ((AddCommand) unmatchedKeyword).getTask().getDescription());

        CommandInterface wrongTime = new AddCommandParser().parse("add movie from Friday 23:61");
        assertEquals("movie from Friday 23:61", ((AddCommand) wrongTime).getTask().getDescription());

        CommandInterface invalidTag = new AddCommandParser().parse("add assignments 2 on today #tag #");
        assertTrue(invalidTag instanceof InvalidCommand);


        System.out.println();
    }

    @Test
    public void testViewCommandParser() throws Exception {
        ArrayList<Tag> tagArrayList = new ArrayList<>();

        //view <view_type>
        CommandInterface command1 = new ViewCommandParser().parse("view all", tagArrayList);
        assertEquals(ViewCommand.VIEW_TYPE.ALL, ((ViewCommand) command1).getViewType());

        CommandInterface command2 = new ViewCommandParser().parse("view all things", tagArrayList);//invalid
        assertTrue(command2 instanceof InvalidCommand);

        CommandInterface command3 = new ViewCommandParser().parse("view done", tagArrayList);
        assertEquals(ViewCommand.VIEW_TYPE.DONE, ((ViewCommand) command3).getViewType());

        CommandInterface command4 = new ViewCommandParser().parse("view undone", tagArrayList);
        assertEquals(ViewCommand.VIEW_TYPE.UNDONE, ((ViewCommand) command4).getViewType());

        CommandInterface command5 = new ViewCommandParser().parse("view overdue", tagArrayList);
        assertEquals(ViewCommand.VIEW_TYPE.OVERDUE, ((ViewCommand) command5).getViewType());

        CommandInterface command6 = new ViewCommandParser().parse("view all done", tagArrayList);//invalid
        assertTrue(command6 instanceof InvalidCommand);

        //view #tags
        tagArrayList.add(new Tag("#a", false));
        tagArrayList.add(new Tag("#b", false));
        tagArrayList.add(new Tag("#ahhh", false));
        tagArrayList.add(new Tag("#a", false));

        CommandInterface command7 = new ViewCommandParser().parse("view #a #b", tagArrayList);
        assertEquals("#a", ((ViewCommand) command7).getTask().getTagsList().get(0));
        assertEquals("#b", ((ViewCommand) command7).getTask().getTagsList().get(1));

        CommandInterface command8 = new ViewCommandParser().parse("view #ahhh boom", tagArrayList);//invalid
        assertTrue(command8 instanceof InvalidCommand);

        CommandInterface command9 = new ViewCommandParser().parse("view #a #c", tagArrayList);
        assertTrue(command9 instanceof InvalidCommand);
        assertEquals("Tag #c doesn't exist!", command9.getMessage());

        //view description
        CommandInterface command10 = new ViewCommandParser().parse("view description task 1", tagArrayList);
        assertEquals("task 1", ((ViewCommand) command10).getTask().getDescription());

        //view <category_type>
        CommandInterface command11 = new ViewCommandParser().parse("view task", tagArrayList);
        assertEquals("task", ((ViewCommand) command11).getTask().getDescription());

        CommandInterface command12 = new ViewCommandParser().parse("view tasks", tagArrayList);
        assertEquals(ViewCommand.CATEGORY_TYPE.TASKS, ((ViewCommand) command12)._categoryType);

        CommandInterface command13 = new ViewCommandParser().parse("view floating", tagArrayList);
        assertEquals("floating", ((ViewCommand) command13).getTask().getDescription());

        CommandInterface command14 = new ViewCommandParser().parse("view deadlines", tagArrayList);
        assertEquals(ViewCommand.CATEGORY_TYPE.DEADLINES, ((ViewCommand) command14)._categoryType);

        CommandInterface command15 = new ViewCommandParser().parse("view events", tagArrayList);
        assertEquals(ViewCommand.CATEGORY_TYPE.EVENTS, ((ViewCommand) command15)._categoryType);

        //view date/time
        LocalDate dateNow = LocalDate.now();
        CommandInterface command16 = new ViewCommandParser().parse("view start time 1pm", tagArrayList);
        LocalTime onePm = LocalTime.of(13, 0, 0);
        assertEquals(onePm, ((ViewCommand)command16).getTask().getStartTime());

        CommandInterface command17 = new ViewCommandParser().parse("view start time 13:00 1pm", tagArrayList);//invalid
        assertTrue(command17 instanceof InvalidCommand);

        CommandInterface command18 = new ViewCommandParser().parse("view end time 2:33", tagArrayList);
        LocalTime two33 = LocalTime.of(2, 33, 0);
        assertEquals(two33, ((ViewCommand)command18).getTask().getEndTime());

        CommandInterface command19 = new ViewCommandParser().parse("view start date Apr 19", tagArrayList);
        LocalDate apr19 = LocalDate.of(dateNow.getYear(), 4, 19);
        assertEquals(apr19, ((ViewCommand)command19).getTask().getStartDate());

        CommandInterface command20 = new ViewCommandParser().parse("view end date today", tagArrayList);
        assertEquals(dateNow, ((ViewCommand)command20).getTask().getEndDate());

        CommandInterface command21 = new ViewCommandParser().parse("view end date today 3pm", tagArrayList);//invalid
        assertTrue(command21 instanceof InvalidCommand);

        CommandInterface command22 = new ViewCommandParser().parse("view from May 4 to Aug 9", tagArrayList);
        LocalDate may4 = LocalDate.of(dateNow.getYear(), 5, 4);
        LocalDate aug9 = LocalDate.of(dateNow.getYear(), 8, 9);
        assertEquals(may4, ((ViewCommand)command22).getTask().getStartDate());
        assertEquals(aug9, ((ViewCommand)command22).getTask().getEndDate());
    }

    @Test
    public void testDeleteCommandParser() throws Exception {
        CommandInterface command1 = new DeleteCommandParser().parse("delete 1", 2);
        assertEquals(0, ((DeleteCommand)command1).getFirstIndex());

        CommandInterface command2 = new DeleteCommandParser().parse("delete 1343234", 100);//invalid
        assertTrue(command2 instanceof InvalidCommand);
        assertEquals("Delete index out of range!", command2.getMessage());

        CommandInterface command3 = new DeleteCommandParser().parse("delete gg", 100);//invalid
        assertTrue(command3 instanceof InvalidCommand);
        assertEquals("Invalid: ID not found", command3.getMessage());

        CommandInterface command4 = new DeleteCommandParser().parse("delete 2 to 40", 100);
        assertEquals(1, ((DeleteCommand)command4).getFirstIndex());
        assertEquals(39, ((DeleteCommand)command4).getLastIndex());

        CommandInterface command5 = new DeleteCommandParser().parse("delete 1 to 400", 100);//invalid
        assertTrue(command5 instanceof InvalidCommand);
        assertEquals("Delete index out of range!", command5.getMessage());
    }

    @Test
    public void testEditCommandParser() throws Exception {

        //edit <id> description ...
        CommandInterface command1 = new EditCommandParser().parse("edit 3 description lalala", 10);
        assertEquals("lalala", ((EditCommand)command1).getTask().getDescription());

        CommandInterface command2 = new EditCommandParser().parse("edit 8 description", 10);
        assertTrue(command2 instanceof InvalidCommand);
        assertEquals("Argument Missing", command2.getMessage());

        CommandInterface command3 = new EditCommandParser().parse("edit 43 description bbb", 10);//invalid
        assertTrue(command3 instanceof InvalidCommand);
        assertEquals("Index out of range", command3.getMessage());

        //edit <id> start ...
        CommandInterface command4 = new EditCommandParser().parse("edit 1 start May 5", 10);
        LocalDate dateNow = LocalDate.now();
        LocalDate may5 = LocalDate.of(dateNow.getYear(), 5, 5);
        assertEquals(may5, ((EditCommand)command4).getTask().getStartDate());

        CommandInterface command5 = new EditCommandParser().parse("edit -1 start this friday 3pm", 10);//invalid
        assertTrue(command5 instanceof InvalidCommand);
        assertEquals("Index out of range", command5.getMessage());

        CommandInterface command6 = new EditCommandParser().parse("edit 3 start time 3pm", 10);
        assertTrue(command6 instanceof InvalidCommand);
        assertEquals("Invalid start date or time", command6.getMessage());

        CommandInterface command7 = new EditCommandParser().parse("edit 3 start 23:23:23", 10);//invalid
        LocalTime command7Time = LocalTime.of(23, 23, 23);
        assertEquals(command7Time, ((EditCommand)command7).getTask().getStartTime());

        //edit <id> end ...
        CommandInterface command9 = new EditCommandParser().parse("edit 5 end tomorrow 1pm", 10);
        assertEquals(dateNow.plusDays(1), ((EditCommand) command9).getTask().getEndDate());
        LocalTime onePm = LocalTime.of(13, 0, 0);
        assertEquals(onePm, ((EditCommand)command9).getTask().getEndTime());

        CommandInterface command10 = new EditCommandParser().parse("edit 3 end 3pm", 10);
//        assertEquals(dateNow, ((EditCommand)command10).getTask().getEndDate());
        LocalTime threePm = LocalTime.of(15, 0, 0);
        assertEquals(threePm, ((EditCommand)command10).getTask().getEndTime());

        //edit <id> #tags
        CommandInterface command11 = new EditCommandParser().parse("edit 3 #a #assignments #233", 10);
        assertEquals("#a", ((EditCommand)command11).getTask().getTagsList().get(0));
        assertEquals("#assignments", ((EditCommand)command11).getTask().getTagsList().get(1));
        assertEquals("#233", ((EditCommand)command11).getTask().getTagsList().get(2));

        CommandInterface command12 = new EditCommandParser().parse("edit 3 #123 fdsf", 10);//invalid
        assertTrue(command12 instanceof InvalidCommand);

        System.out.println();
    }


    @Test
    public void testSetCommandParser() throws Exception {
        CommandInterface command1 = new SetCommandParser().parse("set filepath /usr/bin/storage");
        assertEquals("/usr/bin/storage.txt", ((SetCommand)command1).getFilePath());

        CommandInterface command2 = new SetCommandParser().parse("set filepath default");
        assertEquals(null, ((SetCommand)command2).getFilePath());

        CommandInterface command3 = new SetCommandParser().parse("set filepath default/bin/a");
        assertEquals("default/bin/a.txt", ((SetCommand)command3).getFilePath());

        CommandInterface command4 = new SetCommandParser().parse("set filepath path/to/file");
        assertEquals("path/to/file.txt", ((SetCommand)command4).getFilePath());

        CommandInterface command5 = new SetCommandParser().parse("set filep");
        assertTrue(command5 instanceof InvalidCommand);
    }

    @Test
    public void testTimeParser() throws Exception {
        TimeParserResult timeParserResult1 = new TimeParser().parseTime("1pm to 2pm");
        TimeParserResult timeParserResult2 = new TimeParser().parseTime("1pm to Dec 1");
        TimeParserResult timeParserResult3 = new TimeParser().parseTime("1pm to Dec 1 2pm");
        TimeParserResult timeParserResult4 = new TimeParser().parseTime("Dec 1 to 2pm");
        TimeParserResult timeParserResult5 = new TimeParser().parseTime("Dec 1 to Dec 2 2pm");
        TimeParserResult timeParserResult6 = new TimeParser().parseTime("Dec 1 1pm to 2pm");
        TimeParserResult timeParserResult7 = new TimeParser().parseTime("Dec 1 1pm to Dec 2");
    }
}