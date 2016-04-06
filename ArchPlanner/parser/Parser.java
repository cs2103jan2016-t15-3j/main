package parser;

import logic.Tag;
import logic.commands.*;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 3/23/16.
 */
public class Parser {
    private static final int COMMAND_TYPE_UNKNOWN = -1;
    private static final int COMMAND_TYPE_ADD = 0;
    private static final int COMMAND_TYPE_DELETE = 1;
    private static final int COMMAND_TYPE_EDIT = 2;
    private static final int COMMAND_TYPE_VIEW = 3;
    private static final int COMMAND_TYPE_DONE = 4;
    private static final int COMMAND_TYPE_UNDONE = 5;
    private static final int COMMAND_TYPE_UNDO = 6;
    private static final int COMMAND_TYPE_REDO = 7;
    private static final int COMMAND_TYPE_SEARCH = 8;
    private static final int COMMAND_TYPE_EXIT = 9;
    private static final int COMMAND_TYPE_SET = 10;

    private static final String[] ALL_COMMANDS =
            {"add", "delete", "edit",
                    "view", "done", "undone",
                    "undo", "redo", "search", "exit", "set"};

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

//    public Command parseCommand(String input) {
//        if (input == null) {
//            return new InvalidCommand("Null input");
//        }
//        int commandType = detectCommandType(input);
//        switch (commandType) {
//            case COMMAND_TYPE_UNKNOWN:
//                return new InvalidCommand("Invalid command");
//            case COMMAND_TYPE_ADD:
//                return new AddCommandParser().parse(input);
//            case COMMAND_TYPE_DELETE:
//                return new DeleteCommandParser().parse(input);
//            case COMMAND_TYPE_EDIT:
//                return new EditCommandParser().parse(input);
//            case COMMAND_TYPE_VIEW:
//                return new ViewCommandParser().parse(input);
//            case COMMAND_TYPE_DONE:
//                return new DoneCommandParser().parse(input);
//            case COMMAND_TYPE_UNDONE:
//                return new UndoneCommandParser().parse(input);
//            case COMMAND_TYPE_UNDO:
//                return new UndoCommand();
//            case COMMAND_TYPE_REDO:
//                return new RedoCommand();
//            case COMMAND_TYPE_SEARCH:
//                return new SearchCommandParser().parse(input);
//            case COMMAND_TYPE_EXIT:
//                return new ExitCommand();
//        }
//        return new InvalidCommand("Failed to parse");
//    }

    public CommandInterface parseCommand(String input, int viewListSize, int undoListSize, int redoListSize, ArrayList<Tag> tagList) {
        if (input == null) {
            return new InvalidCommand("Null input");
        }
        input = input.trim();
        int commandType = detectCommandType(input);
        switch (commandType) {
            case COMMAND_TYPE_UNKNOWN:
                return new InvalidCommand("Invalid command");
            case COMMAND_TYPE_ADD:
                return new AddCommandParser().parse(input);
            case COMMAND_TYPE_DELETE:
                return new DeleteCommandParser().parse(input, viewListSize);
            case COMMAND_TYPE_EDIT:
                return new EditCommandParser().parse(input, viewListSize);
            case COMMAND_TYPE_VIEW:
                return new ViewCommandParser().parse(input, tagList);
            case COMMAND_TYPE_DONE:
                return new DoneCommandParser().parse(input, viewListSize);
            case COMMAND_TYPE_UNDONE:
                return new UndoneCommandParser().parse(input, viewListSize);
            case COMMAND_TYPE_UNDO:
                if (undoListSize > 0 && input.split(" ").length == 1) {
                    return new UndoCommand();
                } else {
                    return new InvalidCommand("Can't undo anymore");
                }
            case COMMAND_TYPE_REDO:
                if (redoListSize > 0 && input.split(" ").length == 1) {
                    return new RedoCommand();
                } else {
                    return new InvalidCommand("Can't redo anymore");
                }
//            case COMMAND_TYPE_SEARCH:
//                return new SearchCommandParser().parse(input);
            case COMMAND_TYPE_EXIT:
                return new ExitCommand();
            case COMMAND_TYPE_SET:
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
    private int detectCommandType(String input) {
        for (int i = 0; i < ALL_COMMANDS.length; i++) {
            if (input.startsWith(ALL_COMMANDS[i])) {
                if (i <= COMMAND_TYPE_UNDONE
                        && input.length() > ALL_COMMANDS[i].length()
                        && input.charAt(ALL_COMMANDS[i].length()) != ' ') {
                    return COMMAND_TYPE_UNKNOWN;
                } else {
                    return i;
                }
            }
        }
        return COMMAND_TYPE_UNKNOWN;
    }
}
