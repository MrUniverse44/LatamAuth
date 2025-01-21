package net.latinplay.auth.proxy.services;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.implementation.module.Module;
import net.latinplay.auth.proxy.chat.ChatModerator;
import net.latinplay.auth.proxy.chat.mods.*;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

import java.util.*;

public class ChatModService implements AdvancedModule {

    private final Set<ChatModerator> mods = new HashSet<>();

    @Override
    public void initialize() {
        registerMod(
            new CensoredWordsChatMod(),
            new DomainBlockerChatMod(),
            new FloodChatMod()
        );

        mods.forEach(Module::initialize);
    }

    public boolean checkInvalid(User user, ChatEvent event) {
        ArrayList<ChatModerator> ordered = new ArrayList<>(mods);
        ordered.sort(Comparator.comparingInt(ChatModerator::getPriority));

        return ordered
            .stream()
            .anyMatch(
                mod -> mod.checkInvalid(user, event) &&
                !((ProxiedPlayer)event.getSender()).hasPermission("latam.auth.chat.mod.*")
            );
    }

    @Override
    public void reload() {
        mods.forEach(Module::reload);
    }

    public void registerMod(ChatModerator... moderator) {
        mods.addAll(Arrays.asList(moderator));
    }
}
