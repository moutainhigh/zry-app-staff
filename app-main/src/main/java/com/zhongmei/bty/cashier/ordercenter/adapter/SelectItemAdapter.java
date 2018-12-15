package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import java.util.ArrayList;
import java.util.List;


public class SelectItemAdapter extends BaseAdapter {

    private Context mContext;
    private String mCurrenCheckId;
    private int selectedPosition = -1;
    private List<String> strings = new ArrayList<>();

    public SelectItemAdapter(Context context) {
        mContext = context;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    /**
     * 当前选中的门店ID
     */
    public void currenCheckId(String id) {
        mCurrenCheckId = id;
    }


    public void setData(List<String> data) {
        strings.clear();
        if (data != null) {
            strings.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_center_reason_lv_item_dialog, null);
            holder.mCheckIcon = (ImageView) convertView.findViewById(R.id.check_icon);
            holder.mShopName = (TextView) convertView.findViewById(R.id.reason);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mShopName.setText(strings.get(position));
        if (selectedPosition == position) {
            holder.mCheckIcon.setBackgroundResource(R.drawable.print_checkbox_selected_new);
        } else {
            holder.mCheckIcon.setBackgroundResource(R.drawable.print_checkbox_default);
        }
        holder.mCheckIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView mCheckIcon;
        TextView mShopName;
    }
}
