package com.dpolit.customspawnpoint.util;

import com.dpolit.customspawnpoint.CustomSpawnpoint;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Optional;

import static com.dpolit.customspawnpoint.CustomSpawnpoint.getConsole;
import static com.dpolit.customspawnpoint.util.ConfigShortcuts.LANGUAGE_CONFIG;

public class LanguageManager {

    private static final String LANG_FILE_NAME_FORMAT = "LANG_%s.yml";
    private static final String LANG_DIRECTORY = "/LANG/";

    private String language;
    private FileConfiguration languageFile;

    public LanguageManager(final String language) {
        CustomSpawnpoint.getPlugin().saveResource("LANG/LANG_EN.yml", false);

        this.language = language;

        loadLangFile();
    }

    private Optional<File> getLangFile(String lang) {
        String langFileName = String.format(LANG_FILE_NAME_FORMAT, lang);
        File langFile = new File(CustomSpawnpoint.getPlugin().getDataFolder() + LANG_DIRECTORY + langFileName);
        return langFile.exists() ? Optional.of(langFile) : Optional.empty();
    }

    public void loadLangFile() {
        Optional.ofNullable(language)
                .flatMap(this::getLangFile)
                .map(YamlConfiguration::loadConfiguration)
                .ifPresentOrElse(langFile -> this.languageFile = langFile,
                        () -> getConsole().sendMessage(CustomSpawnpoint.getPlugin().getPrefix() + "§cAn error occurred while loading the language file!"));
    }

    public void reloadLanguage() {
        this.language = CustomSpawnpoint.getPlugin().getConfig().getString(LANGUAGE_CONFIG);

        loadLangFile();
    }

    public void sendMessage(final CommandSender commandSender, final String path, final String... placeholders) {
        getText(path)
                .filter(text -> !text.equals("%noMessage%"))
                .map(text -> {
                    String message = text;
                    for (final String placeholder : placeholders) {
                        message = message.replaceAll(placeholder.split(":")[0], placeholder.split(":")[1]);
                    }
                    return message;
                })
                .ifPresent(commandSender::sendMessage);
    }

    private Optional<String> getText(final String path) {
        return Optional.ofNullable(languageFile)
                .map(text -> text.getString(path))
                .map(lang -> lang.replace((char) 194 + "", "")
                        .replace("&", "§"));
    }
}
