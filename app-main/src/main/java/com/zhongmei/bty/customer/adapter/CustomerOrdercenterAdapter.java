package com.zhongmei.bty.customer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.customer.bean.CustomerOrderBean;
import com.zhongmei.bty.basemodule.customer.bean.CustomerSellOrderBean;
import com.zhongmei.bty.basemodule.devices.mispos.enums.AccountStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment;

import java.util.List;

/**
 * @Date：2016年2月18日
 * @Description:售卡记录或储值记录列表adapter
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class CustomerOrdercenterAdapter extends BaseAdapter {
    private Context mContext;
    private CustomerOrdercenterFragment.WindowToken token;
    private CustomerOrdercenterFragment.OrderCategory orderCategory;
    private List<CustomerOrderBean> customerSellOrderList;

    private int choosePosition = 0;

    public CustomerOrdercenterAdapter(Context context, CustomerOrdercenterFragment.WindowToken token) {
        mContext = context;
        this.token = token;
    }

    @Override
    public int getCount() {
        if (customerSellOrderList == null)
            return 0;
        return customerSellOrderList.size();
    }

    @Override
    public CustomerOrderBean getItem(int position) {
        if (customerSellOrderList != null && customerSellOrderList.size() != 0) {
            return customerSellOrderList.get(position);
        } else {
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.customer_ordercenter_listitem, null);
            viewHolder = new ViewHolder();
            viewHolder.orderTimeTv = (TextView) convertView.findViewById(R.id.order_time_tv);
            viewHolder.cardNumberTv = (TextView) convertView.findViewById(R.id.card_number_tv);
            viewHolder.ivCardType = (TextView) convertView.findViewById(R.id.iv_card_type);
            viewHolder.sellPriceTv = (TextView) convertView.findViewById(R.id.sell_price_tv);
            viewHolder.operaterTv = (TextView) convertView.findViewById(R.id.operater_tv);
            viewHolder.deviceNumberTv = (TextView) convertView.findViewById(R.id.device_number_tv);
            viewHolder.cardOrderStatusTv = (TextView) convertView.findViewById(R.id.card_order_status_tv);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        bindData(viewHolder, customerSellOrderList.get(position), position);
        if (position == this.choosePosition) {
            convertView.setBackgroundColor(Color.parseColor("#f2f5f7"));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    private void bindData(ViewHolder viewHolder, final CustomerOrderBean customerSellOrderBean, final int position) {
        viewHolder.orderTimeTv.setText(customerSellOrderBean.getSellTime());
        viewHolder.cardNumberTv.setText(TextUtils.isEmpty(customerSellOrderBean.getCardNo()) ? customerSellOrderBean.getMobile()
                : customerSellOrderBean.getCardNo());
        viewHolder.sellPriceTv.setText(customerSellOrderBean.getSellMoney());
        viewHolder.operaterTv.setText(customerSellOrderBean.getOperater());
        viewHolder.deviceNumberTv.setText(customerSellOrderBean.getDeviceNo());
        if (isShowOrderStatus()) {
            viewHolder.cardOrderStatusTv.setVisibility(View.VISIBLE);
            setOrderStatusText(viewHolder, customerSellOrderBean);
        } else {
            viewHolder.cardOrderStatusTv.setVisibility(View.GONE);
        }

        //实体卡类型 1:会员实体卡 2:匿名实体卡
        EntityCardType cardType = customerSellOrderBean.getCardType();
        if (cardType != null) {
            if (cardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
//				viewHolder.ivCardType.setImageResource(R.drawable.ic_entity_card_type_anonymous);
                viewHolder.ivCardType.setText(R.string.customer_entity_card_type_anonymous);
                viewHolder.ivCardType.setVisibility(View.VISIBLE);
            } else if (cardType == EntityCardType.CUSTOMER_ENTITY_CARD) {
//				viewHolder.ivCardType.setImageResource(R.drawable.ic_entity_card_type_customer);
                viewHolder.ivCardType.setText(R.string.customer_entity_card_type_customer);
                viewHolder.ivCardType.setVisibility(View.VISIBLE);
            } else if (cardType == EntityCardType.GENERAL_CUSTOMER_CARD) {
//				viewHolder.ivCardType.setImageResource(R.drawable.ic_entity_card_type_general);
                viewHolder.ivCardType.setText(R.string.customer_entity_card_type_general);
                viewHolder.ivCardType.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivCardType.setVisibility(View.GONE);
            }
        } else {
            viewHolder.ivCardType.setVisibility(View.GONE);
        }
    }

    private void setOrderStatusText(ViewHolder viewHolder, CustomerOrderBean customerSellOrderBean) {
        AccountStatus accountStatus = customerSellOrderBean.getAccountStatus();
        TradePayStatus tradePayStatus = customerSellOrderBean.getTradePayStatus();
        viewHolder.cardOrderStatusTv.setText("");
        switch (orderCategory) {
            case PAYED:
            case RETURNED:
                if (accountStatus != null && accountStatus.getTypeDescResId() > 0) {
                    viewHolder.cardOrderStatusTv.setText(accountStatus.getTypeDescResId());
                }
                break;
            case NOT_PAYED:
            case INVALID:
                if (tradePayStatus != null) {
                    int tradePayStatusResID = getTradePayStatusResID(tradePayStatus);
                    if (tradePayStatusResID > 0) {
                        viewHolder.cardOrderStatusTv.setText(tradePayStatusResID);
                    }
                }
                break;
        }
    }

    private int getTradePayStatusResID(TradePayStatus tradePayStatus) {
        switch (tradePayStatus) {
            case UNPAID:
                return R.string.pay_is_pay_unpay;
            case PAYING:
                return R.string.sales_paying;
            case PAID:
                return R.string.pay_success;
            case PAID_FAIL:
            case PAID_ERROR:
                return R.string.pay_error;
            default:
                return 0;
        }
    }

    public boolean isShowOrderStatus() {
        /*return token == CustomerOrdercenterFragment.WindowToken.MEMBER_STORE_VALUE
                || token == CustomerOrdercenterFragment.WindowToken.CARD_STORE_VALUE;*/
        return false;
    }

    public void resetData(CustomerOrdercenterFragment.WindowToken token, CustomerOrdercenterFragment.OrderCategory orderCategory, List<CustomerOrderBean> customerSellOrderBeans) {
        this.token = token;
        this.orderCategory = orderCategory;
        this.customerSellOrderList = customerSellOrderBeans;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView orderTimeTv;

        TextView cardNumberTv;

        TextView ivCardType;

        TextView sellPriceTv;

        TextView operaterTv;

        TextView deviceNumberTv;
        TextView cardOrderStatusTv;
    }

    public void setChooseItem(int position) {
        this.choosePosition = position;
        notifyDataSetChanged();
    }

}
