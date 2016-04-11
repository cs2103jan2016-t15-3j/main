package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.logic.LogicAddCommandTest;
import test.logic.LogicDeleteCommandTest;
import test.logic.LogicDoneCommandTest;
import test.logic.LogicEditCommandTest;

/**
 * @@author A0149647N
 * Run all logic tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({LogicAddCommandTest.class, LogicDeleteCommandTest.class, LogicDoneCommandTest.class, LogicEditCommandTest.class})
public class LogicTest {
}

