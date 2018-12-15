package com.zhongmei.bty.basemodule.notifycenter.event;

import java.io.Serializable;

/**
 * 发送要求选中指定的桌台的通知
 *
 * @version: 1.0
 * @date 2016年4月1日
 */
public class EventSelectDinnertableNotice implements Serializable {

    /**
     * 区域ID
     */
    public final Long zoneId;

    /**
     * 桌台ID（即tableId）
     */
    public final Long dinnertableId;

    /**
     * 订单ID
     */
    public final Long tradeId;

    /**
     * 是否关闭详情页面
     */
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
