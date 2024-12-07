package net.latinplay.auth.bukkit.listener.player;

import net.latinplay.auth.bukkit.commands.EditModeCommand;
import net.latinplay.auth.bukkit.listener.Listeners;

import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;

public class FoodLevelChangeListener implements Listeners {

    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        if (
            getSettings().getBoolean("settings.disabled-events.food-level-change", true) &&
            event.getEntity() instanceof Player &&
            !fetch(EditModeCommand.class).getEditors().contains(event.getEntity().getUniqueId())
        ) {
            event.setCancelled(true);
        }
    }

}
