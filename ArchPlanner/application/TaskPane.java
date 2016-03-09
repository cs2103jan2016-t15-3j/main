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
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TaskPane extends GridPane {
    HBox tagPane = new HBox();
    
    private ColumnConstraints numColumn = new ColumnConstraints();
    private ColumnConstraints desColumn = new ColumnConstraints();
    private ColumnConstraints dateTimeColumn = new ColumnConstraints();
    
    private RowConstraints taskRow1 = new RowConstraints();
    private RowConstraints taskRow2 = new RowConstraints();
    
    private Label number = new Label();
    private Label description = new Label();
    private Label startDateTime = new Label();
    private Label endDateTime = new Label();
    
    //private Color textColor = Color.GRAY;
    //private String font = "Tempus Sans ITC";
    //private int textSize = 24;
    
    public TaskPane() {
        initialize();
    }
    
    public TaskPane(int taskNumber, Task task, ReadOnlyDoubleProperty maxWidth) {        
        initialize();
        
        this.prefWidthProperty().bind(maxWidth.subtract(40));
        setNumber(taskNumber);
        setTaskInfo(task);
    }
    
    private void initialize() {
        this.setStyle("-fx-background-color: lightgrey;" + "-fx-background-radius: 15;" +
                      "-fx-padding: 10;");
        
        numColumn.setMinWidth(50);
        numColumn.setMaxWidth(50);
        desColumn.setHgrow(Priority.SOMETIMES);
        dateTimeColumn.setPercentWidth(30);
        this.getColumnConstraints().addAll(numColumn, desColumn, dateTimeColumn);
          
        taskRow1.setVgrow(Priority.SOMETIMES);
        taskRow2.setVgrow(Priority.SOMETIMES);
        this.getRowConstraints().add(taskRow1);
        this.getRowConstraints().add(taskRow2);
        
        this.addRow(0, number, description, startDateTime);
        this.addRow(1, new Label(), tagPane, endDateTime);
        
        /*
        number.setFont(new Font(font, textSize));
        description.setFont(new Font(font, textSize));
        startDateTime.setFont(new Font(font, textSize));
        endDateTime.setFont(new Font(font, textSize));
        
        number.setTextFill(textColor);
        description.setTextFill(textColor);
        startDateTime.setTextFill(textColor);
        endDateTime.setTextFill(textColor);
        */
    }
    
    public void setTaskInfo(Task task) {
        description.setText(task.getDescription());
        description.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //description.setPadding(new Insets(0, 0, 0, 20));
        
        startDateTime.setText(task.getStartDate() + "   " + task.getStartTime());
        startDateTime.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //startDateTime.setPadding(new Insets(0, 0, 0, 20));
        
        endDateTime.setText(task.getEndDate() + "   " + task.getEndTime());
        endDateTime.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //endDateTime.setPadding(new Insets(0, 0, 0, 20));
        
        Label tag = new Label(task.getTag());
        tagPane.getChildren().add(tag);
    }
    
    public void setNumber(int taskNumber) {
        number.setText(Integer.toString(taskNumber));
        number.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        number.setAlignment(Pos.CENTER);
    }
}
