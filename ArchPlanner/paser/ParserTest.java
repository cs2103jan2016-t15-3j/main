package paser;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import logic.commands.Command;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by lifengshuang on 3/8/16.
 */
public class ParserTest {
    @Test
    public void testAddCommandParser() throws Exception {

        /* Only date */
        //add ... on <date>
        Command command1 = new AddCommandParser().parse("add sth123 on tomorrow");

        //add ... from <date> to <date>
        Command command2 = new AddCommandParser().parse("add lalala from today to tomorrow #a");

        //add ... by <date>
        Command command3 = new AddCommandParser().parse("add miao by Mar 7 #a #b");

        /* Date and time */
        //add ... on <date> <time>
        Command command4 = new AddCommandParser().parse("add assignments on this Thursday 3pm #assign #oh-my-god #help-me!");

        //add ... from <date><time> to <date><time>
        Command command5 = new AddCommandParser().parse("add a lot of things from March 22 0:00:00 to May 3 8:00");

        //add ... from <date><time> to <time>
        Command command6 = new AddCommandParser().parse("add movie from Friday 13:00 to 15:00");

        //add ... by <date><time>
        Command command7 = new AddCommandParser().parse("add manual V2.0 by next Sunday 23:59:59");

        /* Only time */
        //add ... on <time>
        Command command8 = new AddCommandParser().parse("add sth on 3pm");

        //add ... from <time> to <time>
        Command command9 = new AddCommandParser().parse("add movie from 13:00 to 3pm");

        //add ... by <time>
        Command command10 = new AddCommandParser().parse("add sth by 11pm");

        /* No date or time */
        //add ...
        Command command11 = new AddCommandParser().parse("add assignments 2");

        //add ... <without keyword> <date>/<time>
        Command command12 = new AddCommandParser().parse("add assignments today #3");
        Command command13 = new AddCommandParser().parse("add on from by assignments 13:00 #3");

        //add ... #... ... on <date> #...
        Command command14 = new AddCommandParser().parse("add remove #task tag on today #remove ##");

        /* INVALID */
        Command command01 = new AddCommandParser().parse("add movie from Friday from 13:00 to 15:00");
        Command command02 = new AddCommandParser().parse("add movie at Friday 13:00 to 15:00");
        Command command03 = new AddCommandParser().parse("add movie from Friday 13:00");
        Command command04 = new AddCommandParser().parse("add movie from Friday 23:61");
        Command command05 = new AddCommandParser().parse("add assignments 2 on today #tag #");
        //try more


        System.out.println();
    }

    @Test
    public void testViewCommandParser() throws Exception {
        //view <view_type>
        Command command1 = new ViewCommandParser().parse("view all");
        Command command2 = new ViewCommandParser().parse("view all things");//invalid
        Command command3 = new ViewCommandParser().parse("view done");
        Command command4 = new ViewCommandParser().parse("view undone");
        Command command5 = new ViewCommandParser().parse("view overdue");
        Command command6 = new ViewCommandParser().parse("view all done");//invalid
        //view #tags
        Command command7 = new ViewCommandParser().parse("view #a #b");
        Command command8 = new ViewCommandParser().parse("view #ahhh boom");//invalid
        Command command9 = new ViewCommandParser().parse("view #a");
        //view description
        Command command10 = new ViewCommandParser().parse("view description task 1");
        //view <category_type>
        Command command11 = new ViewCommandParser().parse("view task");
        Command command12 = new ViewCommandParser().parse("view tasks");//invalid
        Command command13 = new ViewCommandParser().parse("view floating");
        Command command14 = new ViewCommandParser().parse("view deadline");
        Command command15 = new ViewCommandParser().parse("view event");
        //view date/time
        Command command16 = new ViewCommandParser().parse("view start time 1pm");
        Command command17 = new ViewCommandParser().parse("view start time 13:00 1pm");//invalid
        Command command18 = new ViewCommandParser().parse("view end time 2:33");
        Command command19 = new ViewCommandParser().parse("view start date Apr 19");
        Command command20 = new ViewCommandParser().parse("view end date today");
        Command command21 = new ViewCommandParser().parse("view end date today 3pm");//invalid
        Command command22 = new ViewCommandParser().parse("view from May 4 to Aug 9");
        System.out.println();
    }

    @Test
    public void testDeleteCommandParser() throws Exception {
        Command command1 = new DeleteCommandParser().parse("delete 1");
        Command command2 = new DeleteCommandParser().parse("delete 1343234");
        Command command3 = new DeleteCommandParser().parse("delete 1 23");//invalid
        Command command4 = new DeleteCommandParser().parse("delete 0.123");//invalid
        System.out.println();
    }

    @Test
    public void testEditCommandParser() throws Exception {

        //edit <id> description ...
        Command command1 = new EditCommandParser().parse("edit 3 description lalala");
        Command command2 = new EditCommandParser().parse("edit 13 description");//invalid
        Command command3 = new EditCommandParser().parse("edit 43 lalala");//invalid
        //edit <id> start date ...
        Command command4 = new EditCommandParser().parse("edit 1 start date this friday");
        Command command5 = new EditCommandParser().parse("edit -1 start date this friday 3pm");//invalid
        //edit <id> end date ...
        Command command6 = new EditCommandParser().parse("edit 555 end date tomorrow");
        //edit <id> start time ...
        Command command7 = new EditCommandParser().parse("edit 3 start time 3pm");
        Command command8 = new EditCommandParser().parse("edit -3 start time 23:23:23");
        Command command9 = new EditCommandParser().parse("edit 3 start time tomorrow 23:23:23");//invalid
        //edit <id> end time ...
        Command command10 = new EditCommandParser().parse("edit 3 end time 3pm");
        //edit <id> #tags
        Command command11 = new EditCommandParser().parse("edit 3 #a #assignments #233");
        Command command12 = new EditCommandParser().parse("edit 3 #123 fdsf");//invalid


        System.out.println();
    }

    @Test
    public void testCalendar() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        Calendar calendar = Calendar.getInstance();
        Date result;
        try {
            result = dateFormat.parse("7");
            calendar.setTime(result);
        } catch (ParseException e) {
            System.out.println("Parse Failed");
        }
//        assertEquals("5.4", calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DATE));
        assertEquals("1994", calendar.get(Calendar.MONTH) + "");
    }

    @Test
    public void testNetty() throws Exception {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse("add sth this thursday");
        for (DateGroup group : groups) {
            List<Date> dates = group.getDates();
            int line = group.getLine();
            int column = group.getPosition();
            String matchingValue = group.getText();
            String syntaxTree = group.getSyntaxTree().toStringTree();
            Map parseMap = group.getParseLocations();
            boolean isRecurreing = group.isRecurring();
            Date recursUntil = group.getRecursUntil();
            for (Date date : dates) {
                System.out.println(date.getMonth() + " " + date.getDay() + " " + date.getHours() + " " + date.getMinutes());
            }
            System.out.println("column: " + column);
            System.out.println("matching value: " + matchingValue);
            System.out.println("tree: " + syntaxTree);
        }

    }
}