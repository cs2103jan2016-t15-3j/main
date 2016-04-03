package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import application.TaskPane;
import feedback.Feedback;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import logic.Logic;
import logic.Tag;
import logic.Task;
import logic.commands.Command;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand;
import parser.Parser;

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

    private SequentialTransition fadeTransition = new SequentialTransition ();
    
    Logic logic;
    Feedback feedback;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {        
        //testMethod();
        //--------------------------------------------------------------------          
        feedback = new Feedback();
        logic = new Logic();
        
        logic.loadFile();
        Parser.init();
        
        updateUi();
 
        //-------------------------------------------------------------------         
        userInput.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onTextChanged(newValue);
            }
        });
        
        successIcon = new Image("/images/SuccessIcon.png");
        failIcon = new Image("/images/FailIcon.png");
        
        screenBound = Screen.getPrimary().getVisualBounds();
        int width = (int) screenBound.getMaxX() * 3 / 5;
        int height = (int) screenBound.getMaxY() * 4 / 5;
        mainWindow.setPrefSize(width, height);
        
        tagDisplay.setItems(tagList);
        taskDisplay.setItems(taskList);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userInput.requestFocus();
            }
        });
        
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
        Command command = logic.executeCommand(userInput.getText());
        
        if (command instanceof InvalidCommand) {
            setFeedbackWindow(false, ((InvalidCommand)command).get_error_message());
            log.info("incorrect command entered");
        } else {            
            updateUi();
            
            if (!(command instanceof ViewCommand)) {
                setFeedbackWindow(true, userInput.getText());
            }
            userInput.clear();
            log.info("cmd executed");
        }
    }
    
    @FXML
    private void onKeyReleased(KeyEvent event) {    
        String autoInput = "";
        switch (event.getCode()) {
            case UP : 
                autoInput = logic.getPreviousUserInput();
                if (autoInput.isEmpty()) {
                    autoInput = userInput.getText();
                }
                userInput.setText(autoInput);
                userInput.positionCaret(autoInput.length() + 1);
                break;
                
            case DOWN :
                autoInput = logic.getNextUserInput();
                userInput.setText(autoInput);
                userInput.positionCaret(autoInput.length() + 1);
                break;
                
            case TAB :
                userInput.setText(feedback.getAutoComplete(userInput.getText()));
                userInput.positionCaret(userInput.getText().length() + 1);
                break;
                
            default :
                break;
        } 
    }
    
    @FXML
    private void onKeyPressed(KeyEvent event) { 
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
            event.consume();
        }
    }
    
    public void onTextChanged(String newString) {
        if (newString.isEmpty()) {          
            topPrompt.setVisible(false);
            bottomPrompt.setVisible(false);
            log.info("hide prompt");
        } else {
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
    
    private void onTagPressed(ActionEvent event) {
        ToggleButton clicked = (ToggleButton)event.getSource();
        logic.setSelectedTag(clicked.getText(), clicked.isSelected());
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
        createFader(feedbackWindow);
    }
    
    private void createFader(Node node) {
        fadeTransition.stop();
        node.setVisible(true);
        
        PauseTransition pause = new PauseTransition(Duration.millis(1500));
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setFromValue(1);
        fade.setToValue(0);
        
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                node.setVisible(false);
            }
        });
        fadeTransition.getChildren().remove(0, fadeTransition.getChildren().size());
        fadeTransition.getChildren().addAll(pause, fade);
        fadeTransition.play();
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
            tagButton.setSelected(tags.get(i).getIsSelected());
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
        assert(logic.getTagsList() != null);
        assert(logic.getTagsList() != null);
        assert(logic.getCurrentViewType() != null);
        
    	tasks = logic.getViewList();
        tags = logic.getTagsList();
        categorySelected = logic.getSelectedCategory();
        viewLabel.setText(logic.getCurrentViewType());
        
        updateTaskDisplay();
        updateTagDisplay();
        updateCategoryDisplay();
    }
    
    
    
    /*
    private void setFeedback(boolean isSuccessful, String message) {
        GridPane feedbackPane = new GridPane();
        feedbackPane.setMaxHeight(100);
        feedbackPane.setMaxWidth(Region.USE_PREF_SIZE);
        feedbackPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);" + "-fx-background-radius: 20;"
                            + "-fx-padding: 10, 10, 10, 10;");
        
        ColumnConstraints iconColumn = new ColumnConstraints();
        ColumnConstraints mesageColumn = new ColumnConstraints();
        iconColumn.setMaxWidth(100);
        
        feedbackPane.getColumnConstraints().addAll(iconColumn, mesageColumn);
        
        ImageView iconImg = new ImageView();
        iconImg.setFitHeight(70);
        iconImg.setFitWidth(70);
        Label messageLabel = new Label();
        if (isSuccessful) {
            iconImg.setImage(new Image("/images/SuccessIcon.png"));
            messageLabel.setText("Successfully executed command" + "\n" + message);
        } else { 
            iconImg.setImage(new Image("/images/FailIcon.png"));
            messageLabel.setText("Failed to execute command" + "\n" + message);
        }       
        feedbackPane.addRow(0, iconImg, messageLabel);
        
        mainWindow.setMargin(feedbackPane, new Insets(0, 100, 0, 100));
        mainWindow.getChildren().add(feedbackPane);
        
        FadeTransition fade = new FadeTransition(Duration.seconds(2), feedbackPane);
        fade.setOnFinished((ActionEvent event) -> {
                mainWindow.getChildren().remove(feedbackPane);
        });
        fade.setFromValue(1);
        fade.setToValue(0);

        fade.play();
    }
    */
}
