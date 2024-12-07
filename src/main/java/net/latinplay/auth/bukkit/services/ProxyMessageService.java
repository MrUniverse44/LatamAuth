package net.latinplay.auth.bukkit.services;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.bukkit.LatamAuth;

public class ProxyMessageService implements AdvancedModule {

    @Override
    public void initialize() {
        LatamAuth plugin = fetch(LatamAuth.class);

        /*

        NOT REGISTERED CHANNEL

        plugin.getServer().getMessenger().registerIncomingPluginChannel(
            plugin,
            "latam:" + fetch(FileConfiguration.class, "settings.yml").getString("settings.verification-channel", "verify"),
            new ProxyChannelListener()
        );

         */
    }

}
