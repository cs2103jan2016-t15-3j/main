package application;

import logic.Task;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

/**
 * This class is used to set up the layout of Task GridPane.
 * 
 * @@author A0140034B
 */
public class TaskPane extends GridPane {
    private static final String STRING_NEWLINE = "\n";
    private static final String STRING_THREE_SPACE = "   ";
    private static final String STRING_EMPTY = "";

    private static final String FX_BACKGROUND_COLOR_NORMAL = "-fx-background-color: lightgray;";
    private static final String FX_BACKGROUND_COLOR_OVERDUE = "-fx-background-color: pink;";
    private static final String FX_BACKGROUND_COLOR_DONE = "-fx-background-color: lightgreen;";
    private static final String FX_TEXT_FILL_END_COLOR = "-fx-text-fill: red;";
    private static final String FX_TEXT_FILL_START_COLOR = "-fx-text-fill: green;";
    private static final String FX_BACKGROUND_RADIUS_OF_PANE = "-fx-background-radius: 15;";
    private static final String FX_PADDING_OF_PANE = "-fx-padding: 10;";
    
    private static final int WIDTH_OF_SCROLL_BAR = 40;
    private static final int WIDTH_OF_NUM_COLUMN = 50;
    private static final int WIDTH_OF_DATE_COLUMN = 150;
    private static final int WIDTH_OF_TIME_COLUMN = 130;
    
    private static final int INDEX_INITIAL = 0;
    private static final int DISPLAY_NUMBER_MIN = 1;
    private static final int ROW_1_OF_PANE = 0;
    private static final int ROW_2_OF_PANE = 1;
    
    private ColumnConstraints _numColumn;
    private ColumnConstraints _desColumn;
    private ColumnConstraints _dateColumn;
    private ColumnConstraints _timeColumn;
    
    private RowConstraints _taskRow1;
    private RowConstraints _taskRow2;
    
    private Label _number;
    private Label _description;
    private Label _startDate;
    private Label _startTime;
    private Label _endDate;
    private Label _endTime;
    private Label _tag;
    
    private Tooltip _desciptionHover = new Tooltip();
    private Tooltip _tagHover = new Tooltip();
    
    /**
     * construct a new GridPane with the Task parameters and bind the its width.
     * 
     * @param task      the Task Object with the parameters.
     * @param width     the width that this GridPane will bind to.
     */
    public TaskPane(int displayNumber, Task task, ReadOnlyDoubleProperty width) {        
        initialize();
        
        setWidthProperty(width);
        setNumber(displayNumber);
        setTaskInfo(task);
    }
    
    /**
     * set the number to represent this task.
     * 
     * @param displayNumber     the number to represent this task.
     */
    public void setNumber(int displayNumber) {
        assert(displayNumber >= DISPLAY_NUMBER_MIN);
        
        _number.setText(Integer.toString(displayNumber));        
    }
    
    /**
     * set the task parameters to this TaskPane for display.
     * 
     * @param displayNumber     the task with the parameters.
     */
    public void setTaskInfo(Task task) {
        setTaskParameters(task); 
        setHoverText(task);      
        setBackGroundColor(task);     
    }
    
    private void initialize() {              
        setColunmProperties();
        setRowProperties();
        setLabelProperties();
        setPaneProperties();
    }
    
    private void setColunmProperties() {
        _numColumn = new ColumnConstraints();
        _desColumn = new ColumnConstraints();
        _dateColumn = new ColumnConstraints();
        _timeColumn = new ColumnConstraints();
        
        _numColumn.setMinWidth(WIDTH_OF_NUM_COLUMN);     
        _desColumn.setHgrow(Priority.SOMETIMES);
        _dateColumn.setMinWidth(Region.USE_PREF_SIZE);
        _timeColumn.setMinWidth(Region.USE_PREF_SIZE);
    }
    
    private void setRowProperties() {
        _taskRow1 = new RowConstraints();
        _taskRow2 = new RowConstraints();
       
        _taskRow1.setVgrow(Priority.SOMETIMES);
        _taskRow2.setVgrow(Priority.SOMETIMES);
    }

    private void setLabelProperties() {
        initLabel();
        setLabelAlignment();     
        setLabelSize();
        setLabelTextColor();
    }
 
    private void setPaneProperties() {
        this.setStyle(FX_BACKGROUND_RADIUS_OF_PANE + FX_PADDING_OF_PANE);
        
        this.getColumnConstraints().addAll(_numColumn, _desColumn, _dateColumn, _timeColumn);
        
        this.getRowConstraints().add(_taskRow1);
        this.getRowConstraints().add(_taskRow2);
        
        this.addRow(ROW_1_OF_PANE, _number, _description, _startDate, _startTime);
        this.addRow(ROW_2_OF_PANE, new Label(), _tag, _endDate, _endTime);
    }
   
    private void setWidthProperty(ReadOnlyDoubleProperty maxWidth) {
        this.prefWidthProperty().bind(maxWidth.subtract(WIDTH_OF_SCROLL_BAR));
    }
    
    private void initLabel() {
        _number = new Label();
        _description = new Label();     
        _startDate = new Label();
        _startTime = new Label();      
        _endDate = new Label();
        _endTime = new Label();
        _tag = new Label();
    }
    
    private void setLabelAlignment() {
        _number.setAlignment(Pos.CENTER_LEFT);
        _description.setAlignment(Pos.CENTER_LEFT);
        _startDate.setAlignment(Pos.CENTER_LEFT);
        _startTime.setAlignment(Pos.CENTER_LEFT);
        _endDate.setAlignment(Pos.CENTER_LEFT);
        _endTime.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void setLabelSize() {
        _number.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        _description.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        _startDate.setMinWidth(WIDTH_OF_DATE_COLUMN);
        _startTime.setMinWidth(WIDTH_OF_TIME_COLUMN);
        _endDate.setMinWidth(WIDTH_OF_DATE_COLUMN);
        _endTime.setMinWidth(WIDTH_OF_TIME_COLUMN);
        _tag.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
   
    private void setLabelTextColor() {
        _startDate.setStyle(_startDate.getStyle() + FX_TEXT_FILL_START_COLOR);
        _startTime.setStyle(_startTime.getStyle() + FX_TEXT_FILL_START_COLOR);
        _endDate.setStyle(_endDate.getStyle() + FX_TEXT_FILL_END_COLOR);
        _endTime.setStyle(_endTime.getStyle() + FX_TEXT_FILL_END_COLOR);
    }
    
    private void setTaskParameters(Task task) {
        assert(task.getDescription() != null);
        assert(task.getStartDateString() != null);
        assert(task.getStartTimeString() != null);
        assert(task.getEndDateString() != null);
        assert(task.getEndTimeString() != null);
        assert(task.getTagsList() != null);
        
        _description.setText(task.getDescription());       
        _startDate.setText(task.getStartDateString());
        _startTime.setText(task.getStartTimeString());       
        _endDate.setText(task.getEndDateString());
        _endTime.setText(task.getEndTimeString());
        String allTag = STRING_EMPTY;
        for(int i = INDEX_INITIAL; i < task.getTagsList().size(); i++) {
            allTag += task.getTagsList().get(i) + STRING_THREE_SPACE;
        }
        _tag.setText(allTag);
    }

    private void setHoverText(Task task) {        
        _desciptionHover.setText(task.getDescription());
        _description.setTooltip(_desciptionHover);
        
        String allTag = STRING_EMPTY;
        for(int i = INDEX_INITIAL; i < task.getTagsList().size(); i++) {
            allTag += task.getTagsList().get(i) + STRING_NEWLINE;
        }
        if (!allTag.isEmpty()) {
            _tagHover.setText(allTag);
            _tag.setTooltip(_tagHover);
        } 
    }
    
    private void setBackGroundColor(Task task) {
        if(task.getIsDone()) {
            this.setStyle(this.getStyle() + FX_BACKGROUND_COLOR_DONE);
        } else {
            if (task.getIsOverdue()) {
                this.setStyle(this.getStyle() + FX_BACKGROUND_COLOR_OVERDUE);
            } else {
                this.setStyle(this.getStyle() + FX_BACKGROUND_COLOR_NORMAL);
            }
        }
    }
}
