package feedback;

import paser.InputSeparator;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 3/29/16.
 */
public class IDOnlyPrompt {

    private final String COMMAND = "%s <Task ID>";
    private final String INVALID_ID = "Invalid ID: %s <Task ID>";
    private final String INVALID_ARGUMENTS = "Invalid command: too many arguments";

    public ArrayList<String> getPrompts(String userInput, String commandName) {
        ArrayList<String> promptList = new ArrayList<>();
        InputSeparator inputSeparator = new InputSeparator(userInput);
        if (inputSeparator.getWordCount() == 1) {
            promptList.add(String.format(COMMAND, commandName));
        } else if (inputSeparator.getWordCount() == 2) {
            if (inputSeparator.isEndWithSpace()) {
                promptList.add(INVALID_ARGUMENTS);
            } else if (inputSeparator.getID() != null) {
                if (inputSeparator.getID() > 0) {
                    promptList.add(String.format(COMMAND, commandName));
                } else {
                    promptList.add(String.format(INVALID_ID, commandName));
                }
            } else {
                promptList.add(String.format(INVALID_ID, commandName));
            }
        } else {
            promptList.add(INVALID_ARGUMENTS);
        }
        return promptList;
    }
}
