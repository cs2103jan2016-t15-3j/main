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
        //add sth from 1pm to 3pm
        //add lalala from tomorrow to this Friday #a
        //add miao by Mar 7 #a #b
        //add assignments this Thursday 3pm #assign #oh-my-god #help-me!
        Command command1 = new AddCommandParser().parse("add sth from 1pm to 3pm");
        Command command2 = new AddCommandParser().parse("add lalala from tomorrow to this Friday #a");
        Command command3 = new AddCommandParser().parse("add miao by Mar 7 #a #b");
        Command command4 = new AddCommandParser().parse("add assignments this Thursday 3pm #assign #oh-my-god #help-me!");
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
        Command command3 = new EditCommandParser().parse("edit 2 tag new");
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