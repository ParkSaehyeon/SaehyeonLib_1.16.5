package me.saehyeon.saehyeonlib.shop.event;

import me.saehyeon.saehyeonlib.shop.Shop;
import org.bukkit.entity.Player;

public class ShopCloseEvent {
    Shop shop;
    Player player;

    public ShopCloseEvent(Shop shop, Player player) {
        this.shop = shop;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Shop getShop() {
        return shop;
    }
    
}
