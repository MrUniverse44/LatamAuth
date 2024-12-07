package net.latinplay.auth.proxy.password.providers;

import net.latinplay.auth.proxy.password.object.PasswordProvider;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class ArgonProvider extends PasswordProvider {

    private final int HASH_LENGTH = 32;


    /**
     * Creates a hash with password.
     *
     * @param password text.
     * @return hashed password.
     */
    @Override
    public String hashPassword(String password) {
        SecureRandom random = new SecureRandom();

        int SALT_LENGTH = 16;

        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withSalt(salt)
            .withParallelism(1)
            .withMemoryAsKB(65536)
            .withIterations(3)
            .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);

        byte[] hash = new byte[HASH_LENGTH];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), hash);

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

        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withSalt(salt)
            .withParallelism(1)
            .withMemoryAsKB(65536)
            .withIterations(3)
            .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);

        byte[] actualHash = new byte[HASH_LENGTH];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), actualHash);

        return MessageDigest.isEqual(expectedHash, actualHash);
    }

}
