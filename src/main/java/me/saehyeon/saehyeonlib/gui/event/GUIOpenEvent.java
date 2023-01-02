package me.saehyeon.saehyeonlib.gui.event;

import me.saehyeon.saehyeonlib.gui.GUI;
import org.bukkit.entity.Player;

public class GUIOpenEvent {
    Player player;
    Enum guiType;

    public GUIOpenEvent(Player player) {
        this.player = player;
        this.guiType = GUI.getType(player);
    }

    public Player getPlayer() {
        return player;
    }

    public Enum getGUIType() {
        return guiType;
    }
}
