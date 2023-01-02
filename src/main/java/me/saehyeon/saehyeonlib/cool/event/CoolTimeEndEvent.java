package me.saehyeon.saehyeonlib.cool.event;

import me.saehyeon.saehyeonlib.cool.CoolTime;
import org.bukkit.entity.Player;

public class CoolTimeEndEvent {

    Player player;
    CoolTime finishedCoolTime;

    public CoolTimeEndEvent(Player player, CoolTime finishedCoolTime) {
        this.player = player;
        this.finishedCoolTime = finishedCoolTime;
    }

    public Player getPlayer() {
        return player;
    }

    public CoolTime getCoolTime() {
        return finishedCoolTime;
    }
}
