package com.dpolit.customspawnpoint.domain;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TaskManager {
    private final Map<Player, BukkitTask> activeTasks = new HashMap<>();

    public void addTask(Player player, BukkitTask task) {
        activeTasks.put(player, task);
    }

    public void removeTask(Player player) {
        BukkitTask task = activeTasks.remove(player);
        Optional.ofNullable(task).ifPresent(BukkitTask::cancel);
    }

    public boolean isTaskActive(Player player) {
        return activeTasks.containsKey(player);
    }
}
