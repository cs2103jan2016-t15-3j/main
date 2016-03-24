package feedback;

import java.util.Date;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import feedback.InputParameters.KeyWordType;

public class BreakDateRegion {
    private Date startDateTime;
    private Date endDateTime;
    
    private boolean hasStartDate;
    private boolean hasStartTime;
    private boolean hasEndDate;
    private boolean hasEndTime;
    
    public BreakDateRegion(String dateRegion, KeyWordType keyWord) {
        hasStartDate = false;
        hasStartTime = false;
        hasEndDate = false;
        hasEndTime = false;

        Parser parser = new Parser();
        try {
            System.out.println(dateRegion);
            DateGroup dateTime = parser.parse(dateRegion).get(0);
            if (dateTime.getDates().size() > 1) {
                System.out.println("Got 2 Dates");
                int position = dateRegion.lastIndexOf(" to ");
                if (position > 0) {
                    System.out.println("Got TO");
                    String startDate = dateRegion.substring(0, position).trim();
                    dateTime = parser.parse(startDate).get(0);
                    System.out.println("SDateTime|" + dateTime.getText() + "|");
                    System.out.println("startDate|" + startDate + "|");
                    if (dateTime.getText().equals(startDate)) {
                        System.out.println("No rubbish in start date with 2 dates");
                        startDateTime = dateTime.getDates().get(0);
                        if (!dateTime.isDateInferred())  {
                            hasStartDate = true;
                        }
                        if (!dateTime.isTimeInferred()) {
                            hasStartTime = true;
                        }
                    }
                    String endDate = dateRegion.substring(position + 4).trim();
                    dateTime = parser.parse(endDate).get(0);
                    System.out.println("EDateTime|" + dateTime.getText()+ "|");
                    System.out.println("endDate|" + endDate + "|");
                    if (dateTime.getText().equals(endDate)) {
                        System.out.println("No rubbish in end date");
                        endDateTime = dateTime.getDates().get(0);
                        if (!dateTime.isDateInferred())  {
                            hasEndDate = true;
                        }
                        if (!dateTime.isTimeInferred()) {
                            hasEndTime = true;
                        }
                    }
                } else {
                    System.out.println("No TO");
                    throw new Exception();
                }
            } else {
                System.out.println("Got 1 Dates");
                System.out.println("DateTime|" + dateTime.getText() + "|");
                System.out.println("dateRegion|" + dateRegion + "|");
                if (dateTime.getText().equals(dateRegion)) {  
                    System.out.println("No rubbish in start date");
                    startDateTime = dateTime.getDates().get(0);
                    if (!dateTime.isDateInferred())  {
                        hasStartDate = true;
                    }
                    if (!dateTime.isTimeInferred()) {
                        hasStartTime = true;
                    }
                } else {
                    String textAfterDate = dateRegion.substring(dateTime.getText().length()).trim();
                    System.out.println("textAfterDate: " + textAfterDate);
                    if (keyWord == KeyWordType.FROM && "to".startsWith(textAfterDate)) {
                        startDateTime = dateTime.getDates().get(0);
                        if (!dateTime.isDateInferred())  {
                            hasStartDate = true;
                        }
                        if (!dateTime.isTimeInferred()) {
                            hasStartTime = true;
                        }
                    } else {
                        System.out.println("Got rubbish in start date");
                        throw new Exception();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception");
            hasStartDate = false;
            hasStartTime = false;
            hasEndDate = false;
            hasEndTime = false;
        }
        
        System.out.println();
        System.out.println("BreakDateRegion--------------------------------------");
        System.out.println("DateRegion: " + dateRegion + "|");
        System.out.println("hasStartDate: " + hasStartDate + "|");
        System.out.println("hasStartTime: " + hasStartTime + "|");
        System.out.println("hasEndDate: " + hasEndDate + "|");
        System.out.println("hasEndTime: " + hasEndTime + "|");
        
        if (startDateTime != null)
            System.out.println("StartDateTiime: " + startDateTime);
        if (endDateTime != null)
            System.out.println("EndDateTiime: " + endDateTime);
        System.out.println("--------------------------------------BreakDateRegion");
    }
    
    public boolean hasStartDate() {
        return hasStartDate;
    }
    
    public boolean hasStartTime() {
        return hasStartTime;
    }
    
    public boolean hasEndDate() {
        return hasEndDate;
    }
    
    public boolean hasEndTime() {
        return hasEndTime;
    }
    
    public Date getStartDateTime() {
        return startDateTime;
    }
    
    public Date getEndDateTime() {
        return endDateTime;
    }
}
