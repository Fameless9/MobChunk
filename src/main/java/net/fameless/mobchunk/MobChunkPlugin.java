package net.fameless.mobchunk;

import net.fameless.mobchunk.game.ChunkListeners;
import net.fameless.mobchunk.game.SkipCommand;
import net.fameless.mobchunk.game.Timer;
import net.fameless.mobchunk.language.Lang;
import net.fameless.mobchunk.language.LanguageCommand;
import net.fameless.mobchunk.util.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class MobChunkPlugin extends JavaPlugin {

    private static MobChunkPlugin instance;
    private ChunkListeners chunkListeners;
    private Timer timer;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("lang_de.json", false);
        saveResource("lang_en.json", false);

        Lang.loadLanguage(this);

        chunkListeners = new ChunkListeners(this);

        int timerTime = getConfig().getInt("timer.time", 0);
        timer = new Timer(this, false, timerTime);

        new UpdateChecker(this, 118197, Duration.ofHours(2));
        new Metrics(this, 22714);

        LanguageCommand languageCommand = new LanguageCommand(this);

        getServer().getPluginManager().registerEvents(chunkListeners, this);
        getServer().getPluginManager().registerEvents(languageCommand, this);

        getCommand("language").setExecutor(languageCommand);

        getCommand("skip").setExecutor(new SkipCommand(this));

        getCommand("timer").setExecutor(timer);
        getCommand("timer").setTabCompleter(timer);
    }

    @Override
    public void onDisable() {
        chunkListeners.handleShutdown();

        getConfig().set("timer.time", timer.getTime());
        saveConfig();
    }

    public Timer getTimer() {
        return timer;
    }

    public ChunkListeners getChunkListeners() {
        return chunkListeners;
    }

    public static MobChunkPlugin get() {
        return instance;
    }
}
