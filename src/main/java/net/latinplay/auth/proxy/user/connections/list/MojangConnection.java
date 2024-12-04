package net.latinplay.auth.proxy.user.connections.list;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.blueslime.bungeemeteor.libs.utilitiesapi.utils.consumer.PluginConsumer;

import net.latinplay.auth.proxy.user.connections.PlayerConnection;
import net.latinplay.auth.proxy.user.connections.data.ConnectionData;
import net.latinplay.auth.proxy.user.connections.result.ConnectionResult;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MojangConnection extends PlayerConnection {

    @SuppressWarnings("deprecation")
    @Override
    public ConnectionData load(final String name) {
        return PluginConsumer.ofUnchecked(
            () -> {
                HttpURLConnection connection = (HttpURLConnection) new URL(
                    "https://api.mojang.com/users/profiles/minecraft/" + name
                ).openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() == 200) {
                    var data = GSON.fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);

                    String id = data.get("id")
                        .getAsString();

                    JsonElement demo = data.get("demo");

                    return demo != null ? null : new ConnectionData(
                        ConnectionResult.SUCCESSFULLY,
                        data.get("name").getAsString(),
                        fromUndashedUUID(id),
                        true // Mojang API is always authoritative
                    );
                } else {
                    return new ConnectionData(ConnectionResult.fromCode(connection.getResponseCode()));
                }
            },
            e -> getLogs().error(e, "Can't load player data"),
            () -> new ConnectionData(ConnectionResult.ERROR)
        );
    }

}
