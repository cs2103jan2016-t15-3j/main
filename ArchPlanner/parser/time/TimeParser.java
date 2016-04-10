package parser.time;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import org.antlr.runtime.tree.Tree;

import java.util.Date;
import java.util.List;

/**
 * @@author A0149647N
 * TimeParser parse time with Natty and return a TimeParserResult object.
 */
public class TimeParser {

    private static final String NATTY_DATE_TIME = "DATE_TIME";
    private static final String NATTY_RELATIVE_DATE = "RELATIVE_DATE";
    private static final String NATTY_EXPLICIT_DATE = "EXPLICIT_DATE";
    private static final String NATTY_RELATIVE_TIME = "RELATIVE_TIME";
    private static final String NATTY_EXPLICIT_TIME = "EXPLICIT_TIME";
    private static final int NO_DATE_TIME = 0;
    private static final int ONE_DATE_TIME = 1;
    private static final int TWO_DATE_TIME = 2;
    private static final int FIRST_DATE_INDEX = 0;
    private static final int SECOND_DATE_INDEX = 1;


    private static Parser timeParser = new Parser();
    private int dateTimeCount;
    private TimeParserResult timeParserResult = new TimeParserResult();

    /**
     * Parse time string with Natty and return a TimeParserResult object
     */
    public TimeParserResult parseTime(String input) {

        if (input == null) {
            return timeParserResult;
        }
        List<DateGroup> groups = timeParser.parse(input);
        for (DateGroup group : groups){
            if (dateTimeCount > NO_DATE_TIME) {
                break;
            }
            List<Date> dates = group.getDates();
            Tree tree = group.getSyntaxTree();
            postTraverseSyntaxTree(tree, dates);
            timeParserResult.setMatchString(group.getText());
        }
        timeParserResult.updateDateTime();
        timeParserResult.checkInvalidTimeRange();
        return timeParserResult;
    }

    private void postTraverseSyntaxTree(Tree tree, List<Date> dates) {
        if (tree.getText().equals(NATTY_DATE_TIME)){
            dateTimeCount++;
        }
        if (tree.getText().equals(NATTY_RELATIVE_DATE) || tree.getText().equals(NATTY_EXPLICIT_DATE)) {
            if (dateTimeCount == ONE_DATE_TIME) {
                timeParserResult.setFirstDate(dates.get(FIRST_DATE_INDEX));
            } else if (dateTimeCount == TWO_DATE_TIME) {
                timeParserResult.setSecondDate(dates.get(SECOND_DATE_INDEX));
            }
        } else if (tree.getText().equals(NATTY_RELATIVE_TIME) || tree.getText().equals(NATTY_EXPLICIT_TIME)) {
            if (dateTimeCount == ONE_DATE_TIME) {
                timeParserResult.setFirstTime(dates.get(FIRST_DATE_INDEX));
            } else if (dateTimeCount == TWO_DATE_TIME) {
                timeParserResult.setSecondTime(dates.get(SECOND_DATE_INDEX));
            }
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            postTraverseSyntaxTree(tree.getChild(i), dates);
        }
    }
}
