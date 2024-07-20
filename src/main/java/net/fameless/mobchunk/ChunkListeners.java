package net.fameless.mobchunk;

import net.fameless.mobchunk.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChunkListeners implements Listener {

    private final MobChunkPlugin mobChunkPlugin;
    private final HashMap<Chunk, EntityType> chunkMobHashMap = new HashMap<>();
    private final Random random = new Random();
    private final List<EntityType> availableMobs = new ArrayList<>();
    private Chunk focusedChunk = null;
    private Entity lastSpawned = null;

    public ChunkListeners(@NotNull MobChunkPlugin mobChunkPlugin) {
        this.mobChunkPlugin = mobChunkPlugin;
        List<String> mobsToExclude = mobChunkPlugin.getConfig().getStringList("excluded-mobs");
        Arrays.stream(EntityType.values()).filter(entityType -> !mobsToExclude.contains(entityType.name())).forEach(availableMobs::add);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPopulate(@NotNull ChunkPopulateEvent event) {
        if (!chunkMobHashMap.containsKey(event.getChunk())) {
            updateMob(event.getChunk());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!mobChunkPlugin.getTimer().isRunning()) return;
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) return;
        update(event.getTo().getChunk());
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (focusedChunk == null) return;
        if (event.getEntityType() == getMob(focusedChunk)) {
            resetFocus(event.getEntity().getWorld());
            lastSpawned = null;
        }
    }

    private void update(Chunk chunk) {
        killLastSpawnedEntity();
        focusChunk(chunk);
        EntityType mob = getMob(chunk);
        lastSpawned = chunk.getWorld().spawnEntity(getCenter(chunk), mob);
        Bukkit.broadcastMessage(Lang.getCaption("kill-mob-message", mob));
    }

    @NotNull
    private EntityType getMob(Chunk chunk) {
        return chunkMobHashMap.computeIfAbsent(chunk, c -> availableMobs.get(random.nextInt(availableMobs.size())));
    }

    public void updateMob(Chunk chunk) {
        EntityType mob = availableMobs.get(random.nextInt(availableMobs.size()));
        chunkMobHashMap.put(chunk, mob);
    }

    private void focusChunk(@NotNull Chunk chunk) {
        chunk.getWorld().getWorldBorder().setCenter(getCenter(chunk));
        chunk.getWorld().getWorldBorder().setSize(16);
        focusedChunk = chunk;
    }

    public void resetFocus(@NotNull World world) {
        world.getWorldBorder().reset();
        focusedChunk = null;
    }

    @NotNull
    private Location getCenter(@NotNull Chunk chunk) {
        Location location = chunk.getBlock(8, 64, 8).getLocation();
        location.setY(location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) + 1);
        return location;
    }

    public void handleStart() {
        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        Chunk chunk = Bukkit.getOnlinePlayers().stream().toList().get(0).getLocation().getChunk();
        update(chunk);
    }

    public void handlePause() {
        killLastSpawnedEntity();
        for (World world : Bukkit.getServer().getWorlds()) {
            resetFocus(world);
        }
    }

    public void handleShutdown() {
        for (World world : Bukkit.getServer().getWorlds()) {
            resetFocus(world);
        }
    }

    public void killLastSpawnedEntity() {
        if (lastSpawned != null) {
            lastSpawned.remove();
            lastSpawned = null;
        }
    }

    public Chunk getFocusedChunk() {
        return focusedChunk;
    }
}
