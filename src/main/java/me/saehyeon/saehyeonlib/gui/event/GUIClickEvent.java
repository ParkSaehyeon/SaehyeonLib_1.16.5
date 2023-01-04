package me.saehyeon.saehyeonlib.gui.event;

import me.saehyeon.saehyeonlib.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GUIClickEvent {
    Player player;
    Enum guiType;
    ItemStack clickedItem;

    public GUIClickEvent(Player player, ItemStack clickedItem) {
        this.player = player;
        this.guiType = GUI.getType(player);
        this.clickedItem = clickedItem;
    }

    public Player getPlayer() {
        return player;
    }

    public Enum getGUIType() {
        return guiType;
    }

    public ItemStack getClickedItem() {
        return clickedItem;
    }
}
