package feedback.paser;

import logic.commands.Command;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 3/8/16.
 */
public abstract class CommandParser {

    protected String input;
    protected ArrayList<String> tagList;

    public String detectTagAndRemoveTagString(String input) {
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

    public String[] split(String input) {
        return input.split("\\s+");
    }

//    public String merge(String[] words) {
//        String result = "";
//        for (int i = 0; i < words.length; i++) {
//            result += words[i];
//            if (i != words.length - 1) {
//                result += " ";
//            }
//        }
//        return result;
//    }
//
//    public String merge(String[] words, int startIndex, int endIndex) {
//        String result = "";
//        if (startIndex < 0 || endIndex > words.length || startIndex > endIndex) {
//            return result;
//        }
//        for (int i = startIndex; i < endIndex; i++) {
//            result += words[i];
//            if (i != endIndex - 1) {
//                result += " ";
//            }
//        }
//        return result;
//    }





}
