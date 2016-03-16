package application;
	
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;


public class MainWindow extends Application {
 
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
        } catch (IOException e) {
            e.printStackTrace();
        }       

        Scene scene = new Scene(root);        
        scene.getStylesheets().addAll(cssURL.toExternalForm()); 
        
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT); 
        scene.setFill(Color.TRANSPARENT);
        primaryStage.show();
	}
}
