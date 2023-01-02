package me.saehyeon.saehyeonlib.role.event;

import org.bukkit.entity.Player;

import java.util.UUID;

public class ExPlayerRemoveEvent {
    UUID uuid;

    public ExPlayerRemoveEvent(UUID uuid) {
        this.uuid = uuid;
    }
}
