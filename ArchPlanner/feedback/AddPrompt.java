package feedback;

import java.util.ArrayList;

import parser.AddInputSeparator;
import parser.AddInputSeparator.KeyWordType;

public class AddPrompt {
    
    public enum Parameter {
        DESCRIPTION, KEYWORD, PARTIALKEYWORD, KEYWORDTO, STARTDATE, STARTTIME, ENDDATE, ENDTIME, TAG
    };
    
    private final String STRING_SINGLE_WHITESPACE = " ";
    private final String addKeyWord = "add";
    private final String desPrompt = "<Description>";
    private final String sdPrompt = "<Start Date>";
    private final String stPrompt = "<Start Time>";
    private final String edPrompt = "<End Date>";
    private final String etPrompt = "<End Time>";
    private final String tagPrompt = "#<Tag>";
    private final String invalidTagPrompt = "Invalid Tag:";
    private final String invalidDateRangePrompt = "Invalid Date Range:";
    
    private ArrayList<String> addPrompts;
    private String prompt;
    private Parameter lastAppend;
    
    public AddPrompt() {
        addPrompts = new ArrayList<String>();
        prompt = addKeyWord;
    }
    
    public ArrayList<String> getPrompts(String userInput) {
        
        if (userInput.isEmpty()) {
            addPrompts.add(prompt);
            return addPrompts;
        } else {
            userInput = userInput.substring(1);
        }
        //-----------------------------------------------------------------
        
        AddInputSeparator parameter = new AddInputSeparator(userInput);
        
        prompt += STRING_SINGLE_WHITESPACE + desPrompt;
        lastAppend = Parameter.DESCRIPTION;
        
        if (!parameter.hasDescription()) {
            addPrompts.add(prompt);
            return addPrompts;
        }
        
        if (parameter.hasPartialKeyWord()) {
            prompt += STRING_SINGLE_WHITESPACE + parameter.getPartialKeyWord().toString().toLowerCase();
            lastAppend = Parameter.PARTIALKEYWORD;
        } else if (parameter.hasKeyWord()) {
            prompt += STRING_SINGLE_WHITESPACE + parameter.getKeyWord().toString().toLowerCase();
            lastAppend = Parameter.KEYWORD; 
        }      
        
        if (parameter.hasStartDate()) {
            prompt += STRING_SINGLE_WHITESPACE + sdPrompt;
            lastAppend = Parameter.STARTDATE;
        }
        
        if (parameter.hasStartTime()) {
            prompt += STRING_SINGLE_WHITESPACE + stPrompt;
            lastAppend = Parameter.STARTTIME;
        }
        
        if (parameter.hasPartialKeyWordTo()) {
            prompt += STRING_SINGLE_WHITESPACE + "to";
            lastAppend = Parameter.KEYWORDTO;
        }
        
        if (parameter.hasEndDate()) {
            prompt += STRING_SINGLE_WHITESPACE + edPrompt;
            lastAppend = Parameter.ENDDATE;
        }
        
        if (parameter.hasEndTime()) {
            prompt += STRING_SINGLE_WHITESPACE + etPrompt;
            lastAppend = Parameter.ENDTIME;
        }
        
        if (!parameter.hasTag()) {
            if (parameter.hasSpace()) {
                switch (lastAppend) {
                    case DESCRIPTION :
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + KeyWordType.ON.toString().toLowerCase() + 
                                STRING_SINGLE_WHITESPACE + sdPrompt);
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + KeyWordType.BY.toString().toLowerCase() + 
                                STRING_SINGLE_WHITESPACE + edPrompt);
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + KeyWordType.FROM.toString().toLowerCase() + 
                                STRING_SINGLE_WHITESPACE + sdPrompt + " to " + edPrompt);
                        break;
                    
                    case PARTIALKEYWORD :
                        // Fallthrough
                    case KEYWORD :
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + sdPrompt);
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + stPrompt);
                        break;
                        
                    case KEYWORDTO :
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + edPrompt);
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + edPrompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        break;
                        
                    case STARTDATE :
                        if (parameter.getKeyWord() == KeyWordType.FROM) {
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + "to" + STRING_SINGLE_WHITESPACE + edPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + stPrompt + STRING_SINGLE_WHITESPACE + 
                                           "to" + STRING_SINGLE_WHITESPACE + edPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + stPrompt + STRING_SINGLE_WHITESPACE + 
                                           "to" + STRING_SINGLE_WHITESPACE + edPrompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        } else {
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + stPrompt);
                        }
                        break;
                        
                    case STARTTIME :
                        if (parameter.getKeyWord() == KeyWordType.FROM) {
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + "to" + STRING_SINGLE_WHITESPACE + etPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + "to" + STRING_SINGLE_WHITESPACE + edPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + "to" + STRING_SINGLE_WHITESPACE + 
                                           edPrompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        } else {
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                        }
                        break;
                        
                    case ENDDATE :
                        if (parameter.hasValidDateRange()) {
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        } else {
                            addPrompts.add(invalidDateRangePrompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                        }
                        break;
                        
                    case ENDTIME :
                        if (parameter.hasValidDateRange()) {
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                        } else {
                            addPrompts.add(invalidDateRangePrompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                        }
                        break;
                        
                    default :
                        addPrompts.add(prompt);
                        break;
                }
            } else {
                switch (lastAppend) {
                    case PARTIALKEYWORD :
                        // Fallthrough                  
                    case KEYWORD :
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + sdPrompt);
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + stPrompt);
                        break;
                        
                    case KEYWORDTO :
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + edPrompt);
                        addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + edPrompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        break;
                        
                    case STARTDATE :
                        if (parameter.getKeyWord() == KeyWordType.FROM) {
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + "to" + STRING_SINGLE_WHITESPACE + edPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + stPrompt + STRING_SINGLE_WHITESPACE + 
                                           "to" + STRING_SINGLE_WHITESPACE + edPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + stPrompt + STRING_SINGLE_WHITESPACE + 
                                           "to" + STRING_SINGLE_WHITESPACE + edPrompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        } else {
                            addPrompts.add(prompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + stPrompt);
                        }
                        break;
       
                    case STARTTIME :
                        if (parameter.getKeyWord() == KeyWordType.FROM) {
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + "to" + STRING_SINGLE_WHITESPACE + etPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + "to" + STRING_SINGLE_WHITESPACE + edPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + "to" + STRING_SINGLE_WHITESPACE + 
                                           edPrompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        } else {
                            addPrompts.add(prompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                        }
                        break;
                        
                    case ENDDATE :
                        if (parameter.hasValidDateRange()) {
                            addPrompts.add(prompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + etPrompt);
                        } else {
                            addPrompts.add(invalidDateRangePrompt + STRING_SINGLE_WHITESPACE + prompt);
                        }
                        break;
                        
                    case ENDTIME : 
                        if (parameter.hasValidDateRange()) {
                            addPrompts.add(prompt);
                            addPrompts.add(prompt + STRING_SINGLE_WHITESPACE + tagPrompt);
                        } else {
                            addPrompts.add(invalidDateRangePrompt + STRING_SINGLE_WHITESPACE + prompt);
                        }
                        break;
                        
                    default :
                        addPrompts.add(prompt);
                        break;
                }
            }
        } else {
            for (int i = 0; i < parameter.getTags().length; i++) {
                prompt += STRING_SINGLE_WHITESPACE + tagPrompt;
            }
            if (parameter.hasValidTag()) {
                addPrompts.add(prompt);
            } else {
                addPrompts.add(invalidTagPrompt + STRING_SINGLE_WHITESPACE + prompt);
            }
        }
        return addPrompts;
    }
}
