package com.zhongmei.bty.dinner.ordercenter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.entity.RefundExceptionReason;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.cashier.ordercenter.presenter.IOrderCenterDetailPresenter;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.ordercenter.adapter.DiscountTicketAdapter;
import com.zhongmei.bty.dinner.ordercenter.bean.PayModeDetailsBean;
import com.zhongmei.bty.dinner.table.view.SpreadListView;
import com.zhongmei.bty.snack.offline.Snack;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnLayout extends LinearLayout {
    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private IOrderCenterDetailPresenter presenter;

    private int mColumns = 1;// 列数

    private static Map<Long, Integer> exceptionCodeMap = new HashMap<>();//key是code，value是对应的textresid

    static {
        exceptionCodeMap.put(3107L, R.string.wallet_refund_fail_3107);
        exceptionCodeMap.put(3105L, R.string.wallet_refund_fail_3105);
        exceptionCodeMap.put(3106L, R.string.wallet_refund_fail_3106);
        exceptionCodeMap.put(2006L, R.string.wallet_refund_fail_2006);
        exceptionCodeMap.put(2007L, R.string.wallet_refund_fail_2007);
        exceptionCodeMap.put(2008L, R.string.wallet_refund_fail_2008);
        exceptionCodeMap.put(3302L, R.string.wallet_refund_fail_3302);
        exceptionCodeMap.put(3403L, R.string.wallet_refund_fail_3403);
        exceptionCodeMap.put(3404L, R.string.wallet_refund_fail_3404);
        exceptionCodeMap.put(3411L, R.string.wallet_refund_fail_3411);
        exceptionCodeMap.put(3412L, R.string.wallet_refund_fail_3412);
        exceptionCodeMap.put(3413L, R.string.wallet_refund_fail_3413);
        exceptionCodeMap.put(3414L, R.string.wallet_refund_fail_3414);
        exceptionCodeMap.put(3110L, R.string.wallet_refund_fail_3110);
        exceptionCodeMap.put(4505L, R.string.wallet_refund_fail_4505);
        exceptionCodeMap.put(4501L, R.string.wallet_refund_fail_4501);
        exceptionCodeMap.put(4521L, R.string.wallet_refund_fail_4521);
        exceptionCodeMap.put(4503L, R.string.wallet_refund_fail_4503);
        exceptionCodeMap.put(4522L, R.string.wallet_refund_fail_4522);
        exceptionCodeMap.put(4523L, R.string.wallet_refund_fail_4523);
        exceptionCodeMap.put(4520L, R.string.wallet_refund_fail_4520);
        exceptionCodeMap.put(4525L, R.string.wallet_refund_fail_4525);
        exceptionCodeMap.put(4010L, R.string.wallet_refund_fail_4010);
        exceptionCodeMap.put(4012L, R.string.wallet_refund_fail_4012);
        exceptionCodeMap.put(4103L, R.string.wallet_refund_fail_4103);
        exceptionCodeMap.put(4107L, R.string.wallet_refund_fail_4107);
        exceptionCodeMap.put(4122L, R.string.wallet_refund_fail_4122);
        exceptionCodeMap.put(4123L, R.string.wallet_refund_fail_4123);
        exceptionCodeMap.put(4124L, R.string.wallet_refund_fail_4124);
        exceptionCodeMap.put(4121L, R.string.wallet_refund_fail_4121);
        exceptionCodeMap.put(4120L, R.string.wallet_refund_fail_4120);
    }

    public void setPresenter(IOrderCenterDetailPresenter presenter) {
        this.presenter = presenter;
    }

    public ColumnLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColumnLayout);
        mColumns = a.getInt(R.styleable.ColumnLayout_column, 1);
        a.recycle();

        setOrientation(VERTICAL);
    }

    public void setData(List<String> dataSet) {
        removeAllViews();
        if (dataSet == null || dataSet.isEmpty()) {
            setVisibility(View.GONE);
            return;
        }

        setVisibility(View.VISIBLE);
        int size = dataSet.size();
        int rows = (size + mColumns - 1) / mColumns;
        for (int i = 0; i < rows; i++) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(HORIZONTAL);
            layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < mColumns; j++) {
                View child = mLayoutInflater.inflate(R.layout.column_layout_item, null);
                child.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
                child.setPadding(0, DensityUtil.dip2px(MainApplication.getInstance(), 12), 0, DensityUtil.dip2px(MainApplication.getInstance(), 12));
                layout.addView(child);

                // 设置数据
                int index = i * mColumns + j;
                if (index < size) {
                    String s = dataSet.get(index);
                    TextView textView = (TextView) child.findViewById(R.id.column_layout_item_text);
                    if (mColumns > 1 && j == mColumns - 1) {
                        textView.setGravity(Gravity.RIGHT);
                    }
                    textView.setText(s);
                }
            }
            addView(layout);
        }
    }

    public void setPaymentModes(final TradeVo tradeVo, List<PayModeDetailsBean> paymentItemGroupons) {
        setPaymentModes(tradeVo, paymentItemGroupons, false);
    }

    /**
     * 该方法提供填充团购劵详情,目前只有美团券在使用,后期可扩展其他券详情展示
     *
     * @param tradeVo
     * @param payModeDetailsBeanList
     * @param isRefundAmount         是否有可能金额 true有 false无退金额
     */
    public void setPaymentModes(final TradeVo tradeVo, List<PayModeDetailsBean> payModeDetailsBeanList, boolean isRefundAmount) {
        removeAllViews();
        if (payModeDetailsBeanList == null || payModeDetailsBeanList.isEmpty()) {
            setVisibility(View.GONE);
            return;
        }

        for (final PayModeDetailsBean payModeDetailsBean : payModeDetailsBeanList) {
            View view = mLayoutInflater.inflate(R.layout.layout_order_discountticket, null);
            TextView tv_nameAndFee = (TextView) view.findViewById(R.id.tv_nameAndFee);
            TextView tvPayStatus = (TextView) view.findViewById(R.id.tv_order_pay_status);
            //TextView tvExtraAmount = (TextView) view.findViewById(R.id.tv_extra_amount);
            TextView btnSalesRefund = (TextView) view.findViewById(R.id.btn_order_refund);
            TextView tv_foldDetails = (TextView) view.findViewById(R.id.tv_foldDetails);
            Button tv_refreshState = (Button) view.findViewById(R.id.tv_refreshState);
            TextView btnRefundFailedReason = (TextView) view.findViewById(R.id.tv_refund_failed_reason);
            final LinearLayout layout_ticketDetals = (LinearLayout) view.findViewById(R.id.layout_ticketDetails);
            final TextView tv_ticketTitle = (TextView) view.findViewById(R.id.tv_title);
            SpreadListView slv_discountTickets = (SpreadListView) view.findViewById(R.id.slv_discountTickets);


            StringBuilder sb = new StringBuilder();
            sb.append(formatPaymodeName(payModeDetailsBean.getPayModeName()));
            sb.append("：");
            sb.append(Utils.formatPrice(payModeDetailsBean.getPayModeTotalDenomination().doubleValue()));
            tv_nameAndFee.setText(sb.toString());
            final PaymentItem paymentItem = payModeDetailsBean.getPaymentItem();
            if (paymentItem.getPayStatus() != null) {
                switch (payModeDetailsBean.getPaymentItem().getPayStatus()) {
                    case PAID:
//                        tvPayStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pay_status_paid, 0);
                        tvPayStatus.setTextSize(DensityUtil.sp2px(mContext, 7));
                        tvPayStatus.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));
                        tvPayStatus.setBackgroundResource(R.drawable.lable_bg_gray_d4d4d4_2px_radius);
                        tvPayStatus.setText(R.string.record_payed);
                        break;
                    case REFUNDING:
//                        tvPayStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pay_status_refunding, 0);
                        tvPayStatus.setTextSize(DensityUtil.sp2px(mContext, 7));
                        tvPayStatus.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));
                        tvPayStatus.setBackgroundResource(R.drawable.lable_bg_orange_fdaf33_2px_radius);
                        tvPayStatus.setText(R.string.order_center_refunding);
                        break;
                    case REFUNDED:
//                        tvPayStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pay_status_refunded, 0);
                        if (PayModeId.EARNEST_DEDUCT.value().equals(paymentItem.getPayModeId())) {//预付金抵扣不退款
                            break;
                        }
                        tvPayStatus.setTextSize(DensityUtil.sp2px(mContext, 7));
                        tvPayStatus.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));
                        tvPayStatus.setBackgroundResource(R.drawable.lable_bg_green_4dd5b7_2px_radius);
                        tvPayStatus.setText(R.string.refund_done);
                        break;
                    default:
                        //tv_nameAndFee.append("  ");
//                        tvPayStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        String[] tradePayStatus = mContext.getResources().getStringArray(R.array.trade_pay_status);
                        String s = tradePayStatus[payModeDetailsBean.getPaymentItem().getPayStatus().value() - 1];
                        SpannableString spanString = new SpannableString(s);
                        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
                        /*if (payModeDetailsBean.getPaymentItem().getPayStatus() == TradePayStatus.PAID || payModeDetailsBean.getPaymentItem().getPayStatus() == TradePayStatus.REFUNDED) {
                            span = new ForegroundColorSpan(Color.parseColor("#555555"));
                        }*/
                        spanString.setSpan(span, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvPayStatus.setText(spanString);
                        break;
                }
            }

            btnSalesRefund.setVisibility(View.GONE);
            switch (PayModeId.toEnum(payModeDetailsBean.getPaymentItem().getPayModeId())) {
                case CASH:
                case WEIXIN_PAY:
                case ALIPAY:
                    if (isRefundAmount && isShowRefund(payModeDetailsBeanList, payModeDetailsBean)) {
                        btnSalesRefund.setVisibility(View.VISIBLE);
                        btnSalesRefund.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//下划线
                        btnSalesRefund.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Long paymentItemId = payModeDetailsBean.getPaymentItem().getId();
                                presenter.showRefundDialog(tradeVo.getTrade().getId(), paymentItemId, paymentItem.getUsefulAmount());
                            }
                        });
                    }

                    //显示退款失败原因
                    RefundExceptionReason refundExceptionReason = payModeDetailsBean.getNewestRefundExceptionReason();
                    if (paymentItem.getPayStatus() == TradePayStatus.REFUND_FAILED && refundExceptionReason != null
                            && refundExceptionReason.getExceptionCode() != null) {
                        btnRefundFailedReason.setVisibility(View.VISIBLE);
                        Integer textResId = exceptionCodeMap.get(refundExceptionReason.getExceptionCode());
                        if (textResId != null && textResId > 0) {
                            btnRefundFailedReason.setText(mContext.getString(R.string.failed_reason) + mContext.getString(textResId) + "（" + refundExceptionReason.getExceptionCode().toString() + "）");
                        } else {
                            btnRefundFailedReason.setText(mContext.getString(R.string.failed_reason_other) + "（" + refundExceptionReason.getExceptionCode().toString() + "）");
                        }
                    } else {
                        btnRefundFailedReason.setVisibility(View.INVISIBLE);
                    }
                    break;
                case BAINUO_TUANGOU:
                case MEITUAN_TUANGOU:
                    /*BigDecimal extraAmount = getItemExtraAmount(payModeDetailsBean);
                    if (extraAmount.doubleValue() > 0) {
                        tvExtraAmount.setText(mContext.getString(R.string.dinner_order_center_payinfo_extra,
                                Utils.formatPrice(extraAmount.doubleValue())));
                    }*/
                    break;
                default:
                    break;
            }

            //是否显示优惠劵
            if (payModeDetailsBean.getPayModeItems() != null && payModeDetailsBean.getPayModeItems().size() > 0) {
                tv_ticketTitle.setText(String.format(mContext.getString(R.string.dinner_ordercenter_ticket_info), payModeDetailsBean.getPayModeName()));
                DiscountTicketAdapter ticketAdapter = new DiscountTicketAdapter(mContext, payModeDetailsBean.getPayModeItems());
                slv_discountTickets.setAdapter(ticketAdapter);
                layout_ticketDetals.setVisibility(View.GONE);//默认关闭
                tv_foldDetails.setVisibility(View.VISIBLE);
                tv_foldDetails.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (((TextView) v).getText().toString().equals(mContext.getString(R.string.dinner_order_center_showinfo))) {

                            layout_ticketDetals.setVisibility(View.VISIBLE);
                            ((TextView) v).setText(mContext.getString(R.string.dinner_order_center_hidinfo));
                        } else {
                            layout_ticketDetals.setVisibility(View.GONE);
                            ((TextView) v).setText(mContext.getString(R.string.dinner_order_center_showinfo));
                        }
                    }
                });
            } else {
                layout_ticketDetals.setVisibility(View.GONE);
                tv_foldDetails.setVisibility(View.GONE);
            }

            //判断已使用第三方支付，退货。状态未返回，刷新状态值
//            if (payModeDetailsBean.getPaymentType() != null) {
                if ((paymentItem.getPayModeId() == PayModeId.WEIXIN_PAY.value()
                        || paymentItem.getPayModeId() == PayModeId.ALIPAY.value()
                        || paymentItem.getPayModeId() == PayModeId.BAIFUBAO.value())
                        && (paymentItem.getPayStatus() == TradePayStatus.REFUNDING
                          || paymentItem.getPayStatus() == TradePayStatus.UNPAID
                        || paymentItem.getPayStatus() == TradePayStatus.REFUND_FAILED)) {
                    tv_refreshState.setVisibility(View.VISIBLE);
                } else {
                    tv_refreshState.setVisibility(View.GONE);
                }
                if (presenter == null) {
                    tv_refreshState.setVisibility(View.GONE);
                }
                tv_refreshState.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (presenter != null) {
                            presenter.doRefreshPayStatus(paymentItem);
                        }
                    }
                });
//            } else {
//                tv_refreshState.setVisibility(View.GONE);
//            }

            addView(view);

        }
    }

    /**
     * 计算溢收金额
     *
     * @param paymentVo
     * @return
     */
    private BigDecimal getItemExtraAmount(PayModeDetailsBean paymentVo) {
        BigDecimal extraAmount = BigDecimal.ZERO;
        if (paymentVo.getPaymentType() == PaymentType.TRADE_SELL
                || paymentVo.getPaymentType() == PaymentType.TRADE_REFUND) {
            PaymentItem paymentItem = paymentVo.getPaymentItem();
            if (paymentItem.getPayStatus() == TradePayStatus.PAID || paymentItem.getPayStatus() == TradePayStatus.REFUNDED) {
                BigDecimal augend = paymentItem.getFaceAmount().subtract(paymentItem.getUsefulAmount());
                augend = augend.subtract(paymentItem.getChangeAmount());
                extraAmount = extraAmount.add(augend);
            }
        }

        return extraAmount;
    }

    /**
     * 是否存在退款记录
     *
     * @param paymentGroup
     * @param payModeDetailsBean
     * @return
     */
    private boolean isShowRefund(List<PayModeDetailsBean> paymentGroup, PayModeDetailsBean payModeDetailsBean) {
        PaymentItem paymentItem = payModeDetailsBean.getPaymentItem();
        BigDecimal paymentItemTotal = payModeDetailsBean.getPayModeTotalDenomination().subtract(paymentItem.getChangeAmount());
        if (paymentItem.getPayStatus() != null
                && paymentItem.getPayStatus() == TradePayStatus.PAID
                && paymentItemTotal.doubleValue() > 0) {
            //判断是否存在退款中的记录，如果有则不显示退款
            for (final PayModeDetailsBean bean : paymentGroup) {
                TradePayStatus payStatus = bean.getPaymentItem().getPayStatus();
                if (payStatus != null && payStatus == TradePayStatus.REFUNDING) {
                    return false;
                }
            }

            BigDecimal refundAmount = getRefundAmount(paymentItem, paymentGroup);
            return paymentItemTotal.subtract(refundAmount).doubleValue() > 0;
        }
        return false;
    }

    /**
     * 获取当前一笔支付记录的退款总金额
     *
     * @param paymentItem
     * @param paymentGroup
     * @return
     */
    private BigDecimal getRefundAmount(PaymentItem paymentItem, List<PayModeDetailsBean> paymentGroup) {
        //当前已支付的记录减去当前支付已退款记录,如现金支付记录1 - 现金退款记录1
        BigDecimal refundAmountTotal = BigDecimal.ZERO;
        Long payModeId = paymentItem.getPayModeId();
        if (payModeId != null) {
            for (final PayModeDetailsBean bean : paymentGroup) {
                String relateId = bean.getPaymentItem().getRelateId();
                if (relateId != null && relateId.equals(String.valueOf(paymentItem.getId()))) {
                    Long _payModeId = bean.getPaymentItem().getPayModeId();
                    //判断是否相同支付方式
                    if (_payModeId != null && _payModeId.compareTo(payModeId) == 0) {
                        TradePayStatus payStatus = bean.getPaymentItem().getPayStatus();
                        if (payStatus != null && (payStatus == TradePayStatus.REFUNDED || payStatus == TradePayStatus.REFUNDING)) {
                            refundAmountTotal = refundAmountTotal.add(bean.getPayModeTotalDenomination().abs());
                        }
                    }
                }
            }
        }
        return refundAmountTotal;
    }

    public static String formatPaymodeName(String payModeName) {
        if (!TextUtils.isEmpty(payModeName)) {
            if (payModeName.length() > 9) {
                payModeName = payModeName.substring(0, 9) + "...";
            }
        }
        return payModeName;
    }


}