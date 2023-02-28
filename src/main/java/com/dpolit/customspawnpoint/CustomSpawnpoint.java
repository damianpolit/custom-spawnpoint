package com.dpolit.customspawnpoint;

import com.dpolit.customspawnpoint.commands.ReloadCommand;
import com.dpolit.customspawnpoint.commands.SetSpawnCommand;
import com.dpolit.customspawnpoint.commands.SpawnCommand;
import com.dpolit.customspawnpoint.domain.TaskManager;
import com.dpolit.customspawnpoint.util.LanguageManager;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

        getConsole().sendMessage(getPrefix() + "§aPlugin was activated successfully.");

    }

    private void registerCommands() {
        getCommand("setspawn").setExecutor(new SetSpawnCommand(languageManager, getConfig()));
        getCommand("spawn").setExecutor(new SpawnCommand(getConfig(), languageManager, taskManager));
        getCommand("customSpawnpoint-reload").setExecutor(new ReloadCommand(languageManager));
    }

    @Override
    public void saveConfig() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(getDataFolder(), "config.yml")));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(getDataFolder(), "config.yml")))) {

            final TreeMap<Integer, String> comments = bufferedReader.lines()
                    .filter(line -> line.contains("#") || line.trim().isEmpty())
                    .collect(Collectors.toMap(String::hashCode, line -> line, (a, b) -> a, TreeMap::new));

            final List<String> toSave = getConfig().saveToString().lines()
                    .filter(line -> !line.contains("#"))
                    .collect(Collectors.toList());

            comments.forEach(toSave::add);

            final String result = String.join("\n", toSave);
            bufferedWriter.write(result);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
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
