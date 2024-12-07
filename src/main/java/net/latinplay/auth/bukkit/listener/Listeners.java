package net.latinplay.auth.bukkit.listener;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public interface Listeners extends Listener, AdvancedModule {

    default FileConfiguration getSettings() {
        return fetch(FileConfiguration.class, "settings.yml");
    }

}
