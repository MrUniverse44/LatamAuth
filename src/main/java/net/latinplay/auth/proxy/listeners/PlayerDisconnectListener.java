package net.latinplay.auth.proxy.listeners;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

    }

}
