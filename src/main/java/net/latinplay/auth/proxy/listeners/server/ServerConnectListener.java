package net.latinplay.auth.proxy.listeners.server;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.libs.utilitiesapi.commands.sender.Sender;
import me.blueslime.bungeemeteor.libs.utilitiesapi.text.TextReplacer;
import me.blueslime.bungeemeteor.logs.MeteorLogger;
import me.blueslime.bungeemeteor.storage.StorageDatabase;
import net.latinplay.auth.proxy.password.object.PasswordProvider;
import net.latinplay.auth.proxy.services.PasswordService;
import net.latinplay.auth.proxy.services.ServerService;
import net.latinplay.auth.proxy.services.UserService;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.security.SecureRandom;
import java.util.*;

public class ServerConnectListener implements Listener, AdvancedModule {

    private static final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~";

    @EventHandler(priority=EventPriority.HIGHEST)
    public void on(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Optional<User> optional = fetch(UserService.class).find(player.getUniqueId(), player.getName(), true);

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
            Optional<ServerInfo> optionalServer = user.isPremium() ?
                    service.find(true, user.isTwoStep())
                    : service.find(false, user.isTwoStep());

            optionalServer.ifPresentOrElse(
                event::setTarget,
                () -> {
                    // * Lobbies are shutdown, so we need to move players to the auth
                    // * But we can't move players from auth due to extra security
                    if (user.isPremium()) {
                        Optional<ServerInfo> optionalAuth = service.find(false, user.isTwoStep());
                        optionalAuth.ifPresent(event::setTarget);
                    }
                }
            );
            return;
        }

        if (!user.isLogged() && user.isPremium()) {
            if (!user.isRegistered()) {
                user.setLogged(true);

                PasswordProvider provider = fetch(PasswordService.class).fetchProvider();

                String generatedPassword = generate();

                user.setPassword(
                    provider.hashPassword(generatedPassword)
                );
                Sender.build(event.getPlayer()).send(
                    fetch(Configuration.class, "messages.yml"),
                    "messages.automatically-generated-password",
                    "&aYour password has been successfully generated because you are premium, if you want to change it you need to use /changepassword command. Your password: &c<password>",
                    TextReplacer.builder()
                        .replace("<password>", generatedPassword)
                );
                user.setRegistered(true);
                Sender.build(event.getPlayer()).send(
                    fetch(Configuration.class, "messages.yml"),
                    "messages.automatically-logged",
                    "&aYou has been automatically logged because you are premium"
                );
                fetch(StorageDatabase.class).saveOrUpdateAsync(user);
                return;
            }
            user.setLogged(true);
            Sender.build(event.getPlayer()).send(
                fetch(Configuration.class, "messages.yml"),
                "messages.automatically-logged",
                "&aYou has been automatically logged because you are premium"
            );
            fetch(StorageDatabase.class).saveOrUpdateAsync(user);
            return;
        }

        if (!user.isLogged()) {
            if (!service.fetchServers(user.isPremium()).contains(event.getTarget().getName())) {
                event.setCancelled(true);
            }
        }
    }

    private String generate() {
        SecureRandom random = fetch(SecureRandom.class);

        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(VALID_CHARACTERS.length());
            password.append(VALID_CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
