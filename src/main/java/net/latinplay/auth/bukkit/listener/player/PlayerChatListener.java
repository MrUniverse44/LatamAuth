package net.latinplay.auth.bukkit.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        if (event.getMessage().startsWith("/")) {
            return;
        }
        event.setCancelled(true);
    }

}
