package net.fameless.mobchunk.language;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fameless.mobchunk.MobChunkPlugin;
import net.fameless.mobchunk.util.Format;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

    public static @NotNull String getCaption(String path) {
        String prefix = langObject.get("prefix").getAsString();
        String message = langObject.get(path).getAsString();
        message = message.replace("{timer.time}", Format.formatTime(MobChunkPlugin.get().getTimer().getTime()))
                .replace("{prefix}", prefix);
        return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(message));
    }

    public static @NotNull String getCaption(String path, @NotNull EntityType mobReplacement) {
        String prefix = langObject.get("prefix").getAsString();
        String message = langObject.get(path).getAsString();
        message = message
                .replace("{mob}", Format.formatName(mobReplacement.name()))
                .replace("{timer.time}", Format.formatTime(MobChunkPlugin.get().getTimer().getTime()))
                .replace("{prefix}", prefix);
        return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(message));
    }

    public static Language getLanguage() {
        return language;
    }
}
