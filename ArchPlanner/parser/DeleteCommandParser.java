package parser;

import logic.commands.Command;
import logic.commands.DeleteCommand;
import logic.commands.InvalidCommand;

/**
 * Created by lifengshuang on 3/15/16.
 */
public class DeleteCommandParser extends CommandParser {

    private final int DELETE_ARGUMENT_INDEX = 7;

    @Override
    public Command parse(String input) {
        if (input.length() <= DELETE_ARGUMENT_INDEX) {
            return new InvalidCommand("Delete index not found");
        }
        else {
            try {
                return new DeleteCommand(Integer.parseInt(input.substring(DELETE_ARGUMENT_INDEX)));
            } catch (NumberFormatException e) {
                return new InvalidCommand("Delete index should be a number");
            }
        }
    }
}
