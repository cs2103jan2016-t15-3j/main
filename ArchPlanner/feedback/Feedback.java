package feedback;

import paser.*;
import logic.commands.*;

import java.util.ArrayList;

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
    private static final int COMMAND_TYPE_SEARCH = 8;
    private static final int COMMAND_TYPE_EXIT = 9;

    private static final String[] ALL_COMMANDS =
            {"add", "delete", "edit",
                    "view", "done", "undone",
                    "undo", "redo", "search", "exit"};

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

    //    private static final String SORT = "sort";
//    private static final String SORT_BY_TYPE = "sort <DESCRIPTION | TAG | START_DATE_TIME | END_DATE_TIME | COMPLETION | OVERDUE>";
    private static final String SEARCH_DESCRIPTION = "search <description>";

    private static final String EXIT = "exit";

    private static final String NO_MATCH = "No pattern matched";
    private static final String EMPTY_INPUT = "add | delete | edit | view | search | done | undone | undo | redo | exit";


    private static ArrayList<String> feedbackList = new ArrayList<String>();

    public ArrayList<String> onTextChanged(String input) {
        feedbackList = new ArrayList<>();
        if (input == null) {
            return feedbackList;
        }
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
            case COMMAND_TYPE_SEARCH:
                feedbackList.add(SEARCH_DESCRIPTION);
                break;
            case COMMAND_TYPE_EXIT:
                feedbackList.add(EXIT);
                break;
        }
        return feedbackList;
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
