package feedback;

import separator.InputSeparator;
import parser.time.TimeParser;
import parser.time.TimeParserResult;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 3/29/16.
 */
public class EditPrompt {

    private final String EDIT_ID = "edit <Task ID>";
    private final String EDIT_DESCRIPTION = "edit <Task ID> description";
    private final String EDIT_DESCRIPTION_FULL = "edit <Task ID> description <Description>";
    private final String EDIT_START = "edit <Task ID> start";
    private final String EDIT_START_DATE = "edit <Task ID> start date";
    private final String EDIT_START_DATE_FULL = "edit <Task ID> start date <Start Date>";
    private final String EDIT_START_TIME = "edit <Task ID> start time";
    private final String EDIT_START_TIME_FULL = "edit <Task ID> start time <Start Time>";
    private final String EDIT_END = "edit <Task ID> end";
    private final String EDIT_END_DATE = "edit <Task ID> end date";
    private final String EDIT_END_DATE_FULL = "edit <Task ID> end date <End Date>";
    private final String EDIT_END_TIME = "edit <Task ID> end time";
    private final String EDIT_END_TIME_FULL = "edit <Task ID> end time <End Time>";
    private final String EDIT_TAG = "edit <Task ID> #<Tag>";
    private final String EDIT_TAG_APPENDIX = " #<Tag>";

    private final String REMOVE_START = "edit <Task ID> start remove";
    private final String REMOVE_START_TIME = "edit <Task ID> start time remove";
    private final String REMOVE_START_DATE = "edit <Task ID> start date remove";
    private final String REMOVE_END = "edit <Task ID> end remove";
    private final String REMOVE_END_TIME = "edit <Task ID> end time remove";
    private final String REMOVE_END_DATE = "edit <Task ID> end date remove";
    private final String REMOVE_TAG = "edit <Task ID> # remove";

    private final String INVALID_ID = "Invalid ID: edit <Task ID>";
    private final String INVALID_START_TIME = "Invalid time: edit <Task ID> start time <Start Time>";
    private final String INVALID_START_DATE = "Invalid date: edit <Task ID> start date <Start Date>";
    private final String INVALID_END_TIME = "Invalid time: edit <Task ID> end time <End Time>";
    private final String INVALID_END_DATE = "Invalid date: edit <Task ID> end date <End Date>";
    private final String INVALID_TAG = "Invalid tag: edit <Task ID> #<Tag>";
    private final String INVALID_KEYWORD = "Invalid keyword";

    private final String KEYWORD_DESCRIPTION = "description";
    private final String KEYWORD_START_REMOVE = "start remove";
    private final String KEYWORD_START_TIME = "start time";
    private final String KEYWORD_START_DATE = "start date";
    private final String KEYWORD_END_REMOVE = "end remove";
    private final String KEYWORD_END_TIME = "end time";
    private final String KEYWORD_END_DATE = "end date";
    private final String KEYWORD_TAG_REMOVE = "# remove";


    ArrayList<String> promptList = new ArrayList<>();

    public ArrayList<String> getPrompts(String userInput) {

        InputSeparator inputSeparator = new InputSeparator(userInput);
        int wordCount = inputSeparator.getWordCount();
        Integer id = inputSeparator.getID();
        String parameter = inputSeparator.getParameter();
        InputSeparator.KeywordType keyword = inputSeparator.getKeywordType();
        boolean endWithSpace = inputSeparator.isEndWithSpace();

        //check id
        if (id == null) {
            if (wordCount == 1) {
                promptList.add(EDIT_ID);
            } else {
                promptList.add(INVALID_ID);
            }
            return promptList;
        }
        if (id <= 0) {
            promptList.add(INVALID_ID);
            return promptList;
        }

        if (keyword == null) {
            if (parameter == null) {
                promptList.add(EDIT_DESCRIPTION);
                promptList.add(EDIT_START);
                promptList.add(EDIT_END);
                promptList.add(EDIT_TAG);
            } else {
                //check tags

                boolean hasTags = checkTags(parameter, endWithSpace);

                if (!hasTags) {
                    if (KEYWORD_DESCRIPTION.startsWith(parameter)) {
                        promptList.add(EDIT_DESCRIPTION);
                    }
                    if (KEYWORD_START_DATE.startsWith(parameter)) {
                        promptList.add(EDIT_START_DATE);
                    }
                    if (KEYWORD_START_TIME.startsWith(parameter)) {
                        promptList.add(EDIT_START_TIME);
                    }
                    if (KEYWORD_END_DATE.startsWith(parameter)) {
                        promptList.add(EDIT_END_DATE);
                    }
                    if (KEYWORD_END_TIME.startsWith(parameter)) {
                        promptList.add(EDIT_END_TIME);
                    }
                    if (KEYWORD_START_REMOVE.startsWith(parameter)) {
                        promptList.add(REMOVE_START);
                    }
                    if (KEYWORD_END_REMOVE.startsWith(parameter)) {
                        promptList.add(REMOVE_END);
                    }
                }
                if (KEYWORD_TAG_REMOVE.startsWith(parameter)) {
                    promptList.add(REMOVE_TAG);
                }
            }
        } else {
            TimeParserResult result = new TimeParser().parseTime(parameter);
            switch (keyword) {
                case DESCRIPTION:
                    promptList.add(EDIT_DESCRIPTION_FULL);
                    break;
                case START_DATE:
                    if (isValidDate(result, parameter)) {
                        promptList.add(EDIT_START_DATE_FULL);
                        promptList.add(REMOVE_START_DATE);
                    } else {
                        promptList.add(INVALID_START_DATE);
                    }
                    break;
                case START_TIME:
                    if (isValidTime(result, parameter)) {
                        promptList.add(EDIT_START_TIME_FULL);
                        promptList.add(REMOVE_START_TIME);
                    } else {
                        promptList.add(INVALID_START_TIME);
                    }

                    break;
                case END_DATE:
                    if (isValidDate(result, parameter)) {
                        promptList.add(EDIT_END_DATE_FULL);
                        promptList.add(REMOVE_END_DATE);
                    } else {
                        promptList.add(INVALID_END_DATE);
                    }
                    break;
                case END_TIME:
                    if (isValidTime(result, parameter)) {
                        promptList.add(EDIT_END_TIME_FULL);
                        promptList.add(REMOVE_END_TIME);
                    } else {
                        promptList.add(INVALID_END_TIME);
                    }
                    break;
                default:
                    break;
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
        String tagPrompt = EDIT_TAG;
        int appendixCount = possibleTags.length - 1;
        if (endWithSpace) {
            appendixCount++;
        }
        for (int i = 0; i < appendixCount; i++) {
            tagPrompt += EDIT_TAG_APPENDIX;
        }
        promptList.add(tagPrompt);
        return true;
    }

    private boolean isValidDate(TimeParserResult result, String parameter) {
        if (parameter == null) {
            return true;
        }
        if (result.getMatchString() == null) {
            return false;
        }
        boolean hasOneDate = result.getFirstDate() != null && result.getSecondDate() == null;
        boolean hasNoTime = result.getFirstTime() == null;
        boolean stringMatch = result.getMatchString().equals(parameter);
        return hasOneDate && hasNoTime && stringMatch;
    }

    private boolean isValidTime(TimeParserResult result, String parameter) {
        if (parameter == null) {
            return true;
        }
        if (result.getMatchString() == null) {
            return false;
        }
        boolean hasNoDate = result.getFirstDate() == null;
        boolean hasOneTime = result.getFirstTime() != null && result.getSecondTime() == null;
        boolean stringMatch = result.getMatchString().equals(parameter);
        return hasNoDate && hasOneTime && stringMatch;
    }
}
