package ru.hiik.learninglinux;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
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
public class EditController extends Thread implements Initializable {

    @FXML
    private Tab pass;
    @FXML
    private GridPane llhSelector;
    @FXML
    private GridPane llhEditor;
    @FXML
    private TextArea llhEditorText;
    @FXML
    private WebView llhEditorRenderer;
    @FXML
    private GridPane lleSelector;
    @FXML
    private GridPane lleEditor;
    @FXML
    private TextField questionField;
    @FXML
    private CheckBox cb1;
    @FXML
    private TextField answer1;
    @FXML
    private CheckBox cb2;
    @FXML
    private TextField answer2;
    @FXML
    private CheckBox cb3;
    @FXML
    private TextField answer3;
    @FXML
    private CheckBox cb4;
    @FXML
    private TextField answer4;
    @FXML
    private GridPane imageSelector;
    @FXML
    private GridPane imageViewer;
    @FXML
    private ImageView imagePreview;
    @FXML
    private PasswordField oldPass;
    @FXML
    private PasswordField newPass;
    @FXML
    private PasswordField newPassAgain;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label progressInfo;
    // Диалоговое окно создания или выбора файла
    private final FileChooser fileChooser = new FileChooser();
    // Данные для шифрования и дешифрования
    private SecretKey key;
    private IvParameterSpec iv;
    // Открытый файл llh
    private File openedLlh;
    // Открытый файл lle
    private File openedLle;
    // Открытый файл с изображением
    private File openedImage;
    
    private WebEngine webEngine;
    // Буфер для записи тестов в файл
    private String bufferToWrite = "";
    // Буфер для чтения тестов из файла
    private String bufferToRead;
    private Scanner scanner;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            App.stg.setTitle("Learning Linux - редактирование");
            String[] llcContent = FileManager.readLlc();
            key = CryptoManager.convertStringToSecretKey(llcContent[0]);
            iv = CryptoManager.convertStringToIv(llcContent[1]);
            webEngine = llhEditorRenderer.getEngine();
        } catch (FileNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void createLlh() {
        llhSelector.setVisible(false);
        llhEditor.setVisible(true);
    }
    
    @FXML
    private void selectLlh() {
        try {
            openedLlh = null;
            fileChooser.setInitialDirectory(FileManager.getTheory());
            openedLlh = fileChooser.showOpenDialog(App.stg);
            if (openedLlh == null)
                return;
            llhSelector.setVisible(false);
            llhEditor.setVisible(true);
            llhEditorText.setText(FileManager.readLlh(openedLlh, key, iv));
        } catch (FileNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException |
                InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void removeLlh() {
        openedLlh = null;
        fileChooser.setInitialDirectory(FileManager.getTheory());
        openedLlh = fileChooser.showOpenDialog(App.stg);
        if (openedLlh == null)
            return;
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Удалить файл?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null  || option.get() == ButtonType.CANCEL) {
                return;
            }
            else if (option.get() == ButtonType.OK) {
                FileManager.removeLlh(openedLlh);
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setContentText("Файл удален");
                info.showAndWait();
            }
        }
    }
    
    @FXML
    private void preview() {
        String content = llhEditorText.getText();
        webEngine.loadContent(MarkdownParser.parse(content));
    }
    
    @FXML
    private void saveLlh() {
        try {
            if (llhEditorText.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Текстовое поле не заполнено");
                alert.showAndWait();
                return;
            }
            if (openedLlh != null) {
                    FileManager.createOrEditLlh(openedLlh, llhEditorText.getText(),
                            key, iv);
                    openedLlh = null;
                }
                else {
                    fileChooser.setInitialDirectory(FileManager.getTheory());
                    openedLlh = fileChooser.showSaveDialog(App.stg);
                    if (openedLlh == null)
                        return;
                    FileManager.createOrEditLlh(openedLlh, llhEditorText.getText(),
                            key, iv);
                    openedLlh = null;
                }
                llhEditorText.setText(null);
                webEngine.load(null);
                llhEditor.setVisible(false);
                llhSelector.setVisible(true);
        }
        catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException |
                BadPaddingException | InvalidAlgorithmParameterException |
                InvalidKeyException | IllegalBlockSizeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void cancelLlh() {
        llhEditorText.setText(null);
        webEngine.loadContent("");
        llhEditor.setVisible(false);
        llhSelector.setVisible(true);
    }
    
    @FXML
    private void createLle() {
        lleSelector.setVisible(false);
        lleEditor.setVisible(true);
    }
    
    @FXML
    private void selectLle() {
        try {
            openedLle = null;
            fileChooser.setInitialDirectory(FileManager.getTests());
            openedLle = fileChooser.showOpenDialog(App.stg);
            if (openedLle == null)
                return;
            bufferToRead = FileManager.readLle(openedLle, key, iv);
            scanner = new Scanner(bufferToRead);
            lleSelector.setVisible(false);
            lleEditor.setVisible(true);
            fillLleEditor();
        } catch (FileNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException |
                InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void removeLle() {
        openedLle = null;
        fileChooser.setInitialDirectory(FileManager.getTests());
        openedLle = fileChooser.showOpenDialog(App.stg);
        if (openedLle == null)
            return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Удалить файл?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null  || option.get() == ButtonType.CANCEL)
            return;
        else if (option.get() == ButtonType.OK) {
            FileManager.removeLle(openedLle);
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setContentText("Файл удален");
            info.showAndWait();
        }
    }
    
    @FXML
    private void saveLle() {
        try {
            if (nextQuestion()) {
                bufferToWrite = bufferToWrite.substring(0, bufferToWrite.length() - 1);
                if (openedLle != null) {
                    FileManager.createOrEditLle(openedLle, bufferToWrite, key, iv);
                    openedLle = null;
                }
                else {
                    fileChooser.setInitialDirectory(FileManager.getTests());
                    openedLle = fileChooser.showSaveDialog(App.stg);
                    if (openedLle == null)
                        return;
                    FileManager.createOrEditLle(openedLle, bufferToWrite, key, iv);
                    openedLle = null;
                }
                questionField.setText(null);
                answer1.setText(null);
                cb1.setSelected(false);
                answer2.setText(null);
                cb2.setSelected(false);
                answer3.setText(null);
                cb3.setSelected(false);
                answer4.setText(null);
                cb4.setSelected(false);
                lleEditor.setVisible(false);
                lleSelector.setVisible(true);
                bufferToRead = null;
                scanner = null;
            }
        }
        catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException |
                BadPaddingException | InvalidAlgorithmParameterException |
                InvalidKeyException | IllegalBlockSizeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private boolean nextQuestion() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Не все поля заполнены");
        if (questionField.getText() == null || answer1.getText() == null || answer2.getText() == null
                || answer3.getText() == null || answer4.getText() == null) {
            alert.showAndWait();
            return false;
        }
        if (!cb1.isSelected() && !cb2.isSelected() && !cb3.isSelected() && !cb4.isSelected()) {
            alert.showAndWait();
            return false;
        }
        fillLleEditor();
        return true;
    }
    
    private void fillLleEditor() {
        if (scanner == null || !scanner.hasNextLine()) {
            bufferToWrite += questionField.getText() + "\n" + answer1.getText() + "\n" +
                answer2.getText() + "\n" + answer3.getText() + "\n" + answer4.getText() + "\n";
            if (cb1.isSelected())
                bufferToWrite += "1";
            if (cb2.isSelected())
                bufferToWrite += "2";
            if (cb3.isSelected())
                bufferToWrite += "3";
            if (cb4.isSelected())
                bufferToWrite += "4";
            bufferToWrite += "\n\n";
            questionField.setText(null);
            answer1.setText(null);
            answer2.setText(null);
            answer3.setText(null);
            answer4.setText(null);
            cb1.setSelected(false);
            cb2.setSelected(false);
            cb3.setSelected(false);
            cb4.setSelected(false);
        }
        else {
            bufferToWrite += questionField.getText() + "\n" + answer1.getText() + "\n" +
                answer2.getText() + "\n" + answer3.getText() + "\n" + answer4.getText() + "\n";
            if (cb1.isSelected())
                bufferToWrite += "1";
            if (cb2.isSelected())
                bufferToWrite += "2";
            if (cb3.isSelected())
                bufferToWrite += "3";
            if (cb4.isSelected())
                bufferToWrite += "4";
            cb1.setSelected(false);
            cb2.setSelected(false);
            cb3.setSelected(false);
            cb4.setSelected(false);
            questionField.setText(scanner.nextLine());
            answer1.setText(scanner.nextLine());
            answer2.setText(scanner.nextLine());
            answer3.setText(scanner.nextLine());
            answer4.setText(scanner.nextLine());
            String correctAnswers = scanner.nextLine();
            if (correctAnswers.contains("1"))
                cb1.setSelected(true);
            if (correctAnswers.contains("2"))
                cb2.setSelected(true);
            if (correctAnswers.contains("3"))
                cb3.setSelected(true);
            if (correctAnswers.contains("4"))
               cb4.setSelected(true);
            scanner.nextLine();
        }
    }
    
    @FXML
    private void cancelLle() {
        questionField.setText(null);
        answer1.setText(null);
        cb1.setSelected(false);
        answer2.setText(null);
        cb2.setSelected(false);
        answer3.setText(null);
        cb3.setSelected(false);
        answer4.setText(null);
        cb4.setSelected(false);
        lleEditor.setVisible(false);
        lleSelector.setVisible(true);
        bufferToRead = null;
        scanner = null;
    }
    
    @FXML
    private void addImage() {
        try {
            openedImage = null;
            File home = new File(System.getProperty("user.home"));
            fileChooser.setInitialDirectory(home);
            openedImage = fileChooser.showOpenDialog(App.stg);
            if (openedImage == null)
                return;
            imageSelector.setVisible(false);
            imageViewer.setVisible(true);
            Image image = new Image(FileManager.getImage(openedImage));
            imagePreview.setImage(image);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void removeImage() {
        fileChooser.setInitialDirectory(FileManager.getImages());
        File file = fileChooser.showOpenDialog(App.stg);
        if (file == null)
            return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Удалить файл?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null  || option.get() == ButtonType.CANCEL)
            return;
        else if (option.get() == ButtonType.OK) {
            FileManager.removeImage(file);
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setContentText("Файл удален");
            info.showAndWait();
        }
    }
    
    @FXML
    private void saveImage() {
        try {
            fileChooser.setInitialDirectory(FileManager.getImages());
            File dest = fileChooser.showSaveDialog(App.stg);
            if (dest == null)
                return;
            FileManager.addOrReplaceImage(openedImage, dest);
            openedImage = null;
            imageViewer.setVisible(false);
            imageSelector.setVisible(true);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void cancelImage() {
        openedImage = null;
        imageViewer.setVisible(false);
        imageSelector.setVisible(true);
    }
    
    @FXML
    private void passwordWarning() {
        if (pass.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Процесс смены пароля может занимать значительное время "
                    + "в связи с необходимостью перешифрования всех сохраненных файлов");
            alert.showAndWait();
        }
        oldPass.setText(null);
        newPass.setText(null);
        newPassAgain.setText(null);
    }
    
    /**
     * Смена пароля
     */
    @FXML
    private void changePassword() {
        try {
            // Проверки введенных паролей
            if (oldPass.getText() == null || newPass.getText() == null || newPassAgain.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Не все поля заполнены");
                alert.showAndWait();
                return;
            }
            if (!FileManager.checkPassword(oldPass.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Неверный пароль");
                alert.showAndWait();
                return;
            }
            if (!newPass.getText().equals(newPassAgain.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Пароли не совпадают");
                alert.showAndWait();
                return;
            }
            // Запуск метода смены пароля в отдельном потоке
            start();
        } catch (FileNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
        
    }
    
    @Override
    public void run() {
        try {
            progressIndicator.setVisible(true);
            progressInfo.setVisible(true);
            SecretKey oldKey = key;
            SecretKey newKey = CryptoManager.getKeyFromPassword(newPass.getText(), "07041969");
            IvParameterSpec oldIv = iv;
            IvParameterSpec newIv = CryptoManager.generateIv();
            for (File file : FileManager.getLlhs())
                FileManager.reencryptLlh(file, oldKey, newKey, oldIv, newIv);
            for (File file : FileManager.getLles())
                FileManager.reencryptLle(file, oldKey, newKey, oldIv, newIv);
            String llcNewContent = CryptoManager.convertSecretKeyToString(newKey) + "\n" +
                    CryptoManager.convertIvToString(newIv);
            FileManager.writeLlc(llcNewContent);
            progressIndicator.setVisible(false);
            progressInfo.setVisible(false);
            key = newKey;
            iv = newIv;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException | IOException ex) {
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
