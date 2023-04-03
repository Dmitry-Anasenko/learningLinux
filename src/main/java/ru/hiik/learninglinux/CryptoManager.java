package ru.hiik.learninglinux;

import java.nio.charset.CharacterCodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Класс для работы с шифрованием
 * @author dmitry
 */
public class CryptoManager {
    
    /**
     * Преобразование пароля в ключ шифрования длиной 256 символов
     * @param password Пароль
     * @param salt Случайно выбранное значение
     * @return Ключ шифрования
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static SecretKey getKeyFromPassword(String password, String salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
    
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(),
                65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
            .getEncoded(), "AES");
        return secret;
    }
    
    /**
     * Генерация вектора инициализации
     * @return Вектор инициализации
     */
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    
    /**
     * Шифрование текста
     * @param plainText Исходный текст
     * @param key Ключ шифрования
     * @param iv Вектор инициализации
     * @return Текст в зашифрованном виде
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException 
     */
    public static String encrypt(String plainText, SecretKey key, IvParameterSpec iv)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
        InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return Base64.getEncoder()
            .encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    /**
     * Дешифрование текста
     * @param cipherText Текст в зашифрованном виде
     * @param key Ключ шифрования
     * @param iv Вектор инициализации
     * @return Расшифрованный текст
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException 
     */
    public static String decrypt(String cipherText, SecretKey key, IvParameterSpec iv)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
        InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(Base64.getDecoder()
            .decode(cipherText)));
    }
    
    /**
     * Представление ключа шифрования в виде строки
     * @param key Ключ шифрования (экземпляр класса SecretKey)
     * @return Ключ в строчном представлении
     */
    public static String convertSecretKeyToString(SecretKey key) {
        byte[] rawData = key.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }
    
    /**
     * Преобразование строки в ключ шифрования
     * @param encodedKey Ключ в строчном представлении
     * @return Ключ шифрования (экземпляр класса SecretKey)
     */
    public static SecretKey convertStringToSecretKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return key;
    }
    
    public static String convertIvToString(IvParameterSpec iv) throws CharacterCodingException {
        byte[] rawData = iv.getIV();
        String encodedIv = Base64.getEncoder().encodeToString(rawData);
        return encodedIv;
    }
    
    public static IvParameterSpec convertStringToIv(String encodedIv) {
        byte[] decodedIv = encodedIv.substring(0, 16).getBytes();
        IvParameterSpec iv = new IvParameterSpec(decodedIv);
        return iv;
    }
    
}
