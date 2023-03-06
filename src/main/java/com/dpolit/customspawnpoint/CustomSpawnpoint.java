package com.dpolit.customspawnpoint;

import com.dpolit.customspawnpoint.commands.ReloadCommand;
import com.dpolit.customspawnpoint.commands.SetSpawnCommand;
import com.dpolit.customspawnpoint.commands.SpawnCommand;
import com.dpolit.customspawnpoint.domain.TaskManager;
import com.dpolit.customspawnpoint.listeners.PlayerRespawnListener;
import com.dpolit.customspawnpoint.util.LanguageManager;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

import static com.dpolit.customspawnpoint.util.ConfigShortcuts.LANGUAGE_CONFIG;

@Slf4j
public final class CustomSpawnpoint extends JavaPlugin {

    private static CustomSpawnpoint plugin;
    private LanguageManager languageManager;
    private TaskManager taskManager;

    public static void main(final String[] args) {
        log.debug("Minecraft Plugin, created by Ahmyia.");
    }

    @Override
    public void onEnable() {
        plugin = this;
        taskManager = new TaskManager();

        saveDefaultConfig();
        reloadConfig();
        getConsole().sendMessage(getPrefix() + "§aconfig.yml was loaded successfully.");

        languageManager = new LanguageManager(getConfig().getString(LANGUAGE_CONFIG));
        getConsole().sendMessage(getPrefix() + "§aLanguage: " + getConfig().getString(LANGUAGE_CONFIG) + ".yml was loaded successfully.");

        registerCommands();
        registerListeners();

        getConsole().sendMessage(getPrefix() + "§aPlugin was activated successfully.");

    }

    private void registerCommands() {
        getCommand("setspawn").setExecutor(new SetSpawnCommand(languageManager));
        getCommand("spawn").setExecutor(new SpawnCommand(languageManager, taskManager));
        getCommand("customSpawnpoint-reload").setExecutor(new ReloadCommand(languageManager));
    }

    private void registerListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerRespawnListener(), this);
    }

    public static CustomSpawnpoint getPlugin() {
        return plugin;
    }

    public String getPrefix() {
        return Optional.ofNullable(getConfig().getString("settings.prefix"))
                .map(prefix -> prefix.replace("&", "§"))
                .orElse("");
    }

    public static ConsoleCommandSender getConsole() {
        return Bukkit.getConsoleSender();
    }
}
