package com.dpolit.customspawnpoint.listeners;

import com.dpolit.customspawnpoint.domain.SpawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.dpolit.customspawnpoint.util.PluginUtil.getTeleportationCoords;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.teleport(getTeleportationCoords(SpawnEvent.DEATH));
    }
}
