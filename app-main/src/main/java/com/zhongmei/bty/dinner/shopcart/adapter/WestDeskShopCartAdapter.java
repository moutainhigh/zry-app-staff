package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.List;



public class WestDeskShopCartAdapter extends DinnerDeskShopcartAdapter {


    public WestDeskShopCartAdapter(Context context) {
        super(context);
    }


    @Override
    protected void refreshData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        if (isDishCheckMode) {
            initialMoveDishCheckStatus();            super.refreshData(dataList, tradeVo, isShowInvalid);
        } else {
            initCommonData(tradeVo);
            updateWestData(dataList, this);
            updateTrade(tradeVo, isShowInvalid);            initialRelateDishInfo();        }

    }
}
