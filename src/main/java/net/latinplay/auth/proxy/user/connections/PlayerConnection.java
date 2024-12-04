package net.latinplay.auth.proxy.user.connections;

import com.google.gson.Gson;
import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import me.blueslime.bungeemeteor.logs.MeteorLogger;
import net.latinplay.auth.proxy.user.connections.data.ConnectionData;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class PlayerConnection implements AdvancedModule {

    protected final Gson GSON = fetch(Gson.class);

    public abstract ConnectionData load(String name);

    protected UUID fromUndashedUUID(String id) {
        return id == null ? null : new UUID(
                new BigInteger(id.substring(0, 16), 16).longValue(),
                new BigInteger(id.substring(16, 32), 16).longValue()
        );
    }

    protected String readInput(InputStream inputStream) throws IOException {
        var input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        inputStream.close();
        return input;
    }

    public MeteorLogger getLogs() {
        return fetch(MeteorLogger.class);
    }

}
