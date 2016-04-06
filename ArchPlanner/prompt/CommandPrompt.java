package prompt;

import java.util.ArrayList;

import prompt.Prompt.CommandType;

public class CommandPrompt implements PromptInterface{
    
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    
    private ArrayList<String> commandPrompts;
    
    public CommandPrompt() {
        commandPrompts = new ArrayList<String>();
    }
    
    @Override
    public ArrayList<String> getPrompts(String input) {
        if (getNumOfWords(input) == 1) {
            for (CommandType type : CommandType.values()) {
                if (type != CommandType.UNKNOWN && type.name().toLowerCase().startsWith(input.trim().toLowerCase())) {
                    commandPrompts.add(type.name().toLowerCase());
                }
            }
        }
        return commandPrompts;
    }

    @Override
    public String getAutoWord() {
        if (commandPrompts.size() == 1) {
            return commandPrompts.get(0);
        } else {
            return "";
        }
    }
    
    private int getNumOfWords(String input) {
        return input.trim().split(STRING_MULTIPLE_WHITESPACE).length;
    }
}
