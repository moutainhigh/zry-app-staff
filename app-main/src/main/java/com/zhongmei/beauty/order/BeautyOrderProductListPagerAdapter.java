package com.zhongmei.beauty.order;

import android.content.Context;
import android.widget.ListAdapter;

import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.beauty.order.adapter.BeautyProductAdapter;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishListPagerAdapter;
import com.zhongmei.yunfu.R;

import java.util.List;



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
        return mContext.getResources().getInteger(R.integer.beauty_rows_size);
    }

    @Override
    protected ListAdapter getAdapter(List<DishVo> subDataSet) {
        BeautyProductAdapter orderDishAdapter = new BeautyProductAdapter(mContext, subDataSet, getNumColumns());
        orderDishAdapter.setEditMode(this.isEditMode());        orderDishAdapter.setHidClearNumber(this.isHidClearNumber());        if (getGridHeight() > 0 && getNumRows() > 0) {
            orderDishAdapter.setItemHeight(getGridHeight() / getNumRows());
        }
        return orderDishAdapter;
    }
}
