package feedback.paser;

import logic.commands.Command;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand;

import java.util.Arrays;

/**
 * Created by lifengshuang on 3/16/16.
 */
public class ViewCommandParser extends CommandParser {
    @Override
    public Command parse(String input) {
        if (input.equals("view all")) {
            return new ViewCommand(null, true, false, false, false);
        }
        if (input.equals("view done")) {
            return new ViewCommand(null, false, true, false, false);
        }
        if (input.equals("view undone")) {
            return new ViewCommand(null, false, false, true, false);
        }
        if (input.equals("view overdue")) {
            return new ViewCommand(null, false, false, false, true);
        }
        String[] tags = split(input);
        if (tags.length <= 1) {
            return new InvalidCommand("Argument missing");
        } else {
            tagList.addAll(Arrays.asList(tags).subList(1, tags.length));
            return new ViewCommand(tagList, false, false, false, false);
        }
    }

}
