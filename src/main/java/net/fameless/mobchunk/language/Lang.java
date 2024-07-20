package net.fameless.mobchunk.language;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fameless.mobchunk.MobChunkPlugin;
import net.fameless.mobchunk.util.Format;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Lang {

    private static JsonObject langObject = null;
    private static Language language;

    public static void loadLanguage(@NotNull MobChunkPlugin mobChunkPlugin) {
        mobChunkPlugin.reloadConfig();
        String lang = mobChunkPlugin.getConfig().getString("lang", "en");
        File jsonFile;
        switch (lang) {
            case "de": {
                try {
                    jsonFile = new File(mobChunkPlugin.getDataFolder(), "lang_de.json");
                    langObject = JsonParser.parseReader(new FileReader(jsonFile)).getAsJsonObject();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                language = Language.GERMAN;
                break;
            }
            case "en": {
                try {
                    jsonFile = new File(mobChunkPlugin.getDataFolder(), "lang_en.json");
                    langObject = JsonParser.parseReader(new FileReader(jsonFile)).getAsJsonObject();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                language = Language.ENGLISH;
                break;
            }
        }
    }

    public static @NotNull Component getCaption(String path) {
        String message = langObject.get(path).getAsString();
        message = message.replace("{timer.time}", Format.formatTime(MobChunkPlugin.get().getTimer().getTime()));
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static @NotNull Component getCaption(String path, @NotNull EntityType mobReplacement) {
        String message = langObject.get(path).getAsString();
        message = message.replace("{timer.time}", Format.formatTime(MobChunkPlugin.get().getTimer().getTime()))
                .replace("{mob}", Format.formatName(mobReplacement.name()));
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static Language getLanguage() {
        return language;
    }
}
