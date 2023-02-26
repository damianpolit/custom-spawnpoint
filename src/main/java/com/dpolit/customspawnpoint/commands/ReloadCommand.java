package com.dpolit.customspawnpoint.commands;

import com.dpolit.customspawnpoint.CustomSpawnpoint;
import com.dpolit.customspawnpoint.util.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.dpolit.customspawnpoint.util.ConfigShortcuts.RELOAD_PERMISSION;
import static com.dpolit.customspawnpoint.util.LanguageShortcuts.GENERAL_NO_PERMISSIONS_MESSAGE;
import static com.dpolit.customspawnpoint.util.LanguageShortcuts.RELOAD_SUCCESS_MESSAGE;

public class ReloadCommand implements CommandExecutor {

    private final LanguageManager languageManager;

    public ReloadCommand(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Optional.of(sender)
                .filter(s -> s.hasPermission(RELOAD_PERMISSION))
                .ifPresentOrElse(s -> {
                    CustomSpawnpoint.getPlugin().reloadConfig();
                    languageManager.reloadLanguage();

                    languageManager.sendMessage(s, RELOAD_SUCCESS_MESSAGE);
                }, () -> languageManager.sendMessage(sender, GENERAL_NO_PERMISSIONS_MESSAGE));

        return true;
    }
}
