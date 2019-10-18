package com.zhongmei.bty.basemodule.notifycenter.event;

import java.io.Serializable;


public class EventSelectDinnertableNotice implements Serializable {


    public final Long zoneId;


    public final Long dinnertableId;


    public final Long tradeId;


    public boolean isCloseTableInfoFragment = true;

    public EventSelectDinnertableNotice(Long zoneId, Long dinnertableId, Long tradeId) {
        this(zoneId, dinnertableId, tradeId, true);
    }

    public EventSelectDinnertableNotice(Long zoneId, Long dinnertableId, Long tradeId, boolean colseTableInfo) {
        this.zoneId = zoneId;
        this.dinnertableId = dinnertableId;
        this.tradeId = tradeId;
        this.isCloseTableInfoFragment = colseTableInfo;
    }
}
