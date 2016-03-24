package feedback;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class InputParameters {
    
    public enum KeyWordType {
        ON, BY, FROM, UNKNOWN
    };
    
    public final String[] KEY_WORDS = {"on", "by", "from"};
    
    private final String STRING_SINGLE_WHITESPACE = " ";
    private final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    
    private String _description;
    private KeyWordType _keyWord;
    private KeyWordType _partialKeyWord;
    private Date _startDateTime;
    private Date _endDateTime;
    private LocalDate _startDate;
    private LocalTime _startTime;
    private LocalDate _endDate;
    private LocalTime _endTime;
    private String[] _tags;
    
    private boolean _hasSpace;
    private boolean _hasDescription;
    private boolean _hasKeyWord;
    private boolean _hasPartialKeyWord;
    private boolean _hasStartDate;
    private boolean _hasStartTime;
    private boolean _hasEndDate;
    private boolean _hasEndTime;
    private boolean _hasTag;
    private boolean _hasValidTag;
    
    public InputParameters(String userInput) {
        
        BreakRegion breakUserInput = new BreakRegion(userInput);
        BreakDateRegion breakDateRegion = new BreakDateRegion(breakUserInput.getDateRegion(), 
                                          determineKeyWordType(breakUserInput.getKeyWord()));
        
        _hasSpace = isSpacePresent(userInput);
        _hasDescription = !breakUserInput.getDescription().isEmpty();
        _hasKeyWord = !breakUserInput.getKeyWord().isEmpty();
        _hasPartialKeyWord = isPartialKeyWordPresent(userInput);
        _hasTag = !breakUserInput.getTagRegion().isEmpty();
        _hasValidTag = isValidTag(breakUserInput.getTagRegion());
        _hasStartDate = breakDateRegion.hasStartDate();
        _hasStartTime = breakDateRegion.hasStartTime();
        _hasEndDate = breakDateRegion.hasEndDate();
        _hasEndTime = breakDateRegion.hasEndTime();
        
        _description = breakUserInput.getDescription();
        _keyWord = determineKeyWordType(breakUserInput.getKeyWord());
        _partialKeyWord = searchPartialKeyWord(userInput);
        _startDateTime = breakDateRegion.getStartDateTime();
        _endDateTime = breakDateRegion.getEndDateTime();
        if (_startDateTime != null) {
            _startDate = _startDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            _startTime = _startDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
        if (_endDateTime != null) {
            _endDate = _endDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            _endTime = _endDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
        _tags = getTags(breakUserInput.getTagRegion());
        
        System.out.println("InputParameters------------------------------------------");
        System.out.println("userInput: " + userInput + "|");
        System.out.println("Description: " + _description + "|");
        System.out.println("keyWord: " + _keyWord + "|");
        System.out.println("partialKeyWord: " + _partialKeyWord + "|");
        System.out.println("startDateTime: " + _startDateTime + "|");
        System.out.println("endDateTime: " + _endDateTime + "|");
        System.out.println("startDate: " + _startDate + "|");
        System.out.println("startTime: " + _startTime + "|");
        System.out.println("endDate: " + _endDate + "|");
        System.out.println("endTime: " + _endTime + "|");
        System.out.println("------------------------------------------InputParameters");
    }
    
    private boolean isSpacePresent(String userInput) {
        if (userInput.length() <= 0) {
            return false;
        }
        return userInput.substring(userInput.length() - 1).equals(STRING_SINGLE_WHITESPACE);
    }
    
    private boolean isValidTag(String tagRegion) {
        System.out.println("Tag Region: " + tagRegion);
        if (tagRegion.startsWith("# ") || tagRegion.contains(" # ")) {
            return false;
        }      
        String[] tags = tagRegion.split(STRING_MULTIPLE_WHITESPACE);
        for (int i = 0; i < tags.length; i++) {
            System.out.println("tag - " + tags[i]);
            if(!tags[i].startsWith("#")) {
                return false;
            }
        }        
        return true;
    }
    
    private String[] getTags(String tagRegion) {
        if (isValidTag(tagRegion)) {
            return tagRegion.split(STRING_MULTIPLE_WHITESPACE);
        } else {
            return null;
        }
    }
    
    private boolean isPartialKeyWordPresent(String userInput) {
        return searchPartialKeyWord(userInput) != KeyWordType.UNKNOWN;
    }
    
    private KeyWordType searchPartialKeyWord(String userInput) {
        if (userInput.isEmpty() || isSpacePresent(userInput)) {
            return KeyWordType.UNKNOWN;
        }
        String[] splitInput = userInput.split(STRING_MULTIPLE_WHITESPACE);
        if (splitInput.length < 3) {
            return KeyWordType.UNKNOWN;
        }
        KeyWordType foundKey = KeyWordType.UNKNOWN;
        for (int i = 0; i < KEY_WORDS.length; i++) {
            if (KEY_WORDS[i].startsWith(splitInput[splitInput.length - 1])) {
                foundKey = determineKeyWordType(KEY_WORDS[i]);
            }
        }
        return foundKey;
    }

    private KeyWordType determineKeyWordType(String commandTypeString) {
        if (commandTypeString.isEmpty()) {
            return KeyWordType.UNKNOWN;
        }

        if (commandTypeString.equalsIgnoreCase(KEY_WORDS[0])) {
            return KeyWordType.ON;
        } else if (commandTypeString.equalsIgnoreCase(KEY_WORDS[1])) {
            return KeyWordType.BY;
        } else if (commandTypeString.equalsIgnoreCase(KEY_WORDS[2])) {
            return KeyWordType.FROM;
        } else {
            return KeyWordType.UNKNOWN;
        }
    }
    
    public boolean hasSpace() {
        return _hasSpace;
    }
    
    public boolean hasDescription() {
        return _hasDescription;
    }
    
    public boolean hasKeyWord() {
        return _hasKeyWord;
    }
    
    public boolean hasPartialKeyWord() {
        return _hasPartialKeyWord;
    }
    
    public boolean hasStartDate() {
        return _hasStartDate;
    }
    
    public boolean hasStartTime() {
        return _hasStartTime;
    }
    
    public boolean hasEndDate() {
        return _hasEndDate;
    }
    
    public boolean hasEndTime() {
        return _hasEndTime;
    }
    
    public boolean hasTag() {
        return _hasTag;
    }
    
    public boolean hasValidTag() {
        return _hasValidTag;
    }
    
    public String getDescription() {
        return _description;
    }
    
    public KeyWordType getKeyWord() {
        return _keyWord;
    }
    
    public KeyWordType getPartialKeyWord() {
        return _partialKeyWord;
    }
    
    public Date getStartDateTime() {
        return _startDateTime;
    }
    
    public Date getEndDateTime() {
        return _endDateTime;
    }
    
    public LocalDate getStartDate() {
        if (_hasStartDate) {
            return _startDate;
        } else {
            return null;
        }
    }
    
    public LocalTime getStartTime() {
        if (_hasStartTime) {
            return _startTime;
        } else {
            return null;
        }
    }
    
    public LocalDate getEndDate() {
        if (_hasEndDate) {
            return _endDate;
        } else {
            return null;
        }
    }
 
    public LocalTime getEnTime() {
        if (_hasEndTime) {
            return _endTime;
        } else {
            return null;
        }
    }
    
    public String[] getTags() {
        return _tags;
    }
}
