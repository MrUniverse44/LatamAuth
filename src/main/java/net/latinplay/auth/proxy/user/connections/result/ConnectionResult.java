package net.latinplay.auth.proxy.user.connections.result;

public enum ConnectionResult {
    SERVER_EXCEPTION,
    SUCCESSFULLY,
    THROTTLED,
    UNDEFINED,
    ERROR;

    public static ConnectionResult fromCode(int code) {
        switch (code) {
            case 429 -> {
                return THROTTLED;
            }
            case 204, 404 -> {
                return ERROR;
            }
            default -> {
                return UNDEFINED;
            }
            case 500 -> {
                return SERVER_EXCEPTION;
            }
        }
    }
}
