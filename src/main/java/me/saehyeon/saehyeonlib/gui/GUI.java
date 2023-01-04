package me.saehyeon.saehyeonlib.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GUI {

    static HashMap<UUID, Enum> seeingGUI = new HashMap<>();
    static HashMap<UUID, Inventory> inv = new HashMap<>();

    static HashMap<UUID, ArrayList<GUIRule>> appliedRules = new HashMap<>();

    public static Enum getType(Player player) {
        return seeingGUI.getOrDefault(player.getUniqueId(),null);
    }
    public static void open(Player player, String title, int rows, Enum e) {

        if(e != null)
            seeingGUI.put(player.getUniqueId(),e);
        else
            seeingGUI.remove(player.getUniqueId());

        inv.put(player.getUniqueId(), Bukkit.createInventory(null, rows*9, title));
        player.openInventory(inv.get(player.getUniqueId()));
    }


    public static void open(Player player, String title, int rows) {
        inv.put(player.getUniqueId(), Bukkit.createInventory(null, rows*9, title));
        player.openInventory(inv.get(player.getUniqueId()));
    }

    public static void open(List<Player> players, int rows, String title) {
        players.forEach(p -> open(p, title, rows));
    }

    public static void close(Player player) {
        player.closeInventory();
        init(player);
    }

    public static void init(Player player) {
        inv.remove(player.getUniqueId());
        seeingGUI.remove(player.getUniqueId());
        appliedRules.remove(player.getUniqueId());
    }

    public static void close(List<Player> players) {
        players.forEach(GUI::close);
    }

    public static void clear(Player player) {
        for(int i = 0; i < player.getOpenInventory().getTopInventory().getSize(); i++)
            setItem(player, i, new ItemStack(Material.AIR));
    }

    public static void clear(List<Player> players) {
        players.forEach(GUI::clear);
    }

    public static void setItem(Player player, int slot, ItemStack item) {
        player.getOpenInventory().getTopInventory().setItem(slot, item);
    }

    public static void setRule(Player player, GUIRule... rule) {
        appliedRules.put(player.getUniqueId(), new ArrayList<>( Arrays.asList(rule) ));
    }

    public static void removeRule(Player player, GUIRule rule) {
        getRules(player).remove(rule);
    }

    public static ArrayList<GUIRule> getRules(Player player) {
        return appliedRules.getOrDefault(player.getUniqueId(),new ArrayList<>());
    }

    public static boolean isOpen(Player player) {
        return inv.containsKey(player.getUniqueId());
    }
}
