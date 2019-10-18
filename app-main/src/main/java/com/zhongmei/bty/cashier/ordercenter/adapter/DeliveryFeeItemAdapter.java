package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.enums.SourceId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class DeliveryFeeItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<TradeAndDeliveryFee> tradeAndDeliveryFees = new ArrayList<>();

    public DeliveryFeeItemAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<TradeAndDeliveryFee> data) {
        tradeAndDeliveryFees.clear();
        if (data != null) {
            tradeAndDeliveryFees.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tradeAndDeliveryFees.size();
    }

    @Override
    public Object getItem(int position) {
        return tradeAndDeliveryFees.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_center_delivery_fee_item, null);
            holder.sourceIcon = (ImageView) convertView.findViewById(R.id.source_icon);
            holder.serialNo = (TextView) convertView.findViewById(R.id.serial_number);
            holder.tradeNo = (TextView) convertView.findViewById(R.id.trade_number);
            holder.deliveryFee = (TextView) convertView.findViewById(R.id.delivery_fee);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setSourceIcon(holder.sourceIcon, tradeAndDeliveryFees.get(position).sourceId);
        holder.serialNo.setText(mContext.getString(R.string.dinner_order_center_list_serial_number, tradeAndDeliveryFees.get(position).serialNo));
        holder.tradeNo.setText(mContext.getString(R.string.order_number_st, tradeAndDeliveryFees.get(position).tradeNo));
        holder.deliveryFee.setText(mContext.getString(R.string.order_center_currency_unit) + tradeAndDeliveryFees.get(position).fee);
        return convertView;
    }

    class ViewHolder {
        ImageView sourceIcon;
        TextView serialNo;
        TextView tradeNo;
        TextView deliveryFee;
    }


    private void setSourceIcon(ImageView icon, SourceId sourceId) {
        if (sourceId != null) {
            if (sourceId == SourceId.BAIDU_RICE) {
                icon.setImageResource(R.drawable.baidu_rice_icon);
            } else if (sourceId == SourceId.DIANPING) {
                icon.setImageResource(R.drawable.dianping_icon);
            } else if (sourceId == SourceId.XIN_MEI_DA) {
                icon.setImageResource(R.drawable.xinmeida_icon);
            } else if (sourceId == SourceId.ELEME) {
                icon.setImageResource(R.drawable.eleme_icon);
            } else if (sourceId == SourceId.MEITUAN_TAKEOUT) {
                icon.setImageResource(R.drawable.meituan_icon);
            } else if (sourceId == SourceId.WECHAT) {
                icon.setImageResource(R.drawable.wechat_icon);
            } else if (sourceId == SourceId.POS) {
                icon.setImageResource(R.drawable.local_pos_icon);
            } else if (sourceId == SourceId.BAIDU_TAKEOUT) {
                icon.setImageResource(R.drawable.baidu_takeout_icon);
            } else if (sourceId == SourceId.BAIDU_ZHIDA) {
                icon.setImageResource(R.drawable.baidu_zhida_icon);
            } else if (sourceId == SourceId.BAIDU_MAP) {
                icon.setImageResource(R.drawable.baidu_map_icon);
            } else if (sourceId == SourceId.KIOSK || sourceId == SourceId.KIOSK_ANDROID) {
                icon.setImageResource(R.drawable.kioak_icon);
            } else if (sourceId == SourceId.MERCHANT_HOME) {
                icon.setImageResource(R.drawable.businesshome_icon);
            } else if (sourceId == SourceId.CALL_CENTER) {
                icon.setImageResource(R.drawable.callcenter_icon);
            } else if (sourceId == SourceId.ON_MOBILE) {
                icon.setImageResource(R.drawable.onmobile_icon);
            } else if (sourceId == SourceId.FAMILIAR) {
                icon.setImageResource(R.drawable.familiar_icon);
            } else if (sourceId == SourceId.OPEN_PLATFORM) {
                icon.setImageResource(R.drawable.ic_order_center_list_open_platform);
            } else {
                icon.setImageResource(R.drawable.defalut_source_icon);
            }
        } else {
            icon.setImageResource(R.drawable.defalut_source_icon);
        }
    }

    public static class TradeAndDeliveryFee {
        public SourceId sourceId;        public String serialNo;        public String tradeNo;                public BigDecimal fee;
    }
}
