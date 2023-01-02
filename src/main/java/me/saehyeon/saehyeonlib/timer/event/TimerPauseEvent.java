package me.saehyeon.saehyeonlib.timer.event;

import me.saehyeon.saehyeonlib.timer.Timer;

public class TimerPauseEvent {

    Timer timer;

    public TimerPauseEvent(Timer timer) {
        this.timer = timer;
    }
}
