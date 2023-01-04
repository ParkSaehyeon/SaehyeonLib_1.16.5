package me.saehyeon.saehyeonlib.event;

import me.saehyeon.saehyeonlib.timer.Timer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {
    @EventHandler
    void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        // 들어오면 현재 진행중인 타이머의 보스바를 띄워주기
        Timer.getTimers().forEach(timer -> {

            timer.getBossBar().addPlayer(p);

        });

    }
}
