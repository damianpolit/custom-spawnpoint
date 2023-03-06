package com.dpolit.customspawnpoint.listeners;

import com.dpolit.customspawnpoint.domain.SpawnEvent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.dpolit.customspawnpoint.CustomSpawnpoint.getPlugin;
import static com.dpolit.customspawnpoint.util.PlayerParticlesManager.spawnParticles;
import static com.dpolit.customspawnpoint.util.PluginUtil.getTeleportationCoords;
import static com.dpolit.customspawnpoint.util.SoundEffectManager.playSound;

public class PlayerJoinListener implements Listener {

    private final FileConfiguration config = getPlugin().getConfig();

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (shouldPlayerBeTeleported(player)) {
            player.teleport(getTeleportationCoords(SpawnEvent.FIRSTJOIN));
            spawnParticles(player, Particle.CAMPFIRE_COSY_SMOKE);
            playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        }
    }

    private boolean shouldPlayerBeTeleported(Player player) {
        return config.getBoolean("data.spawnpoints.firstjoin.enable") && !player.hasPlayedBefore();
    }
}
