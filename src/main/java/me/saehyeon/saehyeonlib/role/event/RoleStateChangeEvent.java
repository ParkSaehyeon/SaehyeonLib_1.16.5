package me.saehyeon.saehyeonlib.role.event;

import me.saehyeon.saehyeonlib.role.Role;

public class RoleStateChangeEvent {
    String key;
    Object value;
    Role role;

    public RoleStateChangeEvent(String key,Object value, Role role) {
        this.key = key;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
