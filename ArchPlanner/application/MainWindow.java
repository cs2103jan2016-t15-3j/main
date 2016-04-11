package application;
	
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * This class is used to set up the initial UI by loading the respective files for the initialization.
 * 
 * @@author A0140034B
 */
public class MainWindow extends Application {
    static Logger log = Logger.getLogger(MainWindow.class.getName());
    
    private static final String LOG_MESSAGE_LOADING_FXML = "Loading FXML";
    private static final String LOG_MESSAGE_SUCCESS_LOAD_FXML = "Successfully loaded FXML";
    private static final String LOG_MESSAGE_FAIL_LOAD_FXML = "Fail to load FXML: ";
    private static final String LOG_MESSAGE_LOADING_SCENE = "Loading Scene";
    private static final String LOG_MESSAGE_SETTING_STAGE = "Setting Stage";
    private static final String LOG_MESSAGE_SUCCESS_DISPLAY_WINDOW = "Successfully Displayed Window";
    
    private static final String FILE_PATH_LOGO_IMAGE = "/images/LogoImage.png";
    private static final String FILR_PATH_CSS = "/application/application.css";
    private static final String FILE_PATH_FXML = "/application/MainWindow.fxml";   
    
    public static void main(String[] args) {
        launch(args);
    }
    
    /** Set up the primary stage of the UI. */
	@Override
	public void start(Stage primaryStage) {
	    
	    URL layoutURL = getClass().getResource(FILE_PATH_FXML);
        URL cssURL = getClass().getResource(FILR_PATH_CSS);
        
        Parent root = loadFXML(layoutURL);       
        Scene scene = loadScene(cssURL, root);         
        setStage(primaryStage, scene);
        primaryStage.show();
        
        log.info(LOG_MESSAGE_SUCCESS_DISPLAY_WINDOW);
	}
	
	/** 
	 * Return the layout of the UI after loading from file.
	 * If URL of FXML file is not found, null is return.
	 *  
	 * @param layoutURL   the URL of the FXML file.
	 * @return            the layout of the UI.
	 */
    private Parent loadFXML(URL layoutURL) {
        log.info(LOG_MESSAGE_LOADING_FXML);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent layout = null;
        try {
            layout = (Parent) fxmlLoader.load(layoutURL.openStream());
            log.info(LOG_MESSAGE_SUCCESS_LOAD_FXML);
        } catch (IOException e) {
            log.severe(LOG_MESSAGE_FAIL_LOAD_FXML + e.getMessage());
        }
        return layout;
    }
    
    /** 
     * Return the scene of the UI by setting the layout and applying the CSS file.
     *  The layout is assumed to be initialize.
     *  
     *  @param cssURL   the URL of the CSS file.
     *  @param layout   the layout for the scene.
     *  @return         the scene of the UI.
     */
    private Scene loadScene(URL cssURL, Parent layout) {
        log.info(LOG_MESSAGE_LOADING_SCENE);
        assert(layout != null);
        
        Scene scene = new Scene(layout);       
        scene.getStylesheets().addAll(cssURL.toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        
        return scene;
    }
    
    /** 
     * Set the stage parameters.
     * The scene is assumed to be initialize.
     *  
     * @param stage    the stage that the parameters will be applied to.
     * @param layout   the scene that will be set to the stage.
     */
    private void setStage(Stage stage, Scene scene) {
        log.info(LOG_MESSAGE_SETTING_STAGE);
        assert(scene != null);
        
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);       
        stage.getIcons().add(new Image(FILE_PATH_LOGO_IMAGE));
    }
}
