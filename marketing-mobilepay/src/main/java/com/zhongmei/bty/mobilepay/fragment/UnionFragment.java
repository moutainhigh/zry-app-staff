package com.zhongmei.bty.mobilepay.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.fragment.com.BasePayFragment;
import com.zhongmei.bty.mobilepay.fragment.com.CashEditTextWatcher;
import com.zhongmei.bty.mobilepay.fragment.com.PayView;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;

import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.PosConnectManager;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.UionPayDialogFragment;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;

import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.PayKeyPanel;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;

import java.math.BigDecimal;
import java.util.List;



public class UnionFragment extends BasePayFragment implements PosConnectManager.PosConnectListener, View.OnClickListener, PayView {
    private final String TAG = UnionFragment.class.getSimpleName();
    private Context mContext;
    private PayKeyPanel mNumberKeyBorad;
    private PosConnectManager mPosConnectManager;
    private TextView mUnionPayUnreceived;
    private LinearLayout mInputLayout;
    private RelativeLayout mInput;
    private EditText mCashPayEditValue;
    private ImageView mCashPayDeleteCash;
    private ImageView mPosStatusIcon;
    private TextView mUnionPayErrortext;
    private TextView mCashPayAlerttext;
    private Button mPay;
    private PosTransLog mPosTransLog;

    public static UnionFragment newInstance(IPaymentInfo info, DoPayApi doPayApi) {
        UnionFragment f = new UnionFragment();
        f.setPaymentInfo(info);
        f.setDoPayApi(doPayApi);
        f.setArguments(new Bundle());
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_union_fragment_new, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        mCashPayDeleteCash.setOnClickListener(this);
        mCashPayEditValue.setOnClickListener(this);
        mPay.setOnClickListener(this);
        mCashPayEditValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                return true;
            }
        });
        mNumberKeyBorad = new PayKeyPanel(view);
        mContext = this.getActivity();
        mPosConnectManager = new PosConnectManager(mContext, this);
        mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
        mNumberKeyBorad.setEnabled(true);
        mNumberKeyBorad.setDefaultValue(true);
        if (!mPaymentInfo.isDinner() || mPaymentInfo.isOrderCenter())
            DisplayServiceManager.updateDisplayPay(getActivity(), mPaymentInfo.getActualAmount());
        mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
        mCashPayDeleteCash.setVisibility(View.VISIBLE);
        updateNotPayMent();
        registerEventBus();
        setVewEvents();
        getCurrentPosStatus();    }

    private void assignViews(View view) {
        mUnionPayUnreceived = (TextView) view.findViewById(R.id.union_pay_unreceived);
        mInputLayout = (LinearLayout) view.findViewById(R.id.input_layout);
        mInput = (RelativeLayout) view.findViewById(R.id.input);
        mCashPayEditValue = (EditText) view.findViewById(R.id.cash_pay_edit_value);
        mCashPayAlerttext = (TextView) view.findViewById(R.id.cash_pay_alerttext);
        mCashPayDeleteCash = (ImageView) view.findViewById(R.id.delete_all_text);
        mPosStatusIcon = (ImageView) view.findViewById(R.id.pos_status_icon);
        mUnionPayErrortext = (TextView) view.findViewById(R.id.union_pay_errortext);
        mPay = (Button) view.findViewById(R.id.pay);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            mNumberKeyBorad.setDefaultValue(true);
            mNumberKeyBorad.setEnabled(true);
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
        } else {
                        clearInputData();

        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.pay) {
            if (!ClickManager.getInstance().isClicked()) {
                if (doPayChecked(false)) {
                                        preparePay();
                }
            }
        } else if (vId == R.id.cash_pay_edit_value) {
            mNumberKeyBorad.setCurrentInput(mCashPayEditValue);

        } else if (vId == R.id.delete_all_text) {

            mCashPayEditValue.setText("");
        }
    }


    private void setVewEvents() {
                mCashPayEditValue.addTextChangedListener(new CashEditTextWatcher(this, this.mPaymentInfo, mCashPayEditValue, ShopInfoCfg.getInstance().getCurrencySymbol()));

    }


    private void getCurrentPosStatus() {
        if (PaySettingCache.isUnionpay()) {
            updatePosStatusIcon(PosConnectManager.isPosConnected());
        } else {
            mPosStatusIcon.setVisibility(View.GONE);
        }
    }

    private void updatePosStatusIcon(boolean isPosConnected) {
        if (mPosStatusIcon.getVisibility() == View.GONE)
            mPosStatusIcon.setVisibility(View.VISIBLE);
        if (isPosConnected) {
            mPosStatusIcon.setBackground(mContext.getResources().getDrawable(R.drawable.pos_icon_online));
        } else {
            mPosStatusIcon.setBackground(mContext.getResources().getDrawable(R.drawable.pos_icon_offline));
        }
    }

    IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOK, int errorCode) {
            try {
                if (isOK) {
                    mNumberKeyBorad.setEnabled(true);
                    mCashPayEditValue.setText("");

                } else {
                    mNumberKeyBorad.setEnabled(true);
                    mPay.setEnabled(true);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterEventBus();
        if (mPosConnectManager != null) {
            mPosConnectManager.close();
            mPosConnectManager = null;
        }
        super.onDestroy();
    }


    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            mNumberKeyBorad.setDefaultValue(true);
            updateNotPayMent();
        }
    }


    public void updateNotPayMent() {
        double inputvalue = getInputValue();
        double notpayvalue = mPaymentInfo.getActualAmount();
        double value = inputvalue - notpayvalue;
        if (value > 0) {
            mCashPayAlerttext.setVisibility(View.VISIBLE);
            mCashPayAlerttext.setText(String.format(getString(R.string.cash_change_text), CashInfoManager.formatCash(value)));
        } else if (value < 0) {
            mCashPayAlerttext.setVisibility(View.VISIBLE);
            mCashPayAlerttext.setText(getString(R.string.pay_rest_payment_text) + CashInfoManager.formatCash(-value));
        } else {
            mCashPayAlerttext.setVisibility(View.INVISIBLE);
        }
        DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
    }

    public void inputMoreAlter() {
        ToastUtil.showLongToastCenter(getActivity(),
                getActivity().getString(R.string.pay_more_input_alter));
    }

    @Override
    public void showClearTB(boolean isShow) {
        if (isShow)
            mCashPayDeleteCash.setVisibility(View.VISIBLE);
        else
            mCashPayDeleteCash.setVisibility(View.INVISIBLE);
    }

    @Override
    public void clearInputData() {
        if (mPaymentInfo != null && mPaymentInfo.getOtherPay() != null)
            mPaymentInfo.getOtherPay().clear();
    }



        @Override
    public void onConnected() {
        getCurrentPosStatus();
    }

    @Override
    public void onDisconnected() {
        getCurrentPosStatus();     }

        public double getInputValue() {
        String inputValueStr = mCashPayEditValue.getText().toString();
        return DoPayUtils.formatInputCash(inputValueStr);
    }

        private void preparePay() {
        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_UNION);
        if (PaySettingCache.isUnionpay()) {                        List<PaymentItem> paidItems = mPaymentInfo.getPaidPaymentItems();
            if (!Utils.isEmpty(paidItems)) {
                                for (PaymentItem item : paidItems) {
                    if (PayModeId.POS_CARD.value().equals(item.getPayModeId())) {
                        ToastUtil.showShortToast(R.string.pay_union_pos_pay_repay_alter);
                        return;
                    }
                }
            }
            mNumberKeyBorad.setEnabled(false);
            mPay.setEnabled(false);
            checkUnionPosPay(mPayOverCallback);
        } else {                        mNumberKeyBorad.setEnabled(false);
            mPay.setEnabled(false);
            this.mPaymentInfo.getOtherPay().clear();
            this.mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItem());
            if (mDoPayApi != null)
                mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
        }
    }

        private PayModelItem getPayModelItem() {
        PayModelItem item = null;
        if (PaySettingCache.isUnionpay()) {            item = new PayModelItem(PayModeId.POS_CARD);
            item.setPosTransLog(this.mPosTransLog);        } else {            item = new PayModelItem(PayModeId.BANK_CARD);        }
        item.setUsedValue(BigDecimal.valueOf(this.getInputValue()));
        return item;
    }

    private boolean isPosUnionPayed(PosTransLog log) {
        boolean isPos = false;
        if (log != null && log.getAmount() != null) {
            Double unionAmount = getInputValue() * 100;
            if (log.getAmount() == unionAmount.intValue()) {
                isPos = true;
            }
        }
        return isPos;
    }

        private void checkUnionPosPay(final IPayOverCallback callback) {
                if (PosConnectManager.isPosConnected()) {
            if (this.mPosTransLog != null && this.isPosUnionPayed(this.mPosTransLog))            {
                                this.mPaymentInfo.getOtherPay().clear();
                this.mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItem());
                if (mDoPayApi != null)
                    mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
            } else {
                doUnionPosProcess(getInputValue(), this.getActivity(), callback);            }
        } else {
            callback.onFinished(false, 0);            ToastUtil.showLongToastCenter(this.getActivity(), getString(R.string.pay_pos_connection_closed));
        }
    }


    private void doUnionPosProcess(final double unionPayAmount, final FragmentActivity context, final IPayOverCallback callback) {
                UionPayDialogFragment uionPayDialogFragment = new UionPayDialogFragment.UionPayDialogFragmentBuilder()
                .closeListener(null).buildPay(BigDecimal.valueOf(unionPayAmount),
                        mPaymentInfo.getTradeVo().getTrade().getTradeNo(),
                        new UionPayDialogFragment.PosOvereCallback() {

                            @Override
                            public void onFinished(UionPayDialogFragment.UionPayStaus status, boolean issuccess, PosTransLog log, NewLDResponse ldResponse) {
                                if (issuccess) {                                                                        mPosTransLog = log;
                                    mPaymentInfo.getOtherPay().clear();
                                    mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItem());
                                    if (mDoPayApi != null)
                                        mDoPayApi.doPay(getActivity(), mPaymentInfo, mPayOverCallback);

                                } else {                                    callback.onFinished(false, 0);
                                }
                            }
                        });
        uionPayDialogFragment.show(context.getSupportFragmentManager(), "union");
    }
}
