package net.latinplay.auth.proxy.chat.mods;

import net.latinplay.auth.proxy.chat.ChatModerator;
import net.latinplay.auth.proxy.chat.ChatPriority;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.config.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CensoredWordsChatMod extends ChatModerator {

    private Pattern pattern = null;
    private boolean enabled;

    @Override
    public void initialize() {
        Configuration mod = fetch(Configuration.class, "mod.yml");

        this.enabled = mod.getBoolean("chat-mod.enabled", true);
        this.pattern = Pattern.compile(
            "\\b(" + String.join("|", mod.getStringList("chat-mod.censored-words.list")) + ")\\b"
        );
    }

    @Override
    public boolean checkInvalid(User user, ChatEvent event) {
        if (!enabled || pattern == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(event.getMessage().toLowerCase());

        int lastIndex = 0;
        while (matcher.find()) {
            lastIndex = matcher.end();
        }

        return lastIndex != 0;
    }

    @Override
    public int getPriority() {
        return ChatPriority.HIGH;
    }
}
