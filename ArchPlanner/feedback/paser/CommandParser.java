package feedback.paser;

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
        for (String word : words) {
            if (word.charAt(0) == '#') {
                tagList.add(word.substring(1, word.length()));
            } else {
                result += word + " ";
            }
        }
        return result.trim();
    }

    public abstract Command parse(String input);

    protected String[] split(String input) {
        return input.split("\\s+");
    }


}
