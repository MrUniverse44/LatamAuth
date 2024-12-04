package net.latinplay.auth.proxy.user;

import net.latinplay.auth.proxy.user.object.User;

public class UserSearch {

    private final UserSearchResult searchResult;
    private final User user;

    public UserSearch(UserSearchResult searchResult, User user) {
        this.searchResult = searchResult;
        this.user = user;
    }

    public UserSearch(UserSearchResult searchResult) {
        this.searchResult = searchResult;
        this.user = null;
    }

    public boolean isSuccessfully() {
        return searchResult == UserSearchResult.SUCCESSFULLY;
    }

    public boolean isInvalidCase() {
        return searchResult == UserSearchResult.INVALID_CASE;
    }

    public boolean isNewUser() {
        return searchResult == UserSearchResult.NEW_USER;
    }

    public boolean isDatabaseIssue() {
        return searchResult == UserSearchResult.DATABASE_CONNECTION_ISSUE;
    }

    public boolean isNotFound() {
        return searchResult == UserSearchResult.NOT_FOUND;
    }

    public UserSearchResult getSearchResult() {
        return searchResult;
    }

    public User getUser() {
        return user;
    }
}
