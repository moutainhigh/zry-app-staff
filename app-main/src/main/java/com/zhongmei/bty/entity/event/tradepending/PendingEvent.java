package com.zhongmei.bty.entity.event.tradepending;

import com.zhongmei.bty.basemodule.trade.bean.TradePendingVo;
import com.zhongmei.bty.commonmodule.event.EventBase;


public class PendingEvent extends EventBase {


    public final long totalPendingCount;

    public TradePendingVo mTradePendingVo;

    public boolean needFresh = false;

    public PendingEvent(long totalPendingCount) {
        this.totalPendingCount = totalPendingCount;
    }

    public PendingEvent(long totalPendingCount, Throwable error) {
        super(error);
        this.totalPendingCount = totalPendingCount;
    }

    public TradePendingVo getmTradeVo() {
        return mTradePendingVo;
    }

    public void setmTradeVo(TradePendingVo tradePendingVo) {
        this.mTradePendingVo = tradePendingVo;
    }

}
