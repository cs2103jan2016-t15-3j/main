package test;

import logic.Tag;
import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand;
import org.junit.Before;
import org.junit.Test;
import parser.ViewCommandParser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lifengshuang on 4/11/16.
 */
public class ViewCommandParserTest {
    ArrayList<Tag> tagArrayList;

    /**
     * Initialize tagList
     */
    @Before
    public void setUp() throws Exception {
        tagArrayList = new ArrayList<>();
        tagArrayList.add(new Tag("#a", false));
        tagArrayList.add(new Tag("#b", false));
        tagArrayList.add(new Tag("#ahhh", false));
        tagArrayList.add(new Tag("#a", false));

    }

    /**
     * Test all view type like "view all"
     */
    @Test
    public void testViewType() throws Exception {
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

    }

    /**
     * Test view tags
     */
    @Test
    public void testTag() throws Exception {
        //view #tags
        tagArrayList.add(new Tag("#a", false));
        tagArrayList.add(new Tag("#b", false));
        tagArrayList.add(new Tag("#ahhh", false));
        tagArrayList.add(new Tag("#a", false));

        CommandInterface command7 = new ViewCommandParser().parse("view #a #b", tagArrayList);
        assertEquals("#a", ((ViewCommand) command7).getTaskParameters().getTagsList().get(0));
        assertEquals("#b", ((ViewCommand) command7).getTaskParameters().getTagsList().get(1));

        CommandInterface command8 = new ViewCommandParser().parse("view #ahhh boom", tagArrayList);//invalid
        assertTrue(command8 instanceof InvalidCommand);

        CommandInterface command9 = new ViewCommandParser().parse("view #a #c", tagArrayList);
        assertTrue(command9 instanceof InvalidCommand);
        assertEquals("Tag #c doesn't exist!", command9.getMessage());

    }

    /**
     * Test view description
     */
    @Test
    public void testDescription() throws Exception {
        //view description
        CommandInterface command10 = new ViewCommandParser().parse("view description task 1", tagArrayList);
        assertEquals("task 1", ((ViewCommand) command10).getTaskParameters().getDescription());

    }

    /**
     * Test all category type like "view tasks"
     */
    @Test
    public void testCategoryType() throws Exception {
        //view <category_type>
        CommandInterface command11 = new ViewCommandParser().parse("view task", tagArrayList);
        assertEquals("task", ((ViewCommand) command11).getTaskParameters().getDescription());

        CommandInterface command12 = new ViewCommandParser().parse("view tasks", tagArrayList);
        assertEquals(ViewCommand.CATEGORY_TYPE.TASKS, ((ViewCommand) command12)._categoryType);

        CommandInterface command13 = new ViewCommandParser().parse("view floating", tagArrayList);
        assertEquals("floating", ((ViewCommand) command13).getTaskParameters().getDescription());

        CommandInterface command14 = new ViewCommandParser().parse("view deadlines", tagArrayList);
        assertEquals(ViewCommand.CATEGORY_TYPE.DEADLINES, ((ViewCommand) command14)._categoryType);

        CommandInterface command15 = new ViewCommandParser().parse("view events", tagArrayList);
        assertEquals(ViewCommand.CATEGORY_TYPE.EVENTS, ((ViewCommand) command15)._categoryType);

    }

    /**
     * Test viewing date and time.
     * Including start date, start time, end date and end time.
     */
    @Test
    public void testDateAndTime() throws Exception {
        //view date/time
        LocalDate dateNow = LocalDate.now();
        CommandInterface command16 = new ViewCommandParser().parse("view start time 1pm", tagArrayList);
        LocalTime onePm = LocalTime.of(13, 0, 0);
        assertEquals(onePm, ((ViewCommand)command16).getTaskParameters().getStartTime());

        CommandInterface command17 = new ViewCommandParser().parse("view start time 13:00 1pm", tagArrayList);//invalid
        assertTrue(command17 instanceof InvalidCommand);

        CommandInterface command18 = new ViewCommandParser().parse("view end time 2:33", tagArrayList);
        LocalTime two33 = LocalTime.of(2, 33, 0);
        assertEquals(two33, ((ViewCommand)command18).getTaskParameters().getEndTime());

        CommandInterface command19 = new ViewCommandParser().parse("view start date Apr 19", tagArrayList);
        LocalDate apr19 = LocalDate.of(dateNow.getYear(), 4, 19);
        assertEquals(apr19, ((ViewCommand)command19).getTaskParameters().getStartDate());

        CommandInterface command20 = new ViewCommandParser().parse("view end date today", tagArrayList);
        assertEquals(dateNow, ((ViewCommand)command20).getTaskParameters().getEndDate());

        CommandInterface command21 = new ViewCommandParser().parse("view end date today 3pm", tagArrayList);//invalid
        assertTrue(command21 instanceof InvalidCommand);

        CommandInterface command22 = new ViewCommandParser().parse("view from May 4 to Aug 9", tagArrayList);
        LocalDate may4 = LocalDate.of(dateNow.getYear(), 5, 4);
        LocalDate aug9 = LocalDate.of(dateNow.getYear(), 8, 9);
        assertEquals(may4, ((ViewCommand)command22).getTaskParameters().getStartDate());
        assertEquals(aug9, ((ViewCommand)command22).getTaskParameters().getEndDate());
    }
}
