package net.latinplay.auth.proxy.user.generator;

import me.blueslime.bungeeutilitiesapi.utils.consumer.PluginConsumer;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

public enum Generator {
    MOJANG,
    RANDOM,
    CRACKED;

    public static Generator fromString(String parameter) {
        return PluginConsumer.ofUnchecked(
            () -> Generator.valueOf(parameter.toUpperCase(Locale.ENGLISH)),
            e -> {},
            () -> MOJANG
        );
    }

    public UUID generate(String username, UUID premiumUUID) {
        switch (this) {
            case MOJANG -> {
                return premiumUUID != null ? premiumUUID : cracked(username);
            }
            case RANDOM -> {
                return UUID.randomUUID();
            }
            default -> {
                return cracked(username);
            }
        }
    }

    private UUID cracked(String username) {
        return UUID.nameUUIDFromBytes(
            ("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8)
        );
    }
}
