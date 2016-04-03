package separator;

import java.util.Date;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class AddBreakDateRegion {
    private Date dateTime;   
    private boolean hasDate;
    private boolean hasTime;
    
    public AddBreakDateRegion(String dateRegion) {
        hasDate = false;
        hasTime = false;
        dateRegion = dateRegion.toLowerCase();

        Parser parser = new Parser();
        try {
            System.out.println(dateRegion);
            DateGroup dateGroup = parser.parse(dateRegion).get(0);
            if (dateGroup.getDates().size() > 1) {
                System.out.println("Got more than 1 Dates");
                throw new Exception();
            } else {
                System.out.println("Got 1 Dates");
                System.out.println("DateTime|" + dateGroup.getText() + "|");
                System.out.println("dateRegion|" + dateRegion + "|");
                if (dateGroup.getText().equals(dateRegion)) {  
                    System.out.println("No rubbish in start date");
                    dateTime = dateGroup.getDates().get(0);
                    if (!dateGroup.isDateInferred())  {
                        hasDate = true;
                    }
                    if (!dateGroup.isTimeInferred()) {
                        hasTime = true;
                    }
                } else {
                    System.out.println("Got rubbish in start date");
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception");
            hasDate = false;
            hasTime = false;
        }
        
        System.out.println();
        System.out.println("BreakDateRegion--------------------------------------");
        System.out.println("DateRegion: " + dateRegion + "|");
        System.out.println("hasStartDate: " + hasDate + "|");
        System.out.println("hasStartTime: " + hasTime + "|");
        
        if (dateTime != null)
            System.out.println("StartDateTiime: " + dateTime);
        System.out.println("--------------------------------------BreakDateRegion");
    }
    
    public boolean hasDate() {
        return hasDate;
    }
    
    public boolean hasTime() {
        return hasTime;
    }
    
    public Date getDateTime() {
        return dateTime;
    }
}
