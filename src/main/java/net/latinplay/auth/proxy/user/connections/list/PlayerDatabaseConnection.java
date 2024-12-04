package net.latinplay.auth.proxy.user.connections.list;

import com.google.gson.JsonObject;
import me.blueslime.bungeemeteor.libs.utilitiesapi.utils.consumer.PluginConsumer;

import net.latinplay.auth.proxy.user.connections.PlayerConnection;
import net.latinplay.auth.proxy.user.connections.data.ConnectionData;
import net.latinplay.auth.proxy.user.connections.result.ConnectionResult;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class PlayerDatabaseConnection extends PlayerConnection {

    @SuppressWarnings("deprecation")
    @Override
    public ConnectionData load(final String name) {
        return PluginConsumer.ofUnchecked(
                () -> {
                    HttpURLConnection connection = (HttpURLConnection) new URL(
                        "https://playerdb.co/api/player/minecraft/" + name
                    ).openConnection();

                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    if (connection.getResponseCode() == 200) {
                        var data = GSON.fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);

                        String id = data.get("data")
                            .getAsJsonObject()
                            .get("player")
                            .getAsJsonObject()
                            .get("id")
                            .getAsString();

                        String username = data.get("data")
                            .getAsJsonObject()
                            .get("player")
                            .getAsJsonObject()
                            .get("username")
                            .getAsString();


                        return new ConnectionData(
                            ConnectionResult.SUCCESSFULLY,
                            username,
                            UUID.fromString(id),
                            username.equalsIgnoreCase(name)
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
