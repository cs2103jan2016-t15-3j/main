//@@author A0140034B
package prompt;

import java.util.ArrayList;

import prompt.Prompt.CommandType;

public class CommandPrompt implements PromptInterface{
    
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    
    private ArrayList<String> commandPrompts;
    private CommandType partialCommand;
    
    public CommandPrompt() {
        commandPrompts = new ArrayList<String>();
        partialCommand = CommandType.UNKNOWN;
    }
    
    @Override
    public ArrayList<String> getPrompts(String input) {
        if (getNumOfWords(input) == 1) {
            for (CommandType type : CommandType.values()) {
                if (type != CommandType.UNKNOWN && type.name().toLowerCase().startsWith(input.trim().toLowerCase())) {
                    commandPrompts.add(type.name().toLowerCase());
                    if (partialCommand == CommandType.UNKNOWN) {
                        partialCommand = type;
                    }
                }
            }
            if (commandPrompts.size() <= 0) {
                partialCommand = CommandType.UNKNOWN;
            }
        }
        return commandPrompts;
    }

    @Override
    public String getAutoWord() {
        if (partialCommand != CommandType.UNKNOWN) {
            return partialCommand.toString().toLowerCase();
        } else {
            return "";
        }
    }
    
    private int getNumOfWords(String input) {
        return input.trim().split(STRING_MULTIPLE_WHITESPACE).length;
    }
}
