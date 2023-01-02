package me.saehyeon.saehyeonlib.role.event;

import me.saehyeon.saehyeonlib.role.Role;
import org.bukkit.entity.Player;

public class RoleChangeEvent {
    Role role;

    public RoleChangeEvent(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

}
