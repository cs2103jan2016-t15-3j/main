package feedback;

import feedback.paser.AddCommandParser;
import feedback.paser.DeleteCommandParser;
import feedback.paser.EditCommandParser;
import logic.commands.*;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by lifengshuang on 2/29/16.
 */
public class Feedback {

    private static final int COMMAND_TYPE_UNKNOWN = -1;
    private static final int COMMAND_TYPE_ADD = 0;
    private static final int COMMAND_TYPE_DELETE = 1;
    private static final int COMMAND_TYPE_EDIT = 2;
    private static final int COMMAND_TYPE_VIEW = 3;
    private static final int COMMAND_TYPE_DONE = 4;
    private static final int COMMAND_TYPE_UNDONE = 5;
    private static final int COMMAND_TYPE_UNDO = 6;
    private static final int COMMAND_TYPE_REDO = 7;
    private static final int COMMAND_TYPE_SORT = 8;
    private static final int COMMAND_TYPE_SEARCH = 9;
    private static final int COMMAND_TYPE_EXIT = 10;

    private static final String[] ALL_COMMANDS =
            {"add", "delete", "edit",
                    "view", "done", "undone",
                    "undo", "redo", "sort",
                    "search", "exit"};

    private static final String ADD = "add";
    private static final String ADD_DESCRIPTION = "add <description>";
    private static final String ADD_DESCRIPTION_TIME = "add <description> <time>";
    private static final String ADD_DESCRIPTION_FROM_TIME = "add <description> from <time>";
    private static final String ADD_DESCRIPTION_START_AT_TIME = "add <description> from <time> to <time>";
    private static final String ADD_DESCRIPTION_BY_TIME = "add <description> by <time>";
    private static final String ADD_DESCRIPTION_UNTIL_TIME = "add <description> until <time>";
    private static final String ADD_DESCRIPTION_DEADLINE_TIME = "add <description> deadline <time>";
    private static final String ADD_DESCRIPTION_FROM_TIME_TO_TIME = "add <description> from <time> to <time>";


    private static final String DELETE_INDEX = "delete <index>";

    private static final String EDIT = "edit";
    private static final String EDIT_DESCRIPTION = "edit <index> <description>";
    private static final String EDIT_TIME = "edit <index> <time>";
    private static final String EDIT_DESCRIPTION_TIME = "edit <index> <description> <time>";

    private static final String VIEW = "view";
    private static final String VIEW_DESCRIPTION = "view <description>";
    private static final String VIEW_CATEGORY = "view <category>";
    private static final String VIEW_TIME = "view <time>";
    //more view things

    private static final String DONE = "done <index>";
    private static final String UNDONE = "undone <index>";
    private static final String UNDO = "undo";
    private static final String REDO = "redo";

    private static final String SORT = "sort";
    private static final String SORT_BY_TYPE = "sort <DESCRIPTION | TAG | START_DATE_TIME | END_DATE_TIME | COMPLETION | OVERDUE>";
    private static final String SEARCH_DESCRIPTION = "search <description>";

    private static final String EXIT = "exit";

    private static final String NO_MATCH = "No pattern matched";
    private static final String EMPTY_INPUT = "add | delete | edit | view | search | sort | done | undone | undo | redo | exit";


    private static ArrayList<String> feedbackList = new ArrayList<String>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Feedback feedback = new Feedback();
        while (true) {
            String input = scanner.nextLine();
            ArrayList<String> list = feedback.onTextChanged(input);
            for (String s : list) {
                System.out.println(s);
            }
            feedback.onEnterPressed(input);
        }
    }

    public ArrayList<String> onTextChanged(String input) {
        feedbackList = new ArrayList<String>();
        int commandType = detectCommandType(input);
        switch (commandType) {
            case COMMAND_TYPE_UNKNOWN:
                handleUnknownCommand(input);
                break;
            case COMMAND_TYPE_ADD:
                //todo: good style
                if (input.contains(" by ")) {
                    feedbackList.add(ADD_DESCRIPTION_BY_TIME);
                }
                if (input.contains(" until ")) {
                    feedbackList.add(ADD_DESCRIPTION_UNTIL_TIME);
                }
                if (input.contains(" deadline ")) {
                    feedbackList.add(ADD_DESCRIPTION_DEADLINE_TIME);
                }
                if (input.contains(" start at ")) {
                    feedbackList.add(ADD_DESCRIPTION_START_AT_TIME);
                }
                if (input.contains(" from ")) {
                    if (input.contains(" to ")) {
                        feedbackList.add(ADD_DESCRIPTION_FROM_TIME_TO_TIME);
                    } else {
                        feedbackList.add(ADD_DESCRIPTION_FROM_TIME);
                    }
                }
                feedbackList.add(ADD_DESCRIPTION);

                break;
            case COMMAND_TYPE_DELETE:
                feedbackList.add(DELETE_INDEX);
                break;
            case COMMAND_TYPE_EDIT:
                feedbackList.add(EDIT_DESCRIPTION);
                feedbackList.add(EDIT_DESCRIPTION_TIME);
                break;
            case COMMAND_TYPE_VIEW:
                feedbackList.add(VIEW_DESCRIPTION);
                feedbackList.add(VIEW_CATEGORY);
                feedbackList.add(VIEW_TIME);
                break;
            case COMMAND_TYPE_DONE:
                feedbackList.add(DONE);
                break;
            case COMMAND_TYPE_UNDONE:
                feedbackList.add(UNDONE);
                break;
            case COMMAND_TYPE_UNDO:
                feedbackList.add(UNDO);
                break;
            case COMMAND_TYPE_REDO:
                feedbackList.add(REDO);
                break;
            case COMMAND_TYPE_SORT:
                feedbackList.add(SORT_BY_TYPE);
                break;
            case COMMAND_TYPE_SEARCH:
                feedbackList.add(SEARCH_DESCRIPTION);
                break;
            case COMMAND_TYPE_EXIT:
                feedbackList.add(EXIT);
                break;
        }
        return feedbackList;
    }

    public Command onEnterPressed(String input) {
        int commandType = detectCommandType(input);
        //todo: Convert to Good Style
        //todo: handle complex cases
        //todo: much to do --after midterm
        try {
            switch (commandType) {
                case COMMAND_TYPE_UNKNOWN:
                    return new InvalidCommand("Invalid command");
                case COMMAND_TYPE_ADD:
                    return new AddCommandParser().parse(input);
                case COMMAND_TYPE_DELETE:
                    return new DeleteCommandParser().parse(input);
                case COMMAND_TYPE_EDIT:
                    return new EditCommandParser().parse(input);
                case COMMAND_TYPE_VIEW:
                    return new ViewCommand(input.substring(5), null, null, null);
                case COMMAND_TYPE_DONE:
                    return new DoneCommand(Integer.parseInt(input.substring(5)));
                case COMMAND_TYPE_UNDONE:
                    return new UndoneCommand(Integer.parseInt(input.substring(7)));
                case COMMAND_TYPE_UNDO:
                    return new UndoCommand();
                case COMMAND_TYPE_REDO:
                    return new RedoCommand();
                case COMMAND_TYPE_SORT:
                    return new SortCommand(input.substring(5));
                case COMMAND_TYPE_SEARCH:
                    return new SearchCommand(input.substring(7));
                case COMMAND_TYPE_EXIT:
                    return new ExitCommand();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return new InvalidCommand("Feedback Error");
    }

    private void handleUnknownCommand(String input) {
        if (input.isEmpty()) {
            feedbackList.add(EMPTY_INPUT);
            return;
        }
        for (int i = 0; i < ALL_COMMANDS.length; i++) {
            if (ALL_COMMANDS[i].startsWith(input)) {
                feedbackList.add(ALL_COMMANDS[i]);
                switch (i) {
                    case COMMAND_TYPE_ADD:
                        feedbackList.add(ADD_DESCRIPTION);
                        break;
                    case COMMAND_TYPE_DELETE:
                        feedbackList.add(DELETE_INDEX);
                        break;
                    case COMMAND_TYPE_EDIT:
                        feedbackList.add(EDIT_DESCRIPTION);
                        break;
                    case COMMAND_TYPE_VIEW:
                        feedbackList.add(VIEW_DESCRIPTION);
                        break;
                    case COMMAND_TYPE_SEARCH:
                        feedbackList.add(SEARCH_DESCRIPTION);
                        break;
                }

            }
        }
        if (feedbackList.isEmpty()) {
            feedbackList.add(NO_MATCH);
        }
    }

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
