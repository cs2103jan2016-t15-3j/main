package parser;

import logic.commands.Command;

/**
 * Created by lifengshuang on 3/16/16.
 */
public class SearchCommandParser extends CommandParser {

    private boolean searchDone = true;
    private boolean searchUndone = true;
    private boolean searchOverdue = false;

    @Override
    public Command parse(String input) {
        this.input = parseTag(input);
        parseDoneUndoneAndOverdue();

        return null;
    }

    private void parseDoneUndoneAndOverdue() {
        String[] words = split(input);
        String remaining = "";
        boolean done = false;
        boolean undone = false;
        for (String word : words) {
            switch (word) {
                case "overdue":
                    searchOverdue = true;
                    break;
                case "done":
                    done = true;
                    break;
                case "undone":
                    undone = true;
                    break;
                default:
                    remaining += word + " ";
                    break;
            }
        }
        if (done && !undone) {
            searchDone = true;
            searchUndone = false;
        }
        else if (!done && undone) {
            searchDone = false;
            searchUndone = true;
        }
        this.input = remaining.trim();
    }

}
