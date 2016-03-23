package feedback;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class AddPrompt {
    
    enum KeyWordType {
        ON, BY, FROM, UNKNOWN
    };
    
    private static final String STRING_MULTIPLE_WHITESPACE = "\\s+";
    
    private final String ADD = "add";
    private final String ADD_DESCRIPTION = "add <Description>";
    private final String ADD_DESCRIPTION_TAG = "add <Description> #<Tag>";
    private final String ADD_DESCRIPTION_ON_DATE = "add <Description> on <Start Date>";
    private final String ADD_DESCRIPTION_ON_DATE_TAG = "add <Description> on <Start Date> #<Tag>";
    private final String ADD_DESCRIPTION_ON_DATE_TIME = "add <Description> on <Start Date> <Start Time>";
    private final String ADD_DESCRIPTION_ON_DATE_TIME_TAG = "add <Description> on <Start Date> <Start Time> #<Tag>";
    private final String ADD_DESCRIPTION_BY_DATE = "add <Description> by <End Date>";
    private final String ADD_DESCRIPTION_BY_DATE_TAG = "add <Description> by <End Date> #<Tag>";
    private final String ADD_DESCRIPTION_BY_DATE_TIME = "add <Description> by <End Date> <End Time>";
    private final String ADD_DESCRIPTION_BY_DATE_TIME_TAG = "add <Description> by <End Date> <End Time> #<Tag>";
    private final String ADD_DESCRIPTION_FROM_DATE_TO_DATE = "add <Description> from <Start Date> to <End Date>";
    private final String ADD_DESCRIPTION_FROM_DATE_TO_DATE_TAG = "add <Description> from <Start Date> to <End Date> #<Tag>";
    private final String ADD_DESCRIPTION_FROM_DATE_TIME_TO_TIME = "add <Description> from <Start/End Date> "
                                                                  + "<Start Time> to <End Time>";
    private final String ADD_DESCRIPTION_FROM_DATE_TIME_TO_TIME_TAG = "add <Description> from <Start/End Date> "
                                                                      + "<Start Time> to <End Time> #<Tag>";
    private final String ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME = "add <Description> from "
                              + "<Start Date> <Start Time> to <End Date> <End Time>";
    private final String ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME_TAG = "add <Description> from "
                              + "<Start Date> <Start Time> to <End Date> <End Time> #<Tag>";
    
    private final String[] KEY_WORDS = {"on", "by", "from"};
    
    private final String[] promptInvalidTag = {"Invalid Tag Format: #<One Word>"};
    
    private final String[] promptSet1 = {ADD};
    private final String[] promptSet1s2 = {ADD_DESCRIPTION};
    private final String[] promptSet2s = {ADD_DESCRIPTION_TAG, ADD_DESCRIPTION_ON_DATE, ADD_DESCRIPTION_BY_DATE, 
                                          ADD_DESCRIPTION_FROM_DATE_TO_DATE};
    private final String[] promptSet2tag = {ADD_DESCRIPTION_TAG};
    
    private final String[] promptSet3o = {ADD_DESCRIPTION_ON_DATE};
    private final String[] promptSet3b = {ADD_DESCRIPTION_BY_DATE};
    private final String[] promptSet3f = {ADD_DESCRIPTION_FROM_DATE_TO_DATE};
    
    private final String[] promptSet4o = {ADD_DESCRIPTION_ON_DATE, ADD_DESCRIPTION_ON_DATE_TAG, ADD_DESCRIPTION_ON_DATE_TIME};
    private final String[] promptSet4b = {ADD_DESCRIPTION_BY_DATE, ADD_DESCRIPTION_BY_DATE_TAG, ADD_DESCRIPTION_BY_DATE_TIME};
    private final String[] promptSet4f = {ADD_DESCRIPTION_FROM_DATE_TO_DATE, ADD_DESCRIPTION_FROM_DATE_TIME_TO_TIME, 
                                          ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME};
    private final String[] promptSet4os = {ADD_DESCRIPTION_ON_DATE_TAG, ADD_DESCRIPTION_ON_DATE_TIME};
    private final String[] promptSet4bs = {ADD_DESCRIPTION_BY_DATE_TAG, ADD_DESCRIPTION_BY_DATE_TIME};
    private final String[] promptSet4fs = {ADD_DESCRIPTION_FROM_DATE_TO_DATE, ADD_DESCRIPTION_FROM_DATE_TIME_TO_TIME, 
                                           ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME};
    
    private final String[] promptSet4ft = {ADD_DESCRIPTION_FROM_DATE_TO_DATE, ADD_DESCRIPTION_FROM_DATE_TO_DATE_TAG};
    
    private final String[] promptSet5o = {ADD_DESCRIPTION_ON_DATE_TIME, ADD_DESCRIPTION_ON_DATE_TIME_TAG};
    private final String[] promptSet5b = {ADD_DESCRIPTION_BY_DATE_TIME, ADD_DESCRIPTION_BY_DATE_TIME_TAG};
    private final String[] promptSet5f = {ADD_DESCRIPTION_FROM_DATE_TIME_TO_TIME, ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME};
    private final String[] promptSet5os = {ADD_DESCRIPTION_ON_DATE_TIME_TAG};
    private final String[] promptSet5bs = {ADD_DESCRIPTION_BY_DATE_TIME_TAG};
    private final String[] promptSet5fs = {ADD_DESCRIPTION_FROM_DATE_TIME_TO_TIME, ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME};
    
    private final String[] promptSet5fnt = {ADD_DESCRIPTION_FROM_DATE_TO_DATE, ADD_DESCRIPTION_FROM_DATE_TO_DATE_TAG};
    private final String[] promptSet5fnts = {ADD_DESCRIPTION_FROM_DATE_TO_DATE_TAG};
 
    private final String[] promptSet6ft = {ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME, ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME_TAG};
    private final String[] promptSet6fts = {ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME, ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME_TAG};
    
    private final String[] promptSet7ft = {ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME, 
                                           ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME_TAG};
    private final String[] promptSet7fts = {ADD_DESCRIPTION_FROM_DATE_TIME_TO_DATE_TIME_TAG};
    
    private ArrayList<String> addPrompts;
    private KeyWordType foundKey;
    private boolean hasTag;
    
    public AddPrompt() {
        addPrompts = new ArrayList<String>();
        foundKey = KeyWordType.UNKNOWN;
        hasTag = false;
    }
    
    public ArrayList<String> getPrompts(String userInput, boolean hasSpace) {
        String[] splitInput = userInput.trim().split(STRING_MULTIPLE_WHITESPACE);
        String[] tags;
        
        if (findKeyWord(userInput).isEmpty()) {
            if (splitInput.length <= 1) {
                if (hasSpace) {
                    setPrompts(promptSet1s2, addPrompts);
                } else {
                    setPrompts(promptSet1, addPrompts);
                }
            } else {
                if (userInput.contains(" #")) {
                    int position = userInput.indexOf(" #");
                    tags = userInput.substring(position).trim().split(STRING_MULTIPLE_WHITESPACE);
                    boolean isValid = true;
                    for (int i = 0; i < tags.length; i++) {
                        System.out.println(tags[i]);
                        if(!tags[i].startsWith("#")) {
                            isValid = false;
                        }
                    }
                    if (userInput.contains(" # ")) {
                        isValid = false;
                    }
                    if (isValid) {
                        setPrompts(promptSet2tag, addPrompts);
                    } else {
                        setPrompts(promptInvalidTag, addPrompts);
                    }
                } else {
                    if (hasSpace) {
                        setPrompts(promptSet2s, addPrompts);
                    } else {
                        searchPartialKeyWord(splitInput);
                        promptPartialKeyWord();
                    }
                }
            }
        } else {
            String keyWord = findKeyWord(userInput);
            int position = userInput.lastIndexOf(keyWord) + keyWord.length();
            String dateTime = userInput.substring(position);
            position = findTag(dateTime);
            if (position < 0) {
                position = dateTime.length();
            }
            dateTime = dateTime.substring(0, position);
            System.out.println("DateTIme = " + dateTime);
            
            
            if (userInput.contains(" #")) {
                hasTag = true;
                position = userInput.indexOf(" #");
                tags = userInput.substring(position).trim().split(" ");
                boolean isValid = true;
                for (int i = 0; i < tags.length; i++) {
                    System.out.println(tags[i]);
                    if(!tags[i].startsWith("#")) {
                        isValid = false;
                    }
                }
                if (!isValid) {
                    setPrompts(promptInvalidTag, addPrompts);
                }
            }
            if (hasSpace) {
                foundKey = determineKeyWordType(findKeyWord(userInput));
                    
                if (dateTime.trim().isEmpty()) {                   
                    promptPartialKeyWord();
                } else {
                    catchDateAfterKeyWord(dateTime);
                }
            } else {      
                searchPartialKeyWord(splitInput);         
                if (foundKey == KeyWordType.UNKNOWN) {
                    foundKey = determineKeyWordType(findKeyWord(userInput));
                    catchDateAfterKeyWord(dateTime);
                } else {
                    promptPartialKeyWord();
                }
            }
        }
        return addPrompts;
    }
    
    private void promptCompleteKeyWordWithDate(boolean hasSpace) {
        if (hasSpace) {
            switch (foundKey) {
            case ON :
                setPrompts(promptSet4os, addPrompts);
                break;
            case BY :
                setPrompts(promptSet4bs, addPrompts);
                break;
            case FROM :
                setPrompts(promptSet4fs, addPrompts);
                break;
            case UNKNOWN :
                String[] test = {"???"};
                setPrompts(test, addPrompts);
                break;
            }
        } else {
            switch (foundKey) {
            case ON :
                setPrompts(promptSet4o, addPrompts);
                break;
            case BY :
                setPrompts(promptSet4b, addPrompts);
                break;
            case FROM :
                setPrompts(promptSet4f, addPrompts);
                break;
            case UNKNOWN :
                String[] test = {"???"};
                setPrompts(test, addPrompts);
                break;
            }
        }
    }
    
    private void promptCompleteKeyWordWithTime(boolean hasSpace) {
        if (hasSpace) {
            switch (foundKey) {
            case ON :
                setPrompts(promptSet5os, addPrompts);
                break;
            case BY :
                setPrompts(promptSet5bs, addPrompts);
                break;
            case FROM :
                setPrompts(promptSet5fs, addPrompts);
                break;
            case UNKNOWN :
                String[] test = {"???"};
                setPrompts(test, addPrompts);
                break;
            }
        } else {
            switch (foundKey) {
            case ON :
                setPrompts(promptSet5o, addPrompts);
                break;
            case BY :
                setPrompts(promptSet5b, addPrompts);
                break;
            case FROM :
                setPrompts(promptSet5f, addPrompts);
                break;
            case UNKNOWN :
                String[] test = {"???"};
                setPrompts(test, addPrompts);
                break;
            }
        }
    }

    private void promptPartialKeyWord() {
        switch (foundKey) {
        case ON :
            setPrompts(promptSet3o, addPrompts);
            break;
        case BY :
            setPrompts(promptSet3b, addPrompts);
            break;
        case FROM :
            setPrompts(promptSet3f, addPrompts);
            break;
        case UNKNOWN :
            System.out.println("partialKeyWord UKNOWN");
            setPrompts(promptSet1s2, addPrompts);
        }
    }

    private void catchDateAfterKeyWord(String dateTime) {
        boolean hasSpace = false;
        if (dateTime.charAt(dateTime.length() - 1) == ' ') {
            hasSpace = true;
        }
        dateTime = dateTime.trim();
        String startDate;
        String endDate;
        
        try {
            Parser parser = new Parser();
            List<DateGroup> groups = parser.parse(dateTime);
            System.out.println("num of date: " + groups.get(0).getDates().size());
            System.out.println("position of date: " + groups.get(0).getPosition());
            System.out.println("Date: " + groups.get(0).getDates().get(0));
            
            if (groups.get(0).getDates().size() == 1) {
                if (groups.get(0).getText().equals(dateTime)) {
                    if (!groups.get(0).isDateInferred()) {
                        if (!groups.get(0).isTimeInferred()) {
                            promptCompleteKeyWordWithTime(hasSpace);
                        } else {
                            promptCompleteKeyWordWithDate(hasSpace);
                        }
                    } else {
                        promptPartialKeyWord();
                    }
                } else {
                    if (foundKey == KeyWordType.FROM) {
                        startDate = dateTime.substring(groups.get(0).getText().length()).trim();
                        if ("to".startsWith(startDate)) {
                            if (!groups.get(0).isTimeInferred()) {
                                if (hasSpace) {
                                    setPrompts(promptSet5fs, addPrompts);
                                } else {
                                    setPrompts(promptSet5f, addPrompts);
                                }
                            } else {
                                setPrompts(promptSet4ft, addPrompts);
                            }
                        } else {
                            throw new Exception();
                        }
                    } else {
                        throw new Exception();
                    }
                }
            } else {
                if (foundKey == KeyWordType.FROM) {
                    int position = dateTime.lastIndexOf(" to ");
                    if (position > 0) {
                        startDate = dateTime.substring(0, position).trim();
                        endDate = dateTime.substring(position + 4).trim();
                        
                        groups = parser.parse(startDate);                      
                        if (groups.get(0).getText().equals(startDate)) {
                            if (!groups.get(0).isTimeInferred()) {
                                //Got Start Date got TIme
                                groups = parser.parse(endDate); 
                                if (groups.get(0).getText().equals(endDate)) {
                                  //got start date time and end date/time
                                    if (!groups.get(0).isDateInferred()) {
                                        //got start date time and end date
                                        if (!groups.get(0).isTimeInferred()) {
                                            //got start date time and end date time
                                            if (hasSpace) {
                                                setPrompts(promptSet7fts, addPrompts);
                                            } else {
                                                setPrompts(promptSet7ft, addPrompts);
                                            }
                                        } else {
                                            //got start date time and end date
                                            if (hasSpace) {
                                                setPrompts(promptSet6ft, addPrompts);
                                            } else {
                                                setPrompts(promptSet6fts, addPrompts);
                                            }
                                        }
                                    } else {
                                        //got start date time and no end date but got time
                                        if (hasSpace) {
                                            setPrompts(promptSet6ft, addPrompts);
                                        } else {
                                            setPrompts(promptSet6fts, addPrompts);
                                        }
                                    }
                                } else {
                                    throw new Exception();
                                }
                            } else {
                                //Got Start Date no TIme
                                groups = parser.parse(endDate); 
                                if (groups.get(0).getText().equals(endDate)) {
                                    //got start date time and end date/time
                                    if (!groups.get(0).isDateInferred()) {
                                        //got start date and end date
                                        if (!groups.get(0).isTimeInferred()) {
                                            //got start date and end date time
                                            throw new Exception();
                                        } else {
                                            //got start date and end date
                                            if (hasSpace) {
                                                setPrompts(promptSet5fnts, addPrompts);
                                            } else {
                                                setPrompts(promptSet5fnt, addPrompts);
                                            }
                                        }
                                    } else {
                                        //got start date and no end date but got time
                                        throw new Exception();
                                    }
                                } else {
                                    throw new Exception();
                                }
                            }
                        } else {
                            throw new Exception();
                        }
                    } else {
                        throw new Exception();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            if (hasSpace) {
                setPrompts(promptSet2s, addPrompts);
            } else {
                setPrompts(promptSet1s2, addPrompts);
            }
        }
    }
    
    private void searchPartialKeyWord(String[] splitInput) {
        if (splitInput.length > 2) {
            for (int i = 0; i < KEY_WORDS.length; i++) {
                if (KEY_WORDS[i].startsWith(splitInput[splitInput.length - 1])) {
                    foundKey = determineKeyWordType(KEY_WORDS[i]);
                }
            }
        }
    }
    
    private void setPrompts(String[] sourceSet, ArrayList<String> destList) {
        for (int i = 0; i < sourceSet.length; i++) {
            destList.add(sourceSet[i]);
        }
    }
    
    private String findKeyWord(String userInput) {
        int position = 0;
        String keyWord = "";
        if (userInput.contains(new String(" " + KEY_WORDS[0] + " ")) && userInput.lastIndexOf(KEY_WORDS[0]) > position) {
            position = userInput.lastIndexOf(KEY_WORDS[0]);
            keyWord = KEY_WORDS[0];
        }
        if (userInput.contains(new String(" " + KEY_WORDS[1] + " ")) && userInput.lastIndexOf(KEY_WORDS[1]) > position) {
            position = userInput.lastIndexOf(KEY_WORDS[1]);
            keyWord = KEY_WORDS[1];
        }
        if (userInput.contains(new String(" " + KEY_WORDS[2] + " ")) && userInput.lastIndexOf(KEY_WORDS[2]) > position) {
            position = userInput.lastIndexOf(KEY_WORDS[2]);
            keyWord = KEY_WORDS[2];
        }
        System.out.println("Keyword = " + keyWord);
        return keyWord;
    }
    
    private int findTag(String userInput) {
        return userInput.indexOf('#');
    }
    
    private KeyWordType determineKeyWordType(String commandTypeString) {
        if (commandTypeString.isEmpty()) {
            return KeyWordType.UNKNOWN;
        }

        if (commandTypeString.equalsIgnoreCase(KEY_WORDS[0])) {
            return KeyWordType.ON;
        } else if (commandTypeString.equalsIgnoreCase(KEY_WORDS[1])) {
            return KeyWordType.BY;
        } else if (commandTypeString.equalsIgnoreCase(KEY_WORDS[2])) {
            return KeyWordType.FROM;
        } else {
            return KeyWordType.UNKNOWN;
        }
    }
}
