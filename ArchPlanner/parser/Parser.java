package parser;

import logic.Tag;
import logic.commands.*;

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
    private static final String NATTY_INIT = "today";
    private static final int INITIAL_INDEX = 0;
    private static final String INVALID_NULL = "Null input";
    private static final String INVALID_COMMAND = "Invalid command";
    private static final String INVALID_UNDO_LIST_SIZE = "Can't undo anymore";
    private static final String INVALID_UNDO_LENGTH = "Invalid undo: too many arguments";
    private static final String INVALID_REDO_LIST_SIZE = "Can't redo anymore";
    private static final String INVALID_REDO_LENGTH = "Invalid redo: too many arguments";
    private static final String INVALID_FAILED = "Failed to parse";

    public Parser() {
        init();
    }

    public static void init() {
        //start a new thread calling Natty at beginning to avoid lag of Natty initialization
        new Thread(() -> {
            new com.joestelmach.natty.Parser().parse(NATTY_INIT);
        }).start();
    }

    /**
     * This method is used to parse user input into command object.
     *
     * @param input The user's input.
     * @return parsed command. If input is invalid, will return InvalidCommand
     */
    public CommandInterface parseCommand(String input, int viewListSize, int undoListSize, int redoListSize, ArrayList<Tag> tagList) {
        if (input == null) {
            return new InvalidCommand(INVALID_NULL);
        }
        CommandType commandType = determineCommandType(input);
        switch (commandType) {
            case UNKNOWN:
                return new InvalidCommand(INVALID_COMMAND);
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
                return parseUndoCommand(input, undoListSize);
            case REDO:
                return parseRedoCommand(input, redoListSize);
            case EXIT:
                return new ExitCommand();
            case SET:
                return new SetCommandParser().parse(input);
        }
        return new InvalidCommand(INVALID_FAILED);
    }

    private CommandInterface parseUndoCommand(String input, int undoListSize) {
        if (input.split(STRING_MULTIPLE_WHITESPACE).length != 1) {
            return new InvalidCommand(INVALID_UNDO_LENGTH);
        } else if (undoListSize <= 0) {
            return new InvalidCommand(INVALID_UNDO_LIST_SIZE);
        } else {
            return new UndoCommand();
        }
    }

    private CommandInterface parseRedoCommand(String input, int redoListSize) {
        if (input.split(STRING_MULTIPLE_WHITESPACE).length != 1) {
            return new InvalidCommand(INVALID_REDO_LENGTH);
        } else if (redoListSize <= 0) {
            return new InvalidCommand(INVALID_REDO_LIST_SIZE);
        } else {
            return new UndoCommand();
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
        return input.trim().split(STRING_MULTIPLE_WHITESPACE)[INITIAL_INDEX];
    }

}
