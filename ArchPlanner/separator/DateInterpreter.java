//@@author A0140034B
package separator;

import java.util.Date;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class DateInterpreter {
    private static final int INDEX_OF_FIRST_DATE = 0;
    
    private Date _dateTime;
    private boolean _hasDate;
    private boolean _hasTime;
    
    public DateInterpreter(String dateRegion) {
        _hasDate = false;
        _hasTime = false;
        dateRegion = dateRegion.toLowerCase();

        Parser parser = new Parser();
        try {
            System.out.println(dateRegion);
            DateGroup dateGroup = parser.parse(dateRegion).get(INDEX_OF_FIRST_DATE);
            if (dateGroup.getDates().size() > 1) {
                System.out.println("Got more than 1 Dates");
                throw new Exception();
            } else {
                System.out.println("Got 1 Dates");
                System.out.println("DateTime|" + dateGroup.getText() + "|");
                System.out.println("dateRegion|" + dateRegion + "|");
                if (dateGroup.getText().equals(dateRegion)) {  
                    System.out.println("No rubbish in start date");
                    _dateTime = dateGroup.getDates().get(INDEX_OF_FIRST_DATE);
                    if (!dateGroup.isDateInferred())  {
                        _hasDate = true;
                    }
                    if (!dateGroup.isTimeInferred()) {
                        _hasTime = true;
                    }
                } else {
                    System.out.println("Got rubbish in start date");
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception");
            _hasDate = false;
            _hasTime = false;
        }
        
        System.out.println();
        System.out.println("BreakDateRegion--------------------------------------");
        System.out.println("DateRegion: " + dateRegion + "|");
        System.out.println("hasStartDate: " + _hasDate + "|");
        System.out.println("hasStartTime: " + _hasTime + "|");
        
        if (_dateTime != null)
            System.out.println("StartDateTiime: " + _dateTime);
        System.out.println("--------------------------------------BreakDateRegion");
    }
    
    public boolean hasDate() {
        return _hasDate;
    }
    
    public boolean hasTime() {
        return _hasTime;
    }
    
    public Date getDateTime() {
        return _dateTime;
    }
}
