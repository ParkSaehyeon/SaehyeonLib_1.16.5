package me.saehyeon.saehyeonlib.timer;

import me.saehyeon.saehyeonlib.main.LogLevel;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.timer.event.TimerPauseEvent;
import me.saehyeon.saehyeonlib.timer.event.TimerProgressEvent;
import me.saehyeon.saehyeonlib.timer.event.TimerStartEvent;
import me.saehyeon.saehyeonlib.timer.event.TimerStopEvent;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Timer {

    @FunctionalInterface
    public interface Callback {
        void call();
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

    public Timer start() {

        String bossBarTitle = "";

        if(bossBar != null) {
            bossBar.setProgress(1);
            bossBar.setVisible(true);

            // 보스바를 모두에게 띄우기
            Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);

            // 보스바 초기 타이틀을 저장하기
            bossBarTitle = bossBar.getTitle();

        }

        final String finalBossBarTitle = bossBarTitle;

        leftTime = seconds;

        bukkitTask = BukkitTaskf.timer(() -> {

            if(!pause) {

                if(bossBar != null) {

                    double decrement = 1 / (double) seconds;
                    double totalProgress = bossBar.getProgress() - decrement;

                    // 보스바 타이틀 제목 변경
                    int[] hms = getHourMinSecFromSeconds(leftTime);


                    // 약속된 문자열을 시,분,초로 치환
                    String _title = finalBossBarTitle.replace("{hour}", hms[0]+"")
                            .replace("{minute}",hms[1]+"")
                            .replace("{second}",hms[2]+"")
                            .replace("{h}",hms[0]+"")
                            .replace("{m}",hms[1]+"")
                            .replace("{s}",hms[2]+"");

                    bossBar.setTitle(_title);

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

        return this;
    }

    public Timer clear() {
        bukkitTask.cancel();
        bukkitTask = null;
        pause = false;
        leftTime = seconds;

        if(bossBar != null) {
            bossBar.setProgress(1);
            bossBar.removeAll();
        }

        return this;
    }

    public Timer stop() {
        if(bukkitTask != null)
            bukkitTask.cancel();

        // 이벤트 발생시키기
        SaehyeonLibEvent.doEvent(new TimerStopEvent(this));

        return this;
    }

    public Timer setPause(boolean pause) {
        this.pause = pause;

        // 이벤트 발생시키기
        SaehyeonLibEvent.doEvent(new TimerPauseEvent(this));

        return this;
    }

    public Timer setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return name;
    }

    public Timer setLeftTime(int leftTime) {
        this.leftTime = leftTime;

        return this;
    }

    public Timer setSeconds(int seconds) {
        this.seconds = seconds;

        return this;
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

    public BossBar getBossBar() {
        return bossBar;
    }

    public static Timer StartTimer(String timerName, BossBar bossBar, int seconds) {

        Timer _timer = findByName(timerName);

        if(_timer != null) {
            SaehyeonLib.log(LogLevel.WARNING, timerName+"(이)라는 타이머를 설정하지 않았습니다. (이미 같은 이름의 타이머가 등록되어 있습니다.)");
            return null;
        }

        Timer timer = new Timer(timerName, bossBar,seconds);

        timerInfos.add(timer);
        timer.start();

        return timer;
    }

    public static Timer StopTimer(String timerName) {
        Timer timer = findByName(timerName);

        if(timer != null)
            return timer.stop();

        SaehyeonLib.log(LogLevel.ERROR,timerName+"(이)라는 타이머를 찾을 수 없으므로 타이머 종료 작업을 하지 못했습니다.");
        return null;
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

        int hour = curSec / 3600;

        curSec -= hour*3600;

        int min = curSec / 60;

        curSec -= min*60;

        return new int[] { hour, min, curSec };
    }

    public static void countDown(int second, Callback whenEnd) {

        Playerf.sendTitleAll("","§l"+second,0,20,0);
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER,1,1f));

        for(int i = second; i > 0; i--) {

            final int finalI = i;

            Bukkit.getScheduler().runTaskLater(SaehyeonLib.instance, () -> {

                Playerf.sendTitleAll("","§l"+(second-finalI),0,20,0);
                Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER,1,1f));
            }, 20L *i);

        }

        Bukkit.getScheduler().runTaskLater(SaehyeonLib.instance, () -> {

            whenEnd.call();
            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER,1,2f));

        },20*second+1);
    }

    public static ArrayList<Timer> getTimers() {
        return new ArrayList<>(timerInfos);
    }
}
