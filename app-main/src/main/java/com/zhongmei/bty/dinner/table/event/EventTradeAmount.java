package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.bty.basemodule.trade.manager.BuffetOutTimeManager;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;



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


    public boolean isPaid() {
        boolean paidOutTimeFee = BuffetOutTimeManager.calculateOutTimeFee(mTradeVo).compareTo(BuffetOutTimeManager.getPaidOutTimeFee(mTradeVo)) <= 0;
        if (mTradeVo != null && mTradeVo.getTrade().getBusinessType() == BusinessType.BUFFET && mTradeVo.getTrade().getTradePayStatus() == TradePayStatus.PREPAID && paidOutTimeFee) {
            return true;
        }
        return false;
    }



    public boolean isNeedDeposit() {
        if (mTradeVo != null && mTradeVo.getTradeDeposit() != null && mTradeVo.isPaidTradeposit()) {
            return true;
        }
        return false;
    }
}
