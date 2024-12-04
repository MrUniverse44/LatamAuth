package net.latinplay.auth.proxy.listeners;

import me.blueslime.bukkitmeteor.logs.MeteorLogger;
import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.libs.utilitiesapi.text.TextUtilities;
import me.blueslime.bungeemeteor.storage.object.ReferencedObject;
import net.latinplay.auth.proxy.services.ConnectionService;
import net.latinplay.auth.proxy.services.FloodgateService;
import net.latinplay.auth.proxy.services.IpService;
import net.latinplay.auth.proxy.services.UserService;
import net.latinplay.auth.proxy.user.UserSearch;
import net.latinplay.auth.proxy.user.UserSearchResult;
import net.latinplay.auth.proxy.user.connections.data.ConnectionData;
import net.latinplay.auth.proxy.user.generator.Generator;
import net.latinplay.auth.proxy.user.object.User;
import net.latinplay.auth.proxy.user.object.UserIp;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class PreLoginListener implements Listener, AdvancedModule {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_]*");

    @EventHandler(priority=64)
    public void onPreLogin(PreLoginEvent event) {
        // If this is cancelled is by another plugin so,
        // why should I load a player that didn't join to the proxy
        if (event.isCancelled()) {
            return;
        }

        // Check Floodgate User
        if (fetch(FloodgateService.class).fromFloodgate(event.getConnection().getUniqueId())) {
            return;
        }

        String username = event.getConnection().getName();

        String ip = ((InetSocketAddress)event.getConnection().getSocketAddress())
                .getAddress()
                .getHostAddress();

        Configuration messages = fetch(Configuration.class, "messages.yml");

        if (username.length() < 3 || username.length() > 16 || !NAME_PATTERN.matcher(username).matches()) {
            event.setCancelled(true);
            event.setReason(
                new TextComponent(
                    TextUtilities.colorize(
                        messages
                            .getString("messages.kick-reasons.invalid-name")
                            .replace("\\n", "\n")
                    )
                )
            );
            return;
        }

        Configuration settings = fetch(Configuration.class, "settings.yml");

        int maxAccounts = settings.getInt("settings.max-accounts", -1);

        if (maxAccounts > 0) {
            UserIp IP_DATA = fetch(IpService.class).find(ip, username);

            if (IP_DATA != null && IP_DATA.size() >= maxAccounts && !IP_DATA.getAccounts().contains(username)) {
                event.setCancelled(true);
                event.setReason(
                    new TextComponent(
                        TextUtilities.colorize(
                            messages
                                .getString("messages.kick-reasons.max-account-limit")
                                .replace("\\n", "\n")
                        )
                    )
                );
                return;
            }
        }

        fetch(ConnectionService.class).getPlayerConnection(username).whenComplete(
            (data, throwable) -> {
                if (throwable != null) {
                    event.setCancelled(true);
                    event.setReason(
                        new TextComponent(
                            TextUtilities.colorize(
                                messages
                                    .getString("messages.kick-reasons.service-issue")
                                    .replace("\\n", "\n")
                            )
                        )
                    );
                    return;
                }
                UserSearch search;
                if (!data.isSuccessfully()) {
                    search = validate(username, true);
                } else {
                    UUID premiumUUID = data.getUniqueId();
                    Optional<User> databaseUser = fetch(UserService.class).find(premiumUUID, username);

                    if (databaseUser.isPresent()) {
                        User databaseUserData = databaseUser.get();

                        search = validate(databaseUserData, username);
                    } else {
                        search = validate(username, true);
                    }

                }
                User user = search.getUser();

                if (search.isNewUser()) {
                    Generator generator = Generator.fromString(
                            settings.getString("settings.uuid-generator", "MOJANG")
                    );

                    UUID uuid = generator.generate(
                            username,
                            user != null && user.getUniqueId() != null ?
                                    user.getUniqueId() :
                                    null
                    );

                    Optional<User> databaseUser = fetch(UserService.class).find(uuid, username);

                    if (databaseUser.isPresent()) {
                        User databaseUserData = databaseUser.get();

                        if (databaseUserData.isRegistered()) {
                            event.setCancelled(true);
                            event.setReason(
                                    new TextComponent(
                                            TextUtilities.colorize(
                                                    messages
                                                            .getString("messages.kick-reasons.occupied-username")
                                                            .replace("\\n", "\n")
                                            )
                                    )
                            );
                            return;
                        }
                    }

                    if (user != null && user.isReliable() && settings.getBoolean("settings.auto-register-premium-users")) {
                        if (!user.getUsername().contentEquals(username)) {
                            event.setCancelled(true);
                            event.setReason(
                                    new TextComponent(
                                            TextUtilities.colorize(
                                                    messages
                                                            .getString("messages.kick-reasons.username-from-premium-account")
                                                            .replace("\\n", "\n")
                                            )
                                    )
                            );
                            return;
                        }
                        UUID oldenUUID = user.getUniqueId();
                        user = new User(
                                uuid,
                                username
                        );
                        user.setPremiumIdentifier(oldenUUID.toString());
                        user.setPremium(true);
                    } else {
                        if (user != null && !user.isReliable()) {
                            fetch(MeteorLogger.class).warn("The premium data for %s is not reliable, the user may not have the same name capitalization as the premium one. It is not safe to auto-register this user. Switching to offline registration!".formatted(username));
                        }
                        user = new User(
                                uuid,
                                username
                        );
                    }
                    fetch(UserService.class).update(user);
                    event.getConnection().setOnlineMode(user.isPremium());
                    event.getConnection().setUniqueId(user.getUniqueId());
                    return;
                }
                if (search.isInvalidCase()) {
                    event.setCancelled(true);
                    event.setReason(
                            new TextComponent(
                                    TextUtilities.colorize(
                                            messages
                                                    .getString("messages.kick-reasons.invalid-case-name")
                                                    .replace("\\n", "\n")
                                    )
                            )
                    );
                    return;
                }
                if (search.isDatabaseIssue()) {
                    event.setCancelled(true);
                    event.setReason(
                            new TextComponent(
                                    TextUtilities.colorize(
                                            messages
                                                    .getString("messages.kick-reasons.service-issue")
                                                    .replace("\\n", "\n")
                                    )
                            )
                    );
                    return;
                }
                if (search.isSuccessfully()) {
                    event.getConnection().setOnlineMode(
                        search.getUser().isPremium()
                    );
                }
            }
        );
    }

    private UserSearch validate(User user, String username) {
        // User from database
        if (!user.getUsername().contentEquals(username)) {
            return new UserSearch(
                UserSearchResult.INVALID_CASE.replace("%username%", user.getUsername())
            );
        }
        return new UserSearch(UserSearchResult.SUCCESSFULLY);
    }

    private UserSearch validate(String username, boolean generate) {
        // Get the user by the name not case-sensitively
        Optional<ReferencedObject> user = fetch(UserService.class).findBy(username);

        if (user.isPresent()) {
            // Return object.
            ReferencedObject object = user.get();

            // This object don't exist in database.
            if (object.getObject() == null || object.getOriginalId() == null) {
                if (generate) {
                    return new UserSearch(UserSearchResult.NEW_USER);
                } else {
                    return new UserSearch(UserSearchResult.NOT_FOUND);
                }
            }
            if (!object.getOriginalId().contentEquals(username)) {
                return new UserSearch(
                    UserSearchResult.INVALID_CASE.replace("%username%", object.getOriginalId())
                );
            }
        }
        return new UserSearch(UserSearchResult.DATABASE_CONNECTION_ISSUE);
    }

}
