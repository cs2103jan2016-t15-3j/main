package paser;

import com.joestelmach.natty.Parser;
import logic.commands.Command;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 3/8/16.
 */
public abstract class CommandParser {

    protected String input;
    protected ArrayList<String> tagList = new ArrayList<>();
    protected static Parser timeParser = new Parser();


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

    public abstract Command parse(String input);

    protected String[] split(String input) {
        return input.split("\\s+");
    }


}
