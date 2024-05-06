package com.impactqa.utilities;

import com.impactqa.exceptions.CustomRunTimeException;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

import static com.impactqa.utilities.SystemUtils.*;


/**
 *
 * @version 1.0
 * @description This class provides the facility for encryption and decryption of the Data
 * @since 2021-03-20
 */

public class CryptoUtils {
    private static SecretKey key;

    /**
     * This set SecretKey Object for Encryption and Decryption
     * this method takes Encryption key from environment variable ("EncryptionKey") for decryption of value
     */
    private static void setKey() {

        String encryptionKeyStr = "";//System.getenv("EncryptionKey"); //FrameworkConfig.getStringEnvProperty("encryptionKey");

        if (!CommonUtil.isValidString(encryptionKeyStr)) {
            File file = new File(PROJECT_DIRECTORY + FILE_SEPARATOR +"src/test/resources/EncryptionKey.txt");
            if(file.exists())
                try {
                    encryptionKeyStr = FileUtils.readFileToString(file, Charset.defaultCharset());
                }catch (Exception e){
                    new CustomRunTimeException(e.getMessage(), e);
                }
        }

        if (!CommonUtil.isValidString(encryptionKeyStr))
            throw new RuntimeException("Make sure you have set the 'EncryptionKey' in the environment variables.");

        if (key == null) {
            try {
                DESKeySpec keySpec = new DESKeySpec(encryptionKeyStr.getBytes(StandardCharsets.UTF_8));
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                key = keyFactory.generateSecret(keySpec);
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while creating the key instance of CryptoUtils. ", e);
            }
        }
    }

    /**
     * This method encrypts row data and returns encrypted value
     * This method uses Cipher type DES
     *
     * @param plainText Value the needed to encrypt
     * @return encrypted value
     */
    public static String encryptTheValue(String plainText) throws Exception {
        setKey();
        Base64.Encoder base64encoder = Base64.getEncoder();
        byte[] cleartext = plainText.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("DES");

        cipher.init(Cipher.ENCRYPT_MODE, key);
        return base64encoder.encodeToString(cipher.doFinal(cleartext));
    }

    /**
     * This method decrypts value and return decrypted value
     * This method uses Cipher type DES
     *
     * @param encryptedTextInput Value the needed to Decrypt
     * @return Decrypted value
     */
    public static String decryptTheValue(String encryptedTextInput) throws Exception {
        setKey();
        if (!CommonUtil.isValidString(encryptedTextInput))
            Assert.fail("Encrypted Text Input to be enter should not be null or empty");

        Base64.Decoder base64decoder = Base64.getDecoder();
        byte[] encryptedBytes = base64decoder.decode(encryptedTextInput);
        Cipher cipher1 = Cipher.getInstance("DES");
        cipher1.init(Cipher.DECRYPT_MODE, key);
        try {
            byte[] plainTextPwdBytes = new byte[0];
            plainTextPwdBytes = (cipher1.doFinal(encryptedBytes));
            return new String(plainTextPwdBytes);
        }catch (IllegalBlockSizeException | BadPaddingException e){
            throw new RuntimeException("Error occurred while decrypting the value. Please check the encryption key or encrypted value", e);
        }
    }

    /**
     * This method should be called from command line
     * This method used to encrypt the new value
     */
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.print("\n\nEncrypt value - 1\nDecrypt value - 2\nExit - press E\nEnter You choice : ");
            Scanner scanner = new Scanner(System.in);
            String value = scanner.nextLine();

            if (value.equals("1")) {
                System.out.print("Enter the value that has to be encrypted : ");
                String plainText = scanner.nextLine();
                System.out.println("Encrypted value : " + CryptoUtils.encryptTheValue(plainText));
            } else if (value.equals("2")) {
                System.out.print("Enter the value that has to be decrypted : ");
                String plainText = scanner.nextLine();
                System.out.println("Decrypted value : " + CryptoUtils.decryptTheValue(plainText));
            } else if (value.equalsIgnoreCase("E")) {
                break;
            }
        }
    }
}
