package feedback.paser;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import logic.TaskParameters;
import logic.commands.AddCommand;

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
    private int timeStartIndex;
    private int timeType;
    private final int TYPE_HAS_START_DATE = 0;
    private final int TYPE_ONLY_END_DATE = 1;
    private final int ADD_PARAMETER_INDEX = 4;
    private final String KEYWORD_FROM = "from";
    private final String KEYWORD_STARTS_FROM = "starts from";
    private final String KEYWORD_BY = "by";
    private final String KEYWORD_UNTIL = "until";
    private final String KEYWORD_DEADLINE = "deadline";

    @Override
    public AddCommand parse(String input) {
        this.input = detectTagAndRemoveTagString(input);
        List<DateGroup> groups = timeParser.parse(input);
        DateGroup group = groups.get(0);
        List<Date> dates = group.getDates();
        String matchingValue = group.getText();
        int matchPosition = group.getPosition();
        detectTimeType(matchPosition);
        for (Date date : dates) {
            if (startDate == null && timeType == TYPE_HAS_START_DATE) {
                startDate = Calendar.getInstance();
                startDate.setTime(date);
            } else if (timeType == TYPE_ONLY_END_DATE){
                endDate = Calendar.getInstance();
                endDate.setTime(date);
            }
        }
        description = input.substring(ADD_PARAMETER_INDEX, timeStartIndex);
//        return new AddCommand(description, "tag", startDate, endDate);
        return new AddCommand(new TaskParameters(description, tagList, startDate, endDate));
    }

    private void detectTimeType(int matchPosition) {
        timeStartIndex = matchPosition;
        if (hasPrefixWithKeyword(KEYWORD_BY, matchPosition)
                || hasPrefixWithKeyword(KEYWORD_UNTIL, matchPosition)
                || hasPrefixWithKeyword(KEYWORD_DEADLINE, matchPosition)) {
            timeType = TYPE_ONLY_END_DATE;
        } else if (hasPrefixWithKeyword(KEYWORD_FROM, matchPosition)
                || hasPrefixWithKeyword(KEYWORD_STARTS_FROM, matchPosition)){
            timeType = TYPE_HAS_START_DATE;
        }
    }

    private boolean hasPrefixWithKeyword(String keyword, int index) {
        if (index > keyword.length() + 2) {
            int startIndex = index - keyword.length() - 3;
            if (input.substring(startIndex, index - 1).equals(" " + keyword + " ")) {
                timeStartIndex = startIndex;
                return true;
            }
        }
        return false;
    }

}
