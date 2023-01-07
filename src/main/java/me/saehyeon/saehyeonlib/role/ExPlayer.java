package me.saehyeon.saehyeonlib.role;

import me.saehyeon.saehyeonlib.main.LogLevel;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.role.event.ExPlayerAddEvent;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * <h2>예외 플레이어</h2>
 * 역할이 배정될 때, 여기에 등록된 플레이어들을 무시될 것입니다.<br>
 * 따라서, 컨텐츠 운영자 목록으로 사용할 수 있습니다.<br><br>
 * 예외 플레이어 목록이 변경되면 <u>%appdata%\Local\SaehyeonLib\ExPlayer.txt에 저장됩니다.</u><br>
 * 즉, 이 예외 플레이어 목록은 한 서버가 아니라 모든 서버에 공통적으로 적용되는 목록입니다. <br>예를들어, 컨텐츠 운영진 목록 거의 변하지 않고 항상 컨텐츠에서 무시되어야 합니다. 이럴때 이 예외 플레이어가 사용될 수 있습니다.<br><br>
 * <b>이벤트:</b>
 * <ul>
 *     <li><b>ExPlayerAddEvent:</b> 예외 플레이어가 목록에 플레이어가 추가될 때</li>
 *     <li><b>ExPlayerRemoveEvent:</b> 예외 플레이어가 목록에 플레이어가 제거될 때</li>
 * </ul>
 */
public class ExPlayer {
    private static ArrayList<UUID> exPlayerUUIDs = new ArrayList<>();

    /**
     * 예외 플레이어 목록 저장 파일 경로입니다.
     */
    static final String FOLDER_PATH = "C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Local\\SaehyeonLib";
    static final String PATH = FOLDER_PATH+"\\ExPlayer.txt";

    /**
     * 특정 UUID를 가진 플레이어를 예외 플레이어 목록에 추가합니다.
     * @param uuid 추가할 UUID
     */
    public static void add(UUID uuid) {
        if(!exPlayerUUIDs.contains(uuid)) {
            exPlayerUUIDs.add(uuid);
            save();
            SaehyeonLibEvent.doEvent(new ExPlayerAddEvent(uuid));
        }
    }

    /**
     * 특정 닉네임을 가진 플레이어를 예외 플레이어 목록에 추가합니다.<br>
     * 만약 해당 플레이어가 서버 없다면, 모장 API로부터 해당 이름을 가진 마인크래프트 계정의 UUID를 얻어 목록에 추가합니다.
     * @param playerName 추가될 플레이어 닉네임
     */
    public static void add(String playerName) {
        UUID uuid = Playerf.getUUID(playerName);

        if(uuid == null) {
            SaehyeonLib.log(LogLevel.ERROR, playerName+"(을)를 예외 플레이어 등록에 실패했습니다. 해당 이름을 가진 마인크래프트 계정을 찾을 수 없거나 네트워크 오류일 수 있습니다.");
            return;
        }

        add(uuid);
    }

    /**
     * 특정 플레이어를 예외 플레이어 목록에 추가합니다.
     * @param player 추가될 플레이어
     */
    public static void add(Player player) {
        add(player.getUniqueId());
    }

    /**
     * 특정 UUID를 가진 플레이어를 예외 플레이어에서 제외합니다.
     * @param uuid
     */
    public static void remove(UUID uuid) {
        exPlayerUUIDs.remove(uuid);
        save();
        SaehyeonLibEvent.doEvent(new ExPlayerAddEvent(uuid));
    }

    /**
     * 특정 닉네임을 가진 플레이어를 예외 플레이어에서 제외합니다. <br>
     * 만약 해당 플레이어가 서버 없다면, 모장 API로부터 해당 이름을 가진 마인크래프트 계정의 UUID를 얻어 목록에 추가합니다.
     * @param playerName 제외할 플레이어의 이름
     */
    public static void remove(String playerName) {
        UUID uuid = Playerf.getUUID(playerName);

        if(uuid == null) {
            SaehyeonLib.log(LogLevel.ERROR, playerName+"(을)를 예외 플레이어 등록에 실패했습니다. 해당 이름을 가진 마인크래프트 계정을 찾을 수 없거나 네트워크 오류일 수 있습니다.");
            return;
        }

        remove(uuid);
    }

    /**
     * 특정 플레이어를 예외 플레이어에서 제외합니다.
     * @param player 제외할 플레이어
     */
    public static void remove(Player player) {
        remove(player.getUniqueId());
    }

    /**
     * 특정 플레이어가 예외 플레이어로 등록되어 있는지 여부를 반환합니다.
     * @param player 확인할 플레이어
     * @return true라면 예외 플레이어, 아니라면 false
     */
    public static boolean contains(Player player) {
        return exPlayerUUIDs.contains(player.getUniqueId());
    }

    public static void announce(String message) {

        getPlayers().forEach(p -> {

            for(int i = 0; i < 10; i++) {

                final int ii = i;

                BukkitTaskf.wait(() -> {

                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER,1,1);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER,1,1.5f);

                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER,1,1);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER,1,1.5f);

                    p.sendTitle( (ii % 2 == 0 ? "§b§l" : "§f§l")+"운영진 알림","",0,30,0);

                },4*ii);

            }

            p.sendMessage("");
            p.sendMessage("§b§l운영진 알림 〉 §f"+message);
            p.sendMessage("");

        });
    }

    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        exPlayerUUIDs.forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);

            if(p != null)
                players.add(p);
        });

        return players;
    }

    /**
     * 특정 이름을 가진 플레이어가 예외 플레이어로 등록되어 있는지 여부를 반환합니다.
     * @param playerName 확인 플레이어의 닉네임
     * @return true라면 플레이어가 예외 플레이어인 것, 아니라면 false
     */
    public static boolean contains(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        UUID uuid = null;

        if(player != null)
            uuid = player.getUniqueId();
        else
            uuid = Playerf.getUUID(playerName);

        return exPlayerUUIDs.contains( uuid );
    }

    private static void createFile() {

        try {

            File dir = new File(FOLDER_PATH);
            dir.mkdir();

            File file = new File(PATH);

            if(file.createNewFile())
                SaehyeonLib.log(LogLevel.DEFAULT,"\n\n예외 플레이어가 처음으로 설정되었습니다. 예외 플레이어 설정은 모든 서버에서 공유되는 목록입니다.\n"+PATH+" 에 플레이어들의 고유 아이디가 저장됩니다.\n\n");


        } catch (Exception e) {

            SaehyeonLib.log(LogLevel.ERROR, "\n\n예외 플레이어 목록 저장 파일 생성 도중 오류가 발생했습니다. 이 경우, 예외 플레이어 기능이 제대로 작동하지 않을 수 있습니다.\n\n");

        }
    }

    /**
     * 예외 플레이어 목록을 저장합니다.<br>
     * 일반적으로, 예외 플레이어 목록이 변경될때 호출됩니다.
     */

    public static void save() {

        createFile();

        File file = new File(PATH);

        try {

            FileWriter writer = new FileWriter(file);

            for(UUID u : exPlayerUUIDs)
                writer.write(u+"\n");

            writer.close();

        } catch (Exception e) {

            SaehyeonLib.log(LogLevel.ERROR, "예외 플레이어 목록을 저장하던 도중 오류가 발생했습니다. ("+e+")");
        }

    }

    /**
     * 예외 플레이어 목록을 로드합니다.<br>
     * 일반적으로, 서버가 켜질때 호출됩니다.
     */
    public static void load() {

        createFile();

        File file = new File(PATH);

        try {

            FileReader reader = new FileReader(file);
            BufferedReader buf = new BufferedReader(reader);

            String line = "";

            while ((line = buf.readLine()) != null) {

                if(!line.equals(""))
                    exPlayerUUIDs.add(UUID.fromString(line));

            }

            buf.close();
            reader.close();

        } catch (Exception e) {

            SaehyeonLib.log(LogLevel.ERROR, "예외 플레이어 목록을 읽던 도중 오류가 발생했습니다. ("+e+")");

        }

    }
}
