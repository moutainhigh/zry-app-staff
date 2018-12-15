package com.zhongmei.bty.cashier.ordercenter.manager;

import android.support.v4.app.FragmentActivity;

import com.zhongmei.bty.basemodule.trade.bean.DinnerTableInfo;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.trade.message.TakeDishResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.monitor.CalmResponseListener;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class GroupOrderCenterDetailManager extends OrderCenterDetailManager {
    public GroupOrderCenterDetailManager() {
        super();
    }

    @Override
    public void acceptDinner(Trade trade, ResponseListener<TradeResp> listener) {
        mTradeOperates.acceptDinner(trade, listener);
    }

    @Override
    public void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, ResponseListener<TradeResp> listener) {
        mTradeOperates.dinnerSetTableAndAccept(tradeVo, table, Bool.YES, listener);
    }

    @Override
    public DinnerTableInfo getDinnerTable(TradeVo tradeVo) throws Exception {
        return dal.getDinnerTableByTradeVo(tradeVo);
    }

    @Override
    public void refundDinner(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener) {
        mTradeOperates.refundDinner(tradeVo, reason, inventoryItems, listener);
    }

    @Override
    public void recisionDinner(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener) {
        mTradeOperates.recisionDinner(tradeId, serverUpdateTime, states, reason, inventoryItems, listener);
    }

    @Override
    public void refuseDinner(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener) {
        mTradeOperates.refuseDinner(tradeVo, reason, listener);
    }

    @Override
    public void takeDish(TradeExtra tradeExtra, FragmentActivity activity, CalmResponseListener<ResponseObject<TakeDishResp>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TradeReturnInfo findTradeReturnInfo(Long tradeId) {
        return null;
    }
}
