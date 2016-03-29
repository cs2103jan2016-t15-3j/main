package feedback;

import java.util.ArrayList;

public class Feedback {

    enum CommandType {
        ADD, DELETE, EDIT, VIEW, DONE, UNDONE, UNDO, REDO, EXIT, UNKNOWN
    }

    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    //    private final String[] ALL_COMMANDS = {"add", "delete ", "edit ", "search ",
//                                           "view ", "done ", "undone ", "undo ",
//                                           "redo ", "exit "};
    private final String DELETE_PROMPT = "delete <index>";
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
        if (userInput == null) {
            prompts.add("Null input");
        } else if (userInput.trim().isEmpty()) {
            prompts.add("Invalid command");
        } else {
            String commandInString = getFirstWord(userInput);
            CommandType commandType = determineCommandType(commandInString);
            System.out.println(commandType);

            switch (commandType) {
                case ADD:
                    prompts = new AddPrompt().getPrompts(userInput.substring(3));
                    break;
                case EDIT:
                    //todo: edit prompt
                    prompts = new EditPrompt().getPrompts(userInput);
                    break;
                case VIEW:
                    prompts = new ViewPrompt().getPrompts(userInput);
                    break;
                case DELETE:
                    //fallthrough
                case DONE:
                    //fallthrough
                case UNDONE:
                    prompts = new IDOnlyPrompt().getPrompts(userInput, commandType.name().toLowerCase());
                    break;
                case UNDO:
                    //fallthrough
                case REDO:
                    //fallthrough
                case EXIT:
                    //fallthrough
                case UNKNOWN:
                    if (getNumOfWords(userInput) == 1 && !isSpace) {
                        for (CommandType type : CommandType.values()) {
                            if (type.name().toLowerCase().startsWith(userInput.toLowerCase())) {
                                prompts.add(type.name().toLowerCase());
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

//        System.out.println(prompts.size());
        if (prompts.size() <= 0) {
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
    }

    private CommandType determineCommandType(String commandTypeString) {
        if (commandTypeString.isEmpty()) {
            return CommandType.UNKNOWN;
        }
        for (CommandType type : CommandType.values()) {
            if (commandTypeString.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return CommandType.UNKNOWN;
//        if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[0])) {
//            return CommandType.ADD;
//        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[1])) {
//            return CommandType.DELETE;
//        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[2])) {
//            return CommandType.EDIT;
//        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[3])) {
//            return CommandType.SEARCH;
//        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[4])) {
//            return CommandType.VIEW;
//        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[5])) {
//            return CommandType.DONE;
//        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[6])) {
//            return CommandType.UNDONE;
//        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[7])) {
//            return CommandType.UNDO;
//        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[8])) {
//            return CommandType.REDO;
//        } else if (commandTypeString.equalsIgnoreCase(ALL_COMMANDS[9])) {
//            return CommandType.EXIT;
//        } else {
//            return CommandType.UNKNOWN;
//        }
    }

    private String getFirstWord(String userInput) {
        String commandTypeString = userInput.trim().split(STRING_MULTIPLE_WHITESPACE)[0];
        return commandTypeString;
    }

    private ArrayList<String> listWithSingleString(String string) {
        ArrayList<String> result = new ArrayList<>();
        result.add(string);
        return result;
    }

    private int getNumOfWords(String userInput) {
        return userInput.trim().split(STRING_MULTIPLE_WHITESPACE).length;
    }
}
