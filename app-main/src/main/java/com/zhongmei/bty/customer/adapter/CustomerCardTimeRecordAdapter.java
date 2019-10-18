package com.zhongmei.bty.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceAccount;
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceInfo;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;


@SuppressLint("SimpleDateFormat")
public class CustomerCardTimeRecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<BeautyCardServiceInfo> mCouponList;

    public CustomerCardTimeRecordAdapter(Context context, List<BeautyCardServiceInfo> couponList) {
        this.mContext = context;
        this.mCouponList = couponList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mCouponList != null) {
            return mCouponList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mCouponList != null) {
            return mCouponList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.customer_record_card_time_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
                        viewHolder.usable_num = (TextView) convertView.findViewById(R.id.tv_usable_num);
            viewHolder.tv_expired_date = (TextView) convertView.findViewById(R.id.tv_expired_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mCouponList != null) {
            BeautyCardServiceInfo item = mCouponList.get(position);
            viewHolder.name.setText(item.serviceName);
            if (item.serviceRemainderTime == -1 || item.serviceTotalTime == -1) {
                viewHolder.usable_num.setText(mContext.getString(R.string.beauty_card_service_surplus_count_unlimited));
            } else {
                viewHolder.usable_num.setText(mContext.getString(R.string.beauty_card_service_surplus_count, item.serviceRemainderTime));
            }

            viewHolder.tv_expired_date.setText(formatTime(item.cardExpireDate));
        }


        return convertView;
    }

    private String formatTime(Long endTime) {
        if (endTime == null) {
            return mContext.getString(R.string.beauty_card_time_never_expires);
        }
        String endTimeStr = DateTimeUtils.formatDateTime(endTime, DateTimeUtils.QUERY_DATE_FORMAT);
        return String.format(mContext.getResources().getString(R.string.beauty_order_card_time),endTimeStr);
    }

    class ViewHolder {
        TextView name, usable_num, tv_expired_date;
    }

}
