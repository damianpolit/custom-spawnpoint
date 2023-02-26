package com.dpolit.customspawnpoint.commands;

import com.dpolit.customspawnpoint.exceptions.NotPlayerInstanceException;
import com.dpolit.customspawnpoint.util.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SpawnCommand implements CommandExecutor {

    private final FileConfiguration config;
    private final LanguageManager languageManager;

    public SpawnCommand(FileConfiguration config, LanguageManager languageManager) {
        this.config = config;
        this.languageManager = languageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Location cords =
                new Location(
                        Bukkit.getWorld(Optional.ofNullable(config)
                                .map(c -> c.getString("data.spawnpoints.default.world"))
                                .orElse("")),
                        config.getDouble("data.spawnpoints.default.x"),
                        config.getDouble("data.spawnpoints.default.y"),
                        config.getDouble("data.spawnpoints.default.z"),
                        config.getLong("data.spawnpoints.default.yaw"),
                        config.getLong("data.spawnpoints.default.pitch"));

        Player player = Optional.of(sender)
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .orElseThrow(NotPlayerInstanceException::new);

        languageManager.sendMessage(player, "messages.commands.spawn.success");

        player.teleport(cords);

        return true;
    }
}
