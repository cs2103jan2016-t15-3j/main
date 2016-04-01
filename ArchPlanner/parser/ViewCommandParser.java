package parser;

import parser.time.TimeParser;
import parser.time.TimeParserResult;
import logic.TaskParameters;
import logic.commands.Command;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand;
import separator.InputSeparator;

/**
 * Created by lifengshuang on 3/16/16.
 */
public class ViewCommandParser extends CommandParser {

    private TaskParameters result = new TaskParameters();
//    private final String KEYWORD_START_DATE = "start date ";
//    private final String KEYWORD_START_TIME = "start time ";
//    private final String KEYWORD_END_DATE = "end date ";
//    private final String KEYWORD_END_TIME = "end time ";
//    private final String KEYWORD_FROM = "from ";
//    private final String KEYWORD_DESCRIPTION = "description ";
//    private final int VIEW_INDEX = 5;

    @Override
    public Command parse(String input) {
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
                } else {
                    return new InvalidCommand("Invalid Time");
                }
                break;
            case START_DATE:
                if (timeInvalid) {
                    return new InvalidCommand("Invalid Start Date");
                }
                if (timeParserResult.hasOneDateAndNoTime()) {
                    result.setStartDate(timeParserResult.getFirstDate());
                } else {
                    return new InvalidCommand("Invalid Start Date");
                }
                break;
            case START_TIME:
                if (timeInvalid) {
                    return new InvalidCommand("Invalid Start Time");
                }
                if (timeParserResult.hasNoDateAndOneTime()) {
                    result.setStartTime(timeParserResult.getFirstTime());
                } else {
                    return new InvalidCommand("Invalid Start Time");
                }
                break;
            case END_DATE:
                if (timeInvalid) {
                    return new InvalidCommand("Invalid End Date");
                }
                if (timeParserResult.hasOneDateAndNoTime()) {
                    result.setEndDate(timeParserResult.getFirstDate());
                } else {
                    return new InvalidCommand("Invalid End Date");
                }
                break;
            case END_TIME:
                if (timeInvalid) {
                    return new InvalidCommand("Invalid End Time");
                }
                if (timeParserResult.hasNoDateAndOneTime()) {
                    result.setEndTime(timeParserResult.getFirstTime());
                } else {
                    return new InvalidCommand("Invalid End Time");
                }

                break;
        }
        return new InvalidCommand("Invalid Command");

//        input = input.substring(VIEW_INDEX);
//        if (input.equals("all")) {
//            return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_ALL, null, result);
//        }
//        if (input.equals("done")) {
//            return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_DONE, null, result);
//        }
//        if (input.equals("undone")) {
//            return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_UNDONE, null, result);
//        }
//        if (input.equals("overdue")) {
//            return new ViewCommand(ViewCommand.VIEW_TYPE.VIEW_OVERDUE, null, result);
//        }
//        if (input.equals("all")) {
//            return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_ALL, result);
//        }
//        if (input.equals("deadline")) {
//            return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_DEADLINES, result);
//        }
//        if (input.equals("event")) {
//            return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_EVENTS, result);
//        }
//        if (input.equals("task")) {
//            return new ViewCommand(null, ViewCommand.CATEGORY_TYPE.CATEGORY_TASKS, result);
//        }
//
//        TimeParserResult timeParserResult = new TimeParser().parseTime(input);
//        if (input.startsWith(KEYWORD_DESCRIPTION)) {
//            String description = input.substring(KEYWORD_DESCRIPTION.length());
//            if (description.isEmpty()) {
//                return new InvalidCommand("Description missing");
//            }
//            result.setDescription(description);
//        } else if (input.startsWith(KEYWORD_START_DATE)) {
//            String timeString = input.substring(KEYWORD_START_DATE.length());
//            if (timeParserResult.getMatchString().equals(timeString)) {
//                if (timeParserResult.getFirstDate() != null
//                        && timeParserResult.getFirstTime() == null
//                        && timeParserResult.getSecondDate() == null) {
//                    result.setStartDate(timeParserResult.getFirstDate());
//                } else {
//                    return new InvalidCommand("The date is not valid");
//                }
//            } else {
//                return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
//            }
//        } else if (input.startsWith(KEYWORD_END_DATE)) {
//            String timeString = input.substring(KEYWORD_END_DATE.length());
//            if (timeParserResult.getMatchString().equals(timeString)) {
//                if (timeParserResult.getFirstDate() != null
//                        && timeParserResult.getFirstTime() == null
//                        && timeParserResult.getSecondDate() == null) {
//                    result.setEndDate(timeParserResult.getFirstDate());
//                } else {
//                    return new InvalidCommand("The date is not valid");
//                }
//            } else {
//                return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
//            }
//        } else if (input.startsWith(KEYWORD_START_TIME)) {
//            String timeString = input.substring(KEYWORD_START_TIME.length());
//            if (timeParserResult.getMatchString().equals(timeString)) {
//                if (timeParserResult.getFirstTime() != null
//                        && timeParserResult.getFirstDate() == null) {
//                    result.setStartTime(timeParserResult.getFirstTime());
//                } else {
//                    return new InvalidCommand("The time is not valid");
//                }
//            } else {
//                return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
//            }
//        } else if (input.startsWith(KEYWORD_END_TIME)) {
//            String timeString = input.substring(KEYWORD_END_TIME.length());
//            if (timeParserResult.getMatchString().equals(timeString)) {
//                if (timeParserResult.getFirstTime() != null
//                        && timeParserResult.getFirstDate() == null) {
//                    result.setEndTime(timeParserResult.getFirstTime());
//                } else {
//                    return new InvalidCommand("The time is not valid");
//                }
//            } else {
//                return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
//            }
//        } else if (input.startsWith(KEYWORD_FROM)) {
//            String timeString = input.substring(KEYWORD_FROM.length());
//            if (timeParserResult.getMatchString().equals(timeString)) {
//                if (timeParserResult.getFirstDate() != null && timeParserResult.getSecondDate() != null) {
//                    result.setStartDate(timeParserResult.getFirstDate());
//                    result.setEndDate(timeParserResult.getSecondDate());
//                } else {
//                    return new InvalidCommand("The time is not valid");
//                }
//            } else {
//                return new InvalidCommand("Only \"" + timeParserResult.getMatchString() + "\" is recognized");
//            }
//        } else {
//            String[] tags = split(input);
//            for (String tag : tags) {
//                if (tag.charAt(0) == '#') {
//                    tagList.add(tag);
//                } else {
//                    return new InvalidCommand("Command Invalid");
//                }
//            }
//            result.setTagsList(tagList);
//        }
//        return new ViewCommand(null, null, result);
    }


}
