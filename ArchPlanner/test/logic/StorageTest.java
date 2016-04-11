package test.logic;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.Test;

import logic.Task;
import storage.Storage;

public class StorageTest {
	
    private Storage storageTest;

    @Test
    public void testReadWriteFile() throws Exception {
    	
    	storageTest = new Storage();
    	
    	ArrayList<Task> list = new ArrayList<Task>();
    	
    	ArrayList<String> tags = new ArrayList<String>();
		tags.add("#project");
		
		LocalDate startDate = LocalDate.of(2016, 1, 1);
		LocalTime startTime = LocalTime.of(10, 00);
		LocalDate endDate = LocalDate.of(2016, 2, 2);
		LocalTime endTime = LocalTime.of(11, 00);
		Task task = new Task("transfer task to harddisk", tags, startDate, startTime, endDate, endTime);
		
		list.add(task);
    	
    	storageTest.writeStorageFile(list);
    	
    	storageTest = new Storage();
    	storageTest.loadStorageFile();

    	ArrayList<Task> retrievedList = storageTest.getMasterList();
    	assertEquals(1, retrievedList.size());
    	
    	Task retrievedTask = storageTest.getMasterList().get(0);
    	
    	assertEquals("transfer task to harddisk", retrievedTask.getDescription());
		assertEquals("01 Jan 2016", retrievedTask.getStartDateString());
		assertEquals("10:00AM", retrievedTask.getStartTimeString());
		assertEquals("02 Feb 2016", retrievedTask.getEndDateString());
		assertEquals("11:00AM", retrievedTask.getEndTimeString());
		assertEquals(false , retrievedTask.getIsDone());
		assertEquals("#project", retrievedTask.getTagsList().get(0));
    }
}
