package net.latinplay.auth.bukkit.listener.entity;

import net.latinplay.auth.bukkit.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listeners {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && getSettings().getBoolean("settings.disabled-events.entity-damage", true)) {
            event.setCancelled(true);
        }
    }

}
