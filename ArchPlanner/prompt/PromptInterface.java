//@@author A0140034B
package prompt;

import java.util.ArrayList;

public interface PromptInterface {
    ArrayList<String> getPrompts(String input);
    String getAutoWord();
}
