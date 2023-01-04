package me.saehyeon.saehyeonlib.itemplacer;

import me.saehyeon.saehyeonlib.gui.GUI;
import me.saehyeon.saehyeonlib.main.ErrorMessage;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.util.Stringf;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemPlacerGUI {

    public static boolean openGUI(Player player, Region region, GUIType guiType) {

        if(region == null)
            return SaehyeonLib.sendError(player, ErrorMessage.REGION_NOT_EXIST);

        ItemPlacer itemPlacer = region.getItemPlacer();

        switch (guiType) {
            case ITEM_SETTING:

                GUI.open(player,region.getName()+" 지역의 상자에 숨길 아이템들",6);

                // 아이템 불러오기
                BukkitTaskf.wait(() -> {

                    ArrayList<ItemStack> items = itemPlacer.getItems();

                    for(int i = 0; i < items.size(); i++)
                        GUI.setItem(player, i, items.get(i));

                },1);

                break;

            case REMOTE:

                GUI.open(player, region.getName()+" 지역의 아이템을 적재할 수 있는 블럭들",6, guiType);

                // 상자들 불러오기
                ArrayList<Block> blocks = region.getItemPlacer().getChests();

                Bukkit.broadcastMessage("chests size: "+blocks.size());

                for(int i = 0; i < blocks.size(); i++) {

                    Block block = blocks.get(i);

                    ItemStack item = Itemf.createItemStack(block.getType(), 1, "§l"+block.getType(), Arrays.asList(
                            "§7"+ Stringf.toLocationStr(blocks.get(i).getLocation())+"§f에 위치한 블럭입니다.", "§7클릭§f하여 내용물을 수정할 수 있습니다."
                    ));

                    GUI.setItem(player, i, item);
                }

                break;
        }

        return true;
    }

    public boolean saveItemSetting(Player player, Region region) {

        if(region == null)
            return SaehyeonLib.sendError(player, ErrorMessage.REGION_NOT_EXIST);

        List<ItemStack> items = Arrays.asList( player.getOpenInventory().getTopInventory().getContents() );
        items.removeIf(Objects::isNull);

        region.getItemPlacer().setItems(new ArrayList<>( items ));

        return true;
    }

}
