package me.saehyeon.saehyeonlib.cool.event;

import me.saehyeon.saehyeonlib.cool.CoolTime;
import org.bukkit.entity.Player;

public class CoolTimeProgressEvent {
    CoolTime cooltime;

    public CoolTimeProgressEvent(CoolTime cooltime) {
        this.cooltime = cooltime;
    }

    public CoolTime getCoolTime() {
        return cooltime;
    }

    public long getLeftTime() {
        return cooltime.getLeftTime();
    }
}
