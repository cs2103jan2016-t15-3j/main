package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import application.TaskPane;
import logic.Logic;
import logic.Tag;
import logic.Task;
import logic.commands.CommandInterface;
import logic.commands.InvalidCommand;
import logic.commands.ViewCommand;
import logic.commands.ViewCommand.CATEGORY_TYPE;
import parser.Parser;
import prompt.Prompt;

//@@author A0140034B
public class MainController implements Initializable{
    
    static Logger log = Logger.getLogger(MainController.class.getName());
    
    private static final String VIEW_SCOPE_ALL = "ALL";
    private static final String STRING_EMPTY = "";
    
    private static final String FAIL_IMAGE_PATH = "/images/FailIcon.png";
    private static final String SUCCESS_IMAGE_PATH = "/images/SuccessIcon.png";

    private static final int REFRESH_DELAY_IN_MS = 60000;
    private static final int LIGHT_UP_DELAY_IN_MS = 2000;
    private static final int DISPLAY_FEEDBACK_DELAY_IN_MS = 1500;
    private static final int FADE_AWAY_FEEDBACK_DELAY_IN_MS = 500;
 
    @FXML private Label viewLabel;
    @FXML private Label topPrompt;
    @FXML private Label bottomPrompt;
    @FXML private Label feedbackLabel;
    
    @FXML private ImageView feedbackIcon;

    @FXML private Button miniButton;
    @FXML private Button closeButton;
    @FXML private Button backButton;

    @FXML private ToggleButton event;
    @FXML private ToggleButton deadline;
    @FXML private ToggleButton task;
    
    @FXML private StackPane mainWindow;
    @FXML private GridPane visibleWindow;
    @FXML private GridPane taskWindow;
    @FXML private GridPane feedbackWindow;   
    
    @FXML private TextField userInput;
    
    @FXML public ListView<ToggleButton> tagDisplay;
    @FXML public ListView<GridPane> taskDisplay;
 
    private ArrayList<Tag> tags = new ArrayList<Tag>();;
    private ArrayList<Task> tasks = new ArrayList<Task>();
    
    private CATEGORY_TYPE categorySelected;
    
    private ObservableList<ToggleButton> tagList = FXCollections.observableArrayList();
    private ObservableList<GridPane> taskList = FXCollections.observableArrayList();
    
    private double xPos, yPos;
    private Rectangle2D screenBound;
    
    private Image successIcon;
    private Image failIcon;

    private SequentialTransition fadeTransition = new SequentialTransition ();
    
    Logic logic;
    Prompt feedback;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {                 
        feedback = new Prompt();
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
        
        successIcon = new Image(SUCCESS_IMAGE_PATH);
        failIcon = new Image(FAIL_IMAGE_PATH);
        
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
        
        //-------------------------------------------------------------------
        refreshDisplay();
        
        log.info("MainController initialzed");
    }

    //------------------------------------------------------------------------------------------------------------
    
    @FXML
    private void onWindowPressed(MouseEvent event) {
        Stage stage = (Stage) taskWindow.getScene().getWindow();
        if (!stage.isFullScreen()) {
            xPos = stage.getX() - event.getScreenX();
            yPos = stage.getY() - event.getScreenY();
        }
    }
    
    @FXML
    private void onWindowDragged(MouseEvent event) {
        Stage stage = (Stage) taskWindow.getScene().getWindow();
        if (!stage.isFullScreen()) {
            stage.setX(event.getScreenX() + xPos);
            stage.setY(event.getScreenY() + yPos);
        } 
    }
    
    @FXML
    private void minimize(ActionEvent event) {
        log.info("minimize button clicked");
        ((Stage) miniButton.getScene().getWindow()).setIconified(true);
        userInput.requestFocus();
        userInput.end();
    }
    
    @FXML
    private void maxOrRestore(ActionEvent event) {
        log.info("maxorRestore button clicked");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        if (!stage.isFullScreen()) {
            stage.setFullScreen(true);
            stage.setHeight(screenBound.getMaxY() + 140);
            visibleWindow.setStyle("-fx-background-color: #651b1b;");
        } else {
            stage.setFullScreen(false);
            visibleWindow.setStyle("-fx-background-color: transparent;");
        }
        userInput.requestFocus();
        userInput.end();
    }
    
    @FXML
    private void close(ActionEvent event) {
        log.info("closed button clicked");
        ((Stage) closeButton.getScene().getWindow()).close();
    }
    
    @FXML
    private void back(ActionEvent event) {
        log.info("back button clicked");
        executeCommand("view all");
    }
    
    //------------------------------------------------------------------------------------------------------------
    @FXML
    private void onCategoryPressed(ActionEvent event) {
        ToggleButton clicked = (ToggleButton)event.getSource();
        if (clicked.isSelected()) {
            logic.setSelectedCategory(logic.getCategoryType(clicked.getText()));
        } else {
            logic.setSelectedCategory(CATEGORY_TYPE.CATEGORY_ALL);
        }   
        updateUi();
    }
    
    private void onTagPressed(ActionEvent event) {
        ToggleButton clicked = (ToggleButton)event.getSource();
        logic.setSelectedTag(clicked.getText(), clicked.isSelected());
        updateUi();
    }
  
    @FXML
    private void onEnterPressed() {
        log.info("command entered");
        boolean isSuccessful = executeCommand(userInput.getText());
        if (isSuccessful) {
            userInput.clear();
            log.info("cmd executed");
        }
    }
    
    private boolean executeCommand(String userInput) {
        CommandInterface command = logic.executeCommand(userInput);
        
        if (command instanceof InvalidCommand) {
            setFeedbackWindow(false, ((InvalidCommand)command).getMessage());
            log.info("incorrect command entered");
            return false;
        } else {            
            updateUi();
            
            if (!(command instanceof ViewCommand)) {
                setFeedbackWindow(true, command.getMessage());
                if (logic.getIndexList() != null && !logic.getIndexList().isEmpty()) {
                    taskDisplay.layout();
                    taskDisplay.applyCss();
                    for (int i = 0; i < logic.getIndexList().size(); i++) {
                        fillTransition((TaskPane)taskDisplay.getItems().get(logic.getIndexList().get(i)));
                    }
                    taskDisplay.scrollTo(logic.getIndexList().get(0));
                }
            }
            return true;
        }
    }
    
    @FXML
    private void onKeyReleased(KeyEvent event) {    
        String autoInput = STRING_EMPTY;
        switch (event.getCode()) {
            case UP : 
                autoInput = logic.getPreviousUserInput();
                if (autoInput.isEmpty()) {
                    autoInput = userInput.getText();
                }
                userInput.setText(autoInput);
                userInput.positionCaret(autoInput.length());
                break;
                
            case DOWN :
                autoInput = logic.getNextUserInput();
                userInput.setText(autoInput);
                userInput.positionCaret(autoInput.length());
                break;
                
            case TAB :
                userInput.setText(feedback.getAutoComplete(userInput.getText()));
                userInput.positionCaret(userInput.getText().length());
                break;
                
            default :
                break;
        } 
    }
    
    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
            event.consume();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            visibleWindow.setStyle("-fx-background-color: transparent;");
        } else if (event.isControlDown() && event.getCode() == KeyCode.Z) {
            executeCommand("undo");
        } else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
            executeCommand("redo");
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
    //------------------------------------------------------------------------------------------------------------
    private void setFeedbackWindow(boolean isSuccessful, String message) {
        if (isSuccessful) {
            feedbackIcon.setImage(successIcon);
            feedbackLabel.setText("Success" + "\n" + message);
        } else {
            feedbackIcon.setImage(failIcon);
            feedbackLabel.setText("Failed" + "\n" + message);
        }
        createFader(feedbackWindow);
    }
    
    private void createFader(Node node) {
        fadeTransition.stop();
        node.setVisible(true);
        
        PauseTransition pause = new PauseTransition(Duration.millis(DISPLAY_FEEDBACK_DELAY_IN_MS));
        FadeTransition fade = new FadeTransition(Duration.millis(FADE_AWAY_FEEDBACK_DELAY_IN_MS), node);
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
    
    private void fillTransition(TaskPane taskLabel) {   
        int indexOfBgColor = taskLabel.getStyle().indexOf("-fx-background-color:");
        String endColor =  taskLabel.getStyle().substring(indexOfBgColor).split(";")[0] + ";";
        taskLabel.setStyle(taskLabel.getStyle() + "-fx-background-color: yellow;");

        PauseTransition pause = new PauseTransition(Duration.millis(LIGHT_UP_DELAY_IN_MS));
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                taskLabel.setStyle(taskLabel.getStyle() + endColor);
                taskLabel.applyCss();
                taskLabel.layout();
            }
        });
        pause.play();
    }
    
    private void refreshDisplay() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(REFRESH_DELAY_IN_MS), event -> {
            logic.updateViewList();
            updateUi();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
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
    		case CATEGORY_ALL :
    			event.setSelected(false);
    			deadline.setSelected(false);
    			task.setSelected(false);
    			break;
    		case CATEGORY_EVENTS :
    			event.setSelected(true);
    			break;
    		case CATEGORY_DEADLINES :
    			deadline.setSelected(true);
    			break;
    		case CATEGORY_TASKS :
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

        if (!viewLabel.getText().trim().equalsIgnoreCase(VIEW_SCOPE_ALL)) {
            backButton.setVisible(true);
        } else {
            backButton.setVisible(false);
        }
        
        updateTaskDisplay();
        updateTagDisplay();
        updateCategoryDisplay();
        
        userInput.requestFocus();
        userInput.end();
    }
}
