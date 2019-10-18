package com.zhongmei.bty.dinner.ordercenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;

import java.util.List;



public class DiscountTicketAdapter extends BaseAdapter {

    private Context mContext;
    private List<PaymentItemGroupon> mListPayMentItemGroupon;
    private LayoutInflater mInfalter;
    private ViewHolder mViewHolder;

    public DiscountTicketAdapter(Context context, List<PaymentItemGroupon> data) {
        this.mContext = context;
        this.mListPayMentItemGroupon = data;
        this.mInfalter = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        if (mListPayMentItemGroupon == null) {
            return 0;
        }
        return mListPayMentItemGroupon.size();
    }

    @Override
    public PaymentItemGroupon getItem(int position) {
        return mListPayMentItemGroupon.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInfalter.inflate(R.layout.item_lv_ordercenter_discountticket, null);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            mViewHolder.tv_ticketNo = (TextView) convertView.findViewById(R.id.tv_ticketNo);
            mViewHolder.tv_ticketName = (TextView) convertView.findViewById(R.id.tv_ticketName);
            mViewHolder.tv_ticketDenomination = (TextView) convertView.findViewById(R.id.tv_ticketDenomination);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        PaymentItemGroupon discountTicket = getItem(position);

        mViewHolder.tv_index.setText(String.valueOf(position + 1));
        mViewHolder.tv_ticketNo.setText(discountTicket.getUsedSerialNo());
        mViewHolder.tv_ticketName.setText(discountTicket.getDealTitle());
        mViewHolder.tv_ticketDenomination.setText(String.valueOf(discountTicket.getMarketPrice()));

        return convertView;
    }

    class ViewHolder {
        TextView tv_index;
        TextView tv_ticketNo;
        TextView tv_ticketName;
        TextView tv_ticketDenomination;
    }
}


