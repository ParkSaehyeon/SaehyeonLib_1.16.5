package me.saehyeon.saehyeonlib.dropitem;

import me.saehyeon.saehyeonlib.dropitem.event.ItemDropEvent;
import me.saehyeon.saehyeonlib.dropitem.event.ItemDropStartEvent;
import me.saehyeon.saehyeonlib.dropitem.event.ItemDropStopEvent;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.util.Locationf;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class DropItem implements Serializable {
    Region region;

    ArrayList<ItemStack> items = new ArrayList<>();

    /**
     * 아이템을 지역에 드롭할 주기
     */
    long dropPriod = 1;

    /**
     * 아이템을 지역에 드롭할 횟수<br>
     * 예를들어, 1이라면 모든 아이템을 한 번 씩 모든 지역에 랜덤으로 드롭합니다.<br>
     */
    int dropTime = 1;
    BukkitTask dropTask;

    boolean isDropping = false;

    public DropItem(Region region) {
        this.region = region;
    }

    public void setItems(ArrayList<ItemStack> items) {
        items.removeIf(Objects::isNull);
        this.items = items;
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public Region getRegion() {
        return region;
    }

    public void setDropTime(int dropTime) {
        this.dropTime = dropTime;
    }

    public int getDropTime() {
        return dropTime;
    }

    public void setDropPriod(long dropPriod) {
        this.dropPriod = dropPriod;
    }

    public long getDropPriod() {
        return dropPriod;
    }

    public boolean isDropping() {
        return isDropping;
    }

    public void StartDrop(boolean removeDroppedItems) {

        if(isDropping)
            StopDrop();

        SaehyeonLibEvent.doEvent(new ItemDropStartEvent(region));

        // 땅 바닥에 있는 아이템 치우기
        if(removeDroppedItems) {

            Bukkit.getWorlds().forEach(world -> {

                world.getEntities().forEach(en -> {
                    if(en instanceof Item)
                        en.remove();
                });

            });

        }
        isDropping = true;

        Location pos1 = region.getPosition()[0];
        Location pos2 = region.getPosition()[1];

        ArrayList<Location> locs = Locationf.getAll(pos1,pos2);

        // 아이템을 먹을 수 없는 지역은 삭제
        locs.removeIf(l -> (l.getBlock().getType() != Material.AIR && l.getBlock().getType() != Material.CAVE_AIR) || l.clone().add(0,-1,0).getBlock().getType() == Material.AIR);

        // 아이템을 드롭할 수 있는 블럭들의 갯수가 아이템의 갯수보다 적다면 떨구지 않음.
        if(locs.size() < items.size()) {
            Bukkit.broadcastMessage("§c아이템 드롭을 시작할 수 없습니다. 아이템을 드롭할 공간이 부족합니다.");
            return;
        }

        dropTask = Bukkit.getScheduler().runTaskTimer(SaehyeonLib.instance, () -> {

            for(int i = 0; i < dropTime; i++) {

                for (ItemStack item : items) {

                    if (item != null) {

                        int locIndex = new Random().nextInt(locs.size() - 1);
                        Location loc = locs.get(locIndex);

                        loc.getWorld().dropItem(loc, item);

                    }

                }

                SaehyeonLibEvent.doEvent(new ItemDropEvent(region));

            }

        },0,dropPriod*20);
    }

    public void StopDrop() {
        isDropping = false;

        if(dropTask != null) {
            dropTask.cancel();
            dropTask = null;
        }

        SaehyeonLibEvent.doEvent(new ItemDropStopEvent(region));

    }
}
