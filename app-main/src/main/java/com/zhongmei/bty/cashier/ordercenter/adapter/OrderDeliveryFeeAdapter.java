package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.trade.entity.DeliveryOrderRecord;

import java.util.ArrayList;
import java.util.List;

public class OrderDeliveryFeeAdapter extends BaseAdapter {
    private Context mContext;
    private List<DeliveryOrderRecord> data = new ArrayList<DeliveryOrderRecord>();

    public OrderDeliveryFeeAdapter(Context context, List<DeliveryOrderRecord> data) {
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
            convertView = inflater.inflate(R.layout.adapter_order_delivery_fee, null);
            holder = new ViewHolder((TextView) convertView.findViewById(R.id.tv_fee));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvFee.setText(data.get(position).getMemo());

        return convertView;
    }

    private class ViewHolder {
        public TextView tvFee;

        public ViewHolder(TextView tvFee) {
            this.tvFee = tvFee;
        }
    }
}
