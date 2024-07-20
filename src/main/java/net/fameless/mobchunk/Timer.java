package net.fameless.mobchunk;

import net.fameless.mobchunk.util.Format;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Timer implements CommandExecutor, TabCompleter {

    private final MobChunkPlugin mobChunkPlugin;
    private final Component usage = Component.text("Verwendung: /timer <toggle, set> <Zeit>.", NamedTextColor.RED);
    private boolean running;
    private int time;

    public Timer(MobChunkPlugin mobChunkPlugin, boolean running, int time) {
        this.mobChunkPlugin = mobChunkPlugin;
        this.running = running;
        this.time = time;
        runTask();
    }

    public boolean isRunning() {
        return running;
    }

    public int getTime() {
        return time;
    }

    private void runTask() {
        Bukkit.getScheduler().runTaskTimer(mobChunkPlugin, () -> {
            if (running) {
                time++;
            }
            sendActionbar();
        }, 20, 20);
    }

    private void sendActionbar() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (running) {
                p.sendActionBar(Component.text(Format.formatTime(time), NamedTextColor.GOLD));
            } else {
                p.sendActionBar(Component.text("Timer ist pausiert.", NamedTextColor.GOLD, TextDecoration.ITALIC));
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Nur spieler können diesen Command benutzen!", NamedTextColor.RED));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(usage);
            return false;
        }

        switch (args[0]) {
            case "toggle": {
                this.running = !running;
                if (!running) {
                    mobChunkPlugin.getChunkListeners().resetFocus(player.getWorld());
                }
                break;
            }
            case "set": {
                if (args.length < 2) {
                    sender.sendMessage(usage);
                    return false;
                }
                int time;
                try {
                    time = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Zeit muss eine Zahl sein!", NamedTextColor.RED));
                    return false;
                }
                this.time = time;
                break;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], List.of("set", "toggle"), new ArrayList<>());
        }
        if (args.length == 2 && args[1].isEmpty()) {
            return List.of("<time>");
        }
        return List.of();
    }
}
