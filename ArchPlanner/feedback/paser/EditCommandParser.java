package feedback.paser;

import logic.commands.Command;
import logic.commands.InvalidCommand;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class EditCommandParser extends CommandParser {

    private final int EDIT_ARGUMENT_INDEX = 5;
    private final String KEYWORD_TAG = "tag ";
    private final String KEYWORD_START_DATE = "start date ";
    private final String KEYWORD_END_DATE = "end date ";

    @Override
    public Command parse(String input) {
        if (input.length() <= EDIT_ARGUMENT_INDEX) {
            return new InvalidCommand("Arguments missing");
        }
        String arguments = input.substring(EDIT_ARGUMENT_INDEX);
        if (arguments.startsWith(KEYWORD_TAG)) {

        }
        else if (arguments.startsWith(KEYWORD_START_DATE)) {

        }
        else if (arguments.startsWith(KEYWORD_END_DATE)) {

        } else {

        }

        return null;
    }
}
