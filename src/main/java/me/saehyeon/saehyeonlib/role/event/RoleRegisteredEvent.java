package me.saehyeon.saehyeonlib.role.event;

import me.saehyeon.saehyeonlib.role.Role;

public class RoleRegisteredEvent {

    Role role;

    public RoleRegisteredEvent(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
}
