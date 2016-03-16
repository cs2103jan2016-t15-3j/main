package application;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import logic.Task;

public class HeaderPane extends GridPane{
    
    private Label header = new Label();
    
    public HeaderPane(String headerString, ReadOnlyDoubleProperty maxWidth) {
        this.setStyle("-fx-background-radius: 15;" + "-fx-padding: 10;" + "-fx-background-color: white;");
        
        header.setAlignment(Pos.CENTER_LEFT);
        header.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        setHeader(headerString);
        setWidthProperty(maxWidth);
    }
    
    public void setWidthProperty(ReadOnlyDoubleProperty maxWidth) {
        this.prefWidthProperty().bind(maxWidth.subtract(40));
    }
    
    public void setHeader(String headerString) {
        header.setText(headerString); 
    }
}
