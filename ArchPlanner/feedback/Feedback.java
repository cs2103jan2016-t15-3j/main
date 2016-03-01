package feedback;

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
    private static final int COMMAND_TYPE_EXIT = 8;

    private static final String[] ALL_COMMANDS =
            {"add", "delete", "edit",
                    "view", "done", "undone",
                    "undo", "redo", "exit"};

    private static final String ADD = "add";
    private static final String ADD_DESCRIPTION = "add <description>";
    private static final String ADD_DESCRIPTION_TIME = "add <description> <time>";

    private static final String DELETE = "delete <index>";

    private static final String EDIT = "edit";
    private static final String EDIT_DESCRIPTION = "edit <index> <description>";
    private static final String EDIT_TIME = "edit <index> <time>";
    private static final String EDIT_DESCRIPTION_TIME = "edit <index> <description> <time>";

    private static final String VIEW = "view";
    private static final String VIEW_DESCRIPTION = "view <description>";
    //more view things

    private static final String DONE = "done <index>";
    private static final String UNDONE = "undone <index>";
    private static final String UNDO = "undo";
    private static final String REDO = "redo";
    private static final String EXIT = "exit";

    private static final String NO_MATCH = "No pattern matched";

    private static ArrayList<String> feedbackList = new ArrayList<String>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            ArrayList<String> list = onTextChanged(input);
            for (String s : list) {
                System.out.println(s);
            }
        }
    }

    public static ArrayList<String> onTextChanged(String input) {
        feedbackList = new ArrayList<String>();
        int commandType = detectCommandType(input);
        switch (commandType) {
            case COMMAND_TYPE_UNKNOWN:
                handleUnknownCommand(input);
                break;
            case COMMAND_TYPE_ADD:
                break;
            case COMMAND_TYPE_DELETE:
                feedbackList.add(DELETE);
                break;
            case COMMAND_TYPE_EDIT:
                break;
            case COMMAND_TYPE_VIEW:
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
            case COMMAND_TYPE_EXIT:
                feedbackList.add(EXIT);
                break;
        }
        return feedbackList;
    }

    public static boolean onEnterPressed(String input) {
        int commandType = detectCommandType(input);
        switch (commandType) {
            case COMMAND_TYPE_UNKNOWN:
                return false;
            case COMMAND_TYPE_ADD:
//                Logic.addTask(input.substring(ADD.length() + 1), null, null, null);
                break;
            case COMMAND_TYPE_DELETE:
//                Logic.deleteTask(Integer.parseInt(input.substring(DELETE.length() + 1)));
                break;
            case COMMAND_TYPE_EDIT:
//
                break;
            case COMMAND_TYPE_VIEW:
//
                break;
            case COMMAND_TYPE_DONE:
//
                break;
            case COMMAND_TYPE_UNDONE:
                break;
            case COMMAND_TYPE_UNDO:
                break;
            case COMMAND_TYPE_REDO:
                break;
            case COMMAND_TYPE_EXIT:
                break;
        }
        return true;
    }

    private static void handleUnknownCommand(String input) {
        if (input.isEmpty()) {
            return;
        }
        if (ADD.startsWith(input)) {
            feedbackList.add(ADD_DESCRIPTION);
        }
        if (DELETE.startsWith(input)) {
            feedbackList.add(DELETE);
        }
        if (EDIT.startsWith(input)) {
            feedbackList.add(EDIT_DESCRIPTION);
        }
        if (VIEW.startsWith(input)) {
            feedbackList.add(VIEW_DESCRIPTION);
        }
        if (UNDO.startsWith(input)) {
            feedbackList.add(UNDO);
            feedbackList.add(UNDONE);
        }
        if (REDO.startsWith(input)) {
            feedbackList.add(REDO);
        }
        if (DONE.startsWith(input)) {
            feedbackList.add(DONE);
        }
        if (UNDONE.startsWith(input)) {
            feedbackList.add(UNDONE);
        }
        if (EXIT.startsWith(input)) {
            feedbackList.add(EXIT);
        }
        if (feedbackList.isEmpty()) {
            feedbackList.add(NO_MATCH);
        }
    }

    private static int detectCommandType(String input) {
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
