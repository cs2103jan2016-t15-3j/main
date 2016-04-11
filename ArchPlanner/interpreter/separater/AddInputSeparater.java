package interpreter.separater;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is used to get the all parameters in an add command.
 * 
 * @@author A0140034B
 */
public class AddInputSeparater {
    public enum AddKeyWordType {
        ON, BY, FROM, UNKNOWN
    }

    private static final int DATE_TO_CALENDAR_MONTH_OFFSET = 1;
    private static final int PARAMETERS_MIN_NUM = 1;
    private static final int MINIMUM_WORD_BEFORE_KEYWORD = 1;
    private static final int INITIAL_INDEX = 0;
    private static final int LAST_INDEX_OFFSET = 1;
    private static final int NOT_FOUND = -1;
    
    private static final int FIRST_HOUR_OF_DAY = 0;
    private static final int FIRST_MINUTE_OF_HOUR = 0;
    private static final int FIRST_SECOND_OF_MINUTE = 0;
    private static final int LAST_HOUR_OF_DAY = 23;
    private static final int LAST_MINUTE_OF_HOUR = 59;
    private static final int LAST_SECOND_OF_MINUTE = 59;
    
    private static final String PARTIAL_KEYWORD_TO = "t";
    private static final String KEYWORD_TO = "to";
    private static final String KEYWORD_TAG = "#";
    private static final String INVALID_TAG_2 = " # ";
    private static final String INVALID_TAG_1 = "# ";
  
    private static final String STRING_EMPTY = "";
    private static final String STRING_SINGLE_SPACE = " ";
    private static final String STRING_MULTIPLE_SPACE = "\\s+";
    
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
    private boolean _hasKeyWordTo;
    private boolean _hasValidTag;
    
    /**
     * Interpret the given string and separate all the parameters found in the string.
     * 
     * @param userInput     the string with the add command parameters.
     */
    public AddInputSeparater(String userInput) {
        
        AddRegionSeparater breakUserInput = new AddRegionSeparater(userInput);
        
        _partialKeyWord = findPartialKeyWord(userInput);   
        _tags = findTags(breakUserInput.getTagRegion());
       
        AddKeyWordType initialKeyWord = findInitialKeyWord(breakUserInput, _partialKeyWord);
        String startDateRegion = findStartDateRegion(breakUserInput, initialKeyWord);
        String endDateRegion = findEndDateRegion(breakUserInput, initialKeyWord);
        DateInterpreter startDateParameter = new DateInterpreter(startDateRegion);
        DateInterpreter endDateParameter = new DateInterpreter(endDateRegion);
        
        _startDateTime = findStartDateTime(startDateParameter, endDateParameter, initialKeyWord);
        _endDateTime = findEndDateTime(startDateParameter, endDateParameter, initialKeyWord);
        _keyWord = findKeyWord(_startDateTime, startDateRegion, _endDateTime, endDateRegion, initialKeyWord);
        _description = findDesciption(userInput, breakUserInput, _keyWord, _startDateTime, _endDateTime);
        
        _startDate = findLocalDate(_startDateTime, startDateParameter);
        _startTime = findLocalTime(_startDateTime, startDateParameter);
        _endDate = findLocalDate(_endDateTime, endDateParameter);
        _endTime = findLocalTime(_endDateTime, endDateParameter);
   
        _startDateTime = modifyStartDateTimeToAssumption(_startDateTime, _startDate, _startTime, 
                                                         _endDateTime, _endDate, _endTime);
        _endDateTime = modifyEndDateTimeToAssumption(_startDateTime, _startDate, _startTime, 
                                                     _endDateTime, _endDate, _endTime);
     
        _hasKeyWordTo = isKeyWordToPresent(userInput, _keyWord, _startDateTime, _endDateTime);
        _hasSpace = isSpacePresent(userInput, breakUserInput);
        _hasValidTag = isValidTag(breakUserInput.getTagRegion());
    }
    
    /**
     * Return the keyword that partially matched the last word in the string.
     * Return UNKNOWN if no keyword matched.
     * 
     * @param input         the string that user inputted.
     * @return              the keyword that partially matched the last word in the string.
     */
    private AddKeyWordType findPartialKeyWord(String input) {
        if (input.trim().isEmpty()) {
            return AddKeyWordType.UNKNOWN;
        }

        String[] splitInput = input.trim().split(STRING_MULTIPLE_SPACE);
        if (splitInput.length <= MINIMUM_WORD_BEFORE_KEYWORD) {
            return AddKeyWordType.UNKNOWN;
        }
        
        for (AddKeyWordType type : AddKeyWordType.values()) {
            if ((type.toString().startsWith(splitInput[splitInput.length - LAST_INDEX_OFFSET].toUpperCase()))) {
                return type;
            }
        }
        return AddKeyWordType.UNKNOWN;
    }
    
    /**
     * Return the tags found in the tagRegion. Return null if no tags found.
     * 
     * @param tagRegion     the region that contain all the tags.
     * @return              the tags found in the string.
     */
    private String[] findTags(String tagRegion) {
        if (tagRegion.trim().isEmpty()) {
            return null;
        } else {
            return tagRegion.split(STRING_MULTIPLE_SPACE);
        }
    }
    
    /**
     * Return the converted keyword from the keyWordRegion in the AddRegionSeparator.
     * Return UNKNOWN if partialKeyWord is present.
     * 
     * @param breakUserInput    the class that break the input into regions.
     * @param partialKeyWord    the partial keyword already found.
     * @return                  the keyword converted from the keyword region in the breakUserInput.
     */
    private AddKeyWordType findInitialKeyWord(AddRegionSeparater breakUserInput, AddKeyWordType partialKeyWord) {
        if (partialKeyWord != AddKeyWordType.UNKNOWN) {
            return AddKeyWordType.UNKNOWN;
        } else {
            return determineKeyWordType(breakUserInput.getKeyWordRegion());
        }
    }
    
    /**
     * Return the start date region interpreted from the AddRegionSeparator using the keyword found.
     * 
     * @param breakUserInput    the class that break the input into regions.
     * @param keyWord           the keyword already found.
     * @return                  the start date region.
     */
    private String findStartDateRegion(AddRegionSeparater breakUserInput, AddKeyWordType keyWord) {
        switch (keyWord) {
            case ON :
                return breakUserInput.getFirstDateRegion();
            case FROM :
                return removeKeyWordToIfPresent(breakUserInput.getFirstDateRegion());
            default :
                return STRING_EMPTY;
        }
    }
    
    /**
     * Return the end date region interpreted from the AddRegionSeparator using the keyword found.
     * 
     * @param breakUserInput    the class that break the input into regions.
     * @param keyWord           the keyword already found.
     * @return                  the end date region.
     */
    private String findEndDateRegion(AddRegionSeparater breakUserInput, AddKeyWordType keyWord) {
        switch (keyWord) {
            case BY :
                return breakUserInput.getFirstDateRegion();
            case FROM :
                return breakUserInput.getSecondDateRegion();
            default :
                return STRING_EMPTY;
        }
    }
    
    /**
     * Return the start Date object by comparing the start date, end date and keyword.
     * 
     * @param startDateParameter    the class containing the start date parameters used for comparing.
     * @param endDateParameter      the class containing the end date parameters used for comparing.
     * @param initialKeyWord        the keyword used for comparing.
     * @return                      the start Date object.
     */
    private Date findStartDateTime(DateInterpreter startDateParameter, DateInterpreter endDateParameter, AddKeyWordType initialKeyWord) {
        Date startDateTime = startDateParameter.getDateTime();
        Date endDateTime = null;
        if (initialKeyWord != AddKeyWordType.FROM || startDateTime != null) {
            endDateTime = endDateParameter.getDateTime();
        } 
        if (initialKeyWord == AddKeyWordType.FROM && endDateTime == null) {
            if (!endDateParameter.getDateRegion().isEmpty() || hasTag()) {
                return null;
            }
        }
        return startDateTime;
    }
    
    /**
     * Return the end Date object by comparing the start date, end date and keyword.
     * 
     * @param startDateParameter    the class containing the start date parameters used for comparing.
     * @param endDateParameter      the class containing the end date parameters used for comparing.
     * @param initialKeyWord        the keyword used for comparing.
     * @return                      the end Date object.
     */
    private Date findEndDateTime(DateInterpreter startDateParameter, DateInterpreter endDateParameter, AddKeyWordType initialKeyWord) {
        Date startDateTime = startDateParameter.getDateTime();
        Date endDateTime = null;
        if (initialKeyWord != AddKeyWordType.FROM || startDateTime != null) {
            endDateTime = endDateParameter.getDateTime();
        }
    
        return endDateTime;
    }
    
    /**
     * Return the valid keyword in the input by checking whether there are other invalid within the dates region.
     * 
     * @param startDateTime     the start date found in the start date region.
     * @param startDateRegion   the start date region.
     * @param endDateTime       the end date found in the start date region.
     * @param endDateRegion     the end date region.
     * @param initialKeyWord    the keyword used for previously found.
     * @return                  the valid keyword.
     */
    private AddKeyWordType findKeyWord(Date startDateTime, String startDateRegion, 
                                      Date endDateTime, String endDateRegion, AddKeyWordType initialKeyWord) {
        if (startDateTime == null  && endDateTime == null) {
            if (!startDateRegion.isEmpty() || hasTag()) {
                return AddKeyWordType.UNKNOWN;
            }
        }
        if (initialKeyWord == AddKeyWordType.FROM && endDateTime == null) {
            if (!endDateRegion.isEmpty() || hasTag()) {
                return AddKeyWordType.UNKNOWN;
            }
        }
        return initialKeyWord;
    }
    
    /**
     * Return the description interpreted after checking for valid dates and keyword.
     * 
     * @param userInput         the user inputed string.
     * @param breakUserInput    the class that break the input into regions.
     * @param keyWord           the keyword used for comparing.
     * @param startDateTime     the start date used for comparing.
     * @param endDateTime       the end date used for comparing.
     * @return                  the description interpreted.
     */
    private String findDesciption(String userInput, AddRegionSeparater breakUserInput, AddKeyWordType keyWord, 
                                 Date startDateTime, Date endDateTime) {
        if ((startDateTime == null && endDateTime == null) || (keyWord == AddKeyWordType.FROM && endDateTime == null)) {
            return userInput.substring(INITIAL_INDEX, userInput.length() - breakUserInput.getTagRegion().length()).trim();
        } else {
            return breakUserInput.getDescriptionRegion();
        }
    }
    
    /**
     * Return LocalDate object found in the given Date after interpreting the date region.
     * Return null date is not present.
     * 
     * @param dateTime          the Date object containing the date.
     * @param breakDateRegion   the class interpreting the date region to check for the presence of date.
     * @return                  the LocalDate object.
     */
    private LocalDate findLocalDate(Date dateTime, DateInterpreter breakDateRegion) {
        if (dateTime == null || breakDateRegion.getDateTime() == null || !breakDateRegion.hasDate()) {
            return null;
        } else {
            return breakDateRegion.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }
    
    /**
     * Return LocalTime object found in the given Date after interpreting the date region.
     * Return null date is not present.
     * 
     * @param dateTime          the Date object containing the time.
     * @param breakDateRegion   the class interpreting the date region to check for the presence of time.
     * @return                  the LocalTime object.
     */
    private LocalTime findLocalTime(Date dateTime, DateInterpreter breakDateRegion) {
        if (dateTime == null || breakDateRegion.getDateTime() == null || !breakDateRegion.hasTime()) {
            return null;
        } else {
            return breakDateRegion.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
    }
    
    /**
     * Modify the start Date object to the assumed value if the start/end date or time is not present.
     * 
     * @param startDateTime     the start Date object.
     * @param startDate         the start LocalDate.
     * @param startTime         the start LocalTime.
     * @param endDateTime       the end Date object.
     * @param endDate           the end LocalDate.
     * @param endTime           the end LocalTime.
     * @return                  the modified start Date object.
     */
    private Date modifyStartDateTimeToAssumption(Date startDateTime, LocalDate startDate, LocalTime startTime, 
                                                 Date endDateTime, LocalDate endDate, LocalTime endTime) {
        if (startDateTime != null && endDateTime != null) {
            if (startTime == null && endTime != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDateTime);
                cal.set(Calendar.HOUR_OF_DAY, FIRST_HOUR_OF_DAY);
                cal.set(Calendar.MINUTE, FIRST_MINUTE_OF_HOUR);
                cal.set(Calendar.SECOND, FIRST_SECOND_OF_MINUTE);
                return cal.getTime();
            }
        }
        return startDateTime;
    }
    
    /**
     * Modify the end Date object to the assumed value if the start/end date or time is not present.
     * 
     * @param startDateTime     the start Date object.
     * @param startDate         the start LocalDate.
     * @param startTime         the start LocalTime.
     * @param endDateTime       the end Date object.
     * @param endDate           the end LocalDate.
     * @param endTime           the end LocalTime.
     * @return                  the modified end Date object.
     */
    private Date modifyEndDateTimeToAssumption(Date startDateTime, LocalDate startDate, LocalTime startTime, 
                                               Date endDateTime, LocalDate endDate, LocalTime endTime) {
        if (startDateTime != null && endDateTime != null) {
            if (startDate != null && startTime != null && endDate == null && endTime != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(endDateTime);
                cal.set(Calendar.DATE, startDate.getDayOfMonth());
                cal.set(Calendar.MONTH, startDate.getMonthValue() - DATE_TO_CALENDAR_MONTH_OFFSET);
                cal.set(Calendar.YEAR, startDate.getYear());
                return cal.getTime();
            } else if (startTime != null && endTime == null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(_endDateTime);
                cal.set(Calendar.HOUR_OF_DAY, LAST_HOUR_OF_DAY);
                cal.set(Calendar.MINUTE, LAST_MINUTE_OF_HOUR);
                cal.set(Calendar.SECOND, LAST_SECOND_OF_MINUTE);
                return cal.getTime();
            }
        }
        return endDateTime;
    }
    
    /**
     * Check if the keyword 'to' is present in the user input.
     * 
     * @param userInput     the user inputed string.
     * @param keyWord       the keyword use to determine if the keyword 'to' is present.
     * @param startDateTime the start date use to determine if the keyword 'to' is present.
     * @param endDateTime   the end date use to determine if the keyword 'to' is present.
     * @return              the modified start Date object.
     */
    private boolean isKeyWordToPresent(String userInput, AddKeyWordType keyWord, Date startDateTime, Date endDateTime) {
        if (keyWord == AddKeyWordType.FROM && startDateTime != null && endDateTime == null && getPartialToIndex(userInput) != NOT_FOUND) {
            return true;
        } else if (keyWord == AddKeyWordType.FROM && startDateTime != null && endDateTime != null) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isSpacePresent(String userInput, AddRegionSeparater breakUserInput) {
        if (!breakUserInput.getTagRegion().isEmpty()) {
            return true;
        }
        if (userInput.trim().length() < PARAMETERS_MIN_NUM) {
            return false;
        }
        return userInput.substring(userInput.length() - LAST_INDEX_OFFSET).equals(STRING_SINGLE_SPACE);
    }
    
    /**
     * Check if the tag in the tag region contains all valid tags.
     * 
     * @param tagRegion     the tag region containing the tags
     * @return              whether if all tags are valid.
     */
    private boolean isValidTag(String tagRegion) {
        if (tagRegion.startsWith(INVALID_TAG_1) || tagRegion.contains(INVALID_TAG_2)) {
            return false;
        }
        String[] tags = tagRegion.split(STRING_MULTIPLE_SPACE);
        for (int i = INITIAL_INDEX; i < tags.length; i++) {
            if(!tags[i].startsWith(KEYWORD_TAG)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Remove the keyword 'to' in the start date region if it is found.
     * 
     * @param startDateRegion   the region containing the start date time.
     * @return                  the start date region without the keyword.
     */
    private String removeKeyWordToIfPresent(String startDateRegion) {
        int indexOfPartialTo =  getPartialToIndex(startDateRegion);
        if (indexOfPartialTo == NOT_FOUND) {
            return startDateRegion;
        } else {
            return startDateRegion.substring(INITIAL_INDEX, indexOfPartialTo).trim();
        }
    }
    
    /**
     * Return the index of the keyword 'to' in the input string. Return -1 if keyword is not found.
     * 
     * @param input the string containing the keyword.
     * @return      the index of the keyword 'to'.
     */
    private int getPartialToIndex(String input) {
        if (input.trim().isEmpty()) {
            return NOT_FOUND;
        }
        input = input.toLowerCase();
        String[] splitInput = input.trim().split(STRING_MULTIPLE_SPACE);
        if (splitInput.length <= MINIMUM_WORD_BEFORE_KEYWORD) {
            return NOT_FOUND;
        }
        if (KEYWORD_TO.startsWith(splitInput[splitInput.length - LAST_INDEX_OFFSET])) {
            return input.lastIndexOf(PARTIAL_KEYWORD_TO);
        } else {
            return NOT_FOUND;
        }
    }    
    
    /**
     * Return the KeyWordType from the key word string.
     * 
     * @param KeyWordString     the string of the keyword.
     * @return                  the KeyWordType interpreted from the string.
     */
    private AddKeyWordType determineKeyWordType(String KeyWordString) {
        if (KeyWordString.isEmpty()) {
            return AddKeyWordType.UNKNOWN;
        }
        
        for (AddKeyWordType type : AddKeyWordType.values()) {
            if (KeyWordString.equalsIgnoreCase(type.toString())) {
                return type;
            }
        }
        return AddKeyWordType.UNKNOWN;
    }
    
    /**
     * Return true if a space is found in the user input.
     * 
     * @return  true if a space is found.
     */
    public boolean hasSpace() {
        return _hasSpace;
    }
    
    /**
     * Return true if description is found in the user input.
     * 
     * @return  true if description is found.
     */
    public boolean hasDescription() {
        return _description != null;
    }
    
    /**
     * Return true if keyword is found in the user input.
     * 
     * @return  true if keyword is found.
     */
    public boolean hasKeyWord() {
        return _keyWord != AddKeyWordType.UNKNOWN;
    }
    
    /**
     * Return true if a partial keyword is found in the user input.
     * 
     * @return  true if partial keyword is found.
     */
    public boolean hasPartialKeyWord() {
        return _partialKeyWord != AddKeyWordType.UNKNOWN;
    }
    
    /**
     * Return true if the keyword 'to' is found in the user input.
     * 
     * @return  true if the keyword 'to' is found.
     */
    public boolean hasKeyWordTo() {
        return _hasKeyWordTo;
    }
    
    /**
     * Return true if the start date is found in the user input.
     * 
     * @return  true if the start date is found.
     */
    public boolean hasStartDate() {
        return _startDate != null;
    }
    
    /**
     * Return true if the start time is found in the user input.
     * 
     * @return  true if the start time is found.
     */
    public boolean hasStartTime() {
        return _startTime != null;
    }
    
    /**
     * Return true if the end date is found in the user input.
     * 
     * @return  true if the end date is found.
     */
    public boolean hasEndDate() {
        return _endDate != null;
    }
    
    /**
     * Return true if the end time is found in the user input.
     * 
     * @return  true if the end time is found.
     */
    public boolean hasEndTime() {
        return _endTime != null;
    }
    
    /**
     * Return true if there are tags found in the user input.
     * 
     * @return  true if tags are found.
     */
    public boolean hasTag() {
        return  _tags != null;
    }
    
    /**
     * Return true if the tags found in the user input is valid.
     * 
     * @return  true if the tags found in the user input is valid.
     */
    public boolean hasValidTag() {
        return _hasValidTag;
    }
    
    /**
     * Return true if both start and end date is present and the start date is before the end date.
     * 
     * @return  true if both start and end date is present and the start date is before the end date.
     */
    public boolean hasValidDateRange() {
        if (_startDateTime == null || _endDateTime == null) {
            return false;
        } else {
           return _startDateTime.before(_endDateTime);
        }
    }
    
    /**
     * Return the description found in the user input.
     * 
     * @return  the description found in the user input.
     */
    public String getDescription() {
        return _description;
    }
    
    /**
     * Return the keyword found in the user input.
     * 
     * @return  the keyword found in the user input.
     */
    public AddKeyWordType getKeyWord() {
        return _keyWord;
    }
    
    /**
     * Return the partial keyword found in the user input.
     * 
     * @return  the partial keyword found in the user input.
     */
    public AddKeyWordType getPartialKeyWord() {
        return _partialKeyWord;
    }
    
    /**
     * Return the start Date Object found in the user input.
     * 
     * @return  the start Date Object found in the user input.
     */
    public Date getStartDateTime() {
        return _startDateTime;
    }
    
    /**
     * Return the end Date Object found in the user input.
     * 
     * @return  the end Date Object found in the user input.
     */
    public Date getEndDateTime() {
        return _endDateTime;
    }
    
    /**
     * Return the start LocalDate Object found in the user input.
     * 
     * @return  the start LocalDate Object found in the user input.
     */
    public LocalDate getStartDate() {
        return _startDate;
    }
    
    /**
     * Return the start LocalTime Object found in the user input.
     * 
     * @return  the start LocalTime Object found in the user input.
     */
    public LocalTime getStartTime() {
        return _startTime;
    }
    
    /**
     * Return the end LocalDate Object found in the user input.
     * 
     * @return  the end LocalDate Object found in the user input.
     */
    public LocalDate getEndDate() {
        return _endDate;
    }
    
    /**
     * Return the end LocalTime Object found in the user input.
     * 
     * @return  the end LocalTime Object found in the user input.
     */
    public LocalTime getEndTime() {
        return _endTime;
    }
    
    /**
     * Return the tags found in the user input.
     * 
     * @return  the tags found in the user input.
     */
    public String[] getTags() {
        return _tags;
    }
}
