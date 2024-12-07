package net.latinplay.auth.proxy.password.object;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;

public abstract class PasswordProvider implements AdvancedModule {

    /**
     * Creates a hash with password.
     * @param password text.
     * @return hashed password.
     */
    public abstract String hashPassword(String password);

    /**
     * Verify if a password is correct with the saved password.
     * @param password text
     * @param hashedPassword hash in storage
     * @return True if this password is correct, false if not
     */
    public abstract boolean verifyPassword(String password, String hashedPassword);


}
