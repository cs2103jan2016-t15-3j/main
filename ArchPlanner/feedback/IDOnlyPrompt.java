package feedback;

import separator.InputSeparator;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 3/29/16.
 */
public class IDOnlyPrompt {

    private final String COMMAND = "%s <Task ID>";
    private final String COMMAND_RANGE = "%s <Task ID> to <Task ID>";
    private final String INVALID_ID = "Invalid ID: %s <Task ID>";
    private final String INVALID_COMMAND = "Invalid command";

    public ArrayList<String> getPrompts(String userInput, String commandName) {
        ArrayList<String> promptList = new ArrayList<>();
        InputSeparator inputSeparator = new InputSeparator(userInput);
        if (inputSeparator.getWordCount() == 1) {
            promptList.add(String.format(COMMAND, commandName));
            promptList.add(String.format(COMMAND_RANGE, commandName));
        } else if (inputSeparator.getWordCount() == 2) {
            if (inputSeparator.getID() != null) {
                if (inputSeparator.getID() > 0) {
                    promptList.add(String.format(COMMAND, commandName));
                    promptList.add(String.format(COMMAND_RANGE, commandName));
                } else {
                    promptList.add(String.format(INVALID_ID, commandName));
                }
            } else {
                promptList.add(String.format(INVALID_ID, commandName));
            }
        } else {
            if (inputSeparator.mayHaveTwoValidID()) {
                promptList.add(String.format(COMMAND_RANGE, commandName));
            } else {
                promptList.add(INVALID_COMMAND);
            }
        }
        return promptList;
    }
}
