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

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SysTray {
    
    private TrayIcon trayIcon;
    private boolean firstTime;
    
    public SysTray(Stage stage) {
        //createTrayIcon(primaryStage);
        firstTime = true;
        //Platform.setImplicitExit(false);  
        
        createTrayIcon(stage);
    }
    
    public void createTrayIcon(final Stage stage) {
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            java.awt.Image image = null;
            try {
                URL url = getClass().getResource("/images/icon.png");
                image = ImageIO.read(url);
            } catch (IOException ex) {
                System.out.println(ex);
            }


            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    hide(stage);
                }
            });
            // create a action listener to listen for default action executed on the tray icon
            final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            };

            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);
            popup.add(showItem);
            popup.addSeparator();
            MenuItem closeItem = new MenuItem("Quit");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);
            
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "ArchPlanner", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(showListener);
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            // ...
        }
    }

    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("ArchPlanner is in System Tray",
                                    "You can open ArchPlanner using Alt-space",
                                    TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }

    private void hide(final Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                    showProgramIsMinimizedMsg();
                } else {
                    System.exit(0);
                }
            }
        });
    }
}
