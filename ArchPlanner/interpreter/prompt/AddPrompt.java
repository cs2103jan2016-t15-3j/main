package interpreter.prompt;

import java.util.ArrayList;

import interpreter.separator.AddInputSeparator;
import interpreter.separator.AddInputSeparator.AddKeyWordType;

/**
 * This class is used provide the relevant add command format to be displayed to the user to guide them to enter the
 * correct add command by interpreting the user inputed string.
 * 
 * @@author A0140034B
 */
public class AddPrompt implements PromptInterface { 
    private enum Parameter {
        NONE, DESCRIPTION, KEYWORD, PARTIAL_KEYWORD, KEYWORD_TO, START_DATE, START_TIME, END_DATE, END_TIME, TAG
    }
    
    private static final String STRING_EMPTY = "";
    private static final String STRING_SINGLE_SPACE = " ";
    private static final String STRING_MULTIPLE_SPACE = "\\s+";
    
    private static final String PROMPT_ADD = "add";
    private static final String PROMPT_DESCRIPTION = " <Description>";
    private static final String PROMPT_START_DATE = " <Start Date>";
    private static final String PROMPT_START_TIME = " <Start Time>";
    private static final String PROMPT_END_DATE = " <End Date>";
    private static final String PROMPT_END_TIME = " <End Time>";
    private static final String PROMPT_TAG = " #<Tag>";
    private static final String PROMPT_INVALID_TAG = "Invalid Tag: ";
    private static final String PROMPT_INVALID_DATE_RANGE = "Invalid Date Range: ";
    private static final String PROMPT_INVALID_DATE_RANGE_AND_TAG = "Invalid Date Range and Tag: ";    
    private static final String PROMPT_KEYWORD_TO = " to";
    
    private static final String KEYWORD_TO = "to";

    private static final int MIN_NUM_OF_TAG = 0;
    private static final int INDEX_OF_FIRST_WORD = 0;
  
    private AddInputSeparator _parameter;
    
    /**
     * Interpret the given string to determine the relevant prompts.
     * 
     * @param userInput     the string that will be interpreted to provide the relevant prompts.
     * @return              the prompts to display to user.
     */
    @Override
    public ArrayList<String> getPrompts(String userInput) {             
        _parameter = new AddInputSeparator(removeAddCommand(userInput));
        ArrayList<String> addPrompts = new ArrayList<String>();  
        String prompt = appendPrompt(_parameter);
        Parameter lastAppend = getLastAppend(_parameter);      
        addPromptsAfterLastAppend(addPrompts, prompt, _parameter, lastAppend);
        return addPrompts;
    }
    
    /**
     * Return the add keyword that first partially matched the previously interpreted string in getPrompt.
     * Return the empty string if there is no keyword to auto-complete.
     * 
     * @return  the keyword for auto-completing.
     */
    @Override
    public String getAutoWord() {
        if (isValidPartialKeyWord(_parameter)) {
            return getPartialKeyWordString(_parameter);
        } else if (isValidPartialKeyWordTo(_parameter)){
            return KEYWORD_TO;
        } else {
            return STRING_EMPTY;
        }
    }
    
    /**
     * Form the prompt string using the parameters that the user have already inputed.
     * 
     * @param parameter     the parameters that the user have already inputed.
     * @return              the string formed from the present parameters.
     */
    private String appendPrompt(AddInputSeparator parameter) {
        String prompt = PROMPT_ADD;
        if (parameter.hasDescription()) {
            prompt += PROMPT_DESCRIPTION;
        }
        if (parameter.hasPartialKeyWord()) {
            prompt += STRING_SINGLE_SPACE + getPartialKeyWordString(parameter);
        } else if (parameter.hasKeyWord()) {
            prompt += STRING_SINGLE_SPACE + getKeyWordString(parameter); 
        }      
        if (parameter.hasStartDate()) {
            prompt += PROMPT_START_DATE;
        }     
        if (parameter.hasStartTime()) {
            prompt += PROMPT_START_TIME;
        }
        if (parameter.hasKeyWordTo()) {
            prompt += PROMPT_KEYWORD_TO;
        }    
        if (parameter.hasEndDate()) {
            prompt += PROMPT_END_DATE;
        }  
        if (parameter.hasEndTime()) {
            prompt += PROMPT_END_TIME;
        } 
        if (parameter.hasTag()) {
            for (int i = MIN_NUM_OF_TAG; i < parameter.getTags().length; i++) {
                prompt += PROMPT_TAG;
            }
        }
        return prompt;
    }
    
    /**
     * Check the last parameter that user have inputed.
     * 
     * @param parameter     the parameters that the user have already inputed.
     * @return              the last parameter that user have inputed.
     */
    private Parameter getLastAppend(AddInputSeparator parameter) {
        Parameter lastAppend = Parameter.NONE;
        if (parameter.hasDescription()) {
            lastAppend = Parameter.DESCRIPTION;
        }
        if (parameter.hasPartialKeyWord()) {
            lastAppend = Parameter.PARTIAL_KEYWORD;
        } else if (parameter.hasKeyWord()) {
            lastAppend = Parameter.KEYWORD;  
        }      
        if (parameter.hasStartDate()) {
            lastAppend = Parameter.START_DATE;
        }     
        if (parameter.hasStartTime()) {
            lastAppend = Parameter.START_TIME;
        }
        if (parameter.hasKeyWordTo()) {
            lastAppend = Parameter.KEYWORD_TO;
        }    
        if (parameter.hasEndDate()) {
            lastAppend = Parameter.END_DATE;
        }  
        if (parameter.hasEndTime()) {
            lastAppend = Parameter.END_TIME;
        } 
        if (parameter.hasTag()) {
            lastAppend = Parameter.TAG;
        }
        return lastAppend;
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input by checking what have already been inputed.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     * @param parameter     the parameters that the user have already inputed.
     * @param lastAppend    the last parameter that user have inputed.
     */
    private void addPromptsAfterLastAppend(ArrayList<String> addPrompts, String prompt, AddInputSeparator parameter, Parameter lastAppend) {
        switch (lastAppend) {
            case NONE :
                addPromptsAfterAdd(addPrompts, prompt);
                break;             
            case DESCRIPTION :
                addPromptsAfterDescription(addPrompts, prompt, parameter);
                break;          
            case PARTIAL_KEYWORD :
                // Fallthrough
            case KEYWORD :
                addPromptsAfterKeyWord(addPrompts, prompt, parameter);
                break;                   
            case START_DATE :
                addPromptsAfterStartDate(addPrompts, prompt, parameter);
                break;             
            case START_TIME :                        
                addPromptsAfterStartTime(addPrompts, prompt, parameter);
                break;              
            case KEYWORD_TO :
                addPromptsAfterKeyWordTo(addPrompts, prompt);
                break;             
            case END_DATE :
                addPromptsAfterEndDate(addPrompts, prompt, parameter);
                break;             
            case END_TIME :                    
                addPromptsAfterEndTime(addPrompts, prompt, parameter);
                break;
            case TAG :
                addPromptsAfterTag(addPrompts, prompt, parameter);
                break;
        }
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input after add command.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     */
    private void addPromptsAfterAdd(ArrayList<String> addPrompts, String prompt) {
        prompt += PROMPT_DESCRIPTION;
        addPrompts.add(prompt);
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input after description.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     * @param parameter     the parameters that the user have already inputed.
     */
    private void addPromptsAfterDescription(ArrayList<String> addPrompts, String prompt, AddInputSeparator parameter) {
        if (!parameter.hasSpace()) {
            addPrompts.add(prompt);
        } else {
            addPrompts.add(prompt + PROMPT_TAG);
            addPrompts.add(prompt + STRING_SINGLE_SPACE + AddKeyWordType.ON.toString().toLowerCase() + 
                           PROMPT_START_DATE);
            addPrompts.add(prompt + STRING_SINGLE_SPACE + AddKeyWordType.BY.toString().toLowerCase() + 
                           PROMPT_END_DATE);
            addPrompts.add(prompt + STRING_SINGLE_SPACE + AddKeyWordType.FROM.toString().toLowerCase() + 
                           PROMPT_START_DATE + PROMPT_KEYWORD_TO + PROMPT_END_DATE);
        }
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input after keyword.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     * @param parameter     the parameters that the user have already inputed.
     */
    private void addPromptsAfterKeyWord(ArrayList<String> addPrompts, String prompt, AddInputSeparator parameter) {
        if (parameter.getKeyWord() == AddKeyWordType.BY || parameter.getPartialKeyWord() == AddKeyWordType.BY) {
            addPrompts.add(prompt + PROMPT_END_DATE);
            addPrompts.add(prompt + PROMPT_END_TIME);
        } else {
            addPrompts.add(prompt + PROMPT_START_DATE);
            addPrompts.add(prompt + PROMPT_START_TIME);
        }
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input after start date.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     * @param parameter     the parameters that the user have already inputed.
     */
    private void addPromptsAfterStartDate(ArrayList<String> addPrompts, String prompt, AddInputSeparator parameter) {
        if (parameter.getKeyWord() == AddKeyWordType.FROM) {
            addPrompts.add(prompt + PROMPT_KEYWORD_TO + PROMPT_END_DATE);
            addPrompts.add(prompt + PROMPT_START_TIME + PROMPT_KEYWORD_TO + PROMPT_END_TIME);
            addPrompts.add(prompt + PROMPT_START_TIME + PROMPT_KEYWORD_TO + PROMPT_END_DATE + PROMPT_END_TIME);
        } else {
            if (!parameter.hasSpace()) {
                addPrompts.add(prompt);
            }
            addPrompts.add(prompt + PROMPT_TAG);
            addPrompts.add(prompt + PROMPT_START_TIME);
        }
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input after start time.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     * @param parameter     the parameters that the user have already inputed.
     */
    private void addPromptsAfterStartTime(ArrayList<String> addPrompts, String prompt, AddInputSeparator parameter) {
        if (parameter.getKeyWord() == AddKeyWordType.FROM) {
            addPrompts.add(prompt + PROMPT_KEYWORD_TO + PROMPT_END_TIME);
            addPrompts.add(prompt + PROMPT_KEYWORD_TO + PROMPT_END_DATE);
            addPrompts.add(prompt + PROMPT_KEYWORD_TO + PROMPT_END_DATE + PROMPT_END_TIME);
        } else {
            if (!parameter.hasSpace()) {
                addPrompts.add(prompt);
            }
            addPrompts.add(prompt + PROMPT_TAG);
        }
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input after to.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     */
    private void addPromptsAfterKeyWordTo(ArrayList<String> addPrompts, String prompt) {
        addPrompts.add(prompt + PROMPT_END_TIME);
        addPrompts.add(prompt + PROMPT_END_DATE);
        addPrompts.add(prompt + PROMPT_END_DATE + PROMPT_END_TIME);
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input after end date.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     * @param parameter     the parameters that the user have already inputed.
     */
    private void addPromptsAfterEndDate(ArrayList<String> addPrompts, String prompt, AddInputSeparator parameter) {
        if (parameter.getKeyWord() == AddKeyWordType.FROM && !parameter.hasValidDateRange()) {
            if (!parameter.hasSpace()) {
                addPrompts.add(PROMPT_INVALID_DATE_RANGE  + prompt);
            } else {
                addPrompts.add(PROMPT_INVALID_DATE_RANGE + prompt + PROMPT_TAG);
            }
        } else {
            if (!parameter.hasSpace()) {
                addPrompts.add(prompt);
            }
            addPrompts.add(prompt + PROMPT_TAG);
            addPrompts.add(prompt + PROMPT_END_TIME);
        }
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input after end time.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     * @param parameter     the parameters that the user have already inputed.
     */
    private void addPromptsAfterEndTime(ArrayList<String> addPrompts, String prompt, AddInputSeparator parameter) {
        if (parameter.getKeyWord() == AddKeyWordType.FROM && !parameter.hasValidDateRange()) {
            if (!parameter.hasSpace()) {
                addPrompts.add(PROMPT_INVALID_DATE_RANGE  + prompt);
            } else {
                addPrompts.add(PROMPT_INVALID_DATE_RANGE + prompt + PROMPT_TAG);
            }
        } else {
            if (!parameter.hasSpace()) {
                addPrompts.add(prompt);
            }
            addPrompts.add(prompt + PROMPT_TAG);
        }
    }
    
    /**
     * Add the additional prompts to guide the user on the next parameter that he could input after tags.
     * 
     * @param addPrompts    the list of prompts that will be form in this method to guide the user on the next parameter.
     * @param prompt        the string formed by the parameters that have already been inputed.
     * @param parameter     the parameters that the user have already inputed.
     */
    private void addPromptsAfterTag(ArrayList<String> addPrompts, String prompt, AddInputSeparator parameter) {
        if (parameter.getKeyWord() == AddKeyWordType.FROM && !parameter.hasValidDateRange()) {
            if (parameter.hasValidTag()) {
                addPrompts.add(PROMPT_INVALID_DATE_RANGE  + prompt);
            } else {
                addPrompts.add(PROMPT_INVALID_DATE_RANGE_AND_TAG  + prompt);
            }
        } else {
            if (parameter.hasValidTag()) {
                addPrompts.add(prompt);
            } else {
                addPrompts.add(PROMPT_INVALID_TAG  + prompt);
            }
        }
    }

    private boolean isValidPartialKeyWord(AddInputSeparator parameter) {
        return parameter.getPartialKeyWord() != AddKeyWordType.UNKNOWN;
    }

    private boolean isValidPartialKeyWordTo(AddInputSeparator parameter) {
        if (parameter.getKeyWord() != AddKeyWordType.FROM) {
            return false;
        }    
        if (!parameter.hasKeyWordTo()) {
            return false;
        }     
        if (parameter.getEndDateTime() != null) {
            return false;
        }
        return true;
    }
    
    private String getPartialKeyWordString(AddInputSeparator parameter) {
        return parameter.getPartialKeyWord().toString().toLowerCase();
    }

    private String getKeyWordString(AddInputSeparator parameter) {
        return parameter.getKeyWord().toString().toLowerCase();
    }
    
    private String removeAddCommand(String input) {
        return input.substring(getFirstWord(input).length());
    }
    
    private String getFirstWord(String input) {
        return input.trim().split(STRING_MULTIPLE_SPACE)[INDEX_OF_FIRST_WORD];
    }
}
