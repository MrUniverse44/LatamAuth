package net.latinplay.auth.proxy.listeners.server;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;

import net.latinplay.auth.proxy.services.ServerService;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

import static net.md_5.bungee.event.EventPriority.LOW;

public class ServerKickListener implements Listener, AdvancedModule {

    @EventHandler(priority = LOW)
    public void onKick(ServerKickEvent event) {
        Configuration settings = fetch(Configuration.class, "settings.yml");

        if (settings.getBoolean("settings.auto-fallback-to-lobbies", true)) {
            if (event.getState() == ServerKickEvent.State.CONNECTED) {
                Optional<ServerInfo> optional = fetch(ServerService.class).find(true, false);

                optional.ifPresent(server -> {
                    event.setCancelled(true);
                    event.setCancelServer(server);
                });
            }
        }
    }

}
