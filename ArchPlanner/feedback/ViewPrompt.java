package feedback;

import parser.InputSeparator;
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
    private final String VIEW_FROM_1 = "view from <Start Date> to <End Date>";
    private final String VIEW_FROM_2 = "view from <Start Date> <Start Time> to <End Date> <End Time>";
    private final String VIEW_TAGS_HEADER = "view #<Tag>";
    private final String VIEW_TAGS_APPENDIX = " #<Tag>";
    private final String VIEW_CATEGORY = "view events | deadlines | tasks";
    private final String VIEW_TYPE = "view all | done | undone | overdue";

    private final String INVALID_COMMAND = "Invalid command";
    private final String INVALID_TAG = "Invalid Tag: view #<Tag>";
    private final String INVALID_TIME_1 = "Invalid Time: view from <Start Date> to <End Date>";
    private final String INVALID_TIME_2 = "Invalid Time: view from <Start Date> <Start Time> to <End Date> <End Time>";

    ArrayList<String> promptList = new ArrayList<>();

    public ArrayList<String> getPrompts(String command) {
        InputSeparator inputSeparator = new InputSeparator(command);
        InputSeparator.KeywordType type = inputSeparator.getKeywordType();
        String lowerCaseCommand = command.toLowerCase();
        if (inputSeparator.getWordCount() == 1) {
            promptList.add(VIEW_DESCRIPTION);
            promptList.add(VIEW_CATEGORY);
            promptList.add(VIEW_TYPE);
            promptList.add(VIEW_TAGS_HEADER);
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
            promptList.add(VIEW_FROM_1);
            promptList.add(VIEW_FROM_2);
        }
        if (VIEW_DESCRIPTION.startsWith(lowerCaseCommand)) {
            promptList.add(VIEW_DESCRIPTION);
        }
        boolean hasTags = checkTags(inputSeparator.getParameter(), inputSeparator.isEndWithSpace());
        if (type == InputSeparator.KeywordType.FROM) {
            if (inputSeparator.getParameter() != null) {
                TimeParserResult result = new TimeParser().parseTime(inputSeparator.getParameter());
                if (result.getMatchString() == null || !result.getMatchString().equals(inputSeparator.getParameter())) {
                    promptList.add(INVALID_TIME_1);
                    promptList.add(INVALID_TIME_2);
                } else {
                    if (result.getFirstDate() != null && result.getSecondDate() != null) {
                        if (result.getFirstTime() == null && result.getSecondTime() == null) {
                            promptList.add(VIEW_FROM_1);
                        }
                        else if (result.getFirstTime() != null && result.getSecondTime() != null) {
                            promptList.add(VIEW_FROM_2);
                        }
                    } else {
                        promptList.add(INVALID_TIME_1);
                        promptList.add(INVALID_TIME_2);
                    }
                }
            } else {
                promptList.add(INVALID_TIME_1);
                promptList.add(INVALID_TIME_2);
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
