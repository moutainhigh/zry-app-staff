package com.zhongmei.beauty.operates;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.beauty.operates.message.BeautyTradeResp;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.util.List;



public interface BeautyOperates extends IOperates {


    void submitBeauty(TradeVo tradeVo, CalmResponseListener<ResponseObject<BeautyTradeResp>> listener, FragmentActivity activity);


    void modifyBeauty(TradeVo tradeVo, CalmResponseListener<ResponseObject<BeautyTradeResp>> listener, FragmentActivity activity);


    void deleteTrade(Long tradeId, Long serverUpdateTime, Reason mReason, List<InventoryItemReq> inventoryItems, CalmResponseListener<ResponseObject<BeautyTradeResp>> listener, Fragment fragment);
}
