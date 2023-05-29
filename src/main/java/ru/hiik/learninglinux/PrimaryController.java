package ru.hiik.learninglinux;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PrimaryController implements Initializable {

    protected boolean isSuccessfullyAuth;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.stg.setTitle("Learning Linux");
        try {
            // Создание файлов по умолчанию
            byte code = FileManager.firstRun();
            if (code == 0) {
                String[] llcContent = FileManager.readLlc();
                String separator = FileManager.getSeparator();
                File defaultLlhDir = new File(System.getProperty("user.dir") + separator + "defaults"
                        + separator + "llhs");
                File defaultLleDir = new File(System.getProperty("user.dir") + separator + "defaults"
                        + separator + "lles");
                
                if (defaultLlhDir.exists()) {
                    File[] llhs = defaultLlhDir.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".md");
                        }
                    });

                    Scanner scanner;
                    for (File file : llhs) {
                        scanner = new Scanner(file);
                        String content = "";
                        while (scanner.hasNextLine()) {
                            content += scanner.nextLine();
                            content += "\n";
                        }
                        scanner.close();
                        scanner = null;
                        content = content.substring(0, content.length() - 1);
                        String fileName = file.getName().substring(0, file.getName()
                                .lastIndexOf('.'));
                        File llh = new File(FileManager.getTheory().getAbsolutePath() 
                                + FileManager.getSeparator() + fileName);
                        FileManager.createOrEditLlh(llh, content, CryptoManager
                                .convertStringToSecretKey(llcContent[0]), CryptoManager
                                .convertStringToIv(llcContent[1]));
                        file.delete();
                    }
                    for (File file : defaultLlhDir.listFiles()) {
                        File dest = new File(FileManager.getImages().getAbsolutePath()
                                + FileManager.getSeparator() + file.getName());
                        FileManager.addOrReplaceImage(file, dest);
                        file.delete();
                    }
                    
                    for (File file : defaultLleDir.listFiles()) {
                        scanner = new Scanner(file);
                        String content = "";
                        while (scanner.hasNextLine()) {
                            content += scanner.nextLine();
                            content += "\n";
                        }
                        scanner.close();
                        scanner = null;
                        content = content.substring(0, content.length() - 1);
                        String fileName = file.getName().substring(0, file.getName()
                                .indexOf("."));
                        File lle = new File(FileManager.getTests().getAbsolutePath()
                                + FileManager.getSeparator() + fileName);
                        FileManager.createOrEditLle(lle, content, CryptoManager
                                .convertStringToSecretKey(llcContent[0]), CryptoManager
                                .convertStringToIv(llcContent[1]));
                        file.delete();
                    }
                    
                    defaultLlhDir.delete();
                    defaultLleDir.delete();
                    File defaults = new File(System.getProperty("user.dir") + separator + "defaults");
                    defaults.delete();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Ошибка при создании файлов. Переустановите программу");
                    alert.showAndWait();
                }
            }
            
            if (code == 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Некоторые необходимые для работы программы файлы отсутствуют."
                        + "Переустановите программу");
            }
            isSuccessfullyAuth = false;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException
                | NoSuchPaddingException | InvalidAlgorithmParameterException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
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
        if (isSuccessfullyAuth) {
            App.setRoot("edit");
        }
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