package parser.time;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import org.antlr.runtime.tree.Tree;

import java.util.Date;
import java.util.List;

/**
 * Created by lifengshuang on 3/22/16.
 */
//getCase(1,0,1,1,1,0,0,0,1,1,0)
public class TimeParser {

    private static Parser timeParser = new Parser();
    private int dateCount, timeCount, dateTimeCount;
    private TimeParserResult timeParserResult = new TimeParserResult();

    public TimeParserResult parseTime(String input) {

        if (input == null) {
            return timeParserResult;
        }
        List<DateGroup> groups = timeParser.parse(input);
        for (DateGroup group : groups){
            if (group.getText().length() < 3){
                continue;
            }
            List<Date> dates = group.getDates();
            Tree tree = group.getSyntaxTree();
            postTraverseSyntaxTree(tree, dates);
            if (dates.size() < dateCount || dates.size() < timeCount) {
                return timeParserResult;
            }
            timeParserResult.setMatchPosition(group.getPosition());
            timeParserResult.setMatchString(group.getText());

        }
        timeParserResult.updateDateTime();
        timeParserResult.checkInvalid();
        return timeParserResult;
    }

    private void postTraverseSyntaxTree(Tree tree, List<Date> dates) {
        if (tree.getText().equals("DATE_TIME")){
            dateTimeCount++;
        }
        if (tree.getText().equals("RELATIVE_DATE") || tree.getText().equals("EXPLICIT_DATE")) {
            if (dateTimeCount == 1) {
                timeParserResult.setFirstDate(dates.get(0));
            } else if (dateTimeCount == 2) {
                timeParserResult.setSecondDate(dates.get(1));
            }
        } else if (tree.getText().equals("RELATIVE_TIME") || tree.getText().equals("EXPLICIT_TIME")) {
            if (dateTimeCount == 1) {
                timeParserResult.setFirstTime(dates.get(0));
            } else if (dateTimeCount == 2) {
                timeParserResult.setSecondTime(dates.get(1));
            }
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            postTraverseSyntaxTree(tree.getChild(i), dates);
        }
    }
}
