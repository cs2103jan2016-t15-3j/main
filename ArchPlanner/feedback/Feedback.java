package feedback;

import java.util.ArrayList;

import separator.AddInputSeparator.KeyWordType;

public class Feedback {

    enum CommandType {
        ADD, DELETE, EDIT, VIEW, DONE, UNDONE, UNDO, REDO, EXIT, UNKNOWN, SET
    }

    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    private final String DELETE_PROMPT = "delete <index>";
    private ArrayList<String> prompts;

    public Feedback() {
        prompts = new ArrayList<String>();
    }

    public ArrayList<String> getPrompts(String userInput, int taskListSize, int tagListSize) {
        prompts.clear();
        if (userInput == null) {
            prompts.add("Null input");
        } else if (!userInput.trim().isEmpty()){
            String commandInString = getFirstWord(userInput);
            CommandType commandType = determineCommandType(commandInString);
            System.out.println(commandType);

            switch (commandType) {
                case ADD:
                    System.out.println("IN here");
                    prompts = new AddPrompt().getPrompts(userInput.substring(CommandType.ADD.name().length()));
                    break;
                case EDIT:
                    prompts = new EditPrompt().getPrompts(userInput);
                    break;
                case VIEW:
                    prompts = new ViewPrompt().getPrompts(userInput);
                    break;
                case SET:
                    prompts = new SetPrompt().getPrompts(userInput);
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
                    if (getNumOfWords(userInput) == 1) {
                        for (CommandType type : CommandType.values()) {
                            if (type != CommandType.UNKNOWN && type.name().toLowerCase().startsWith(userInput.trim().toLowerCase())) {
                                prompts.add(type.name().toLowerCase());
                            }
                        }
                    }
                    break;
            }
        }

        //System.out.println(prompts.size());
        if (prompts.size() <= 0) {
            prompts.add("Invalid command: add | delete | edit | view | done | undone | undo | redo | exit");
        }
        return prompts;
    }

    public String getAutoComplete(String userInput) {
        ArrayList<String> cmdPrompts = new ArrayList<String>();
        if (getNumOfWords(userInput) == 1) {
            for (CommandType type : CommandType.values()) {
                if (type != CommandType.UNKNOWN && type.name().toLowerCase().startsWith(userInput.toLowerCase())) {
                    cmdPrompts.add(type.name().toLowerCase());
                }
            }
        }
        if (cmdPrompts.size() == 1) {
            return cmdPrompts.get(0) + " ";
        } else {
            return userInput;
        }
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
    }

    private String getFirstWord(String userInput) {
        String commandTypeString = userInput.trim().split(STRING_MULTIPLE_WHITESPACE)[0];
        return commandTypeString;
    }

    private int getNumOfWords(String userInput) {
        return userInput.trim().split(STRING_MULTIPLE_WHITESPACE).length;
    }
}
