package me.saehyeon.saehyeonlib.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class Locationf {
    public static boolean isWithin(Location loc, Location pos1, Location pos2) {
        double x1 = pos1.getX();
        double x2 = pos2.getX();

        double y1 = pos1.getY();
        double y2 = pos2.getY();

        double z1 = pos1.getZ();
        double z2 = pos2.getZ();

        return Math.min(x1,x2) <= loc.getX() && loc.getX() <= Math.max(x1,x2) && Math.min(y1,y2) <= loc.getY() && loc.getY() <= Math.max(y1,y2) && Math.min(z1,z2) <= loc.getZ() && loc.getZ() <= Math.max(z1,z2);
    }

    public static ArrayList<Location> getAll(Location pos1, Location pos2) {
        ArrayList<Location> locs = new ArrayList<>();

        for(double y = Math.min(pos1.getBlockY(), pos2.getBlockY()); y <= Math.max(pos1.getBlockY(),pos2.getBlockY()); y += 1) {
            for(double x = Math.min(pos1.getBlockX(), pos2.getBlockX()); x <= Math.max(pos1.getBlockX(),pos2.getBlockX()); x += 1) {
                for(double z = Math.min(pos1.getBlockZ(), pos2.getBlockZ()); z <= Math.max(pos1.getBlockZ(),pos2.getBlockZ()); z += 1) {
                    locs.add(new Location(pos1.getWorld(), x,y,z));
                }
            }
        }

        return locs;
    }

    public static boolean equal(Location loc1, Location loc2) {
        return loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ();
    }
}
