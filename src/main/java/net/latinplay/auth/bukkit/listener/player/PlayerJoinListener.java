package net.latinplay.auth.bukkit.listener.player;

import me.blueslime.bukkitmeteor.implementation.Implements;
import net.latinplay.auth.bukkit.LatamAuth;
import net.latinplay.auth.bukkit.listener.Listeners;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listeners {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            player.setHealth(maxHealth.getValue());
        }
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);
        player.setFireTicks(0);

        if (!player.getActivePotionEffects().isEmpty()) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        }

        Location bukkitLocation = fetch(LatamAuth.class)
            .getServer()
            .getWorlds()
            .getFirst()
            .getSpawnLocation();

        player.teleport(bukkitLocation);

        new BukkitRunnable() {
            public void run() {
                player.teleport(
                    bukkitLocation
                );
                player.setGameMode(GameMode.ADVENTURE);
            }
        }.runTaskLater(Implements.fetch(LatamAuth.class), 10);
    }

}
