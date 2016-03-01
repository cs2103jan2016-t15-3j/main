package logic;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCreation;

import org.junit.Test;

public class LogicTest {


	public ArrayList<Task> _masterList;


	@Test
	public void test() throws ClassNotFoundException, IOException, ParseException {

		Logic logic = new Logic();
		
		_masterList = logic.storage.getTasksList();
		Date startDateTime1 = new Date();
		Date endDateTime1 = new Date();
		Date startDateTime2 = new Date();
		Date endDateTime2 = new Date();
		Date startDateTime3 = new Date();
		Date endDateTime3 = new Date();
		configureDate(startDateTime1, 1, 1, 1, 1, 2001);
		configureDate(endDateTime1, 2, 2, 2, 2, 2002);
		configureDate(startDateTime2, 3, 3, 3, 3, 2003);
		configureDate(endDateTime2, 4, 4, 4, 4, 2004);
		configureDate(startDateTime3, 5, 5, 5, 5, 2005);
		configureDate(endDateTime3, 6, 6, 6, 6, 2006);
		logic.addTask("task 1", "tag 1", startDateTime1, endDateTime1);
		logic.addTask("task 2", "tag 2", startDateTime2, endDateTime2);
		logic.addTask("task 3", "tag 3", startDateTime3, endDateTime3);
		_masterList = logic.storage.getTasksList();
		assertEquals("task 1", _masterList.get(0).getDescription().toString());
		assertEquals("task 2", _masterList.get(1).getDescription().toString());
		assertEquals("task 3", _masterList.get(2).getDescription().toString());
		assertEquals("tag 1", _masterList.get(0).getTag().toString());
		assertEquals("tag 2", _masterList.get(1).getTag().toString());
		assertEquals("tag 3", _masterList.get(2).getTag().toString());
		assertEquals("1/1/2001", _masterList.get(0).getStartDate());
		assertEquals("2/2/2002", _masterList.get(0).getEndDate());
		assertEquals("3/3/2003", _masterList.get(1).getStartDate());
		assertEquals("4/4/2004", _masterList.get(1).getEndDate());
		assertEquals("5/5/2005", _masterList.get(2).getStartDate());
		assertEquals("6/6/2006", _masterList.get(2).getEndDate());
		assertEquals("tag 1", logic.getTagsList().get(0));
		assertEquals("tag 2", logic.getTagsList().get(1));
		assertEquals("tag 3",logic.getTagsList().get(2));
		
		assertEquals("task 1", logic.getViewList().get(0).getDescription().toString());
		assertEquals("task 2",  logic.getViewList().get(1).getDescription().toString());
		assertEquals("task 3",  logic.getViewList().get(2).getDescription().toString());
		assertEquals("tag 1", logic.getViewList().get(0).getTag().toString());
		assertEquals("tag 2",  logic.getViewList().get(1).getTag().toString());
		assertEquals("tag 3",  logic.getViewList().get(2).getTag().toString());
		
		logic.deleteTask(2);
	}
	
	public Date configureDate(Date dateTime, int hours, int minutes, int date, int month, int year) {
		dateTime.setHours(hours);
		dateTime.setMinutes(minutes);
		dateTime.setDate(date);
		dateTime.setMonth(month);
		dateTime.setYear(year);
		return dateTime;
	}

}
