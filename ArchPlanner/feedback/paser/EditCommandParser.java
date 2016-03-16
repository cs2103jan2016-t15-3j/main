package feedback.paser;

import com.joestelmach.natty.DateGroup;
import logic.TaskParameters;
import logic.commands.Command;
import logic.commands.EditCommand;
import logic.commands.InvalidCommand;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class EditCommandParser extends CommandParser {

    private final int EDIT_ARGUMENT_INDEX = 5;
    private final String KEYWORD_TAG = "tag ";
    private final String KEYWORD_START_TIME = "start time ";
    private final String KEYWORD_END_TIME = "end time ";

    @Override
    public Command parse(String input) {
        if (input.length() <= EDIT_ARGUMENT_INDEX) {
            return new InvalidCommand("Arguments missing");
        }
        String arguments = input.substring(EDIT_ARGUMENT_INDEX).trim();
        int firstSpace = arguments.indexOf(' ');
        if (firstSpace == -1) {
            return new InvalidCommand("Arguments missing");
        }
        int index;
        try {
            index = Integer.parseInt(arguments.substring(0, firstSpace));
        } catch (NumberFormatException e) {
            return new InvalidCommand("Index should be a number");
        }
        arguments = arguments.substring(firstSpace + 1, arguments.length());
        if (arguments.startsWith(KEYWORD_TAG)) {
//            return new EditCommand(index, null, arguments.substring(KEYWORD_TAG.length()), null, null);
            // todo: multiple tags
            String[] tags = split(arguments.substring(KEYWORD_TAG.length()));
            Collections.addAll(tagList, tags);
            return new EditCommand(index, new TaskParameters(null, tagList, null, null));
        } else if (arguments.startsWith(KEYWORD_START_TIME)) {
            String timeString = arguments.substring(KEYWORD_START_TIME.length());
            List<DateGroup> groups = timeParser.parse(input);
            if (groups.size() == 0) {
                return new InvalidCommand("Start time is not recognized");
            }
            DateGroup group = groups.get(0);
            List<Date> dates = group.getDates();
            String matchingValue = group.getText();
            if (dates.size() > 1) {
                return new InvalidCommand("You should input only one time");
            }
            if (matchingValue.equals(timeString)) {
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(dates.get(0));
//                return new EditCommand(index, null, null, startDate, null);
                return new EditCommand(index, new TaskParameters(null, null, startDate, null));
            } else {
                return new InvalidCommand("Only " + matchingValue + " is recognized");
            }
        } else if (arguments.startsWith(KEYWORD_END_TIME)) {
            String timeString = arguments.substring(KEYWORD_END_TIME.length());
            List<DateGroup> groups = timeParser.parse(input);
            if (groups.size() == 0) {
                return new InvalidCommand("End time is not recognized");
            }
            DateGroup group = groups.get(0);
            List<Date> dates = group.getDates();
            String matchingValue = group.getText();
            if (dates.size() > 1) {
                return new InvalidCommand("You should input only one time");
            }
            if (matchingValue.equals(timeString)) {
                Calendar endDate = Calendar.getInstance();
                endDate.setTime(dates.get(0));
//                return new EditCommand(index, null, null, null, endDate);
                return new EditCommand(index, new TaskParameters(null, null, null, endDate));
            } else {
                return new InvalidCommand("Only \"" + matchingValue + "\" is recognized");
            }
        } else {
//            return new EditCommand(index, arguments, null, null, null);
            return new EditCommand(index, new TaskParameters(arguments, null, null, null));
        }
    }
}
