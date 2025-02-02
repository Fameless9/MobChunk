package net.fameless.mobchunk.game;

import net.fameless.mobchunk.MobChunkPlugin;
import net.fameless.mobchunk.language.Lang;
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
            sender.sendMessage(Lang.getCaption("not-a-player"));
            return false;
        }
        if (mobChunkPlugin.getChunkListeners().getFocusedChunk() == null) {
            player.sendMessage(Lang.getCaption("no-mob-to-skip"));
            return false;
        }

        mobChunkPlugin.getChunkListeners().updateMob(mobChunkPlugin.getChunkListeners().getFocusedChunk());
        mobChunkPlugin.getChunkListeners().resetFocus(player.getWorld());
        mobChunkPlugin.getChunkListeners().killLastSpawnedEntity();
        Bukkit.broadcastMessage(Lang.getCaption("mob-skipped"));
        return false;
    }
}
