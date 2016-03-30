package parser;

import com.joestelmach.natty.Parser;
import logic.commands.Command;

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
     * Parse the tags from the end of the input string
     * @param input user's input
     * @return string after removing the tags
     */
    protected String parseTag(String input) {
        String[] words = split(input);
        String result = "";

        for (int i = words.length - 1; i >= 0; i--) {
            if (words[i].charAt(0) == '#') {
                if (words[i].length() == 1) {
                    return null;
                }
                tagList.add(words[i]);
            } else {
                for (int j = 0; j <= i; j++) {
                    result += words[j] + " ";
                }
                break;
            }
        }
        return result.trim();
    }

    /**
     * Key method for parser to parse commands.
     * @param input string to parse
     * @return parsed Command object
     */
    public abstract Command parse(String input);

    /**
     * A utility method to split a string by space
     * @param input string to split
     * @return string array splitted
     */
    protected String[] split(String input) {
        return input.split("\\s+");
    }


}
