package feedback.paser;

import java.text.SimpleDateFormat;

/**
 * Created by lifengshuang on 3/8/16.
 */
public class CommandParser {

    protected String input;
    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat();



    public CommandParser() {
    }

    public CommandParser(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
