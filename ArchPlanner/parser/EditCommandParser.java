package parser;

import parser.time.TimeParser;
import parser.time.TimeParserResult;
import logic.TaskParameters;
import logic.commands.CommandInterface;
import logic.commands.EditCommand;
import logic.commands.InvalidCommand;
import separator.InputSeparator;

import java.util.ArrayList;

/**
 * @@author A0149647N
 * EditCommandParser parse edit command with InputSeparator
 */
public class EditCommandParser {

    private static final String INVALID_NO_ID = "Index not found!";
    private static final String INVALID_ID_OUT_OF_RANGE = "Index out of range";
    private static final String INVALID_NO_KEYWORD = "Keyword not found!";
    private static final String INVALID_KEYWORD = "Keyword Invalid!";
    private static final String INVALID_ARGUMENT_MISSING = "Argument Missing";
    private static final String INVALID_DATE_TIME = "Invalid date or time";
    private static final String INVALID_START_DATE_TIME = "Invalid start date or time";
    private static final String INVALID_END_DATE_TIME = "Invalid end date or time";
    private static final String INVALID_TIME_RANGE = "Invalid: Start time is after end time";
    private static final String INVALID_COMMAND = "Invalid Command";

    private static final String REMOVE = "remove";
    private static final String START_REMOVE = "start remove";
    private static final String END_REMOVE = "end remove";
    private static final String TAG_NOTATION = "#";
    private static final String TAG_REMOVE = "# remove";
    private static final String INVALID_TAG_EMPTY = "Empty Tag is not allowed";
    private static final String INVALID_TAG_NO_PREFIX = "Tag requires \"#\" prefix";
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    private static final int FIRST_INDEX = 1;

    private TaskParameters result = new TaskParameters();

    /**
     * Parse edit command with InputSeparator
     * @return Parsed command object
     */
    public CommandInterface parse(String input, int viewListSize) {
        InputSeparator inputSeparator = new InputSeparator(input);
        Integer index = inputSeparator.getID();
        InputSeparator.KeywordType type = inputSeparator.getKeywordType();
        String parameter = inputSeparator.getParameter();

        if (index == null) {
            return new InvalidCommand(INVALID_NO_ID);
        }
        if (index < FIRST_INDEX || index > viewListSize) {
            return new InvalidCommand(INVALID_ID_OUT_OF_RANGE);
        }
        if (type == null) {
            return parserNoKeywordCase(parameter, index);
        }
        if (parameter == null) {
            return new InvalidCommand(INVALID_ARGUMENT_MISSING);
        } else {
            return parserKeywordWithParameter(parameter, index, type);
        }
    }

    private CommandInterface parserNoKeywordCase(String parameter, Integer index) {
        if (parameter == null) {
            return new InvalidCommand(INVALID_NO_KEYWORD);
        } else if (parameter.startsWith(TAG_NOTATION)) {
            return parseTag(parameter, index);
        } else {
            return new InvalidCommand(INVALID_KEYWORD);
        }
    }

    private CommandInterface parseTag(String parameter, Integer index){
        if (parameter.equalsIgnoreCase(TAG_REMOVE)) {
            return new EditCommand(index, result, EditCommand.REMOVE_TYPE.TAG);
        }
        ArrayList<String> tagList = new ArrayList<>();
        String[] tags = parameter.split(STRING_MULTIPLE_WHITESPACE);
        for (String tag : tags) {
            if (tag.startsWith(TAG_NOTATION)) {
                if (tag.length() > 1) {
                    tagList.add(tag);
                } else {
                    return new InvalidCommand(INVALID_TAG_EMPTY);
                }
            } else {
                return new InvalidCommand(INVALID_TAG_NO_PREFIX);
            }
        }
        result.setTagsList(tagList);
        return new EditCommand(index, result);
    }

    private CommandInterface parserKeywordWithParameter(String parameter, Integer index, InputSeparator.KeywordType type) {
        TimeParserResult timeParserResult = new TimeParser().parseTime(parameter);
        boolean timeInvalid = timeParserResult.getMatchString() == null || !timeParserResult.getMatchString().equals(parameter);
        boolean isRemove = parameter.equalsIgnoreCase(REMOVE);
        switch (type) {
            case DESCRIPTION:
                result.setDescription(parameter);
                return new EditCommand(index, result);
            case START:
                return parseStart(isRemove, timeInvalid, index, timeParserResult);
            case END:
                return parseEnd(isRemove, timeInvalid, index, timeParserResult);
            case FROM:
                return parseFrom(timeInvalid, index, timeParserResult);
            default:
                break;
        }
        if (parameter.equalsIgnoreCase(START_REMOVE)) {
            return new EditCommand(index, result, EditCommand.REMOVE_TYPE.START);
        }
        if (parameter.equalsIgnoreCase(END_REMOVE)) {
            return new EditCommand(index, result, EditCommand.REMOVE_TYPE.END);
        }
        return new InvalidCommand(INVALID_COMMAND);
    }

    private CommandInterface parseStart(boolean isRemove, boolean timeInvalid, Integer index, TimeParserResult timeParserResult) {
        if (isRemove) {
            return new EditCommand(index, result, EditCommand.REMOVE_TYPE.START);
        }
        if (timeInvalid) {
            return new InvalidCommand(INVALID_START_DATE_TIME);
        }

        switch (timeParserResult.getDateTimeStatus()) {
            case START_TIME:
                result.setStartTime(timeParserResult.getFirstTime());
                return new EditCommand(index, result);
            case START_DATE:
                result.setStartDate(timeParserResult.getFirstDate());
                return new EditCommand(index, result);
            case START_DATE_START_TIME:
                result.setStartDate(timeParserResult.getFirstDate());
                result.setStartTime(timeParserResult.getFirstTime());
                return new EditCommand(index, result);
            default:
                return new InvalidCommand(INVALID_START_DATE_TIME);
        }
    }

    private CommandInterface parseEnd(boolean isRemove, boolean timeInvalid, Integer index, TimeParserResult timeParserResult) {
        if (isRemove) {
            return new EditCommand(index, result, EditCommand.REMOVE_TYPE.END);
        }
        if (timeInvalid) {
            return new InvalidCommand(INVALID_END_DATE_TIME);
        }
        switch (timeParserResult.getDateTimeStatus()) {
            case START_TIME:
                result.setEndTime(timeParserResult.getFirstTime());
                return new EditCommand(index, result);
            case START_DATE:
                result.setEndDate(timeParserResult.getFirstDate());
                return new EditCommand(index, result);
            case START_DATE_START_TIME:
                result.setEndDate(timeParserResult.getFirstDate());
                result.setEndTime(timeParserResult.getFirstTime());
                return new EditCommand(index, result);
            default:
                return new InvalidCommand(INVALID_END_DATE_TIME);
        }
    }

    private CommandInterface parseFrom(boolean timeInvalid, Integer index, TimeParserResult timeParserResult) {
        if (timeInvalid) {
            return new InvalidCommand(INVALID_DATE_TIME);
        }
        if (!timeParserResult.isTimeValid()) {
            return new InvalidCommand(INVALID_TIME_RANGE);
        }
        switch (timeParserResult.getDateTimeStatus()) {
            case START_TIME_END_TIME:
                result.setStartTime(timeParserResult.getFirstTime());
                result.setEndTime(timeParserResult.getSecondTime());
                return new EditCommand(index, result);
            case START_DATE_END_DATE:
                result.setStartDate(timeParserResult.getFirstDate());
                result.setEndDate(timeParserResult.getSecondDate());
                return new EditCommand(index, result);
            case START_DATE_START_TIME_END_DATE_END_TIME:
                result.setStartDate(timeParserResult.getFirstDate());
                result.setStartTime(timeParserResult.getFirstTime());
                result.setEndDate(timeParserResult.getSecondDate());
                result.setEndTime(timeParserResult.getSecondTime());
                return new EditCommand(index, result);
            default:
                return new InvalidCommand(INVALID_DATE_TIME);
        }
    }
}