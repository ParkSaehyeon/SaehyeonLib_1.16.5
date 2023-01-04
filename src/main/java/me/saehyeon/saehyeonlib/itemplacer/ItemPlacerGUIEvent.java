package me.saehyeon.saehyeonlib.itemplacer;

import me.saehyeon.saehyeonlib.gui.event.GUICloseEvent;
import me.saehyeon.saehyeonlib.main.SaehyeonLibListener;
import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.state.PlayerState;

public class ItemPlacerGUIEvent implements SaehyeonLibListener {

    void onClose(GUICloseEvent e) {

        if (e.getGUIType() != null && e.getGUIType().equals(GUIType.ITEM_SETTING)) {

            // 아이템 설정 저장
            new ItemPlacerGUI().saveItemSetting(e.getPlayer(), (Region)PlayerState.get(e.getPlayer(), "selectedRegion"));

            // 플레이어의 State 변경 (선택된 지역 없애기)
            PlayerState.remove(e.getPlayer(), "chest.gui.region");
        }

    }
}
