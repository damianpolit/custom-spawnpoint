package com.dpolit.customspawnpoint.listeners;

import com.dpolit.customspawnpoint.domain.SpawnEvent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.dpolit.customspawnpoint.CustomSpawnpoint.getPlugin;
import static com.dpolit.customspawnpoint.util.PlayerParticlesManager.spawnParticles;
import static com.dpolit.customspawnpoint.util.PluginUtil.getTeleportationCoords;
import static com.dpolit.customspawnpoint.util.SoundEffectManager.playSound;

public class PlayerRespawnListener implements Listener {

    private final FileConfiguration config = getPlugin().getConfig();

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (shouldPlayerBeTeleported(player)) {
            event.setRespawnLocation(getTeleportationCoords(SpawnEvent.DEATH));
            player.teleport(getTeleportationCoords(SpawnEvent.DEATH));
            spawnParticles(player, Particle.CAMPFIRE_COSY_SMOKE);
            playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        }
    }

    private boolean shouldPlayerBeTeleported(Player player) {
        boolean bedSpawnEnabled = config.getBoolean("settings.deathSpawnWhenBedSpawnIsCreated");
        return config.getBoolean("data.spawnpoints.death.enable")
                && (!bedSpawnEnabled || player.getBedSpawnLocation() == null);
    }
}
