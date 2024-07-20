package net.fameless.mobchunk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkipCommand implements CommandExecutor {

    private final MobChunkPlugin mobChunkPlugin;

    public SkipCommand(MobChunkPlugin mobChunkPlugin) {
        this.mobChunkPlugin = mobChunkPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Nur spieler können diesen Command benutzen!", NamedTextColor.RED));
            return false;
        }
        if (mobChunkPlugin.getChunkListeners().getFocusedChunk() == null) {
            player.sendMessage(Component.text("Es gibt keinen Mob zum skippen.", NamedTextColor.RED));
            return false;
        }

        mobChunkPlugin.getChunkListeners().updateMob(mobChunkPlugin.getChunkListeners().getFocusedChunk());
        mobChunkPlugin.getChunkListeners().resetFocus(player.getWorld());
        mobChunkPlugin.getChunkListeners().killLastSpawnedEntity();
        Bukkit.broadcast(Component.text("Der Mob für diesen Chunk wurde geskippt.", NamedTextColor.GREEN));
        return false;
    }
}
