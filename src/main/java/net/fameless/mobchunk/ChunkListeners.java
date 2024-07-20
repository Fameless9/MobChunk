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

    public ChunkListeners(MobChunkPlugin mobChunkPlugin) {
        this.mobChunkPlugin = mobChunkPlugin;
        List<String> mobsToExclude = mobChunkPlugin.getConfig().getStringList("excluded-mobs");
        Arrays.stream(EntityType.values()).filter(entityType -> !mobsToExclude.contains(entityType.name())).forEach(availableMobs::add);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPopulate(ChunkPopulateEvent event) {
        if (!chunkMobHashMap.containsKey(event.getChunk())) {
            updateMob(event.getChunk());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!mobChunkPlugin.getTimer().isRunning()) return;
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) return;
        killLastSpawnedEntity();
        focusChunk(event.getTo().getChunk());
        EntityType mob = getMob(event.getTo().getChunk());
        lastSpawned = event.getTo().getWorld().spawnEntity(getCenter(event.getTo().getChunk()), mob);
        Bukkit.broadcast(Lang.getCaption("kill-mob-message", mob));
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (focusedChunk == null) return;
        if (event.getEntityType() == getMob(focusedChunk)) {
            resetFocus(event.getEntity().getWorld());
            lastSpawned = null;
        }
    }

    @NotNull
    private EntityType getMob(Chunk chunk) {
        return chunkMobHashMap.computeIfAbsent(chunk, c -> availableMobs.get(random.nextInt(availableMobs.size())));
    }

    public void updateMob(Chunk chunk) {
        EntityType mob = availableMobs.get(random.nextInt(availableMobs.size()));
        chunkMobHashMap.put(chunk, mob);
    }

    private void focusChunk(Chunk chunk) {
        chunk.getWorld().getWorldBorder().setCenter(getCenter(chunk));
        chunk.getWorld().getWorldBorder().setSize(16);
        focusedChunk = chunk;
    }

    public void resetFocus(World world) {
        world.getWorldBorder().reset();
        focusedChunk = null;
    }

    private Location getCenter(Chunk chunk) {
        Location location = chunk.getBlock(8, 64, 8).getLocation();
        location.setY(location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) + 1);
        return location;
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
