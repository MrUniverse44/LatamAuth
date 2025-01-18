package net.latinplay.auth.bukkit.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

}
