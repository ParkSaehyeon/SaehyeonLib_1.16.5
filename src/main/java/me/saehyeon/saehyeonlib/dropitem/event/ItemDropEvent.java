package me.saehyeon.saehyeonlib.dropitem.event;

import me.saehyeon.saehyeonlib.region.Region;

public class ItemDropEvent {

    Region region;

    public ItemDropEvent(Region region) {
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }
}
