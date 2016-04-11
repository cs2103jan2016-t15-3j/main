package interpreter;

import java.util.ArrayList;

import interpreter.parser.Parser;
import interpreter.prompt.Prompt;
import logic.Tag;
import logic.commands.CommandInterface;

/**
 * This class is a facade class for the other component to interface with the Prompt and Parser class.
 * 
 * @@author A0140034B
 */
public class Interpreter {
    
    private Prompt prompt;
    private Parser parser;
    
    /** initialize the Prompt and Parser object */
    public Interpreter() {
        prompt = new Prompt();
        parser = new Parser();
    }
    
    /**
     * Interpret the string and return the appropriate prompts to UI to display to the user.
     * 
     * @param userInput the string that represent the command.
     * @return          the list of prompts to display to user.
     */
    public ArrayList<String> getPrompts(String userInput) {
        return prompt.getPrompts(userInput);
    }
    
    /**
     * Interpret the string and return the appropriate string with the auto-complete keyword.
     * There is no keyword to auto-complete if the last character is a space.
     * Return the same string if there is no keyword to auto-complete.
     * 
     * @param userInput the string that represent the command.
     * @return          the string after auto-completing the keyword.
     */
    public String getAutoComplete(String userInput) {
        return prompt.getAutoComplete(userInput);
    }
    
    /**
     * This method is used to parse user input into command object.
     *
     * @param userInput     input The user's input.
     * @param viewListSize  the size of the list that the user is viewing to use for validating.
     * @param undoListSize  the size of the undo list to use for validating if there is nothing to undo.
     * @param redoListSize  the size of the redo list to use for validating if there is nothing to redo.
     * @param tagList       the list of tag for validating if a given tag exist.
     * @return              parsed command. If input is invalid, will return InvalidCommand
     */
    public CommandInterface parseCommand(String userInput, int viewListSize, int undoListSize, int redoListSize, ArrayList<Tag> tagList) {
        return parser.parseCommand(userInput, viewListSize, undoListSize, redoListSize, tagList);
    }
}
