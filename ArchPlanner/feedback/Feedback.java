package feedback;

import java.util.ArrayList;

import logic.commands.Command;

public class Feedback {

    enum CommandType {
        ADD, DELETE, EDIT, SEARCH, VIEW, DONE, UNDONE, UNDO, REDO, EXIT, UNKNOWN
    };
    
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    private final String[] ALL_COMMANDS = {"add", "delete", "edit", "search", 
                                           "view", "done", "undone", "undo",
                                           "redo", "exit"};
    
    private CommandType holdCmd;
    private ArrayList<String> prompts;
    private int prevStringLength;
    private boolean isSpace;
    
    public Feedback() {
        prompts = new ArrayList<String>();
        holdCmd = CommandType.UNKNOWN;
        prevStringLength = 0;
        isSpace = false;
    }
    
    public ArrayList<String> getPrompts(String userInput) {
        prompts.clear();
        
        if (userInput.trim().isEmpty()) {
            prompts.add("Invalid command");
        } else {
            String commandInString = getFirstWord(userInput);
            CommandType commandType = determineCommandType(commandInString);
            System.out.println(commandType);
            
            switch (commandType) {
                case ADD :
                    prompts = new AddPrompt().getPrompts(userInput, isSpace);
                    break;
                case UNKNOWN :
                    if (getNumOfWords(userInput) == 1 && !isSpace) {
                        for (int i = 0; i < ALL_COMMANDS.length; i++) {
                            if (ALL_COMMANDS[i].startsWith(commandInString)) {
                            }
                        }
                    } else {
                        prompts.add("Invalid command");
                    }
                    break;
                default:
                    System.out.println("Not supoose to happen");
            }
        }
        
        /*
        if (userInput.trim().isEmpty()) {
            prompts.add("Invalid command");
        } else {
            String[] partialCmd = userInput.trim().split(STRING_MULTIPLE_WHITESPACE);
            System.out.println("Partial: " + partialCmd.length);
            System.out.println(partialCmd[0]);
            if (partialCmd.length == 1) {
                for (int i = 0; i < ALL_COMMANDS.length; i++) {
                    if (ALL_COMMANDS[i].startsWith(partialCmd[0])) {
                        prompts.add(ALL_COMMANDS[i]);
                    }
                }
                System.out.println("Prompt: " + prompts.size());
                if(prompts.size() == 1) {
                    holdCmd = determineCommandType(partialCmd[0]);
                } else if (prompts.size() <= 0) {
                    prompts.add("Invalid command");
                }
            } else {
                if (holdCmd == CommandType.UNKNOWN) {
                    prompts.add("Invalid command");
                } else {
                    switch (holdCmd) {
                        case ADD: prompts = new AddPrompt().getPrompts(userInput.substring(3));
                    }
                }
            }
            System.out.println(holdCmd);
        }
        */
        System.out.println(prompts.size());
        if(prompts.size() <= 0) {
            prompts.add("Invalid command");
        }
        return prompts;
    }
    
    public String getAutoComplete(String userInput) {
        if (userInput.charAt(userInput.length() - 1) == ' ') {
            isSpace = true;
        } else {
            isSpace = false;
        }
        return userInput;  
            /*
            if (prompts.size() == 1) {
                return prompts.get(0) + " ";
            } else {
                return userInput;
            }*/
    }
    
    private CommandType determineCommandType(String commandTypeString) {
        if (commandTypeString.isEmpty()) {
            return CommandType.UNKNOWN;
        }

        if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[0])) {
            return CommandType.ADD;
        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[1])) {
            return CommandType.DELETE;
        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[2])) {
            return CommandType.EDIT;
        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[3])) {
            return CommandType.SEARCH;
        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[4])) {
            return CommandType.VIEW;
        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[5])) {
            return CommandType.DONE;
        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[6])) {
            return CommandType.UNDONE;
        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[7])) {
            return CommandType.UNDO;
        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[8])) {
            return CommandType.REDO;
        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[9])) {
            return CommandType.EXIT;
        } else {
            return CommandType.UNKNOWN;
        }
    }
    
    private String getFirstWord(String userInput) {
        String commandTypeString = userInput.trim().split(STRING_MULTIPLE_WHITESPACE)[0];
        return commandTypeString;
    }
    
    private int getNumOfWords(String userInput) {
        return userInput.trim().split(STRING_MULTIPLE_WHITESPACE).length;
    }
}
