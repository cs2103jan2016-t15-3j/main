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
    
    private ColumnConstraints numColumn;
    private ColumnConstraints desColumn;
    private ColumnConstraints dateColumn;
    private ColumnConstraints timeColumn;
    
    private RowConstraints taskRow1;
    private RowConstraints taskRow2;
    
    private Label number;
    private Label description;
    private Label startDate;
    private Label startTime;
    private Label endDate;
    private Label endTime;
    private Label tag;
    
    private Tooltip desciptionHover = new Tooltip();
    private Tooltip tagHover = new Tooltip();
    
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
        
        number.setText(Integer.toString(displayNumber));        
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
        numColumn = new ColumnConstraints();
        desColumn = new ColumnConstraints();
        dateColumn = new ColumnConstraints();
        timeColumn = new ColumnConstraints();
        
        numColumn.setMinWidth(WIDTH_OF_NUM_COLUMN);     
        desColumn.setHgrow(Priority.SOMETIMES);
        dateColumn.setMinWidth(Region.USE_PREF_SIZE);
        timeColumn.setMinWidth(Region.USE_PREF_SIZE);
    }
    
    private void setRowProperties() {
        taskRow1 = new RowConstraints();
        taskRow2 = new RowConstraints();
       
        taskRow1.setVgrow(Priority.SOMETIMES);
        taskRow2.setVgrow(Priority.SOMETIMES);
    }

    private void setLabelProperties() {
        initLabel();
        setLabelAlignment();     
        setLabelSize();
        setLabelTextColor();
    }
 
    private void setPaneProperties() {
        this.setStyle(FX_BACKGROUND_RADIUS_OF_PANE + FX_PADDING_OF_PANE);
        
        this.getColumnConstraints().addAll(numColumn, desColumn, dateColumn, timeColumn);
        
        this.getRowConstraints().add(taskRow1);
        this.getRowConstraints().add(taskRow2);
        
        this.addRow(ROW_1_OF_PANE, number, description, startDate, startTime);
        this.addRow(ROW_2_OF_PANE, new Label(), tag, endDate, endTime);
    }
   
    private void setWidthProperty(ReadOnlyDoubleProperty maxWidth) {
        this.prefWidthProperty().bind(maxWidth.subtract(WIDTH_OF_SCROLL_BAR));
    }
    
    private void initLabel() {
        number = new Label();
        description = new Label();     
        startDate = new Label();
        startTime = new Label();      
        endDate = new Label();
        endTime = new Label();
        tag = new Label();
    }
    
    private void setLabelAlignment() {
        number.setAlignment(Pos.CENTER_LEFT);
        description.setAlignment(Pos.CENTER_LEFT);
        startDate.setAlignment(Pos.CENTER_LEFT);
        startTime.setAlignment(Pos.CENTER_LEFT);
        endDate.setAlignment(Pos.CENTER_LEFT);
        endTime.setAlignment(Pos.CENTER_LEFT);
    }
    
    private void setLabelSize() {
        number.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        description.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        startDate.setMinWidth(WIDTH_OF_DATE_COLUMN);
        startTime.setMinWidth(WIDTH_OF_TIME_COLUMN);
        endDate.setMinWidth(WIDTH_OF_DATE_COLUMN);
        endTime.setMinWidth(WIDTH_OF_TIME_COLUMN);
        tag.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
   
    private void setLabelTextColor() {
        startDate.setStyle(startDate.getStyle() + FX_TEXT_FILL_START_COLOR);
        startTime.setStyle(startTime.getStyle() + FX_TEXT_FILL_START_COLOR);
        endDate.setStyle(endDate.getStyle() + FX_TEXT_FILL_END_COLOR);
        endTime.setStyle(endTime.getStyle() + FX_TEXT_FILL_END_COLOR);
    }
    
    private void setTaskParameters(Task task) {
        assert(task.getDescription() != null);
        assert(task.getStartDateString() != null);
        assert(task.getStartTimeString() != null);
        assert(task.getEndDateString() != null);
        assert(task.getEndTimeString() != null);
        assert(task.getTagsList() != null);
        
        description.setText(task.getDescription());       
        startDate.setText(task.getStartDateString());
        startTime.setText(task.getStartTimeString());       
        endDate.setText(task.getEndDateString());
        endTime.setText(task.getEndTimeString());
        String allTag = STRING_EMPTY;
        for(int i = INDEX_INITIAL; i < task.getTagsList().size(); i++) {
            allTag += task.getTagsList().get(i) + STRING_THREE_SPACE;
        }
        tag.setText(allTag);
    }

    private void setHoverText(Task task) {        
        desciptionHover.setText(task.getDescription());
        description.setTooltip(desciptionHover);
        
        String allTag = STRING_EMPTY;
        for(int i = INDEX_INITIAL; i < task.getTagsList().size(); i++) {
            allTag += task.getTagsList().get(i) + STRING_NEWLINE;
        }
        if (!allTag.isEmpty()) {
            tagHover.setText(allTag);
            tag.setTooltip(tagHover);
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
