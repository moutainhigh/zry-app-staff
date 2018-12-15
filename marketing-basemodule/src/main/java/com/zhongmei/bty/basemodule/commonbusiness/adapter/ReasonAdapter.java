package com.zhongmei.bty.basemodule.commonbusiness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;

import java.util.ArrayList;
import java.util.List;

public class ReasonAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReasonSetting> data = new ArrayList<ReasonSetting>();
    private int current_checked_item = 0;

    public ReasonAdapter(Context context) {
        mContext = context;
    }

    public ReasonAdapter(Context context, List<ReasonSetting> inputData) {
        mContext = context;
        setData(inputData);
    }

    public void setData(List<ReasonSetting> inputData) {
        if (inputData != null && inputData.size() > 0) {
            data.clear();
            data.addAll(inputData);
            this.notifyDataSetChanged();
        }
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
            convertView = inflater.inflate(R.layout.reason_lv_item_dialog, null);
            holder = new ViewHolder((ImageView) convertView.findViewById(R.id.check_icon),
                    (TextView) convertView.findViewById(R.id.reason));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.reason.setText(data.get(position).getContent());
        if (current_checked_item == position) {
            holder.imageView.setBackgroundResource(R.drawable.print_checkbox_selected_new);
            holder.reason.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        } else {
            holder.imageView.setBackgroundResource(R.drawable.print_checkbox_default);
            holder.reason.setTextColor(mContext.getResources().getColor(R.color.color_333333));
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
