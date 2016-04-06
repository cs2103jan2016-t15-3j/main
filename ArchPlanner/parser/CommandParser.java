package parser;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 3/8/16.
 */

/**
 * CommandParser is the super class of all command parsers.
 *
 */
public abstract class CommandParser {

    protected String input;
    protected ArrayList<String> tagList = new ArrayList<>();



    /**
     * Key method for parser to parse commands.
     * @param input string to parse
     * @return parsed Command object
     */
//    public abstract Command parse(String input);

    /**
     * A utility method to split a string by space
     * @param input string to split
     * @return string array splitted
     */
    protected String[] split(String input) {
        return input.split("\\s+");
    }


}
