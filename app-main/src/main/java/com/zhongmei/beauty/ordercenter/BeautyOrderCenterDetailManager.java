package com.zhongmei.beauty.ordercenter;

import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.cashier.ordercenter.manager.OrderCenterDetailManager;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.util.List;



public class BeautyOrderCenterDetailManager extends OrderCenterDetailManager {

    @Override
    public void recisionBeauty(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener) {
        mTradeOperates.recisionBeauty(tradeId, serverUpdateTime, states, reason, inventoryItems, listener);
    }

    @Override
    public void refundBeauty(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener) {
        mTradeOperates.refundBeauty(tradeVo, reason, inventoryItems, listener);
    }

    @Override
    public void repayBeautyOrder(TradeVo tradeVo, Reason reason, ResponseListener<PaymentResp> listener) {
        mTradeOperates.beautyTradeRepay(toTradeRepayReq(tradeVo, reason), listener);
    }

    @Override
    public TradeReturnInfo findTradeReturnInfo(Long tradeId) {
        return null;
    }
}
