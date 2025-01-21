package net.latinplay.auth.proxy.listeners.player;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeeutilitiesapi.commands.sender.Sender;
import net.latinplay.auth.proxy.services.ChatModService;
import net.latinplay.auth.proxy.services.UserService;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Locale;
import java.util.Optional;

public class PlayerChatListener implements Listener, AdvancedModule {

    @EventHandler(priority=EventPriority.LOWEST)
    public void on(ChatEvent event) {
        if (event.isCancelled() || !(event.getSender() instanceof ProxiedPlayer player)) {
            return;
        }

        Optional<User> optional = fetch(UserService.class).find(player.getUniqueId(), player.getName(), true);
        Configuration messages = fetch(Configuration.class, "messages.yml");
        Sender sender = Sender.build(player);

        if (optional.isEmpty()) {
            event.setCancelled(true);
            sender.send(messages, "messages.error.can-not-find-player");
            return;
        }

        String message = event.getMessage().toLowerCase(Locale.ENGLISH);

        if (
            message.startsWith("/register") ||
            message.startsWith("/login") ||
            message.startsWith("/premium")
        ) {
            return;
        }

        User user = optional.get();

        if (user.isLogged()) {
            inspect(user, event);
            return;
        }

        if (user.isRegistered()) {
            sender.send(messages, "messages.auth.login");
            event.setCancelled(true);
            return;
        }

        sender.send(messages, "messages.auth.register");
        event.setCancelled(true);
    }

    public void inspect(User user, ChatEvent event) {
        if (fetch(ChatModService.class).checkInvalid(user, event)) {
            event.setCancelled(true);
        }
    }

}
