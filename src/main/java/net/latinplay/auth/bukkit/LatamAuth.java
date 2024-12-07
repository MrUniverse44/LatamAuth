package net.latinplay.auth.bukkit;

import com.google.gson.Gson;

import me.blueslime.bukkitmeteor.BukkitMeteorPlugin;
import me.blueslime.bukkitmeteor.implementation.Implementer;

import net.latinplay.auth.bukkit.services.ListenerService;
import net.latinplay.auth.bukkit.services.ProxyMessageService;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class LatamAuth extends BukkitMeteorPlugin implements Implementer {

    @Override
    public void onEnable() {
        initialize(this, false, false);
    }

    @Override
    public void registerModules() {
        // Registered gson
        registerImpl(Gson.class, new Gson(), true);
        // Overwrite settings.yml implement
        registerImpl(
            FileConfiguration.class,
            "messages.yml",
            load(
                new File(getDataFolder(), "messages.yml"),
                "bukkit/messages.yml"
            )
        );
        // Overwrite settings.yml implement
        registerImpl(
            FileConfiguration.class,
            "settings.yml",
            load(
                new File(getDataFolder(), "settings.yml"),
                "bukkit/settings.yml"
            )
        );

        registerModule(
            ProxyMessageService.class,
            ListenerService.class
        );
    }

}
