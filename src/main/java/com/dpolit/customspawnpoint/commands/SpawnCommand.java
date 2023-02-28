package com.dpolit.customspawnpoint.commands;

import com.dpolit.customspawnpoint.domain.TaskManager;
import com.dpolit.customspawnpoint.util.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.dpolit.customspawnpoint.CustomSpawnpoint.getPlugin;
import static com.dpolit.customspawnpoint.util.PlayerParticlesManager.spawnParticles;
import static com.dpolit.customspawnpoint.util.SoundEffectManager.playSound;

public class SpawnCommand implements CommandExecutor {

    private final FileConfiguration config;
    private final LanguageManager languageManager;
    private final TaskManager taskManager;

    public SpawnCommand(FileConfiguration config, LanguageManager languageManager, TaskManager taskManager) {
        this.config = config;
        this.languageManager = languageManager;
        this.taskManager = taskManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Optional.of(sender)
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .ifPresent(player -> {
                    if (taskManager.isTaskActive(player)) {
                        languageManager.sendMessage(player, "messages.commands.spawn.wait");
                        return;
                    }
                    int delay = getDelay();
                    Location loc = player.getLocation();
                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, delay * 20, 2);
                    player.addPotionEffect(potionEffect);
                    taskManager.addTask(player, new BukkitRunnable() {
                        int countDown = delay;
                        final Location spawn = new Location(
                                Bukkit.getWorld(Optional.ofNullable(config)
                                        .map(c -> c.getString("data.spawnpoints.default.world"))
                                        .orElse("")),
                                config.getDouble("data.spawnpoints.default.x"),
                                config.getDouble("data.spawnpoints.default.y"),
                                config.getDouble("data.spawnpoints.default.z"),
                                config.getLong("data.spawnpoints.default.yaw"),
                                config.getLong("data.spawnpoints.default.pitch"));

                        @Override
                        public void run() {
                            if (countDown <= 0) {
                                player.teleport(spawn);
                                player.removePotionEffect(PotionEffectType.BLINDNESS);
                                languageManager.sendMessage(player, "messages.commands.spawn.success");
                                taskManager.removeTask(player);
                                cancel();
                            } else {
                                if (hasPlayerMoved(player, loc)) {
                                    languageManager.sendMessage(player, "messages.commands.spawn.cancelled");
                                    taskManager.removeTask(player);
                                    cancel();
                                    return;
                                }
                                playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                                spawnParticles(player, Particle.WHITE_ASH);
                                player.sendMessage(ChatColor.GREEN + "Teleporting to spawn in " + countDown + " seconds. Don't move!");
                                countDown--;
                            }
                        }
                    }.runTaskTimer(getPlugin(), 0L, 20L));
                });
        return true;
    }

    private int getDelay() {
        return getPlugin().getConfig().getInt("settings.delay");
    }

    private boolean hasPlayerMoved(Player player, Location originalLocation) {
        return !player.getLocation().equals(originalLocation);
    }
}
