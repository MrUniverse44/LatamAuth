package net.latinplay.auth.proxy.listeners.server;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.logs.MeteorLogger;
import net.latinplay.auth.proxy.services.ServerService;
import net.latinplay.auth.proxy.services.UserService;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.*;

public class ServerConnectListener implements Listener, AdvancedModule {

    @EventHandler(priority=EventPriority.HIGHEST)
    public void on(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Optional<User> optional = fetch(UserService.class).find(player.getUniqueId(), player.getName());

        if (optional.isEmpty()) {
            fetch(MeteorLogger.class).error(
                "Can't find the player (" + player.getName() + ") in our database",
                "This player was registered or playing?",
                "If your answer is yes it could be a big problem for your network security, please ensure",
                "to have connection with your mongodb service or contact the developer because this should be a big problem"
            );
            return;
        }

        // * Gets the server service
        ServerService service = fetch(ServerService.class);

        // * Get this user
        User user = optional.get();

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY || event.getPlayer().getServer() == null) {
            Optional<ServerInfo> optionalServer = user.isPremium() ? service.find(true) : service.find(false);

            optionalServer.ifPresentOrElse(
                event::setTarget,
                () -> {
                    // * Lobbies are shutdown, so we need to move players to the auth
                    // * But we can't move players from auth due to extra security
                    if (user.isPremium()) {
                        Optional<ServerInfo> optionalAuth = service.find(false);
                        optionalAuth.ifPresent(event::setTarget);
                    }
                }
            );
            return;
        }

        if (!user.isLogged()) {
            if (!service.fetchServers(user.isPremium()).contains(event.getTarget().getName())) {
                event.setCancelled(true);
            }
        }
    }
}
