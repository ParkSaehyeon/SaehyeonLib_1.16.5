package me.saehyeon.saehyeonlib.cool.event;

import me.saehyeon.saehyeonlib.cool.CoolTime;
import org.bukkit.entity.Player;

public class CoolTimePauseEvent {

    CoolTime cooltime;

    public CoolTimePauseEvent(CoolTime cooltime) {
        this.cooltime = cooltime;
    }

    public CoolTime getCoolTime() {
        return cooltime;
    }
}
