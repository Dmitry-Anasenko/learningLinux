package ru.hiik.learninglinux;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Класс для работы с файлами
 * Файлы с теорией имеют расширение llh, тестами - lle, конфигурационный файл - llc
 * Для защиты файлов с теорией и тестами используется шифрование AES (класс CryptoManager)
 * @author dmitry
 */
public class FileManager {
    // Путь к рабочему каталогу
    private static final String path = System.getProperty("user.dir");
    // Определяет разделитель в файловой системе
    private static final String separator = System.getProperty("file.separator");
    // Инициализация каталога с теорией
    private static final File theory = new File(path + separator + "theory");
    // Инициализация каталога с тестами
    private static final File tests = new File(path + separator + "tests");
    // Инициализация каталога с изображениями
    private static final File images = new File(theory + separator + "images");
    // Инициализация файла конфигурации
    private static final File config = new File(path + separator + "config.llc");
    
    /**
     * Создание или редактирование файла с теорией
     * @param file Файл для записи. Если не существует, то будет создан
     * @param content Содержимое файла
     * @param key Ключ шифрования
     * @param iv Вектор инициализации
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static void createOrEditLlh(File file, String content, SecretKey key, IvParameterSpec iv)
            throws IOException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        String encryptedContent = CryptoManager.encrypt(content, key, iv);
        File fileToWrite;
        if (file.exists())
            fileToWrite = file;
        else {
            fileToWrite = new File(file.getAbsolutePath() + ".llh");
            fileToWrite.createNewFile();
        }
        writeFile(fileToWrite, encryptedContent);
    }
    
    /**
     * Создание или редактирование файла с тестом
     * @param file Файл для записи. Если не существует, то будет создан
     * @param content Содержимое файла
     * @param key Ключ шифрования
     * @param iv Вектор инициализации
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static void createOrEditLle(File file, String content, SecretKey key, IvParameterSpec iv)
            throws IOException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        String encryptedContent = CryptoManager.encrypt(content, key, iv);
        File fileToWrite;
        if (file.exists())
            fileToWrite = file;
        else {
            fileToWrite = new File(file.getAbsolutePath() + ".lle");
            fileToWrite.createNewFile();
        }
        writeFile(fileToWrite, encryptedContent);
    }
    
    /**
     * Чтение файла с теорией
     * @param file Файл для чтения
     * @param key Ключ шифрования
     * @param iv Вектор инициализации
     * @return Считанное и расшифрованное содержимое файла
     * @throws FileNotFoundException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException 
     */
    public static String readLlh(File file, SecretKey key, IvParameterSpec iv)
            throws FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        String content = readFile(file);
        return CryptoManager.decrypt(content, key, iv);
    }
    
    /**
     * Чтение файла с тестом
     * @param file Файл для чтения
     * @param key Ключ шифрования
     * @param iv Вектор инициализации
     * @return Считанное и расшифрованное содержимое файла
     * @throws FileNotFoundException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException 
     */
    public static String readLle(File file, SecretKey key, IvParameterSpec iv)
            throws FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        String content = readFile(file);
        return CryptoManager.decrypt(content, key, iv);
    }
    
    /**
     * Удаление файла с теорией
     * @param file Файл для удаления
     */
    public static void removeLlh(File file) {
        removeFile(file);
    }
    
    /**
     * Удаление файла с тестом
     * @param file Файл для удаления
     */
    public static void removeLle(File file) {
        removeFile(file);
    }
    
    /**
     * Получение массива файлов с теорией
     * @return Массив файлов с теорией
     */
    public static File[] getLlhs() {
        File[] llhs = theory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".llh");
            }
        });
        return llhs;
    }
    
    /**
     * Получение массива файлов с тестами
     * @return Массив файлов с тестами
     */
    public static File[] getLles() {
        return tests.listFiles();
    }
    
    /**
     * Добавление или изменение изображения
     * @param src Файл-источник изображения
     * @param dest Файл для создания изображения
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void addOrReplaceImage(File src, File dest) throws FileNotFoundException,
            IOException {
        copyFile(src, dest);
    }
    
    /**
     * Получение изображения из указанного файла
     * @param file Изображение в виде файла
     * @return Изображение в виде файлового потока
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static InputStream getImage(File file) throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(file);
        return is;
    }
    
    /**
     * Удаление изображения
     * @param file Файл для удаления
     */
    public static void removeImage(File file) {
        removeFile(file);
    }
    
    /**
     * Метод для первого запуска программы (создает файлы по умолчанию)
     * @return False, если не существуют какие-либо каталоги
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static byte firstRun() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if ((theory.exists()) && (tests.exists()) && (config.exists())) {
            // Если каталоги существуют, то никаких действий не производится
            return 1;
        }
        if ((!theory.exists()) && (!tests.exists()) && (!config.exists())) {
            deleteDirectory(theory);
            deleteDirectory(tests);
            config.delete();
            // Создание каталогов с теорией и тестами и конфигурационного файла
            theory.mkdir();
            tests.mkdir();
            images.mkdir();
            config.createNewFile();
            // Запись данных в конфигурационный файл
            // Пароль по умолчанию - LinuxIsThePower (должен быть заменен)
            String key = CryptoManager.convertSecretKeyToString(CryptoManager
                    .getKeyFromPassword("LinuxIsThePower", "07041969"));
            String iv = CryptoManager.convertIvToString(CryptoManager.generateIv());
            String configContent =  key + "\n" + iv;
            writeLlc(configContent);
            return 0;
        }
        return 2;
    }
    
    /**
     * Проверка пароля
     * @param password Пароль для проверки
     * @return True - если ключ, полученный путем преобразования пароля, совпадает с ключом, записанном
     * в файле config.llc
     * @throws FileNotFoundException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static boolean checkPassword(String password) throws FileNotFoundException,
            NoSuchAlgorithmException, InvalidKeySpecException {
        Scanner scanner = new Scanner(config);
        String passwordFromFile = scanner.nextLine();
        String key = CryptoManager.convertSecretKeyToString(
                CryptoManager.getKeyFromPassword(password, "07041969"));
        if (key.equals(passwordFromFile))
            return true;
        return false;
    }
    
    /**
     * Получение информации из файла конфигурации
     * @return Массив строк. Первый элемент - ключ шифрования, второй - вектор инициализации
     * @throws FileNotFoundException
     */
    public static String[] readLlc() throws FileNotFoundException {
        String content = readFile(config);
        String[] result = new String[2];
        result[0] = content.substring(0, content.indexOf("\n"));
        result[1] = content.substring(content.indexOf("\n"));
        return result;
    }
    
    /**
     * Запись в файл конфигурации
     * @param content
     * @throws IOException 
     */
    public static void writeLlc(String content) throws IOException {
        writeFile(config, content);
    }
    
    /**
     * Запись в файл
     * @param file Файл для записи
     * @param content Содержимое файла для записи
     * @throws IOException
     */
    private static void writeFile(File file, String content) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.flush();
        fw.close();
    }
    
    /**
     * Чтение из файла
     * @param file Файл для чтения
     * @return Полученное содержимое файла
     * @throws FileNotFoundException 
     */
    private static String readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String content = "";
        while (scanner.hasNextLine()) {
            content += scanner.nextLine();
            content += "\n";
        }
        scanner.close();
        return content.substring(0, content.length() - 1);
    }
    
    /**
     * Копирование файла. Используется для добавления изображения или для создания дефолтных файлов
     * @param src
     * @param dest
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private static void copyFile(File src, File dest) throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        while((length = is.read(buffer)) > 0)
            os.write(buffer, 0, length);
        is.close();
        os.close();
    }
    
    /**
     * Удаление файла
     * @param file Файл для удаления
     */
    private static void removeFile(File file) {
        file.delete();
    }
    
    /**
     * Удаление каталога (рекурсивное)
     * @param directoryToBeDeleted Каталог для удаления
     * @return 
     */
    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
    
    public static void reencryptLlh(File file, SecretKey oldKey, SecretKey newKey, IvParameterSpec oldIv,
            IvParameterSpec newIv) throws
            FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, IOException {
        String content = readLlh(file, oldKey, oldIv);
        createOrEditLlh(file, content, newKey, newIv);
    }
    
    public static void reencryptLle(File file, SecretKey oldKey, SecretKey newKey, IvParameterSpec oldIv,
            IvParameterSpec newIv) throws
            FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, IOException {
        String content = readLle(file, oldKey, oldIv);
        createOrEditLle(file, content, newKey, newIv);
    }

    public static String getSeparator() {
        return separator;
    }

    public static File getTheory() {
        return theory;
    }

    public static File getTests() {
        return tests;
    }

    public static File getImages() {
        return images;
    }

    public static File getConfig() {
        return config;
    }
    
}