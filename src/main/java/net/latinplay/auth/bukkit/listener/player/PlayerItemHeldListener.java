package net.latinplay.auth.bukkit.listener.player;

import net.latinplay.auth.bukkit.commands.EditModeCommand;
import net.latinplay.auth.bukkit.listener.Listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerItemHeldListener implements Listeners {

    @EventHandler
    public void on(PlayerItemHeldEvent event) {
        if (
            getSettings().getBoolean("settings.disabled-events.item-held-change", true) &&
            !fetch(EditModeCommand.class).getEditors().contains(event.getPlayer().getUniqueId())
        ) {
            event.setCancelled(true);
        }
    }
}
