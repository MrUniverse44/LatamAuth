package net.latinplay.auth.bukkit.listener.player;

import net.latinplay.auth.bukkit.commands.EditModeCommand;
import net.latinplay.auth.bukkit.listener.Listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listeners {

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (
            getSettings().getBoolean("settings.disabled-events.player-interact", true) &&
            !fetch(EditModeCommand.class).getEditors().contains(event.getPlayer().getUniqueId())
        ) {
            event.setCancelled(true);
        }
    }

}
