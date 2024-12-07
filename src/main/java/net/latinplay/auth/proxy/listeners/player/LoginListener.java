package net.latinplay.auth.proxy.listeners.player;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.libs.utilitiesapi.utils.consumer.PluginConsumer;
import me.blueslime.bungeemeteor.logs.MeteorLogger;
import me.blueslime.bungeemeteor.storage.object.ReferencedObject;
import net.latinplay.auth.proxy.services.FloodgateService;
import net.latinplay.auth.proxy.services.UserService;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

public class LoginListener implements Listener, AdvancedModule {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(LoginEvent event) {

        // Check Floodgate User
        if (fetch(FloodgateService.class).fromFloodgate(event.getConnection().getUniqueId())) {
            return;
        }

        Optional<ReferencedObject> referenced = fetch(UserService.class)
            .findBy(event.getConnection().getName());

        PendingConnection connection = event.getConnection();

        referenced.ifPresentOrElse(
            object -> {
                if (object.getObject() == null || object.getOriginalId() == null) {
                    event.setCancelled(true);
                }

                Optional<User> userOptional = fetch(UserService.class).find(
                    object.getObject(), event.getConnection().getName()
                );

                if (userOptional.isEmpty()) {
                    event.setCancelled(true);
                    return;
                }

                User user = userOptional.get();

                PluginConsumer.process(
                    () -> {
                        setField(
                            connection,
                            "uniqueId",
                            user.isPremium() ?
                                UUID.fromString(user.getPremiumIdentifier())
                                : user.getUniqueId(),
                            true
                        );
                        setField(
                            connection,
                            "rewriteId",
                            user.isPremium() ?
                                UUID.fromString(user.getPremiumIdentifier())
                                : user.getUniqueId(),
                            false
                        );
                    },
                    e -> {}
                );
            },
            () -> event.setCancelled(true)
        );
    }

    private void setField(PendingConnection connection, String fieldName, Object value, boolean debug) throws NoSuchFieldException {
        Class<?> clazz = connection.getClass();

        PluginConsumer.process(
            () -> {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(connection, value);
            },
            e -> {
                if (debug) {
                    fetch(MeteorLogger.class).error(e, "Can't modify important fields on a PendingConnection");
                }
            }
        );
    }
}
