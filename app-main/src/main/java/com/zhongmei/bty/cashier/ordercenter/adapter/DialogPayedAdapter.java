package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;

public class DialogPayedAdapter extends BaseAdapter {
    private static final int DISALLOWA_UNION_CARD = 0;
    private static final int DISALLOWA_QR_CODE = 1;
    private Context mContext;
    private List<PaymentItem> paymentData;
    private String[] paymentModeName;
    private int mType = -1;

    public DialogPayedAdapter(Context context, int type, List<PaymentItem> data) {
        super();
        mContext = context;
        paymentData = data;
        paymentModeName = mContext.getResources().getStringArray(R.array.trade_payment_mode);
        mType = type;//主要用于无单退货
    }

    @Override
    public int getCount() {
        return paymentData.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // order_center_refund_pay_lv_item_dialog
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_center_refund_pay_lv_item_dialog, parent,
                    false);
            holder = new ViewHolder();
            //holder.im = (ImageView) convertView.findViewById(R.id.iv_cash_icon);
            holder.cashNameTv = (TextView) convertView.findViewById(R.id.tv_cashName);
            holder.cashStateTv = (TextView) convertView.findViewById(R.id.tv_cashstate);
            holder.cashAmountTv = (TextView) convertView.findViewById(R.id.tv_cash_amount);
            holder.warningTv = (TextView) convertView.findViewById(R.id.warning);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PaymentItem paymentItem = (PaymentItem) getItem(position);
        if (paymentItem != null) {
            String payModeName = getPayModeName(paymentItem);
            holder.cashNameTv.setText(payModeName);

//			switch (Integer.valueOf(String.valueOf(paymentItem.getPayModeId()))) {
//			case -1:
//				// -1:会员卡余额
//				holder.im.setImageResource(R.drawable.pay_member_icon_new);
//				break;
//			case -2:
//				// 优惠券
//				holder.im.setImageResource(R.drawable.pay_coupons_icon_new);
//				break;
//			case -3:
//				// 现金
//				holder.im.setImageResource(R.drawable.pay_cash_icon_new);
//				break;
//			case -4:
//				// 银行卡
//				holder.im.setImageResource(R.drawable.pay_union_icon_new);
//				setWarningDisplay(holder, DISALLOWA_UNION_CARD);
//				break;
//			case -5:
//				// 微信支付
//				holder.im.setImageResource(R.drawable.pay_wechat_icon_new);
//				setWarningDisplay(holder, DISALLOWA_QR_CODE);
//				break;
//			case -6:
//				// 支付宝
//				holder.im.setImageResource(R.drawable.pay_alipay_icon_new);
//				setWarningDisplay(holder, DISALLOWA_QR_CODE);
//				break;
//			case -7:
//				// 百度钱包
//				holder.im.setImageResource(R.drawable.pay_baiduqianbao_icon_new);
//				setWarningDisplay(holder, DISALLOWA_QR_CODE);
//				break;
//			case -8:
//				// 百度直达号
//				holder.im.setImageResource(R.drawable.pay_baiduzhida_icon_new);
//				break;
//			case -9:
//				// 积分抵现
//				holder.im.setImageResource(R.drawable.pay_integral_icon_new);
//				break;
//			default:
//				// 其他
//				holder.im.setImageResource(R.drawable.pay_other_icon_new);
//				break;
//			}

            //holder.im.setVisibility(View.GONE);
            if (mType != 1) {
                switch (paymentItem.getPayStatus()) {
                    case PAID:
                        holder.cashStateTv.setText(R.string.record_payed);
                        break;
                    case REFUNDED:
                        holder.cashStateTv.setText(R.string.refund_done);
                        break;
                    case UNPAID:
                    case PAID_ERROR:
                        holder.cashStateTv.setText(R.string.display_pay_fail);
                        break;

                    default:
                        break;
                }
            }
            holder.cashAmountTv.setText(Utils.formatPrice(paymentItem.getUsefulAmount().doubleValue()));
        }
        if (mType == 1) {//假设无单退货类型为1
            holder.cashStateTv.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void setWarningDisplay(ViewHolder holder, int type) {
        holder.warningTv.setVisibility(View.VISIBLE);
        switch (type) {
            case DISALLOWA_UNION_CARD:
                holder.warningTv.setText(R.string.order_center_fragment_dialog_pay_disallow_card);
                break;
            case DISALLOWA_QR_CODE:
                holder.warningTv.setText(R.string.order_center_fragment_dialog_pay_disallow_code);
                break;
            default:
                break;
        }
    }

    private class ViewHolder {
        //ImageView im;

        TextView cashNameTv, cashStateTv, cashAmountTv, warningTv;

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
}
