package interpreter.parser;

import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import logic.commands.SetCommand;
import interpreter.separator.InputSeparator;

/**
 * @@author A0149647N
 * SetCommandParser parse delete command with InputSeparator
 */
public class SetCommandParser {

    private static final String DEFAULT = "default";
    private static final String INVALID_PATH_MISSING = "Path missing!";
    private static final String INVALID_COMMAND = "Set command Invalid";

    /**
     * Parse set command with InputSeparator
     * @return Parsed command object
     */
    public CommandInterface parse(String input) {
        InputSeparator separator = new InputSeparator(input);
        if (isValidCommand(separator)) {
            if (separator.getParameter() == null) {
                return new InvalidCommand(INVALID_PATH_MISSING);
            } else if (DEFAULT.equals(separator.getParameter().toLowerCase())) {
                return new SetCommand(null);
            } else {
                return new SetCommand(separator.getParameter());
            }
        }
        return new InvalidCommand(INVALID_COMMAND);
    }

    /**
     * Check if the set command is valid
     * The set command is valid if it doesn't have ID and the keyword if FILEPATH
     * @param separator This is the InputSeparator object which contains parsed result
     * @return True if the command is valid
     */
    private boolean isValidCommand(InputSeparator separator) {
        boolean noId = separator.getID() == null;
        boolean validKeyword = separator.getKeywordType() != null && separator.getKeywordType() == InputSeparator.KeywordType.FILEPATH;
        return noId && validKeyword;
    }
}
