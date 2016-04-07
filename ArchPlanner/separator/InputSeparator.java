package separator;

import prompt.Prompt;

import java.util.HashMap;

/**
 * Created by lifengshuang on 3/28/16.
 */
public class InputSeparator {

    public enum KeywordType {
        DESCRIPTION, DONE, UNDONE, OVERDUE, ALL, DEADLINES, EVENTS, TASKS, TO, FILEPATH,
        START_TIME, END_TIME, START_DATE, END_DATE, FROM, START, END
    }

    private static final HashMap<Prompt.CommandType, KeywordType[]> commandMap = new HashMap<>();

    private String command;
    private Prompt.CommandType commandType;
    private int wordCount;
    private Integer id;
    private Integer secondId;
    private KeywordType keywordType;
    private String parameter;
    private int idPosition = 1;
    private int keywordPosition = 1;
    private int parameterPosition = 1;
    private boolean endWithSpace;

    public InputSeparator(String command) {
        this.command = command;
        commandType = determineCommandType(command);
        String[] breakUserInput = command.split("\\s+");
        this.wordCount = breakUserInput.length;
        this.id = parseID(breakUserInput);
        this.keywordType = parseKeyword(breakUserInput);
        this.secondId = parseSecondId(breakUserInput);
        this.parameter = parseParameter(breakUserInput);
        this.endWithSpace = command.endsWith(" ");
    }

    private Integer parseSecondId(String[] input) {
        try {
            if (keywordType != null && keywordType == KeywordType.TO) {
                Integer result = Integer.parseInt(input[parameterPosition]);
                parameterPosition++;
                return result;
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public String getPartialKeyword() {
        if (keywordType == null) {
            for (KeywordType type : KeywordType.values()) {
                if (commandHasKeyword(commandType, type)) {
                    if (enumNameToString(type).startsWith(parameter)) {
                        String[] splitType = enumNameToString(type).split("\\s+");
                        if (splitType.length > 1) {
                            if (parameter.contains(" ")) {
                                return splitType[1];
                            } else {
                                return splitType[0];
                            }
                        }
                        return enumNameToString(type);
                    }
                }
            }
        }
        return "";
    }

    private Integer parseID(String[] input) {
        try {
            Integer result = Integer.parseInt(input[idPosition]);
            keywordPosition++;
            parameterPosition++;
            return result;
        } catch (NumberFormatException e) {
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private KeywordType parseKeyword(String[] input) {
        try {
            for (KeywordType type : KeywordType.values()) {
                if (input[keywordPosition].equalsIgnoreCase(enumNameToString(type))) {
                    if (commandHasKeyword(commandType, type)) {
                        parameterPosition++;
                        return type;
                    }
                }
            }
            String twoWord = input[keywordPosition] + " " + input[keywordPosition + 1];
            for (KeywordType type : KeywordType.values()) {
                if (twoWord.equalsIgnoreCase(enumNameToString(type))) {
                    if (commandHasKeyword(commandType, type)) {
                        parameterPosition += 2;
                        return type;
                    }
                }
            }
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private String parseParameter(String[] input) {
        if (input.length == parameterPosition) {
            return null;
        }
        String result = "";
        for (int i = parameterPosition; i < input.length; i++) {
            result += input[i] + " ";
        }
        return result.trim();
    }

    private String enumNameToString(KeywordType type) {
        String result = type.name().toLowerCase();
        result = result.replace('_', ' ');
        return result;
    }

    public Integer getID() {
        return this.id;
    }

    public Integer getSecondId() {
        return secondId;
    }

    public KeywordType getKeywordType() {
        return this.keywordType;
    }

    public String getParameter() {
        return this.parameter;
    }

    public boolean isIdOnly() {
        return id != null && keywordType == null && parameter == null;
    }

    public boolean isIdRangeValid(Integer id, int viewListSize) {
        return id != null && id > 0 && id <= viewListSize;
    }

    public boolean hasTwoValidId(int viewListSize) {
        try {

            return isIdRangeValid(id, viewListSize)
                    && isIdRangeValid(secondId, viewListSize)
                    && id < secondId
                    && keywordType == KeywordType.TO
                    && parameter == null;

        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean mayHaveTwoValidID() {
        if (parameter == null) {
            if (id != null && secondId != null && id >= secondId) {
                return false;
            }
            return true;
        } else if (id != null && parameter.equals("t")) {
            return true;
        }
        return false;
    }

    public int getWordCount() {
        return wordCount;
    }

    public boolean isEndWithSpace() {
        return endWithSpace;
    }

    private Prompt.CommandType determineCommandType(String input) {
        String commandTypeString = input.trim().split("\\s+")[0];
        if (commandTypeString.isEmpty()) {
            return Prompt.CommandType.UNKNOWN;
        }
        for (Prompt.CommandType type : Prompt.CommandType.values()) {
            if (commandTypeString.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return Prompt.CommandType.UNKNOWN;
    }

    private boolean commandHasKeyword(Prompt.CommandType commandType, KeywordType keywordType) {
        if (commandMap.isEmpty()) {
            initCommandMap();
        }
        KeywordType[] keywordTypes = commandMap.get(commandType);
        for (KeywordType type : keywordTypes) {
            if (type == keywordType) {
                return true;
            }
        }
        return false;
    }

    private String keywordFollowSpace(KeywordType type) {
        switch (type) {
            case ALL:
            case DONE:
            case UNDONE:
            case OVERDUE:
            case DEADLINES:
            case TASKS:
            case EVENTS:
                return "";
            default:
                return " ";
        }
    }

    private void initCommandMap() {
        commandMap.put(Prompt.CommandType.ADD, new KeywordType[]{});
        commandMap.put(Prompt.CommandType.DELETE, new KeywordType[]{KeywordType.TO});
        commandMap.put(Prompt.CommandType.DONE, new KeywordType[]{KeywordType.TO});
        commandMap.put(Prompt.CommandType.UNDONE, new KeywordType[]{KeywordType.TO});
        commandMap.put(Prompt.CommandType.SET, new KeywordType[]{KeywordType.FILEPATH});
        commandMap.put(Prompt.CommandType.EDIT, new KeywordType[]
                {KeywordType.DESCRIPTION, KeywordType.END, KeywordType.START, KeywordType.FROM});
        commandMap.put(Prompt.CommandType.VIEW, new KeywordType[]
                {KeywordType.ALL, KeywordType.DONE, KeywordType.UNDONE, KeywordType.OVERDUE,
                        KeywordType.DEADLINES, KeywordType.TASKS, KeywordType.EVENTS,
                        KeywordType.DESCRIPTION, KeywordType.FROM, KeywordType.START_DATE, KeywordType.START_TIME,
                        KeywordType.END_DATE, KeywordType.END_TIME});
    }


}
