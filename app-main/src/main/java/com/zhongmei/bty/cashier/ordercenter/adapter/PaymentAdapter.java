package com.zhongmei.bty.cashier.ordercenter.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.context.util.Utils;

public class PaymentAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private List<PaymentGroup> paymentGroups;

    private String[] paymentModeName;

    public PaymentAdapter(Context context, List<PaymentGroup> paymentGroups) {
        super();
        this.mContext = context;
        this.paymentGroups = paymentGroups;
        paymentModeName = mContext.getResources().getStringArray(R.array.trade_payment_mode);
    }

    private class ChildViewHolder {
        ImageView im;

        TextView cashNameTv, cashStateTv, cashAmountTv;

    }

    @Override
    public PaymentItem getChild(int groupPosition, int childPosition) {
        return paymentGroups.get(groupPosition).getPaymentItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(mContext).inflate(R.layout.order_center_refund_pay_lv_item, parent, false);
            holder = new ChildViewHolder();
            holder.im = (ImageView) convertView.findViewById(R.id.iv_cash_icon);
            holder.cashNameTv = (TextView) convertView.findViewById(R.id.tv_cashName);
            holder.cashStateTv = (TextView) convertView.findViewById(R.id.tv_cashstate);
            holder.cashAmountTv = (TextView) convertView.findViewById(R.id.tv_cash_amount);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        PaymentItem paymentItem = getChild(groupPosition, childPosition);
        if (paymentItem != null) {
            String payModeName = getPayModeName(paymentItem);
            holder.cashNameTv.setText(payModeName);

            switch (Integer.valueOf(String.valueOf(paymentItem.getPayModeId()))) {
                case -1:
                    // -1:会员卡余额
                    holder.im.setImageResource(R.drawable.pay_member_icon_new);
                    break;
                case -2:
                    // 优惠券
                    holder.im.setImageResource(R.drawable.pay_coupons_icon_new);
                    break;
                case -3:
                    // 现金
                    holder.im.setImageResource(R.drawable.pay_cash_icon_new);
                    break;
                case -4:
                    // 银行卡
                    holder.im.setImageResource(R.drawable.pay_union_icon_new);
                    break;
                case -5:
                    // 微信支付
                    holder.im.setImageResource(R.drawable.pay_wechat_icon_new);
                    break;
                case -6:
                    // 支付宝
                    holder.im.setImageResource(R.drawable.pay_alipay_icon_new);
                    break;
                case -7:
                    // 百度钱包
                    holder.im.setImageResource(R.drawable.pay_baiduqianbao_icon_new);
                    break;
                case -8:
                    // 百度直达号
                    holder.im.setImageResource(R.drawable.pay_baiduzhida_icon_new);
                    break;
                case -9:
                    // 积分抵现
                    holder.im.setImageResource(R.drawable.pay_integral_icon_new);
                    break;
                default:
                    // 其他
                    holder.im.setImageResource(R.drawable.pay_other_icon_new);
                    break;
            }

            switch (paymentItem.getPayStatus()) {
                case PAID:
                    holder.cashStateTv.setText(R.string.record_payed);

                    break;
                case UNPAID:

                    holder.cashStateTv.setText(R.string.pay_error);
                    break;

                default:
                    break;
            }
            holder.cashAmountTv.setText(Utils.formatPrice(paymentItem.getUsefulAmount().doubleValue()));
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPostion) {
        return paymentGroups.get(groupPostion).getPaymentItems().size();
    }

    @Override
    public PaymentGroup getGroup(int groupPostion) {
        return paymentGroups.get(groupPostion);
    }

    @Override
    public int getGroupCount() {
        return paymentGroups.size();
    }

    @Override
    public long getGroupId(int position) {
        return position;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        PaymentGroup paymentGroup = paymentGroups.get(groupPosition);
        TextView textView = new TextView(mContext);
        if (!TextUtils.isEmpty(paymentGroup.getMemo())) {
            textView.setText(paymentGroup.getName() + "（" + paymentGroup.getMemo() + "）");
        } else {
            textView.setText(paymentGroup.getName());
        }
        textView.setTextSize(24);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textView.setPadding(10, 0, 0, 0);
        textView.setLayoutParams(lp);

        isExpanded = true;

        return textView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return false;
    }

    private String getPayModeName(PaymentItem paymentItem) {
        if (TextUtils.isEmpty(paymentItem.getPayModeName())) {
            StringBuffer sb = new StringBuffer();

            switch ((int) (paymentItem.getPayModeId() + 0)) {
                case -1:
                    // -1:会员卡余额
                    sb.append(paymentModeName[0] + ",");
                    break;
                case -2:
                    // 优惠券
                    sb.append(paymentModeName[1] + ",");
                    break;
                case -3:
                    // 现金
                    sb.append(paymentModeName[2] + ",");
                    break;
                case -4:
                    // 银行卡
                    sb.append(paymentModeName[3] + ",");
                    break;
                case -5:
                    // 微信支付
                    sb.append(paymentModeName[4] + ",");
                    break;
                case -6:
                    // 支付宝
                    sb.append(paymentModeName[5] + ",");
                    break;
                case -7:
                    // 百度钱包
                    sb.append(paymentModeName[6] + ",");
                    break;
                case -8:
                    // 百度直达号
                    sb.append(paymentModeName[7] + ",");
                    break;
                case -9:
                    // 积分抵现
                    sb.append(paymentModeName[8] + ",");
                    break;
                case -10:
                    // 百度地图
                    sb.append(paymentModeName[9] + ",");
                    break;
                case -11:
                    // 银联POS刷卡
                    sb.append(paymentModeName[10] + ",");
                    break;
                case -12:
                    //百糯到店付
                    sb.append(paymentModeName[11] + ",");
                    break;
                case -13:
                    //百度外卖
                    sb.append(paymentModeName[12] + ",");
                    break;
                default:
                    // 其他
                    sb.append(paymentModeName[13] + ",");
                    break;
            }

            if (sb.toString().endsWith(",")) {
                sb.deleteCharAt(sb.length() - 1);
            }

            return sb.toString();
        } else {
            return paymentItem.getPayModeName();
        }
    }

    public final static class PaymentGroup {

        private String name;

        private String memo;

        private List<PaymentItem> paymentItems;

        public PaymentGroup(String name, String memo, List<PaymentItem> paymentItems) {
            this.name = name;
            this.memo = memo;
            this.paymentItems = paymentItems;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public List<PaymentItem> getPaymentItems() {
            return paymentItems;
        }

        public void setPaymentItems(List<PaymentItem> paymentItems) {
            this.paymentItems = paymentItems;
        }

    }

}