package application;
	
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;


public class MainWindow extends Application {
 
    static Logger log = Logger.getLogger(MainWindow.class.getName());
    
    public static void main(String[] args) {
        launch(args);
    }
    
	@Override
	public void start(Stage primaryStage) {
	    //SysTray systemTray = new SysTray();
	    
	    URL layoutURL = getClass().getResource("/application/MainWindow.fxml");
        URL cssURL = getClass().getResource("application.css");
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load(layoutURL.openStream());
        } catch (Exception e) {
            log.severe("fxml load failed: " + e.getMessage());
        }       
        
        Scene scene = new Scene(root);       
        scene.getStylesheets().addAll(cssURL.toExternalForm()); 
        
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT); 
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
        log.info("Window Display Success");
	}
}
