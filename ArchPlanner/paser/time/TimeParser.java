package paser.time;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import org.antlr.runtime.tree.Tree;

import java.util.Date;
import java.util.List;

/**
 * Created by lifengshuang on 3/22/16.
 */
public class TimeParser {

    private static Parser timeParser = new Parser();
    private int dateCount, timeCount;

    public TimeParserResult parseTime(String input) {
        TimeParserResult timeParserResult = new TimeParserResult();
        List<DateGroup> groups = timeParser.parse(input);
        for (DateGroup group : groups){
            if (group.getText().length() < 3){
                continue;
            }
            List<Date> dates = group.getDates();
            Tree tree = group.getSyntaxTree();
            postTraverseSyntaxTree(tree);
            if (dates.size() < dateCount || dates.size() < timeCount) {
                return timeParserResult;
            }
            timeParserResult.setMatchPosition(group.getPosition());
            timeParserResult.setMatchString(group.getText());
            for (int i = 0; i < dateCount; i++) {
                timeParserResult.setDate(dates.get(i));
            }
            for (int i = 0; i < timeCount; i++) {
                timeParserResult.setTime(dates.get(i));
            }
            break;
        }
        return timeParserResult;
    }

    private void postTraverseSyntaxTree(Tree tree) {
        if (tree.getText().equals("RELATIVE_DATE") || tree.getText().equals("EXPLICIT_DATE")) {
            dateCount++;
        } else if (tree.getText().equals("RELATIVE_TIME") || tree.getText().equals("EXPLICIT_TIME")) {
            timeCount++;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            postTraverseSyntaxTree(tree.getChild(i));
        }
    }
}
