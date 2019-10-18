package com.zhongmei.bty.buffet.orderdish;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.common.view.CalmResponseToastFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.List;


public class DepositInfoDialog extends Dialog implements View.OnClickListener, NumberInputdialog.InputOverListener {

    private FragmentActivity mContext;

    private TradeVo mTradeVo;
    private TextView mTvTitle;
    private Button mBtnClose;
    private TextView mTvDesktopNum;
    private TextView mTvSerialNum;
    private TextView mTvPrice;
    private TextView mTvDepositValue;
    private TextView mBtnEdit;
    private Button mBtnOk;
    private LinearLayout layout_depositWay;
    private TextView tv_depositWay;

    private void assignViews() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mBtnClose = (Button) findViewById(R.id.btn_close);
        mTvDesktopNum = (TextView) findViewById(R.id.tv_desktop_num);
        mTvSerialNum = (TextView) findViewById(R.id.tv_serial_num);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvDepositValue = (TextView) findViewById(R.id.tv_deposit_value);
        mBtnEdit = (TextView) findViewById(R.id.btn_edit);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        layout_depositWay = (LinearLayout) findViewById(R.id.layout_deposit_way);
        tv_depositWay = (TextView) findViewById(R.id.tv_deposit_way);
        mBtnClose.setOnClickListener(this);
        mBtnEdit.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
    }


    public DepositInfoDialog(Context context, TradeVo vo) {
        super(context, R.style.custom_alert_dialog);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = (FragmentActivity) context;
        mTradeVo = vo;
        setContentView(R.layout.dialog_deposit_info);
        assignViews();
        mTvSerialNum.setText(MainApplication.getInstance().getResources().getString(R.string.deposit_info_dialog_serial_no) + mTradeVo.getTradeExtra().getSerialNumber());
        List<TradeTable> list = mTradeVo.getTradeTableList();
        String tables = "";
        if (list != null) {
            int len = list.size();
            for (int i = 0; i < len; i++) {
                if (i == len - 1)
                    tables += list.get(i).getTableName();
                else
                    tables += list.get(i).getTableName() + ",";
            }
        }
        if (TextUtils.isEmpty(tables)) {
            mTvDesktopNum.setVisibility(View.GONE);
        } else {
            mTvDesktopNum.setVisibility(View.VISIBLE);
            mTvDesktopNum.setText(MainApplication.getInstance().getResources().getString(R.string.deposit_info_dialog_desk_no) + tables);
        }
        mTvPrice.setText(MainApplication.getInstance().getResources().getString(R.string.deposit_info_dialog_trade_amount)
                + ShopInfoCfg.formatCurrencySymbol(mTradeVo.getTrade().getTradeAmount().toString()));
        mTvDepositValue.setText(mTradeVo.getTradeDeposit().getDepositPay().toString());

        layout_depositWay.setVisibility(View.VISIBLE);
        tv_depositWay.setText(vo.getTradeDepositPaymentItem().getPayModeName());
            }

    public static DepositInfoDialog show(Context context, TradeVo vo) {
        DepositInfoDialog depositInfoDialog = new DepositInfoDialog(context, vo);
        depositInfoDialog.show();
        return depositInfoDialog;
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_edit:
                showNumberInputDialog();
                break;
            case R.id.btn_ok:
                if (!ClickManager.getInstance().isClicked()) {                                        VerifyHelper.verifyAlert(mContext, DinnerApplication.PERMISSION_DINNER_CASH,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    BigDecimal temp = BigDecimal.ZERO;
                                    String valueStr = mTvDepositValue.getText().toString().trim();
                                    if (!TextUtils.isEmpty(valueStr)) {
                                        if (valueStr.startsWith(ShopInfoCfg.getInstance().getCurrencySymbol()))
                                            valueStr = valueStr.substring(ShopInfoCfg.getInstance().getCurrencySymbol().length());
                                        BigDecimal value = new BigDecimal(valueStr);
                                        if (value.floatValue() > mTradeVo.getTradeDeposit().getDepositPay().floatValue()) {
                                            ToastUtil.showShortToast(R.string.deposit_value_error);
                                            return;
                                        }
                                        temp = value;
                                    } else {
                                        ToastUtil.showShortToast(R.string.depostit_value_isnull);
                                        return;
                                    }
                                                                        if (mTradeVo.getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN && mTradeVo.getTrade().getTradePayStatus() == TradePayStatus.PREPAID) {
                                        mainTradeFinishAndDepositRefund(mTradeVo.getTradeDepositPayRelation().getPaymentItemId(), temp);
                                    } else {
                                        backDeposit(mTradeVo.getTrade(), mTradeVo.getTradeDepositPayRelation().getPaymentItemId(), temp);
                                    }
                                }
                            });
                }
                break;
        }
    }

    @Override
    public void afterInputOver(String inputContent) {
        mTvDepositValue.setText(ShopInfoCfg.formatCurrencySymbol(inputContent));
    }


    private void showNumberInputDialog() {
        double inputvalue = mTradeVo.getTradeDeposit().getDepositPay().doubleValue();
        double maxValue = inputvalue;
        String defaultInput = null;
        NumberInputdialog numberDialog = new NumberInputdialog(mContext,
                mContext.getString(R.string.deposit_dialog_title_amount),
                mContext.getString(R.string.deposit_dialog_input_amount_hint),
                defaultInput, maxValue, this);
        numberDialog.setNumberType(NumberInputdialog.NUMBER_TYPE_FLOAT)
                .setDotType(NumberInputdialog.DotType.DOT)
                .setRemark(mContext.getString(R.string.deposit_dialog_all_amount) + ShopInfoCfg.formatCurrencySymbol(mTradeVo.getTradeDeposit().getDepositPay().toString()))
                .show();
    }

    private void backDeposit(Trade trade, Long paymentitemId, BigDecimal depositRefund) {
        TradeOperates mTradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<PayResp> listener = LoadingResponseListener.ensure(responseListener, mContext.getSupportFragmentManager());
        Reason reason = null;

        if (trade.getTradePayStatus() == TradePayStatus.PREPAID) {            mTradeOperates.buffetFinishAndDepositRefund(trade.getId(), depositRefund, paymentitemId, reason, listener);
        } else {            mTradeOperates.buffetDepositRefund(trade.getId(), depositRefund, paymentitemId, reason, listener);
        }
    }

        private void mainTradeFinishAndDepositRefund(final Long paymentitemId, final BigDecimal depositRefund) {

        final TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<TradeResp> unionistener = new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showLongToast(response.getMessage());
                                        ResponseListener<PayResp> listener = LoadingResponseListener.ensure(responseListener, mContext.getSupportFragmentManager());
                    Reason reason = null;
                                        tradeOperates.buffetDepositRefund(mTradeVo.getTrade().getId(), depositRefund, paymentitemId, reason, listener);
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };

    }

        private ResponseListener<PayResp> responseListener = new ResponseListener<PayResp>() {
        @Override
        public void onResponse(ResponseObject<PayResp> response) {
            if (ResponseObject.isOk(response)) {
                List<PaymentItem> list = response.getContent().getPaymentItems();
                for (PaymentItem item : list) {
                    if (item.getPayModeId() == PayModeId.CASH.value()) {
                                                                    }
                }
                CalmResponseToastFragment.newInstance(CalmResponseToastFragment.SUCCESS, mContext.getString(R.string.order_deposit_refund_success_message), null)
                        .show(mContext.getSupportFragmentManager(), "backDeposit");
            } else {

                CalmResponseToastFragment.newInstance(CalmResponseToastFragment.FAIL, mContext.getString(R.string.order_deposit_refund_fail_message), response.getMessage())
                        .show(mContext.getSupportFragmentManager(), "backDeposit");
            }
            dismiss();
        }

        @Override
        public void onError(VolleyError error) {
            CalmResponseToastFragment.newInstance(CalmResponseToastFragment.FAIL, mContext.getString(R.string.order_deposit_refund_fail_message), error.getMessage())
                    .show(mContext.getSupportFragmentManager(), "backDeposit");
            dismiss();
        }
    };
}


