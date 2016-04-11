package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.prompt.*;

/**
 * @@author A0149647N
 * Run all prompt tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CommandPromptTest.class, AddPromptTest.class, EditPromptTest.class, IdOnlyPromptTest.class, 
                     SetPromptTest.class, ViewPromptTest.class})
public class PromptTest {

}
