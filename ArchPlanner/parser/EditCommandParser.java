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

    private TaskParameters result = new TaskParameters();


    public CommandInterface parse(String input, int viewListSize) {
        InputSeparator inputSeparator = new InputSeparator(input);
        Integer index = inputSeparator.getID();
        InputSeparator.KeywordType type = inputSeparator.getKeywordType();
        String parameter = inputSeparator.getParameter();

        if (index == null) {
            return new InvalidCommand("Index not found!");
        }
        if (index <= 0 || index > viewListSize) {
            return new InvalidCommand("Index out of range");
        }
        if (type == null) {
            if (parameter == null) {
                return new InvalidCommand("Keyword not found!");
            } else if (parameter.startsWith("#")) {
                if (parameter.equalsIgnoreCase("# remove")) {
                    return new EditCommand(index, result, EditCommand.REMOVE_TYPE.TAG);
                }
                ArrayList<String> tagList = new ArrayList<>();
                String[] tags = parameter.split("\\s+");
                for (String tag : tags) {
                    if (tag.startsWith("#")) {
                        if (tag.length() > 1) {
                            tagList.add(tag);
                        } else {
                            return new InvalidCommand("Empty Tag is not allowed");
                        }
                    } else {
                        return new InvalidCommand("Tag requires \"#\" prefix");
                    }
                }
                result.setTagsList(tagList);
                return new EditCommand(index, result);
            } else {
                return new InvalidCommand("Keyword Invalid!");
            }
        }
        if (parameter == null) {
            return new InvalidCommand("Argument Missing");
        } else {
            TimeParserResult timeParserResult = new TimeParser().parseTime(parameter);
            boolean timeInvalid = timeParserResult.getMatchString() == null || !timeParserResult.getMatchString().equals(parameter);
            boolean isRemove = parameter.equalsIgnoreCase("remove");
            switch (type) {
                case DESCRIPTION:
                    result.setDescription(parameter);
                    return new EditCommand(index, result);
                case START:
                    if (isRemove) {
                        return new EditCommand(index, result, EditCommand.REMOVE_TYPE.START);
                    }
                    if (timeInvalid) {
                        return new InvalidCommand("Invalid start date or time");
                    }

                    switch (timeParserResult.getDateTimeStatus()) {
                        //0100
                        case START_TIME:
                            result.setStartTime(timeParserResult.getFirstTime());
                            return new EditCommand(index, result);
                        //1000
                        case START_DATE:
                            result.setStartDate(timeParserResult.getFirstDate());
                            return new EditCommand(index, result);
                        case START_DATE_START_TIME:
                            result.setStartDate(timeParserResult.getFirstDate());
                            result.setStartTime(timeParserResult.getFirstTime());
                            return new EditCommand(index, result);
                        default:
                            return new InvalidCommand("Invalid start date or time");
                    }
                case END:
                    if (isRemove) {
                        return new EditCommand(index, result, EditCommand.REMOVE_TYPE.END);
                    }
                    if (timeInvalid) {
                        return new InvalidCommand("Invalid end date or time");
                    }
                    switch (timeParserResult.getDateTimeStatus()) {
                        //0100
                        case START_TIME:
                            result.setEndTime(timeParserResult.getFirstTime());
                            return new EditCommand(index, result);
                        //1000
                        case START_DATE:
                            result.setEndDate(timeParserResult.getFirstDate());
                            return new EditCommand(index, result);
                        case START_DATE_START_TIME:
                            result.setEndDate(timeParserResult.getFirstDate());
                            result.setEndTime(timeParserResult.getFirstTime());
                            return new EditCommand(index, result);
                        default:
                            return new InvalidCommand("Invalid end date or time");
                    }
                case FROM:
                    if (timeInvalid) {
                        return new InvalidCommand("Invalid date or time");
                    }
                    if (!timeParserResult.isTimeValid()) {
                        return new InvalidCommand("Invalid: Start time is after end time!");
                    }
                    switch (timeParserResult.getDateTimeStatus()) {
                        //0101
                        case START_TIME_END_TIME:
                            result.setStartTime(timeParserResult.getFirstTime());
                            result.setEndTime(timeParserResult.getSecondTime());
                            return new EditCommand(index, result);
                        //1010
                        case START_DATE_END_DATE:
                            result.setStartDate(timeParserResult.getFirstDate());
                            result.setEndDate(timeParserResult.getSecondDate());
                            return new EditCommand(index, result);
                        //1111
                        case START_DATE_START_TIME_END_DATE_END_TIME:
                            result.setStartDate(timeParserResult.getFirstDate());
                            result.setStartTime(timeParserResult.getFirstTime());
                            result.setEndDate(timeParserResult.getSecondDate());
                            result.setEndTime(timeParserResult.getSecondTime());
                            return new EditCommand(index, result);
                        default:
                            return new InvalidCommand("Invalid date or time");
                    }
                case START_TIME:
                    if (isRemove) {
                        return new EditCommand(index, result, EditCommand.REMOVE_TYPE.START_TIME);
                    } else {
                        return new InvalidCommand("Invalid command.");
                    }
                case END_TIME:
                    if (isRemove) {
                        return new EditCommand(index, result, EditCommand.REMOVE_TYPE.END_TIME);
                    } else {
                        return new InvalidCommand("Invalid command.");
                    }
            }
            if (parameter.equalsIgnoreCase("start remove")) {
                return new EditCommand(index, result, EditCommand.REMOVE_TYPE.START);
            }
            if (parameter.equalsIgnoreCase("end remove")) {
                return new EditCommand(index, result, EditCommand.REMOVE_TYPE.END);
            }
        }

        return new InvalidCommand("Invalid Command");
    }

}
