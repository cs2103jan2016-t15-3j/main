package separator;

/**
 * Created by lifengshuang on 3/28/16.
 */
public class InputSeparator {

    public enum KeywordType {
        DESCRIPTION, START_TIME, END_TIME, START_DATE, END_DATE, FROM, START, END,
        DONE, UNDONE, OVERDUE, ALL, DEADLINES, EVENTS, TASKS, TO, FILEPATH
    }


    //    private boolean isValid;
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
//        this.isValid = isValid(command);
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

//    private boolean isValid(String command) {
////        //two space is not allowed
////        if (command.contains("  ")) {
////            return false;
////        }
////        //Can't start with space
////        if (command.startsWith(" ")) {
////            return false;
////        }
//        return true;
//    }

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
                    parameterPosition++;
                    return type;
                }
            }
            String twoWord = input[keywordPosition] + " " + input[keywordPosition + 1];
            for (KeywordType type : KeywordType.values()) {
                if (twoWord.equalsIgnoreCase(enumNameToString(type))) {
                    parameterPosition += 2;
                    return type;
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

//    public boolean isValid() {
//        return isValid;
//    }

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
}
