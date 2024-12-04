package net.latinplay.auth.floodgate;

import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class FloodgateDoor {

    private FloodgateApi apiService;

    public FloodgateDoor() {
        apiService = FloodgateApi.getInstance();
    }

    public FloodgateApi getApiService() {
        return apiService;
    }

    public boolean isUser(UUID uuid) {
        return apiService.isFloodgateId(uuid);
    }
}
