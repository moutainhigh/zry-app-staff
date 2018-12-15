package com.zhongmei.bty.entity.event.tradepending;

import com.zhongmei.bty.basemodule.trade.bean.TradePendingVo;
import com.zhongmei.bty.commonmodule.event.EventBase;

/**
 * 执行挂单操作后的通知
 *
 * @version: 1.0
 * @date 2015年5月8日
 */
public class PendingEvent extends EventBase {

    /**
     * 当前挂单的trade数量，挂单失败时此字段也会有值
     */
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
