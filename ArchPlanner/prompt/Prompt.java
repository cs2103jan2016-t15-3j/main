//@@author A0140034B
package prompt;

import java.util.ArrayList;

public class Prompt { 

    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    private static final String STRING_INVALID_COMMAND = "Invalid command: add | delete | edit | view | done | undone | undo | redo | exit";
    private static final char CHAR_SPACE = ' ';
    
    private static final int PROMPTS_MIN_NUM = 1;
    private static final int INITIAL_INDEX = 0;
    private static final int LAST_INDEX_OFFSET = 1;

    public enum CommandType {
        ADD, DELETE, EDIT, VIEW, DONE, UNDONE, UNDO, REDO, EXIT, UNKNOWN, SET
    }
    
    private ArrayList<String> _prompts;
    private PromptInterface _promptObj;

    public Prompt() {
        _prompts = new ArrayList<String>();
    }

    public ArrayList<String> getPrompts(String userInput, int taskListSize, int tagListSize) {
        _prompts.clear();
        
        if (userInput == null) {
            _prompts.add(STRING_INVALID_COMMAND);
        } else if (!userInput.trim().isEmpty()) {
//            userInput = removeMultipleSpace(userInput);
            CommandType commandType = determineCommandType(userInput);

            _promptObj = getPromptObject(commandType);
            _prompts = _promptObj.getPrompts(userInput);
        }

        if (_prompts.size() < PROMPTS_MIN_NUM) {
            _prompts.add(STRING_INVALID_COMMAND);
        }
        return _prompts;
    }

    public String getAutoComplete(String userInput) {
        if (userInput.isEmpty()) {
            return userInput;
        }
        if (userInput.charAt(userInput.length() - LAST_INDEX_OFFSET) != CHAR_SPACE) {
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
                //fallthrough
            case DONE :
                //fallthrough
            case UNDONE :
                return new IDOnlyPrompt();
            case UNDO :
                //fallthrough
            case REDO :
                //fallthrough
            case EXIT :
                //fallthrough
            case UNKNOWN :
                return new CommandPrompt();
            default :
                return null;
        }
    }

    private CommandType determineCommandType(String input) {
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
        String commandTypeString = input.trim().split(STRING_MULTIPLE_WHITESPACE)[INITIAL_INDEX];
        return commandTypeString;
    }
    
    private String removeLastWord(String input) {
        String[] splitInput = input.trim().split(STRING_MULTIPLE_WHITESPACE);
        int indexOfLastWord = input.lastIndexOf(splitInput[splitInput.length - LAST_INDEX_OFFSET]);
        return input.substring(INITIAL_INDEX, indexOfLastWord);
    }

    private String removeMultipleSpace(String input) {
        String[] splitInput = input.trim().split(STRING_MULTIPLE_WHITESPACE);
        String result = "";
        for (String part : splitInput) {
            result += part + " ";
        }
        return result.trim();
    }
}
