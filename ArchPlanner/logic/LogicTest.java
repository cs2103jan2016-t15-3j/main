package logic;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import logic.commands.AddCommand;
import logic.commands.Command;
import logic.commands.DeleteCommand;
import logic.commands.EditCommand;

public class LogicTest {

/*
	public ArrayList<Task> _masterList = new ArrayList<Task>();


	@Test
	public void test() throws ClassNotFoundException, IOException, ParseException {

		Logic logic = new Logic();

		Calendar startDateTime1 = Calendar.getInstance();
		Calendar endDateTime1 = Calendar.getInstance();
		Calendar startDateTime2 = Calendar.getInstance();
		Calendar endDateTime2 = Calendar.getInstance();
		Calendar startDateTime3 = Calendar.getInstance();
		Calendar endDateTime3 = Calendar.getInstance();

		startDateTime1 = configureDateTime(2001, 1, 1, 1, 1);
		endDateTime1 = configureDateTime(2002, 2, 2, 2, 2);
		startDateTime2 = configureDateTime(2003, 3, 3, 3, 3);
		endDateTime2 = configureDateTime(2004, 4, 4, 4, 4);
		startDateTime3 = configureDateTime(2005, 5, 5, 5, 5);
		endDateTime3 = configureDateTime(2006, 6, 6, 6, 6);

		AddCommand addCommand1 = new AddCommand("task 1", "tag 1", startDateTime1, endDateTime1);
		AddCommand addCommand2 = new AddCommand("task 2", "tag 2", startDateTime2, endDateTime2);
		AddCommand addCommand3 = new AddCommand("task 3", "tag 3", startDateTime3, endDateTime3);

		Command commandObj1 = (Command) addCommand1;
		Command commandObj2 = (Command) addCommand2;
		Command commandObj3 = (Command) addCommand3;

		logic.executeCommand(commandObj1);
		logic.executeCommand(commandObj2);
		logic.executeCommand(commandObj3);

		Storage storage = new Storage();
		storage.loadStorageFile();
		
		assertEquals("task 1", logic.getViewList().get(0).getDescription().toString());
		assertEquals("task 2",logic.getViewList().get(1).getDescription().toString());
		assertEquals("task 3", logic.getViewList().get(2).getDescription().toString());
		assertEquals("tag 1", logic.getViewList().get(0).getTag().toString());
		assertEquals("tag 2", logic.getViewList().get(1).getTag().toString());
		assertEquals("tag 3", logic.getViewList().get(2).getTag().toString());
		assertEquals("01/01/2001", logic.getViewList().get(0).getStartDate());
		assertEquals("01:01", logic.getViewList().get(0).getStartTime());
		assertEquals("02/02/2002", logic.getViewList().get(0).getEndDate());
		assertEquals("02:02", logic.getViewList().get(0).getEndTime());
		assertEquals("03/03/2003", logic.getViewList().get(1).getStartDate());
		assertEquals("03:03", logic.getViewList().get(1).getStartTime());
		assertEquals("04/04/2004", logic.getViewList().get(1).getEndDate());
		assertEquals("04:04", logic.getViewList().get(1).getEndTime());
		assertEquals("05/05/2005", logic.getViewList().get(2).getStartDate());
		assertEquals("05:05", logic.getViewList().get(2).getStartTime());
		assertEquals("06/06/2006", logic.getViewList().get(2).getEndDate());
		assertEquals("06:06", logic.getViewList().get(2).getEndTime());
		assertEquals("tag 1", logic.getViewList().get(0).getTag());
		assertEquals("tag 2",logic.getViewList().get(1).getTag());
		assertEquals("tag 3", logic.getViewList().get(2).getTag());

		DeleteCommand command4 = new DeleteCommand(2);
		Command commandObj4 = (Command) command4;
		logic.executeCommand(commandObj4);
		
		assertEquals("task 1",logic.getViewList().get(0).getDescription());
		assertEquals("task 3", logic.getViewList().get(1).getDescription());
		
		EditCommand command5 = new EditCommand(2, "task two", "tag two", startDateTime2, endDateTime2);
		Command commandObj5 = (Command) command5;
		logic.executeEditCommand(commandObj5);
		
		assertEquals("task two", logic.getViewList().get(1).getDescription());
	}

	public Calendar configureDateTime(int year, int month, int day, int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute);
		return cal;
	}
*/
}
