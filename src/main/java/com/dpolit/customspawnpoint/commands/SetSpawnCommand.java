package com.dpolit.customspawnpoint.commands;

import com.dpolit.customspawnpoint.CustomSpawnpoint;
import com.dpolit.customspawnpoint.util.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.dpolit.customspawnpoint.util.LanguageShortcuts.SET_SPAWN_MESSAGE;

public class SetSpawnCommand implements CommandExecutor {

    private final LanguageManager languageManager;
    private final FileConfiguration config;

    public SetSpawnCommand(LanguageManager languageManager, FileConfiguration config) {
        this.languageManager = languageManager;
        this.config = config;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Optional<Player> playerOptional = Optional.of(sender)
                .filter(Player.class::isInstance)
                .map(Player.class::cast);

        playerOptional.filter(this::playerHasPermission)
                .filter(p -> args.length == 0)
                .ifPresent(player -> {
                    setConfigSpawnpointLocation(
                            getWorldName(player),
                            player.getLocation().getX(),
                            player.getLocation().getY(),
                            player.getLocation().getZ(),
                            player.getLocation().getYaw(),
                            player.getLocation().getPitch());

                    languageManager.sendMessage(player, SET_SPAWN_MESSAGE.concat("success"));
                });

        playerOptional.filter(this::playerHasPermission)
                .filter(p -> args.length == 3)
                .ifPresent(player -> {
                    double x = Double.parseDouble(args[0]);
                    double y = Double.parseDouble(args[1]);
                    double z = Double.parseDouble(args[2]);

                    setConfigSpawnpointLocation(
                            getWorldName(player),
                            x, y, z,
                            player.getLocation().getYaw(),
                            player.getLocation().getPitch()
                    );

                    languageManager.sendMessage(player, SET_SPAWN_MESSAGE.concat("success"));
                });

        playerOptional.filter(this::playerHasPermission)
                .filter(p -> args.length == 5)
                .ifPresent(player -> {
                    double x = Double.parseDouble(args[0]);
                    double y = Double.parseDouble(args[1]);
                    double z = Double.parseDouble(args[2]);
                    double yaw = Double.parseDouble(args[3]);
                    double pitch = Double.parseDouble(args[4]);

                    setConfigSpawnpointLocation(
                            getWorldName(player),
                            x, y, z,
                            yaw, pitch
                    );

                    languageManager.sendMessage(player, SET_SPAWN_MESSAGE.concat("success"));
                });

        return true;
    }

    private String getWorldName(Player player) {
        return Optional.ofNullable(Bukkit.getWorld(player.getWorld().getName()))
                .map(WorldInfo::getName)
                .orElse("");
    }

    private void setConfigSpawnpointLocation(String world, double x, double y, double z, double yaw, double pitch) {
        config.set("data.spawnpoints.default.world", world);
        config.set("data.spawnpoints.default.x", x);
        config.set("data.spawnpoints.default.y", y);
        config.set("data.spawnpoints.default.z", z);
        config.set("data.spawnpoints.default.yaw", yaw);
        config.set("data.spawnpoints.default.pitch", pitch);

        CustomSpawnpoint.getPlugin().saveConfig();
    }

    private boolean playerHasPermission(CommandSender sender) {
        String permission = config.getString("setSpawnpoint", "");
        return Optional.ofNullable(sender)
                .filter(s -> s.hasPermission(permission))
                .isPresent();
    }
}
