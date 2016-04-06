package parser;

import logic.commands.CommandInterface;
import logic.commands.DeleteCommand;
import logic.commands.InvalidCommand;
import separator.InputSeparator;

/**
 * Created by lifengshuang on 3/15/16.
 */
public class DeleteCommandParser extends CommandParser {

    private final int DELETE_ARGUMENT_INDEX = 7;

//    public Command parse(String input) {
//        if (input.length() <= DELETE_ARGUMENT_INDEX) {
//            return new InvalidCommand("Delete index not found");
//        }
//        else {
//            try {
//                return new DeleteCommand(Integer.parseInt(input.substring(DELETE_ARGUMENT_INDEX)));
//            } catch (NumberFormatException e) {
//                return new InvalidCommand("Delete index should be a number");
//            }
//        }
//    }

    public CommandInterface parse(String input, int viewListSize) {
        if (input.length() <= DELETE_ARGUMENT_INDEX) {
            return new InvalidCommand("Delete index not found");
        }
        else {
            InputSeparator separator = new InputSeparator(input);
            if (separator.isIdOnly()) {
                if (separator.isIdRangeValid(separator.getID(), viewListSize)) {
                    return new DeleteCommand(separator.getID());
                } else {
                    return new InvalidCommand("Delete index out of range!");
                }
            }
            if (separator.hasTwoValidId(viewListSize)) {
                return new DeleteCommand(separator.getID(), separator.getSecondId());
            }
            return new InvalidCommand("Delete command is invalid!");
        }
    }
}
