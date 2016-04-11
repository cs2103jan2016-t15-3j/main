package test.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import logic.Logic;
import logic.TaskParameters;
import logic.commands.AddCommand;
import logic.commands.CommandInterface;
import logic.commands.DeleteCommand;

/**
 * This class is used to conduct unit testing on logic component for delete command.
 * 
 * @@author A0140021J
 *
 */
public class LogicDeleteCommandTest {

	 private Logic logic;

	    private CommandInterface deleteCommandTest;


	    @Before
	    public void setUp() throws Exception {
	        logic = new Logic();
	        tearDown();
	    }

	    @After
	    public void tearDown() throws Exception {
	        logic.getMainList().clear();
	        logic.getViewList().clear();
	    }

	    @Test
	    public void testDeleteOneTask() {
	    	
	    	addOneTask();
	    	assertEquals(1, logic.getViewList().size());
	    	
	    	deleteCommandTest = new DeleteCommand(1);
	        logic.logicTestEnvironment(deleteCommandTest);

	        assertEquals(0, logic.getViewList().size());
	    }
	    
	    @Test
	    public void testDeleteMultipleTask() {
	    	
	    	addOneTask();
	    	addOneTask();
	    	addOneTask();
	    	assertEquals(3, logic.getViewList().size());
	    	
	    	deleteCommandTest = new DeleteCommand(1, 3);
	        logic.logicTestEnvironment(deleteCommandTest);

	        assertEquals(0, logic.getViewList().size());
	    }
	    
	    public void addOneTask() {
	    	
	    	TaskParameters task = new TaskParameters("transfer task to harddisk", new ArrayList<String>(), null, null, null, null);
	    	AddCommand addCommand = new AddCommand(task);
	        
	        logic.logicTestEnvironment(addCommand);
	    }
}
