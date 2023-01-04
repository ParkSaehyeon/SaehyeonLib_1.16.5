package me.saehyeon.saehyeonlib.shop;

import me.saehyeon.saehyeonlib.gui.event.GUIClickEvent;
import me.saehyeon.saehyeonlib.gui.event.GUICloseEvent;
import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.main.SaehyeonLibListener;
import me.saehyeon.saehyeonlib.shop.event.ShopClickItemEvent;
import me.saehyeon.saehyeonlib.shop.event.ShopCloseEvent;
import me.saehyeon.saehyeonlib.state.PlayerState;
import me.saehyeon.saehyeonlib.util.Itemf;

public class ShopGUIEvent implements SaehyeonLibListener {

    void onClick(GUIClickEvent e) {

        // 상점에 있는 아이템을 클릭함
        if(e.getGUIType() != null && e.getGUIType() == GUIType.SHOP) {

            // 클릭한 아이템이 상점 GUI에 배치된 아이템이라면 -> 상점 클릭 이벤트 발동

            // 플레이어가 현재 보고 있는 상점 불러오기
            Shop shop = (Shop)PlayerState.get(e.getPlayer(), "currentShop");

            if(shop.guiItems.containsValue(e.getClickedItem()))
                SaehyeonLibEvent.doEvent(new ShopClickItemEvent(shop, e.getPlayer(), e.getClickedItem()));

        }
    }

    void onClose(GUICloseEvent e) {

        // 상점이 닫힘
        if(e.getGUIType() != null && e.getGUIType() == GUIType.SHOP) {

            // 보고 있는 상점 불러오기
            Shop shop = (Shop)PlayerState.get(e.getPlayer(),"currentShop");

            // 보고 있는 상점 정보 없애기
            PlayerState.remove(e.getPlayer(), "currentShop");

            SaehyeonLibEvent.doEvent(new ShopCloseEvent(shop, e.getPlayer()));

        }
    }
}
