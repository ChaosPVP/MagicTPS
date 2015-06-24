package me.vemacs.magictps;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MagicTPS extends JavaPlugin implements Listener {
    private int tpsMultiplier;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        tpsMultiplier = getConfig().getInt("tps-multiplier");
    }

    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().split(" ")[0].equalsIgnoreCase("/tps")) return;
        if (!event.getPlayer().hasPermission("bukkit.command.tps")) return;
        event.setCancelled(true);
        double[] actualTps = TPSUtil.getRecentTps();
        StringBuilder sb = new StringBuilder(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: ");
        for (double tps : actualTps) {
            sb.append(format(tps));
            sb.append(", ");
        }
        event.getPlayer().sendMessage(sb.substring(0, sb.length() - 2));
    }

    private String format(double actualTps) {
        double magicTps = tpsMultiplier * actualTps;
        double maxMagicTps = 20 * tpsMultiplier;
        return ((magicTps > maxMagicTps - 2) ? ChatColor.GREEN :
                (magicTps > maxMagicTps - 4) ? ChatColor.YELLOW : ChatColor.RED).toString()
                + ((magicTps > maxMagicTps) ? "*" : "")
                + Math.min(Math.round(magicTps * 100.0) / 100.0, maxMagicTps);
    }
}