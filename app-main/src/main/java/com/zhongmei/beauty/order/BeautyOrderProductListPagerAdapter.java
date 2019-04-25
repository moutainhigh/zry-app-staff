package com.zhongmei.beauty.order;

import android.content.Context;
import android.widget.ListAdapter;

import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.beauty.order.adapter.BeautyProductAdapter;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishListPagerAdapter;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public abstract class BeautyOrderProductListPagerAdapter extends OrderDishListPagerAdapter {


    public BeautyOrderProductListPagerAdapter(Context context, List<DishVo> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected int getNumColumns() {
        return 3;
    }

    @Override
    protected int getNumRows() {
        return 6;
    }

    @Override
    protected ListAdapter getAdapter(List<DishVo> subDataSet) {
        BeautyProductAdapter orderDishAdapter = new BeautyProductAdapter(mContext, subDataSet, getNumColumns());
//        orderDishAdapter.setDishCardBg(this.dishCardType);
        orderDishAdapter.setEditMode(this.isEditMode());//yutang modify 20160811
        orderDishAdapter.setHidClearNumber(this.isHidClearNumber());//yutang modify 20160816
        if (getGridHeight() > 0 && getNumRows() > 0) {
            orderDishAdapter.setItemHeight(getGridHeight() / getNumRows());
        }
        return orderDishAdapter;
    }
}
