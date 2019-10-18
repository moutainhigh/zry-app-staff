package com.zhongmei.bty.dinner.table.event;

import java.util.List;

import com.zhongmei.bty.dinner.table.model.ZoneModel;


public class EventZonesNotice {

    public final List<ZoneModel> zones;
    public final String uuid;

    public EventZonesNotice(List<ZoneModel> zones, String uuid) {
        this.zones = zones;
        this.uuid = uuid;

    }

}
