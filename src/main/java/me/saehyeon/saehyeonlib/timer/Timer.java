package me.saehyeon.saehyeonlib.timer;

import me.saehyeon.saehyeonlib.main.LogLevel;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.timer.event.TimerPauseEvent;
import me.saehyeon.saehyeonlib.timer.event.TimerProgressEvent;
import me.saehyeon.saehyeonlib.timer.event.TimerStartEvent;
import me.saehyeon.saehyeonlib.timer.event.TimerStopEvent;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Timer {

    public interface Callback {
        void call();
        void callInProgress(int leftTime);
    }
    static ArrayList<Timer> timerInfos = new ArrayList<>();

    String name;
    BossBar bossBar;
    BukkitTask bukkitTask;
    int seconds;
    int leftTime;

    boolean pause = false;

    public Timer(String name, BossBar bossBar, int seconds) {
        this.name = name;
        this.bossBar = bossBar;
        this.seconds = seconds;
    }

    public void start() {

        if(bossBar != null)
            bossBar.setProgress(1);

        leftTime = seconds;

        bukkitTask = BukkitTaskf.timer(() -> {

            if(!pause) {

                if(bossBar != null) {

                    double decrement = 1 / (double) seconds;
                    double totalProgress = bossBar.getProgress() - decrement;

                    // 보스바 타이틀 제목 변경
                    int[] hms = getHourMinSecFromSeconds(leftTime);

                    // 약속된 문자열을 시,분,초로 치환
                    String bossBarTitle = bossBar.getTitle().replace("{hour}", hms[0]+"")
                            .replace("{minute}",hms[1]+"")
                            .replace("{second}",hms[2]+"")
                            .replace("{h}",hms[0]+"")
                            .replace("{m}",hms[1]+"")
                            .replace("{s}",hms[2]+"");

                    bossBar.setTitle(bossBarTitle);

                    // 보스바 감소
                    if (totalProgress >= 0)
                        bossBar.setProgress(bossBar.getProgress() - decrement);

                }

                leftTime--;

                // 이벤트 발생시키기
                SaehyeonLibEvent.doEvent(new TimerProgressEvent(this));

                if (leftTime - 1 <= 0)
                    stop();
            }

        },0,20);

        // 이벤트 발생시키기
        SaehyeonLibEvent.doEvent(new TimerStartEvent(this));

    }

    public void stop() {
        if(bukkitTask != null)
            bukkitTask.cancel();

        // 이벤트 발생시키기
        SaehyeonLibEvent.doEvent(new TimerStopEvent(this));

    }

    public void setPause(boolean pause) {
        this.pause = pause;

        // 이벤트 발생시키기
        SaehyeonLibEvent.doEvent(new TimerPauseEvent(this));

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getLeftTime() {
        return leftTime;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isPaused() {
        return pause;
    }

    public static void StartTimer(String timerName, BossBar bossBar, int seconds) {

        Timer _timer = findByName(timerName);

        if(_timer != null) {
            SaehyeonLib.log(LogLevel.WARNING, timerName+"(이)라는 타이머를 설정하지 않았습니다. (이미 같은 이름의 타이머가 등록되어 있습니다.)");
            return;
        }

        Timer timer = new Timer(timerName, bossBar,seconds);

        timerInfos.add(timer);
        timer.start();

    }

    public static void StopTimer(String timerName) {
        findByName(timerName).stop();
    }

    public static boolean contains(Timer timer) {
        return timerInfos.contains(timer);
    }

    public static Timer findByName(String timerName) {
        for(Timer timer : Timer.timerInfos) {
            if(timer.getName().equals(timerName))
                return timer;
        }

        return null;
    }

    /**
     * 초를 시간, 분, 초로 변경합니다.
     * @param seconds 변경할 초
     * @return 인덱스 0번은 시간, 1번은 분, 2번은 초를 의미합니다.
     */
    public static int[] getHourMinSecFromSeconds(int seconds) {

        int curSec = seconds;

        int hour = seconds / 3600;

        curSec -= seconds/ 3600;

        int min = seconds / 60;

        curSec -= min;

        return new int[] { hour, min, curSec };
    }

    public static void countDown(int second, Callback whenProgress, Callback whenEnd) {
        for(int i = second; i > 0; i--) {


            whenProgress.callInProgress(i);
        }
    }
}
