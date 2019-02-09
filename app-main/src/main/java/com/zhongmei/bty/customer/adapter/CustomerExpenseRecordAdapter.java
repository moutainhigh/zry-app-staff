package com.zhongmei.bty.customer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.customer.bean.CustomerExpenseRecordResp;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.ui.H5WebViewActivity;

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
            convertView = mInflater.inflate(R.layout.customer_expense_record_item, parent, false);
            viewHolder.itemView = (LinearLayout) convertView.findViewById(R.id.itemView);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.tradeType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.tradeNo = (TextView) convertView.findViewById(R.id.other_balance);
            viewHolder.tradeValue = (TextView) convertView.findViewById(R.id.end_value);
            viewHolder.person = (TextView) convertView.findViewById(R.id.person);
            viewHolder.viewDetails = (LinearLayout)convertView.findViewById(R.id.view_details);
            viewHolder.typeIcon = (ImageView) convertView.findViewById(R.id.typeIcon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mRecordList != null) {

            if(position % 2 == 1){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#F1F1F1"));
            }

            final CustomerExpenseRecordResp record = mRecordList.get(position);
            viewHolder.time.setText(formatter.format(new Date(record.getModifyDateTime())));
            viewHolder.tradeType.setText(mContext.getResources().getString(R.string.order_type_lable)+record.getTradeType());
            viewHolder.tradeNo.setText(record.getTradeNo());
            viewHolder.tradeValue.setText(mContext.getResources().getString(R.string.order_amount_lable)+record.getTradeValue().toString());
            viewHolder.person.setText(getPersonName(record.getUserId()));

            //交易类型 1:SELL:售货 2:REFUND:退货 3:REPAY:反结账 4:REPAY_FOR_REFUND:反结账退货
            if(record.getTradeType().equals("售货")){
                viewHolder.typeIcon.setBackground(mContext.getResources().getDrawable(R.drawable.customer_sales_icon));
            }else if(record.getTradeType().equals("退货")){
                viewHolder.typeIcon.setBackground(mContext.getResources().getDrawable(R.drawable.customer_return_icon));
            }


            viewHolder.viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     //http://mk.zhongmeiyunfu.com/marketing/internal/trade/tradeDetail?tradeId=1001&brandIdenty=1&shopIdenty=1
                    Long brandId = ShopInfoManager.getInstance().getShopInfo().getBrandId();
                    Long shopId = ShopInfoManager.getInstance().getShopInfo().getShopId();
                    String url = String.format("http://mk.zhongmeiyunfu.com/marketing/internal/trade/tradeDetail?source=1&tradeId=%d&brandIdenty=%d&shopIdenty=%d", record.getTradeId(), brandId, shopId);
                    H5WebViewActivity.start(mContext, url);
                }
            });
        }
        return convertView;
    }

    private void setupTextCenter(TextView view) {
        view.setPadding(0, 0, 0, 0);
        view.setGravity(Gravity.CENTER);
    }

    class ViewHolder {
        TextView time, tradeType, tradeNo, tradeValue, person;
        LinearLayout itemView,viewDetails;
        ImageView typeIcon;
    }

    private String getPersonName(String userId) {
        String personName = "";
        personName = TextUtils.isEmpty(personName) ? userId : personName;

        return personName;
    }

}
