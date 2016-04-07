package parser.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @@author A0149647N
 * TimeParserResult hold the result after parse time with Natty.
 * TimeParserResult also check the time and update it according to the rule.
 */
public class TimeParserResult {
    private int matchPosition;
    private String matchString;
    private LocalDate firstDate;
    private LocalDate secondDate;
    private LocalTime firstTime;
    private LocalTime secondTime;
    private boolean timeValid;
    private DateTimeStatus rawDateTimeStatus = DateTimeStatus.NONE;

    public enum DateTimeStatus {
        NONE, END_TIME, END_DATE, END_DATE_END_TIME,
        START_TIME, START_TIME_END_TIME, START_TIME_END_DATE, START_TIME_END_DATE_END_TIME,
        START_DATE, START_DATE_END_TIME, START_DATE_END_DATE, START_DATE_END_DATE_END_TIME,
        START_DATE_START_TIME, START_DATE_START_TIME_END_TIME,
        START_DATE_START_TIME_END_DATE, START_DATE_START_TIME_END_DATE_END_TIME;
    }
    public void checkInvalid() {
        timeValid = true;
        DateTimeStatus status = getDateTimeStatus();
        switch (status) {
            case START_DATE_END_DATE:
                if (firstDate.isAfter(secondDate)) {
                    timeValid = false;
                }
                break;
            case START_DATE_START_TIME_END_DATE_END_TIME:
                if (firstDate.isAfter(secondDate)) {
                    timeValid = false;
                } else if (firstDate.isEqual(secondDate) && !firstTime.isBefore(secondTime)) {
                    timeValid = false;
                }
                break;
        }
    }


    public void updateDateTime() {
        rawDateTimeStatus = getDateTimeStatus();
        DateTimeStatus status = getDateTimeStatus();
        switch (status) {
            //0101
            case START_TIME_END_TIME:
                firstDate = LocalDate.now();
                secondDate = LocalDate.now();
                break;
            //0110
            case START_TIME_END_DATE:
                firstDate = LocalDate.now();
                secondTime = LocalTime.of(23, 59, 59);
                break;
            //0111
            case START_TIME_END_DATE_END_TIME:
                firstDate = LocalDate.now();
                break;
            //1001
            case START_DATE_END_TIME:
                firstTime = LocalTime.of(0, 0, 0);
                secondDate = LocalDate.now();
                break;
            //1011
            case START_DATE_END_DATE_END_TIME:
                firstTime = LocalTime.of(0, 0, 0);
                break;
            //1101
            case START_DATE_START_TIME_END_TIME:
                secondDate = firstDate;
                break;
            //1110
            case START_DATE_START_TIME_END_DATE:
                secondTime = LocalTime.of(23, 59, 59);
                break;
            default:
                break;
        }
    }

    public DateTimeStatus getDateTimeStatus() {
        DateTimeStatus dateTimeStatus = DateTimeStatus.NONE;
        if (secondTime != null) {
            dateTimeStatus = DateTimeStatus.END_TIME;
        }
        if (secondDate != null) {
            switch (dateTimeStatus) {
                case NONE:
                    dateTimeStatus = DateTimeStatus.END_DATE;
                    break;
                case END_TIME:
                    dateTimeStatus = DateTimeStatus.END_DATE_END_TIME;
                    break;
            }
        }
        if (firstTime != null) {
            switch (dateTimeStatus) {
                case NONE:
                    dateTimeStatus = DateTimeStatus.START_TIME;
                    break;
                case END_TIME:
                    dateTimeStatus = DateTimeStatus.START_TIME_END_TIME;
                    break;
                case END_DATE:
                    dateTimeStatus = DateTimeStatus.START_TIME_END_DATE;
                    break;
                case END_DATE_END_TIME:
                    dateTimeStatus = DateTimeStatus.START_TIME_END_DATE_END_TIME;
                    break;
            }
        }
        if (firstDate != null) {
            switch (dateTimeStatus) {
                case NONE:
                    dateTimeStatus = DateTimeStatus.START_DATE;
                    break;
                case END_TIME:
                    dateTimeStatus = DateTimeStatus.START_DATE_END_TIME;
                    break;
                case END_DATE:
                    dateTimeStatus = DateTimeStatus.START_DATE_END_DATE;
                    break;
                case END_DATE_END_TIME:
                    dateTimeStatus = DateTimeStatus.START_DATE_END_DATE_END_TIME;
                    break;
                case START_TIME:
                    dateTimeStatus = DateTimeStatus.START_DATE_START_TIME;
                    break;
                case START_TIME_END_TIME:
                    dateTimeStatus = DateTimeStatus.START_DATE_START_TIME_END_TIME;
                    break;
                case START_TIME_END_DATE:
                    dateTimeStatus = DateTimeStatus.START_DATE_START_TIME_END_DATE;
                    break;
                case START_TIME_END_DATE_END_TIME:
                    dateTimeStatus = DateTimeStatus.START_DATE_START_TIME_END_DATE_END_TIME;
                    break;
            }
        }
        return dateTimeStatus;
    }

    public DateTimeStatus getRawDateTimeStatus() {
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
