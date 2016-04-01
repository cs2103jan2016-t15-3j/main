package parser;

import logic.commands.Command;
import logic.commands.DoneCommand;
import logic.commands.InvalidCommand;
import separator.InputSeparator;

/**
 * Created by lifengshuang on 3/15/16.
 */
public class DoneCommandParser extends CommandParser {

    private final int DONE_ARGUMENT_INDEX = 5;

    @Override
    public Command parse(String input) {
        if (input.length() <= DONE_ARGUMENT_INDEX) {
            return new InvalidCommand("Done index not found");
        }
        else {
            try {
                return new DoneCommand(Integer.parseInt(input.substring(DONE_ARGUMENT_INDEX)));
            } catch (NumberFormatException e) {
                return new InvalidCommand("Done index should be a number");
            }
        }
    }

    public Command parse(String input, int viewListSize) {
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
            return new InvalidCommand("Done command is invalid!");
        }
    }
}
