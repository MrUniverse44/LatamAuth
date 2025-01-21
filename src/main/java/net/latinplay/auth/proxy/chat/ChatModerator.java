package net.latinplay.auth.proxy.chat;

import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.event.ChatEvent;

public abstract class ChatModerator implements AdvancedModule {

    public abstract boolean checkInvalid(User user, ChatEvent event);

    public int getPriority() {
        return ChatPriority.LOWEST;
    }

}
