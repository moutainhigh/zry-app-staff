package com.zhongmei.bty.dinner.table.event;

import java.util.List;

import com.zhongmei.bty.dinner.table.model.ZoneModel;

/**
 * 用于通知正餐桌台区域数据
 *
 * @version: 1.0
 * @date 2015年9月16日
 */
public class EventZonesNotice {

    public final List<ZoneModel> zones;
    public final String uuid;

    public EventZonesNotice(List<ZoneModel> zones, String uuid) {
        this.zones = zones;
        this.uuid = uuid;

    }

}
