package parser;

import logic.Tag;
import logic.commands.*;
import prompt.Prompt;

import java.util.ArrayList;

/**
 * @@author A0149647N
 * Parser receives user input and dispatch it to specific parser to parse it.
 */
public class Parser {

    enum CommandType {
        ADD, DELETE, EDIT, VIEW, DONE, UNDONE, UNDO, REDO, EXIT, UNKNOWN, SET
    }

    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    private static final int INITIAL_INDEX = 0;

    public static void init() {
        new Thread(() -> {
            new com.joestelmach.natty.Parser().parse("today");
        }).start();
    }

    /**
     * This method is used to parse user input into Command object.
     *
     * @param input The user's input.
     * @return parsed command. If input is invalid, will return InvalidCommand
     */

    public CommandInterface parseCommand(String input, int viewListSize, int undoListSize, int redoListSize, ArrayList<Tag> tagList) {
        if (input == null) {
            return new InvalidCommand("Null input");
        }
        CommandType commandType = determineCommandType(input);
        switch (commandType) {
            case UNKNOWN:
                return new InvalidCommand("Invalid command");
            case ADD:
                return new AddCommandParser().parse(input);
            case DELETE:
                return new DeleteCommandParser().parse(input, viewListSize);
            case EDIT:
                return new EditCommandParser().parse(input, viewListSize);
            case VIEW:
                return new ViewCommandParser().parse(input, tagList);
            case DONE:
                return new DoneCommandParser().parse(input, viewListSize);
            case UNDONE:
                return new UndoneCommandParser().parse(input, viewListSize);
            case UNDO:
                if (undoListSize > 0 && input.split(" ").length == 1) {
                    return new UndoCommand();
                } else {
                    return new InvalidCommand("Can't undo anymore");
                }
            case REDO:
                if (redoListSize > 0 && input.split(" ").length == 1) {
                    return new RedoCommand();
                } else {
                    return new InvalidCommand("Can't redo anymore");
                }
            case EXIT:
                return new ExitCommand();
            case SET:
                return new SetCommandParser().parse(input);
        }
        return new InvalidCommand("Failed to parse");
    }

    /**
     * Detect the command type of user input.
     *
     * @param input The user's input
     * @return detected type code
     */
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

}
