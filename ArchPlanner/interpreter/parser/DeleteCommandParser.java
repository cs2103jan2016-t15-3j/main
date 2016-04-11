package interpreter.parser;

import logic.commands.CommandInterface;
import logic.commands.DeleteCommand;
import logic.commands.InvalidCommand;
import interpreter.separator.InputSeparator;

/**
 * @@author A0149647N
 * DeleteCommandParser parse delete command with InputSeparator
 */
public class DeleteCommandParser {

    private static final String INVALID_ID = "Invalid: ID not found";
    private static final String INVALID_COMMAND = "Delete command is invalid!";
    private static final String INVALID_OUT_OF_RANGE = "Delete index out of range!";

    /**
     * Parse delete command with InputSeparator
     * @param input User's input
     * @param viewListSize Current view list's size
     * @return Parsed command object
     */
    public CommandInterface parse(String input, int viewListSize) {
        InputSeparator separator = new InputSeparator(input);
        if (separator.getID() == null) {
            return new InvalidCommand(INVALID_ID);
        }
        if (separator.isIdOnly()) {
            if (separator.isIdRangeValid(separator.getID(), viewListSize)) {
                return new DeleteCommand(separator.getID());
            } else {
                return new InvalidCommand(INVALID_OUT_OF_RANGE);
            }
        }
        if (separator.hasTwoValidId(viewListSize)) {
            return new DeleteCommand(separator.getID(), separator.getSecondId());
        }
        if (!separator.isIdRangeValid(separator.getSecondId(), viewListSize)) {
            return new InvalidCommand(INVALID_OUT_OF_RANGE);
        }
        return new InvalidCommand(INVALID_COMMAND);
    }
}
