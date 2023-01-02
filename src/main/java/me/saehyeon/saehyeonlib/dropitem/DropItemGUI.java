package me.saehyeon.saehyeonlib.dropitem;

import me.saehyeon.saehyeonlib.gui.GUI;
import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.state.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DropItemGUI {
    public static void open(Player player, DropItem dropItem) {

        GUI.open(player, dropItem.getRegion().getName()+" 지역의 드롭 아이템 설정", 6,DropItemGUIType.ITEM_SETTING);

        ArrayList<ItemStack> items = dropItem.getItems();

        for(int i = 0; i < items.size(); i++)
            GUI.setItem(player, i, items.get(i));

        PlayerState.set(player, "selectedRegion", dropItem.getRegion());

    }

    public static void saveItemSetting(Player player) {
        DropItem dropItem = ((Region)PlayerState.get(player, "selectedRegion")).getDropItem();

        ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList( player.getOpenInventory().getTopInventory().getContents() ) );
        items.removeIf(Objects::isNull);

        dropItem.setItems(items);

        player.sendMessage("§7"+ dropItem.getRegion().getName()+"§f 지역 드롭 아이템 목록 변경을 저장했습니다.");
    }
}
