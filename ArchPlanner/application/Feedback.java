package application;

import javafx.scene.input.KeyCode;

public class Feedback {
    private static KeyCode keystroke;
    private static String collection = "";
    
    public static String setKeystroke(KeyCode key) {
        keystroke = key;
        printKeystroke();
        if (key.isLetterKey()) {
            collection += (key.toString());
        } else if (key == KeyCode.ENTER) {
            TestClass.add();
        }else if (key == KeyCode.SHIFT) {
            TestClass.addCategory();
        }
        return collection;
    }
    
    public static String getPrompt(String userInput) {
        return userInput;
    }
    
    private static void printKeystroke() {
        System.out.println(keystroke);
    }
    
    public static void addTasks() {
        TestClass.add();
    }
}
