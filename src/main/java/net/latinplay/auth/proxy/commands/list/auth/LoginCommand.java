package net.latinplay.auth.proxy.commands.list.auth;

import me.blueslime.bungeemeteor.libs.utilitiesapi.commands.sender.Sender;
import net.latinplay.auth.events.AuthAuthenticationEvent;
import net.latinplay.auth.proxy.commands.object.ProxyCommand;
import net.latinplay.auth.proxy.password.object.PasswordProvider;
import net.latinplay.auth.proxy.services.PasswordService;
import net.latinplay.auth.proxy.services.UserService;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.Optional;

public class LoginCommand extends ProxyCommand {

    public LoginCommand(String command) {
        super(command);
    }

    /**
     * Execute an async command
     *
     * @param sender of the command
     * @param args   from the command
     */
    @Override
    public void execute(Sender sender, String[] args) {
        if (!sender.isPlayer()) {
            return;
        }

        if (args.length < 1) {
            return;
        }

        ProxiedPlayer player = sender.toPlayer();
        Optional<User> optional = fetch(UserService.class).find(player.getUniqueId(), player.getName(), true);
        Configuration messages = fetch(Configuration.class, "messages.yml");

        if (optional.isEmpty()) {
            return;
        }

        User user = optional.get();

        if (user.isLogged()) {
            sender.send(messages, "messages.auth.already-logged");
            return;
        }

        if (!user.isRegistered()) {
            sender.send(messages, "messages.auth.error-not-registered");
            return;
        }

        String hashedPassword = user.getPassword();
        String password = args[0];

        PasswordProvider provider = fetch(PasswordService.class).fetchProvider();

        if (provider.verifyPassword(password, hashedPassword)) {
            sender.send(messages, "messages.auth.password-not-match");
            return;
        }

        sender.send(messages, "messages.auth.logged-success");
        user.setLogged(true);
        fetch(UserService.class).update(user);
        AuthAuthenticationEvent.call(sender.toPlayer(), user);
    }
}
