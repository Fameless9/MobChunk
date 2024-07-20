package net.fameless.mobchunk;

import net.fameless.mobchunk.util.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class MobChunkPlugin extends JavaPlugin {

    private ChunkListeners chunkListeners;
    private Timer timer;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        chunkListeners = new ChunkListeners(this);

        int timerTime = getConfig().getInt("timer.time", 0);
        timer = new Timer(this, false, timerTime);

        new UpdateChecker(this, 118197, Duration.ofHours(2));
        new Metrics(this, 22714);

        getServer().getPluginManager().registerEvents(chunkListeners, this);

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
}
