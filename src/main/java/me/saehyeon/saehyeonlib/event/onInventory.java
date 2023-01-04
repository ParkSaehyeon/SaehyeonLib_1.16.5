package me.saehyeon.saehyeonlib.event;

import me.saehyeon.saehyeonlib.gui.event.GUIClickEvent;
import me.saehyeon.saehyeonlib.gui.event.GUICloseEvent;
import me.saehyeon.saehyeonlib.gui.event.GUIOpenEvent;
import me.saehyeon.saehyeonlib.itemplacer.GUIType;
import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.gui.GUI;
import me.saehyeon.saehyeonlib.gui.GUIRule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class onInventory implements Listener {
    @EventHandler
    void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();

        if(GUI.isOpen(p)) {

            // 클릭했다는 이벤트 발동
            SaehyeonLibEvent.doEvent(new GUIClickEvent(p,e.getCurrentItem()));

            // GUI 아이템 클릭이 금지되어 있으면 이벤트 취소
            if(GUI.getRules(p).contains(GUIRule.CANT_ITEM_CLICK)) {
                e.setCancelled(true);
                return;
            }

            // 현재 열고 있는 GUI가 상자 원격제어 GUI임
            if(GUI.getType(p) == GUIType.REMOTE) {

                if(e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getItemMeta().getLore() == null)
                    return;

                e.setCancelled(true);

                // 클릭한 아이템의 lore에서 좌표 가져오기
                String lore     = e.getCurrentItem().getItemMeta().getLore().get(0);
                String locStr   = ChatColor.stripColor(lore).split("에 ")[0];

                String[] splitStr     = locStr.split(", ");

                if(splitStr.length < 3)
                    return;

                double dx = Double.parseDouble(splitStr[0]);
                double dy = Double.parseDouble(splitStr[1]);
                double dz = Double.parseDouble(splitStr[2]);

                Location location = new Location(p.getWorld(), dx,dy,dz);

                if(!location.getBlock().getType().isInteractable()) {
                    p.sendMessage("§c해당 위치의 블럭이 변경되었거나 없어진 것 같습니다.");
                    return;
                }

                // 해당 블럭의 인벤토리 열기
                switch (location.getBlock().getType()) {
                    case TRAPPED_CHEST:
                    case CHEST:

                        Chest chest = (Chest)location.getBlock().getState();

                        p.openInventory(chest.getBlockInventory());

                        break;

                    case DISPENSER:

                        Dispenser dispenser = (Dispenser) location.getBlock().getState();

                        p.openInventory(dispenser.getInventory());

                        break;

                    case HOPPER:

                        Hopper hopper = (Hopper) location.getBlock().getState();

                        p.openInventory(hopper.getInventory());

                        break;

                    case DROPPER:

                        Dropper dropper = (Dropper) location.getBlock().getState();

                        p.openInventory(dropper.getInventory());

                        break;

                    default:

                        p.sendMessage("§c이 위치에 있는 블럭은 아이템을 수납할 수 없습니다. 블럭이 변경되었거나 없어진 것 같습니다.");
                        break;

                }

            }

        }

    }

    @EventHandler
    void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player)e.getPlayer();

        if(GUI.isOpen(p))
            SaehyeonLibEvent.doEvent(new GUIOpenEvent( p ));
    }

    @EventHandler
    void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();

        if(GUI.isOpen(p)) {

            // GUI가 닫을 수 없게 설정되어 있음
            if(GUI.getRules(p).contains(GUIRule.CANT_CLOSE)) {

                Inventory inv = p.getOpenInventory().getTopInventory();

                BukkitTaskf.wait(() -> p.openInventory(inv),1);

                return;
            }

            // 이벤트 발생 시키기
            SaehyeonLibEvent.doEvent(new GUICloseEvent( (Player)e.getPlayer() ));

            // GUI 닫을 수 있으면 현재 보고 있는 GUI 종류 Enum도 제거하기
            GUI.init(p);

        }
    }
}
