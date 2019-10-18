package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.List;



public class DinnerWestShopcartAdapter extends DinnerShopCartAdapter {

    public DinnerWestShopcartAdapter(Context context) {
        super(context);
    }

    @Override
    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        initCommonData(tradeVo);
        updateWestData(dataList, this);
        initialDishCheckStatus();        updateTrade(tradeVo, isShowInvalid);        initialRelateDishInfo();    }
}
