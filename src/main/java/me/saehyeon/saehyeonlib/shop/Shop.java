package me.saehyeon.saehyeonlib.shop;

import me.saehyeon.saehyeonlib.gui.GUI;
import me.saehyeon.saehyeonlib.gui.GUIRule;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.shop.event.ShopOpenEvent;
import me.saehyeon.saehyeonlib.state.PlayerState;
import me.saehyeon.saehyeonlib.util.Filef;
import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.util.Serialize;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Shop implements Serializable {

    static ArrayList<Shop> shops = new ArrayList<>();

    /**
     * 이 상점을 열리게 할 클릭 시의 NPC 이름 목록입니다. <br>
     * 만약, 이 목록에 A 라는 문자열이 등록되어 있다면 A라는 이름을 가진 플레이어 또는 엔티티를 클릭했을 때 이 상점이 열리게 됩니다.
     */
    List<String> openNPCNames = new ArrayList<>();

    /**
     * 이 상점의 이름
     */
    String name;

    /**
     * GUI가 열렸을 때 보여질 제목
     */
    String guiTitle;

    /**
     * GUI 슬롯
     */
    int guiRows = 3;

    /**
     * 이 상점이 열렸을 때 배치될 아이템들과 인덱스입니다. <br>
     * key는 인덱스, value는 해당 인덱스에 배치될 아이템입니다.
     */
    HashMap<Integer, ItemStack> guiItems = new HashMap<>();

    public Shop(String name) {
        this.name = name;
    }

    public Shop(String name, String guiTitle, int rows) {
        this.name = name;
        this.guiTitle = guiTitle;
        this.guiRows = rows;
    }

    public Shop setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Shop addGUIItem(ItemStack itemstack, int slot) {

        guiItems.put(slot,itemstack);

        return this;
    }

    public Shop setNPCNames(String... name) {
        openNPCNames = new ArrayList<>( Arrays.asList(name) );
        return this;
    }

    public Shop addNPCName(String... name) {
        openNPCNames.addAll(Arrays.asList(name));
        return this;
    }

    public Shop removeNPCName(String... name) {
        openNPCNames.removeAll(Arrays.asList(name));
        return this;
    }

    public List<String> getNPCNames() {
        return openNPCNames;
    }

    public Shop setGUITitle(String guiTitle) {
        this.guiTitle = guiTitle;
        return this;
    }

    public String getGuiTitle() {
        return guiTitle;
    }

    public void openGUI(Player player) {

        GUI.open(player,guiTitle,guiRows,GUIType.SHOP);
        GUI.setRule(player, GUIRule.CANT_ITEM_CLICK);

        guiItems.forEach((key, value) -> {
            GUI.setItem(player,key,value);
        });

        // 현재 플레이어가 보고 있는 상점을 저장
        PlayerState.set(player, "currentShop",this);

        SaehyeonLibEvent.doEvent(new ShopOpenEvent(this, player));
    }

    public void register() {
        if(!shops.contains(this))
            shops.add(this);
    }

    public HashMap<Integer, ItemStack> getGUIItems() {
        return guiItems;
    }
    public static Shop getByName(String name) {
        for(Shop shop : shops) {
            if(shop.getName().equals(name))
                return shop;
        }

        return null;
    }

    public static Shop getByNPCName(String name) {
        for(Shop shop : shops) {

            if(shop.getNPCNames().contains(name)) {

                return shop;

            }
        }

        return null;
    }
}
