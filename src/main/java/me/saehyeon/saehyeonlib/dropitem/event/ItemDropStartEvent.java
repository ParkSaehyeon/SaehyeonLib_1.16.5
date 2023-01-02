package me.saehyeon.saehyeonlib.dropitem.event;

import me.saehyeon.saehyeonlib.region.Region;

public class ItemDropStartEvent {

    Region region;

    public ItemDropStartEvent(Region region) {
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }
}
