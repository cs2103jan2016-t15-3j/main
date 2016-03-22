package feedback.paser;

import feedback.paser.time.TimeParser;
import feedback.paser.time.TimeParserResult;
import logic.TaskParameters;
import logic.commands.Command;
import logic.commands.EditCommand;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand;

import java.util.Arrays;

/**
 * Created by lifengshuang on 3/16/16.
 */
public class ViewCommandParser extends CommandParser {

    private TaskParameters result = new TaskParameters();
    private final String KEYWORD_START_DATE = "start date ";
    private final String KEYWORD_START_TIME = "start time ";
    private final String KEYWORD_END_DATE = "end date ";
    private final String KEYWORD_END_TIME = "end time ";
    private final String KEYWORD_DESCRIPTION = "description ";
    private final int VIEW_INDEX = 5;

    @Override
    public Command parse(String input) {
        input = input.substring(VIEW_INDEX);
        if (input.equals("all")) {
            return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_ALL, null, result);
        }
        if (input.equals("done")) {
            return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_DONE, null, result);
        }
        if (input.equals("undone")) {
            return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_UNDONE, null, result);
        }
        if (input.equals("overdue")) {
            return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_OVERDUE, null, result);
        }
        if (input.equals("floating")) {
            return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_FLOATING, result);
        }
        if (input.equals("deadline")) {
            return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_DEADLINE, result);
        }
        if (input.equals("task")) {
            return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_TASKS, result);
        }
        if (input.equals("event")) {
            return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_EVENT, result);
        }

        TimeParserResult timeParserResult = new TimeParser().parseTime(input);
        if (input.startsWith(KEYWORD_DESCRIPTION)) {
            result.setDescription(input.substring(KEYWORD_DESCRIPTION.length()));
        } else if (input.startsWith(KEYWORD_START_DATE)) {
            String timeString = input.substring(KEYWORD_START_DATE.length());
            if (timeParserResult.getMatchString().equals(timeString)) {
                if (timeParserResult.getFirstDate() != null
                        && timeParserResult.getFirstTime() == null
                        && timeParserResult.getSecondDate() == null) {
                    result.setStartDate(timeParserResult.getFirstDate());
                } else {
                    return new InvalidCommand("The date is not valid");
                }
            } else {
                return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
            }
        } else if (input.startsWith(KEYWORD_END_DATE)) {
            String timeString = input.substring(KEYWORD_END_DATE.length());
            if (timeParserResult.getMatchString().equals(timeString)) {
                if (timeParserResult.getFirstDate() != null
                        && timeParserResult.getFirstTime() == null
                        && timeParserResult.getSecondDate() == null) {
                    result.setEndDate(timeParserResult.getFirstDate());
                } else {
                    return new InvalidCommand("The date is not valid");
                }
            } else {
                return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
            }
        } else if (input.startsWith(KEYWORD_START_TIME)) {
            String timeString = input.substring(KEYWORD_START_TIME.length());
            if (timeParserResult.getMatchString().equals(timeString)) {
                if (timeParserResult.getFirstTime() != null
                        && timeParserResult.getFirstDate() == null) {
                    result.setStartTime(timeParserResult.getFirstTime());
                } else {
                    return new InvalidCommand("The time is not valid");
                }
            } else {
                return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
            }
        } else if (input.startsWith(KEYWORD_END_TIME)) {
            String timeString = input.substring(KEYWORD_END_TIME.length());
            if (timeParserResult.getMatchString().equals(timeString)) {
                if (timeParserResult.getFirstTime() != null
                        && timeParserResult.getFirstDate() == null) {
                    result.setEndTime(timeParserResult.getFirstTime());
                } else {
                    return new InvalidCommand("The time is not valid");
                }
            } else {
                return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
            }
        } else {

            String[] tags = split(input);
            if (tags.length <= 1) {
                return new InvalidCommand("Argument missing");
            } else {
                for (String tag : tags) {
                    if (tag.charAt(0) == '#') {
                        tagList.add(tag);
                    } else {
                        return new InvalidCommand("Command Invalid");
                    }
                }
                result.setTagsList(tagList);
            }
        }
        return new ViewCommand(null, null, result);
    }

}
