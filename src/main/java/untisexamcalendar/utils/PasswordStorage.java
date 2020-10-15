package untisexamcalendar.utils;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class PasswordStorage {

    public static void saveCredentials(String user, String key) {
        try {
            OutputStream output = new FileOutputStream("untiscredentials");
            Properties prop = new Properties();

            prop.setProperty("user", user);
            prop.setProperty("key", key);

            prop.store(output, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUser() {
        File f = new File("untiscredentials");

        if (f.exists()) {
            try {
                InputStream input = new FileInputStream(f);

                Properties prop = new Properties();
                prop.load(input);

                return prop.getProperty("user");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }

        return "";
    }

    public static String getKey() {
        File f = new File("untiscredentials");

        if (f.exists()) {
            try {
                InputStream input = new FileInputStream(f);

                Properties prop = new Properties();
                prop.load(input);

                return prop.getProperty("key");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }

        return "";
    }

}

