package net.latinplay.auth.proxy.services;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.proxy.LatamAuth;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerService implements AdvancedModule {

    public List<String> fetchServers(boolean lobby) {
        Configuration configuration = fetch(Configuration.class, "settings.yml");

        return lobby ?
            configuration.getStringList("settings.lobby-servers")
            : configuration.getStringList("settings.auth-servers");
    }

    public Optional<ServerInfo> find(boolean lobby) {
        Configuration configuration = fetch(Configuration.class, "settings.yml");

        List<String> serverList = lobby ?
                configuration.getStringList("settings.lobby-servers")
                : configuration.getStringList("settings.auth-servers");

        if (serverList.isEmpty()) {
            return Optional.empty();
        }

        LatamAuth plugin = fetch(LatamAuth.class);
        ProxyServer proxy = plugin.getProxy();
        List<ServerInfo> result = new ArrayList<>();

        for (String name : serverList) {
            ServerInfo server = proxy.getServers().get(name);

            if (server == null) {
                plugin.getLogs().warn(
                    "You are seeing this message because the server in your servers section: " + name + " ",
                    "is offline or can't be find, if the server is being restarted this should be normal, this is only a warning"
                );
                continue;
            }

            result.add(server);
        }

        result.sort((o1, o2) -> Integer.compare(o2.getPlayers().size(), o1.getPlayers().size()));

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

}
