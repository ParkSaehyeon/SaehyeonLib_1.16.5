package me.saehyeon.saehyeonlib.main;

import me.saehyeon.saehyeonlib.dropitem.DropItemGUIEvent;
import me.saehyeon.saehyeonlib.event.onClick;
import me.saehyeon.saehyeonlib.itemplacer.ItemPlacerGUIEvent;
import me.saehyeon.saehyeonlib.event.onCommand;
import me.saehyeon.saehyeonlib.event.onInventory;
import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.role.ExPlayer;
import me.saehyeon.saehyeonlib.state.PlayerState;
import me.saehyeon.saehyeonlib.state.State;
import me.saehyeon.saehyeonlib.util.Stringf;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

public final class SaehyeonLib extends JavaPlugin {

    public static boolean debugMode;
    public static Scoreboard scoreboard;

    public static SaehyeonLib instance;

    @Override
    public void onEnable() {
        instance = this;

        getDataFolder().mkdir();

        Bukkit.getPluginCommand("s-lib").setExecutor(new onCommand());

        Bukkit.getPluginManager().registerEvents(new onInventory(), this);
        Bukkit.getPluginManager().registerEvents(new onClick(), this);

        SaehyeonLibEvent.register(new ItemPlacerGUIEvent());
        SaehyeonLibEvent.register(new DropItemGUIEvent());

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        log("\n\nSaehyeonLib는 자체적인 스코어보드를 이용합니다. \n따라서 scoreboard를 이용한 마인크래프트 명령이나 자체적인 스코어보드를 사용하는 다른 플러그인과 호환되지 않을 수 있습니다.\n\n");

        ExPlayer.load();
        Region.load();
        new PlayerState().load();
        new State().load();

    }

    @Override
    public void onDisable() {

        ExPlayer.save();
        Region.save();
        new PlayerState().save();
        new State().save();

    }

    public static void debugLog(Object obj) {
        if(debugMode)
            Bukkit.broadcastMessage("[SaehyeonLib]: "+Stringf.toSystemColor(obj+""));
    }

    public static void log(String message) {

        message = Stringf.toSystemColor(message);
        log(LogLevel.DEFAULT, message);
    }

    public static void log(LogLevel level, String message) {
        message = Stringf.toSystemColor(message);

        StringBuilder sb = new StringBuilder("");

        switch (level) {
            case DEFAULT:
                sb.append(message);
                instance.getLogger().info(sb.toString());
                break;

            case WARNING:
                sb.append(ChatColor.YELLOW).append(message);
                instance.getLogger().warning(sb.toString());
                break;

            case ERROR:
                sb.append(ChatColor.RED).append(message);
                instance.getLogger().warning(sb.toString());
                break;

            default:
                instance.getLogger().info(message);
                break;
        }
    }

    public static boolean sendError(Player player, ErrorMessage errorMessage) {
        player.sendMessage("§c"+errorMessage.toMessage());

        return false;
    }

}
