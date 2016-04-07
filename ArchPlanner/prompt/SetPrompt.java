package prompt;

import separator.InputSeparator;

import java.util.ArrayList;

/**
 * @@author A0149647N
 * SetPrompt return the prompts of set command for the user input
 */
public class SetPrompt implements PromptInterface{
    private final String SET_PROMPT = "set filepath <New File Path>";
    private final String INVALID_KEYWORD = "Invalid keyword: set filepath <New File Path>";
    private final String INVALID_ID = "Invalid id: set filepath <New File Path>";
    ArrayList<String> promptList = new ArrayList<>();
    InputSeparator inputSeparator;

    @Override
    public ArrayList<String> getPrompts(String userInput) {
        this.inputSeparator = new InputSeparator(userInput);
        if (inputSeparator.getID() == null) {
            if (inputSeparator.getKeywordType() != null && inputSeparator.getKeywordType() == InputSeparator.KeywordType.FILEPATH) {
                promptList.add(SET_PROMPT);
            } else {
                if (inputSeparator.getParameter() != null && inputSeparator.getParameter().split(" ").length > 1) {
                    promptList.add(INVALID_KEYWORD);
                } else {
                    promptList.add(SET_PROMPT);
                }
            }
        } else {
            promptList.add(INVALID_ID);
        }
        return promptList;
    }

    @Override
    public String getAutoWord() {
        return inputSeparator.getPartialKeyword();
    }
}
