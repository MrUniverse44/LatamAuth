package net.latinplay.auth.proxy.services;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.storage.StorageDatabase;
import me.blueslime.bungeemeteor.storage.object.ReferencedObject;
import net.latinplay.auth.proxy.user.object.User;

import java.util.*;

public class UserService implements AdvancedModule {

    private final Map<UUID, User> playerMap = new HashMap<>();
    private final Map<String, ReferencedObject> playerNameMap = new HashMap<>();

    public Optional<User> find(UUID uuid, String username, boolean premium) {
        return Optional.of(
            playerMap.computeIfAbsent(
                uuid,
                id -> {
                    /* Search this user in the database */
                    Optional<User> optional = fetch(StorageDatabase.class)
                        .loadByIdSync(
                            User.class,
                            id.toString()
                        );
                    return optional.orElse(new User(uuid, username, premium));
                }
            )
        );
    }

    public Optional<User> find(String uuid, String username, boolean premium) {
        return find(UUID.fromString(uuid), username, premium);
    }

    public Optional<ReferencedObject> findBy(String name) {
        return Optional.of(
                playerNameMap.computeIfAbsent(
                    name,
                    id -> {
                        /* Search this user in the database */
                        Optional<ReferencedObject> optional = fetch(StorageDatabase.class)
                            .loadByExtraIdentifierSync(
                                User.class,
                                id
                            );
                        return optional.orElse(new ReferencedObject(null, null));
                    }
                )
        );
    }

    public Optional<User> find(String name) {
        return fetch(StorageDatabase.class)
            .loadByIdSync(
                User.class,
                name
        );
    }

    public void update(User user) {
        // Update in local cache
        playerMap.put(user.getUniqueId(), user);
        // Update in mongo cache
        fetch(StorageDatabase.class).saveOrUpdateAsync(user);
    }

    public void saveAndRemove(UUID uuid, String username) {
        playerNameMap.remove(username.toLowerCase(Locale.ENGLISH));
        playerNameMap.remove(username);
        if (playerMap.containsKey(uuid) && playerMap.get(uuid) != null) {
            fetch(StorageDatabase.class).saveOrUpdateAsync(playerMap.remove(uuid));
        }
    }

    public void delete(UUID uuid) {
        fetch(StorageDatabase.class).deleteByIdAsync(User.class, uuid.toString());
    }

    public Set<User> getUsers() {
        return new HashSet<>(playerMap.values());
    }
}

