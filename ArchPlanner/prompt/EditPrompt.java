package prompt;

import separator.InputSeparator;
import parser.time.TimeParser;
import parser.time.TimeParserResult;

import java.util.ArrayList;


/**
 * @@author A0149647N
 * EditPrompt return the prompts of edit command for the user input
 */
public class EditPrompt implements PromptInterface {

    private static final String EDIT_ID = "edit <Task ID>";
    private static final String EDIT_DESCRIPTION = "edit <Task ID> description";
    private static final String EDIT_DESCRIPTION_FULL = "edit <Task ID> description <Description>";
    private static final String EDIT_TAG = "edit <Task ID> #<Tag>";
    private static final String EDIT_TAG_APPENDIX = " #<Tag>";

    private static final String EDIT_TIME = "edit <Task ID> start | end | from";
    private static final String EDIT_START = "edit <Task ID> start";
    private static final String EDIT_START_REMOVE = "edit <Task ID> start remove";
    private static final String EDIT_START_10 = "edit <Task ID> start <Date>";
    private static final String EDIT_START_01 = "edit <Task ID> start <Time>";
    private static final String EDIT_START_11 = "edit <Task ID> start <Date> <Time>";
    private static final String EDIT_END = "edit <Task ID> end";
    private static final String EDIT_END_REMOVE = "edit <Task ID> end remove";
    private static final String EDIT_END_10 = "edit <Task ID> end <Date>";
    private static final String EDIT_END_01 = "edit <Task ID> end <Time>";
    private static final String EDIT_END_11 = "edit <Task ID> end <Date> <Time>";

    private static final String EDIT_FROM = "edit <Task ID> from";
    private static final String EDIT_FROM_1010 = "edit <Task ID> from <Start Date> to <End Date>";
    private static final String EDIT_FROM_0101 = "edit <Task ID> from <Start Time> to <End Time>";
    private static final String EDIT_FROM_0110 = "edit <Task ID> from <Start Time> to <End Date>";
    private static final String EDIT_FROM_1011 = "edit <Task ID> from <Start Date> to <End Date> <End Time>";
    private static final String EDIT_FROM_0111 = "edit <Task ID> from <Start Time> to <End Date> <End Time>";
    private static final String EDIT_FROM_1110 = "edit <Task ID> from <Start Date> <Start Time> to <End Date>";
    private static final String EDIT_FROM_1101 = "edit <Task ID> from <Start Date> <Start Time> to <End Time>";
    private static final String EDIT_FROM_1111 = "edit <Task ID> from <Start Date> <Start Time> to <End Date> <End Time>";

    private static final String REMOVE = "remove";
    private static final String REMOVE_START = "edit <Task ID> start remove";
    private static final String REMOVE_END = "edit <Task ID> end remove";
    private static final String REMOVE_TAG = "edit <Task ID> # remove";

    private static final String INVALID_ID = "Invalid ID: edit <Task ID>";
    private static final String INVALID_TIME = "Invalid Time";
    private static final String INVALID_COMMAND = "Invalid Edit Command";
    private static final String INVALID_START = "Invalid Time: edit <Task ID> start <Date> <Time>";
    private static final String INVALID_END = "Invalid Time: edit <Task ID> end <Date> <Time>";
    private static final String INVALID_TAG = "Invalid tag: edit <Task ID> #<Tag>";

    private static final String KEYWORD_DESCRIPTION = "description";
    private static final String KEYWORD_START_REMOVE = "start remove";
    private static final String KEYWORD_START = "start";
    private static final String KEYWORD_END = "end";
    private static final String KEYWORD_END_REMOVE = "end remove";
    private static final String KEYWORD_FROM = "from";
    private static final String KEYWORD_TAG_REMOVE = "# remove";

    private static final String TAG_NOTATION = "#";
    private static final String EMPTY_STRING = "";
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    private static final int MINIMUM_INDEX = 1;
    private static final int ONE_WORD = 1;

    ArrayList<String> promptList = new ArrayList<>();
    InputSeparator inputSeparator;

    /**
     * Get prompts of edit command
     */
    @Override
    public ArrayList<String> getPrompts(String userInput) {

        this.inputSeparator = new InputSeparator(userInput);
        int wordCount = inputSeparator.getWordCount();
        Integer id = inputSeparator.getID();
        String parameter = inputSeparator.getParameter();
        InputSeparator.KeywordType keyword = inputSeparator.getKeywordType();
        boolean endWithSpace = inputSeparator.isEndWithSpace();

        if (id == null) {
            if (wordCount == ONE_WORD) {
                promptList.add(EDIT_ID);
            } else {
                promptList.add(INVALID_ID);
            }
            return promptList;
        }
        if (id < MINIMUM_INDEX) {
            promptList.add(INVALID_ID);
            return promptList;
        }

        if (keyword == null) {
            handleNoKeywordCase(parameter, endWithSpace);
        } else {
            handleKeywordWithParameter(keyword, parameter);
        }
        if (promptList.isEmpty()) {
            promptList.add(INVALID_COMMAND);
        }
        return promptList;
    }

    private void handleNoKeywordCase(String parameter, boolean endWithSpace) {
        if (parameter == null) {
            promptList.add(EDIT_DESCRIPTION);
            promptList.add(EDIT_TIME);
            promptList.add(EDIT_TAG);
        } else {

            boolean hasTags = checkTags(parameter, endWithSpace);

            if (!hasTags) {
                if (KEYWORD_DESCRIPTION.startsWith(parameter)) {
                    promptList.add(EDIT_DESCRIPTION);
                }
                if (KEYWORD_START.startsWith(parameter)) {
                    promptList.add(EDIT_START);
                }
                if (KEYWORD_END.startsWith(parameter)) {
                    promptList.add(EDIT_END);
                }
                if (KEYWORD_START_REMOVE.startsWith(parameter)) {
                    promptList.add(REMOVE_START);
                }
                if (KEYWORD_END_REMOVE.startsWith(parameter)) {
                    promptList.add(REMOVE_END);
                }
                if (KEYWORD_FROM.startsWith(parameter)) {
                    promptList.add(EDIT_FROM);
                }
            } else if (KEYWORD_TAG_REMOVE.startsWith(parameter)) {
                promptList.add(REMOVE_TAG);
            } else if (promptList.isEmpty()) {
                promptList.add(INVALID_TAG);
            }
        }
    }

    private void handleKeywordWithParameter(InputSeparator.KeywordType keyword, String parameter) {
        TimeParserResult result = new TimeParser().parseTime(parameter);
        switch (keyword) {
            case DESCRIPTION:
                promptList.add(EDIT_DESCRIPTION_FULL);
                break;
            case START:
                handleStart(parameter, result);
                break;
            case END:
                handleEnd(parameter, result);
                break;
            case FROM:
                handleFrom(result);
            default:
                break;
        }
    }

    @Override
    public String getAutoWord() {
        if (inputSeparator.getID() != null) {
            if (inputSeparator.getKeywordType() == InputSeparator.KeywordType.START
                    || inputSeparator.getKeywordType() == InputSeparator.KeywordType.END) {
                String remove = REMOVE;
                if (remove.startsWith(inputSeparator.getParameter())) {
                    return remove;
                }
            }
            return inputSeparator.getPartialKeyword();
        }
        return EMPTY_STRING;
    }

    private void handleStart(String parameter, TimeParserResult result) {
        if (parameter == null) {
            promptList.add(EDIT_START_10);
            promptList.add(EDIT_START_01);
            promptList.add(EDIT_START_11);
            promptList.add(EDIT_START_REMOVE);
        } else if (isToRemove(parameter)) {
            promptList.add(EDIT_START_REMOVE);
        } else if (isTimeInvalid(result)) {
            promptList.add(INVALID_TIME);
        } else {
            switch (result.getDateTimeStatus()) {
                case NONE:
                    promptList.add(EDIT_START_10);
                    promptList.add(EDIT_START_01);
                    promptList.add(EDIT_START_11);
                    break;
                case START_TIME:
                    promptList.add(EDIT_START_01);
                    break;
                case START_DATE:
                    promptList.add(EDIT_START_10);
                    promptList.add(EDIT_START_11);
                    break;
                case START_DATE_START_TIME:
                    promptList.add(EDIT_START_11);
                    break;
                default:
                    promptList.add(INVALID_START);
            }
        }
    }

    private void handleEnd(String parameter, TimeParserResult result) {
        if (parameter == null) {
            promptList.add(EDIT_END_10);
            promptList.add(EDIT_END_01);
            promptList.add(EDIT_END_11);
            promptList.add(EDIT_END_REMOVE);
        } else if (isToRemove(parameter)) {
            promptList.add(EDIT_END_REMOVE);
        } else if (isTimeInvalid(result)) {
            promptList.add(INVALID_TIME);
        } else {
            switch (result.getDateTimeStatus()) {
                case NONE:
                    promptList.add(EDIT_END_10);
                    promptList.add(EDIT_END_01);
                    promptList.add(EDIT_END_11);
                    break;
                case START_TIME:
                    promptList.add(EDIT_END_01);
                    break;
                case START_DATE:
                    promptList.add(EDIT_END_10);
                    promptList.add(EDIT_END_11);
                    break;
                case START_DATE_START_TIME:
                    promptList.add(EDIT_END_11);
                    break;
                default:
                    promptList.add(INVALID_END);
            }
        }
    }

    private void handleFrom(TimeParserResult result) {
        switch (result.getRawDateTimeStatus()) {
            case NONE:
                promptList.add(EDIT_FROM_1010);
                promptList.add(EDIT_FROM_0101);
                promptList.add(EDIT_FROM_1111);
                break;
            case START_TIME:
                promptList.add(EDIT_FROM_0101);
                promptList.add(EDIT_FROM_0110);
                promptList.add(EDIT_FROM_0111);
                break;
            case START_TIME_END_TIME:
                promptList.add(EDIT_FROM_0101);
                break;
            case START_TIME_END_DATE:
                promptList.add(EDIT_FROM_0110);
                promptList.add(EDIT_FROM_0111);
                break;
            case START_TIME_END_DATE_END_TIME:
                promptList.add(EDIT_FROM_0111);
                break;
            case START_DATE:
                promptList.add(EDIT_FROM_1010);
                promptList.add(EDIT_FROM_1111);
                break;
            case START_DATE_END_DATE:
                promptList.add(EDIT_FROM_1010);
                promptList.add(EDIT_FROM_1011);
                break;
            case START_DATE_END_DATE_END_TIME:
                promptList.add(EDIT_FROM_1011);
                break;
            case START_DATE_START_TIME:
                promptList.add(EDIT_FROM_1101);
                promptList.add(EDIT_FROM_1111);
                break;
            case START_DATE_START_TIME_END_TIME:
                promptList.add(EDIT_FROM_1101);
                break;
            case START_DATE_START_TIME_END_DATE:
                promptList.add(EDIT_FROM_1111);
                promptList.add(EDIT_FROM_1110);
                break;
            case START_DATE_START_TIME_END_DATE_END_TIME:
                promptList.add(EDIT_FROM_1111);
                break;
        }
    }

    private boolean checkTags(String parameter, boolean endWithSpace) {
        if (parameter == null) {
            return false;
        }
        String[] possibleTags = parameter.split(STRING_MULTIPLE_WHITESPACE);
        for (int i = 0; i < possibleTags.length; i++) {
            String possibleTag = possibleTags[i];
            if (!possibleTag.startsWith(TAG_NOTATION)) {
                return i != 0;
            }
            if (possibleTag.equals(TAG_NOTATION)) {
                if (endWithSpace || i != possibleTags.length - 1) {
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


    private boolean isToRemove(String parameter) {
        if (parameter != null) {
            String remove = REMOVE;
            return remove.startsWith(parameter.toLowerCase());
        }
        return true;
    }

    private boolean isTimeInvalid(TimeParserResult timeParserResult) {
        String parameter = inputSeparator.getParameter();
        if (parameter != null) {
            int parameterCount = parameter.split(STRING_MULTIPLE_WHITESPACE).length;
            if (timeParserResult.getMatchString() == null) {
                return parameterCount > ONE_WORD;
            }
            int timeCount = timeParserResult.getMatchString().split(STRING_MULTIPLE_WHITESPACE).length;
            if (parameterCount - timeCount > ONE_WORD) {
                return true;
            }
        }
        return false;
    }

}
