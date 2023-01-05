package me.saehyeon.saehyeonlib.event;

import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.timer.Timer;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {
    @EventHandler
    void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        BukkitTaskf.wait(() -> {

            // 들어오면 현재 진행중인 타이머의 보스바를 띄워주기
            Timer.getTimers().forEach(timer -> {

                timer.getBossBar().addPlayer(p);

            });


            // 들어오면 스코어보드에 다시 가입시키기
            p.setScoreboard(SaehyeonLib.scoreboard);

        },2);

    }
}
