package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishSetmealAdapter;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;

public class DinnerDishSetmealAdapter extends OrderDishSetmealAdapter {

    protected InventoryCacheUtil mInventoryCacheUtil;

    public DinnerDishSetmealAdapter(Context context, int numColumns) {
        super(context, numColumns);
        mInventoryCacheUtil = InventoryCacheUtil.getInstance();
    }


    @Override
    protected int getItemLayoutResId() {
        return R.layout.order_dish_grid_item;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        TextView tvResidue = (TextView) convertView.findViewById(R.id.tv_residue);
        detailResidue(tvResidue, position);
        return convertView;
    }

    protected void detailResidue(TextView tvResidue, int position) {
        if (mInventoryCacheUtil.getSaleSwitch()) {//实时库存开启
            DishVo dishVo = (DishVo) getItem(position);
            if (dishVo != null) {
                String residue;
                boolean isShowInventory = InventoryCacheUtil.getInstance().getSaleNumOpenSwitch();
                if (dishVo.getDishShop() != null && dishVo.getDishShop().getSaleType() == SaleType.WEIGHING && dishVo.getInventoryNum() != null && isShowInventory) {
                    residue = mContext.getString(R.string.order_weighing_inventory_item);
                    tvResidue.setText(String.format(residue, dishVo.getInventoryNum()));
                    tvResidue.setVisibility(View.VISIBLE);
                } else if (dishVo.getInventoryNum() != null && isShowInventory) {
                    residue = mContext.getString(R.string.order_inventory_item);
                    tvResidue.setText(String.format(residue, dishVo.getInventoryNum()));
                    tvResidue.setVisibility(View.VISIBLE);
                } else if (dishVo.getDishShop() != null && dishVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
                    residue = mContext.getString(R.string.order_weighing_item);
                    tvResidue.setText(residue);
                    tvResidue.setVisibility(View.VISIBLE);
                } else {
                    tvResidue.setText("");
                    tvResidue.setVisibility(View.GONE);
                }
            }

        }
    }
}
