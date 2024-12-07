package net.latinplay.auth.proxy.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.blueslime.bungeemeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.proxy.user.connections.PlayerConnection;
import net.latinplay.auth.proxy.user.connections.data.ConnectionData;
import net.latinplay.auth.proxy.user.connections.list.MojangConnection;
import net.latinplay.auth.proxy.user.connections.list.PlayerDatabaseConnection;
import net.latinplay.auth.proxy.user.connections.result.ConnectionResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConnectionService implements AdvancedModule {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(100);
    private final Set<PlayerConnection> connectionMethods = new HashSet<>();

    private final Cache<String, ConnectionData> dataCache;

    public ConnectionService() {
        // Cache
        dataCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    }

    @Override
    public void initialize() {
        // Register mojang connection check
        connectionMethods.add(new MojangConnection());
        // Register external player db check
        connectionMethods.add(new PlayerDatabaseConnection());
    }

    @Override
    public void shutdown() {
        EXECUTOR_SERVICE.shutdown();
    }

    public CompletableFuture<ConnectionData> getPlayerConnection(String username) {
        ConnectionData present = dataCache.getIfPresent(username);

        if (present != null) {
            return CompletableFuture.completedFuture(present);
        }

        List<CompletableFuture<ConnectionData>> futures = connectionMethods.stream()
            .map(connection -> CompletableFuture.supplyAsync(() -> connection.load(username), EXECUTOR_SERVICE))
            .toList();

        return CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        ).thenApply(
            value -> {
                ConnectionData result = futures.stream()
                        .map(CompletableFuture::join)
                        .filter(ConnectionData::isSuccessfully)
                        .findFirst()
                        .orElse(new ConnectionData(ConnectionResult.ERROR));

                dataCache.put(username, result);

                return result;
            }
        );
    }

    public Cache<String, ConnectionData> getDataCache() {
        return dataCache;
    }
}
