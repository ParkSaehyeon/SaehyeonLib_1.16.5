package me.saehyeon.saehyeonlib.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Itemf {
    public static ItemStack createItemStack(Material material, int amount) {
        return new ItemStack(material, amount);
    }

    public static ItemStack createItemStack(Material material, int amount, String displayName, List<String> lore) {
        ItemStack item  = createItemStack(material,amount);
        ItemMeta meta   = item.getItemMeta();

        if(displayName != null)
            meta.setDisplayName(displayName);

        if(lore != null)
            meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setDisplayName(ItemStack itemstack, String displayName) {
        ItemMeta meta = itemstack.getItemMeta();
        meta.setDisplayName(displayName);
        itemstack.setItemMeta(meta);

        return itemstack;
    }

    public static ItemStack addLore(ItemStack itemstack, String lore) {

        List<String> lores = getLore(itemstack);
        lores.add(lore);

        ItemMeta meta = itemstack.getItemMeta();
        meta.setLore(lores);
        itemstack.setItemMeta(meta);

        return itemstack;
    }

    public static String getDisplayName(ItemStack itemStack) {
        return itemStack != null && itemStack.getItemMeta() != null ? itemStack.getItemMeta().getDisplayName() : "";
    }

    public static ItemStack setLore(ItemStack itemstack, String... lores) {

        ItemMeta meta = itemstack.getItemMeta();
        meta.setLore(Arrays.asList(lores));
        itemstack.setItemMeta(meta);

        return itemstack;

    }

    public static List<String> getLore(ItemStack itemstack) {

        if(itemstack == null || itemstack.getItemMeta() == null)
            return new ArrayList<>();

        List<String> lores = itemstack.getItemMeta().getLore();

        return lores == null ? new ArrayList<>() : new ArrayList<>( lores );
    }
    public static ItemStack removeLore(ItemStack itemstack, int index) {
        ItemMeta meta       = itemstack.getItemMeta();

        if(meta == null)
            return itemstack;

        List<String> lore   = meta.getLore();

        if(lore != null && lore.size() >= index)
            lore.remove(index-1);

        meta.setLore( lore );

        itemstack.setItemMeta(meta);

        return itemstack;
    }

    public static ItemStack setCustomModelData(ItemStack itemstack, int customModelData) {
        ItemMeta meta = itemstack.getItemMeta();
        meta.setCustomModelData(customModelData);
        itemstack.setItemMeta(meta);

        return itemstack;

    }

    public static ItemStack getHead(String playerName) {

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        return Bukkit.getUnsafe().modifyItemStack(skull, "{SkullOwner:\""+playerName+"\"}");
    }

    public static void clearDroppedItems() {

        Bukkit.getWorlds().forEach(world -> {

            world.getEntities().forEach(en -> {
                if(en instanceof Item)
                    en.remove();
            });

        });

    }

}
