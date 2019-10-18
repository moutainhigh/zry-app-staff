package com.zhongmei.bty.manager;

import java.util.List;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;

public class TradeCenterManager {
    private static TradeCenterManager sOrderCenterManager = new TradeCenterManager();

    private TradeCenterManager() {
            }

    public static TradeCenterManager getInstance() {
        return sOrderCenterManager;
    }

    Trade mCurrentTrade;
    TradeExtra mCurrentTradeExtra;
    TradeCustomer mTradeCustomer;

    List<TradeItemVo> mOrderDetailInfolist;

    public void setTradeInfo(Trade mCurrentTrade, TradeExtra mCurrentTradeExtra, TradeCustomer mTradeCustomer) {
        this.mCurrentTrade = mCurrentTrade;
        this.mCurrentTradeExtra = mCurrentTradeExtra;
        this.mTradeCustomer = mTradeCustomer;
    }


    public Trade getmCurrentTrade() {
        return mCurrentTrade;
    }

    public void setmCurrentTrade(Trade mCurrentTrade) {
        this.mCurrentTrade = mCurrentTrade;
    }

    public TradeExtra getmCurrentTradeExtra() {
        return mCurrentTradeExtra;
    }

    public void setmCurrentTradeExtra(TradeExtra mCurrentTradeExtra) {
        this.mCurrentTradeExtra = mCurrentTradeExtra;
    }

    public TradeCustomer getmTradeCustomer() {
        return mTradeCustomer;
    }

    public void setmTradeCustomer(TradeCustomer mTradeCustomer) {
        this.mTradeCustomer = mTradeCustomer;
    }

    public List<TradeItemVo> getmOrderDetailInfolist() {
        return mOrderDetailInfolist;
    }

    public void setmOrderDetailInfolist(List<TradeItemVo> mOrderDetailInfolist) {
        this.mOrderDetailInfolist = mOrderDetailInfolist;
    }


}
