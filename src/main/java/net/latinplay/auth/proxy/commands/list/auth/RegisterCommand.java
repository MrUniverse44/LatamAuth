package net.latinplay.auth.proxy.commands.list.auth;

import me.blueslime.bungeemeteor.libs.utilitiesapi.commands.sender.Sender;
import me.blueslime.bungeemeteor.libs.utilitiesapi.text.TextReplacer;
import net.latinplay.auth.proxy.commands.object.ProxyCommand;
import net.latinplay.auth.proxy.password.object.PasswordProvider;
import net.latinplay.auth.proxy.services.PasswordService;
import net.latinplay.auth.proxy.services.ServerService;
import net.latinplay.auth.proxy.services.UserService;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.Optional;

public class RegisterCommand extends ProxyCommand {

    public RegisterCommand(String command) {
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

        ProxiedPlayer player = sender.toPlayer();
        Optional<User> optional = fetch(UserService.class).find(player.getUniqueId(), player.getName());
        Configuration messages = fetch(Configuration.class, "messages.yml");

        if (optional.isEmpty()) {
            return;
        }

        User user = optional.get();

        if (user.isLogged()) {
            sender.send(messages, "messages.auth.already-logged");
            return;
        }

        if (user.isRegistered()) {
            sender.send(messages, "messages.auth.error-already-registered");
            return;
        }

        if (args.length < 3) {
            sender.send(
                messages,
                "messages.auth.wrong-password-command",
                TextReplacer.builder()
                    .replace("<code>", user.getRandomCode())
            );
            return;
        }

        String repeatedPassword = args[1];
        String password = args[0];
        String code = args[2];

        if (!code.equals(user.getRandomCode())) {
            sender.send(
                messages,
                "messages.auth.registration-wrong-code",
                TextReplacer.builder().replace("<code>", user.getRandomCode())
            );
            return;
        }

        if (!password.equals(repeatedPassword)) {
            sender.send(
                messages,
                "messages.auth.password-mismatch"
            );
            return;
        }

        sender.send(
            messages,
            "messages.auth.registration-successfully"
        );

        PasswordProvider provider = fetch(PasswordService.class).fetchProvider();

        user.setPassword(provider.hashPassword(password));
        user.setLogged(true);
        user.setRegistered(true);

        fetch(ServerService.class).find(true, false).ifPresent(player::connect);
        fetch(UserService.class).update(user);
    }
}
