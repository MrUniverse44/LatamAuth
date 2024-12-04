package net.latinplay.auth.proxy.services;

import me.blueslime.bukkitmeteor.logs.MeteorLogger;
import me.blueslime.bungeemeteor.BungeeMeteorPlugin;
import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.floodgate.FloodgateDoor;

import java.util.UUID;

public class FloodgateService implements AdvancedModule {

    private FloodgateDoor door = null;

    @Override
    public void initialize() {
        if (
            fetch(BungeeMeteorPlugin.class)
                .getProxy()
                .getPluginManager()
                .getPlugin("floodgate") != null
        ) {
            fetch(MeteorLogger.class).info("Floodgate detected, enabling bedrock support...");
            door = new FloodgateDoor();
        }
    }

    public boolean fromFloodgate(UUID uuid) {
        return door != null && uuid != null && door.isUser(uuid);
    }
}
