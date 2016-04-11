package prompt;

import java.util.ArrayList;

/**
 * This is an Interface for all Prompt Objects to provide prompts and auto-complete words
 * 
 * @@author A0140034B
 */
public interface PromptInterface {
    ArrayList<String> getPrompts(String input);
    String getAutoWord();
}
