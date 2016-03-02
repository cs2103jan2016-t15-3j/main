package application;

import logic.Task;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class TaskPane extends GridPane {
    
    private ColumnConstraints numColumn = new ColumnConstraints();
    private ColumnConstraints desColumn = new ColumnConstraints();
    private ColumnConstraints startColumn = new ColumnConstraints();
    private ColumnConstraints endColumn = new ColumnConstraints();
    
    private RowConstraints taskRow = new RowConstraints();
    
    private Label number = new Label();
    private Label description = new Label();
    private Label startDateTime = new Label();
    private Label endStartTime = new Label();
    
    public TaskPane() {
        this.getColumnConstraints().addAll(numColumn, desColumn, startColumn, endColumn);
        taskRow.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(taskRow);
        this.addRow(0, number, description, startDateTime, endStartTime);
    }
    
    public TaskPane(ReadOnlyDoubleProperty numWidth, ReadOnlyDoubleProperty desWidth, 
                    ReadOnlyDoubleProperty startWidth, ReadOnlyDoubleProperty endWidth, 
                    int taskNumber, Task task) {
        
        numColumn.prefWidthProperty().bind(numWidth);      
        desColumn.prefWidthProperty().bind(desWidth);     
        startColumn.prefWidthProperty().bind(startWidth);
        endColumn.prefWidthProperty().bind(endWidth);
        this.getColumnConstraints().addAll(numColumn, desColumn, startColumn, endColumn);
              
        taskRow.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(taskRow);
        
        number.setText(Integer.toString(taskNumber));
        number.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        number.setAlignment(Pos.CENTER);
        
        description.setText(task.getDescription());
        description.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        description.setPadding(new Insets(0, 0, 0, 10));
        
        startDateTime.setText(task.getStartDate() + " " + task.getStartTime());
        startDateTime.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        startDateTime.setPadding(new Insets(0, 0, 0, 10));
        
        endStartTime.setText(task.getEndDate() + " " + task.getEndTime());
        endStartTime.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        endStartTime.setPadding(new Insets(0, 0, 0, 10));
        
        this.addRow(0, number, description, startDateTime, endStartTime);
    }
    
    public void setColumnConstraints(ReadOnlyDoubleProperty numWidth, ReadOnlyDoubleProperty desWidth, 
                                     ReadOnlyDoubleProperty startWidth, ReadOnlyDoubleProperty endWidth) {
        numColumn.prefWidthProperty().bind(numWidth);      
        desColumn.prefWidthProperty().bind(desWidth);     
        startColumn.prefWidthProperty().bind(startWidth);
        endColumn.prefWidthProperty().bind(endWidth);
    }
    
    public void setTaskInfo(Task task) {
        description.setText(task.getDescription());
        description.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        description.setPadding(new Insets(0, 0, 0, 10));
        
        startDateTime.setText(task.getStartDate() + " " + task.getStartTime());
        startDateTime.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        startDateTime.setPadding(new Insets(0, 0, 0, 10));
        
        endStartTime.setText(task.getEndDate() + " " + task.getEndTime());
        endStartTime.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        endStartTime.setPadding(new Insets(0, 0, 0, 10));
    }
    
    public void setNumber(int taskNumber) {
        number.setText(Integer.toString(taskNumber));
        number.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        number.setAlignment(Pos.CENTER);
    }
}
