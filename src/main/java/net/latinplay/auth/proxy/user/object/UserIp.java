package net.latinplay.auth.proxy.user.object;

import me.blueslime.bungeemeteor.storage.interfaces.*;

import java.util.HashSet;
import java.util.Set;

public class UserIp implements StorageObject {

    @StorageIdentifier
    private final String id;

    @StorageKey(key = "accounts")
    private Set<String> accounts;

    /**
     * From BukkitMeteor 1.9.1.4, you can use @StorageIdentifier
     * in the constructor, but it only supports String saving.
     * If you want to save using id, please follow the example in this GamePlayer.
     * the StorageIdentifier saves the content of a field in a String.
     * The field name for the StorageIdentifier is not important.
     **/
    @StorageConstructor
    public UserIp(
            @StorageIdentifier String id,
            @StorageKey(key = "accounts") Set<String> accounts
    ) {
        this.accounts = accounts;
        this.id = id;
    }

    public UserIp(String id, String ip) {
        this.accounts = new HashSet<>();
        this.id = id;

        accounts.add(ip);
    }

    public String getId() {
        return id;
    }

    public Set<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<String> accounts) {
        this.accounts = accounts;
    }

    public int size() {
        return accounts.size();
    }

    public void addAccount(String account) {
        accounts.add(account);
    }

    public void removeAccount(String account) {
        accounts.remove(account);
    }
}

