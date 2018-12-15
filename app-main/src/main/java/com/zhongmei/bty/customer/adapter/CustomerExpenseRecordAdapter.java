package com.zhongmei.bty.customer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.customer.bean.CustomerExpenseRecordResp;
import com.zhongmei.yunfu.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 消费记录
 */
public class CustomerExpenseRecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<CustomerExpenseRecordResp> mRecordList;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public CustomerExpenseRecordAdapter(Context context, List<CustomerExpenseRecordResp> recordList) {
        this.mContext = context;
        this.mRecordList = recordList;
        mInflater = LayoutInflater.from(mContext);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.customer_balance_item, parent, false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.tradeType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.tradeNo = (TextView) convertView.findViewById(R.id.other_balance);
            viewHolder.tradeValue = (TextView) convertView.findViewById(R.id.end_value);
            viewHolder.person = (TextView) convertView.findViewById(R.id.person);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mRecordList != null) {
            CustomerExpenseRecordResp record = mRecordList.get(position);
            viewHolder.time.setText(formatter.format(new Date(record.getModifyDateTime())));

            viewHolder.tradeType.setText(record.getTradeType());
            viewHolder.tradeNo.setText(record.getTradeNo());
            viewHolder.tradeValue.setText(record.getTradeValue().toString());
            viewHolder.person.setText(getPersonName(record.getUserId()));
        }
        return convertView;
    }

    private void setupTextCenter(TextView view) {
        view.setPadding(0, 0, 0, 0);
        view.setGravity(Gravity.CENTER);
    }

    class ViewHolder {
        TextView time, tradeType, tradeNo, tradeValue, person;
    }

    private String getPersonName(String userId) {
        String personName = "";
        personName = TextUtils.isEmpty(personName) ? userId : personName;

        return personName;
    }

}
