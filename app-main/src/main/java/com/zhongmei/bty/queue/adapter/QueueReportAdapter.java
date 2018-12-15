package com.zhongmei.bty.queue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.MathDecimal;

import java.util.ArrayList;
import java.util.List;

public class QueueReportAdapter extends BaseAdapter {

    private Context mContext;

    public List<QueueReportDataItem> datas = new ArrayList<>();

    public QueueReportAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (datas == null) {
            return 0;
        }

        return datas.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public QueueReportDataItem getItem(int position) {
        if (datas == null) {
            return null;
        }

        return datas.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.queue_reportcenter_loss_rate_listitem, null);
            viewHolder = new ViewHolder();
            viewHolder.tvQueueName = (TextView) convertView.findViewById(R.id.tv_queue_name);
            viewHolder.tvDeskCount = (TextView) convertView.findViewById(R.id.tv_desk_count);
            viewHolder.tvQueueCount = (TextView) convertView.findViewById(R.id.tv_queue_count);
            viewHolder.tvMealCount = (TextView) convertView.findViewById(R.id.tv_meal_count);
            viewHolder.tvOutCount = (TextView) convertView.findViewById(R.id.tv_out_count);
            viewHolder.tvLossRate = (TextView) convertView.findViewById(R.id.tv_loss_rate);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        QueueReportDataItem item = getItem(position);
        if (item != null) {
            viewHolder.tvQueueName.setText(item.queueName);
            viewHolder.tvDeskCount.setText(item.deskCount + "");
            viewHolder.tvQueueCount.setText(item.queueCount + "");
            viewHolder.tvMealCount.setText(item.mealCount + "");
            viewHolder.tvOutCount.setText(item.outCount + "");
            viewHolder.tvLossRate.setText(MathDecimal.round(item.lossRate * 100, 2) + "%");
        }

        return convertView;
    }

    public static class QueueReportDataItem {

        public String queueName;

        public int deskCount;

        public int queueCount;

        public int mealCount;

        public int outCount;

        public double lossRate;

        public Long queueLineId;
    }

    private class ViewHolder {

        TextView tvQueueName;

        TextView tvDeskCount;

        TextView tvQueueCount;

        TextView tvMealCount;

        TextView tvOutCount;

        TextView tvLossRate;
    }
}
