package com.zhongmei.beauty.order.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishAdapter;
import com.zhongmei.yunfu.net.volley.RequestQueue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class BeautyProductAdapter extends OrderDishAdapter {

    DisplayImageOptions options = null;

    public BeautyProductAdapter(Context context, List<DishVo> dishList, int columns) {
        super(context, dishList, columns);
    }

    protected void inflateHolder(View convertView, ViewHolder viewHolder) {
        super.inflateHolder(convertView, viewHolder);
        viewHolder.iv_shopLogo = (ImageView) convertView.findViewById(R.id.iv_goods_icon);
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
    protected void bindView(ViewHolder holder, int position) {
        super.bindView(holder, position);
    }

    @Override
    protected void controlViewVisible(ViewHolder holder, DishVo dishVo) {
        if (dishVo.isClear()) {
            holder.tvResidue.setTextColor(mContext.getResources().getColor(R.color.beauty_color_BCBCBC));
            holder.tvShortName.setTextColor(mContext.getResources().getColor(R.color.beauty_color_BCBCBC));
            holder.tvMarketPrice.setTextColor(mContext.getResources().getColor(R.color.beauty_color_BCBCBC));
        } else {
            holder.tvShortName.setTextColor(mContext.getResources().getColor(R.color.beauty_color_434343));
            holder.tvResidue.setTextColor(mContext.getResources().getColor(R.color.beauty_color_6E8DFF));
            holder.tvMarketPrice.setTextColor(mContext.getResources().getColor(R.color.beauty_color_FF666666));
        }

        ImageLoader.getInstance().displayImage(dishVo.getDishShopImgUrl(), holder.iv_shopLogo, getImageLoadOption());
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

    public DisplayImageOptions getImageLoadOption(){
        if(options==null){
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.icon_image_empty) // resource or drawable
                    .showImageForEmptyUri(R.drawable.icon_image_empty) // resource or drawable
                    .showImageOnFail(R.drawable.icon_image_empty) // resource or drawable
                    .resetViewBeforeLoading(false)  // default
                    .delayBeforeLoading(1000)
                    .cacheInMemory(true) // default
                    .cacheOnDisk(true) // default
                    .considerExifParams(true) // default
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                    .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                    .displayer(new SimpleBitmapDisplayer()) // default
                    .build();
        }
        return options;
    }
}
