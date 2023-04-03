package ru.hiik.learninglinux;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * FXML Controller class
 *
 * @author dmitry
 */
public class TheoryController implements Initializable {

    @FXML
    private ComboBox<String> comboBox = new ComboBox<>();
    @FXML
    private WebView browser;
    private WebEngine webEngine;
    private SecretKey key;
    private IvParameterSpec iv;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            App.stg.setTitle("Learning Linux - теория");
            // Заполнение списка элемента ComboBox
            File[] files = FileManager.getLlhs();
            ObservableList<String> ol = FXCollections.observableArrayList();
            for (int i = 0; i < files.length; i++) {
                ol.add(files[i].getName());
            }
            comboBox.setItems(ol);
            webEngine = browser.getEngine();
            String[] llcContent = FileManager.readLlc();
            key = CryptoManager.convertStringToSecretKey(llcContent[0]);
            iv = CryptoManager.convertStringToIv(llcContent[1]);
        } catch (FileNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    /**
     * Обработка выбора темы
     */
    @FXML
    private void processSelection() {
        try {
            String theme = comboBox.getValue();
            String content = FileManager.readLlh(theme, key, iv);
            webEngine.loadContent(MarkdownParser.parse(content));
        } catch (FileNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException |
                InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    
}