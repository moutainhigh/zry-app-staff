package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.bty.dinner.table.model.ZoneModel;


public class EventZoneSwitchNotice {

    public final ZoneModel zoneModel;

    public EventZoneSwitchNotice(ZoneModel zoneModel) {
        this.zoneModel = zoneModel;
    }

}
