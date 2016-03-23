package paser;

import logic.commands.Command;
import logic.commands.DoneCommand;
import logic.commands.InvalidCommand;

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
}
