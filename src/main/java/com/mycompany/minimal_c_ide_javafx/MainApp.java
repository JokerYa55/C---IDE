package com.mycompany.minimal_c_ide_javafx;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;


public class MainApp extends Application {

    private static Logger log = Logger.getLogger(MainApp.class); 
    private Scene scene;
    @Override
    public void start(Stage stage) throws Exception {
        log.debug("start");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("C-- IDE");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
