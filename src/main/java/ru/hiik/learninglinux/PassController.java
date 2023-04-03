package ru.hiik.learninglinux;

import java.io.FileNotFoundException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author dmitry
 */
public class PassController implements Initializable {

    protected PrimaryController main;
    @FXML
    private AnchorPane globParent;
    @FXML
    private PasswordField passField;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passField.requestFocus();
    }
    
    @FXML
    private void enter() {
        passField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                auth();
            }
        } );
    }
    
    @FXML
    private void auth() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            String password = passField.getText();
            if (password != null && FileManager.checkPassword(password)) {
                main.isSuccessfullyAuth = true;
                globParent.getScene().getWindow().hide();
            }
            else {
                alert.setTitle("Learning Linux - неверный пароль");
                alert.setContentText("Неверный пароль");
                alert.showAndWait();
                passField.setText(null);
            }
        } catch (FileNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            alert.setTitle("Learning Linux - ошибка");
            alert.setContentText(ex.getLocalizedMessage());
        }
    }
    
}
