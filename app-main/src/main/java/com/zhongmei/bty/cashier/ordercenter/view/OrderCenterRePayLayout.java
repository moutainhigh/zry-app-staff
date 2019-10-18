package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.cashier.ordercenter.presenter.IOrderCenterDetailPresenter;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;


@EViewGroup(R.layout.dinner_ordercenter_repay_order_item)
public class OrderCenterRePayLayout extends LinearLayout {

    @ViewById(R.id.labelNo)
    TextView tvLabelNo;

    @ViewById(R.id.amount)
    TextView tvAmount;

    @ViewById(R.id.time)
    TextView tvTime;

    @ViewById(R.id.showinfos)
    TextView tvShowInfos;

    @ViewById(R.id.trade_infos)
    LinearLayout llTradeInfos;

    private int mLabelNo;

    private Context mContext;

    private TradePaymentVo mTradePaymentVo;

    private TradeType mTradeType = TradeType.SELL;

    boolean mIsHide = true;

    private IOrderCenterDetailPresenter mOrderCenterDetailPresenter;

    public OrderCenterRePayLayout(Context context) {
        super(context);
        mContext = context;
    }

    public OrderCenterRePayLayout(Context context, TradePaymentVo tradePaymentVo, int labelNo, IOrderCenterDetailPresenter presenter) {
        super(context);
        mContext = context;
        mTradePaymentVo = tradePaymentVo;
        mLabelNo = labelNo;
        mOrderCenterDetailPresenter = presenter;
    }

    public OrderCenterRePayLayout(Context context, TradePaymentVo tradePaymentVo, int labelNo, IOrderCenterDetailPresenter presenter, TradeType tradeType) {
        super(context);
        mContext = context;
        mTradePaymentVo = tradePaymentVo;
        mLabelNo = labelNo;
        mOrderCenterDetailPresenter = presenter;
        mTradeType = tradeType;
    }

    @AfterViews
    public void init() {
        if (mTradeType == TradeType.SPLIT) {
            tvLabelNo.setText(mContext.getString(R.string.split_trade) + " " + mLabelNo);
        } else {
            tvLabelNo.setText(mContext.getString(R.string.dinner_order_center_repay) + " " + mLabelNo);

        }
        if (mTradePaymentVo.getPaymentVoList() != null && mTradePaymentVo.getPaymentVoList().size() > 0) {
                        BigDecimal totalAmount = mOrderCenterDetailPresenter.getShiShouAmountString(mTradePaymentVo.getTradeVo(), mTradePaymentVo.getPaymentVoList(), false);
            tvAmount.setText(
                    mContext.getString(R.string.customer_money) + " " + Utils.formatPrice(totalAmount.doubleValue()));
                        if (mTradePaymentVo.getTradeVo() != null
                    && mTradePaymentVo.getTradeVo().getTrade() != null
                    && mTradePaymentVo.getTradeVo().getTrade().getClientUpdateTime() != null) {
                tvTime.setText(DateTimeUtils.formatDateTime(mTradePaymentVo.getTradeVo().getTrade().getClientUpdateTime())
                        + " (" + mTradePaymentVo.getTradeVo().getTrade().getUpdatorName() + ")");
            }
        } else {
            if (mTradePaymentVo.getTradeVo() != null && mTradePaymentVo.getTradeVo().getTrade() != null
                    && mTradePaymentVo.getTradeVo().getTrade().getTradeAmount() != null) {
                tvAmount.setText(mContext.getString(R.string.customer_money) + " "
                        + Utils.formatPrice(mTradePaymentVo.getTradeVo().getTrade().getTradeAmount().doubleValue()));
                                if (mTradePaymentVo.getTradeVo() != null
                        && mTradePaymentVo.getTradeVo().getTrade() != null
                        && mTradePaymentVo.getTradeVo().getTrade().getClientUpdateTime() != null) {
                    tvTime.setText(DateTimeUtils.formatDateTime(mTradePaymentVo.getTradeVo().getTrade().getClientUpdateTime())
                            + " (" + mTradePaymentVo.getTradeVo().getTrade().getUpdatorName() + ")");
                }
            }
        }
        OrderCenterInfoLayout infoLayout = OrderCenterInfoLayout_.build(mContext, mTradePaymentVo, mOrderCenterDetailPresenter);
        llTradeInfos.addView(infoLayout);
        showTradeInfos();
        tvShowInfos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mIsHide) {
                    mIsHide = false;
                } else {
                    mIsHide = true;
                }
                showTradeInfos();
            }
        });
    }

    private void showTradeInfos() {
        if (mIsHide) {
            tvShowInfos.setText(mContext.getString(R.string.dinner_order_center_showinfo));
            llTradeInfos.setVisibility(View.GONE);
        } else {
            tvShowInfos.setText(mContext.getString(R.string.dinner_order_center_hidinfo));
            llTradeInfos.setVisibility(View.VISIBLE);
        }
    }
}
