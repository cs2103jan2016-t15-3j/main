package feedback.paser;

import logic.commands.Command;
import logic.commands.ViewCommand;

/**
 * Created by lifengshuang on 3/16/16.
 */
public class ViewCommandParser extends CommandParser {
    @Override
    public Command parse(String input) {
        if (input.equals("view all")) {
//            return new ViewCommand();
        }
        return null;
    }
}
