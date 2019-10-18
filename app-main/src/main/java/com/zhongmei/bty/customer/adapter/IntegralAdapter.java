package com.zhongmei.bty.customer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.customer.bean.IntegralRecord;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.customer.util.RecordType;
import com.zhongmei.yunfu.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class IntegralAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<IntegralRecord> mRecordList;
    private HashMap<String, String> mAllUser;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public IntegralAdapter(Context context, List<IntegralRecord> recordList, HashMap<String, String> allUser) {
        this.mContext = context;
        this.mRecordList = recordList;
        mInflater = LayoutInflater.from(mContext);
        this.mAllUser = allUser;
    }


    @Override
    public int getCount() {
        if (mRecordList != null) {
            return mRecordList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mRecordList != null) {
            return mRecordList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
                return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.customer_integral_item, parent, false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.person = (TextView) convertView.findViewById(R.id.person);
            viewHolder.tradeVal = (TextView) convertView.findViewById(R.id.trade_val);
            viewHolder.tradeReason = (TextView) convertView.findViewById(R.id.trade_reason);
            viewHolder.available = (TextView) convertView.findViewById(R.id.available);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mRecordList != null) {
            IntegralRecord record = mRecordList.get(position);
            viewHolder.time.setText(formatter.format(new Date(record.getModifyDateTime())));

            viewHolder.tradeReason.setVisibility(View.GONE);
            viewHolder.tradeReason.setText(mContext.getString(R.string.customer_integral_add_reason) + record.getReason());
            viewHolder.available.setText(record.getEndIntegral());
            viewHolder.person.setText(getPersonName(record.getUserId()));
            RecordType recordType = ValueEnums.toEnum(RecordType.class, record.getRecordType());
            switch (recordType) {
                case BUY:
                    viewHolder.tradeVal.setText(mContext.getString(R.string.customer_integral_recharging, record.getAddIntegral()));
                    break;
                case EXPENSE:
                    viewHolder.tradeVal.setText(mContext.getString(R.string.customer_integral_consume, record.getAddIntegral()));
                    break;
                case REFUND:
                    viewHolder.tradeVal.setText(mContext.getString(R.string.customer_integral_refund, record.getAddIntegral()));
                    break;
            }
        }
        return convertView;
    }

    private void setupTextCenter(TextView view) {
        view.setPadding(0, 0, 0, 0);
        view.setGravity(Gravity.CENTER);
    }

    class ViewHolder {
        TextView time, tradeVal, tradeReason, available, person;
    }

    private String getPersonName(String userId) {
        String personName = "";
        if (mAllUser != null) {
            personName = mAllUser.get(userId);
        }
        personName = TextUtils.isEmpty(personName) ? userId : personName;

        return personName;
    }

}
