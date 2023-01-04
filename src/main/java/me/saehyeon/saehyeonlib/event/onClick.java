package me.saehyeon.saehyeonlib.event;

import me.saehyeon.saehyeonlib.shop.Shop;
import me.saehyeon.saehyeonlib.state.PlayerState;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static org.bukkit.Bukkit.getServer;

public class onClick implements Listener {

    @EventHandler
    void onClickShopNPC(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        Entity clickedEn = e.getRightClicked();

        Shop shop = Shop.getByNPCName(clickedEn.getName());

        if(shop != null) {

            // 상점 GUI 열기
            shop.openGUI(p);

        }
    }

    @EventHandler
    void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        // 나무 도끼로 구역 지정
        if(Playerf.getMainHand(p).getType() == Material.WOODEN_AXE) {

            // 월드에딧 플러그인이 설치되어 있는지 확인하기
            boolean worldEdit = getServer().getPluginManager().getPlugin("WorldEdit")!=null;

            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

                e.setCancelled(true);

                // 만약 월드 에딧이 없으면 메세지 전송하기
                if(!worldEdit)
                    p.sendMessage("첫번째 지점이 설정되었습니다.");

                PlayerState.set(p, "pos1", e.getClickedBlock().getLocation());

            }

            else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                e.setCancelled(true);

                // 만약 월드 에딧이 없으면 메세지 전송하기
                if(!worldEdit)
                    p.sendMessage("두번째 지점이 설정되었습니다.");


                PlayerState.set(p, "pos2", e.getClickedBlock().getLocation());
            }

        }
    }
}
