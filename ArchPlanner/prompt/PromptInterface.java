package prompt;

import java.util.ArrayList;

public interface PromptInterface {
    public ArrayList<String> getPrompts(String input);
    public String getAutoWord();
}
