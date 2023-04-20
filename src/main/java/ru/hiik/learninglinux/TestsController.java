package ru.hiik.learninglinux;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
public class TestsController implements Initializable {

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private GridPane fileSelector;
    @FXML
    private GridPane tester;
    @FXML
    private Label question;
    @FXML
    private CheckBox cb1;
    @FXML
    private CheckBox cb2;
    @FXML
    private CheckBox cb3;
    @FXML
    private CheckBox cb4;
    private SecretKey key;
    private IvParameterSpec iv;
    private String buffer;
    private Scanner scanner;
    // Количество вопросов
    private int questionsQuantity = 0;
    // Количество правильных ответов. Если ответ частично правильный, то используется дробное число
    private float correctAnswers = 0;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            App.stg.setTitle("Learning Linux - тесты");
            // Заполнение списка элемента ComboBox
            File[] files = FileManager.getLles();
            ObservableList<String> ol = FXCollections.observableArrayList();
            for (int i = 0; i < files.length; i++) {
                ol.add(files[i].getName());
            }
            comboBox.setItems(ol);
            String[] llcContent = FileManager.readLlc();
            key = CryptoManager.convertStringToSecretKey(llcContent[0]);
            iv = CryptoManager.convertStringToIv(llcContent[1]);
        } catch (FileNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void processSelection() {
        try {
            if (comboBox.getValue() == null)
                return;
            String theme = comboBox.getValue();
            File file = new File(FileManager.getTests() + FileManager.getSeparator() + theme);
            buffer = FileManager.readLle(file, key, iv);
            fileSelector.setVisible(false);
            tester.setVisible(true);
            scanner = new Scanner(buffer);
            firstQuestion();
        } catch (FileNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException |
                InvalidAlgorithmParameterException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getLocalizedMessage());
            alert.showAndWait();
        }
    }
    
    private void firstQuestion() {
        fillTester();
        questionsQuantity += 1;
    }
    
    
    /**
     * Сравнение данных ответов с правильными, запись результата и переключение на следующий вопрос
     */
    @FXML
    private void nextQuestion() {
        // Запись правильных ответов из файла в буфер
        char[] answersFromFile = scanner.nextLine().toCharArray();
        boolean[] convertedAnswersFromFile = {false, false, false, false};
        for (int i = 0; i < answersFromFile.length; i++)
            switch (answersFromFile[i]) {
                case '1':
                    convertedAnswersFromFile[0] = true;
                    break;
                case '2':
                    convertedAnswersFromFile[1] = true;
                    break;
                case '3':
                    convertedAnswersFromFile[2] = true;
                    break;
                case '4':
                    convertedAnswersFromFile[3] = true;
                    break;
            }
        // Запись данных ответов в буфер
        boolean[] givenAnswers = {cb1.isSelected(), cb2.isSelected(), cb3.isSelected(), cb4.isSelected()};
        // Подсчет количества правильных ответов из файла
        float correctAnswersFromFile = 0;
        // Подсчет количества выбранных ответов
        float correctAnswersFromGiven = 0;
        for (int i = 0; i < 4; i++) {
            if (convertedAnswersFromFile[i])
                correctAnswersFromFile++;
            if (givenAnswers[i])
                correctAnswersFromGiven++;
        }
        // Подсчет баллов
        for (int i = 0; i < 4; i++) {
            if (givenAnswers[i])
                if (givenAnswers[i] == convertedAnswersFromFile[i]) {
                    if (correctAnswersFromGiven <= correctAnswersFromFile)
                        correctAnswers +=  correctAnswersFromGiven / correctAnswersFromFile;
                    else
                        correctAnswers += correctAnswersFromFile / correctAnswersFromGiven;
                }
        }
        questionsQuantity += 1;
        // Переключение на следующий вопрос или завершение теста
        if (scanner.hasNextLine()) {
            scanner.nextLine();
            fillTester();
        }
        else {
            int mark = 2;
            if ((correctAnswers / questionsQuantity) > 0.5)
                mark = 3;
            if ((correctAnswers / questionsQuantity) > 0.7)
                mark = 4;
            if ((correctAnswers / questionsQuantity) > 0.85)
                mark = 5;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Результаты");
            alert.setContentText("Общее количество вопросов: " + questionsQuantity +
                    "\nКоличество набранных баллов: " + correctAnswers + "\nОценка: " + mark);
            alert.showAndWait();
            question.setText(null);
            cb1.setText(null);
            cb1.setSelected(false);
            cb2.setText(null);
            cb2.setSelected(false);
            cb3.setText(null);
            cb3.setSelected(false);
            cb4.setText(null);
            cb4.setSelected(false);
            fileSelector.setVisible(true);
            tester.setVisible(false);
            comboBox.setValue(null);
            questionsQuantity = 0;
            correctAnswers = 0;
        }
    }
    
    /**
     * Заполнение тестера (вопрос и варианты ответа)
     */
    private void fillTester() {
        question.setText(scanner.nextLine());
        cb1.setText(scanner.nextLine());
        cb1.setSelected(false);
        cb2.setText(scanner.nextLine());
        cb2.setSelected(false);
        cb3.setText(scanner.nextLine());
        cb3.setSelected(false);
        cb4.setText(scanner.nextLine());
        cb4.setSelected(false);
    }
    
    @FXML
    private void cancelTest() {
        
    }
    
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    
}
