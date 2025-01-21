package net.latinplay.auth.proxy.chat.mods;

import net.latinplay.auth.proxy.chat.ChatModerator;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.config.Configuration;

public class FloodChatMod extends ChatModerator {

    private boolean enabled;
    private int maxRepeats;

    @Override
    public void initialize() {
        Configuration mod = fetch(Configuration.class, "mod.yml");

        this.enabled = mod.getBoolean("chat-mod.enabled", true);
        this.maxRepeats = mod.getInt("chat-mod.flood.max-repetitions", 4);
    }

    @Override
    public boolean checkInvalid(User user, ChatEvent event) {
        if (!enabled) {
            return false;
        }
        char[] chars = event.getMessage().toCharArray();
        boolean valid = true;

        int repeatedCount = 0;
        for (int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];

            if (currentChar == ' ') {
                continue;
            }

            if (i == chars.length - 1 || currentChar != chars[i + 1]) {
                repeatedCount = 0;
                continue;
            }

            repeatedCount++;
            if (repeatedCount >= maxRepeats) {
                valid = false;
                while (i < chars.length - 1 && chars[i] == chars[i + 1]) {
                    i++;
                }
                repeatedCount = 0;
            }
        }

        return !valid;
    }


}
