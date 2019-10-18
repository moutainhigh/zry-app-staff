package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.cashier.ordercenter.adapter.DialogPayedAdapter;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class PayedLayout extends LinearLayout {
    private String uuid;
        private List<PaymentVo> sellPaymentVo = new ArrayList<>();

        private List<PaymentVo> refundPaymentVo = new ArrayList<>();

        private List<PaymentVo> adjustPaymentVos = new ArrayList<PaymentVo>();

    private Context mContext;

    private View mContentView;

    private View main_content;

    private TextView title;
    private int mType = -1;

    private TradeDeposit mTradeDeposit;

    public PayedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_center_operate_dialog_fragment_payed, null);
        main_content = view.findViewById(R.id.content);
        title = (TextView) view.findViewById(R.id.title);
        addView(view);
    }

    public PayedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setTitle(int titleID) {
        this.title.setText(titleID);
    }

    public void setCurrentType(int type) {
        mType = type;
    }

    public void setPayment(PaymentVo paymentVo) {
        sellPaymentVo.add(paymentVo);
        if (sellPaymentVo == null || sellPaymentVo.size() == 0) {
            main_content.setVisibility(View.GONE);
            return;
        }
        initPayedListView();
    }

    public void setPayment(List<PaymentVo> paymentVos) {
        adjustPaymentVos = paymentVos;
        if (adjustPaymentVos == null) {
            main_content.setVisibility(View.GONE);
            return;
        }
        initPaymentVos(paymentVos);
        initPayedListView();
    }

    public void setTradeDeposit(TradeDeposit tradeDeposit) {
        mTradeDeposit = tradeDeposit;
    }

    private void initPaymentVos(List<PaymentVo> paymentVos) {
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment() != null) {
                switch (paymentVo.getPayment().getPaymentType()) {
                    case TRADE_SELL:
                        sellPaymentVo.add(paymentVo);
                        break;
                    case TRADE_REFUND:
                        refundPaymentVo.add(paymentVo);
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

    private void initPayedListView() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.order_center_operate_dialog_fragment_payed_content, null);
        addView(mContentView);


        List<PaymentItem> paymentItems = makePaymentList();
                BigDecimal paymentAmount = BigDecimal.ZERO;
        for (PaymentItem item : paymentItems) {
            if (item.getPayStatus() == TradePayStatus.PAID
                    || item.getPayStatus() == TradePayStatus.REFUNDED
                    || item.getPayStatus().isUnknownEnum()) {
                paymentAmount = paymentAmount.add(item.getUsefulAmount());
            }
        }
                if (mTradeDeposit != null && mTradeDeposit.getDepositRefund() != null) {
            paymentAmount = paymentAmount.add(mTradeDeposit.getDepositPay().negate());

                        paymentItems.clear();
            PaymentItem paymentItem = new PaymentItem();
            paymentItem.setPayModeId(PayModeId.CASH.value());
            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(PayModeId.CASH.value()));
            paymentItem.setUsefulAmount(paymentAmount);
            paymentItems.add(paymentItem);
        }
        ((TextView) mContentView.findViewById(R.id.totalPay))
                .setText(Utils.formatPrice(paymentAmount.doubleValue()));

        ListView lv = (ListView) mContentView.findViewById(R.id.lv_cashRecord);
        DialogPayedAdapter paymentAdapter = new DialogPayedAdapter(mContext, mType, paymentItems);
        lv.setAdapter(paymentAdapter);
        setListViewHeightBasedOnChildren(lv);
    }

    private List<PaymentItem> makePaymentList() {
        List<PaymentItem> paymentItems = new ArrayList<PaymentItem>();
                if (sellPaymentVo != null) {
            for (PaymentVo paymentVo : sellPaymentVo) {
                List<PaymentItem> pis = paymentVo.getPaymentItemList();
                if (pis != null) {
                    paymentItems.addAll(pis);
                }
            }
        }
                if (!Utils.isEmpty(refundPaymentVo)) {
            for (PaymentVo paymentVo : refundPaymentVo) {
                List<PaymentItem> pis = paymentVo.getPaymentItemList();
                if (pis != null) {
                    paymentItems.addAll(pis);
                }
            }

        }
        return paymentItems;
    }


    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int singleHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            singleHeight = listItem.getMeasuredHeight();
            break;
        }
        listView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                singleHeight * listAdapter.getCount()));
    }
}
