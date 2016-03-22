package application;

import logic.Task;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

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
    
    public TaskPane(int displayNumber, Task task, ReadOnlyDoubleProperty maxWidth) {        
        initialize();
        
        setWidthProperty(maxWidth);
        setNumber(displayNumber);
        setTaskInfo(task);
    }
    
    private void initialize() {
        setPaneProperties();       
        setColunmProperties();                
        setRowProperties();        
        setLabelProperties();  
        //startTime.setStyle(startTime.getStyle() + ("-fx-background-color: lightgreen;"));
        //endTime.setStyle(endTime.getStyle() + ("-fx-background-color: lightgreen;"));
        //startDate.setStyle(startDate.getStyle() + ("-fx-background-color: lightblue;"));
        //endDate.setStyle(endDate.getStyle() + ("-fx-background-color: lightblue;"));
    }

    private void setLabelProperties() {
        setLabelAlignment();     
        setLabelSize();
    }

    private void setLabelSize() {
        number.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        description.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        startDate.setMinWidth(180);
        startTime.setMinWidth(100);
        endDate.setMinWidth(180);
        endTime.setMinWidth(100);
        tag.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    private void setLabelAlignment() {
        number.setAlignment(Pos.CENTER_LEFT);
        description.setAlignment(Pos.CENTER_LEFT);
        startDate.setAlignment(Pos.CENTER_LEFT);
        startTime.setAlignment(Pos.CENTER_LEFT);
        endDate.setAlignment(Pos.CENTER_LEFT);
        endTime.setAlignment(Pos.CENTER_LEFT);
    }

    private void setRowProperties() {
        taskRow1.setVgrow(Priority.SOMETIMES);
        taskRow2.setVgrow(Priority.SOMETIMES);
    }

    private void setColunmProperties() {
        numColumn.setMinWidth(50);     
        desColumn.setHgrow(Priority.SOMETIMES);
        dateColumn.setMinWidth(Region.USE_PREF_SIZE);
        timeColumn.setMinWidth(Region.USE_PREF_SIZE);
    }

    private void setPaneProperties() {
        this.setStyle("-fx-background-radius: 15;" + "-fx-padding: 10;");
        
        this.getColumnConstraints().addAll(numColumn, desColumn, dateColumn, timeColumn);
        
        this.getRowConstraints().add(taskRow1);
        this.getRowConstraints().add(taskRow2);

        this.addRow(0, number, description, startDate, startTime);
        this.addRow(1, new Label(), tag, endDate, endTime);
    }

    public void setWidthProperty(ReadOnlyDoubleProperty maxWidth) {
        this.prefWidthProperty().bind(maxWidth.subtract(40));
    }

    public void setTaskInfo(Task task) {
        assert(task.getDescription() != null);
        assert(task.getStartDate() != null);
        assert(task.getStartTime() != null);
        assert(task.getEndDate() != null);
        assert(task.getEndTime() != null);

        description.setText(task.getDescription());
//        startDate.setText(task.getStartDate());
//        startTime.setText(task.getStartTime());
//        endDate.setText(task.getEndDate());
//        endTime.setText(task.getEndTime());
        
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
    
    public void setNumber(int displayNumber) {
        assert(displayNumber > 0);
        number.setText(Integer.toString(displayNumber));        
    }
}
