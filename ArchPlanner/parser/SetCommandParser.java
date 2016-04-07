package parser;

import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import logic.commands.SetCommand;
import separator.InputSeparator;

/**
 * @@author A0149647N
 * SetCommandParser parse delete command with InputSeparator
 */
public class SetCommandParser {

    private final String DEFAULT = "default";

    public CommandInterface parse(String input) {
        InputSeparator separator = new InputSeparator(input);
        if (separator.getID() == null
                && separator.getKeywordType() != null
                && separator.getKeywordType() == InputSeparator.KeywordType.FILEPATH) {
            if (separator.getParameter() == null) {
                return new InvalidCommand("Path missing!");
            } else {
                if (separator.getParameter().equalsIgnoreCase(DEFAULT)) {
                    return new SetCommand(null);
                }
                return new SetCommand(separator.getParameter());
            }
        }
        return new InvalidCommand("Set command Invalid");
    }
}
