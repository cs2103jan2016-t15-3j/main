//@@author A0140034B
package prompt;

import java.util.ArrayList;

import separator.AddInputSeparator;
import separator.AddInputSeparator.AddKeyWordType;

public class AddPrompt implements PromptInterface {
 
    private static final String STRING_EMPTY = "";
    private static final String STRING_SINGLE_WHITESPACE = " ";
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    
    private static final String PROMPT_ADD = "add";
    private static final String PROMPT_DESCRIPTION = "<Description>";
    private static final String PROMPT_START_DATE = "<Start Date>";
    private static final String PROMPT_START_TIME = "<Start Time>";
    private static final String PROMPT_END_DATE = "<End Date>";
    private static final String PROMPT_END_TIME = "<End Time>";
    private static final String PROMPT_TAG = "#<Tag>";
    private static final String PROMPT_INVALID_TAG = "Invalid Tag:";
    private static final String PROMPT_INVALID_DATE_RANGE = "Invalid Date Range:";
    
    private static final String KEYWORD_TO = "to";
    
    private enum Parameter {
        DESCRIPTION, KEYWORD, PARTIALKEYWORD, KEYWORDTO, STARTDATE, STARTTIME, ENDDATE, ENDTIME
    };
    
    private ArrayList<String> _addPrompts;
    private String _prompt;
    private Parameter _lastAppend;
    private AddInputSeparator _parameter;
    
    public AddPrompt() {
        _addPrompts = new ArrayList<String>();
    }
    
    @Override
    public ArrayList<String> getPrompts(String userInput) {
        _prompt = PROMPT_ADD;
        _addPrompts.clear();
        
        userInput = removeFirstWord(userInput);
 
        if (userInput.isEmpty()) {
            _addPrompts.add(_prompt);
            return _addPrompts;
        } else {
            userInput = userInput.substring(1);
        }
  
        _parameter = new AddInputSeparator(userInput);
        
        _prompt += STRING_SINGLE_WHITESPACE + PROMPT_DESCRIPTION;
        _lastAppend = Parameter.DESCRIPTION;
        
        if (!_parameter.hasDescription()) {
            _addPrompts.add(_prompt);
            return _addPrompts;
        }
        
        if (_parameter.hasPartialKeyWord()) {
            _prompt += STRING_SINGLE_WHITESPACE + _parameter.getPartialKeyWord().toString().toLowerCase();
            _lastAppend = Parameter.PARTIALKEYWORD;
        } else if (_parameter.hasKeyWord()) {
            _prompt += STRING_SINGLE_WHITESPACE + _parameter.getKeyWord().toString().toLowerCase();
            _lastAppend = Parameter.KEYWORD; 
        }      
        
        if (_parameter.hasStartDate()) {
            _prompt += STRING_SINGLE_WHITESPACE + PROMPT_START_DATE;
            _lastAppend = Parameter.STARTDATE;
        }
        
        if (_parameter.hasStartTime()) {
            _prompt += STRING_SINGLE_WHITESPACE + PROMPT_START_TIME;
            _lastAppend = Parameter.STARTTIME;
        }
        
        if (_parameter.hasPartialKeyWordTo()) {
            _prompt += STRING_SINGLE_WHITESPACE + KEYWORD_TO;
            _lastAppend = Parameter.KEYWORDTO;
        }
        
        if (_parameter.hasEndDate()) {
            _prompt += STRING_SINGLE_WHITESPACE + PROMPT_END_DATE;
            _lastAppend = Parameter.ENDDATE;
        }
        
        if (_parameter.hasEndTime()) {
            _prompt += STRING_SINGLE_WHITESPACE + PROMPT_END_TIME;
            _lastAppend = Parameter.ENDTIME;
        }
        
        if (!_parameter.hasTag()) {
            switch (_lastAppend) {
                case DESCRIPTION :
                    if (!_parameter.hasSpace()) {
                        _addPrompts.add(_prompt);
                    } else {
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_TAG);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + AddKeyWordType.ON.toString().toLowerCase() + 
                                STRING_SINGLE_WHITESPACE + PROMPT_START_DATE);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + AddKeyWordType.BY.toString().toLowerCase() + 
                                STRING_SINGLE_WHITESPACE + PROMPT_END_DATE);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + AddKeyWordType.FROM.toString().toLowerCase() + 
                                STRING_SINGLE_WHITESPACE + PROMPT_START_DATE + " to " + PROMPT_END_DATE);
                    }
                    break;
                
                case PARTIALKEYWORD :
                    // Fallthrough
                case KEYWORD :
                    if (_parameter.getKeyWord() == AddKeyWordType.BY || _parameter.getPartialKeyWord() == AddKeyWordType.BY) {
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_END_DATE);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_END_TIME);
                    } else {
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_START_DATE);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_START_TIME);
                    }
                    break;     
                    
                case STARTDATE :
                    if (_parameter.getKeyWord() == AddKeyWordType.FROM) {
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + KEYWORD_TO + STRING_SINGLE_WHITESPACE + PROMPT_END_DATE);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_START_TIME + STRING_SINGLE_WHITESPACE + 
                                       KEYWORD_TO + STRING_SINGLE_WHITESPACE + PROMPT_END_TIME);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_START_TIME + STRING_SINGLE_WHITESPACE + 
                                       KEYWORD_TO + STRING_SINGLE_WHITESPACE + PROMPT_END_DATE + STRING_SINGLE_WHITESPACE + PROMPT_END_TIME);
                    } else {
                        if (!_parameter.hasSpace()) {
                            _addPrompts.add(_prompt);
                        }
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_TAG);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_START_TIME);
                    }
                    break;
                    
                case STARTTIME :                        
                    if (_parameter.getKeyWord() == AddKeyWordType.FROM) {
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + KEYWORD_TO + STRING_SINGLE_WHITESPACE + PROMPT_END_TIME);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + KEYWORD_TO + STRING_SINGLE_WHITESPACE + PROMPT_END_DATE);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + KEYWORD_TO + STRING_SINGLE_WHITESPACE + 
                                       PROMPT_END_DATE + STRING_SINGLE_WHITESPACE + PROMPT_END_TIME);
                    } else {
                        if (!_parameter.hasSpace()) {
                            _addPrompts.add(_prompt);
                        }
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_TAG);
                    }
                    break;
                    
                case KEYWORDTO :
                    _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_END_TIME);
                    _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_END_DATE);
                    _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_END_DATE + STRING_SINGLE_WHITESPACE + PROMPT_END_TIME);
                    break;
                    
                case ENDDATE :
                    if (_parameter.getKeyWord() == AddKeyWordType.FROM && !_parameter.hasValidDateRange()) {
                        if (!_parameter.hasSpace()) {
                            _addPrompts.add(PROMPT_INVALID_DATE_RANGE + STRING_SINGLE_WHITESPACE + _prompt);
                        } else {
                            _addPrompts.add(PROMPT_INVALID_DATE_RANGE + STRING_SINGLE_WHITESPACE + PROMPT_TAG);
                        }
                    } else {
                        if (!_parameter.hasSpace()) {
                            _addPrompts.add(_prompt);
                        }
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_TAG);
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_END_TIME);
                    }
                    break;
                    
                case ENDTIME :                    
                    if (_parameter.getKeyWord() == AddKeyWordType.FROM && !_parameter.hasValidDateRange()) {
                        if (!_parameter.hasSpace()) {
                            _addPrompts.add(PROMPT_INVALID_DATE_RANGE + STRING_SINGLE_WHITESPACE + _prompt);
                        } else {
                            _addPrompts.add(PROMPT_INVALID_DATE_RANGE + STRING_SINGLE_WHITESPACE + PROMPT_TAG);
                        }
                    } else {
                        if (!_parameter.hasSpace()) {
                            _addPrompts.add(_prompt);
                        }
                        _addPrompts.add(_prompt + STRING_SINGLE_WHITESPACE + PROMPT_TAG);
                    }
                    break;
                    
                default :
                    _addPrompts.add(_prompt);
                    break;
            }        
        } else {
            for (int i = 0; i < _parameter.getTags().length; i++) {
                _prompt += STRING_SINGLE_WHITESPACE + PROMPT_TAG;
            }
            if (_parameter.hasValidTag()) {
                _addPrompts.add(_prompt);
            } else {
                _addPrompts.add(PROMPT_INVALID_TAG + STRING_SINGLE_WHITESPACE + _prompt);
            }
        }
        return _addPrompts;
    }
    
    @Override
    public String getAutoWord() {
        if (_parameter.getPartialKeyWord() != AddKeyWordType.UNKNOWN) {
            return _parameter.getPartialKeyWord().name().toLowerCase();
        } else if (_parameter.getKeyWord() == AddKeyWordType.FROM && _parameter.hasPartialKeyWordTo() && _parameter.getEndDateTime() == null){
            return KEYWORD_TO;         
        } else {
            return STRING_EMPTY;
        }
    }
    
    private String removeFirstWord(String input) {
        return input.substring(input.indexOf(getFirstWord(input)) + 3);
    }
    
    private String getFirstWord(String input) {
        String commandTypeString = input.trim().split(STRING_MULTIPLE_WHITESPACE)[0];
        return commandTypeString;
    }
}
