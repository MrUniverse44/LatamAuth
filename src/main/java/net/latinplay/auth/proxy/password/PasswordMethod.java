package net.latinplay.auth.proxy.password;

import me.blueslime.bungeeutilitiesapi.utils.consumer.PluginConsumer;

import java.util.Locale;

public enum PasswordMethod {
    BCRYPT,
    ARGON2;

    public static PasswordMethod fromString(String parameter) {
        return PluginConsumer.ofUnchecked(
            () -> PasswordMethod.valueOf(parameter.toUpperCase(Locale.ENGLISH)),
            e -> {},
            () -> ARGON2
        );
    }
}
