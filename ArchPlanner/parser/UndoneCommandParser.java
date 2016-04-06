package parser;

import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import logic.commands.UndoneCommand;
import separator.InputSeparator;

/**
 * Created by lifengshuang on 3/15/16.
 */
public class UndoneCommandParser extends CommandParser {
    private final int UNDONE_ARGUMENT_INDEX = 7;

//    public Command parse(String input) {
//        if (input.length() <= UNDONE_ARGUMENT_INDEX) {
//            return new InvalidCommand("Undone index not found");
//        }
//        else {
//            try {
//                return new UndoneCommand(Integer.parseInt(input.substring(UNDONE_ARGUMENT_INDEX)));
//            } catch (NumberFormatException e) {
//                return new InvalidCommand("Undone index should be a number");
//            }
//        }
//    }

    public CommandInterface parse(String input, int viewListSize) {
        if (input.length() <= UNDONE_ARGUMENT_INDEX) {
            return new InvalidCommand("Undone index not found");
        }
        else {
            InputSeparator separator = new InputSeparator(input);
            if (separator.isIdOnly()) {
                if (separator.isIdRangeValid(separator.getID(), viewListSize)) {
                    return new UndoneCommand(separator.getID());
                } else {
                    return new InvalidCommand("Undone index out of range!");
                }
            }
            if (separator.hasTwoValidId(viewListSize)) {
                return new UndoneCommand(separator.getID(), separator.getSecondId());
            }
            return new InvalidCommand("Undone command is invalid!");
        }
    }
}
