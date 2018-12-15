package com.zhongmei.beauty.order.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.bty.dinner.adapter.DinnerDishSetmealAdapter;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishSetmealAdapter;

public class BeautyDishSetmealAdapter extends DinnerDishSetmealAdapter {

    public BeautyDishSetmealAdapter(Context context, int numColumns) {
        super(context, numColumns);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.beauty_order_grid_item;
    }

    @Override
    protected int getBackgroundResource(int position) {
        return R.drawable.beauty_grid_item_selector;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        setViewStyle(convertView);
        TextView tvResidue = (TextView) convertView.findViewById(R.id.tv_residue);
        detailResidue(tvResidue, position);
        return convertView;
    }

    private void setViewStyle(View convertView) {
        TextView tvShortName = (TextView) convertView.findViewById(R.id.tv_short_name);
        tvShortName.setTextColor(mContext.getResources().getColor(R.color.beauty_color_434343));
    }

}
