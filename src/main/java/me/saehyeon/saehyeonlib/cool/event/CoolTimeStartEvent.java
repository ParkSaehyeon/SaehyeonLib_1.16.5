package me.saehyeon.saehyeonlib.cool.event;

import me.saehyeon.saehyeonlib.cool.CoolTime;
import org.bukkit.entity.Player;

public class CoolTimeStartEvent {

    Player player;
    CoolTime cooltime;

    public CoolTimeStartEvent(Player player, CoolTime cooltime) {
        this.player = player;
        this.cooltime = cooltime;
    }

    public Player getPlayer() {
        return player;
    }

    public CoolTime getCoolTime() {
        return cooltime;
    }
}
