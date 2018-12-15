package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.List;

/**
 * 西餐点菜界面购物车显示
 * Created by demo on 2018/12/15
 */

public class DinnerWestShopcartAdapter extends DinnerShopCartAdapter {
    /**
     * @param context
     * @Constructor
     * @Description 构造函数，
     */
    public DinnerWestShopcartAdapter(Context context) {
        super(context);
    }

    @Override
    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        initCommonData(tradeVo);
        updateWestData(dataList, this);
        initialDishCheckStatus();// 初始化菜品选择状态，等叫等
        updateTrade(tradeVo, isShowInvalid);// 构建整单属性显示对象并刷新列表
        initialRelateDishInfo();// 初始化退菜数据
    }
}
