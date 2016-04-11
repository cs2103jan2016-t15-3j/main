package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.parser.*;

/**
 * @@author A0149647N
 * Run all parser tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({AddCommandParserTest.class, EditCommandParserTest.class, DeleteCommandParserTest.class,
                     DoneCommandParserTest.class, SetCommandParserTest.class, UndoneCommandParserTest.class, 
                     ViewCommandParserTest.class})
public class ParserTest {

}

