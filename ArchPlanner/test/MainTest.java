package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @@author A0149647N
 * Run all tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ParserTest.class, LogicTest.class, PromptTest.class, StorageTest.class})
public class MainTest {
}
