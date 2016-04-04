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
    private boolean timeValid;
    private int rawDateTimeStatus;

    public enum DateTimeStatus {START_DATE, START_DATE_START_TIME, }
//1000
    //1100
    public void checkInvalid() {
        timeValid = true;
        int status = getDateTimeStatus();
        switch (status) {
            //1010
            case 10:
                if (firstDate.isAfter(secondDate)) {
                    timeValid = false;
                }
                break;
            //1111
            case 15:
                if (firstDate.isAfter(secondDate)) {
                    timeValid = false;
                } else if (firstDate.isEqual(secondDate) && firstTime.isAfter(secondTime)) {
                    timeValid = false;
                }
                break;
        }
    }


    public void updateDateTime() {
        rawDateTimeStatus = getDateTimeStatus();
        int status = getDateTimeStatus();
        switch (status) {
            //0101
            case 5:
                firstDate = LocalDate.now();
                secondDate = LocalDate.now();
                break;
            //0110
            case 6:
                firstDate = LocalDate.now();
                secondTime = LocalTime.of(23, 59, 59);
                break;
            //0111
            case 7:
                firstDate = LocalDate.now();
                break;
            //1001
            case 9:
                firstTime = LocalTime.of(0, 0, 0);
                secondDate = LocalDate.now();
                break;
            //1011
            case 11:
                firstTime = LocalTime.of(0, 0, 0);
                break;
            //1101
            case 13:
                secondDate = firstDate;
                break;
            //1110
            case 14:
                secondTime = LocalTime.of(23, 59, 59);
                break;
            default:
                break;
        }
    }

    public int getDateTimeStatus() {
        int status = 0;
        if (secondTime != null) {
            status += 1;
        }
        if (secondDate != null) {
            status += 2;
        }
        if (firstTime != null) {
            status += 4;
        }
        if (firstDate != null) {
            status += 8;
        }
        return status;
    }

    public int getRawDateTimeStatus() {
        return rawDateTimeStatus;
    }

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

    public void setFirstDate(Date date) {

        firstDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setSecondDate(Date date) {
        secondDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setFirstTime(Date date) {
        firstTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public void setSecondTime(Date date) {
        secondTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

//    public void setDate(Date date) {
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        if (firstDate == null) {
//            firstDate = localDate;
//        } else if (secondDate == null) {
//            secondDate = localDate;
//        }
//    }
//
//    public void setTime(Date date) {
//        LocalTime localTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
//        if (firstTime == null) {
//            firstTime = localTime;
//        } else if (secondTime == null) {
//            secondTime = localTime;
//        }
//    }

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

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public void setSecondDate(LocalDate secondDate) {
        this.secondDate = secondDate;
    }

    public void setFirstTime(LocalTime firstTime) {
        this.firstTime = firstTime;
    }

    public void setSecondTime(LocalTime secondTime) {
        this.secondTime = secondTime;
    }

    public boolean isTimeValid() {
        return timeValid;
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
