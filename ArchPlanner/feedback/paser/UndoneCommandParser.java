package feedback.paser;

import logic.commands.Command;
import logic.commands.InvalidCommand;
import logic.commands.UndoneCommand;

/**
 * Created by lifengshuang on 3/15/16.
 */
public class UndoneCommandParser extends CommandParser {
    private final int UNDONE_ARGUMENT_INDEX = 7;

    @Override
    public Command parse(String input) {
        if (input.length() <= UNDONE_ARGUMENT_INDEX) {
            return new InvalidCommand("Undone index not found");
        }
        else {
            try {
                return new UndoneCommand(Integer.parseInt(input.substring(UNDONE_ARGUMENT_INDEX)));
            } catch (NumberFormatException e) {
                return new InvalidCommand("Undone index should be a number");
            }
        }
    }
}
