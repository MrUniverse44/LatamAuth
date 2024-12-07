package net.latinplay.auth.proxy.services;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.proxy.listeners.player.LoginListener;
import net.latinplay.auth.proxy.listeners.player.PlayerChatListener;
import net.latinplay.auth.proxy.listeners.player.PlayerDisconnectListener;
import net.latinplay.auth.proxy.listeners.player.PreLoginListener;
import net.latinplay.auth.proxy.listeners.server.ServerConnectListener;
import net.latinplay.auth.proxy.listeners.server.ServerKickListener;

public class ListenerService implements AdvancedModule {
    @Override
    public void initialize() {
        registerAll(
            // PLAYER LISTENERS
            new PlayerChatListener(),
            new PlayerDisconnectListener(),
            new PreLoginListener(),
            new LoginListener(),
            // SERVER LISTENERS
            new ServerConnectListener(),
            new ServerKickListener()
        );
    }
}
