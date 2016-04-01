package feedback;

import separator.InputSeparator;
import parser.time.TimeParser;
import parser.time.TimeParserResult;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 3/26/16.
 */
public class ViewPrompt {
    private final String VIEW_ALL = "view all";
    private final String VIEW_DONE = "view done";
    private final String VIEW_UNDONE = "view undone";
    private final String VIEW_OVERDUE = "view overdue";
    private final String VIEW_DEADLINE = "view deadlines";
    private final String VIEW_EVENT = "view events";
    private final String VIEW_TASK = "view tasks";
    private final String VIEW_DESCRIPTION = "view description <Partial Description>";
    private final String VIEW_DESCRIPTION_WITHOUT_KEYWORD = "view <Partial Description>";
    private final String VIEW_FROM = "view from ";
    private final String VIEW_FROM_FULL = "view from <Start Date> to <End Date>";
    private final String VIEW_START = "view start";
    private final String VIEW_START_DATE = "view start date ";
    private final String VIEW_START_DATE_FULL = "view start date <Start Date>";
    private final String VIEW_START_TIME = "view start time ";
    private final String VIEW_START_TIME_FULL = "view start time <Start Time>";
    private final String VIEW_END = "view end";
    private final String VIEW_END_DATE = "view end date ";
    private final String VIEW_END_DATE_FULL = "view end date <End Date>";
    private final String VIEW_END_TIME = "view end time ";
    private final String VIEW_END_TIME_FULL = "view end time <End Time>";
    private final String VIEW_TAGS_HEADER = "view #<Tag>";
    private final String VIEW_TAGS_APPENDIX = " #<Tag>";
    private final String VIEW_CATEGORY = "view events | deadlines | tasks";
    private final String VIEW_TYPE = "view all | done | undone | overdue";
    private final String VIEW_TIME = "view start | end | from";

    private final String INVALID_COMMAND = "Invalid command";
    private final String INVALID_TAG = "Invalid Tag: view #<Tag>";
    private final String INVALID_FROM = "Invalid Time: view from <Start Date> to <End Date>";
    private final String INVALID_START_TIME = "Invalid time: view start time <Start Time>";
    private final String INVALID_START_DATE = "Invalid date: view start date <Start Date>";
    private final String INVALID_END_TIME = "Invalid time: view end time <End Time>";
    private final String INVALID_END_DATE = "Invalid date: view end date <End Date>";

    ArrayList<String> promptList = new ArrayList<>();

    public ArrayList<String> getPrompts(String command) {
        InputSeparator inputSeparator = new InputSeparator(command);
        InputSeparator.KeywordType type = inputSeparator.getKeywordType();
        String parameter = inputSeparator.getParameter();
        String lowerCaseCommand = command.toLowerCase();
        if (inputSeparator.getWordCount() == 1) {
            promptList.add(VIEW_DESCRIPTION);
            promptList.add(VIEW_CATEGORY);
            promptList.add(VIEW_TYPE);
            promptList.add(VIEW_TIME);
//            promptList.add(VIEW_TAGS_HEADER);
            return promptList;
        }
        if (VIEW_ALL.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_ALL);
        }
        if (VIEW_DONE.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_DONE);
        }
        if (VIEW_UNDONE.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_UNDONE);
        }
        if (VIEW_OVERDUE.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_OVERDUE);
        }
        if (VIEW_DEADLINE.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_DEADLINE);
        }
        if (VIEW_EVENT.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_EVENT);
        }
        if (VIEW_TASK.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_TASK);
        }
        if (VIEW_FROM.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_FROM_FULL);
        }
        if (VIEW_DESCRIPTION.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_DESCRIPTION);
        }
        if (VIEW_START_DATE.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_START_DATE_FULL);
        }
        if (VIEW_START_TIME.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_START_TIME_FULL);
        }
        if (VIEW_END_DATE.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_END_DATE_FULL);
        }
        if (VIEW_END_TIME.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_END_TIME_FULL);
        }
        boolean hasTags = checkTags(inputSeparator.getParameter(), inputSeparator.isEndWithSpace());
        if (type != null) {
            switch (type) {
                case FROM:
                    if (parameter != null) {
                        TimeParserResult result = new TimeParser().parseTime(parameter);
                        if (result.getMatchString() == null || !result.getMatchString().equals(parameter)) {
                            promptList.add(INVALID_FROM);
                        } else {
                            if (result.getFirstDate() != null && result.getSecondDate() != null) {
                                if (result.getFirstTime() == null && result.getSecondTime() == null) {
                                    promptList.add(VIEW_FROM_FULL);
                                }
                            } else {
                                promptList.add(INVALID_FROM);
                            }
                        }
                    }
                    break;
                case START_DATE:
                    if (parameter != null) {
                        TimeParserResult result = new TimeParser().parseTime(parameter);
                        if (result.getMatchString() == null || !result.getMatchString().equals(parameter)) {
                            promptList.add(INVALID_START_DATE);
                        } else {
                            if (result.getFirstDate() != null && result.getSecondDate() == null
                                    && result.getFirstTime() == null && result.getSecondTime() == null) {
                                promptList.add(VIEW_START_DATE_FULL);
                            } else {
                                promptList.add(INVALID_START_DATE);
                            }
                        }
                    }
                    break;
                case START_TIME:
                    if (parameter != null) {
                        TimeParserResult result = new TimeParser().parseTime(parameter);
                        if (result.getMatchString() == null || !result.getMatchString().equals(parameter)) {
                            promptList.add(INVALID_START_TIME);
                        } else {
                            if (result.getFirstDate() == null && result.getSecondDate() == null
                                    && result.getFirstTime() != null && result.getSecondTime() == null) {
                                promptList.add(VIEW_START_TIME_FULL);
                            } else {
                                promptList.add(INVALID_START_TIME);
                            }
                        }
                    }
                    break;
                case END_DATE:
                    if (parameter != null) {
                        TimeParserResult result = new TimeParser().parseTime(parameter);
                        if (result.getMatchString() == null || !result.getMatchString().equals(parameter)) {
                            promptList.add(INVALID_END_DATE);
                        } else {
                            if (result.getFirstDate() != null && result.getSecondDate() == null
                                    && result.getFirstTime() == null && result.getSecondTime() == null) {
                                promptList.add(VIEW_END_DATE_FULL);
                            } else {
                                promptList.add(INVALID_END_DATE);
                            }
                        }
                    }
                    break;
                case END_TIME:
                    if (parameter != null) {
                        TimeParserResult result = new TimeParser().parseTime(parameter);
                        if (result.getMatchString() == null || !result.getMatchString().equals(parameter)) {
                            promptList.add(INVALID_END_TIME);
                        } else {
                            if (result.getFirstDate() == null && result.getSecondDate() == null
                                    && result.getFirstTime() != null && result.getSecondTime() == null) {
                                promptList.add(VIEW_END_TIME_FULL);
                            } else {
                                promptList.add(INVALID_END_TIME);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        if (promptList.isEmpty()) {
            if (hasTags) {
                promptList.add(INVALID_COMMAND);
            } else {
                promptList.add(VIEW_DESCRIPTION_WITHOUT_KEYWORD);
            }
        }
        return promptList;
    }

    private boolean checkTags(String parameter, boolean endWithSpace) {
        if (parameter == null) {
            return false;
        }
        String[] possibleTags = parameter.split("\\s+");
        for (int i = 0; i < possibleTags.length; i++) {
            String possibleTag = possibleTags[i];
            if (!possibleTag.startsWith("#")) {
                if (i == 0) {
                    return false;
                }
                promptList.add(INVALID_TAG);
                return true;
            }
            if (possibleTag.equals("#")) {
                if (endWithSpace || i != possibleTags.length - 1) {
                    promptList.add(INVALID_TAG);
                    return true;
                }
            }
        }
        String tagPrompt = VIEW_TAGS_HEADER;
        int appendixCount = possibleTags.length - 1;
        if (endWithSpace) {
            appendixCount++;
        }
        for (int i = 0; i < appendixCount; i++) {
            tagPrompt += VIEW_TAGS_APPENDIX;
        }
        promptList.add(tagPrompt);
        return true;
    }

}
