package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.List;



public class GroupShopCartAdapter extends DinnerShopCartAdapter {


    public GroupShopCartAdapter(Context context) {
        super(context);
    }


    public void updateGroupSelectData(List<IShopcartItem> dataList, TradeVo tradeVo) {
        super.updateGroupSelectData(dataList, tradeVo, true);
        initialDishCheckStatus();
    }

    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        updateGroupData(dataList, tradeVo, false);
        initialDishCheckStatus();        updateTrade(tradeVo, isShowInvalid);        initialRelateDishInfo();    }

}
