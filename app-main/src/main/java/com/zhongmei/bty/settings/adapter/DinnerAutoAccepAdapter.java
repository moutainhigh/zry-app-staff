package com.zhongmei.bty.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSettingItem;

import java.util.ArrayList;
import java.util.List;


public class DinnerAutoAccepAdapter extends BaseAdapter {
    private Context context;

    private List<TradeDealSettingItem> mDataSet;

    public DinnerAutoAccepAdapter(Context context) {
        this.context = context;
        mDataSet = new ArrayList<TradeDealSettingItem>();
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dinner_auto_accept_item, parent, false);
            viewHolder.showTime = (TextView) convertView.findViewById(R.id.time);
            viewHolder.showSingles = (TextView) convertView.findViewById(R.id.singles);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bindView(viewHolder, position);
        return convertView;
    }

    public void bindView(final ViewHolder viewHolder, int position) {
        TradeDealSettingItem tradeDealSettingItem = (TradeDealSettingItem) getItem(position);
        viewHolder.showTime.setText(context.getString(R.string.time_frame_msg, tradeDealSettingItem.getStartTime(), tradeDealSettingItem.getEndTime()));
        viewHolder.showSingles.setText(context.getString(R.string.order_number_msg, tradeDealSettingItem.getOrderNum()));
    }

    public void setDataSet(List<TradeDealSettingItem> dataSet) {
        mDataSet.clear();
        if (dataSet != null) {
            mDataSet.addAll(dataSet);
        }
        notifyDataSetChanged();
    }

    public final class ViewHolder {

        TextView showTime;

        TextView showSingles;
    }
}
