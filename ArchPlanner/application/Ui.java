package application;
	
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Ui extends Application {
	
    private static UiController uiInstance;
    
    public static UiController getInstance() {
        return uiInstance;
    }
    
	@Override
	public void start(Stage primaryStage) {

	    URL layoutURL = getClass().getResource("/application/Ui3.fxml");
	    URL cssURL = getClass().getResource("application.css");
	    
	    FXMLLoader fxmlLoader = new FXMLLoader();
	    Parent root = null;
	    try {
            root = (Parent) fxmlLoader.load(layoutURL.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

	    uiInstance = fxmlLoader.getController();	   
	    
	    Scene scene = new Scene(root, 780, 400);	    
	    scene.getStylesheets().addAll(cssURL.toExternalForm());	
	    
	    primaryStage.setScene(scene);
	    primaryStage.setMinWidth(950);
        primaryStage.setMinHeight(700);
	    primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
