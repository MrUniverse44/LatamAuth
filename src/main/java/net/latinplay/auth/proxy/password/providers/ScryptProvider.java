package net.latinplay.auth.proxy.password.providers;

import net.latinplay.auth.proxy.password.object.PasswordProvider;
import org.bouncycastle.crypto.generators.SCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class ScryptProvider extends PasswordProvider {

    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 32;
    private static final int N = 16384;
    private static final int R = 8;
    private static final int P = 1;

    /**
     * Creates a hash with password.
     *
     * @param password text.
     * @return hashed password.
     */
    @Override
    public String hashPassword(String password) {
        SecureRandom random = new SecureRandom();

        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        byte[] hash = SCrypt.generate(
            password.getBytes(StandardCharsets.UTF_8),
            salt,
            N, R, P,
            KEY_LENGTH
        );

        return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verify if a password is correct with the saved password.
     *
     * @param password       text
     * @param hashedPassword hash in storage
     * @return True if this password is correct, false if not
     */
    @Override
    public boolean verifyPassword(String password, String hashedPassword) {
        String[] parts = hashedPassword.split("\\$");
        if (parts.length != 2) return false;

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

        byte[] actualHash = SCrypt.generate(
            password.getBytes(StandardCharsets.UTF_8),
            salt,
            N, R, P,
            KEY_LENGTH
        );

        return MessageDigest.isEqual(expectedHash, actualHash);
    }

}
