package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
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
    public ListView<ToggleButton> tagView;
    @FXML
    public ListView<GridPane> taskView;
 
    private ArrayList<String> tags = new ArrayList<String>();
    private ArrayList<Task> tasks = new ArrayList<Task>();
    
    private ObservableList<ToggleButton> tagList = FXCollections.observableArrayList();
    private ObservableList<GridPane> taskList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        //------------------------------------------------------------------
        tasks.add(new Task("Task 1", "16/03/16", "", "17/03/16", ""));
        tasks.add(new Task("Task 2", "23/03/16", "09:00AM", "", ""));
        tasks.add(new Task("Task 3", "", "", "30/03/16", "5:00PM"));
        tasks.add(new Task("Task 4", "", "", "", ""));

        tags.add("#Events");
        tags.add("#Deadlines");
        tags.add("#Tasks");
        //-------------------------------------------------------------------
        
        //-------------------------------------------------------------------
        
        userInput.textProperty().addListener(new myChangeListener());
        
        //--------------------------------------------------------------------
        tagView.setItems(tagList);
        taskView.setItems(taskList);
        
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
    
    public void sendChangeInText(String newValue) {
        String prompt = Feedback.getPrompt(newValue);
        promptLabel.setText(prompt);
    }
    
    public void sendCmd(ActionEvent enter) {
        System.out.println(userInput.getText());
        userInput.clear();
        Feedback.addTasks();
    }
}

class myChangeListener implements ChangeListener<String> {

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        // TODO Auto-generated method stub
        Ui.getInstance().sendChangeInText(newValue);
    }
}
