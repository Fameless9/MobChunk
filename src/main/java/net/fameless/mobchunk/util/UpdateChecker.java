package net.fameless.mobchunk.util;

import net.fameless.mobchunk.MobChunkPlugin;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;

public class UpdateChecker {

    private final MobChunkPlugin mobChunkPlugin;
    private final int resourceId;
    private final Duration checkInterval;
    private Instant lastCheckTime;

    public UpdateChecker(MobChunkPlugin mobChunkPlugin, int resourceId, Duration checkInterval) {
        this.mobChunkPlugin = mobChunkPlugin;
        this.resourceId = resourceId;
        this.checkInterval = checkInterval;
        this.lastCheckTime = Instant.MIN;
        checkForUpdates();
    }

    public void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(mobChunkPlugin, () -> {
            Instant currentTime = Instant.now();
            if (Duration.between(lastCheckTime, currentTime).compareTo(checkInterval) < 0) {
                return;
            }

            try {
                URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                String latestVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

                String currentVersion = mobChunkPlugin.getDescription().getVersion();
                if (latestVersion != null && !latestVersion.equalsIgnoreCase(currentVersion)) {
                    mobChunkPlugin.getLogger().info("A new update is available! Version " + latestVersion + " can be downloaded from the SpigotMC website: https://www.spigotmc.org/resources/" + resourceId);
                }
                lastCheckTime = currentTime;
            } catch (IOException e) {
                mobChunkPlugin.getLogger().log(Level.WARNING, "Failed to check for updates: " + e.getMessage(), e);
            }
        });
    }
}
