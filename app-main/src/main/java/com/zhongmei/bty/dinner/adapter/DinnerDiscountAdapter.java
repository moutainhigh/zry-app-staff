package com.zhongmei.bty.dinner.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.vo.DiscountShopVo;
import com.zhongmei.bty.basemodule.discount.entity.DiscountShop;


public class DinnerDiscountAdapter extends BaseAdapter {
    private Context mContext;

    private List<DiscountShopVo> discountList;

    private int mBgResId = R.drawable.ic_dish_property_item_bg;

    public DinnerDiscountAdapter(Context context, List<DiscountShopVo> discountList) {
        mContext = context;
        this.discountList = discountList;
    }

    public void setItemBg(int bgResId) {
        mBgResId = bgResId;
    }

    @Override
    public int getCount() {
                if (discountList == null)
            return 0;
        return discountList.size();
    }

    @Override
    public DiscountShopVo getItem(int position) {
                if (discountList.size() > position) {
            return discountList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dinner_discount_item, null);
            convertView.setBackgroundResource(mBgResId);
            viewHolder = new ViewHolder();
            viewHolder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        DiscountShopVo discoutShopVo = getItem(position);
        bindData(viewHolder, discoutShopVo, position);
        return convertView;
    }

    private void bindData(ViewHolder viewHolder, final DiscountShopVo discoutShopVo, final int position) {
                if (discoutShopVo == null) {
            return;
        }
        DiscountShop discountShop = discoutShopVo.getDiscountShop();
        viewHolder.tv_discount.setText(discountShop.getName());
        if (discoutShopVo.isSelected()) {
            viewHolder.tv_discount.setSelected(true);
        } else {
            viewHolder.tv_discount.setSelected(false);
        }
    }

    public synchronized void resetData(List<DiscountShopVo> discountList) {
        this.discountList = discountList;
        notifyDataSetChanged();
    }

    public void changeSelected(int position) {
        if (discountList == null) {
            return;
        }
        for (int i = 0; i < discountList.size(); i++) {
            if (i == position) {
                discountList.get(i).setSelected(true);
            } else {
                discountList.get(i).setSelected(false);
            }
        }
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tv_discount;
    }

}
