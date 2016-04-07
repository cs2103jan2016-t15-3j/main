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

    private final int DONE_ARGUMENT_INDEX = 5;

    public CommandInterface parse(String input, int viewListSize) {
        if (input.length() <= DONE_ARGUMENT_INDEX) {
            return new InvalidCommand("Done index not found");
        }
        else {
            InputSeparator separator = new InputSeparator(input);
            if (separator.isIdOnly()) {
                if (separator.isIdRangeValid(separator.getID(), viewListSize)) {
                    return new DoneCommand(separator.getID());
                } else {
                    return new InvalidCommand("Done index out of range!");
                }
            }
            if (separator.hasTwoValidId(viewListSize)) {
                return new DoneCommand(separator.getID(), separator.getSecondId());
            }
            if (!separator.isIdRangeValid(separator.getSecondId(), viewListSize)) {
                return new InvalidCommand("Done index out of range!");
            }
            return new InvalidCommand("Done command is invalid!");
        }
    }
}
