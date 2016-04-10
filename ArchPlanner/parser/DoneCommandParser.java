package parser;

import logic.commands.CommandInterface;
import logic.commands.DoneCommand;
import logic.commands.InvalidCommand;
import separator.InputSeparator;

/**
 * @@author A0149647N
 * DoneCommandParser parse done command with InputSeparator
 */
public class DoneCommandParser {

    private static final String INVALID_ID = "Invalid: ID not found";
    private static final String INVALID_COMMAND = "Done command is invalid!";
    private static final String INVALID_OUT_OF_RANGE = "Done index out of range!";

    /**
     * Parse delete command with InputSeparator
     * @return Parsed command object
     */
    public CommandInterface parse(String input, int viewListSize) {
        InputSeparator separator = new InputSeparator(input);
        if (separator.getID() == null) {
            return new InvalidCommand(INVALID_ID);
        }
        if (separator.isIdOnly()) {
            if (separator.isIdRangeValid(separator.getID(), viewListSize)) {
                return new DoneCommand(separator.getID());
            } else {
                return new InvalidCommand(INVALID_OUT_OF_RANGE);
            }
        }
        if (separator.hasTwoValidId(viewListSize)) {
            return new DoneCommand(separator.getID(), separator.getSecondId());
        }
        if (!separator.isIdRangeValid(separator.getSecondId(), viewListSize)) {
            return new InvalidCommand(INVALID_OUT_OF_RANGE);
        }
        return new InvalidCommand(INVALID_COMMAND);
    }
}
