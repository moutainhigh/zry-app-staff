package com.zhongmei.beauty.order.adapter;

import android.content.Context;
import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishAdapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 美业产品/服务 展示adapter
 * Created by demo on 2018/12/15
 */

public class BeautyProductAdapter extends OrderDishAdapter {
    public BeautyProductAdapter(Context context, List<DishVo> dishList, int columns) {
        super(context, dishList, columns);
    }

    protected void inflateHolder(View convertView, ViewHolder viewHolder) {
        super.inflateHolder(convertView, viewHolder);
    }

    protected void setResidueView(DishVo dishVo, DishShop dishShop, ViewHolder holder) {
        setInventView(dishVo, holder.tvResidue);
    }


    protected int getClearBg(ViewHolder holder, DishVo dishVo) {
        return R.drawable.beauty_grid_item_selector;
    }

    protected int getItemLayoutResId() {
        return R.layout.beauty_order_grid_item;
    }

    @Override
    protected int getPlaceBg(ViewHolder holder) {
//        holder.topView.setVisibility(View.GONE);
        return R.drawable.beauty_card_empty;
    }

    @Override
    protected int getResourceCardId(DishVo dishVo, int position) {
        return R.drawable.beauty_grid_item_selector;
    }

    @Override
    protected int getResourceCardId1(DishVo dishVo, int position) {
        return R.drawable.beauty_grid_item_selector;
    }

    @Override
    protected void controlViewVisible(ViewHolder holder, DishVo dishVo) {
//        holder.topView.setVisibility(View.VISIBLE);
        if (dishVo.isClear()) {
//            holder.topView.setBackgroundResource(R.drawable.beauty_grid_item_topview_clear_bg);
            holder.tvResidue.setTextColor(mContext.getResources().getColor(R.color.beauty_color_BCBCBC));
            holder.tvShortName.setTextColor(mContext.getResources().getColor(R.color.beauty_color_BCBCBC));
            holder.tvMarketPrice.setTextColor(mContext.getResources().getColor(R.color.beauty_color_BCBCBC));
        } else {
            holder.tvShortName.setTextColor(mContext.getResources().getColor(R.color.beauty_color_434343));
//            holder.topView.setBackgroundResource(R.drawable.beauty_grid_item_topview_bg);
            holder.tvResidue.setTextColor(mContext.getResources().getColor(R.color.beauty_color_6E8DFF));
            holder.tvMarketPrice.setTextColor(mContext.getResources().getColor(R.color.beauty_color_FF666666));
        }
    }

    @Override
    protected BigDecimal getSelectedQty(DishVo dishVo) {
        Map<String, BigDecimal> map = DinnerShoppingCart.getInstance().getDinnerDishSelectQTY();
        BigDecimal qty = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            if (dishVo.isSameSeries(entry.getKey())) {
                qty = qty.add(entry.getValue());
            }
        }
        return qty;
    }
}
