package me.saehyeon.saehyeonlib.timer.event;

import me.saehyeon.saehyeonlib.timer.Timer;

public class TimerStartEvent {

    Timer timer;

    public TimerStartEvent(Timer timer) {
        this.timer = timer;
    }

    public Timer getTimer() { return timer; }
}
