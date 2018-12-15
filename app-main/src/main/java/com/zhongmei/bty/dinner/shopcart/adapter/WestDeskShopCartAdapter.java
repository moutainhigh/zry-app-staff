package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class WestDeskShopCartAdapter extends DinnerDeskShopcartAdapter {


    public WestDeskShopCartAdapter(Context context) {
        super(context);
    }


    @Override
    protected void refreshData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        if (isDishCheckMode) {
            initialMoveDishCheckStatus();//初始化移菜/复制菜品 选择状态
            super.refreshData(dataList, tradeVo, isShowInvalid);
        } else {
            initCommonData(tradeVo);
            updateWestData(dataList, this);
            updateTrade(tradeVo, isShowInvalid);// 构建整单属性显示对象并刷新列表
            initialRelateDishInfo();// 初始化退菜数据
        }

    }
}
