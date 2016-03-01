package application;

import java.util.ArrayList;

public class TestClass {
    
    private static ArrayList<Task> tasks = new ArrayList<Task>();
    private static ArrayList<String> tags = new ArrayList<String>();
    
    public static void add() {
        tasks.add(new Task("Task 5frefrefrefefrefefefefrefefeffrefrefreferfrefefefeferferferferferferfefreferfefefrefer", "28/03/16", "", "29/03/16", ""));
        tasks.add(new Task("Task 6", "12/03/16bhgbtgrbtrbgtrbhtrbtrbtrbhtrbhtbtbgt", "10:00AM", "", ""));
        tasks.add(new Task("Task 7", "", "", "15/03/16", "9:00PM"));
        tasks.add(new Task("Task 8", "", "", "", ""));
        
        Ui.getInstance().updateTaskDisplay(tasks);
    }
    
    public static void addCategory() {
        tags.add("#myCategory123456789dsfsdfsfsdfsdf");
        tags.add("#myCategory2");
        tags.add("#myCategory3");
        
        Ui.getInstance().updateTagDisplay(tags);
    }
}
