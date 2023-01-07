package me.saehyeon.saehyeonlib.region;

import me.saehyeon.saehyeonlib.dropitem.DropItem;
import me.saehyeon.saehyeonlib.itemplacer.ItemPlacer;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.util.Filef;
import me.saehyeon.saehyeonlib.util.Locationf;
import me.saehyeon.saehyeonlib.util.Serialize;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.*;

/**
 * <h2>지역</h2>
 * 이 라이브러리에서 범위 지정을 요구하는 기능들의 범위 기능을 담당합니다.<br>
 * 아래의 기능에 영향을 미칩니다:
 * <ul>
 *     <li>ChestItem(상자에 아이템 숨기는 기능)</li>
 *     <li>DropItem(아이템을 떨구는 기능)</li>
 * </ul>
 */
public class Region implements Serializable {

    static ArrayList<Region> regions;

    String name;
    protected Location pos1;
    protected Location pos2;
    ItemPlacer itemPlacerInfo;
    DropItem dropItem;
    HashMap<String, Object> state = new HashMap<>();

    public Region(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.itemPlacerInfo = new ItemPlacer(this);
        this.dropItem = new DropItem(this);
    }

    /**
     * 지역의 두 지점을 설정합니다.
     * @param pos1
     * @param pos2
     */
    public void setPosition(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    /**
     * 지역의 두 지점을 반환합니다.
     * @return
     */
    public Location[] getPosition() {
        return new Location[] { pos1, pos2 };
    }

    /**
     * 지역의 이름을 설정합니다.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 지역의 이름을 반환합니다.
     * @return
     */
    public String getName() {
        return name;
    }

    public void setState(String key, Object value) {
        state.put(key,value);
    }

    public Object getState(String key) {
        return state.getOrDefault(key,null);
    }

    /**
     * 지역을 등록합니다. 지역 생성 수 <b>필수적으로</b> 호출되어야 합니다.
     * 일반적으로, 명령어를 통해 지역을 생성한다면 이 메소드가 자동으로 호출됩니다.
     */
    public void register() {
        if(!regions.contains(this))
            regions.add(this);
    }

    /**
     * 이 지역을 제거합니다. 이 메소드를 호출하게 되면, 더 이상 이 지역은 등록된 지역이 아니게 됩니다.
     */
    public void remove() {
        regions.remove(this);
        dropItem.StopDrop();
    }

    public ItemPlacer getItemPlacer() {
        return this.itemPlacerInfo;
    }

    public DropItem getDropItem() {
        return dropItem;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> result = new ArrayList<>( Bukkit.getOnlinePlayers() );
        result.removeIf(p -> !Locationf.isWithin(p.getLocation(),pos1,pos2));

        return result;
    }

    /**
     * 특정 이름을 가진 지역을 찾고 반환합니다.
     * @param regionName 이 이름을 가지고 있는 지역이 반환됩니다
     * @return
     */
    public static Region findByName(String regionName) {
        for(Region r : regions)
            if(r.getName().equals(regionName))
                return r;

        return null;
    }

    public static ArrayList<Region> getRegions() {
        return (ArrayList<Region>) regions.clone();
    }

    /**
     * 특정 이름을 가지고 있는 지역이 등록되어 있는지 반환합니다.
     * @param regionName
     * @return
     */
    public static boolean containsByName(String regionName) {
        return findByName(regionName) != null;
    }

    public static void save() {

        String str = Serialize.serialize(regions);

        Filef.write(SaehyeonLib.instance.getDataFolder()+"\\region.json",str);

    }

    public static void load() {

        List<String> lines = Filef.read(SaehyeonLib.instance.getDataFolder()+"\\region.json");

        if(lines != null && !lines.isEmpty()) {

            String str = lines.get(0);

            ArrayList<Region> _regions = (ArrayList<Region>) Serialize.deSerialize(str);

            regions = _regions == null ? new ArrayList<>() : _regions;

        } else {

            regions = new ArrayList<>();

        }

    }

}
