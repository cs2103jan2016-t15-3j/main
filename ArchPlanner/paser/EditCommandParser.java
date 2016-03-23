package paser;

import paser.time.TimeParser;
import paser.time.TimeParserResult;
import logic.TaskParameters;
import logic.commands.Command;
import logic.commands.EditCommand;
import logic.commands.InvalidCommand;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class EditCommandParser extends CommandParser {

    private int index;

    private final int EDIT_ARGUMENT_INDEX = 5;
    private final String KEYWORD_START_DATE = "start date ";
    private final String KEYWORD_START_TIME = "start time ";
    private final String KEYWORD_END_DATE = "end date ";
    private final String KEYWORD_END_TIME = "end time ";
    private final String KEYWORD_DESCRIPTION = "description ";
    private TaskParameters result = new TaskParameters();

    @Override
    public Command parse(String input) {
        this.input = input;
        if (input.length() <= EDIT_ARGUMENT_INDEX) {
            return new InvalidCommand("Arguments missing");
        }
        String arguments = input.substring(EDIT_ARGUMENT_INDEX).trim();
        int firstSpace = arguments.indexOf(' ');
        if (firstSpace == -1) {
            return new InvalidCommand("Arguments missing");
        }
        try {
            index = Integer.parseInt(arguments.substring(0, firstSpace));
        } catch (NumberFormatException e) {
            return new InvalidCommand("Index should be a number");
        }
        arguments = arguments.substring(firstSpace + 1, arguments.length());

        if (arguments.startsWith(KEYWORD_DESCRIPTION)) {
            result.setDescription(arguments.substring(KEYWORD_DESCRIPTION.length()));
            return new EditCommand(index, result);
        } else if (arguments.startsWith(KEYWORD_START_DATE)) {
            return parseEditStartDate(arguments);
        } else if (arguments.startsWith(KEYWORD_END_DATE)) {
            return parseEditEndDate(arguments);
        } else if (arguments.startsWith(KEYWORD_START_TIME)) {
            return parseEditStartTime(arguments);
        } else if (arguments.startsWith(KEYWORD_END_TIME)) {
            return parseEditEndTime(arguments);
        } else {
            return parseEditTag(arguments);
        }
    }

    private Command parseEditTag(String arguments) {
        String[] tags = split(arguments);
        for (String tag : tags) {
            if (!tag.isEmpty() && tag.charAt(0) == '#') {
                tagList.add(tag);
            } else {
                return new InvalidCommand("Command Invalid!");
            }
        }
        result.setTagsList(tagList);
        return new EditCommand(index, result);
    }

    private Command parseEditStartDate(String arguments) {
        String timeString = arguments.substring(KEYWORD_START_DATE.length());
        TimeParserResult timeParserResult = new TimeParser().parseTime(arguments);
        if (timeParserResult.getMatchString().equals(timeString)) {
            if (timeParserResult.getFirstDate() != null
                    && timeParserResult.getFirstTime() == null
                    && timeParserResult.getSecondDate() == null) {
                result.setStartDate(timeParserResult.getFirstDate());
                return new EditCommand(index, result);
            } else {
                return new InvalidCommand("The date is not valid");
            }
        } else {
            return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
        }
    }

    private Command parseEditEndDate(String arguments) {
        String timeString = arguments.substring(KEYWORD_END_DATE.length());
        TimeParserResult timeParserResult = new TimeParser().parseTime(arguments);
        if (timeParserResult.getMatchString().equals(timeString)) {
            if (timeParserResult.getFirstDate() != null
                    && timeParserResult.getFirstTime() == null
                    && timeParserResult.getSecondDate() == null) {
                result.setEndDate(timeParserResult.getFirstDate());
                return new EditCommand(index, result);
            } else {
                return new InvalidCommand("The date is not valid");
            }
        } else {
            return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
        }
    }

    private Command parseEditStartTime(String arguments) {
        String timeString = arguments.substring(KEYWORD_START_TIME.length());
        TimeParserResult timeParserResult = new TimeParser().parseTime(arguments);
        if (timeParserResult.getMatchString().equals(timeString)) {
            if (timeParserResult.getFirstTime() != null
                    && timeParserResult.getFirstDate() == null) {
                result.setStartTime(timeParserResult.getFirstTime());
                return new EditCommand(index, result);
            } else {
                return new InvalidCommand("The time is not valid");
            }
        } else {
            return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
        }
    }

    private Command parseEditEndTime(String arguments) {
        String timeString = arguments.substring(KEYWORD_END_TIME.length());
        TimeParserResult timeParserResult = new TimeParser().parseTime(arguments);
        if (timeParserResult.getMatchString().equals(timeString)) {
            if (timeParserResult.getFirstTime() != null
                    && timeParserResult.getFirstDate() == null) {
                result.setEndTime(timeParserResult.getFirstTime());
                return new EditCommand(index, result);
            } else {
                return new InvalidCommand("The time is not valid");
            }
        } else {
            return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
        }
    }
}
