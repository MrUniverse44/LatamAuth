package net.latinplay.auth.proxy.listeners.player;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.logs.MeteorLogger;
import net.latinplay.auth.proxy.services.UserService;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

public class PlayerDisconnectListener implements Listener, AdvancedModule {

    @EventHandler
    public void on(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Optional<User> optional = fetch(UserService.class).find(player.getUniqueId(), player.getName(), true);

        if (optional.isEmpty()) {
            fetch(MeteorLogger.class).error(
                "Can't find the disconnected player (" + player.getName() + ") in our database",
                "This player was registered or playing?",
                "If your answer is yes it could be a big problem for your network security, please ensure",
                "to have connection with your mongodb service or contact the developer because this should be a big problem"
            );
            return;
        }

        User user = optional.get();

        // * Make this player to need to log in again
        user.setLogged(false);

        // * Save this player unlogged into the database.
        fetch(UserService.class).update(user);
    }

}
