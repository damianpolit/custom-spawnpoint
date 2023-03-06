package com.dpolit.customspawnpoint.listeners;

import com.dpolit.customspawnpoint.domain.SpawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.dpolit.customspawnpoint.util.PluginUtil.getTeleportationCoords;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            player.teleport(getTeleportationCoords(SpawnEvent.FIRSTJOIN));
        }
    }
}
