package feedback.paser.time;

/**
 * Created by lifengshuang on 3/11/16.
 */
public class TimeParser {

    private static String PATTERN_MMMDD_1 = "%MMM% %DD%";
    private static String PATTERN_MMMDD_2 = "%MMM%.%DD%";

    private static String PATTERN_EEE = "%EEE%";

    private static String PATTERN_TOMORROW_YESTERDAY = "%tt%";

    private static String PATTERN_TIME_1 = "%HH%%a%";
    private static String PATTERN_TIME_2 = "%HH% o'clock%a%";
    private static String PATTERN_TIME_3 = "%HH%:%mm%%a%";
    private static String PATTERN_TIME_4 = "%HH%:%mm%:%ss%%a%";

    private static String PATTERN_FULL_DATE = "%yy% $MMMDD$ $TIME$";

}
