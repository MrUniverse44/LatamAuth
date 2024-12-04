package net.latinplay.auth.proxy.services;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.proxy.listeners.*;

public class ListenerService implements AdvancedModule {
    @Override
    public void initialize() {
        registerAll(
            // PLAYER LISTENERS
            new PlayerChatListener(),
            new PlayerDisconnectListener(),
            new PreLoginListener(),
            // SERVER LISTENERS
            new ServerConnectedListener(),
            new ServerConnectListener()
        );
    }
}
