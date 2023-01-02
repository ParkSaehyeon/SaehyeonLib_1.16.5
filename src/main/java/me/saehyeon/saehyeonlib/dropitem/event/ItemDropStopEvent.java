package me.saehyeon.saehyeonlib.dropitem.event;

import me.saehyeon.saehyeonlib.region.Region;

public class ItemDropStopEvent {

    Region region;

    public ItemDropStopEvent(Region region) {
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }
}
