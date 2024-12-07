package net.latinplay.auth.proxy.services;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.proxy.LatamAuth;
import net.latinplay.auth.proxy.task.AuthTask;

import java.util.concurrent.TimeUnit;

public class TaskService implements AdvancedModule {

    @Override
    public void initialize() {
        LatamAuth plugin = fetch(LatamAuth.class);

        plugin.getProxy().getScheduler().schedule(
            plugin,
            new AuthTask(),
            0,
            20,
            TimeUnit.SECONDS
        );
    }
}
