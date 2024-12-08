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
        // Registered gson
        registerImpl(Gson.class, new Gson(), true);
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (messagesFile.exists() && FileInstaller.isFileEmpty(messagesFile)) {
            // This is used for empty files
            messagesFile.delete();
        }
        // Overwrite settings.yml implement
        registerImpl(
            Configuration.class,
            "messages.yml",
            load(
                new File(getDataFolder(), "messages.yml"),
                "proxy/messages.yml"
            )
        );
        File settingsFile = new File(getDataFolder(), "settings.yml");
        if (settingsFile.exists() && FileInstaller.isFileEmpty(settingsFile)) {
            // This is used for empty files
            settingsFile.delete();
        }
        // Overwrite settings.yml implement
        registerImpl(
            Configuration.class,
            "settings.yml",
            load(
                settingsFile,
                "proxy/settings.yml"
            )
        );
        // Register the executor pool for async things
        registerImpl(ExecutorService.class, new ForkJoinPool(4), true);

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

    @Override
    public void registerDatabases() {
        Configuration configuration = fetch(Configuration.class, "settings.yml");

        registerDatabase(
            new MongoDatabaseService(
                configuration.getString("settings.mongodb.uri"),
                configuration.getString("settings.mongodb.database"),
                RegistrationType.DOUBLE_REGISTER
            )
        );
    }
}
