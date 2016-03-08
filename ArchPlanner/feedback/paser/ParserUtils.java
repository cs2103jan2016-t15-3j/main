package feedback.paser;

/**
 * Created by lifengshuang on 3/8/16.
 */
public class ParserUtils {
    public static String[] split(String input) {
        return input.split("\\s+");
    }

    public static String merge(String[] words) {
        String result = "";
        for (int i = 0; i < words.length; i++) {
            result += words[i];
            if (i != words.length - 1) {
                result += " ";
            }
        }
        return result;
    }

    //todo: bug
    public static String merge(String[] words, int startIndex, int endIndex) {
        String result = "";
        for (int i = startIndex; i < endIndex; i++) {
            result += words[i];
            if (i != endIndex - 1) {
                result += " ";
            }
        }
        return result;
    }

    public static String retrieveTag(CommandParser parser) {
        String tag = null;
        String input = parser.getInput();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '#') {
                tag = input.substring(i + 1);
                parser.setInput(input.substring(0, i));
                break;
            }
        }
        return tag;
    }

}
