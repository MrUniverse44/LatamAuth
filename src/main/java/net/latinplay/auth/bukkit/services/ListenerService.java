package net.latinplay.auth.bukkit.services;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import net.latinplay.auth.bukkit.listener.block.BlockBreakListener;
import net.latinplay.auth.bukkit.listener.block.BlockPlaceListener;
import net.latinplay.auth.bukkit.listener.inventory.InventoryOpenListener;
import net.latinplay.auth.bukkit.listener.player.*;
import net.latinplay.auth.bukkit.listener.world.WeatherChangeListener;

public class ListenerService implements AdvancedModule {

    @Override
    public void initialize() {
        registerAll(
            // * BLOCK LISTENERS
            new BlockBreakListener(),
            new BlockPlaceListener(),
            // * INVENTORY LISTENERS
            new InventoryOpenListener(),
            // * PLAYER LISTENERS
            new FoodLevelChangeListener(),
            new PlayerDropListener(),
            new PlayerInteractListener(),
            new PlayerItemHeldListener(),
            new PlayerQuitListener(),
            // * WORLD LISTENERS
            new WeatherChangeListener()
        );
    }
}
