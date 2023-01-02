package me.saehyeon.saehyeonlib.role.event;

import org.bukkit.entity.Player;

import java.util.UUID;

public class ExPlayerAddEvent {
    UUID uuid;

    public ExPlayerAddEvent(UUID uuid) {
        this.uuid = uuid;
    }
}
