package net.latinplay.auth.proxy;

import com.google.gson.Gson;
import me.blueslime.bungeemeteor.BungeeMeteorPlugin;
import me.blueslime.bungeemeteor.implementation.Implementer;
import me.blueslime.bungeemeteor.storage.type.MongoDatabaseService;
import me.blueslime.bungeemeteor.storage.type.RegistrationType;
import net.latinplay.auth.proxy.services.*;
import net.latinplay.auth.utils.file.FileInstaller;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public final class LatamAuth extends BungeeMeteorPlugin implements Implementer {

    @Override
    public void onEnable() {
        initialize(this);
        info(
           "&6 ╭╮╱╱╱╱╱╭╮╱╱╱╱╱╱╭━━━╮╱╱╭╮╭╮",
           "&6 ┃┃╱╱╱╱╭╯╰╮╱╱╱╱╱┃╭━╮┃╱╭╯╰┫┃",
           "&6 ┃┃╱╱╭━┻╮╭╋━━┳╮╭┫┃╱┃┣╮┣╮╭┫╰━╮",
           "&6 ┃┃╱╭┫╭╮┃┃┃╭╮┃╰╯┃╰━╯┃┃┃┃┃┃╭╮┃",
           "&6 ┃╰━╯┃╭╮┃╰┫╭╮┃┃┃┃╭━╮┃╰╯┃╰┫┃┃┃",
           "&6 ╰━━━┻╯╰┻━┻╯╰┻┻┻┻╯╱╰┻━━┻━┻╯╰╯"
        );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void registerModules() {
        registerImpl(LatamAuth.class, this, true);
        registerModule(
            ConnectionService.class,
            IpService.class,
            FloodgateService.class,
            UserService.class,
            ServerService.class,
            ListenerService.class,
            PasswordService.class,
            CommandService.class
        ).finish();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void registerDatabases() {
        // Registered gson
        registerImpl(Gson.class, new Gson(), true);
        // Register SecureRandom
        registerImpl(SecureRandom.class, new SecureRandom(), true);
        // Creates the messages file instance
        File messagesFile = new File(getDataFolder(), "messages.yml");
        // This is used only to verify if a file is empty or not
        if (messagesFile.exists() && FileInstaller.isFileEmpty(messagesFile)) {
            // This is used for empty files
            messagesFile.delete();
        }
        // Creates a messages.yml implement
        registerImpl(
            Configuration.class,
            "messages.yml",
            load(
                new File(getDataFolder(), "messages.yml"),
                "/proxy/messages.yml"
            )
        );
        // Creates the settings file instance
        File settingsFile = new File(getDataFolder(), "settings.yml");
        // This is used only to verify if a file is empty or not
        if (settingsFile.exists() && FileInstaller.isFileEmpty(settingsFile)) {
            // This is used for empty files
            settingsFile.delete();
        }
        // Obtain Configuration instance
        Configuration settings = load(settingsFile, "/proxy/settings.yml");
        // Overwrite settings.yml implement
        registerImpl(
            Configuration.class,
            "settings.yml",
            settings
        );
        // Register the executor pool for async things
        registerImpl(ExecutorService.class, new ForkJoinPool(4), true);
        // Register database
        registerDatabase(
            new MongoDatabaseService(
                settings.getString("settings.mongodb.uri", ""),
                settings.getString("settings.mongodb.database", "LatamAuth"),
                RegistrationType.DOUBLE_REGISTER
            )
        );
    }
}
