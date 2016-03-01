package application;

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
    
    private Label num = new Label();
    private Label description = new Label();
    private Label start = new Label();
    private Label end = new Label();
    
    public TaskPane() {
        this.getColumnConstraints().addAll(numColumn, desColumn, startColumn, endColumn);
        taskRow.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(taskRow);
        this.addRow(0, num, description, start, end);
    }
    
    public TaskPane(ReadOnlyDoubleProperty numWidth, ReadOnlyDoubleProperty desWidth, 
                    ReadOnlyDoubleProperty startWidth, ReadOnlyDoubleProperty endWidth, 
                    int number, Task task) {
        
        numColumn.prefWidthProperty().bind(numWidth);      
        desColumn.prefWidthProperty().bind(desWidth);     
        startColumn.prefWidthProperty().bind(startWidth);
        endColumn.prefWidthProperty().bind(endWidth);
        this.getColumnConstraints().addAll(numColumn, desColumn, startColumn, endColumn);
              
        taskRow.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(taskRow);
        
        num.setText(Integer.toString(number));
        num.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        num.setAlignment(Pos.CENTER);
        
        description.setText(task.getDescription());
        description.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        description.setPadding(new Insets(0, 0, 0, 10));
        
        start.setText(task.getStartDate() + " " + task.getStartTime());
        start.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        start.setPadding(new Insets(0, 0, 0, 10));
        
        end.setText(task.getEndDate() + " " + task.getEndTime());
        end.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        end.setPadding(new Insets(0, 0, 0, 10));
        
        this.addRow(0, num, description, start, end);
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
        
        start.setText(task.getStartDate() + " " + task.getStartTime());
        start.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        start.setPadding(new Insets(0, 0, 0, 10));
        
        end.setText(task.getEndDate() + " " + task.getEndTime());
        end.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        end.setPadding(new Insets(0, 0, 0, 10));
    }
    
    public void setNumber(int number) {
        num.setText(Integer.toString(number));
        num.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        num.setAlignment(Pos.CENTER);
    }
}
