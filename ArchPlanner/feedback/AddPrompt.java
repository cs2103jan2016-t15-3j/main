package feedback;

import java.util.ArrayList;
import java.util.Arrays;

import feedback.InputParameters.KeyWordType;

public class AddPrompt {
    
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
    
    public AddPrompt() {
        addPrompts = new ArrayList<String>();
    }
    
    public ArrayList<String> getPrompts(String userInput) {
        
        InputParameters parameter = new InputParameters(userInput);
        boolean[] parameterSet = {parameter.hasDescription(), parameter.hasKeyWord(), parameter.hasPartialKeyWord(),
                                  parameter.hasStartDate(), parameter.hasStartTime(), parameter.hasEndDate(), 
                                  parameter.hasEndTime(), parameter.hasTag(), parameter.hasValidTag(), parameter.hasSpace()};
        int intParameterSet = convertToInt(parameterSet);
        System.out.println(Arrays.toString(parameterSet));
        
        switch (intParameterSet) {
            case 0 :
                setPrompts(promptSet1, addPrompts);
                break;
            case 518 :
                // Fallthrough
            case 519 : 
                setPrompts(promptSet2tag, addPrompts);
                break;
            case 516 :
                // Fallthrough
            case 517 :
                // Fallthrough
            case 644 :
                setPrompts(promptInvalidTag, addPrompts);
                break;
            case 640 :
                // Fallthrough
            case 768 :
                // Fallthrough
            case 896 :
                setPrompts(getKeyWordPrompts(parameter.getPartialKeyWord()), addPrompts);
                break;
            case 513 :
                // Fallthrough
            case 641 :
                setPrompts(promptSet2s, addPrompts);
                break;
            case 769 :
                setPrompts(getKeyWordPrompts(parameter.getKeyWord()), addPrompts);
                break;
            case 832 :
                setPrompts(getKeyWordPromptsWithDate(parameter.getKeyWord()), addPrompts);
                break;
            case 833 :
                setPrompts(getKeyWordPromptsWithDateSpaced(parameter.getKeyWord()), addPrompts);
                break;
            case 848 :
                setPrompts(promptSet5fnt, addPrompts);
                break;
            case 849 :
                setPrompts(promptSet5fnts, addPrompts);
                break;
            case 864 :
                setPrompts(getKeyWordPromptsWithTime(parameter.getKeyWord()), addPrompts);
                break;
            case 865 :
                setPrompts(getKeyWordPromptsWithTimeSpaced(parameter.getKeyWord()), addPrompts);
                break;
            case 880 :
                setPrompts(promptSet6ft, addPrompts);
                break;
            case 881 :
                setPrompts(promptSet6fts, addPrompts);
                break;
            case 888 :
                setPrompts(promptSet7ft, addPrompts);
                break;
            case 889 :
                setPrompts(promptSet7fts, addPrompts);
                break;
            default :
                setPrompts(promptSet1s2, addPrompts);
                break;
                
        }
        return addPrompts;
    }
    
    private String[] getKeyWordPrompts(KeyWordType keyWord) {
        switch (keyWord) {
        case ON :
            return promptSet3o;
        case BY :
            return promptSet3b;
        case FROM :
            return promptSet3f;
        default :
            return promptSet1s2;
        }
    }
    
    private String[] getKeyWordPromptsWithDate(KeyWordType keyWord) {
        switch (keyWord) {
        case ON :
            return promptSet4o;
        case BY :
            return promptSet4b;
        case FROM :
            return promptSet4f;
        default :
            return promptSet1s2;
        }
    }
    
    private String[] getKeyWordPromptsWithDateSpaced(KeyWordType keyWord) {
        switch (keyWord) {
        case ON :
            return promptSet4os;
        case BY :
            return promptSet4bs;
        case FROM :
            return promptSet4fs;
        default :
            return promptSet1s2;
        }
    }
    
    private String[] getKeyWordPromptsWithTime(KeyWordType keyWord) {
        switch (keyWord) {
        case ON :
            return promptSet5o;
        case BY :
            return promptSet5b;
        case FROM :
            return promptSet5f;
        default :
            return promptSet1s2;
        }
    }
    
    private String[] getKeyWordPromptsWithTimeSpaced(KeyWordType keyWord) {
        switch (keyWord) {
        case ON :
            return promptSet5os;
        case BY :
            return promptSet5bs;
        case FROM :
            return promptSet5fs;
        default :
            return promptSet1s2;
        }
    }
    
    private int convertToInt(boolean[] booleanSet) {
        int bin = 0;
        for (int i = 0; i < booleanSet.length; i++) {
            int value = (booleanSet[i] ? 1 : 0) << booleanSet.length - 1 - i;
            bin = bin | value;
        }
        System.out.println("Bin: " + bin);
        return bin;
    }
    
    private void setPrompts(String[] sourceSet, ArrayList<String> destList) {
        for (int i = 0; i < sourceSet.length; i++) {
            destList.add(sourceSet[i]);
        }
    }
}
