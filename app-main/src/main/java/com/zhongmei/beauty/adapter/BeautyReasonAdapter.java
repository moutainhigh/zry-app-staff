package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;

import java.util.ArrayList;
import java.util.List;

public class BeautyReasonAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReasonSetting> data = new ArrayList<ReasonSetting>();
    private int current_checked_item = 0;

    public BeautyReasonAdapter(Context context, List<ReasonSetting> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.beauty_order_center_reason_lv_item_dialog, null);
            holder = new ViewHolder((ImageView) convertView.findViewById(R.id.check_icon),
                    (TextView) convertView.findViewById(R.id.reason));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.reason.setText(data.get(position).getContent());
        if (current_checked_item == position) {
            holder.imageView.setBackgroundResource(R.drawable.beauty_icon_cb_checked);
            holder.reason.setTextColor(mContext.getResources().getColor(R.color.reason_radio_color2));
        } else {
            holder.imageView.setBackgroundResource(R.drawable.beauty_icon_cb_nomal);
            holder.reason.setTextColor(mContext.getResources().getColor(R.color.reason_radio_color2));
        }
        return convertView;
    }

    public void setCurrentCheckedItem(int item) {
        current_checked_item = item;
        notifyDataSetChanged();
    }

    public int getCurrentCheckedItem() {
        return current_checked_item;
    }

    private class ViewHolder {
        public ImageView imageView;
        public TextView reason;

        public ViewHolder(ImageView imageView, TextView reason) {
            this.imageView = imageView;
            this.reason = reason;
        }
    }
}
