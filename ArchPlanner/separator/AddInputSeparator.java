package separator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class AddInputSeparator {

    private static final int PARAMETERS_MIN_NUM = 1;
    private static final int MINIMUM_WORD_BEFORE_KEYWORD = 1;
    private static final int INITIAL_INDEX = 0;
    private static final int LAST_INDEX_OFFSET = 1;
    private static final int NOT_FOUND = -1;
    
    private static final String PARTIAL_KEYWORD_TO = "t";
    private static final String KEYWORD_TO = "to";
    private static final String KEYWORD_TAG = "#";
    private static final String INVALID_TAG_2 = " # ";
    private static final String INVALID_TAG_1 = "# ";
  
    private static final String STRING_EMPTY = "";
    private static final String STRING_SINGLE_WHITESPACE = " ";
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    
    public enum AddKeyWordType {
        ON, BY, FROM, UNKNOWN
    };
    
    private String _description;
    private AddKeyWordType _keyWord;
    private AddKeyWordType _partialKeyWord;
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
        
        AddRegionSeparator breakUserInput = new AddRegionSeparator(userInput);
        _keyWord = determineKeyWordType(breakUserInput.getKeyWord());
        
        String startDateRegion = STRING_EMPTY;
        String endDateRegion = STRING_EMPTY;
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
       
        DateInterpreter breakStartDateRegion = new DateInterpreter(startDateRegion);
        DateInterpreter breakEndDateRegion = new DateInterpreter(endDateRegion);
        
        _hasSpace = isSpacePresent(userInput) || !breakUserInput.getTagRegion().isEmpty();
        _hasDescription = !breakUserInput.getDescription().isEmpty();
        
        _hasPartialKeyWord = isPartialKeyWordPresent(userInput);
        _hasTag = !breakUserInput.getTagRegion().isEmpty();
        _hasValidTag = isValidTag(breakUserInput.getTagRegion());
        
        _description = breakUserInput.getDescription();
        _partialKeyWord = searchPartialKeyWord(userInput);
        if (_partialKeyWord != AddKeyWordType.UNKNOWN) {
            _keyWord = AddKeyWordType.UNKNOWN;
        }

        _startDateTime = breakStartDateRegion.getDateTime();
        _hasStartDate = breakStartDateRegion.hasDate();
        _hasStartTime = breakStartDateRegion.hasTime();

        if (_keyWord != AddKeyWordType.FROM || _startDateTime != null) {
            _endDateTime = breakEndDateRegion.getDateTime();
            _hasEndDate = breakEndDateRegion.hasDate();
            _hasEndTime = breakEndDateRegion.hasTime();
        }

        if (_startDateTime != null) {
            _startDate = _startDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            _startTime = _startDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
        if (_endDateTime != null) {
            _endDate = _endDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            _endTime = _endDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
        if (_startDateTime == null && _endDateTime == null) {
            _description = userInput.substring(INITIAL_INDEX, userInput.length() - breakUserInput.getTagRegion().length()).trim();
            _hasPartialKeyWordTo = false;
            if (!breakUserInput.getFirstDateRegion().isEmpty() || _hasTag) {
                _keyWord = AddKeyWordType.UNKNOWN;
            }
        }
        if (_keyWord == AddKeyWordType.FROM && _endDateTime == null) {
            _description = userInput.substring(INITIAL_INDEX, userInput.length() - breakUserInput.getTagRegion().length()).trim();
            if (!breakUserInput.getSecondDateRegion().isEmpty() || _hasTag) {
                _keyWord = AddKeyWordType.UNKNOWN;
                _hasPartialKeyWordTo = false;
                _startDate = null;
                _startTime = null;
                _startDateTime = null;
                _hasStartDate = false;
                _hasStartTime = false;
            }
        }
    
        if (_startDateTime != null && _endDateTime != null) {
            if (_hasStartDate && _hasStartTime && !_hasEndDate && _hasEndTime) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(_endDateTime);
                cal.set(Calendar.DATE, _startDate.getDayOfMonth());
                cal.set(Calendar.MONTH, _startDate.getMonthValue() - 1);
                cal.set(Calendar.YEAR, _startDate.getYear());
                _endDateTime = cal.getTime();
            } else if (!_hasStartTime && _hasEndTime) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(_startDateTime);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                _startDateTime = cal.getTime();
            } else if (_hasStartTime && !_hasEndTime) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(_startDateTime);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 99);
                _endDateTime = cal.getTime();
            }
            _hasValidDateRange = _startDateTime.before(_endDateTime);
        }
        
        _tags = getTags(breakUserInput.getTagRegion());
        _hasKeyWord = _keyWord != AddKeyWordType.UNKNOWN;
        
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
        if (userInput.length() < PARAMETERS_MIN_NUM) {
            return false;
        }
        return userInput.substring(userInput.length() - LAST_INDEX_OFFSET).equals(STRING_SINGLE_WHITESPACE);
    }
    
    private boolean isValidTag(String tagRegion) {
        if (tagRegion.startsWith(INVALID_TAG_1) || tagRegion.contains(INVALID_TAG_2)) {
            return false;
        }
        String[] tags = tagRegion.split(STRING_MULTIPLE_WHITESPACE);
        for (int i = INITIAL_INDEX; i < tags.length; i++) {
            if(!tags[i].startsWith(KEYWORD_TAG)) {
                return false;
            }
        }
        return true;
    }
    
    private String[] getTags(String tagRegion) {
        return tagRegion.split(STRING_MULTIPLE_WHITESPACE);
    }
    
    private boolean isPartialToPresent(String input) {
        return getPartialToIndex(input) != NOT_FOUND;
    }
    
    private int getPartialToIndex(String input) {
        if (input.trim().isEmpty()) {
            return NOT_FOUND;
        }
        input = input.toLowerCase();
        String[] splitInput = input.trim().split(STRING_MULTIPLE_WHITESPACE);
        if (splitInput.length <= MINIMUM_WORD_BEFORE_KEYWORD) {
            return NOT_FOUND;
        }
        if (KEYWORD_TO.startsWith(splitInput[splitInput.length - LAST_INDEX_OFFSET])) {
            return input.lastIndexOf(PARTIAL_KEYWORD_TO);
        } else {
            return NOT_FOUND;
        }
    }
    
    private boolean isPartialKeyWordPresent(String input) {
        return searchPartialKeyWord(input) != AddKeyWordType.UNKNOWN;
    }
    
    
    private AddKeyWordType searchPartialKeyWord(String input) {
        if (input.trim().isEmpty()) {
            return AddKeyWordType.UNKNOWN;
        }

        String[] splitInput = input.trim().split(STRING_MULTIPLE_WHITESPACE);
        if (splitInput.length <= MINIMUM_WORD_BEFORE_KEYWORD) {
            return AddKeyWordType.UNKNOWN;
        }
        
        for (AddKeyWordType type : AddKeyWordType.values()) {
            if ((type.name().startsWith(splitInput[splitInput.length - LAST_INDEX_OFFSET].toUpperCase()))) {
                return type;
            }
        }
        return AddKeyWordType.UNKNOWN;
    }

    private AddKeyWordType determineKeyWordType(String KeyWordString) {
        if (KeyWordString.isEmpty()) {
            return AddKeyWordType.UNKNOWN;
        }
        
        for (AddKeyWordType type : AddKeyWordType.values()) {
            if (KeyWordString.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return AddKeyWordType.UNKNOWN;
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
    
    public AddKeyWordType getKeyWord() {
        return _keyWord;
    }
    
    public AddKeyWordType getPartialKeyWord() {
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
