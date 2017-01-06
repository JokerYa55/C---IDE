package com.mycompany.minimal_c_ide_javafx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

public class FXMLController implements Initializable {

    private static Logger log = Logger.getLogger(FXMLController.class);

    @FXML
    private Label label;

    @FXML
    private TextArea idTextEditor;

    @FXML
    private MenuItem idMenuItemOpen;
    
    @FXML
    private MenuItem idMenuItemCompile;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        idTextEditor.setText("GOOD!");
    }

    @FXML
    private void idMenuItemCompileAction(ActionEvent event) {
        
    }
    
    @FXML
    private void idMenuItemOpenAction(ActionEvent event) {
        log.debug(event);
        
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Open Document");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("c-- files (*.c--)", "*.c--");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);//Указываем текущую сцену CodeNote.mainStage MainApp.scene.mainStage
        if (file != null) {
            try (BufferedReader fr = new BufferedReader(new FileReader(file))) {
                try {
                    String temp = "";
                    StringBuilder sb = new StringBuilder();
                    while ((temp = fr.readLine()) != null) {
                        sb.append(temp + "\n");
                    }
                    
                    idTextEditor.setText(sb.toString());
                    log.debug("program = " + idTextEditor.getText());
                    
                } catch (Exception e) {
                    log.error(e);
                }
            } catch (FileNotFoundException ex) {
                log.error(ex);
            } catch (IOException ex) {
                log.error(ex);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        log.debug(rb);
    }
}
