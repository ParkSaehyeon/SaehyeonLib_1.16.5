package me.saehyeon.saehyeonlib.cool;

import me.saehyeon.saehyeonlib.cool.event.CoolTimeEndEvent;
import me.saehyeon.saehyeonlib.cool.event.CoolTimePauseEvent;
import me.saehyeon.saehyeonlib.cool.event.CoolTimeProgressEvent;
import me.saehyeon.saehyeonlib.cool.event.CoolTimeStartEvent;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CoolTime {
    public interface Callback {
        void call();
    }

    String name;
    long time;
    long leftTime;
    BukkitTask task;
    boolean isPaused;

    static HashMap<UUID, ArrayList<CoolTime>> cooltimeMap = new HashMap<>();

    /**
     * <b>쿨타임 클래스</b><br>
     * 쿨타임은 인스턴스 생성과 함께 시작되며, 쿨타임이 종료되면 자동으로 이 쿨타임 클래스를 적용받고 있는 플레이어들로부터 제거됩니다.
     * @param name 쿨타임 이름
     * @param time 쿨타임 시간
     */
    public CoolTime(String name, long time) {
        this.name = name;           // 쿨타임 이름
        this.time = time;           // 쿨타임 시간
        this.leftTime = time;       // 쿨타임 현재 남은 시간
    }

    /**
     * 현재 이 쿨타임이 일시정지 상태인지 반환합니다.
     * @return true라면 일시정지인 상태, 아니라면 false
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * 이 쿨타임을 시작합니다.
     */
    void start(Callback callback) {
        leftTime = time;

        task = BukkitTaskf.timer(() -> {

            if(!isPaused) {

                // 남은시간 줄이기
                leftTime--;
                SaehyeonLibEvent.doEvent(new CoolTimeProgressEvent(this));

                if(leftTime+1 <= 0) {
                    stop();

                    if(callback != null)
                        callback.call();
                }
            }

        },0,20);
    }

    /**
     * 이 쿨타임을 종료합니다. <br>
     * 이 메소드는 쿨타임이 모두 종료됐을 때 호출됩니다.
     */
    void stop() {
        if(task != null)
            task.cancel();

        task = null;
    }

    /**
     * 쿨타임 진행을 일시정지하거나 일시정지를 해제합니다.합니다.
     * @param paused true라면 쿨타임이 진행되지 않습니다. false라면 쿨타임의 시간이 흘러갑니다. (기본값은 false)
     */
    public void setPause(boolean paused) {
        isPaused = paused;

        // 쿨타임 일시정지 이벤트 발생시키기
        SaehyeonLibEvent.doEvent(new CoolTimePauseEvent(this));
    }

    /**
     * 쿨타임의 이름을 설정합니다.
     * @param name 설정될 이름
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 쿨타임의 시간을 설정합니다.
     * @param time 설정될 시간
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * 쿨타임의 남은 시간을 설정합니다.
     * @param lefttime 설정될 남은시간
     */
    public void setLeftTime(long lefttime) {
        this.leftTime = lefttime;
    }

    /**
     * 쿨타임의 남은 시간을 반환합니다.
     * @return 남은시간
     */
    public long getLeftTime() {
        return leftTime;
    }

    /**
     * 쿨타임의 이름을 반환합니다.
     * @return 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 쿨타임의 시간을 반환합니다.
     * @return 시간
     */
    public long getTime() {
        return time;
    }

    /**
     * 이 쿨타임과 똑같은 속성을 가진 쿨타임 클래스를 생성합니다.
     */
    public CoolTime clone() {
        return new CoolTime(name,time);
    }

    /**
     * 이 쿨타임이 적용된 플레이어들을 반환합니다.
     */
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> result = new ArrayList<>();

        cooltimeMap.forEach((key, value) -> {

            if(value.contains(this))
                result.add(Bukkit.getPlayer(key));

        });

        return result;
    }

    /**
     * 특정 플레이어에게 쿨타임을 추가합니다.
     * @param player 추가될 플레이어
     * @param cooltime 추가될 쿨타임
     */
    public static void add(Player player, CoolTime cooltime) {
        cooltimeMap.putIfAbsent(player.getUniqueId(),new ArrayList<>());
        cooltimeMap.get(player.getUniqueId()).add(cooltime);

        // 쿨타임 시작 이벤트 발생 시키기
        SaehyeonLibEvent.doEvent(new CoolTimeStartEvent(player,cooltime));

        // 쿨타임이 끝나면 쿨타임을 제거
        cooltime.start(() -> {
            remove(player,cooltime);

            // 쿨타임 종료 이벤트 발생시키기
            SaehyeonLibEvent.doEvent(new CoolTimeEndEvent(player, cooltime));
        });
    }

    /**
     * 특정 플레이어의 쿨타임을 제거합니다.
     * @param player 제거될 플레이어
     * @param cooltime 제거될 쿨타임
     */
    public static void remove(Player player, CoolTime cooltime) {
        if(cooltimeMap.containsKey(player.getUniqueId())) {

            // 쿨타임 종료
            cooltime.stop();

            // 플레이어의 쿨타임 목록에서 제거
            cooltimeMap.get(player.getUniqueId()).remove(cooltime);

        }
    }

    /**
     * 특정 플레이어에게서 특정 이름을 가진 쿨타임을 제거합니다.
     * @param player 제거될 플레이어
     * @param name 제거될 쿨타임이 가져야하는 이름
     */
    public static void removeByName(Player player, String name) {
        CoolTime coolTime = findByName(player,name);
        remove(player,coolTime);
    }

    /**
     * 특정 플레이어가 가지고 있는 쿨타임 배열을 반환합니다.
     * @param player 쿨타임 배열을 얻을 플레이어
     * @return 플레이어에게 현재 적용된 쿨타임들
     */
    public static ArrayList<CoolTime> get(Player player) {
        return cooltimeMap.getOrDefault(player.getUniqueId(),new ArrayList<>());
    }

    /**
     * 특정 플레이어가 현재 쿨타임을 적용받고 있는지 반환합니다.
     * @param player 확인할 플레이어
     * @param cooltime 적용되었는지 확인할 쿨타임
     * @return 쿨타임이 해당 플레이어에게 적용되어 있다면 true, 아니라면 false
     */
    public static boolean contains(Player player, CoolTime cooltime) {
        return cooltimeMap.getOrDefault(player.getUniqueId(), new ArrayList<>()).contains(cooltime);
    }

    public static boolean contains(Player player, String cooltimeName) {
        return findByName(player,cooltimeName) != null;
    }

    /**
     * 특정 플레이어가 가지고 있는 특정 이름의 쿨타임을 반환합니다.
     * @param player
     * @param name 얻을 쿨타임의 이름
     * @return 해당 이름을 가지고 있는 플레이어에게 적용된 쿨타임
     */
    public static CoolTime findByName(Player player, String name) {

        for(CoolTime cool : cooltimeMap.getOrDefault(player.getUniqueId(),new ArrayList<>())) {

            if(cool.getName().equals(name))
                return cool;
        }

        return null;
    }

    /**
     * 특정 플레이어가 적용받고 있는 쿨타임들을 반환합니다.
     * @param player
     * @return
     */
    public static ArrayList<CoolTime> findByPlayer(Player player) {

        ArrayList<CoolTime> result = new ArrayList<>( cooltimeMap.getOrDefault(player.getUniqueId(), new ArrayList<>()) );
        result.removeIf(cool -> !cool.getPlayers().contains(player));

        return result;
    }
}
