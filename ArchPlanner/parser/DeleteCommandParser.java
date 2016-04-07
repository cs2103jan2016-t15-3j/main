package parser;

import logic.commands.CommandInterface;
import logic.commands.DeleteCommand;
import logic.commands.InvalidCommand;
import separator.InputSeparator;

/**
 * @@author A0149647N
 * DeleteCommandParser parse delete command with InputSeparator
 */
public class DeleteCommandParser {

    private final int DELETE_ARGUMENT_INDEX = 7;


    public CommandInterface parse(String input, int viewListSize) {
        if (input.length() <= DELETE_ARGUMENT_INDEX) {
            return new InvalidCommand("Delete index not found");
        } else {
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
            if (!separator.isIdRangeValid(separator.getSecondId(), viewListSize)) {
                return new InvalidCommand("Delete index out of range!");
            }

            return new InvalidCommand("Delete command is invalid!");
        }
    }
}
