package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.bty.dinner.table.model.ZoneModel;

/**
 * 在切换区域时将发送此通知
 *
 * @version: 1.0
 * @date 2015年9月21日
 */
public class EventZoneSwitchNotice {

    public final ZoneModel zoneModel;

    public EventZoneSwitchNotice(ZoneModel zoneModel) {
        this.zoneModel = zoneModel;
    }

}
