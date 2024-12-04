package net.latinplay.auth.proxy.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {

    @EventHandler(priority=64)
    public void onServerConnect(ServerConnectEvent e) {

        if(e.isCancelled()) {
            return;
        }

    }

}
