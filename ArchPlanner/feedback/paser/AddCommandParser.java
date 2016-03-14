package feedback.paser;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import logic.commands.AddCommand;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class AddCommandParser extends CommandParser {

    private String description;
    private String startTime;
    private String endTime;
    //add sth from 07/05/16 09:07 AM to 08/06/16 7:7 PM #my tag
    //add finish this project by 04/15/16 0:0 AM #ArchPlanner
    //add enjoy summer holiday from 05/05/16 0:0 PM #Holiday!!!
    private Calendar startDate;
    private Calendar endDate;

    @Override
    public AddCommand parse(String input) {
        this.input = detectTagAndRemoveTagString(input);
        Parser timeParser = new Parser();
        List<DateGroup> groups = timeParser.parse(input);
        DateGroup group = groups.get(0);
        List<Date> dates = group.getDates();
        String matchingValue = group.getText();
        int matchPosition = group.getPosition();
        for (Date date : dates) {
            if (startDate == null) {
                startDate = Calendar.getInstance();
                startDate.setTime(date);
            } else {
                endDate = Calendar.getInstance();
                endDate.setTime(date);
            }
        }

        return new AddCommand(description, tagList.get(0), startDate, endDate);
    }

    private void removeTimeString(String timeString) {

    }
}
