package feedback.paser;

import logic.commands.EditCommand;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class EditCommandParser {
    public static EditCommand parser(String input) {
        String[] s = input.split(" ");
        return new EditCommand(Integer.parseInt(s[1]), ParserUtils.merge(s, 2, s.length), null, null, null);
    }
}
