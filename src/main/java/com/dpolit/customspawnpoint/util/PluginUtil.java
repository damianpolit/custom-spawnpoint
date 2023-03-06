package com.dpolit.customspawnpoint.util;

import com.dpolit.customspawnpoint.domain.SpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;

import static com.dpolit.customspawnpoint.CustomSpawnpoint.getPlugin;

public class PluginUtil {

    private PluginUtil() {
    }

    private static final FileConfiguration config = getPlugin().getConfig();

    public static Location getTeleportationCoords(SpawnEvent type) {
        String spawnType = type.toString().toLowerCase();
        return new Location(
                Bukkit.getWorld(Optional.ofNullable(config)
                        .map(c -> c.getString("data.spawnpoints." + spawnType + ".world"))
                        .orElse("")),
                config.getDouble("data.spawnpoints." + spawnType + ".x"),
                config.getDouble("data.spawnpoints." + spawnType + ".y"),
                config.getDouble("data.spawnpoints." + spawnType + ".z"),
                config.getLong("data.spawnpoints." + spawnType + ".yaw"),
                config.getLong("data.spawnpoints." + spawnType + ".pitch"));
    }

}
