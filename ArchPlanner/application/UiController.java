package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import feedback.Feedback;
import logic.Logic;
import logic.Task;
import logic.commands.Command;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

public class UiController implements Initializable{
    @FXML
    public Label tagLabel;
    @FXML
    public Label viewLabel;
    @FXML
    public Label promptLabel;
    @FXML
    public Label numLabel;
    @FXML
    public Label desLabel;
    @FXML
    public Label startLabel;
    @FXML
    public Label endLabel;
    
    @FXML
    public TextField userInput;  
    
    @FXML
    public ListView<ToggleButton> tagDisplay;
    @FXML
    public ListView<GridPane> taskDisplay;
 
    private ArrayList<String> tags = new ArrayList<String>();
    private ArrayList<Task> tasks = new ArrayList<Task>();
    
    private ObservableList<ToggleButton> tagList = FXCollections.observableArrayList();
    private ObservableList<GridPane> taskList = FXCollections.observableArrayList();
    
    Logic logic;
    Feedback feedback;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        /*------------------------------------------------------------------
        tasks.add(new Task("Task 1", null, new Date(2016, 3, 3, 0, 0), new Date(2016, 3, 3, 0, 0)));
        tasks.add(new Task("Task 2", null, new Date(2016, 3, 23, 9, 0), null));
        tasks.add(new Task("Task 3", null, null, new Date(2016, 3, 23, 17, 0)));
        tasks.add(new Task("Task 4", null, null, null));

        tags.add("#Events");
        tags.add("#Deadlines");
        tags.add("#Tasks");
        //-------------------------------------------------------------------*/
        
        //-------------------------------------------------------------------
        
        
        
        //--------------------------------------------------------------------
        userInput.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onTextChanged(newValue);
            }
        });
        
        logic = new Logic();
        feedback = new Feedback();
        
        logic.loadFile();
        
        tags = logic.getTagsList();
        tasks = logic.getViewList();
                
        tagDisplay.setItems(tagList);
        taskDisplay.setItems(taskList);
        
        updateTaskDisplay();
        updateTagDisplay();
    }
    
    public void updateTaskDisplay(ArrayList<Task> displayList) {
        tasks = displayList;
        updateTaskDisplay();
    }
    
    public void updateTagDisplay(ArrayList<String> displayList) {
        tags = displayList;
        updateTagDisplay();
    }
    
    private void updateTaskDisplay() {       
        taskList.clear();
        
        for (int i = 0; i < tasks.size(); i++) {            
            TaskPane displayTask = new TaskPane();
            displayTask.setColumnConstraints(numLabel.widthProperty(), desLabel.widthProperty(), 
                                             startLabel.widthProperty(), endLabel.widthProperty());
            displayTask.setTaskInfo(tasks.get(i));
            displayTask.setNumber(i + 1);
            taskList.add(displayTask);
        }
    }
    
    private void updateTagDisplay() {
        tagList.clear();
        
        for (int i = 0; i < tags.size(); i++) {
            ToggleButton newButton = new ToggleButton(tags.get(i));
            newButton.prefWidthProperty().bind(tagLabel.widthProperty().subtract(40));
            newButton.setAlignment(Pos.CENTER_LEFT);
            tagList.add(newButton);
        }
    }
    
    public void onTextChanged(String newString) {
        ArrayList<String> prompt = feedback.onTextChanged(newString);
        if (!prompt.isEmpty()) {
            promptLabel.setText(prompt.get(0));
        }
    }
    
    public void onEnterPressed() {
        Command userCmd = feedback.onEnterPressed(userInput.getText());
        boolean isSuccessful = logic.executeCommand(userCmd);
        
        if (isSuccessful) {            
            tasks = logic.getViewList();
            tags = logic.getTagsList();
            
            updateTaskDisplay();
            updateTagDisplay();
            
            userInput.clear();
            promptLabel.setText("");
        } else {
            //error
        }
    }
}
