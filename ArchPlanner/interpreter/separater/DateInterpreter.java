package interpreter.separater;

import java.util.Date;
import java.util.logging.Logger;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * This class is used to check if a one date and one time is present in the give string.
 * If there are other words that do not represent the date or time present in the string, no date time will be assumed.
 * If there is more than one date found, no date time will be assumed.
 * 
 * @@author A0140034B
 */
public class DateInterpreter {
    static Logger log = Logger.getLogger(DateInterpreter.class.getName());
    
    private static final String LOG_MESSAGE_MORE_THAN_ONE_DATE_FOUND = "More than one date found: ";
    private static final String LOG_MESSAGE_EXTRA_WORDS_ARE_FOUND = "Extra words found: ";
    private static final String LOG_MESSAGE_DATE_FOUND = "Valid Date found: ";
    private static final String LOG_MESSAGE_NO_DATE_FOUND = "No Date found: ";
    
    private static final int INDEX_OF_FIRST_DATE = 0;
    
    private String _dateRegion;
    private Date _dateTime;
    private boolean _hasDate;
    private boolean _hasTime;
    
    /**
     * Interpret the given string to a Date Object and checking if date and time is present in the Date.
     * No Date is present if the string contains other words that do not represent date or time.
     * 
     * @param dateRegion    the string that will be interpreted to Date Object
     */
    public DateInterpreter(String dateRegion) {
        _dateRegion = dateRegion.trim();
        DateGroup dateGroup = getDateGroup(dateRegion.toLowerCase());
        _dateTime = getDate(dateGroup);
        _hasDate = isDatePresent(dateGroup);
        _hasTime = isTimePresent(dateGroup);
    }
    
    /**
     * Interpret the given string to a Date Group. 
     * null will be return:
     * 1. no Date is found
     * 2. more than one Date is found
     * 3. additional words not representing the date is in the string
     * 
     * @param dateRegion    the string to be interpreted.
     * @return              the DateGroup containing the Date.
     */
    private DateGroup getDateGroup(String dateRegion) {
        Parser parser = new Parser();
        try {
            DateGroup dateGroup = parser.parse(dateRegion).get(INDEX_OF_FIRST_DATE);
            if (dateGroup.getText().equals(dateRegion)) {
                if (dateGroup.getDates().size() == 1) {
                    log.info(LOG_MESSAGE_DATE_FOUND + dateRegion);
                    return dateGroup;
                } else {
                    log.info(LOG_MESSAGE_MORE_THAN_ONE_DATE_FOUND + dateRegion);
                    return null;
                }
            } else {
                log.info(LOG_MESSAGE_EXTRA_WORDS_ARE_FOUND + dateRegion);
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            log.info(LOG_MESSAGE_NO_DATE_FOUND + dateRegion);
            return null;
        }
    }
    
    /**
     * Return the Date object in the DateGroup.
     * Return null if DateGroup is null.
     * 
     * @param dateGroup     the DateGroup containing the Date.
     * @return              Date object found in DateGroup
     */
    private Date getDate(DateGroup dateGroup) {
        if (dateGroup != null) {
            return dateGroup.getDates().get(INDEX_OF_FIRST_DATE);
        } else {
            return null;
        }
    }
    
    /**
     * Check if the date is interpreted from the string in the DateGroup
     * Return false if DateGroup is null.
     * 
     * @param dateGroup     the DateGroup containing the Date.
     * @return              whether interpreted date from the string in the DateGroup
     */
    private boolean isDatePresent(DateGroup dateGroup) {
        if (dateGroup != null && !dateGroup.isDateInferred())  {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Check if the time is interpreted from the string in the DateGroup
     * Return null if DateGroup is null.
     * 
     * @param dateGroup     the DateGroup containing the Date.
     * @return              whether interpreted time from the string in the DateGroup
     */
    private boolean isTimePresent(DateGroup dateGroup) {
        if (dateGroup != null && !dateGroup.isTimeInferred())  {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Return the interpreted Date.
     *
     * @return  the interpreted Date.
     */
    public boolean hasDate() {
        return _hasDate;
    }
    
    /**
     * Return whether date is found in string.
     *
     * @return  whether date is found in string.
     */
    public boolean hasTime() {
        return _hasTime;
    }
    
    /**
     * Return whether time is found in string.
     *
     * @return  whether time is found in string.
     */
    public Date getDateTime() {
        return _dateTime;
    }
    
    /**
     * Return original string.
     *
     * @return  original string.
     */
    public String getDateRegion() {
        return _dateRegion;
    }
}
