package net.latinplay.auth.bukkit.listener.player;

import net.latinplay.auth.bukkit.commands.EditModeCommand;
import net.latinplay.auth.bukkit.listener.Listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listeners {

    @EventHandler
    public void on(PlayerQuitEvent event) {
        fetch(EditModeCommand.class).getEditors().remove(event.getPlayer().getUniqueId());
        event.setQuitMessage(null);
    }

}
