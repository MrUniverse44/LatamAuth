package net.latinplay.auth.bukkit;

import com.google.gson.Gson;

import me.blueslime.bukkitmeteor.BukkitMeteorPlugin;
import me.blueslime.bukkitmeteor.implementation.Implementer;

import net.latinplay.auth.bukkit.commands.EditModeCommand;
import net.latinplay.auth.bukkit.services.ListenerService;
import net.latinplay.auth.bukkit.services.ProxyMessageService;

import net.latinplay.auth.utils.file.FileInstaller;
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
        // Creates messages file instance
        File messagesFile = new File(getDataFolder(), "messages.yml");
        // Checks if the file is empty
        if (messagesFile.exists() && FileInstaller.isFileEmpty(messagesFile)) {
            // This is used for empty files
            messagesFile.delete();
        }
        // Creates a messages.yml implement
        registerImpl(
            FileConfiguration.class,
            "messages.yml",
            load(
                messagesFile,
                "bukkit/messages.yml"
            )
        );
        // Creates settings file instance
        File settingsFile = new File(getDataFolder(), "settings.yml");
        // Checks if a file is empty
        if (settingsFile.exists() && FileInstaller.isFileEmpty(settingsFile)) {
            // This is used for empty files
            settingsFile.delete();
        }
        // Overwrite settings.yml implement
        registerImpl(
            FileConfiguration.class,
            "settings.yml",
            load(
                settingsFile,
                "bukkit/settings.yml"
            )
        );
        EditModeCommand command = createInstance(EditModeCommand.class);
        registerImpl(
            EditModeCommand.class,
            command,
            true
        );
        command.register();

        registerModule(
            ProxyMessageService.class,
            ListenerService.class
        );
    }

}
