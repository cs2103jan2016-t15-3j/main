package parser;

import logic.TaskParameters;
import logic.commands.AddCommand;
import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import parser.time.TimeParserResult;
import separator.AddInputSeparator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @@author A0149647N
 * AddCommandParser parse add command with AddInputSeparator
 */
public class AddCommandParser {

    private static final int ADD_PARAMETER_INDEX = 4;
    private static final String INVALID_TIME = "Invalid Time!";
    private static final String INVALID_TAG = "Invalid Tags";

    TaskParameters result = new TaskParameters();

    /**
     * Parse add command with AddInputSeparator
     * @return Parsed command object
     */
    public CommandInterface parse(String input) {
        AddInputSeparator addInputSeparator = new AddInputSeparator(input.substring(ADD_PARAMETER_INDEX));
        if (addInputSeparator.hasDescription()) {
            result.setDescription(addInputSeparator.getDescription());
            TimeParserResult timeParserResult = getDateTime(addInputSeparator);
            result.setStartDate(timeParserResult.getFirstDate());
            result.setStartTime(timeParserResult.getFirstTime());
            result.setEndDate(timeParserResult.getSecondDate());
            result.setEndTime(timeParserResult.getSecondTime());
            result.setTagsList(new ArrayList<>());
            if (!checkTimeValidWithKeyword(addInputSeparator.getKeyWord(), timeParserResult)){
                return new InvalidCommand(INVALID_TIME);
            }
            if (timeParserResult.getMatchString() != null && !timeParserResult.isTimeValid()) {
                return new InvalidCommand(INVALID_TIME);
            }
            if (addInputSeparator.hasValidTag()) {
                if (addInputSeparator.getTags().length == 1 && addInputSeparator.getTags()[0].equals("#")) {
                    return new InvalidCommand(INVALID_TAG);
                } else {
                    ArrayList<String> arrayList = new ArrayList<>();
                    Collections.addAll(arrayList, addInputSeparator.getTags());
                    result.setTagsList(arrayList);
                }
            } else if (addInputSeparator.hasTag()) {
                return new InvalidCommand(INVALID_TAG);
            }
        }
        return new AddCommand(result);
    }

    private TimeParserResult getDateTime(AddInputSeparator addInputSeparator) {
        TimeParserResult timeParserResult = new TimeParserResult();
        if (addInputSeparator.hasStartDate()) {
            timeParserResult.setFirstDate(addInputSeparator.getStartDate());
        }
        if (addInputSeparator.hasStartTime()) {
            timeParserResult.setFirstTime(addInputSeparator.getStartTime());
        }
        if (addInputSeparator.hasEndDate()) {
            timeParserResult.setSecondDate(addInputSeparator.getEndDate());
        }
        if (addInputSeparator.hasEndTime()) {
            timeParserResult.setSecondTime(addInputSeparator.getEndTime());
        }
        timeParserResult.updateDateTime();
        if (timeParserResult.getFirstTime() != null && timeParserResult.getFirstDate() == null) {
            timeParserResult.setFirstDate(LocalDate.now());
        }
        if (timeParserResult.getSecondTime() != null && timeParserResult.getSecondDate() == null) {
            timeParserResult.setSecondDate(LocalDate.now());
        }
        return timeParserResult;
    }

    private boolean checkTimeValidWithKeyword(AddInputSeparator.AddKeyWordType keyword, TimeParserResult timeParserResult) {
        if (keyword == null) {
            return true;
        }
        switch (keyword) {
            case UNKNOWN:
                return true;
            case ON:
                if (timeParserResult.getFirstDate() == null || timeParserResult.getSecondDate() != null) {
                    return false;
                }
                break;
            case BY:
                if (timeParserResult.getFirstDate() != null || timeParserResult.getSecondDate() == null) {
                    return false;
                }
                break;
            case FROM:
                if (timeParserResult.getFirstDate() == null || timeParserResult.getSecondDate() == null) {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

}