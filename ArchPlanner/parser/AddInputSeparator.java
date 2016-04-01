package parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class AddInputSeparator {
    
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
    private boolean _hasPartialKeyWordTo;
    private boolean _hasStartDate;
    private boolean _hasStartTime;
    private boolean _hasEndDate;
    private boolean _hasEndTime;
    private boolean _hasTag;
    private boolean _hasValidTag;
    private boolean _hasValidDateRange;
    
    public AddInputSeparator(String userInput) {
        
        AddBreakRegion breakUserInput = new AddBreakRegion(userInput);
        _keyWord = determineKeyWordType(breakUserInput.getKeyWord());
        
        String startDateRegion = "";
        String endDateRegion = "";
        switch (_keyWord) {
            case ON :
                startDateRegion = breakUserInput.getFirstDateRegion();
                break;
            case BY :
                endDateRegion = breakUserInput.getFirstDateRegion();
                break;
            case FROM :
                startDateRegion = breakUserInput.getFirstDateRegion();
                endDateRegion = breakUserInput.getSecondDateRegion();
                if (isPartialToPresent(startDateRegion)) {
                    startDateRegion = startDateRegion.substring(0, getPartialToIndex(startDateRegion)).trim();
                    _hasPartialKeyWordTo = true;
                } else {
                    _hasPartialKeyWordTo = false;
                }
                break;
            default:
                break;
        }
       
        AddBreakDateRegion breakStartDateRegion = new AddBreakDateRegion(startDateRegion);
        AddBreakDateRegion breakEndDateRegion = new AddBreakDateRegion(endDateRegion);
        
        _hasSpace = isSpacePresent(userInput) || !breakUserInput.getTagRegion().isEmpty();
        _hasDescription = !breakUserInput.getDescription().isEmpty();
        
        _hasPartialKeyWord = isPartialKeyWordPresent(userInput);
        _hasTag = !breakUserInput.getTagRegion().isEmpty();
        _hasValidTag = isValidTag(breakUserInput.getTagRegion());
        _hasStartDate = breakStartDateRegion.hasDate();
        _hasStartTime = breakStartDateRegion.hasTime();
        _hasEndDate = breakEndDateRegion.hasDate();
        _hasEndTime = breakEndDateRegion.hasTime();
        
        _description = breakUserInput.getDescription();
        _partialKeyWord = searchPartialKeyWord(userInput);
        if (_partialKeyWord != KeyWordType.UNKNOWN) {
            _keyWord = KeyWordType.UNKNOWN;
        }
        _startDateTime = breakStartDateRegion.getDateTime();
        _endDateTime = breakEndDateRegion.getDateTime();
        if (_startDateTime != null) {
            _startDate = _startDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            _startTime = _startDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
        if (_endDateTime != null) {
            _endDate = _endDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            _endTime = _endDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
        if (_startDateTime == null && _endDateTime == null) {
            _description = userInput.substring(0, userInput.length() - breakUserInput.getTagRegion().length()).trim();
            _hasPartialKeyWordTo = false;
            if (!breakUserInput.getFirstDateRegion().isEmpty() || _hasTag) {
                _keyWord = KeyWordType.UNKNOWN;
            }
        }
        if (_keyWord == KeyWordType.FROM && _endDateTime == null) {
            _description = userInput.substring(0, userInput.length() - breakUserInput.getTagRegion().length()).trim();
            if (!breakUserInput.getSecondDateRegion().isEmpty() || _hasTag) {
                _keyWord = KeyWordType.UNKNOWN;
                _hasPartialKeyWordTo = false;
                _startDate = null;
                _startTime = null;
                _startDateTime = null;
                _hasStartDate = false;
                _hasStartTime = false;
            }
        }
    
        if (_startDateTime != null && _endDateTime != null) {
            _hasValidDateRange = _startDateTime.before(_endDateTime);
        }
        
        _tags = getTags(breakUserInput.getTagRegion());
        _hasKeyWord = _keyWord != KeyWordType.UNKNOWN;
        
        System.out.println("InputParameters------------------------------------------");
        System.out.println("userInput: |" + userInput + "|");
        System.out.println("Description: |" + _description + "|" + _hasDescription);
        System.out.println("keyWord: |" + _keyWord + "|" + _hasKeyWord);
        System.out.println("partialKeyWord: |" + _partialKeyWord + "|" + _hasPartialKeyWord); 
        System.out.println("startDateTime: |" + _startDateTime + "|");
        System.out.println("endDateTime: |" + _endDateTime + "|");
        System.out.println("startDate: |" + _startDate + "|" + _hasStartDate);
        System.out.println("startTime: |" + _startTime + "|" + _hasStartTime);
        System.out.println("endDate: |" + _endDate + "|" + _hasEndDate);
        System.out.println("endTime: |" + _endTime + "|" + _hasEndTime);
        System.out.println("partialTo: |" + _hasPartialKeyWordTo + "|");
        System.out.println("hasSpace |" + _hasSpace + "|");
        System.out.println("hasTag |" + _hasTag + "|");
        System.out.println("hasValidTag |" + _hasValidTag + "|");
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
        return tagRegion.split(STRING_MULTIPLE_WHITESPACE);
    }
    
    private boolean isPartialToPresent(String input) {
        return getPartialToIndex(input) != -1;
    }
    
    private int getPartialToIndex(String input) {
        if (input.trim().isEmpty()) {
            return -1;
        }
        input = input.toLowerCase();
        String[] splitInput = input.trim().split(STRING_MULTIPLE_WHITESPACE);
        if (splitInput.length < 2) {
            return -1;
        }
        if ("to".startsWith(splitInput[splitInput.length - 1])) {
            return input.lastIndexOf("t");
        } else {
            return -1;
        }
    }
    
    private boolean isPartialKeyWordPresent(String input) {
        return searchPartialKeyWord(input) != KeyWordType.UNKNOWN;
    }
    
    
    private KeyWordType searchPartialKeyWord(String input) {
        if (input.trim().isEmpty()) {
            return KeyWordType.UNKNOWN;
        }
        input = input.toLowerCase();
        String[] splitInput = input.trim().split(STRING_MULTIPLE_WHITESPACE);
        if (splitInput.length < 2) {
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

    private KeyWordType determineKeyWordType(String KeyWordString) {
        if (KeyWordString.isEmpty()) {
            return KeyWordType.UNKNOWN;
        }

        if (KEY_WORDS[0].equalsIgnoreCase(KeyWordString)) {
            return KeyWordType.ON;
        } else if (KEY_WORDS[1].equalsIgnoreCase(KeyWordString)) {
            return KeyWordType.BY;
        } else if (KEY_WORDS[2].equalsIgnoreCase(KeyWordString)) {
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
    
    public boolean hasPartialKeyWordTo() {
        return _hasPartialKeyWordTo;
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
    
    public boolean hasValidDateRange() {
        return _hasValidDateRange;
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
 
    public LocalTime getEndTime() {
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
