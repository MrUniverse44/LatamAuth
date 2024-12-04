package net.latinplay.auth.proxy.user.object;

import me.blueslime.bungeemeteor.storage.interfaces.*;

import java.util.UUID;

public class User implements StorageObject {

    @StorageIdentifier
    private final String id;

    @StorageExtraIdentifier
    @StorageKey(key = "username")
    private String username;

    @StorageKey(key = "password")
    private String password;

    @StorageKey(key = "last_ip")
    private String ip;

    @StorageKey(key = "premium")
    private boolean premium;

    @StorageKey(key = "premiumIdentifier")
    private String premiumIdentifier;

    @StorageKey(key = "reliable", defaultValue = "false")
    private boolean reliable;

    @StorageKey(key = "logged", defaultValue = "false")
    private boolean logged;

    @StorageKey(key = "registered", defaultValue = "false")
    private boolean registered;

    @StorageKey(key = "loaded", defaultValue = "false")
    private boolean loaded;

    @StorageIgnore
    private UUID uuid;

    /**
     * From BukkitMeteor 1.9.1.4, you can use @StorageIdentifier
     * in the constructor, but it only supports String saving.
     * If you want to save using id, please follow the example in this GamePlayer.
     * the StorageIdentifier saves the content of a field in a String.
     * The field name for the StorageIdentifier is not important.
     **/
    @StorageConstructor
    public User(
            @StorageIdentifier String id,
            @StorageKey(key = "username") String username,
            @StorageKey(key = "password") String password,
            @StorageKey(key = "last_ip") String ip,
            @StorageKey(key = "premium") boolean premium,
            @StorageKey(key = "logged", defaultValue = "false") boolean logged,
            @StorageKey(key = "registered", defaultValue = "false") boolean registered,
            @StorageKey(key = "premiumIdentifier") String premiumIdentifier,
            @StorageKey(key = "reliable", defaultValue = "false") boolean reliable,
            @StorageKey(key = "loaded", defaultValue = "false") boolean loaded
    ) {
        this.premiumIdentifier = premiumIdentifier;
        this.uuid = UUID.fromString(id);
        this.registered = registered;
        this.password = password;
        this.username = username;
        this.premium = premium;
        this.reliable = reliable;
        this.logged = logged;
        this.loaded = loaded;
        this.ip = ip;
        this.id = id;
    }

    public User(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.premiumIdentifier = uuid.toString();
        this.registered = false;
        this.reliable = false;
        this.password = null;
        this.premium = false;
        this.logged = false;
        this.loaded = false;
        this.ip = "";
        this.id = uuid.toString();
    }

    public boolean isReliable() {
        return reliable;
    }

    public void setReliable(boolean reliable) {
        this.reliable = reliable;
    }

    public UUID getUniqueId() {
        if (uuid == null) {
            uuid = UUID.fromString(id);
        }
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getPremiumIdentifier() {
        return premiumIdentifier;
    }

    public void setPremiumIdentifier(String premiumIdentifier) {
        this.premiumIdentifier = premiumIdentifier;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
