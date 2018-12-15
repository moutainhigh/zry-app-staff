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
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 次卡消费记录
 */
@SuppressLint("SimpleDateFormat")
public class CustomerCardTimeRecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<BeautyCardServiceAccount> mCouponList;

    public CustomerCardTimeRecordAdapter(Context context, List<BeautyCardServiceAccount> couponList) {
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
            viewHolder.all_num = (TextView) convertView.findViewById(R.id.tv_all_num);
            viewHolder.usable_num = (TextView) convertView.findViewById(R.id.tv_usable_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mCouponList != null) {
            BeautyCardServiceAccount item = mCouponList.get(position);
            viewHolder.name.setText(item.serviceName);
            viewHolder.all_num.setText(mContext.getString(R.string.beauty_card_service_all_count, item.serviceTotalTime));
            viewHolder.usable_num.setText(mContext.getString(R.string.beauty_card_service_surplus_count, item.serviceRemainderTime));
        }


        return convertView;
    }

    class ViewHolder {
        View vwLabel;
        TextView name, all_num, usable_num, discount, timeLimit, cashprice;
        LinearLayout discountLayout;
    }

}
