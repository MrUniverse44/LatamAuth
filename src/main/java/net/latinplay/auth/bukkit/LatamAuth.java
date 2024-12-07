package net.latinplay.auth.bukkit;

import me.blueslime.bukkitmeteor.BukkitMeteorPlugin;
import net.latinplay.auth.bukkit.services.ListenerService;
import net.latinplay.auth.bukkit.services.ProxyMessageService;

public class LatamAuth extends BukkitMeteorPlugin {

    @Override
    public void onEnable() {
        initialize(this, false, false);
    }

    @Override
    public void registerModules() {
        registerModule(
            ProxyMessageService.class,
            ListenerService.class
        );
    }

}
