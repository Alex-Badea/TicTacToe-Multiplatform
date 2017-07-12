package classes.handlers.database.misc;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Created by balex on 24.05.2017.
 * Clasa ce contine o metoda de criptare a parolei, folosita in general pentru o baza de date
 */
public abstract class Encryptor {
    public static String encrypt(String data, String keyValue) {
        try {
            Key key = new SecretKeySpec(keyValue.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return new BASE64Encoder().encode(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
