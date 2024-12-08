package net.latinplay.auth.proxy.services;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;

import net.latinplay.auth.proxy.password.PasswordMethod;
import net.latinplay.auth.proxy.password.object.PasswordProvider;
import net.latinplay.auth.proxy.password.providers.ArgonProvider;
import net.latinplay.auth.proxy.password.providers.BCryptProvider;
import net.latinplay.auth.proxy.password.providers.ScryptProvider;

import net.md_5.bungee.config.Configuration;

import java.util.EnumMap;
import java.util.Map;

public class PasswordService implements AdvancedModule {
    private final Map<PasswordMethod, PasswordProvider> passwordProviderMap = new EnumMap<>(PasswordMethod.class);

    @Override
    public void initialize() {
        passwordProviderMap.put(PasswordMethod.ARGON2, new ArgonProvider());
        passwordProviderMap.put(PasswordMethod.BCRYPT, new BCryptProvider());
        passwordProviderMap.put(PasswordMethod.SCRYPT, new ScryptProvider());
    }

    public PasswordProvider fetchProvider() {
        PasswordMethod method = PasswordMethod.fromString(
            fetch(Configuration.class, "settings.yml").getString(
                "settings.password-provider",
                "ARGON2"
            )
        );

        return passwordProviderMap.get(method);
    }
}
