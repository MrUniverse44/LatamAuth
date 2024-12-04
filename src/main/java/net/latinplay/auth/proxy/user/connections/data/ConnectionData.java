package net.latinplay.auth.proxy.user.connections.data;

import net.latinplay.auth.proxy.user.connections.result.ConnectionResult;

import java.util.UUID;

public class ConnectionData {

    private final ConnectionResult result;
    private final String nickname;
    private final UUID uuid;

    private final boolean reliable;

    public ConnectionData(final ConnectionResult result, final String nickname, final UUID uuid, final boolean reliable) {
        this.nickname = nickname;
        this.result = result;
        this.uuid = uuid;

        this.reliable = reliable;
    }

    public ConnectionData(final ConnectionResult result) {
        this.reliable = false;
        this.nickname = null;
        this.result = result;
        this.uuid = null;
    }

    public ConnectionResult getResult() {
        return result;
    }

    public boolean isReliable() {
        return reliable;
    }

    public String getNickname() {
        return nickname;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public boolean isSuccessfully() {
        return result == ConnectionResult.SUCCESSFULLY;
    }
}
