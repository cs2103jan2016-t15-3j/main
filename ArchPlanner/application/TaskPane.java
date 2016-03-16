package application;

import logic.Task;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TaskPane extends GridPane {
       
    private ColumnConstraints numColumn = new ColumnConstraints();
    private ColumnConstraints desColumn = new ColumnConstraints();
    private ColumnConstraints dateColumn = new ColumnConstraints();
    private ColumnConstraints timeColumn = new ColumnConstraints();
    
    private RowConstraints taskRow1 = new RowConstraints();
    private RowConstraints taskRow2 = new RowConstraints();
    
    private Label number = new Label();
    private Label description = new Label();
    private Label startDate = new Label();
    private Label startTime = new Label();
    private Label endDate = new Label();
    private Label endTime = new Label();
    private Label tag = new Label();
    
    public TaskPane(int taskNumber, Task task, ReadOnlyDoubleProperty maxWidth) {        
        initialize();
        
        setWidthProperty(maxWidth);
        setNumber(taskNumber);
        setTaskInfo(task);
    }
    
    private void initialize() {
        this.setStyle("-fx-background-radius: 15;" + "-fx-padding: 10;");
        
        numColumn.setMinWidth(50);     
        desColumn.setHgrow(Priority.SOMETIMES);
        dateColumn.setMinWidth(Region.USE_PREF_SIZE);
        timeColumn.setMinWidth(Region.USE_PREF_SIZE);
        this.getColumnConstraints().addAll(numColumn, desColumn, dateColumn, timeColumn);
          
        taskRow1.setVgrow(Priority.SOMETIMES);
        taskRow2.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(taskRow1);
        this.getRowConstraints().add(taskRow2);
        
        this.addRow(0, number, description, startDate, startTime);
        this.addRow(1, new Label(), tag, endDate, endTime);
        
        number.setAlignment(Pos.CENTER_LEFT);
        description.setAlignment(Pos.CENTER_LEFT);
        startDate.setAlignment(Pos.CENTER_LEFT);
        startTime.setAlignment(Pos.CENTER_LEFT);
        endDate.setAlignment(Pos.CENTER_LEFT);
        endTime.setAlignment(Pos.CENTER_LEFT);
        
        number.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        description.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        startDate.setMinWidth(160);
        startTime.setMinWidth(90);
        endDate.setMinWidth(160);
        endTime.setMinWidth(90);
        tag.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
    
    public void setWidthProperty(ReadOnlyDoubleProperty maxWidth) {
        this.prefWidthProperty().bind(maxWidth.subtract(40));
    }
    
    public void setTaskInfo(Task task) {
        description.setText(task.getDescription());       
        startDate.setText(task.getStartDate());
        startTime.setText(task.getStartTime());       
        endDate.setText(task.getEndDate());
        endTime.setText(task.getEndTime());
        
        String allTag = "";
        for(int i = 0; i < task.getTagsList().size(); i++) {
            allTag += task.getTagsList().get(i) + "   ";
        }
        tag.setText(allTag);
        
        if(task.getIsDone()) {
            this.setStyle(this.getStyle() + ("-fx-background-color: lightgreen;"));
        } else {
            this.setStyle(this.getStyle() + ("-fx-background-color: lightgray;"));
        }
    }
    
    public void setNumber(int taskNumber) {
        number.setText(Integer.toString(taskNumber));        
    }
}
