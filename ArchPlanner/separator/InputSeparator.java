package separator;

import prompt.Prompt;

import java.util.HashMap;

/**
 * @@author A0149647N
 * InputSeparator return the parsed components of a command
 * This class serves for all commands except add command
 */
public class InputSeparator {

    public enum KeywordType {
        DESCRIPTION, DONE, UNDONE, OVERDUE, ALL, DEADLINES, EVENTS, TASKS, TO, FILEPATH,
        START_TIME, END_TIME, START_DATE, END_DATE, FROM, START, END;

        @Override
        public String toString() {
            return this.name().toLowerCase().replace('_', ' ');
        }
    }

    private static final HashMap<Prompt.CommandType, KeywordType[]> commandMap = new HashMap<>();

    private static final int INITIAL_PARSE_INDEX = 1;
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    private static final String EMPTY_STRING = "";
    private static final String SPACE_STRING = " ";

    private Prompt.CommandType commandType;
    private int wordCount;
    private Integer id;
    private Integer secondId;
    private KeywordType keywordType;
    private String parameter;
    private int idPosition = INITIAL_PARSE_INDEX;
    private int keywordPosition = INITIAL_PARSE_INDEX;
    private int parameterPosition = INITIAL_PARSE_INDEX;
    private boolean endWithSpace;

    /**
     * Parse ID, keyword and parameter
     *
     * @param command This is user's input
     */
    public InputSeparator(String command) {
        commandType = determineCommandType(command);
        String[] breakUserInput = command.split(STRING_MULTIPLE_WHITESPACE);
        this.wordCount = breakUserInput.length;
        this.id = parseID(breakUserInput);
        this.keywordType = parseKeyword(breakUserInput);
        this.secondId = parseSecondId(breakUserInput);
        this.parameter = parseParameter(breakUserInput);
        this.endWithSpace = command.endsWith(SPACE_STRING);
    }

    /**
     * Parse second ID for delete, done and undone command
     *
     * @param input This is user's input split by space
     * @return Parsed ID, null if doesn't exist
     */
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

    /**
     * Get the partial keyword from current input
     * This method serves for parser to get partial word
     *
     * @return Auto complete partial keyword
     */
    public String getPartialKeyword() {
        if (keywordType != null) {
            return EMPTY_STRING;
        }
        for (KeywordType type : KeywordType.values()) {
            if (commandHasKeyword(commandType, type) && type.toString().startsWith(parameter)) {
                return getAutoCompleteWord(type);
            }
        }
        return EMPTY_STRING;
    }

    /**
     * Get the auto complete word according to the type and current typing status
     *
     * @param type the keyword type
     * @return The auto complete word
     */
    private String getAutoCompleteWord(KeywordType type) {
        String[] splitType = type.toString().split(STRING_MULTIPLE_WHITESPACE);
        if (splitType.length > 1) {
            if (parameter.contains(SPACE_STRING)) {
                return splitType[1];
            } else {
                return splitType[0];
            }
        }
        return type.toString();
    }

    /**
     * Parse ID from user's input if exist
     *
     * @param input This is the user's input split by space
     * @return Parsed ID, null if doesn't exist
     */
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

    /**
     * Parse keyword from user's input if exist
     *
     * @param input This is the user's input split by space
     * @return Parsed keyword, null if doesn't exist
     */
    private KeywordType parseKeyword(String[] input) {
        try {
            for (KeywordType type : KeywordType.values()) {
                if (input[keywordPosition].equalsIgnoreCase(type.toString())
                        && commandHasKeyword(commandType, type)) {
                    parameterPosition++;
                    return type;
                }
            }
            String twoWord = input[keywordPosition] + SPACE_STRING + input[keywordPosition + 1];
            for (KeywordType type : KeywordType.values()) {
                if (twoWord.equalsIgnoreCase(type.toString())
                        && commandHasKeyword(commandType, type)) {
                    parameterPosition += 2;
                    return type;
                }
            }
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Parse parameter from user's input if exist
     *
     * @param input This is the user's input split by space
     * @return Parsed parameter, null if doesn't exist
     */
    private String parseParameter(String[] input) {
        if (input.length == parameterPosition) {
            return null;
        }
        String result = EMPTY_STRING;
        for (int i = parameterPosition; i < input.length; i++) {
            result += input[i] + SPACE_STRING;
        }
        return result.trim();
    }

    /**
     * Check if the command only has a ID
     *
     * @return True if contains ID only
     */
    public boolean isIdOnly() {
        return id != null && keywordType == null && parameter == null;
    }

    /**
     * Check if the ID's range is between 1 and the current view list
     *
     * @param id           User typed ID
     * @param viewListSize The size of current view list
     * @return True if ID's range is valid
     */
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

    /**
     * Check whether user may want to input two ID
     */
    public boolean mayHaveTwoValidID() {
        if (parameter == null) {
            return !(id != null && secondId != null && id >= secondId);
        } else if (id != null && KeywordType.TO.toString().startsWith(parameter.toLowerCase())) {
            return true;
        }
        return false;
    }

    /**
     * Determine the command type of the command
     *
     * @param input User's command
     * @return Recognized commant type
     */
    private Prompt.CommandType determineCommandType(String input) {
        String commandTypeString = input.trim().split(STRING_MULTIPLE_WHITESPACE)[0];
        if (commandTypeString.isEmpty()) {
            return Prompt.CommandType.UNKNOWN;
        }
        for (Prompt.CommandType type : Prompt.CommandType.values()) {
            if (commandTypeString.equalsIgnoreCase(type.toString())) {
                return type;
            }
        }
        return Prompt.CommandType.UNKNOWN;
    }

    /**
     * Check whether the keyword is valid for the specific command type
     *
     * @param commandType This is the command type
     * @param keywordType This is the keyword type
     * @return True if the keyword is valid for the command type
     */
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

    /**
     * Initialize mapping from command type to its keywords
     */
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

    public int getWordCount() {
        return wordCount;
    }

    public boolean isEndWithSpace() {
        return endWithSpace;
    }


}
