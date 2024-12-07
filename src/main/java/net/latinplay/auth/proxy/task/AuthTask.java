package net.latinplay.auth.proxy.task;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.libs.utilitiesapi.text.TextUtilities;
import net.latinplay.auth.proxy.LatamAuth;
import net.latinplay.auth.proxy.services.UserService;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthTask implements Runnable, AdvancedModule {

    @Override
    public void run() {
        UserService service = fetch(UserService.class);

        // * Filter to get only players that are not logged
        Set<User> users = service.getUsers()
            .stream()
            .filter(user -> !user.isLogged())
            .collect(Collectors.toSet());

        LatamAuth plugin = fetch(LatamAuth.class);
        ProxyServer server = plugin.getProxy();

        // * Gets the configuration instance
        Configuration messages = fetch(Configuration.class, "messages.yml");

        // * Create new titles for all players
        // * Title for registered users
        Title registered = server.createTitle().title(
            new TextComponent(TextUtilities.colorize(messages.getString("messages.titles.auth.login.title")))
        ).subTitle(
            new TextComponent(TextUtilities.colorize(messages.getString("messages.titles.auth.login.subtitle")))
        ).fadeIn(20).fadeOut(20).stay(40);
        // * Title for unregistered users
        Title newUser = server.createTitle().title(
            new TextComponent(TextUtilities.colorize(messages.getString("messages.titles.auth.register.title")))
        ).subTitle(
            new TextComponent(TextUtilities.colorize(messages.getString("messages.titles.auth.register.subtitle")))
        ).fadeIn(20).fadeOut(20).stay(40);

        for (User user : users) {
            // * Gets the player instance in the network
            ProxiedPlayer player = server.getPlayer(user.getUniqueId());

            // * Checks if the player is online or not
            if (player == null) {
                continue;
            }

            // * title to be showed to this player
            // * Select the title depending on if this player is registered or not.
            Title title = user.isRegistered() ? registered : newUser;
            // * Send this title to this player
            title.send(player);
        }
    }

}
