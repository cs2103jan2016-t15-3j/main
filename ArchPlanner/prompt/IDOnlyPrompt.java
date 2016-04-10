package prompt;

import separator.InputSeparator;

import java.util.ArrayList;

/**
 * @@author A0149647N
 * IdOnlyPrompt return the prompts of id only command for the user input
 */
public class IdOnlyPrompt implements PromptInterface{

    private static final String COMMAND = "%s <Task ID>";
    private static final String COMMAND_RANGE = "%s <Task ID> to <Task ID>";
    private static final String INVALID_ID = "Invalid ID: %s <Task ID>";
    private static final String INVALID_COMMAND = "Invalid command";
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    private static final String EMPTY_STRING = "";
    private static final int FIRST_INDEX = 0;
    private static final int ONE_WORD = 1;
    private static final int TWO_WORD = 2;
    private static final int MINIMUM_ID = 1;

    InputSeparator inputSeparator;

    /**
     * Get the prompt of ID only command: delete, done and undone.
     */
    @Override
    public ArrayList<String> getPrompts(String userInput) {
        String commandName = userInput.trim().toLowerCase().split(STRING_MULTIPLE_WHITESPACE)[FIRST_INDEX];
        ArrayList<String> promptList = new ArrayList<>();
        this.inputSeparator = new InputSeparator(userInput);
        if (inputSeparator.getWordCount() == ONE_WORD) {
            promptList.add(String.format(COMMAND, commandName));
            promptList.add(String.format(COMMAND_RANGE, commandName));
        } else if (inputSeparator.getWordCount() == TWO_WORD) {
            if (inputSeparator.getID() != null) {
                if (inputSeparator.getID() >= MINIMUM_ID) {
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

    /**
     * Get auto complete word of current input
     * @return The auto complete word
     */
    @Override
    public String getAutoWord() {
        if (inputSeparator.getID() != null) {
            return inputSeparator.getPartialKeyword();
        }
        return EMPTY_STRING;
    }
}
