package interpreter.parser;

import logic.Tag;
import interpreter.parser.time.TimeParser;
import interpreter.parser.time.TimeParserResult;
import interpreter.separater.InputSeparater;
import logic.TaskParameters;
import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand;

import java.util.ArrayList;

/**
 * @@author A0149647N
 * ViewCommandParser parse view command with InputSeparator
 */
public class ViewCommandParser {

    private static final String INVALID_HAVE_ID = "View command shouldn't have id";
    private static final String INVALID_INCOMPLETE = "View command incomplete";
    private static final String INVALID_TAG_NOT_EXIST = "Tag %s doesn't exist!";
    private static final String INVALID_TAG_EMPTY = "Empty Tag is not allowed";
    private static final String INVALID_TAG_NO_PREFIX = "Tag requires \"#\" prefix";
    private static final String INVALID_TIME = "Invalid Time";
    private static final String INVALID_START_TIME = "Invalid Start Time";
    private static final String INVALID_START_DATE = "Invalid Start Date";
    private static final String INVALID_END_TIME = "Invalid End Time";
    private static final String INVALID_END_DATE = "Invalid End Date";
    private static final String INVALID_COMMAND = "Invalid Command";
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    private static final String TAG_NOTATION = "#";
    private static final int MINIMUM_STRING_LENGTH = 1;

    private TaskParameters result = new TaskParameters();

    /**
     * Parse view command with InputSeparator
     * @return Parsed command object
     */
    public CommandInterface parse(String input, ArrayList<Tag> currentTagList) {
        InputSeparater separator = new InputSeparater(input);
        InputSeparater.KeywordType type = separator.getKeywordType();
        String parameter = separator.getParameter();
        if (separator.getID() != null) {
            return new InvalidCommand(INVALID_HAVE_ID);
        }

        if (type == null) {
            return parseNoKeywordCase(parameter, currentTagList);
        }

        if (parameter == null) {
            return parseKeywordOnlyCase(type);
        }

        return parserKeywordWithParameter(parameter, type);
    }

    /**
     * Parse view command with keyword and its parameter
     * May return parsed ViewCommand or InvalidCommand
     * @param parameter This is the parameter of the keyword
     * @param type This is the keyword typr
     * @return Parsed result
     */
    private CommandInterface parserKeywordWithParameter(String parameter, InputSeparater.KeywordType type) {
        TimeParserResult timeParserResult = new TimeParser().parseTime(parameter);
        boolean timeInvalid = timeParserResult.getMatchString() == null || !timeParserResult.getMatchString().equals(parameter);
        switch (type) {
            case DESCRIPTION:
                return parseDescription(parameter);
            case FROM:
                return parseFrom(timeInvalid, timeParserResult);
            case START_DATE:
                return parseStartDate(timeInvalid, timeParserResult);
            case START_TIME:
                return parseStartTime(timeInvalid, timeParserResult);
            case END_DATE:
                return parseEndDate(timeInvalid, timeParserResult);
            case END_TIME:
                return parseEndTime(timeInvalid, timeParserResult);
            default:
                return new InvalidCommand(INVALID_COMMAND);
        }
    }

    /**
     * Parse "view description" command
     * @param parameter This is the description to view
     * @return Parsed result
     */
    private CommandInterface parseDescription(String parameter) {
        result.setDescription(parameter);
        return new ViewCommand(null, null, result);
    }

    /**
     * Parse "view from" command
     * May return parsed ViewCommand or InvalidCommand
     * @param timeInvalid True if the time is valid
     * @param timeParserResult This is the TimeParserResult object which contains data
     * @return Parsed result
     */
    private CommandInterface parseFrom(boolean timeInvalid, TimeParserResult timeParserResult) {
        if (timeInvalid) {
            return new InvalidCommand(INVALID_TIME);
        }
        if (timeParserResult.hasTwoDateAndNoTime()) {
            result.setStartDate(timeParserResult.getFirstDate());
            result.setEndDate(timeParserResult.getSecondDate());
            return new ViewCommand(null, null, result);
        } else {
            return new InvalidCommand(INVALID_TIME);
        }
    }

    /**
     * Parse "view start time" command
     * May return parsed ViewCommand or InvalidCommand
     * @param timeInvalid True if the time is valid
     * @param timeParserResult This is the TimeParserResult object which contains data
     * @return Parsed result
     */
    private CommandInterface parseStartTime(boolean timeInvalid, TimeParserResult timeParserResult) {
        if (timeInvalid) {
            return new InvalidCommand(INVALID_START_TIME);
        }
        if (timeParserResult.hasNoDateAndOneTime()) {
            result.setStartTime(timeParserResult.getFirstTime());
            return new ViewCommand(null, null, result);
        } else {
            return new InvalidCommand(INVALID_START_TIME);
        }
    }

    /**
     * Parse "view start date" command
     * May return parsed ViewCommand or InvalidCommand
     * @param timeInvalid True if the time is valid
     * @param timeParserResult This is the TimeParserResult object which contains data
     * @return Parsed result
     */
    private CommandInterface parseStartDate(boolean timeInvalid, TimeParserResult timeParserResult) {
        if (timeInvalid) {
            return new InvalidCommand(INVALID_START_DATE);
        }
        if (timeParserResult.hasOneDateAndNoTime()) {
            result.setStartDate(timeParserResult.getFirstDate());
            return new ViewCommand(null, null, result);
        } else {
            return new InvalidCommand(INVALID_START_DATE);
        }
    }

    /**
     * Parse "view end time" command
     * May return parsed ViewCommand or InvalidCommand
     * @param timeInvalid True if the time is valid
     * @param timeParserResult This is the TimeParserResult object which contains data
     * @return Parsed result
     */
    private CommandInterface parseEndTime(boolean timeInvalid, TimeParserResult timeParserResult) {
        if (timeInvalid) {
            return new InvalidCommand(INVALID_END_TIME);
        }
        if (timeParserResult.hasNoDateAndOneTime()) {
            result.setEndTime(timeParserResult.getFirstTime());
            return new ViewCommand(null, null, result);
        } else {
            return new InvalidCommand(INVALID_END_TIME);
        }
    }

    /**
     * Parse "view end date" command
     * May return parsed ViewCommand or InvalidCommand
     * @param timeInvalid True if the time is valid
     * @param timeParserResult This is the TimeParserResult object which contains data
     * @return Parsed result
     */
    private CommandInterface parseEndDate(boolean timeInvalid, TimeParserResult timeParserResult) {
        if (timeInvalid) {
            return new InvalidCommand(INVALID_END_DATE);
        }
        if (timeParserResult.hasOneDateAndNoTime()) {
            result.setEndDate(timeParserResult.getFirstDate());
            return new ViewCommand(null, null, result);
        } else {
            return new InvalidCommand(INVALID_END_DATE);
        }
    }

    /**
     * Parse view command without keyword
     * If the command is to view tag, parse the tags
     * Else, the parameter will be processed as partial description
     * @param parameter This is the parameter
     * @param currentTagList This is the current tag list. Tag to view should in this list
     * @return Parsed result
     */
    private CommandInterface parseNoKeywordCase(String parameter, ArrayList<Tag> currentTagList) {
        if (parameter == null) {
            return new InvalidCommand(INVALID_INCOMPLETE);
        } else if (parameter.startsWith(TAG_NOTATION)) {
            return parseTag(parameter, currentTagList);
        } else {
            result.setDescription(parameter);
            return new ViewCommand(null, null, result);
        }
    }

    /**
     * Parse view command with only keyword
     * The command is to view types or categories
     * The command maybe invalid if keyword doesn't match
     * @param type This is the command type
     * @return Parsed result
     */
    private CommandInterface parseKeywordOnlyCase(InputSeparater.KeywordType type) {
        switch (type) {
            case ALL:
                return new ViewCommand(ViewCommand.VIEW_TYPE.ALL, null, result);
            case DONE:
                return new ViewCommand(ViewCommand.VIEW_TYPE.DONE, null, result);
            case UNDONE:
                return new ViewCommand(ViewCommand.VIEW_TYPE.UNDONE, null, result);
            case OVERDUE:
                return new ViewCommand(ViewCommand.VIEW_TYPE.OVERDUE, null, result);
            case TASKS:
                return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.TASKS, result);
            case DEADLINES:
                return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.DEADLINES, result);
            case EVENTS:
                return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.EVENTS, result);
            default:
                return new InvalidCommand(INVALID_INCOMPLETE);
        }
    }

    /**
     * Parse the tag string to a tag list
     * The tag in the string should be in the global tag list
     * @param parameter This is the tag string
     * @param currentTagList This is the current global tag list
     * @return Parsed result
     */
    private CommandInterface parseTag(String parameter, ArrayList<Tag> currentTagList) {
        String[] tags = parameter.split(STRING_MULTIPLE_WHITESPACE);
        ArrayList<String> tagList = new ArrayList<>();
        for (String tag : tags) {
            if (!tag.startsWith(TAG_NOTATION)) {
                return new InvalidCommand(INVALID_TAG_NO_PREFIX);
            }
            if (tag.length() <= MINIMUM_STRING_LENGTH) {
                return new InvalidCommand(INVALID_TAG_EMPTY);
            }
            if (!containTag(tag, currentTagList)) {
                return new InvalidCommand(String.format(INVALID_TAG_NOT_EXIST, tag));
            }
            tagList.add(tag);
        }
        result.setTagsList(tagList);
        return new ViewCommand(null, null, result);
    }

    /**
     * Check whether the tag is in the global tag list
     * @param tagName This is the tag name to check
     * @param currentTagList This is the current global tag list
     * @return True if the tag name is in the tag list
     */
    private boolean containTag(String tagName, ArrayList<Tag> currentTagList) {
        for (Tag tag : currentTagList) {
            if (tag.getName().equals(tagName)) {
                return true;
            }
        }
        return false;
    }


}
