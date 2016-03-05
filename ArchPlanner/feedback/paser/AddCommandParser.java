package feedback.paser;

import logic.commands.AddCommand;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class AddCommandParser {

    public static AddCommand parser(String input) {

        return new AddCommand(input.substring(4), null, null, null);
    }
}
