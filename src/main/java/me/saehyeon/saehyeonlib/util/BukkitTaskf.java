package me.saehyeon.saehyeonlib.util;

import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class BukkitTaskf {

    public interface Callback {
        void call();
    }

    public static BukkitTask wait(Callback callback, long delay) {
        return Bukkit.getScheduler().runTaskLater(SaehyeonLib.instance, callback::call, delay);
    }

    public static BukkitTask timer(Callback callback, long delay, long priod) {
        return Bukkit.getScheduler().runTaskTimer(SaehyeonLib.instance, callback::call, delay, priod);
    }

}
