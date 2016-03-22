package feedback.paser;

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
        Command command1 = new AddCommandParser().parse("add sth on tomorrow");

        //add ... from <date> to <date>
        Command command2 = new AddCommandParser().parse("add lalala from tomorrow to this Friday #a");

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

        /* INVALID */
        Command command14 = new AddCommandParser().parse("add movie from Friday from 13:00 to 15:00");
        Command command15 = new AddCommandParser().parse("add movie at Friday 13:00 to 15:00");
        Command command16 = new AddCommandParser().parse("add movie from Friday 13:00");
        Command command17 = new AddCommandParser().parse("add movie from Friday 23:61");
        //try more


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
        Command command1 = new EditCommandParser().parse("edit 3 lalala");
        Command command2 = new EditCommandParser().parse("edit start time ...");//invalid
        Command command3 = new EditCommandParser().parse("edit 2 tag new 1 2 3");
        Command command4 = new EditCommandParser().parse("edit 3 start time this friday 3pm");
        Command command5 = new EditCommandParser().parse("edit 233 end time this friday 3pm to 5pm");//invalid
        Command command6 = new EditCommandParser().parse("edit 233 sth");
        Command command7 = new EditCommandParser().parse("edit 233 start time 1pm lalala");//invalid
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