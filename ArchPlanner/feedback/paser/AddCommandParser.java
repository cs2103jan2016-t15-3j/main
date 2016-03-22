package feedback.paser;

import com.joestelmach.natty.DateGroup;
import logic.TaskParameters;
import logic.commands.AddCommand;
import org.antlr.runtime.tree.Tree;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class AddCommandParser extends CommandParser {

    private String description;
    //add sth from 07/05/16 09:07 AM to 08/06/16 7:7 PM #my tag
    //add finish this project by 04/15/16 0:0 AM #ArchPlanner
    //add enjoy summer holiday from 05/05/16 0:0 PM #Holiday!!!
    private Calendar startDate;
    private Calendar endDate;
    //add sth from 1pm to 3pm
    //add lalala from tomorrow to this Friday #a
    //add miao by Mar 7 #a #b
    //add assignments this Thursday 3pm #assign #oh-my-god #help-me!
    private final int TIME_TYPE_HAS_START_DATE = 0;
    private final int TIME_TYPE_ONLY_END_DATE = 1;
    private final int ADD_PARAMETER_INDEX = 4;
    private final String KEYWORD_FROM = "from";
    private final String KEYWORD_START_FROM = "start from";
    private final String KEYWORD_BY = "by";
    private final String KEYWORD_UNTIL = "until";
    private final String KEYWORD_DEADLINE = "deadline";

    @Override
    public AddCommand parse(String input) {
        this.input = parseTag(input);
        this.input = parseTime(this.input);
        description = this.input.substring(ADD_PARAMETER_INDEX);
//        return new AddCommand(description, "tag", startDate, endDate);
        return new AddCommand(new TaskParameters(description, tagList, startDate, endDate));
    }

    private String parseTime(String input) {
        List<DateGroup> groups = timeParser.parse(input);
        if (groups.size() > 0) {
            DateGroup group = groups.get(0);
            List<Date> dates = group.getDates();
            String matchingValue = group.getText();
            Tree tree = group.getSyntaxTree();
            if (matchingValue.length() >= 3) {
                int matchPosition = group.getPosition();
                String keyword = detectTimeKeyword(matchPosition);
                if (isDeadlineKeyword(keyword)) {
                    endDate = Calendar.getInstance();
                    endDate.setTime(dates.get(0));
                } else {
                    startDate = Calendar.getInstance();
                    startDate.setTime(dates.get(0));
                    if (dates.size() > 1) {
                        endDate = Calendar.getInstance();
                        endDate.setTime(dates.get(1));
                    }
                }
                input = removeTimeString(input, matchPosition, keyword);
            }
        }
        return input;
    }

    private String removeTimeString(String input, int matchingPosition, String keyword) {
        if (keyword.isEmpty()) {
            return input.substring(0, matchingPosition - 2);
        }
        return input.substring(0, matchingPosition - keyword.length() - 3);
    }

    private boolean isDeadlineKeyword(String keyword) {
        if (keyword == null) {
            return false;
        }
        if (keyword.equals(KEYWORD_BY) || keyword.equals(KEYWORD_DEADLINE) || keyword.equals(KEYWORD_UNTIL)) {
            return true;
        } else {
            return false;
        }
    }

    private String detectTimeKeyword(int matchPosition) {
        if (hasPrefixWithKeyword(KEYWORD_BY, matchPosition)) {
            return KEYWORD_BY;
        }
        if (hasPrefixWithKeyword(KEYWORD_UNTIL, matchPosition)) {
            return KEYWORD_UNTIL;
        }
        if (hasPrefixWithKeyword(KEYWORD_DEADLINE, matchPosition)) {
            return KEYWORD_DEADLINE;
        }
        if (hasPrefixWithKeyword(KEYWORD_START_FROM, matchPosition)) {
            return KEYWORD_START_FROM;
        }
        if (hasPrefixWithKeyword(KEYWORD_FROM, matchPosition)) {
            return KEYWORD_FROM;
        }
        return "";
    }

    private boolean hasPrefixWithKeyword(String keyword, int index) {
        if (index > keyword.length() + 2) {
            int startIndex = index - keyword.length() - 3;
            if (input.substring(startIndex, index - 1).equals(" " + keyword + " ")) {
                return true;
            }
        }
        return false;
    }

}