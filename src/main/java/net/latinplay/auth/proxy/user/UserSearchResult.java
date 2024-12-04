package net.latinplay.auth.proxy.user;

import me.blueslime.bungeeutilitiesapi.text.TextReplacer;

public enum UserSearchResult {
    DATABASE_CONNECTION_ISSUE,
    SUCCESSFULLY,
    INVALID_CASE,
    NOT_FOUND,
    NEW_USER;

    private final TextReplacer replacer = TextReplacer.builder();

    public UserSearchResult replace(String variable, String result) {
        replacer.replace(variable, result);
        return this;
    }

    public UserSearchResult replace(String variable, int result) {
        replacer.replace(variable, result);
        return this;
    }

    public TextReplacer getReplacer() {
        return replacer;
    }
}
