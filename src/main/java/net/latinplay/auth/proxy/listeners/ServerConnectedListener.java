package net.latinplay.auth.proxy.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectedListener implements Listener {

    @EventHandler(priority=64)
    public void onServerConnectedEvent(ServerConnectedEvent e) {
        ProxiedPlayer player = e.getPlayer();
    }
}
