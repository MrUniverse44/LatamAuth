package net.latinplay.auth.proxy.chat.mods;

import net.latinplay.auth.proxy.chat.ChatModerator;
import net.latinplay.auth.proxy.chat.ChatPriority;
import net.latinplay.auth.proxy.user.object.User;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomainBlockerChatMod extends ChatModerator {

    private final Pattern DOMAIN_PATTERN = Pattern.compile(
        "\\b(?:[a-zA-Z0-9-]+(?:\\s|\\.|,|\\(punto\\)|<punto>|punto|\\(dot\\)|<dot>|dot|\\(coma\\)|<coma>|coma|point)+)+(?:ml|ga|cf|gq|tk|net|com|org|io|xyz|club|site|top|gg|me)(?:\\s|\\.|,|\\(punto\\)|<punto>|punto|\\(dot\\)|<dot>|dot|\\(coma\\)|<coma>|coma|point)*(?::\\d{1,5})?\\b",
        Pattern.CASE_INSENSITIVE
    );

    private final List<String> ALLOWED_DOMAINS = new ArrayList<>();
    private boolean enabled;

    @Override
    public void initialize() {
        Configuration mod = fetch(Configuration.class, "mod.yml");

        this.enabled = mod.getBoolean("chat-mod.enabled", true);
        this.ALLOWED_DOMAINS.addAll(
            mod.getStringList(
                "chat-mod.other-domain.ignore"
            )
        );
    }

    @Override
    public boolean checkInvalid(User user, ChatEvent event) {
        if (!enabled) {
            return false;
        }

        String message = event.getMessage().toLowerCase();
        Matcher matcher = DOMAIN_PATTERN.matcher(message);

        while (matcher.find()) {
            String foundDomain = matcher.group();

            if (ALLOWED_DOMAINS.contains(foundDomain)) {
                continue;
            }

            return true;
        }

        return false;
    }

    @Override
    public int getPriority() {
        return ChatPriority.HIGHEST;
    }
}

