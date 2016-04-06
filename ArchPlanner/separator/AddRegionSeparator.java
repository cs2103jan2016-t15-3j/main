package separator;

import separator.AddInputSeparator.AddKeyWordType;

public class AddRegionSeparator {
    
    private static final int ONE_SPACE = 1;
    private static final int TWO_SPACE = 2;
    private static final int NOT_FOUND = -1;
    private static final int INITIAL_INDEX = 0;
    
    private static final String STRING_EMPTY = "";
    private static final String STRING_SINGLE_WHITESPACE = " ";
    
    private static final String KEYWORD_TAG = " #";
    private static final String KEYWORD_TO = " to ";
    
    private String _description;
    private String _firstDateRegion;
    private String _secondDateRegion;
    private String _tagRegion;
    private String _keyWord;
    
    public AddRegionSeparator(String userInput) {
        _description = STRING_EMPTY;
        _firstDateRegion = STRING_EMPTY;
        _secondDateRegion = STRING_EMPTY;
        _tagRegion = STRING_EMPTY;
        _keyWord = STRING_EMPTY;
        
        int keyWordPosition = getKeyWordPosition(userInput);
        int keyWordToPosition = INITIAL_INDEX;
        int tagPosition = INITIAL_INDEX;
        if (keyWordPosition > INITIAL_INDEX) {
            _description = userInput.substring(INITIAL_INDEX, keyWordPosition);
            _keyWord = findKeyWord(userInput);
            
            String dateRegion;
            String afterKeyWord = userInput.substring(keyWordPosition + _keyWord.length() + TWO_SPACE);
            tagPosition = getTagPosition(afterKeyWord);
            if (tagPosition >= INITIAL_INDEX) {
                dateRegion = afterKeyWord.substring(INITIAL_INDEX, tagPosition);
                _tagRegion = afterKeyWord.substring(tagPosition + ONE_SPACE);
            } else {
                dateRegion = afterKeyWord;
            }
            dateRegion = dateRegion.toLowerCase();
            
            if (_keyWord.equalsIgnoreCase(AddKeyWordType.FROM.name())) {
                keyWordToPosition = dateRegion.indexOf(KEYWORD_TO);

                if (keyWordToPosition > INITIAL_INDEX) {
                    _firstDateRegion = dateRegion.substring(0, keyWordToPosition + KEYWORD_TO.length());
                    _secondDateRegion = dateRegion.substring(keyWordToPosition + KEYWORD_TO.length());
                } else {
                    _firstDateRegion = dateRegion;
                }
            } else {
                _firstDateRegion = dateRegion;
            }
        } else {
            tagPosition = getTagPosition(userInput);
            if (tagPosition > INITIAL_INDEX) {
                _description = userInput.substring(INITIAL_INDEX, tagPosition);
                _tagRegion = userInput.substring(tagPosition + ONE_SPACE);
            } else {
                _description = userInput.trim();
            }
        }
        
        _description = _description.trim();
        _keyWord = _keyWord.trim();
        _firstDateRegion = _firstDateRegion.trim();
        _secondDateRegion = _secondDateRegion.trim();
        
        System.out.println();
        System.out.println("AddRegionSeparator------------------------------------------");
        System.out.println("Description: |" + _description + "|");
        System.out.println("startDateRegion: |" + _firstDateRegion + "|");
        System.out.println("endDateRegion: |" + _secondDateRegion + "|");
        System.out.println("TagRegion: |" + _tagRegion + "|");
        System.out.println("KeyWord: |" + _keyWord + "|");
        System.out.println("------------------------------------------AddRegionSeparator");
    }
    
    public String getDescription() {
        return _description;
    }
    
    public String getFirstDateRegion() {
        return _firstDateRegion;
    }
    
    public String getSecondDateRegion() {
        return _secondDateRegion;
    }
    
    public String getTagRegion() {
        return _tagRegion;
    }
    
    public String getKeyWord() {
        return _keyWord;
    }
    
    private int getKeyWordPosition(String input) {
        input = input.toLowerCase();
        String keyWord = findKeyWord(input);
        if (!keyWord.isEmpty()) {
            return input.lastIndexOf(STRING_SINGLE_WHITESPACE + keyWord + STRING_SINGLE_WHITESPACE);
        } else {
            return NOT_FOUND;
        }
    }
    
    private int getTagPosition(String input) {
        if (input.contains(KEYWORD_TAG)) {
            return input.indexOf(KEYWORD_TAG);
        } else {
            return NOT_FOUND;
        }
    }
    
    private String findKeyWord(String input) {
        int position = INITIAL_INDEX;
        AddKeyWordType keyWord = AddKeyWordType.UNKNOWN;
        input = input.toUpperCase();
        
        for (AddKeyWordType type : AddKeyWordType.values()) {
            String typeInString = STRING_SINGLE_WHITESPACE + type.name() + STRING_SINGLE_WHITESPACE;
            if (input.contains(typeInString) && input.lastIndexOf(typeInString) > position) {
                position = input.lastIndexOf(type.name());
                keyWord = type;
            }
        }

        if (keyWord == AddKeyWordType.UNKNOWN) {
            return STRING_EMPTY;
        } else {
            return keyWord.name().toLowerCase();
        }
    }
}
