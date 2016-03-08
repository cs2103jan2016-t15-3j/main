package feedback.paser;

import logic.commands.AddCommand;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by lifengshuang on 3/5/16.
 */
public class AddCommandParser extends CommandParser {

    private String description;
    private String tag;
    private String startTime;
    private String endTime;
    //add sth from 07/05/16 09:07 AM to 08/06/16 7:7 PM #my tag
    //add finish this project by 04/15/16 0:0 AM #ArchPlanner
    //add enjoy summer holiday from 05/05/16 0:0 PM #Holiday!!!
    private Calendar startDate;
    private Calendar endDate;

    public AddCommand parse(String input) {
        this.input = input;
        tag = ParserUtils.retrieveTag(this);
        String[] words = ParserUtils.split(this.input);
        int i;
        for (i = 1; i < words.length; i++) {
            //only start time
            //todo: bug for index out of bound
            if (words[i].equals("start") && words[i + 1].equals("at")) {
                startTime = ParserUtils.merge(words, i + 1, words.length);
                break;
            } else if (words[i].equals("by") || words[i].equals("until") || words[i].equals("deadline")) {
                endTime = ParserUtils.merge(words, i + 1, words.length);
                break;
            } else if (words[i].equals("from")) {
                int index = 0;
                for (int j = i; j < words.length; j++) {
                    if (words[j].equals("to")) {
                        index = j;
                        break;
                    }
                }
                if (index == 0) {
                    startTime = ParserUtils.merge(words, i + 1, words.length);
                } else {
                    startTime = ParserUtils.merge(words, i + 1, index);
                    endTime = ParserUtils.merge(words, index + 1, words.length);
                }
                break;
            }
            description = ParserUtils.merge(words, 1, Math.min(i + 1, words.length));

        }
        if (startTime != null) {
            try {
                startDate = Calendar.getInstance();
                startDate.setTime(simpleDateFormat.parse(startTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (endTime != null) {
            try {
                endDate = Calendar.getInstance();
                endDate.setTime(simpleDateFormat.parse(endTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        System.out.println("add " + description + " st: " + " ed: " + endDate.toString());
        return new AddCommand(description, tag, startDate, endDate);
    }
}
