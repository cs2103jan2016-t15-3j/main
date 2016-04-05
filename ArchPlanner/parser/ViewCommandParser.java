package parser;

import logic.Tag;
import parser.time.TimeParser;
import parser.time.TimeParserResult;
import logic.TaskParameters;
import logic.commands.Command;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand;
import separator.InputSeparator;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 3/16/16.
 */
public class ViewCommandParser extends CommandParser {

    private TaskParameters result = new TaskParameters();

    public Command parse(String input, ArrayList<Tag> currentTagList) {
        InputSeparator separator = new InputSeparator(input);
        InputSeparator.KeywordType type = separator.getKeywordType();
        String parameter = separator.getParameter();
        if (separator.getID() != null) {
            return new InvalidCommand("View command shouldn't have id");
        }
        if (type == null) {
            if (parameter == null) {
                return new InvalidCommand("View command incomplete");
            } else if (parameter.startsWith("#")) {
                String[] tags = parameter.split("\\s+");
                for (String tag : tags) {
                    if (tag.startsWith("#")) {
                        if (tag.length() > 1) {
                            if (!containTag(tag, currentTagList)) {
                                return new InvalidCommand("Tag " + tag + " doesn't exist!");
                            }
                            tagList.add(tag);
                        } else {
                            return new InvalidCommand("Empty Tag is not allowed");
                        }
                    } else {
                        return new InvalidCommand("Tag requires \"#\" prefix");
                    }
                }
                result.setTagsList(tagList);
                return new ViewCommand(null, null, result);
            } else {
                result.setDescription(parameter);
                return new ViewCommand(null, null, result);
            }
        }

        if (parameter == null) {
            switch (type) {
                case ALL:
                    return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_ALL, null, result);
                case DONE:
                    return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_DONE, null, result);
                case UNDONE:
                    return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_UNDONE, null, result);
                case OVERDUE:
                    return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_OVERDUE, null, result);
                case TASKS:
                    return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_TASKS, result);
                case DEADLINES:
                    return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_DEADLINES, result);
                case EVENTS:
                    return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_EVENTS, result);
                default:
                    return new InvalidCommand("View command incomplete");
            }
        }

        TimeParserResult timeParserResult = new TimeParser().parseTime(parameter);
        boolean timeInvalid = timeParserResult.getMatchString() == null || !timeParserResult.getMatchString().equals(parameter);
        switch (type) {
            case DESCRIPTION:
                result.setDescription(parameter);
                return new ViewCommand(null, null, result);
            case FROM:
                if (timeInvalid) {
                    return new InvalidCommand("Invalid Time");
                }
                if (timeParserResult.hasTwoDateAndNoTime()) {
                    result.setStartDate(timeParserResult.getFirstDate());
                    result.setEndDate(timeParserResult.getSecondDate());
                    return new ViewCommand(null, null, result);
                } else {
                    return new InvalidCommand("Invalid Time");
                }
            case START_DATE:
                if (timeInvalid) {
                    return new InvalidCommand("Invalid Start Date");
                }
                if (timeParserResult.hasOneDateAndNoTime()) {
                    result.setStartDate(timeParserResult.getFirstDate());
                    return new ViewCommand(null, null, result);
                } else {
                    return new InvalidCommand("Invalid Start Date");
                }
            case START_TIME:
                if (timeInvalid) {
                    return new InvalidCommand("Invalid Start Time");
                }
                if (timeParserResult.hasNoDateAndOneTime()) {
                    result.setStartTime(timeParserResult.getFirstTime());
                    return new ViewCommand(null, null, result);
                } else {
                    return new InvalidCommand("Invalid Start Time");
                }
            case END_DATE:
                if (timeInvalid) {
                    return new InvalidCommand("Invalid End Date");
                }
                if (timeParserResult.hasOneDateAndNoTime()) {
                    result.setEndDate(timeParserResult.getFirstDate());
                    return new ViewCommand(null, null, result);
                } else {
                    return new InvalidCommand("Invalid End Date");
                }
            case END_TIME:
                if (timeInvalid) {
                    return new InvalidCommand("Invalid End Time");
                }
                if (timeParserResult.hasNoDateAndOneTime()) {
                    result.setEndTime(timeParserResult.getFirstTime());
                    return new ViewCommand(null, null, result);
                } else {
                    return new InvalidCommand("Invalid End Time");
                }

        }
        return new InvalidCommand("Invalid Command");
    }

    private boolean containTag(String tagName, ArrayList<Tag> currentTagList) {
        for (Tag tag : currentTagList) {
            if (tag.getName().equals(tagName)) {
                return true;
            }
        }
        return false;
    }


}
