package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.bty.basemodule.trade.manager.BuffetOutTimeManager;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

/**
 * Created by demo on 2018/12/15
 */

public class EventTradeAmount {
    private String tradeAmount;

    private TradeVo mTradeVo;

    public TradeVo getmTradeVo() {
        return mTradeVo;
    }

    public void setmTradeVo(TradeVo mTradeVo) {
        this.mTradeVo = mTradeVo;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public EventTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public EventTradeAmount(String tradeAmount, TradeVo tradeVo) {
        this.tradeAmount = tradeAmount;
        this.mTradeVo = tradeVo;
    }

    /**
     * 已经支付完成
     */
    public boolean isPaid() {
        boolean paidOutTimeFee = BuffetOutTimeManager.calculateOutTimeFee(mTradeVo).compareTo(BuffetOutTimeManager.getPaidOutTimeFee(mTradeVo)) <= 0;
        if (mTradeVo != null && mTradeVo.getTrade().getBusinessType() == BusinessType.BUFFET && mTradeVo.getTrade().getTradePayStatus() == TradePayStatus.PREPAID && paidOutTimeFee) {
            return true;
        }
        return false;
    }


    /**
     * 是否需要退押金
     *
     * @return
     */
    public boolean isNeedDeposit() {
        if (mTradeVo != null && mTradeVo.getTradeDeposit() != null && mTradeVo.isPaidTradeposit()) {
            return true;
        }
        return false;
    }
}
