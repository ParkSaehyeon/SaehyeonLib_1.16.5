package me.saehyeon.saehyeonlib.dropitem;

import me.saehyeon.saehyeonlib.gui.event.GUICloseEvent;
import me.saehyeon.saehyeonlib.main.SaehyeonLibListener;

public class DropItemGUIEvent implements SaehyeonLibListener {
    void onClose(GUICloseEvent e) {

        if(e.getGUIType() != null && e.getGUIType() == DropItemGUIType.ITEM_SETTING) {

            // 아이템 저장
            DropItemGUI.saveItemSetting(e.getPlayer());

        }

    }
}
