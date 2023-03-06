package com.dpolit.customspawnpoint.commands;

import com.dpolit.customspawnpoint.CustomSpawnpoint;
import com.dpolit.customspawnpoint.domain.Cords;
import com.dpolit.customspawnpoint.domain.SpawnEvent;
import com.dpolit.customspawnpoint.util.LanguageManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.dpolit.customspawnpoint.CustomSpawnpoint.getPlugin;
import static com.dpolit.customspawnpoint.util.LanguageShortcuts.SET_SPAWN_MESSAGE;

public class SetSpawnCommand implements CommandExecutor {

    private final LanguageManager languageManager;
    private final FileConfiguration config = getPlugin().getConfig();

    public SetSpawnCommand(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Optional<Player> playerOptional = Optional.of(sender)
                .filter(Player.class::isInstance)
                .map(Player.class::cast);

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();

            playerOptional.filter(this::playerHasPermission)
                    .filter(p -> args.length == 0)
                    .ifPresent(p -> setSpawnLocation(player.getLocation(), getType(null)));


            playerOptional.filter(this::playerHasPermission)
                    .filter(p -> args.length == 1)
                    .ifPresent(p -> setSpawnLocation(player.getLocation(), getType(args[0])));

            playerOptional.filter(this::playerHasPermission)
                    .filter(p -> args.length == 3 || args.length == 4)
                    .map(p -> parseArgs(args))
                    .ifPresent(coords -> setSpawnLocation(new Location(
                                    player.getWorld(),
                                    coords.getX(),
                                    coords.getY(),
                                    coords.getZ(),
                                    player.getLocation().getYaw(),
                                    player.getLocation().getPitch()),
                            coords.getType()));

            languageManager.sendMessage(player, SET_SPAWN_MESSAGE.concat("success"));
            return true;
        }

        return false;
    }

    private void setSpawnLocation(Location location, SpawnEvent type) {
        setConfigSpawnpointLocation(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                getType(type.toString())
        );
    }

    private Cords parseArgs(String[] args) throws NumberFormatException {
        return new Cords(
                Double.parseDouble(args[0]),
                Double.parseDouble(args[1]),
                Double.parseDouble(args[2]),
                getType(args[3])
        );
    }

    private SpawnEvent getType(String arg) {
        SpawnEvent result;
        try {
            result = arg != null ? SpawnEvent.valueOf(arg.toUpperCase()) : SpawnEvent.DEFAULT;
        } catch (IllegalArgumentException e) {
            result = SpawnEvent.DEFAULT;
        }
        return result;
    }

    private void setConfigSpawnpointLocation(String world, double x, double y, double z, double yaw, double pitch, SpawnEvent type) {
        String eventType = type.toString().toLowerCase();
        config.set("data.spawnpoints." + eventType + ".world", world);
        config.set("data.spawnpoints." + eventType + ".x", x);
        config.set("data.spawnpoints." + eventType + ".y", y);
        config.set("data.spawnpoints." + eventType + ".z", z);
        config.set("data.spawnpoints." + eventType + ".yaw", yaw);
        config.set("data.spawnpoints." + eventType + ".pitch", pitch);

        CustomSpawnpoint.getPlugin().saveConfig();
    }

    private boolean playerHasPermission(CommandSender sender) {
        String permission = config.getString("setSpawnpoint", "");
        return Optional.ofNullable(sender)
                .filter(s -> s.hasPermission(permission))
                .isPresent();
    }
}
