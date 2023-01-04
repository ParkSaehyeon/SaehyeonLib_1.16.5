package me.saehyeon.saehyeonlib.timer.event;

import me.saehyeon.saehyeonlib.timer.Timer;

public class TimerStopEvent {

    Timer timer;

    public TimerStopEvent(Timer timer) {
        this.timer = timer;
    }

    public Timer getTimer() { return timer; }
}
