package interpreter.separator;

import interpreter.separator.AddInputSeparator.AddKeyWordType;

/**
 * This class is used to separate the given string into 5 regions.
 * Regions: descriptionRegion, keywordRegion, firstDateRegion, secondDateRegion, tagsRegion.
 * 
 * example:
 * user input = add holiday@Malaysia from next friday 10am to next sunday 9pm #holiday #leisure
 * descriptionRegion = holiday@Malaysia
 * keywordRegion = from
 * firstDateRegion = next friday 10am
 * secondDateRegion =  next sunday 9pm
 * tagsRegion = #holiday #leisure
 * 
 * descriptionRegion is compulsory before any other region can be detected.
 * keywordRegion are on, by, from.
 * firstDateRegion is detected right after keyword.
 * secondDateRegion is detected right after "to" only if firstDateRegion is detected and keyword is "from".
 * tagsRegion are detected as all string after the first " #".
 * 
 * @@author A0140034B
 */
public class AddRegionSeparator {
   
    private static final int ONE_SPACE = 1;
    private static final int TWO_SPACE = 2;
    private static final int NOT_FOUND = -1;
    private static final int INDEX_INITIAL = 0;
    private static final int INDEX_INVALID_TAG = 0;
    
    private static final String STRING_EMPTY = "";
    private static final String STRING_SINGLE_SPACE = " ";
    
    private static final String KEYWORD_TAG = " #";
    private static final String KEYWORD_TO = " to ";
    
    private String _descriptionRegion;
    private String _keyWordRegion;
    private String _firstDateRegion;
    private String _secondDateRegion;
    private String _tagRegion;
    
    /**
     * Separate the given string into 5 regions: descriptionRegion, keywordRegion, 
     * firstDateRegion, secondDateRegion, tagsRegion.
     * 
     * @param userInput    the string that will be separated to the different regions.
     */
    public AddRegionSeparator(String userInput) {
        _keyWordRegion = extractKeyWordRegion(userInput);
        _tagRegion = extractTagRegion(userInput, _keyWordRegion);
        _descriptionRegion = extractDescriptionRegion(userInput, _keyWordRegion, _tagRegion);
        
        String dateRegion = extractDateRegion(userInput, _keyWordRegion, _tagRegion);
        _firstDateRegion = extractFirstDateRegion(dateRegion, _keyWordRegion);
        _secondDateRegion = extractSecondDateRegion(dateRegion, _firstDateRegion);
        
        _descriptionRegion = _descriptionRegion.trim();
        _keyWordRegion = _keyWordRegion.trim();
        _firstDateRegion = _firstDateRegion.trim();
        _secondDateRegion = _secondDateRegion.trim();
    }
    
    /**
     * Extract the keyWordRegion by finding the last occurrence of a keyWord.
     * Return empty string if keyWord cannot be found.
     * 
     * @param input     the string used to extract the keyWordRegion.
     * @return          the keyWordRegion extracted from input.
     */
    private String extractKeyWordRegion(String input) {
        int position = INDEX_INITIAL;
        AddKeyWordType keyWord = AddKeyWordType.UNKNOWN;
        input = input.toUpperCase();
        
        for (AddKeyWordType type : AddKeyWordType.values()) {
            String typeInString = STRING_SINGLE_SPACE + type.toString() + STRING_SINGLE_SPACE;
            if (input.contains(typeInString) && input.lastIndexOf(typeInString) > position) {
                position = input.lastIndexOf(type.toString());
                keyWord = type;
            }
        }

        if (keyWord == AddKeyWordType.UNKNOWN) {
            return STRING_EMPTY;
        } else {
            return keyWord.toString().toLowerCase();
        }
    }
    
    /**
     * Extract the tagRegion by finding the first occurrence of a " #" after the keyWordRegion.
     * If there is no KeyWordRegion, the first occurrence of a " #" in the given string will be return.
     * Return empty string if " #" cannot be found.
     * 
     * @param input             the string used to extract the tagRegion.
     * @param keyWordRegion     the string that separate the descriptionRegion from the tagRegion.
     * @return                  the tagRegion extracted from input.
     */
    private String extractTagRegion(String input, String keyWordRegion) {
        int indexOfTag = getTagRegionIndex(input);
        int indexOfKeyWord = input.lastIndexOf(STRING_SINGLE_SPACE + keyWordRegion + STRING_SINGLE_SPACE);
        if (indexOfTag <= INDEX_INVALID_TAG) {
            return STRING_EMPTY;
        }
        if (keyWordRegion.isEmpty()) {
            return input.substring(indexOfTag + ONE_SPACE);
        }
        
        String afterKeyWord = input.substring(indexOfKeyWord + keyWordRegion.length() + ONE_SPACE);
        indexOfTag = getTagRegionIndex(afterKeyWord);
        if (indexOfTag <= NOT_FOUND) {
            return STRING_EMPTY;
        } else {
            return afterKeyWord.substring(indexOfTag + ONE_SPACE);
        }
    }
    
    /**
     * Extract the descriptionRegion by getting the string before the keyWordRegion.
     * If there is no keyWordRegion, the string before the TagRegion will be return.
     * If there is no keyWordRegion and TagRegion, the given string will be return.
     * 
     * @param input             the string used to extract the descriptionRegion.
     * @param keyWordRegion     the string used to determine the ending point of the descriptionRegion.
     * @param tagRegion         the string used to determine the ending point of the descriptionRegion.
     * @return                  the descriptionRegion extracted from input.
     */
    private String extractDescriptionRegion(String input, String keyWordRegion, String tagRegion) {
        if (keyWordRegion.isEmpty()) {
            if (tagRegion.isEmpty()) {
                return input;
            } else {
                return input.substring(INDEX_INITIAL, input.lastIndexOf(tagRegion));
            }
        } else {
            int indexOfKeyWord = input.lastIndexOf(STRING_SINGLE_SPACE + keyWordRegion + STRING_SINGLE_SPACE);
            return input.substring(INDEX_INITIAL, indexOfKeyWord);
        }
    }
    
    /**
     * Extract the dateRegion by getting the string after the keyWordRegion to before the tagRegion.
     * If there is no keyWordRegion, empty string will be return as dateRegion cannot exist with keyWordRegion
     * 
     * @param input             the string used to extract the dateRegion.
     * @param keyWordRegion     the string used to determine the starting point of the dateRegion.
     * @param tagRegion         the string that ends the dateRegion.
     * @return                  the dateRegion extracted from input.
     */
    private String extractDateRegion(String input, String keyWordRegion, String tagRegion) {
        if (keyWordRegion.isEmpty()) {
            return STRING_EMPTY;
        }
        
        int indexOfTag = input.lastIndexOf(tagRegion);
        int indexOfKeyWord = input.substring(INDEX_INITIAL, indexOfTag).lastIndexOf(STRING_SINGLE_SPACE + keyWordRegion + STRING_SINGLE_SPACE);
        if (tagRegion.isEmpty()) {
            return input.substring(indexOfKeyWord + keyWordRegion.length() + TWO_SPACE);
        } else {
            return input.substring(indexOfKeyWord + keyWordRegion.length() + TWO_SPACE, indexOfTag);
        }
    }
    
    /**
     * Extract the firstDateRegion by getting the string in dateRegion before the "to" inclusive.
     * If the keyWordRegion is not "from", firstDateRegion is assumed to be dateRegion.
     * If "to" is not found in the dateRegion, firstDateRegion is assumed to be dateRegion.
     * 
     * @param dateRegion        the string used to extract the firstDateRegion.
     * @param keyWordRegion     the string that determine if a secondDate might be present.
     * @return                  the firstDateRegion extracted from dateRegion.
     */
    private String extractFirstDateRegion(String dateRegion, String keyWordRegion) {
        if (keyWordRegion.equalsIgnoreCase(AddKeyWordType.FROM.toString())) {
            int keyWordToPosition = dateRegion.indexOf(KEYWORD_TO);
            if (keyWordToPosition > INDEX_INITIAL) {
                return dateRegion.substring(INDEX_INITIAL, keyWordToPosition + KEYWORD_TO.length());
            } else {
                return dateRegion;
            }
        } else {
            return dateRegion;
        }
    }
    
    /**
     * Extract the secondDateRegion by getting the string in dateRegion after the firstDateRegion.
     * 
     * @param dateRegion        the string used to extract the firstDateRegion.
     * @param firstDateRegion   the string used to determine the starting point of the secondDateRegion.
     * @return                  the secondDateRegion extracted from dateRegion.
     */
    private String extractSecondDateRegion(String dateRegion, String firstDateRegion) {
        if (dateRegion.length() <= firstDateRegion.length()) {
            return STRING_EMPTY;
        }
        return dateRegion.substring(firstDateRegion.length());
    }
    
    /**
     * Return the index of the tagRegion in the given string.
     * Return -1 if no tagRegion is found.
     * 
     * @param input        the string used to find the index of the tagRegion.
     * @return             the index of the tagRegion in the given string.
     */
    private int getTagRegionIndex(String input) {
        if (input.contains(KEYWORD_TAG)) {
            return input.indexOf(KEYWORD_TAG);
        } else {
            return NOT_FOUND;
        }
    }
    
    /**
     * Return the descriptionRegion extracted form the string.
     * 
     * @return  the descriptionRegion.
     */
    public String getDescriptionRegion() {
        return _descriptionRegion;
    }
    
    /**
     * Return the keyWordRegion extracted form the string.
     * 
     * @return  the keyWordRegion.
     */
    public String getKeyWordRegion() {
        return _keyWordRegion;
    }
    
    /**
     * Return the firstDateRegion extracted form the string.
     * 
     * @return  the firstDateRegion.
     */
    public String getFirstDateRegion() {
        return _firstDateRegion;
    }
    
    /**
     * Return the secondDateRegion extracted form the string.
     * 
     * @return  the secondDateRegion.
     */
    public String getSecondDateRegion() {
        return _secondDateRegion;
    }
    
    /**
     * Return the tagRegion extracted form the string.
     * 
     * @return  the tagRegion.
     */
    public String getTagRegion() {
        return _tagRegion;
    }
}
