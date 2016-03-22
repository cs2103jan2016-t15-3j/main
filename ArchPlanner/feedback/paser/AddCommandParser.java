package feedback.paser;

import feedback.paser.time.TimeParser;
import feedback.paser.time.TimeParserResult;
import logic.TaskParameters;
import logic.commands.AddCommand;
import logic.commands.Command;
import logic.commands.InvalidCommand;

import java.time.LocalDate;
import java.util.Calendar;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class AddCommandParser extends CommandParser {

    //add sth from 07/05/16 09:07 AM to 08/06/16 7:7 PM #my tag
    //add finish this project by 04/15/16 0:0 AM #ArchPlanner
    //add enjoy summer holiday from 05/05/16 0:0 PM #Holiday!!!
    private Calendar startDate;
    private Calendar endDate;
    //add sth from 1pm to 3pm
    //add lalala from tomorrow to this Friday #a
    //add miao by Mar 7 #a #b
    //add assignments this Thursday 3pm #assign #oh-my-god #help-me!
    private final int TIME_TYPE_HAS_START_DATE = 0;
    private final int TIME_TYPE_ONLY_END_DATE = 1;
    private final int ADD_PARAMETER_INDEX = 4;
    private final String KEYWORD_FROM = "from";
    private final String KEYWORD_BY = "by";
    private final String KEYWORD_ON = "on";
    TaskParameters result = new TaskParameters();

    @Override
    public Command parse(String input) {
        this.input = input;
        int lastKeywordOccurrence = getLastOccurrenceOfKeyword(input);
        if (lastKeywordOccurrence > 0) {
            String beforeKeyword = input.substring(0, lastKeywordOccurrence);
            String afterKeyword = input.substring(lastKeywordOccurrence);
            String tagRemoved = parseTag(afterKeyword);
            if (tagRemoved == null) {
                return new InvalidCommand("Empty tag is not allowed");
            }
            this.input = beforeKeyword + tagRemoved;
        }
        this.input = parseTime(this.input);
        String description = this.input.substring(ADD_PARAMETER_INDEX);
        result.setDescription(description);
        result.setTagsList(tagList);
        return new AddCommand(result);
    }

    private String parseTime(String input) {
        TimeParserResult timeResult = new TimeParser().parseTime(input);
        String keyword = detectTimeKeyword(timeResult.getMatchPosition());
        switch (keyword) {
            case KEYWORD_ON:
                if (timeResult.getFirstDate() == null) {
                    result.setStartDate(LocalDate.now());
                } else {
                    result.setStartDate(timeResult.getFirstDate());
                }
                result.setStartTime(timeResult.getFirstTime());
                break;
            case KEYWORD_BY:
                if (timeResult.getFirstDate() == null) {
                    result.setEndDate(LocalDate.now());
                } else {
                    result.setEndDate(timeResult.getFirstDate());
                }
                result.setEndTime(timeResult.getFirstTime());
                break;
            case KEYWORD_FROM:
                if (timeResult.getFirstDate() != null) {
                    if (timeResult.getSecondDate() == null && timeResult.getSecondTime() == null) {
                        keyword = "";
                        break;
                    }
                    result.setStartDate(timeResult.getFirstDate());
                    if (timeResult.getSecondDate() == null) {
                        result.setEndDate(timeResult.getFirstDate());
                    } else {
                        result.setEndDate(timeResult.getSecondDate());
                    }
                } else if (timeResult.getSecondTime() == null) {
                    keyword = "";
                    break;
                }
                result.setStartDate(LocalDate.now());
                result.setEndDate(LocalDate.now());
                result.setStartTime(timeResult.getFirstTime());
                result.setEndTime(timeResult.getSecondTime());
                break;
            default:
                break;
        }
        input = removeTimeString(input, timeResult.getMatchPosition(), keyword);
        return input;
    }


    private String removeTimeString(String input, int matchingPosition, String keyword) {
        if (keyword.isEmpty()) {
            return input;
        }
        return input.substring(0, matchingPosition - keyword.length() - 3);
    }

    private String detectTimeKeyword(int matchPosition) {
        int lastKeywordOccurrence = getLastOccurrenceOfKeyword(input) + 2;
        if (hasPrefixWithKeyword(KEYWORD_BY, matchPosition)
                && lastKeywordOccurrence + KEYWORD_BY.length() == matchPosition) {
            return KEYWORD_BY;
        }
        if (hasPrefixWithKeyword(KEYWORD_ON, matchPosition)
                && lastKeywordOccurrence + KEYWORD_ON.length() == matchPosition) {
            return KEYWORD_ON;
        }
        if (hasPrefixWithKeyword(KEYWORD_FROM, matchPosition)
                && lastKeywordOccurrence + KEYWORD_FROM.length() == matchPosition) {
            return KEYWORD_FROM;
        }
        return "";
    }

    private boolean hasPrefixWithKeyword(String keyword, int index) {
        if (index > keyword.length() + 2) {
            int startIndex = index - keyword.length() - 3;
            if (input.substring(startIndex, index - 1).equals(" " + keyword + " ")) {
                return true;
            }
        }
        return false;
    }

    private int getLastOccurrenceOfKeyword(String input) {
        return Math.max(
                Math.max(input.lastIndexOf(KEYWORD_BY), input.lastIndexOf(KEYWORD_FROM)),
                input.lastIndexOf(KEYWORD_ON));
    }

}