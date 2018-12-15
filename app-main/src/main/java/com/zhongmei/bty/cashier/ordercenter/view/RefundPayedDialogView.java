package com.zhongmei.bty.cashier.ordercenter.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.cashier.ordercenter.adapter.PaymentAdapter;
import com.zhongmei.bty.cashier.ordercenter.adapter.PaymentAdapter.PaymentGroup;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;

public class RefundPayedDialogView extends CustomRefuseDialogView {

    // 销货支付对象
    private PaymentVo sellPaymentVo;

    // 调账支付对象
    private List<PaymentVo> adjustPaymentVos = new ArrayList<PaymentVo>();

    private Context mContext;

    private View mContentView;

    /**
     * 用于展示退货的构造方法
     *
     * @param context
     * @param paymentVo
     */
    public RefundPayedDialogView(Context context, PaymentVo paymentVo) {
        super(context);
        this.mContext = context;
        this.sellPaymentVo = paymentVo;

        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        setTitle(R.string.order_center_detail_refund_detail_title);
        setOnCloseListener();

        initPayedListView();
    }

    /**
     * 用于展示退货的构造方法
     *
     * @param context
     * @param paymentVos
     */
    public RefundPayedDialogView(Context context, List<PaymentVo> paymentVos) {
        super(context);
        this.mContext = context;
        initPaymentVos(paymentVos);

        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        setTitle(R.string.order_center_detail_refund_detail_title);
        setOnCloseListener();

        initPayedListView();
    }

    private void initPaymentVos(List<PaymentVo> paymentVos) {
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment() != null) {
                switch (paymentVo.getPayment().getPaymentType()) {
                    case TRADE_SELL:
                        sellPaymentVo = paymentVo;
                        break;
                    case ADJUST:
                        adjustPaymentVos.add(paymentVo);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setOnCloseListener() {
        super.setOnCloseButton(new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * <pre>
     * 参数说明:
     * 返回类型:@return void
     * 方法说明:用于展示退货的收银记录
     * 修改历史:
     *    修改人员:mac 修改日期:2015年4月23日 下午1:55:37
     *    修改目的:
     * </pre>
     */
    private void initPayedListView() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.order_center_refund_pay_view, null);
        LinearLayout ll = getLinearLayoutForCustom();
        ll.addView(mContentView);

        // 展示抹零数据
        LinearLayout llExempt = (LinearLayout) mContentView.findViewById(R.id.ll_exempt);
        if (sellPaymentVo.getPayment() != null
                && (sellPaymentVo.getPayment().getExemptAmount().compareTo(BigDecimal.ZERO) > 0)) {
            llExempt.setVisibility(View.VISIBLE);
            Payment payment = sellPaymentVo.getPayment();
            TextView tvExemptAmount = (TextView) mContentView.findViewById(R.id.tv_exempt_amount);
            tvExemptAmount.setText(ShopInfoCfg.formatCurrencySymbol(
                    payment.getExemptAmount().negate()));
        }

        ExpandableListView lv = (ExpandableListView) mContentView.findViewById(R.id.lv_cashRecord);
        List<PaymentGroup> paymentGroups = makePaymentGroup();
        PaymentAdapter paymentAdapter = new PaymentAdapter(mContext, paymentGroups);
        lv.setAdapter(paymentAdapter);
        for (int i = 0; i < paymentGroups.size(); i++) {
            lv.expandGroup(i);
        }
        //不能收缩
        lv.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView listView, View groupView, int position, long id) {
                return true;
            }
        });
        show();
    }

    /**
     * 初始化结账和调账信息
     *
     * @return
     */
    private List<PaymentGroup> makePaymentGroup() {
        List<PaymentGroup> paymentGroups = new ArrayList<PaymentAdapter.PaymentGroup>();
        // 结账
        if (sellPaymentVo != null && sellPaymentVo.getPayment() != null && sellPaymentVo.getPaymentItemList() != null
                & sellPaymentVo.getPaymentItemList().size() > 0) {
            PaymentGroup paymentGroup =
                    new PaymentGroup(getContext().getString(R.string.dinner_checkout), sellPaymentVo.getPayment().getMemo(), sellPaymentVo.getPaymentItemList());
            paymentGroups.add(paymentGroup);
        }

        // 调账（暂不显示，调账退货这块逻辑复杂，产品还没想好）
        // if (adjustPaymentVos != null &&
        // adjustPaymentVos.size() > 0) {
        // for (int i = 0; i < adjustPaymentVos.size(); i++)
        // {
        // PaymentVo paymentVo = adjustPaymentVos.get(i);
        // if (paymentVo != null &&
        // paymentVo.getPayment().getMemo() != null
        // && paymentVo.getPaymentItemList() != null &
        // paymentVo.getPaymentItemList().size() > 0) {
        // PaymentGroup paymentGroup =
        // new PaymentGroup("调账" + (i + 1),
        // paymentVo.getPayment().getMemo(),
        // sellPaymentVo.getPaymentItemList());
        // paymentGroups.add(paymentGroup);
        // }
        // }
        // }

        return paymentGroups;
    }

}