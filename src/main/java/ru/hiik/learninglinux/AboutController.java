package ru.hiik.learninglinux;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author dmitry
 */
public class AboutController implements Initializable {
    
    @FXML
    private Label info = new Label();
    @FXML
    private AnchorPane pane;
    @FXML
    private ImageView img;
    /**
     * Вывод информации о программе и среде выполнения
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        img.setPreserveRatio(false);
        img.fitWidthProperty().bind(pane.widthProperty());
        img.fitHeightProperty().bind(pane.heightProperty());
        App.stg.setTitle("Learning Linux - информация");
        info.setText("Learning Linux - программа для изучения операционных систем Linux\n\n"
        + "Рабочий каталог программы: " + System.getProperty("user.dir") + "\n\n"
        + "Домашний каталог Java Runtime Environment: " + System.getProperty("java.home") + "\n\n"
        + "Операционная система: " + System.getProperty("os.name")
        + " " + System.getProperty("os.version"));
    }    
    
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
