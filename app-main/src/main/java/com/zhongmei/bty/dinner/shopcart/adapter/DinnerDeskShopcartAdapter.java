package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;

import java.util.ArrayList;
import java.util.List;


/**
 * 桌台购物车界面视图adapter
 * Created by demo on 2018/12/15
 */

public class DinnerDeskShopcartAdapter extends DinnerShopCartAdapter {

    public DinnerDeskShopcartAdapter(Context context) {
        super(context);
    }

    protected View loadDishLayout() {
        View convertView = LayoutInflater.from(context).inflate(R.layout.dinner_dish_shopcart_item_desk, null);
        return convertView;
    }

    @Override
    protected View initDishLayout(ViewHolder holder) {
        View convertView = loadDishLayout();
        initDishCommon(holder, convertView);
        initSlideLayout(holder, convertView);
        initDishOtherLayout(holder, convertView);
        initTimeView(holder, convertView);
        return convertView;
    }

    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        ArrayList<DishDataItem> oldData = new ArrayList<>(data);
        refreshData(dataList, tradeVo, isShowInvalid);
        initialMoveDishCheckStatus();//初始化移菜/复制菜品 选择状态
        initialServingStatus();// 初始化上菜状态
        resetSlideItems();// 初始化item滑动状态
        //处理划菜时，不被刷新导致划菜状态丢失
        if (oldData.size() > 0 && data.size() > 0) {
            for (DishDataItem item : data) {
                if (item.getBase() != null) {
                    DishDataItem dishDataItem = getDishDataItem(oldData, item.getBase().getId());
                    if (dishDataItem != null) {
                        item.setDishServing(dishDataItem.isDishServing());
                    }
                }
            }
        }
    }


    protected void refreshData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        super.updateData(dataList, tradeVo, isShowInvalid);
    }

    /**
     * @Title: getIsComboDiyWh
     * @Description: 设置子菜显示样式
     * @Param @param context
     * @Return LinearLayout.LayoutParams 返回类型
     */
    public RelativeLayout.LayoutParams getIsComboDiyWh(Context context) {
        int left = DensityUtil.dip2px(context, 427);
        int topOrBottom = DensityUtil.dip2px(context, 5);
        RelativeLayout.LayoutParams diyWh =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(left, topOrBottom, 0, topOrBottom);
        return diyWh;
    }

    /**
     * @Title: getNoComboDiyWh
     * @Description: 获取普通菜品边距
     * @Param @param context
     * @Return LinearLayout.LayoutParams 返回类型
     */
    public RelativeLayout.LayoutParams getNoComboDiyWh(Context context) {
        int top = (int) context.getResources().getDimension(R.dimen.dinner_shoppingCard_dishName_margin);
        int bottom = (int) context.getResources().getDimension(R.dimen.dinner_shoppingCard_dishName_margin);
        int left = DensityUtil.dip2px(context, 409);
        RelativeLayout.LayoutParams diyWh =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(left, top, 0, bottom);
        return diyWh;
    }


    protected LinearLayout.LayoutParams getExtraDiyWh(Context context, boolean isChild) {
        int left = 0;
        if (isChild) {
            left = DensityUtil.dip2px(context, 44);
        } else {
            left = DensityUtil.dip2px(context, 30);
        }
        return getExtraDiyWh(left, 0, 0, 0);
    }

}
