package me.saehyeon.saehyeonlib.timer.event;

import me.saehyeon.saehyeonlib.timer.Timer;

public class TimerProgressEvent {

    Timer timer;

    public TimerProgressEvent(Timer timer) {
        this.timer = timer;
    }
}
