package parser.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by lifengshuang on 3/22/16.
 */
public class TimeParserResult {
    private int matchPosition;
    private String matchString;
    private LocalDate firstDate;
    private LocalDate secondDate;
    private LocalTime firstTime;
    private LocalTime secondTime;

    public int getMatchPosition() {
        return matchPosition;
    }

    public void setMatchPosition(int matchPosition) {
        this.matchPosition = matchPosition;
    }

    public String getMatchString() {
        return matchString;
    }

    public void setMatchString(String matchString) {
        this.matchString = matchString;
    }

    public void setDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (firstDate == null) {
            firstDate = localDate;
        } else if (secondDate == null) {
            secondDate = localDate;
        }
    }

    public void setTime(Date date) {
        LocalTime localTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        if (firstTime == null) {
            firstTime = localTime;
        } else if (secondTime == null) {
            secondTime = localTime;
        }
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public LocalDate getSecondDate() {
        return secondDate;
    }

    public LocalTime getFirstTime() {
        return firstTime;
    }

    public LocalTime getSecondTime() {
        return secondTime;
    }

    public boolean hasNoDateAndOneTime() {
        return firstDate == null && secondDate == null && firstTime != null && secondTime == null;
    }

    public boolean hasOneDateAndNoTime() {
        return firstDate != null && secondDate == null && firstTime == null && secondTime == null;
    }

    public boolean hasTwoDateAndNoTime() {
        return firstDate != null && secondDate != null && firstTime == null && secondTime == null;
    }
}
