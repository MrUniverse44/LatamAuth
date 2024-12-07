package net.latinplay.auth.bukkit.listener.inventory;

import net.latinplay.auth.bukkit.commands.EditModeCommand;
import net.latinplay.auth.bukkit.listener.Listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryOpenListener implements Listeners {

    @EventHandler
    public void on(InventoryOpenEvent event) {
        if (
            getSettings().getBoolean("settings.disabled-events.inventory-open", true) &&
            !fetch(EditModeCommand.class).getEditors().contains(event.getPlayer().getUniqueId())
        ) {
            event.setCancelled(true);
        }
    }

}
