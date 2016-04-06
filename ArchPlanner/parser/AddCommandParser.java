package parser;

import logic.TaskParameters;
import logic.commands.AddCommand;
import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import parser.time.TimeParserResult;
import separator.AddInputSeparator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class AddCommandParser extends CommandParser {

    private final int ADD_PARAMETER_INDEX = 4;
    TaskParameters result = new TaskParameters();

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
                return new InvalidCommand("Invalid Time!");
            }
            if (timeParserResult.getMatchString() != null && !timeParserResult.isTimeValid()) {
                return new InvalidCommand("Invalid Time!");
            }
            if (addInputSeparator.hasValidTag()) {
                if (addInputSeparator.getTags().length == 1 && addInputSeparator.getTags()[0].equals("#")) {
                    return new InvalidCommand("Invalid Tags");
                } else {
                    ArrayList<String> arrayList = new ArrayList<>();
                    Collections.addAll(arrayList, addInputSeparator.getTags());
                    result.setTagsList(arrayList);
                }
            } else if (addInputSeparator.hasTag()) {
                return new InvalidCommand("Invalid Tags");
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
        }
        return true;
    }

}