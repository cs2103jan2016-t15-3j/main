package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import application.TaskPane;
import feedback.Feedback;
import feedback.Feedback;
import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Logic;
import logic.Tag;
import logic.Task;

public class MainController implements Initializable{
    
    static Logger log = Logger.getLogger(MainController.class.getName());
    
    @FXML private Label viewLabel;
    @FXML private Label topPrompt;
    @FXML private Label bottomPrompt;
    @FXML private Label feedbackLabel;
    
    @FXML private ImageView feedbackIcon;

    @FXML private Button miniButton;
    @FXML private Button closeButton;

    @FXML private ToggleButton event;
    @FXML private ToggleButton deadline;
    @FXML private ToggleButton task;
    
    @FXML private StackPane mainWindow;
    @FXML private GridPane taskWindow;
    @FXML private GridPane feedbackWindow;   
    
    @FXML private TextField userInput;
    
    @FXML public ListView<ToggleButton> tagDisplay;
    @FXML public ListView<GridPane> taskDisplay;
 
    private ArrayList<Tag> tags = new ArrayList<Tag>();;
    private ArrayList<Task> tasks = new ArrayList<Task>();
    
    private String categorySelected;
    
    private ObservableList<ToggleButton> tagList = FXCollections.observableArrayList();
    private ObservableList<GridPane> taskList = FXCollections.observableArrayList();
    
    private double xPos, yPos;
    private Rectangle2D screenBound;
    
    private Image successIcon;
    private Image failIcon;
    
    Logic logic;
    Feedback feedback;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {        
        //testMethod();      
        //--------------------------------------------------------------------          
        feedback = new Feedback();
        logic = new Logic();
        
        logic.loadFile();
        
        assert(logic.getTagsList() != null);
        assert(logic.getTagsList() != null);
        assert(logic.getCurrentViewType() != null);
        
        updateUi();
 
        //-------------------------------------------------------------------         
        userInput.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onTextChanged(newValue);
            }
        });
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userInput.requestFocus();
            }
        });
        
        successIcon = new Image("/images/SuccessIcon.png");
        failIcon = new Image("/images/FailIcon.png");
        
        topPrompt.setVisible(false);
        bottomPrompt.setVisible(false);
        feedbackWindow.setVisible(false);
        
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
        ArrayList<String> tag1 = new ArrayList<String>();
        tag1.add("#tag1");
        tag1.add("#tag2");
        
        ArrayList<String> tag2 = new ArrayList<String>();
        tag2.add("#tag3");
        
        ArrayList<String> tag3 = new ArrayList<String>();
        tag3.add("#tag2");
        
        ArrayList<String> tag4 = new ArrayList<String>();
        tag4.add("#tag4");
        tag4.add("#tag5");
        
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
    private void onCategoryPressed(ActionEvent event) {
        ToggleButton clicked = (ToggleButton)event.getSource();
        if (clicked.isSelected()) {
            logic.setSelectedCategory(clicked.getText());
        } else {
            logic.setSelectedCategory("All");
        }
        
        updateUi();
    }
    
    
    @FXML
    private void onEnterPressed() {
        log.info("command entered");
        boolean isSuccessful = logic.executeCommand(userInput.getText());
        
        if (isSuccessful) {
            assert(logic.getTagsList() != null);
            assert(logic.getTagsList() != null);
            assert(logic.getCurrentViewType() != null);
            
            updateUi();
            
            setFeedbackWindow(true, userInput.getText());
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
            String autoComplete = feedback.getAutoComplete(userInput.getText());
            userInput.setText(autoComplete);
            userInput.positionCaret(userInput.getText().length() + 1);;
            
            ArrayList<String> prompt = feedback.getPrompts(newString);
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
    
    public void onSpacePressed(KeyEvent event) {
        /*
        if (event.getCode() == KeyCode.SPACE) {
            String autoComplete = feedback.getAutoComplete(userInput.getText());
            userInput.setText(autoComplete);
            userInput.positionCaret(userInput.getText().length() + 1);;
        }
        */
    }
    
    private void onTagPressed(ActionEvent event) {
        ToggleButton clicked = (ToggleButton)event.getSource();
        logic.setSelectedTag(clicked.getText());
        updateUi();
    }
    
    //------------------------------------------------------------------------------------------------------------
    private void setFeedbackWindow(boolean isSuccessful, String message) {
        if (isSuccessful) {
            feedbackIcon.setImage(successIcon);
            feedbackLabel.setText("Successfully executed command" + "\n" + message);
        } else {
            feedbackIcon.setImage(failIcon);
            feedbackLabel.setText("Failed to execute command" + "\n" + message);
        }
        feedbackWindow.setVisible(true);
    }
    
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
            ToggleButton tagButton = new ToggleButton(tags.get(i).getName());
            tagButton.setSelected(tags.get(i).getIsSelected());
            tagButton.setMaxWidth(Double.MAX_VALUE);
            tagButton.prefWidthProperty().bind(tagDisplay.widthProperty().subtract(40));
            tagButton.setAlignment(Pos.CENTER_LEFT);
            tagButton.setOnAction(tagObj -> {
                onTagPressed(tagObj);
            });
            tagList.add(tagButton);
        }     
        log.info("Tags Refreshed");
    }
    
    private void updateCategoryDisplay() {
    	switch (categorySelected) {
    		case "All" :
    			event.setSelected(false);
    			deadline.setSelected(false);
    			task.setSelected(false);
    			break;
    		case "Events" :
    			event.setSelected(true);
    			break;
    		case "Deadlines" :
    			deadline.setSelected(true);
    			break;
    		case "Tasks" :
    			task.setSelected(true);
    			break;
    	}
    }
    
    private void updateUi() {
    	tasks = logic.getViewList();
        tags = logic.getTagsList();
        categorySelected = logic.getSelectedCategory();
        viewLabel.setText(logic.getCurrentViewType());
        
        updateTaskDisplay();
        updateTagDisplay();
        updateCategoryDisplay();
    }
}
