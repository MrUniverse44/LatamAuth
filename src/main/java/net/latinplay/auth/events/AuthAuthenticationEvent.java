package net.latinplay.auth.events;

import me.blueslime.bungeemeteor.implementation.Implements;
import me.blueslime.bungeemeteor.logs.MeteorLogger;
import net.latinplay.auth.proxy.LatamAuth;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.AsyncEvent;

public class AuthAuthenticationEvent extends AsyncEvent<ProxiedPlayer> {

    private final ProxiedPlayer player;
    private final User user;

    public AuthAuthenticationEvent(ProxiedPlayer player, User user) {
        super((proxiedPlayer, throwable) -> {
            if (throwable != null) {
                MeteorLogger.fetch().error(throwable, "Can't call AuthAuthenticationEvent");
            }
        });
        this.player = player;
        this.user = user;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public User getUser() {
        return user;
    }

    public static void call(ProxiedPlayer player, User user) {
        AuthAuthenticationEvent event = new AuthAuthenticationEvent(player, user);

        Implements.fetch(LatamAuth.class)
            .getProxy()
            .getPluginManager()
            .callEvent(event);

    }

}
