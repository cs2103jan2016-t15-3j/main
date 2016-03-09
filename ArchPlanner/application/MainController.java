package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import application.TaskPane;
import feedback.Feedback;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import logic.Logic;
import logic.Task;
import logic.commands.Command;

public class MainController implements Initializable{
    
    @FXML
    private Label viewLabel;
    private Label promptLabel;

    @FXML
    private Button miniButton;
    @FXML
    private Button closeButton;
    
    @FXML
    private GridPane taskWindow;
    @FXML
    private StackPane mainWindow;
    
    @FXML
    private TextField userInput; 
    
    @FXML
    public ListView<ToggleButton> tagDisplay;
    @FXML
    public ListView<GridPane> taskDisplay;
 
    private ArrayList<String> tags = new ArrayList<String>();
    private ArrayList<Task> tasks = new ArrayList<Task>();
    
    private ObservableList<ToggleButton> tagList = FXCollections.observableArrayList();
    private ObservableList<GridPane> taskList = FXCollections.observableArrayList();
    
    private double xPos, yPos;
    private Rectangle2D screenBound;
    
    Logic logic;
    Feedback feedback;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 1999);
        date.set(Calendar.MONTH, 7);
        date.set(Calendar.DAY_OF_MONTH, 26);
        
        tasks.add(new Task("Task 1", "#Events", date, date));
        tasks.add(new Task("Task 2", null, date, null));
        tasks.add(new Task("Task 3", "#Deadline", null, date));
        tasks.add(new Task("Task 4", "#Task", null, null));

        tags.add("#Events");
        tags.add("#Deadlines");
        tags.add("#Tasks");
        tags.add("#Events");
        tags.add("#Deadlinesewqewqewq");
        tags.add("#Events");
        tags.add("#Deadlines");
        tags.add("#Tasks");
        tags.add("#Events");
        tags.add("#Deadlinesewqewqewq");
        tags.add("#Events");
        tags.add("#Deadlines");
        tags.add("#Tasks");
        tags.add("#Events");
        tags.add("#Deadlinesewqewqewq");
        
        //--------------------------------------------------------------------
        
        userInput.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onTextChanged(newValue);
            }
        });
        
        promptLabel = new Label();
        String promptSytle = "-fx-background-color: rgba(0, 0, 0, 0.8);" + "-fx-background-radius: 20;" + 
                             "-fx-text-fill: white;" + "-fx-padding: 0 25 0 25;" + "-fx-font: 24px \"System\";";
        promptLabel.setStyle(promptSytle);
        promptLabel.setMinSize(1000, 70);
        
        screenBound = Screen.getPrimary().getVisualBounds();
        
        //-------------------------------------------------------------------
        
        logic = new Logic();
        feedback = new Feedback();
        
        logic.loadFile();
        
        //tags = logic.getTagsList();
        //tasks = logic.getViewList();
                
        tagDisplay.setItems(tagList);
        taskDisplay.setItems(taskList);
        
        updateTaskDisplay();
        updateTagDisplay();
    }
    
    @FXML
    private void onWindowPressed(MouseEvent event) {
        xPos = taskWindow.getScene().getWindow().getX() - event.getScreenX();
        yPos = taskWindow.getScene().getWindow().getY() - event.getScreenY();
    }
    
    @FXML
    private void onWindowDragged(MouseEvent event) {
        taskWindow.getScene().getWindow().setX(event.getScreenX() + xPos);
        taskWindow.getScene().getWindow().setY(event.getScreenY() + yPos);
    }
    
    @FXML
    private void minimize(ActionEvent event) {
        ((Stage) miniButton.getScene().getWindow()).setIconified(true);
    }
    
    @FXML
    private void close(ActionEvent event) {
        ((Stage) miniButton.getScene().getWindow()).close();
    }
    
    @FXML
    private void onEnterPressed() {
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
    
    public void onTextChanged(String newString) {
        if (newString.isEmpty()) {
            mainWindow.getChildren().remove(promptLabel);
        } else {
            if (!mainWindow.getChildren().contains(promptLabel)){
                if (mainWindow.localToScreen(mainWindow.getBoundsInLocal()).getMaxY() > screenBound.getMaxY()) {
                    promptLabel.setTranslateY((mainWindow.getHeight())/2 - 200);
                } else {
                    promptLabel.setTranslateY((mainWindow.getHeight())/2 - 35);
                }
                mainWindow.getChildren().add(promptLabel);               
            }
            
            ArrayList<String> prompt = feedback.onTextChanged(newString);
            if (!prompt.isEmpty()) {
                promptLabel.setText(prompt.get(0));
            }
        }
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    private void updateTaskDisplay() {       
        taskList.clear();
        
        for (int i = 0; i < tasks.size(); i++) {            
            TaskPane displayTask = new TaskPane(i + 1, tasks.get(i), taskDisplay.widthProperty());
            taskList.add(displayTask);
        }
    }
    
    private void updateTagDisplay() {
        tagList.clear();
        
        for (int i = 0; i < tags.size(); i++) {
            ToggleButton newButton = new ToggleButton(tags.get(i));
            newButton.setMaxWidth(Double.MAX_VALUE);
            newButton.prefWidthProperty().bind(tagDisplay.widthProperty().subtract(40));
            newButton.setAlignment(Pos.CENTER_LEFT);
            tagList.add(newButton);
        }
    }
}
