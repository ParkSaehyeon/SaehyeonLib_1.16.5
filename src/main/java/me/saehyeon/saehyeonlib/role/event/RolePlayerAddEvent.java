package me.saehyeon.saehyeonlib.role.event;

import me.saehyeon.saehyeonlib.role.Role;
import org.bukkit.entity.Player;

public class RolePlayerAddEvent {
    Player player;
    Role role;

    public RolePlayerAddEvent(Player player, Role role) {
        this.player = player;
        this.role = role;
    }

    public Player getPlayer() {
        return player;
    }

    public Role getRole() {
        return role;
    }

}
