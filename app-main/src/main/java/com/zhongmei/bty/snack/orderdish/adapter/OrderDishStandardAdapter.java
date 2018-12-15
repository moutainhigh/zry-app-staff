package com.zhongmei.bty.snack.orderdish.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;

public class OrderDishStandardAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private String mPropertyTypeName;

    private List<DishStandardVo> mDataSet;

    private DishStandardVo mSelectedItem;

    public OrderDishStandardAdapter(Context context, String propertyTypeName, List<DishStandardVo> dataSet) {
        this.mContext = context;
        this.mPropertyTypeName = propertyTypeName;
        this.mDataSet = dataSet;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDataSet.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null;
        } else {
            return mDataSet.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.orderdish_clear_status_item, parent, false);
            holder.tvStandardName = (TextView) convertView.findViewById(R.id.tv_property_name);
            holder.tvStandardName.setBackgroundDrawable(mContext.getResources()
                    .getDrawable(R.drawable.orderdish_clear_status_standard_item_bg_selector));
            holder.tvStandardName.setTextColor(mContext.getResources()
                    .getColorStateList(R.color.orderdish_clear_status_item_text_sale_selector));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        bindView(holder, position);

        return convertView;
    }

    private void bindView(ViewHolder holder, int position) {
        if (position == 0) {
            holder.tvStandardName.setText(mPropertyTypeName);
            if (mSelectedItem == null) {
                holder.tvStandardName.setSelected(true);
            } else {
                holder.tvStandardName.setSelected(false);
            }
        } else {
            DishStandardVo dishStandardVo = (DishStandardVo) getItem(position);

            if (mSelectedItem != null && mSelectedItem == dishStandardVo) {
                holder.tvStandardName.setSelected(true);
            } else {
                holder.tvStandardName.setSelected(false);
            }

            holder.tvStandardName.setText(dishStandardVo.getProperty().getName());
        }
    }

    public void setSelectedItem(DishStandardVo dishStandardVo) {
        mSelectedItem = dishStandardVo;
    }

    static class ViewHolder {
        TextView tvStandardName;
    }

}
