package com.dpolit.customspawnpoint.listeners;

import com.dpolit.customspawnpoint.domain.TaskManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final TaskManager taskManager;

    public PlayerMoveListener(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        //
    }
}
