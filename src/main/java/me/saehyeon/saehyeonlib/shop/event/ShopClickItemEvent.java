package me.saehyeon.saehyeonlib.shop.event;

import me.saehyeon.saehyeonlib.shop.Shop;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopClickItemEvent {
    Shop shop;
    Player player;
    ItemStack clickedItem;

    public ShopClickItemEvent(Shop shop, Player player, ItemStack clickedItem) {
        this.shop = shop;
        this.player = player;
        this.clickedItem = clickedItem;
    }

    public Player getPlayer() {
        return player;
    }

    public Shop getShop() {
        return shop;
    }

    public ItemStack getClickedItem() {
        return clickedItem;
    }
    
}
