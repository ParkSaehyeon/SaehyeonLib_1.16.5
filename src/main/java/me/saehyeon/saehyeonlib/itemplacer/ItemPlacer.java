package me.saehyeon.saehyeonlib.itemplacer;

import me.saehyeon.saehyeonlib.main.LogLevel;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.util.Locationf;
import me.saehyeon.saehyeonlib.util.Stringf;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.*;

public class ItemPlacer implements Serializable {

    ArrayList<ItemStack> items = new ArrayList<>();    // 상자에 숨길 아이템 배열
    ArrayList<Rule> rules = new ArrayList<>();    // 상자 숨길때 규칙
    int itemPerChest = 0;                                   // 상자 당 숨겨져야 하는 아이템의 갯수

    Region region;

    public ItemPlacer(Region region) {
        this.region = region;
    }

    /**
     * 이 지역의 상자에 아이템을 숨길 때, 한 상자 당 얼마 만큼의 아이템이 숨겨질 지를 설정합니다.<br>
     * 만약 0이라면 아무런 제약없이 랜덤으로 숨길 것 입니다.<br>
     * <b>아이템 갯수가 아닌, 아이템 목록의 인자 수 입니다.</b>
     * @return
     */
    public void setItemPerChest(int amount) {
        this.itemPerChest = amount;
    }

    /**
     * 이 지역의 상자에 아이템을 숨길 때, 한 상자 당 얼마 만큼의 아이템이 숨겨질 지를 반환합니다.<br>
     * 만약 0이라면 아무런 제약없이 랜덤으로 숨길 것 입니다.<br>
     * <b>아이템 갯수가 아닌, 아이템 목록의 인자 수 입니다.</b>
     * @return
     */
    public int getItemPerChest() {
        return itemPerChest;
    }

    /**
     * 이 지역의 상자에 숨겨질 아이템들을 설정합니다.
     */
    public void setItems(ArrayList<ItemStack> items) {
        this.items = items;
    }

    /**
     * 이 지역의 상자에 숨겨질 아이템들을
     * @return
     */
    public ArrayList<ItemStack> getItems() {
        return items;
    }

    /**
     * 이 지역의 상자에 아이템을 숨길 때 지켜야하는 규칙들을 추가합니다.
     * @param rules 추가될 규칙들
     */
    public void addRule(Rule[] rules) {
        this.rules.addAll( Arrays.asList(rules) );
    }

    /**
     * 이 지역의 상자에 아이템을 숨길 때 지켜야하는 규칙들 중 일부를 제거합니다.
     * @param rules 제거될 규칙들
     */
    public void removeRule(Rule[] rules) {
        this.rules.removeAll( Arrays.asList( rules) );
    }

    /**
     * 이 지역의 상자에 아이템을 숨길 때 지켜야하는 규칙들을 반환합니다.
     * @return
     */
    public ArrayList<Rule> getRules() {
        return rules;
    }

    /**
     * 설정된 규칙에 따라 아이템을 랜덤으로 숨깁니다.
     * @return 숨긴 아이템의 갯수 (아이템 배열의 인자 갯수가 아님)
     */
    public int spreadItem() {

        Location[] loc = region.getPosition();

        ArrayList<ItemStack> items  = (ArrayList<ItemStack>) getItems().clone();
        ArrayList<Block> blocks     = getChests();

        // 아이템이 숨겨질 수 있는 갯수가 아이템을 숨겨야 하는 갯수보다 적다면 작업 취소
        // 값이 0이면 무제한이기 때문에 무시
        int canSpreadItemAmount = blocks.size() * getItemPerChest();

        if(canSpreadItemAmount != 0 && !getRules().contains(Rule.IGNORE_LEFT_ITEM) && items.size() > canSpreadItemAmount)
            return -1;

        // 숨겨질 아이템 배열들 섞기
        Collections.shuffle(items);
        Collections.shuffle(blocks);

        SaehyeonLib.debugLog("\n\nspreadItem이 호출됨. 자세한 정보:\n -> 숨겨야 하는 아이템 수: "+items.size()+"개\n -> 아이템을 숨길 블럭 수: "+blocks.size()+"개\n -> 계산된 아이템이 숨겨질 수 있는 수 (0이라면 무제한. 공식 = 아이템을 숨길 블럭 수 * 한 블럭 당 숨겨질 수 있는 아이템 인자 수 ): "+canSpreadItemAmount);

        int curItemIndex = 0;
        int itemAmount = 0;

        // 아이템 숨기기 시작
        while(curItemIndex < items.size()) {

            for (Block block : blocks) {

                if(getItemPerChest() != 0) {

                    for (int j = 0; j < getItemPerChest(); j++) {

                        ItemStack item = items.get(curItemIndex);

                        // 아이템 숨기기
                        putItemInBlock(block, item);

                        // 숨긴 아이템 갯수 합하기
                        itemAmount += item.getAmount();

                        // 아이템을 숨긴 인자 만큼 인덱스 더 하기
                        curItemIndex++;

                        // 디버그 로그
                        SaehyeonLib.debugLog(Stringf.toLocationStr(block.getLocation()) + " 블럭에 아이템을 숨겼음. (한 블럭 당 " + canSpreadItemAmount + "번 반복) / curItemIndex: "+curItemIndex+" / 아이템 배열 인자 수: "+items.size());

                        // 더 이상 숨길 아이템이 없다면 작업 끝내기
                        if (curItemIndex >= items.size())
                            return itemAmount;

                    }

                } else {

                    /* 상자 당 숨겨져야 하는 아이템의 갯수가 0일 경우, 제한 없이 계속 반복 */
                    ItemStack item = items.get(curItemIndex);

                    // 아이템 숨기기
                    putItemInBlock(block, item);

                    // 숨긴 아이템 갯수 합하기
                    itemAmount += item.getAmount();

                    curItemIndex++;

                    SaehyeonLib.debugLog(Stringf.toLocationStr(block.getLocation())+" 블럭에 아이템을 숨겼음. (curItemIndex: "+curItemIndex+" / 아이템 배열의 인자 수: "+items.size());

                }

            }

            // 만약 상자 당 숨겨질 수 있는 최대 갯수가 0개가 아닌(무제한으로 숨겨질 수 있는  것이 아님)데도
            // 숨겨지지 못한 아이템이 있다면 그냥 작업 종료
            if(getItemPerChest() != 0 && items.size()-1 > curItemIndex) {
                SaehyeonLib.debugLog("아이템 숨김 작업 종료: 총 "+((items.size()-1)-curItemIndex)+"개의 아이템이 숨겨지지 못했음.");
                return itemAmount;
            }

        }

        return itemAmount;

    }

    public static int spreadItem(Region region) {
        return region.getItemPlacer().spreadItem();
    }

    void putItemInBlock(Block block, ItemStack item) {

        switch (block.getType()) {
            case TRAPPED_CHEST:
            case CHEST:
                Chest chest = (Chest) block.getState();
                chest.getBlockInventory().addItem(item);
                break;

            case DISPENSER:
                Dispenser dispenser = (Dispenser) block.getState();
                dispenser.getInventory().addItem(item);
                break;

            case HOPPER:

                Hopper hopper = (Hopper) block.getState();
                hopper.getInventory().addItem(item);
                break;

            default:
                SaehyeonLib.log(LogLevel.ERROR, Stringf.toLocationStr(block.getLocation()) + ": 이 지역에 아이템을 숨기지 못했습니다. (아이템을 숨길 수 없는 블럭입니다.)");
                break;
        }

    }

    public static void addRule(Region region, Rule[] rules) {
        if(region != null)
            region.getItemPlacer().addRule(rules);
    }

    public static void removeRule(Region region, Rule[] rules) {
        if(region != null)
            region.getItemPlacer().removeRule(rules);
    }

    /**
     * 상자 위에 있어도 상자가 열릴 수 있는 블럭의 키워드들을 반환합니다.
     */
    List<String> canChestOpenBlockKeyWord() {
        return Arrays.asList(
                "GLASS","STAIR","SLAB","GLASS","AIR","GRASS","FLOWER","FENCE"
        );
    }

    /**
     * 아이템을 숨길 수 있는 블럭의 키워드를 반환합니다.
     */
    List<Material> canSpreadItemBlockMaterial() {
        return Arrays.asList(
                Material.CHEST,
                Material.CHEST_MINECART,
                Material.HOPPER,
                Material.HOPPER_MINECART,
                Material.DISPENSER,
                Material.TRAPPED_CHEST
        );
    }

    protected ArrayList<Block> getChests() {

        Location[] pos = region.getPosition();

        List<Location> locations = Locationf.getAll(pos[0], pos[1]);

        // 아이템을 숨길 수 없는 블럭들 제거
        locations.removeIf(l ->
                !canSpreadItemBlockMaterial().contains(l.getBlock().getType())
        );

        // 블럭이 상자이거나 덫 상자이고 그 위에 상자를 열게 할 수 없는 블럭이 있다면 제거
        locations.removeIf(l ->
                (l.getBlock().getType() == Material.CHEST || l.getBlock().getType() == Material.TRAPPED_CHEST) && canChestOpenBlockKeyWord().contains(l.getBlock().getType().toString())
        );

        Bukkit.broadcastMessage("locations size: "+locations.size());

        // 규칙에 따른 블럭 제거
        locations.removeIf(l ->
                ( rules.contains(Rule.IGNORE_TRAP_CHEST) && l.getBlock().getType() == Material.TRAPPED_CHEST ) ||
                ( rules.contains(Rule.IGNORE_HOPPER) && l.getBlock().getType() == Material.HOPPER ) ||
                ( rules.contains(Rule.IGNORE_DISPENSER) && l.getBlock().getType() == Material.DISPENSER)
        );

        // Chest형 배열 만들기
        ArrayList<Block> blocks = new ArrayList<>();

        for(Location loc : locations)
            blocks.add(loc.getBlock());

        return blocks;
    }

}
