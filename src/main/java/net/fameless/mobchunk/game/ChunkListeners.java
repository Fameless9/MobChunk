package net.fameless.mobchunk.game;

import net.fameless.mobchunk.MobChunkPlugin;
import net.fameless.mobchunk.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChunkListeners implements Listener {

    private final MobChunkPlugin mobChunkPlugin;
    private final Random random = new Random();
    private final HashMap<Chunk, EntityType> chunkMobHashMap = new HashMap<>();
    private final List<EntityType> availableMobs = new ArrayList<>();
    private Chunk focusedChunk = null;
    private Entity lastSpawned = null;

    public ChunkListeners(@NotNull MobChunkPlugin mobChunkPlugin) {
        this.mobChunkPlugin = mobChunkPlugin;
        updateAvailableMobs();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPopulate(@NotNull ChunkPopulateEvent event) {
        if (!chunkMobHashMap.containsKey(event.getChunk())) {
            updateMob(event.getChunk());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        if (event.getTo() == null) return;
        if (!mobChunkPlugin.getTimer().isRunning()) return;
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == event.getPlayer()) continue;
            player.teleport(event.getTo());
        }
        update(event.getTo());
    }

    // To handle creeper explosions
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (focusedChunk == null) return;
        if (event.getEntityType() == lastSpawned.getType()) {
            resetFocus(event.getEntity().getWorld());
            lastSpawned = null;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (focusedChunk == null) return;
        if (event.getEntityType() == lastSpawned.getType()) {
            resetFocus(event.getEntity().getWorld());
            lastSpawned = null;
        }
    }

    // Prevent entity to teleport out of chunk
    @EventHandler(ignoreCancelled = true)
    public void onEntityTeleport(@NotNull EntityTeleportEvent event) {
        if (event.getTo() == null) return;
        if (focusedChunk == null) return;
        if (event.getEntity() != lastSpawned) return;
        if (event.getTo().getChunk() == focusedChunk) return;
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPortal(@NotNull EntityPortalEvent event) {
        if (event.getTo() == null) return;
        if (focusedChunk == null) return;
        if (event.getEntity() != lastSpawned) return;
        if (event.getTo().getChunk() == focusedChunk) return;
        event.setCancelled(true);
    }

    private void updateAvailableMobs() {
        mobChunkPlugin.reloadConfig();
        List<String> mobsToExclude = mobChunkPlugin.getConfig().getStringList("excluded-mobs");
        availableMobs.clear();
        Arrays.stream(EntityType.values()).filter(entityType -> !mobsToExclude.contains(entityType.name())).forEach(availableMobs::add);
    }

    private void update(@NotNull Location playerLoc) {
        Chunk chunk = playerLoc.getChunk();
        killLastSpawnedEntity();
        focusChunk(playerLoc);
        EntityType mob = getMob(chunk);
        lastSpawned = chunk.getWorld().spawnEntity(getCenter(playerLoc), mob);
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

    private void focusChunk(@NotNull Location playerLoc) {
        playerLoc.getChunk().getWorld().getWorldBorder().setCenter(getCenter(playerLoc));
        playerLoc.getChunk().getWorld().getWorldBorder().setSize(16);
        focusedChunk = playerLoc.getChunk();
    }

    public void resetFocus(@NotNull World world) {
        world.getWorldBorder().reset();
        focusedChunk = null;
    }

    @NotNull
    private Location getCenter(@NotNull Location playerLoc) {
        Location chunkCenter = playerLoc.getChunk().getBlock(8, 0, 8).getLocation();
        Location location = playerLoc.clone();
        location.setX(chunkCenter.getX());
        location.setZ(chunkCenter.getZ());

        if (location.getWorld().getBlockAt(location.subtract(0, 1, 0)).isEmpty()) {
            while (location.getWorld().getBlockAt(location.subtract(0, 1, 0)).isEmpty()) {
                location.subtract(0, 1, 0);
            }
            location.add(0, 2, 0);
        } else {
            while (!location.getWorld().getBlockAt(location.add(0, 1, 0)).isEmpty()) {
                location.add(0, 1, 0);
            }
            location.subtract(0, 1, 0);
        }
        return location;
    }

    public void handleStart() {
        Bukkit.getOnlinePlayers().stream().findFirst().ifPresent(player -> update(player.getLocation()));
    }

    public void handlePause() {
        killLastSpawnedEntity();
        Bukkit.getServer().getWorlds().forEach(this::resetFocus);
    }

    public void handleShutdown() {
        Bukkit.getServer().getWorlds().forEach(this::resetFocus);
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
