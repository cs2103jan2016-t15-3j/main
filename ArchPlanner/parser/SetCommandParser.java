package parser;

import logic.commands.Command;
import logic.commands.InvalidCommand;
import logic.commands.SetCommand;
import separator.InputSeparator;

/**
 * Created by lifengshuang on 4/1/16.
 */
public class SetCommandParser extends CommandParser {
    public Command parse(String input) {
        InputSeparator separator = new InputSeparator(input);
        if (separator.getID() == null
                && separator.getKeywordType() != null
                && separator.getKeywordType() == InputSeparator.KeywordType.FILEPATH) {
            if (separator.getParameter() == null) {
                return new InvalidCommand("Path missing!");
            } else {
                return new SetCommand(separator.getParameter());
            }
        }
        return new InvalidCommand("Set command Invalid");
    }
}
