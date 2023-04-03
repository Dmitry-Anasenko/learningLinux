package ru.hiik.learninglinux;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {
    
    protected boolean isSuccessfullyAuth;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.stg.setTitle("Learning Linux");
        try {
            FileManager.firstRun();
            isSuccessfullyAuth = false;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
        }
    }
    
    @FXML
    private void switchToTheory() throws IOException {
        App.setRoot("theory");
    }
    
    @FXML
    private void switchToTests() throws IOException {
        App.setRoot("tests");
    }
    
    @FXML
    private void auth() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("pass.fxml"));
        Stage stage = new Stage();
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Learning Linux - аутентификация");
        PassController pc = (PassController) loader.getController();
        pc.main = this;
        stage.showAndWait();
        if (isSuccessfullyAuth)
            App.setRoot("edit");
    }
    
    @FXML
    private void switchToAbout() throws IOException {
        App.setRoot("about");
    }
    
    @FXML
    private void exit() {
        App.stg.close();
    }
    
}
