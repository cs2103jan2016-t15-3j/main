package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;

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
import javafx.stage.WindowEvent;
import logic.Logic;
import logic.Task;
import logic.commands.Command;

public class MainController implements Initializable{
    
    static Logger log = Logger.getLogger(MainController.class.getName());
    
    @FXML private Label viewLabel;
    @FXML private Label topPrompt;
    @FXML private Label bottomPrompt;

    @FXML private Button miniButton;
    @FXML private Button closeButton;
    
    @FXML private GridPane taskWindow;
    @FXML private StackPane mainWindow;
    
    @FXML private TextField userInput; 
    
    @FXML public ListView<ToggleButton> tagDisplay;
    @FXML public ListView<GridPane> taskDisplay;
 
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
        testMethod();      
        //--------------------------------------------------------------------          
        feedback = new Feedback();
        logic = new Logic();
        
        logic.loadFile();
        
        assert(logic.getTagsList() != null);
        assert(logic.getTagsList() != null);
        assert(logic.getCurrentView() != null);
        
        //tags = logic.getTagsList();
        //tasks = logic.getViewList();
        //viewLabel.setText(logic.getCurrentView());
        updateTaskDisplay();
        updateTagDisplay();
 
        //-------------------------------------------------------------------         
        userInput.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onTextChanged(newValue);
            }
        });
        
        topPrompt.setVisible(false);
        bottomPrompt.setVisible(false);
        
        screenBound = Screen.getPrimary().getVisualBounds();
        int width = (int) screenBound.getMaxX() * 3 / 5;
        int height = (int) screenBound.getMaxY() * 4 / 5;
        mainWindow.setPrefSize(width, height);
        
        tagDisplay.setItems(tagList);
        taskDisplay.setItems(taskList);
        
        log.info("MainController initialzed");
    }
    
    //------------------------------------------------------------------------------------------------------------
    private void testMethod() {
        ArrayList<String> tag = new ArrayList<String>();
        tag.add("#tag1");
        tag.add("#tag2");
        tag.add("#tag3");
        tag.add("#tag4");
        tag.add("#tag5");
        tag.add("#tag6");
        tag.add("#tag7");
        
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 1999);
        date.set(Calendar.MONTH, 7);
        date.set(Calendar.DAY_OF_MONTH, 26);
        
        Calendar date2 = Calendar.getInstance();
        date2.set(Calendar.YEAR, 2000);
        date2.set(Calendar.MONTH, 8);
        date2.set(Calendar.DAY_OF_MONTH, 8);
        date2.set(Calendar.HOUR, 00);
        date2.set(Calendar.MINUTE, 00);
  
        tasks.add(new Task("Task 1 ggregegegregregregrgregregregregregregreg", tag, date, date));
        tasks.add(new Task("Task 2", tag, date, null));
        tasks.add(new Task("Task 3", tag, null, date));
        tasks.add(new Task("Task 1 ggregegegregregregrgregregregregregregreg", tag, date2, date2));
        tasks.add(new Task("Task 2", tag, date2, null));
        tasks.add(new Task("Task 3", tag, null, date2));
        tasks.add(new Task("Task 4", tag, null, null));
        tasks.add(new Task("Task 4", tag, null, null));

        tags.add("#tag111111111111111111111111111");
        tags.add("#tag2");
        tags.add("#tag3");
        tags.add("#tag4");
        tags.add("#tag5");
        tags.add("#tag6");
        tags.add("#tag7");
    }
    //------------------------------------------------------------------------------------------------------------
    
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
        log.info("minimise button clicked");
        ((Stage) miniButton.getScene().getWindow()).setIconified(true);
    }
    
    @FXML
    private void close(ActionEvent event) {
        log.info("closed button clicked");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    @FXML
    private void onEnterPressed() {
        log.info("command entered");
        Command userCmd = feedback.onEnterPressed(userInput.getText());
        assert(userCmd != null);
        boolean isSuccessful = logic.executeCommand(userCmd);
        
        if (isSuccessful) {
            assert(logic.getTagsList() != null);
            assert(logic.getTagsList() != null);
            assert(logic.getCurrentView() != null);
            
            tasks = logic.getViewList();
            tags = logic.getTagsList();
            //viewLabel.setText(logic.getCurrentView());
            updateTaskDisplay();
            updateTagDisplay();
            
            userInput.clear();
            log.info("cmd executed");
        } else {
            log.info("incorrect command entered");
        }
    }
    
    public void onTextChanged(String newString) {
        if (newString.isEmpty()) {          
            topPrompt.setVisible(false);
            bottomPrompt.setVisible(false);
            log.info("hide prompt");
        } else {            
            ArrayList<String> prompt = feedback.onTextChanged(newString);
            assert(prompt != null);
            assert(prompt.size() > 0 && prompt.size() <= 4);
            if (!prompt.isEmpty()) {
                topPrompt.setText(prompt.get(0));
                bottomPrompt.setText(prompt.get(0));
                for (int i  = 1; i < prompt.size(); i++) {
                    topPrompt.setText(prompt.get(i) + "\n" + topPrompt.getText());
                    bottomPrompt.setText(bottomPrompt.getText() + "\n" + prompt.get(i));
                }
            } else {
                log.warning("empty prompt");
            }
            
            if (mainWindow.localToScreen(mainWindow.getBoundsInLocal()).getMaxY() - 27.5 * (4 - prompt.size())
                > screenBound.getMaxY()) {
                topPrompt.setVisible(true);
                bottomPrompt.setVisible(false);
            } else {
                topPrompt.setVisible(false);
                bottomPrompt.setVisible(true);
            }
            log.info("display prompt");
        }
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    private void updateTaskDisplay() {       
        taskList.clear();
        
        for (int i = 0; i < tasks.size(); i++) {        
            TaskPane displayTask = new TaskPane(i + 1, tasks.get(i), taskDisplay.widthProperty());
            taskList.add(displayTask);
        }  
        log.info("Tasks Refreshed");
    }
    
    private void updateTagDisplay() {
        tagList.clear();
        
        for (int i = 0; i < tags.size(); i++) {
            ToggleButton tagButton = new ToggleButton(tags.get(i));
            tagButton.setMaxWidth(Double.MAX_VALUE);
            tagButton.prefWidthProperty().bind(tagDisplay.widthProperty().subtract(40));
            tagButton.setAlignment(Pos.CENTER_LEFT);
            tagList.add(tagButton);
        }     
        log.info("Tags Refreshed");
    }
}
