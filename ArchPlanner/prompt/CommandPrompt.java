package prompt;

import java.util.ArrayList;

import prompt.Prompt.CommandType;

/**
 * This class is used to prompt command suggestion to the user.
 * 
 * @@author A0140034B
 */
public class CommandPrompt implements PromptInterface{
    
    private static final String STRING_EMPTY = "";
    private static final String STRING_MULTIPLE_SPACE = "\\s+";
    private static final char CHAR_SPACE = ' ';
    
    private static final int INDEX_OFFSET_OF_ARRAY_AND_DISPLAY_NUMBER = 1;
    private static final int PROMPTS_MIN = 1;
    private static final int FIRST_COMMAND_PROMPT = 0;   
    
    private ArrayList<String> _commandPrompts;
    private CommandType _partialCommand;
    
    /** Initializes a newly created CommandPrompt object. */
    public CommandPrompt() {
        _commandPrompts = new ArrayList<String>();
        _partialCommand = CommandType.UNKNOWN;
    }
    
    /**
     * Interpret the string and return the appropriate command prompts to UI to display to the user.
     * 
     * @param userInput the string that represent the command.
     * @return          the list of command prompts to display to user.
     */
    @Override
    public ArrayList<String> getPrompts(String input) {
        if (!hasEndedWithSpace(input) && getNumOfWords(input) == 1) {
            _commandPrompts = getPartialCommands(input);
            if (_commandPrompts.size() < PROMPTS_MIN) {
                _partialCommand = CommandType.UNKNOWN;
            } else {
                _partialCommand = CommandType.valueOf(_commandPrompts.get(FIRST_COMMAND_PROMPT).toUpperCase());
            }
        }
        return _commandPrompts;
    }
    
    /**
     * Return the command that first partially matched the string.
     * Return the empty string if there is no keyword to auto-complete.
     * 
     * @return  the command for auto-completing.
     */
    @Override
    public String getAutoWord() {
        if (_partialCommand != CommandType.UNKNOWN) {
            return _partialCommand.toString().toLowerCase();
        } else {
            return STRING_EMPTY;
        }
    }
    
    /**
     * Return all commands that partially matched the string.
     * Return empty list if there is no command that partially matched the string.
     * 
     * @return  the list of commands that partially matched the string.
     */
    private ArrayList<String> getPartialCommands(String input) {
        ArrayList<String> partialCommands = new ArrayList<String>();
        input = input.trim().toUpperCase();
        for (CommandType commandType : CommandType.values()) {
            if (commandType != CommandType.UNKNOWN && commandType.toString().startsWith(input)) {
                partialCommands.add(commandType.toString().toLowerCase());
            }
        }
        return partialCommands;
    }
    
    private boolean hasEndedWithSpace(String input) {
        char lastChar = input.charAt(input.length() - INDEX_OFFSET_OF_ARRAY_AND_DISPLAY_NUMBER);
        if (lastChar == CHAR_SPACE) {
            return true;
        } else {
            return false;
        }
    }
    
    private int getNumOfWords(String input) {
        return input.trim().split(STRING_MULTIPLE_SPACE).length;
    }
}
