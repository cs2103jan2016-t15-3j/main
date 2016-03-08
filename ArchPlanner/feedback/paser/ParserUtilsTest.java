package feedback.paser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lifengshuang on 3/8/16.
 */
public class ParserUtilsTest {

    @Test
    public void testSplit() throws Exception  {
        String s1 = "a b c";
        String s2 = "a   b c";
        String s3 = " a b  c  ";
        String[] r = new String[]{"a", "b", "c"};
        assertEquals(r, ParserUtils.split(s1));
        assertEquals(r, ParserUtils.split(s1));
        assertEquals(r, ParserUtils.split(s1));
    }

    @Test
    public void testMerge() throws Exception {

    }

    @Test
    public void testRetrieveTag() throws Exception {
        String s1 = "fasdfdsaf ref e f #233";
        String s2 = "fdsfd#";
        String s3 = "ff df #re #sdf";
        CommandParser p1 = new CommandParser(s1);
        CommandParser p2 = new CommandParser(s2);
        CommandParser p3 = new CommandParser(s3);
        assertEquals("233", ParserUtils.retrieveTag(p1));
        assertEquals("", ParserUtils.retrieveTag(p2));
        assertEquals("re #sdf", ParserUtils.retrieveTag(p3));

        assertEquals("fasdfdsaf ref e f ", p1.getInput());
        assertEquals("fdsfd", p2.getInput());
        assertEquals("ff df ", p3.getInput());
    }
}