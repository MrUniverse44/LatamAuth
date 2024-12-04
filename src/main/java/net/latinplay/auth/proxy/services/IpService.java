package net.latinplay.auth.proxy.services;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.storage.StorageDatabase;
import net.latinplay.auth.proxy.user.object.User;
import net.latinplay.auth.proxy.user.object.UserIp;

import java.util.*;

public class IpService implements AdvancedModule {

    private final Map<String, UserIp> ipMap = new HashMap<>();

    public UserIp find(String ip, String username) {
        return ipMap.computeIfAbsent(
            ip,
            id -> {
                /* Search this user in the database */
                Optional<UserIp> optional = fetch(StorageDatabase.class)
                    .loadByIdSync(
                        UserIp.class,
                        id
                    );
                return optional.orElse(new UserIp(ip, username));
            }
        );
    }

    public void update(UserIp user) {
        // Update in local cache
        ipMap.put(user.getId(), user);
        // Update in mongo cache
        fetch(StorageDatabase.class).saveOrUpdateAsync(user);
    }

    public void saveAndRemove(String ip) {
        if (ipMap.containsKey(ip) && ipMap.get(ip) != null) {
            fetch(StorageDatabase.class).saveOrUpdateAsync(ipMap.remove(ip));
        }
    }

    public void delete(UUID uuid) {
        fetch(StorageDatabase.class).deleteByIdAsync(User.class, uuid.toString());
    }

    public Set<UserIp> getUsers() {
        return new HashSet<>(ipMap.values());
    }
}