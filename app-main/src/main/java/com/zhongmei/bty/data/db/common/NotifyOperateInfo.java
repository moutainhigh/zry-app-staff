package com.zhongmei.bty.data.db.common;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.TradeStatus;

/**
 * 第三方平台(百度，饿了么，美团)取消订单信息
 */

public class NotifyOperateInfo {
    private Long tradeId;
    private Integer tradeStatus;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public TradeStatus getTradeStatus() {
        return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = ValueEnums.toValue(tradeStatus);
    }
}
