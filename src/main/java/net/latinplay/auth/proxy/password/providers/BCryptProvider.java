package net.latinplay.auth.proxy.password.providers;

import net.latinplay.auth.proxy.password.object.PasswordProvider;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptProvider extends PasswordProvider {

    /**
     * Creates a hash with password.
     *
     * @param password text.
     * @return hashed password.
     */
    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
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
        return BCrypt.checkpw(password, hashedPassword);
    }

}
