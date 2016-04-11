package prompt;

import java.util.ArrayList;

/**
 * This class is used find the appropriate prompts to show to user depending of the current inputed text
 * and return to UI for display.
 * 
 * @@author A0140034B
 */
public class Prompt { 
    public enum CommandType {
        ADD, DELETE, EDIT, VIEW, DONE, UNDONE, UNDO, REDO, EXIT, UNKNOWN, SET
    }

    private static final String STRING_MULTIPLE_SPACE = "\\s+";
    private static final String STRING_INVALID_COMMAND = "Invalid command: add | delete | edit | view | done | undone | undo | redo | exit";
    private static final char CHAR_SPACE = ' ';
    
    private static final int PROMPTS_MIN = 1;
    private static final int INITIAL_INDEX = 0;
    private static final int INDEX_OFFSET_OF_ARRAY_AND_DISPLAY_NUMBER = 1;
    
    private ArrayList<String> _prompts;
    private PromptInterface _promptObj;
    
    /** Initializes a newly created Prompt object. */
    public Prompt() {
        _prompts = new ArrayList<String>();
    }
    
    /**
     * Interpret the string and return the appropriate prompts to UI to display to the user.
     * 
     * @param userInput the string that represent the command.
     * @return          the list of prompts to display to user.
     */
    public ArrayList<String> getPrompts(String userInput) {
        _prompts.clear();
        
        if (userInput == null) {
            _prompts.add(STRING_INVALID_COMMAND);
        } else if (!userInput.trim().isEmpty()) {
            CommandType commandType = determineCommandType(userInput);

            _promptObj = getPromptObject(commandType);
            _prompts = _promptObj.getPrompts(userInput);
        }

        if (_prompts.size() < PROMPTS_MIN) {
            _prompts.add(STRING_INVALID_COMMAND);
        }
        return _prompts;
    }
    
    /**
     * Interpret the string and return the appropriate string with the auto-complete keyword.
     * There is no keyword to auto-complete if the last character is a space.
     * Return the same string if there is no keyword to auto-complete.
     * 
     * @param userInput the string that represent the command.
     * @return          the string after auto-completing the keyword.
     */
    public String getAutoComplete(String userInput) {
        if (userInput.isEmpty()) {
            return userInput;
        }
        char lastChar = userInput.charAt(userInput.length() - INDEX_OFFSET_OF_ARRAY_AND_DISPLAY_NUMBER);
        if (lastChar != CHAR_SPACE) {
            String autoWords = _promptObj.getAutoWord();
    
            if (autoWords == null || autoWords.isEmpty()) {
                return userInput;
            } else {
                return removeLastWord(userInput) + autoWords;
            }
        } else {
            return userInput;
        }
    }
    
    /**
     * Return the relevant Prompt Object depending on the command.
     * 
     * @param commandType   the command detected in the user input.
     * @return              the Object associated with the command
     */
    private PromptInterface getPromptObject(CommandType commandType) {
        switch (commandType) {
            case ADD :
                return new AddPrompt();
            case EDIT :
                return new EditPrompt();
            case VIEW :
                return new ViewPrompt();
            case SET :
                return new SetPrompt();
            case DELETE :
                // Fallthrough
            case DONE :
                // Fallthrough
            case UNDONE :
                return new IdOnlyPrompt();
            case UNDO :
                // Fallthrough
            case REDO :
                // Fallthrough
            case EXIT :
                // Fallthrough
            case UNKNOWN :
                return new CommandPrompt();
            default :
                return null;
        }
    }
    
    /**
     * Interpret the first word in the string and return the command associated with the word.
     * Return UNKNOWN if string is empty or no command is found.
     * 
     * @param input   the string used to interpret the command.
     * @return        the Command interpreted from the first word.
     */
    private CommandType determineCommandType(String input) {
        assert(input != null);
        
        String commandTypeString = getFirstWord(input);
        if (commandTypeString.isEmpty()) {
            return CommandType.UNKNOWN;
        }
        for (CommandType type : CommandType.values()) {
            if (commandTypeString.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return CommandType.UNKNOWN;
    }

    private String getFirstWord(String input) {
        String commandTypeString = input.trim().split(STRING_MULTIPLE_SPACE)[INITIAL_INDEX];
        return commandTypeString;
    }
    
    private String removeLastWord(String input) {
        String[] splitInput = input.trim().split(STRING_MULTIPLE_SPACE);
        String lastWord = splitInput[splitInput.length - INDEX_OFFSET_OF_ARRAY_AND_DISPLAY_NUMBER];
        int indexOfLastWord = input.lastIndexOf(lastWord);
        return input.substring(INITIAL_INDEX, indexOfLastWord);
    }
}
