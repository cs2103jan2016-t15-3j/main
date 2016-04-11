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
import javafx.scene.layout.Pane;
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
import prompt.Prompt;

/**
 * This class is used to set up the initial layout of the UI and handle all events happening on the UI
 * such as key press and button click.
 * 
 * @@author A0140034B
 */
public class MainController implements Initializable{ 
    static Logger log = Logger.getLogger(MainController.class.getName());
    
    private static final String LOG_MESSAGE_SETTING_DEFAULT_WINDOW_SIZE = "Setting default window size";
    private static final String LOG_MESSAGE_SETTING_PERIODIC_REFRESH = "Setting periodic refresh to display";
    private static final String LOG_MESSAGE_INITIALIZING_TEXTFIELD = "Initializing TextField";
    private static final String LOG_MESSAGE_MAINCONTROLLER_INITIALIZED = "MainController initialized";   
    private static final String LOG_MESSAGE_MINIMIZE_BUTTON_CLICKED = "Minimize button clicked";
    private static final String LOG_MESSAGE_MAX_BUTTON_CLICKED = "Maximize/Restore button clicked";
    private static final String LOG_MESSAGE_CLOSED_BUTTON_CLICKED = "Close button clicked";
    private static final String LOG_MESSAGE_BACK_BUTTON_CLICKED = "Back button clicked";
    private static final String LOG_MESSAGE_CATEGORY_BUTTON_CLICKED = "Category button clicked: ";
    private static final String LOG_MESSAGE_TAG_BUTTON_CLICKED = "Tag button clicked: ";
    private static final String LOG_MESSAGE_ENTER_PRESSED = "Enter Pressed";
    private static final String LOG_MESSAGE_COMMAND_EXECUTION_SUCCESS = "Command execution success";
    private static final String LOG_MESSAGE_COMMAND_EXECUTION_FAILED = "Command execution failed";
    private static final String LOG_MESSAGE_DISPLAYING_FEEDBACK_WINDOW = "Displaying feedback window";
    private static final String LOG_MESSAGE_LIGHTING_UP_TASK = "Lighting up tasks";
    private static final String LOG_MESSAGE_PROMPT_HIDE = "Prompt hide";
    private static final String LOG_MESSAGE_PROMPT_DISPLAY = "Prompt display";
    private static final String LOG_MESSAGE_PROMPT_EMPTY = "Prompt empty";
    private static final String LOG_MESSAGE_REFRESHING_TASKS_DISPLAY = "Refreshing Tasks Display";
    private static final String LOG_MESSAGE_REFRESHING_TAGS_DISPLAY = "Refreshing Tags Display";
    private static final String LOG_MESSAGE_REFRESHING_CATEGORY_DISPLAY = "Refreshing Category Display";
    
    private static final String STRING_EMPTY = "";
    private static final String STRING_NEW_LINE = "\n";
    private static final String STRING_FAILED = "Failed";
    private static final String STRING_SUCCESS = "Success";
    private static final String VIEW_SCOPE_ALL = "ALL";
    
    private static final String SIMULATE_REDO = "redo";
    private static final String SIMULATE_UNDO = "undo";
    
    private static final String FILE_PATH_FAIL_IMAGE = "/images/FailIcon.png";
    private static final String FILE_PATH_SUCCESS_IMAGE = "/images/SuccessIcon.png";
    
    private static final String FX_WINDOW_DEFAULT_BACKGROUND_COLOR = "-fx-background-color: transparent;";
    private static final String FX_WINDOW_FULL_SCREEN_BACKGROUND_COLOR = "-fx-background-color: #651b1b;";
    private static final String FX_TASK_LIGHT_UP_COLOR = "-fx-background-color: yellow;";

    private static final int DELAY_IN_MS_DISPLAY_REFRESH = 60000;
    private static final int DELAY_IN_MS_TASKS_LIGHT_UP = 2000;
    private static final int DELAY_IN_MS_FEEDBACK_DISPLAY = 1500;
    private static final int DELAY_IN_MS_FEEDBACK_FADE_AWAY = 500;
    
    private static final double RATIO_OF_WIDTH = 0.6;
    private static final double RATIO_OF_HEIGHT = 0.8;
    
    private static final int FIRST_CHANGED_TASK = 0;      
    private static final int OPACITY_MIN = 0;
    private static final int OPACITY_MAX = 1;
    private static final int INDEX_INITIAL = 0; 
    private static final int INDEX_OFFSET_OF_ARRAY_AND_DISPLAY_NUMBER = 1;
    private static final int INDEX_NEXT = 1;
    private static final int PROMPTS_INDEX_MIN = 0;
    private static final int PROMPTS_INDEX_MAX = 4;
    private static final int PROMPT_BOX_MAX_HEIGHT = 140;
    
    // mainWindow contains all Windows
    @FXML private StackPane _mainWindow;
    // visibleWindow contains taskWindow and userInputTextField
    @FXML private GridPane _visibleWindow;
    // taskWindow contains category, tag, and task Pane, all windows modification buttons and scopeofViewLabel
    @FXML private GridPane _taskWindow;
    // feedbackWindows contains feedbackLabel and feedbackIcon
    @FXML private GridPane _feedbackWindow; 
    
    @FXML private Label _scopeOfViewLabel;
    @FXML private Label _topPromptLabel;
    @FXML private Label _bottomPromptLabel;
    @FXML private Label _feedbackLabel;

    @FXML private Button _miniButton;
    @FXML private Button _maxiButton;
    @FXML private Button _closeButton;
    @FXML private Button _backButton;

    @FXML private ToggleButton _eventTButton;
    @FXML private ToggleButton _deadlineTButton;
    @FXML private ToggleButton _taskTButton;  
    
    @FXML private TextField _userInputTextField;
    
    @FXML private ListView<ToggleButton> _tagListView;
    @FXML private ListView<GridPane> _taskListView;
    
    @FXML private ImageView _feedbackIcon;
 
    private ArrayList<Tag> _tags = new ArrayList<Tag>();
    private ArrayList<Task> _tasks = new ArrayList<Task>();
    
    private ObservableList<ToggleButton> _tagList = FXCollections.observableArrayList();
    private ObservableList<GridPane> _taskList = FXCollections.observableArrayList();
    
    private double _xCoordinate, _yCoordinate;
    private Rectangle2D _screenBound;
    
    private Image _successIcon;
    private Image _failIcon;
    
    private CATEGORY_TYPE _categorySelected;
    
    private SequentialTransition _fadeTransition;
    
    private Logic _logic;
    private Prompt _prompt;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {              
        initPrompt();
        initLogic();   
        initFeedbackItems();
        
        _screenBound = Screen.getPrimary().getVisualBounds();
        setDefaultWindowSize(_mainWindow, _screenBound);
        
        _tagListView.setItems(_tagList);
        _taskListView.setItems(_taskList);
        updateUi(_logic);
        
        initTextField(_userInputTextField);
        
        setPeriodicRefreshToDisplay(_logic);    

        log.info(LOG_MESSAGE_MAINCONTROLLER_INITIALIZED);
    }

    private void initLogic() {
        _logic = new Logic();
    }

    private void initPrompt() {
        _prompt = new Prompt();
    }

    private void initFeedbackItems() {
        _fadeTransition = new SequentialTransition();
        _successIcon = new Image(FILE_PATH_SUCCESS_IMAGE);
        _failIcon = new Image(FILE_PATH_FAIL_IMAGE);
    }

    private void setDefaultWindowSize(Pane node, Rectangle2D screenBound) {
        log.info(LOG_MESSAGE_SETTING_DEFAULT_WINDOW_SIZE);
        
        int width = (int) (screenBound.getMaxX() * RATIO_OF_WIDTH);
        int height = (int) (screenBound.getMaxY() * RATIO_OF_HEIGHT);
        node.setPrefSize(width, height);
    }
    
    private void setPeriodicRefreshToDisplay(Logic logic) {
        log.info(LOG_MESSAGE_SETTING_PERIODIC_REFRESH);
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(DELAY_IN_MS_DISPLAY_REFRESH), timesUp -> {
            logic.updateViewList();
            updateUi(logic);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    private void initTextField(TextField userInputTextField) {
        log.info(LOG_MESSAGE_INITIALIZING_TEXTFIELD);
        
        userInputTextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldString, String newString) {
                onTextChanged(newString);
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setTextFieldFocus(userInputTextField);
            }
        });
    }
    
    /**
     * set the initial position of mouse coordinates.
     * 
     * @param event     mouse clicked on taskWindow.
     */
    @FXML
    private void onWindowPressed(MouseEvent event) {
        Stage stage = (Stage) _taskWindow.getScene().getWindow();
        if (!stage.isFullScreen()) {
            _xCoordinate = stage.getX() - event.getScreenX();
            _yCoordinate = stage.getY() - event.getScreenY();
        }
    }
    
    /**
     * set the stage coordinates to move window when drag.
     * 
     * @param event     mouse dragged on taskWindow.
     */
    @FXML
    private void onWindowDragged(MouseEvent event) {
        Stage stage = (Stage) _taskWindow.getScene().getWindow();
        if (!stage.isFullScreen()) {
            stage.setX(event.getScreenX() + _xCoordinate);
            stage.setY(event.getScreenY() + _yCoordinate);
        } 
    }
    
    /** miniButton clicked to minimize window. */
    @FXML
    private void minimize() {
        log.info(LOG_MESSAGE_MINIMIZE_BUTTON_CLICKED);
        
        ((Stage) _miniButton.getScene().getWindow()).setIconified(true);
        setTextFieldFocus(_userInputTextField);
    }
    
    /** maxiButton clicked to maximize or restore window. */
    @FXML
    private void maxOrRestore() {
        log.info(LOG_MESSAGE_MAX_BUTTON_CLICKED);
        
        Stage stage = (Stage) _maxiButton.getScene().getWindow();
        if (!stage.isFullScreen()) {
            maxWindowSize();
        } else {
            restoreWindowSize();
        }
        setTextFieldFocus(_userInputTextField);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                onTextChanged(_userInputTextField.getText());
            }
        });
    }
    
    private void maxWindowSize() {
        Stage stage = (Stage) _maxiButton.getScene().getWindow();
        stage.setFullScreen(true);
        stage.setHeight(_screenBound.getMaxY() + PROMPT_BOX_MAX_HEIGHT);
        _visibleWindow.setStyle(FX_WINDOW_FULL_SCREEN_BACKGROUND_COLOR);
    }
    
    private void restoreWindowSize() {
        Stage stage = (Stage) _maxiButton.getScene().getWindow();
        stage.setFullScreen(false);
        _visibleWindow.setStyle(FX_WINDOW_DEFAULT_BACKGROUND_COLOR);
    }
    
    /** closeButton clicked to close program. */
    @FXML
    private void close() {
        log.info(LOG_MESSAGE_CLOSED_BUTTON_CLICKED);
        
        ((Stage) _closeButton.getScene().getWindow()).close();
    }
    
     /** switch scope of view to view all tasks. */
    @FXML
    private void back() {
        log.info(LOG_MESSAGE_BACK_BUTTON_CLICKED);
        
        _logic.setSelectedCategory(CATEGORY_TYPE.ALL);
        updateUi(_logic);        
        setTextFieldFocus(_userInputTextField);
    }
    
    /**
     * verify the category clicked and send to Logic to update display.
     * 
     * @param event     the category clicked.
     */
    @FXML
    private void onCategoryPressed(ActionEvent event) {
        log.info(LOG_MESSAGE_CATEGORY_BUTTON_CLICKED + event.getSource().toString());
        
        ToggleButton clicked = (ToggleButton)event.getSource();
        if (clicked.isSelected()) {
            _logic.setSelectedCategory(_logic.getCategoryType(clicked.getText()));
            //_logic.setSelectedCategory(clicked.getText().toString());
        } else {
            _logic.setSelectedCategory(CATEGORY_TYPE.ALL);
        }   
        updateUi(_logic);        
        setTextFieldFocus(_userInputTextField);
    }
    
    /**
     * verify the tag clicked and inform Logic to update display.
     * 
     * @param event     the tag clicked.
     */
    private void onTagPressed(ActionEvent event) {
        log.info(LOG_MESSAGE_TAG_BUTTON_CLICKED + event.getSource().toString());
        
        TagButton clicked = (TagButton)event.getSource();
        _logic.setSelectedTag(clicked.getText(), clicked.isSelected());
        updateUi(_logic);        
        setTextFieldFocus(_userInputTextField);
    }
    
    /** enter pressed in userInputTextField and send text in TextField for execution. */
    @FXML
    private void onEnterPressed() {
        log.info(LOG_MESSAGE_ENTER_PRESSED);
        
        executeCommand(_userInputTextField.getText());
    }
    
    /**
     * send text to Logic for execution and feedback to user the status of the command.
     * 
     * @param input     the string of command to be processed.
     */
    private void executeCommand(String input) {
        CommandInterface command = _logic.executeCommand(input);
        
        if (command instanceof InvalidCommand) {
            log.info(LOG_MESSAGE_COMMAND_EXECUTION_FAILED);
            
            setFeedbackWindow(false, command.getMessage());
        } else {
            log.info(LOG_MESSAGE_COMMAND_EXECUTION_SUCCESS);
            
            updateUi(_logic);
            if (!(command instanceof ViewCommand)) {
                setFeedbackWindow(true, command.getMessage());
                lightUpTasksChange(_logic);
            }
            _userInputTextField.clear();
        }
    }
    
    /**
     * show Feedback window to user the status of the command.
     * 
     * @param isSuccessful  the status of the command.
     * @param message       the message to display to user.
     */
    private void setFeedbackWindow(boolean isSuccessful, String message) {
        log.info(LOG_MESSAGE_DISPLAYING_FEEDBACK_WINDOW);
        
        if (isSuccessful) {
            _feedbackIcon.setImage(_successIcon);
            _feedbackLabel.setText(STRING_SUCCESS + STRING_NEW_LINE + message);
        } else {
            _feedbackIcon.setImage(_failIcon);
            _feedbackLabel.setText(STRING_FAILED + STRING_NEW_LINE + message);
        }
        createFader(_feedbackWindow);
    }
    
    /**
     * create a transition for Feedback window to display and fade away.
     * 
     * @param node  the window to display and fade away.
     */
    private void createFader(Node node) {
        _fadeTransition.stop();
        node.setVisible(true);
        
        PauseTransition pause = new PauseTransition(Duration.millis(DELAY_IN_MS_FEEDBACK_DISPLAY));
        FadeTransition fade = new FadeTransition(Duration.millis(DELAY_IN_MS_FEEDBACK_FADE_AWAY), node);
        fade.setFromValue(OPACITY_MAX);
        fade.setToValue(OPACITY_MIN);
        
        _fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                node.setVisible(false);
            }
        });
        _fadeTransition.getChildren().remove(INDEX_INITIAL, _fadeTransition.getChildren().size());
        _fadeTransition.getChildren().addAll(pause, fade);
        _fadeTransition.play();
    }
    
    /**
     * show Feedback window to user the status of the command.
     * 
     * @param isSuccessful  the status of the command.
     * @param message       the message to display to user.
     */
    private void lightUpTasksChange(Logic logic) {
        log.info(LOG_MESSAGE_LIGHTING_UP_TASK);
        
        ArrayList<Integer> tasksChanged = logic.getIndexList();
        if (tasksChanged != null && !tasksChanged.isEmpty()) {
            _taskListView.layout();
            _taskListView.applyCss();
            for (int i = INDEX_INITIAL; i < tasksChanged.size(); i++) {
                fillTransition((TaskPane)_taskListView.getItems().get(tasksChanged.get(i)));
            }
            _taskListView.scrollTo(tasksChanged.get(FIRST_CHANGED_TASK));
        }
    }
    
    /**
     * light up task for a duration.
     * 
     * @param taskLabel     the task that will be lighted up.
     */
    private void fillTransition(TaskPane taskLabel) {
        String defaultStyleOfTask = taskLabel.getStyle();
        taskLabel.setStyle(defaultStyleOfTask + FX_TASK_LIGHT_UP_COLOR);

        PauseTransition pause = new PauseTransition(Duration.millis(DELAY_IN_MS_TASKS_LIGHT_UP));
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                taskLabel.setStyle(defaultStyleOfTask);
                taskLabel.applyCss();
                taskLabel.layout();
            }
        });
        pause.play();
    } 
    
    /**
     * keys to help user input command in TextField.
     * 
     * @param taskLabel     the key that is released in userInputTextField.
     */
    @FXML
    private void onKeyReleased(KeyEvent event) {    
        String autoInput = STRING_EMPTY;
        switch (event.getCode()) {
            case UP : 
                autoInput = _logic.getPreviousUserInput();
                if (autoInput.isEmpty()) {
                    autoInput = _userInputTextField.getText();
                }
                _userInputTextField.setText(autoInput);
                setTextFieldFocus(_userInputTextField);
                break;
                
            case DOWN :
                autoInput = _logic.getNextUserInput();
                _userInputTextField.setText(autoInput);
                setTextFieldFocus(_userInputTextField);
                break;
                
            case TAB :
                autoInput = _prompt.getAutoComplete(_userInputTextField.getText());
                _userInputTextField.setText(autoInput);
                setTextFieldFocus(_userInputTextField);
                break;
                
            default :
                break;
        } 
    }
    
    /**
     * hotkeys to help user execute a command.
     * 
     * @param taskLabel     the keys that is pressed in userInputTextField.
     */
    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (event.isControlDown()) {
            switch(event.getCode()) {
                case D :
                    _deadlineTButton.fire();
                    break;
                case E :
                    _eventTButton.fire();
                    break;
                case T :
                    _taskTButton.fire();
                    break;
                case Y :
                    executeCommand(SIMULATE_REDO);
                    break;
                case Z :
                    executeCommand(SIMULATE_UNDO);
                    break;
                default:
                    break;
            }
        } else if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
            event.consume();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            restoreWindowSize();
        }
    }
    
    /**
     * display prompts to user depending of the text in the TextField.
     * 
     * @param newString     the changed text in userInputTextField.
     */
    private void onTextChanged(String newString) {
        if (newString.isEmpty()) {
            log.info(LOG_MESSAGE_PROMPT_HIDE);
            
            hidePrompt();
        } else {
            log.info(LOG_MESSAGE_PROMPT_DISPLAY);
            
            setPrompt(newString);
            showPrompt();
        }
    }
    
    private void hidePrompt() {
        _topPromptLabel.setVisible(false);
        _bottomPromptLabel.setVisible(false);
    }
    
    /** show prompts bottom or top of userInputTextfield depending if prompt is out of screen. */
    private void showPrompt() {
        // get snapshot of node so height can be calculated
        _bottomPromptLabel.getScene().snapshot(null);
        
        double bottomIndexOfVisibleWindow =_mainWindow.localToScreen(_mainWindow.getBoundsInLocal()).getMaxY() - PROMPT_BOX_MAX_HEIGHT;
        double heightOfPromptBox = _bottomPromptLabel.getHeight();

        if (bottomIndexOfVisibleWindow + heightOfPromptBox > _screenBound.getMaxY()) {
            _topPromptLabel.setVisible(true);
            _bottomPromptLabel.setVisible(false);
        } else {
            _topPromptLabel.setVisible(false);
            _bottomPromptLabel.setVisible(true);
        }
    }
    
    private void setPrompt(String newString) {
        ArrayList<String> prompt = _prompt.getPrompts(newString);
        assert(prompt.size() > PROMPTS_INDEX_MIN && prompt.size() <= PROMPTS_INDEX_MAX);
        
        if (prompt != null && !prompt.isEmpty()) {
            _topPromptLabel.setText(prompt.get(PROMPTS_INDEX_MIN));
            _bottomPromptLabel.setText(prompt.get(PROMPTS_INDEX_MIN));
            for (int i  = PROMPTS_INDEX_MIN + INDEX_NEXT; i < prompt.size(); i++) {
                _topPromptLabel.setText(prompt.get(i) + STRING_NEW_LINE + _topPromptLabel.getText());
                _bottomPromptLabel.setText(_bottomPromptLabel.getText() + STRING_NEW_LINE + prompt.get(i));
            }
        } else {
            log.warning(LOG_MESSAGE_PROMPT_EMPTY);
        }
    }

    private void updateUi(Logic logic) {
        assert(logic.getViewList() != null);
        assert(logic.getTagsList() != null);
        assert(logic.getSelectedCategory() != null);
        assert(logic.getCurrentViewType() != null);
        
    	_tasks = logic.getViewList();
    	updateTaskPane(_tasks);
    	
        _tags = logic.getTagsList();
        updateTagPane(_tags);
        
        _categorySelected = logic.getSelectedCategory();
        updateCategoryPane(_categorySelected);
        
        _scopeOfViewLabel.setText(logic.getCurrentViewType());
        displayBackButton(_scopeOfViewLabel.getText());
    }
    
    private void updateTaskPane(ArrayList<Task> tasks) {
        log.info(LOG_MESSAGE_REFRESHING_TASKS_DISPLAY);
        
        _taskList.clear();      
        for (int i = INDEX_INITIAL; i < tasks.size(); i++) {        
            TaskPane displayTask = new TaskPane(i + INDEX_OFFSET_OF_ARRAY_AND_DISPLAY_NUMBER, tasks.get(i), _taskListView.widthProperty());
            _taskList.add(displayTask);
        }
    }
    
    private void updateTagPane(ArrayList<Tag> tags) {
        log.info(LOG_MESSAGE_REFRESHING_TAGS_DISPLAY);
        
        _tagList.clear();       
        for (int i = INDEX_INITIAL; i < tags.size(); i++) {
            TagButton tagButton = new TagButton(tags.get(i), _tagListView.widthProperty());
            tagButton.setOnAction(tagObj -> {
                onTagPressed(tagObj);
            });
            _tagList.add(tagButton);
        }
    }
    
    private void updateCategoryPane(CATEGORY_TYPE categorySelected) {
        log.info(LOG_MESSAGE_REFRESHING_CATEGORY_DISPLAY);
        
        switch (categorySelected) {
            case ALL :
                _eventTButton.setSelected(false);
                _deadlineTButton.setSelected(false);
                _taskTButton.setSelected(false);
                break;
            case EVENTS :
                _eventTButton.setSelected(true);
                break;
            case DEADLINES :
                _deadlineTButton.setSelected(true);
                break;
            case TASKS :
                _taskTButton.setSelected(true);
                break;
        }
    }

    private void displayBackButton(String scopeOfView) {
        //if (!scopeOfView.trim().equalsIgnoreCase(VIEW_TYPE.ALL)) {
        if (!scopeOfView.trim().equalsIgnoreCase(VIEW_SCOPE_ALL)) {
            _backButton.setVisible(true);
        } else {
            _backButton.setVisible(false);
        }
    }
    
    private void setTextFieldFocus(TextField textField) {
        textField.requestFocus();
        textField.end();
    }
}
