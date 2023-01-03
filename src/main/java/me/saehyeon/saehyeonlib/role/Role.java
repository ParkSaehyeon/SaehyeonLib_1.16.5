package me.saehyeon.saehyeonlib.role;

import me.saehyeon.saehyeonlib.main.LogLevel;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.role.event.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * <h2>역할 시스템</h2>
 * 컨텐츠에서 자주 쓰이는 역할 시스템입니다.<br><br>
 * <b>이벤트:</b>
 * <ul>
 *     <li><b>RolePlayerAddEvent:</b> 플레이어가 역할에 소속될 때</li>
 *     <li><b>RolePlayerRemoveEvent:</b> 플레이어가 역할에서 제거될 때</li>
 *     <li><b>RoleRegisteredEvent:</b> 역할이 등록되었을 때</li>
 *     <li><b>RoleStateChangeEvent:</b> setState() 메소드로 역할의 State가 변경되었을 때</li>
 * </ul>
 */
public class Role {

    public interface RoleCallback {
        void call();
        void call(Player player);
    }

    public static ArrayList<Role> roles = new ArrayList<>();

    // 이 역할에 소속된 플레이어들
    ArrayList<Player> players = new ArrayList<>();

    // 역할의 이름
    String name;

    // 역할의 상태(주석같은 느낌)
    HashMap<String, Object> state = new HashMap<>();

    // 이 역할에 들어가야하는 사람들의 수 (0이라면 들어가야하는 사람들의 수가 지정된 역할들을 먼저 배정한 다음, 랜덤으로 배치될 것임.)
    int needPeople = 0;

    // team
    Team team;

    public Role(String name) {
        this.name = name;
        team = SaehyeonLib.scoreboard.registerNewTeam(name);
    }

    public Role(String name,int needPeople) {
        this.name = name;
        this.needPeople = needPeople;
        team = SaehyeonLib.scoreboard.registerNewTeam(name);
    }

    /**
     * 이 역할이 배정되어야 할 인원 수를 설정합니다.<br>
     * @param amount 0 이상의 자연수만약 <b>매개변수가 0이라면 설정하지 않는 것입니다.</b>applyRandom 메소드로 사람들에게 역할을 랜덤 배정할 때, 이 값이 0이 아닌 역할이 먼저 배정됩니다.
     */
    public void setNeedPeopleAmount(int amount) {
        if(amount < 0) {
            SaehyeonLib.log(LogLevel.ERROR, name+" 역할의 인원 수를 "+amount+"로 설정할 수 없습니다. 인원 수는 0 이상의 자연수여야합니다.");
            return;
        }

        this.needPeople = amount;
    }

    /**
     * 이 역할이 배정되어야 하는 인원 수를 반환합니다.
     */
    public int getNeedPeopleAmount() {
        return needPeople;
    }

    /**
     * 특정 플레이어를 이 역할에 소속시킵니다.
     * @param player 소속될 플레이어
     */
    public void add(Player player) {

        if(players.contains(player))
            return;

        // 플레이어가 기존에 가지고 있던 역할은 없애기
        Role formerRole = Role.findByPlayer(player);

        if(formerRole != null)
            formerRole.remove(player);

        players.add(player);

        // 팀에 플레이어 추가하기
        player.setScoreboard(SaehyeonLib.scoreboard);
        team.addEntry(player.getName());

        SaehyeonLibEvent.doEvent(new RolePlayerAddEvent(player,this));
        SaehyeonLibEvent.doEvent(new RoleChangeEvent(this));
    }

    /**
     * 특정 플레이어를 이 역할에서 제거합니다.
     * @param player 제거될 플레이어
     */
    public void remove(Player player) {
        players.remove(player);
        SaehyeonLibEvent.doEvent(new RolePlayerRemoveEvent(player,this));
        SaehyeonLibEvent.doEvent(new RoleChangeEvent(this));
    }

    /**
     * 이 역할에 소속된 모든 플레이어들을 반환합니다.
     * @return 이 역할에 소속된 플레이어들
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * 이 역할에 대한 키,값 입니다. MAP과 비슷합니다.<br>
     * 이미 key를 사용하고 있는 항목이 있다면, 해당 항목을 덮어쓰게 됩니다.
     * @param key
     * @param value
     */
    public void setState(String key, Object value) {
        if(value == null)
            state.remove(key);
        else
            state.put(key, value);

        SaehyeonLibEvent.doEvent(new RoleStateChangeEvent(key,value,this));
        SaehyeonLibEvent.doEvent(new RoleChangeEvent(this));
    }

    /**
     * 이 역할에 특정 key에 대한 값을 반환합니다.
     * @param key
     * @return key로 등록된 Object형 변수
     */
    public Object getState(String key) {
        return state.getOrDefault(key, null);
    }

    /**
     * 이 역할이 부여된 플레이어들에게 무언가를 합니다.
     * @param callback (player) -> { player.sendMessage("ㅎㅇ"); } 이런식으로 이 역할이 부여된 플레이어들에게 인사할 수 있습니다!
     */
    public void doPlayers(RoleCallback callback) {
        players.forEach(callback::call);
    }

    /**
     * 이 역할을 등록합니다.<br>
     * 이 메소드는 <b>역할 생성 직후 호출</b>되어야 합니다.<br>
     * 역할이 등록되면, 시스템은 이 역할은 공식적으로 사용되는 역할로 판단하고 역할 랜덤배정, 역할 검색 메소드 등에서 이 역할을 활용할 수 있게됩니다.
     */
    public void register() {
        Role role = findByName(this.name);

        // 만약 같은 이름을 가진 역할이 이미 있다면 등록 금지
        if(role != null) {
            SaehyeonLib.log(LogLevel.ERROR, name+"(이)라는 이름으로 역할을 등록하지 못했습니다. (이미 있는 같은 이름을 사용중인 역할이 있습니다.)");
        }

        roles.add(this);
        SaehyeonLibEvent.doEvent(new RoleRegisteredEvent(this));
    }

    /**
     * 이 역할의 이름을 설정합니다.
     * @param name 설정될 이름
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 이 역할의 이름을 반환합니다.
     * @return 이 역할의 이름
     */
    public String getName() {
        return name;
    }

    public void setPrefix(String prefix) {
        team.setPrefix(prefix);
    }

    public String getPrefix() {
        return team.getPrefix();
    }

    public void setSuffix(String suffix) {
        team.setSuffix(suffix);
    }

    public String getSuffix() {
        return team.getSuffix();
    }

    /**
     * 특정 이름을 가진 역할을 반환합니다.<br>
     * 등록되어 있지 않은 역할은 찾을 수 없습니다.
     * @param roleName
     * @return
     */
    public static Role findByName(String roleName) {
        for(Role role : roles) {
            if(role.getName().equals(roleName))
                return role;
        }

        return null;
    }

    /**
     * 특정 역할을 가지고 있는 플레이어들에게 해당 플레이어들이 매개변수로 있는 람다식을 실행합니다.
     * @param role 특정 역할
     * @param callback Player형 매개변수가 있는 람다식
     */
    public static void doPlayers(Role role, RoleCallback callback) {
        role.getPlayers().forEach(callback::call);
    }

    /**
     * 지정된 플레이어들에게 랜덤으로 역할을 배정합니다. <br>
     * 이때, 배정 인원이 정해진(Role 클래스의 needPeople이 0이 아닌 역할)에 먼저 사람이 배정되고<br>
     * 그 이후, 나머지 역할에 사람이 배정됩니다.<br><br>
     * <i>만약 지정된 플레이어들의 수가 배정되어야 하는 사람의 수보다 적다면, 오류 메세지가 뜰 것 입니다.</i>
     * @param players 역할이 배정될 플레이어들
     * @param callback 역할 배정 완료 시 호출될 callback 함수
     */
    public static void applyRandom(List<Player> players, RoleCallback callback) {

        // 먼저 플레이어들의 역할 없애기
        players.forEach(Role::removeIfHas);

        // 배정 인원이 정해진 역할들과 그렇지 않은 역할들을 나누기
        ArrayList<Role> needPeopleRoles     = (ArrayList<Role>) roles.clone();
        ArrayList<Role> notNeedPeopleRoles  = (ArrayList<Role>) roles.clone();

        needPeopleRoles.removeIf(role -> role.needPeople == 0);
        notNeedPeopleRoles.removeIf(role -> role.needPeople != 0);

        // 역할을 배정받아야 하는 인원 수가 충분한지 확인
        int needPeople = needPeopleRoles.size();

        if(players.size() < needPeople) {
            Bukkit.broadcastMessage("§c역할 랜덤 배정이 불가합니다. ("+needPeople+"명이 필요§c하지만 현재 역할 배정 대상은 "+players.size()+"명입니다.)");
            return;
        }

        // 배정 인원을 요구하는 역할들 먼저 배정하기
        Collections.shuffle(players);
        Collections.shuffle(needPeopleRoles);
        Collections.shuffle(notNeedPeopleRoles);

        // 현재까지 배정된 플레이어 목록의 인덱스
        int curPlayersIndex = 0;

        SaehyeonLib.debugLog("인원 수가 정해져있는 역할 먼저 분배: ");

        for(Role role : needPeopleRoles) {

            SaehyeonLib.debugLog(" -> "+role.getName()+"에 역할 분배중 (필요 인원 수: "+role.needPeople+", 현재 인원 수: "+role.players.size()+")");

            while(role.needPeople != role.players.size()) {

                Player targetPlayer = players.get(curPlayersIndex++);
                role.add(targetPlayer);
                SaehyeonLib.debugLog(" --> "+targetPlayer.getName()+"(이)가 이 역할에 소속됐음.");
                SaehyeonLib.debugLog(" ---> 이 역할의 필요 인원 수: "+role.needPeople+", 현재 인원 수: "+role.players.size());
            }

        }

        SaehyeonLib.debugLog("\n인원 수가 정해진 역할 분배 끝, 인원 수 제한이 없는 역할 분배 시작: ");
        SaehyeonLib.debugLog(" -> 모든 사람이 역할을 분배받아서 작업을 종료해야 하는가?(역할을 분배받은 마지막 플레이어 번째: "+curPlayersIndex+" / 플레이어 수: "+players.size()+")");

        // 배정 인원이 정해지지 않은 남은 역할들 배정
        // 만약 이미 모든 사람이 위 작업에서 역할을 배정받았다면 작업 종료
        // 여기서 curPlayerIndex에 -1을 하는 이유는 위 반복문에서 마지막으로 역할을 부여받은 번째 수에 +1을 하기 때문(실제로는 +1되는  시점의 플레이어가 마지막으로 부여받는데)
        // 즉, 반복문의 마지막에서 +1되는 것을 취소하는 것
        if(curPlayersIndex-1 < players.size()-1) {

            SaehyeonLib.debugLog(" -> 작업 시작: ");

            // 역할들 섞기
            Collections.shuffle(notNeedPeopleRoles);
            Collections.shuffle(notNeedPeopleRoles);

            // 모두가 역할을 배정받을 때 까지 남은 역할 배정하기
            int roleIndex = 0;

            for(int i = curPlayersIndex; i < players.size(); i++) {

                Player p = players.get(i);

                Role role = notNeedPeopleRoles.get(roleIndex++);

                role.add(p);

                SaehyeonLib.debugLog(" --> "+role.getName()+" 역할에 "+p.getName()+"(을)를 소속시킴.");

                if(roleIndex > notNeedPeopleRoles.size()-1)
                    roleIndex = 0;

            }
        }

        SaehyeonLib.debugLog("끝");

        // 역할 배정 완료
        if(callback != null)
            callback.call();
    }

    /**
     * 서버에 있는 모든 플레이어들의 역할을 랜덤으로 배정합니다.
     * 예외 플레이어로 등록된 플레이어에게는 역할이 배정되지 않습니다.
     */
    public static void applyRandomAll() {

        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        players.removeIf(ExPlayer::contains);

        applyRandom(players, null);
    }

    /**
     * 특정 플레이어가 적용받고 있는 역할들을 반환합니다.
     * @param player
     */
    public static Role findByPlayer(Player player) {

        ArrayList<Role> result = new ArrayList<>( roles );
        result.removeIf(role -> !role.getPlayers().contains(player));

        return result.isEmpty() ? null : result.get(0);

    }

    /**
     * 특정 플레이어에게 부여된 역할이 있다면, 해당 역할에서 해당 플레이어를 제거합니다.
     * @param player
     */
    public static void removeIfHas(Player player) {
        Role role = Role.findByPlayer(player);

        if(role != null)
            role.remove(player);
    }

}
