package net.latinplay.auth.bukkit.listener.world;

import net.latinplay.auth.bukkit.listener.Listeners;

import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.EventHandler;

public class WeatherChangeListener implements Listeners {

    @EventHandler
    public void on(WeatherChangeEvent event) {
        if (
            getSettings().getBoolean("settings.disabled-events.weather-change", true)
        ) {
            event.setCancelled(true);
        }
    }

}

