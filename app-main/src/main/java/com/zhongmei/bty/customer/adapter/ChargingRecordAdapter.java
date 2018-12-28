package com.zhongmei.bty.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.bean.req.CustomerStoredBalanceResp;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 消费记录Adapter
 */
@SuppressLint("SimpleDateFormat")
public class ChargingRecordAdapter extends BaseAdapter {

    /**
     * 储值
     */
    private static final int TYPE_CHARGING = 1;

    /**
     * 消费
     */
    private static final int TYPE_EXPENSE = 2;

    /**
     * 退款
     */
    private static final int TYPE_REFUND = 3;

    /**
     * 调账
     */
    private static final int TYPE_CAHNGE_BALANCE = 4;

    private LayoutInflater mInflater;

    private Context mContext;

    private List<CustomerStoredBalanceResp> mRecordList;

    private HashMap<String, String> mAllUser;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private OnRecordItemClickListener mOnRecordItemClickListener;

    public interface OnRecordItemClickListener {
        /**
         * 补打
         *
         * @param history  记录
         * @param authUser 经手人
         */
        void onRePrint(CustomerStoredBalanceResp history, String authUser);
    }

    public void setOnRecordItemClickListener(OnRecordItemClickListener listener) {
        mOnRecordItemClickListener = listener;
    }

    public ChargingRecordAdapter(Context context, List<CustomerStoredBalanceResp> recordList,
                                 HashMap<String, String> allUser) {
        this.mContext = context;
        this.mRecordList = recordList;
        mAllUser = allUser;
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
            viewHolder.person = (TextView) convertView.findViewById(R.id.person);
            viewHolder.balance = (TextView) convertView.findViewById(R.id.balance);
            viewHolder.give = (TextView) convertView.findViewById(R.id.give);
            viewHolder.endvalue = (TextView) convertView.findViewById(R.id.end_value);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.charingBalanceLayout = (RelativeLayout) convertView.findViewById(R.id.charging_balance_layout);
            viewHolder.otherBalance = (TextView) convertView.findViewById(R.id.other_balance);
            viewHolder.otherBalanceLayout = (LinearLayout) convertView.findViewById(R.id.other_balance_layout);
            viewHolder.balanceReprint = (Button) convertView.findViewById(R.id.customer_balance_reprint);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String userName;
        if (mRecordList != null) {
            final CustomerStoredBalanceResp chargingRecord = mRecordList.get(position);
            String createDateTime = formatter.format(new Date(chargingRecord.getCreateDateTime()));
            viewHolder.time.setText(createDateTime);
            userName = chargingRecord.getUserName();
            if (userName == null) {
                userName = getPersonName(chargingRecord.getUserId());
            }
            viewHolder.person.setText(userName);
            viewHolder.endvalue.setText(ShopInfoCfg.formatCurrencySymbol(chargingRecord.getEndValuecard()));
            switch (chargingRecord.getType()) {
                case TYPE_CHARGING: {
                    String recharging = mContext.getString(R.string.customer_account_list_recharging, chargingRecord.getAddValuecard());
                    if (chargingRecord.getSendValuecard() != null && chargingRecord.getSendValuecard().compareTo(BigDecimal.ZERO) > 0) {
                        String giving = mContext.getString(R.string.customer_account_list_giving, chargingRecord.getSendValuecard());
                        viewHolder.give.setVisibility(View.VISIBLE);
                        viewHolder.give.setText(giving);
                    } else {
                        viewHolder.give.setVisibility(View.GONE);
                    }
                    viewHolder.balance.setText(recharging);
                    //viewHolder.type.setVisibility(View.VISIBLE);
                    //viewHolder.type.setText(chargingRecord.getPaymentModeName());
                    viewHolder.otherBalanceLayout.setVisibility(View.GONE);
                    viewHolder.charingBalanceLayout.setVisibility(View.VISIBLE);
                    viewHolder.balanceReprint.setVisibility(View.INVISIBLE);
                }
                break;
                case TYPE_EXPENSE:
                    viewHolder.otherBalance.setText(String.format(mContext.getString(R.string.customer_account_list_consume), chargingRecord.getAddValuecard().abs()));
                    viewHolder.otherBalanceLayout.setVisibility(View.VISIBLE);
                    viewHolder.charingBalanceLayout.setVisibility(View.GONE);
                    //viewHolder.type.setVisibility(View.INVISIBLE);
                    //viewHolder.type.setText(chargingRecord.getPaymentModeName());
                    viewHolder.balanceReprint.setVisibility(View.INVISIBLE);
                    break;
                case TYPE_REFUND:
                    if (chargingRecord.getAddValuecard().doubleValue() > 0) {// 退款
                        viewHolder.otherBalance.setText(String.format(mContext.getString(R.string.customer_account_list_refund), chargingRecord.getAddValuecard()));
                        viewHolder.charingBalanceLayout.setVisibility(View.GONE);
                        viewHolder.otherBalanceLayout.setVisibility(View.VISIBLE);
                        //viewHolder.type.setVisibility(View.INVISIBLE);
                        //viewHolder.type.setText(chargingRecord.getPaymentModeName());
                    }
                    viewHolder.balanceReprint.setVisibility(View.INVISIBLE);
                    break;
                case TYPE_CAHNGE_BALANCE:
                    if (chargingRecord.getAddValuecard().doubleValue() < 0) {// 调账
                        String recharging = mContext.getString(R.string.customer_change_balance_value, chargingRecord.getAddValuecard());
                        String giving = mContext.getString(R.string.customer_account_list_giving, chargingRecord.getSendValuecard());
                        viewHolder.balance.setText(recharging);
                        viewHolder.give.setText(giving);
                        //viewHolder.type.setVisibility(View.INVISIBLE);
                        viewHolder.otherBalanceLayout.setVisibility(View.GONE);
                        viewHolder.charingBalanceLayout.setVisibility(View.VISIBLE);
                    }
                    viewHolder.balanceReprint.setVisibility(View.INVISIBLE);
                    break;
            }
            final String finalUserName = userName;
            viewHolder.balanceReprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecordItemClickListener != null) {
                        mOnRecordItemClickListener.onRePrint(chargingRecord, finalUserName);
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView date, time, person, balance, give, endvalue, otherBalance;

        TextView type;

        RelativeLayout charingBalanceLayout;

        LinearLayout otherBalanceLayout;
        Button balanceReprint;
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
