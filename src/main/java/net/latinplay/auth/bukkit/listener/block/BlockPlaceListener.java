package net.latinplay.auth.bukkit.listener.block;

import net.latinplay.auth.bukkit.commands.EditModeCommand;
import net.latinplay.auth.bukkit.listener.Listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listeners {

    @EventHandler
    public void on(BlockPlaceEvent event) {
        if (
            getSettings().getBoolean("settings.disabled-events.block-place", true) &&
            !fetch(EditModeCommand.class).getEditors().contains(event.getPlayer().getUniqueId())
        ) {
            event.setCancelled(true);
        }
    }

}
