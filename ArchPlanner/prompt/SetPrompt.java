package prompt;

import separator.InputSeparator;

import java.util.ArrayList;

/**
 * @@author A0149647N
 * SetPrompt return the prompts of set command for the user input
 */
public class SetPrompt implements PromptInterface {
    private final String SET_PROMPT = "set filepath <New File Path>";
    private final String SET_PROMPT_DEFAULT = "set filepath default";
    private final String FILEPATH = "filepath";
    private final String DEFAULT = "default";
    private final String INVALID_KEYWORD = "Invalid keyword: set filepath <New File Path>";
    private final String INVALID_ID = "Invalid id: set filepath <New File Path>";
    private final String INVALID_DEFAULT = "Invalid command: set filepath default";
    ArrayList<String> promptList = new ArrayList<>();
    InputSeparator inputSeparator;

    @Override
    public ArrayList<String> getPrompts(String userInput) {
        this.inputSeparator = new InputSeparator(userInput);
        if (inputSeparator.getID() == null) {
            if (inputSeparator.getKeywordType() != null) {
                if (inputSeparator.getKeywordType() == InputSeparator.KeywordType.FILEPATH) {
                    if (inputSeparator.getParameter() == null) {
                        promptList.add(SET_PROMPT);
                        promptList.add(SET_PROMPT_DEFAULT);
                    } else if (DEFAULT.startsWith(inputSeparator.getParameter().toLowerCase())) {
                        promptList.add(SET_PROMPT_DEFAULT);
                        promptList.add(SET_PROMPT);
                    } else {
                        promptList.add(SET_PROMPT);
                    }
                }
            } else {
                if (inputSeparator.getParameter() == null || FILEPATH.startsWith(inputSeparator.getParameter().toLowerCase())) {
                    promptList.add(SET_PROMPT);
                    promptList.add(SET_PROMPT_DEFAULT);
                }
            }
        } else {
            promptList.add(INVALID_ID);
        }
        return promptList;
    }

    @Override
    public String getAutoWord() {
        if (inputSeparator.getKeywordType() != null && inputSeparator.getKeywordType() == InputSeparator.KeywordType.FILEPATH) {
            if (DEFAULT.startsWith(inputSeparator.getParameter().toLowerCase())) {
                return DEFAULT;
            }
        }
        return inputSeparator.getPartialKeyword();
    }
}
