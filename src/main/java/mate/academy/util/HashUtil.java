package mate.academy.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashUtil {
    private static final String PBKDF_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;

    private HashUtil() {
    }

    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF_ALGORITHM);
            byte[] bytes = factory.generateSecret(spec).getEncoded();
            StringBuilder hashedPassword = new StringBuilder();
            for (byte b : bytes) {
                hashedPassword.append(String.format("%02x", b));
            }
            return hashedPassword.toString();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Can't hash the password: " + password, e);
        }
    }
}
