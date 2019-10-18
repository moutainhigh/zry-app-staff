package com.zhongmei.bty.mobilepay.fragment.onlinepay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.bty.mobilepay.IOnlinePayBreakCallback;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPaymentMenuType;
import com.zhongmei.bty.mobilepay.PayKeyPanel;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.MobliePayMenuTool;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.event.AmountEditedEvent;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.fragment.com.CashEditTextWatcher;
import com.zhongmei.bty.mobilepay.fragment.com.PayView;
import com.zhongmei.bty.mobilepay.fragment.com.BasePayFragment;
import com.zhongmei.bty.mobilepay.fragment.mobilepay.MobilePayDialog;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;




public class OnlinePayFragment extends BasePayFragment implements View.OnClickListener, IPaymentMenuType, IOnlinePayBreakCallback, PayView {

    private final String TAG = OnlinePayFragment.class.getSimpleName();

    private PayKeyPanel mNumberKeyBorad;
    private AmountEditedEvent mAmountEditedEvent = new AmountEditedEvent(PayModelGroup.CASH, "");
    private int mPayType = PAY_MENU_TYPE_ALIPAY;
    private PayModeId mCurrentPayModeId = PayModeId.WEIXIN_PAY;    private TextView mCashPayAlerttext;

    private EditText mCashPayEditValue;
    private ImageView mCashPayDeleteCash;
    private boolean isDefaultShowDialog = true;    private Button mPay;
    private boolean mInit = false;

    public static OnlinePayFragment newInstance(int payType, IPaymentInfo paymentInfo, DoPayApi doPayApi) {
        OnlinePayFragment f = new OnlinePayFragment();
        f.setPayType(payType);
        f.setDoPayApi(doPayApi);
        f.setPaymentInfo(paymentInfo);
        Bundle bundle = new Bundle();
        f.setArguments(bundle);
        return f;
    }

    private void setPayType(int payType) {
        this.mPayType = payType;
    }


    private void assignViews(View view) {
        mCashPayAlerttext = (TextView) view.findViewById(R.id.cash_pay_alerttext);
        mCashPayEditValue = (EditText) view.findViewById(R.id.cash_pay_edit_value);
        mCashPayDeleteCash = (ImageView) view.findViewById(R.id.delete_all_text);
        mPay = (Button) view.findViewById(R.id.pay);
    }

    public void setDefaultShowDialog(boolean defaultShowDialog) {
        isDefaultShowDialog = defaultShowDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (mPayType) {
            case PAY_MENU_TYPE_ALIPAY:
                mCurrentPayModeId = PayModeId.ALIPAY;
                break;
            case PAY_MENU_TYPE_BAIDU:
                mCurrentPayModeId = PayModeId.BAIFUBAO;
                break;
            case PAY_MENU_TYPE_WEIXIN:
                mCurrentPayModeId = PayModeId.WEIXIN_PAY;
                break;
            case PAY_MENU_TYPE_MEITUANCASHPAY:
                mCurrentPayModeId = PayModeId.MEITUAN_FASTPAY;
                break;
            case PAY_MENU_TYPE_MEMBER:                mCurrentPayModeId = PayModeId.MEMBER_CARD;
                break;
            case PAY_MENU_TYPE_JIN_CHENG:
                mCurrentPayModeId = PayModeId.JIN_CHENG;
                break;
            case PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD:
                mCurrentPayModeId = PayModeId.JIN_CHENG_VALUE_CARD;
                break;
            case PAY_MENU_TYPE_UNIONPAY_CLOUD:                mCurrentPayModeId = PayModeId.UNIONPAY_CLOUD_PAY;
                break;
            case PAY_MENU_TYPE_ICBC_EPAY:                mCurrentPayModeId = PayModeId.ICBC_E_PAY;
                break;
            case PAY_MENU_TYPE_MOBILE:                mCurrentPayModeId = PayModeId.MOBILE_PAY;
                break;
            case PAY_MENU_TYPE_DX_YIPAY:                mCurrentPayModeId = PayModeId.DIANXIN_YIPAY;
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mInit) {
            mInit = true;
            if (isDefaultShowDialog && this.mPaymentInfo != null && !this.mPaymentInfo.isNeedToPayDeposit() && !isNeedToDeductionEarnest()) {
                verify();
            }
        }
    }

    @Override
    public void onPayStop() {
        try {
            if (this.getActivity() != null && !this.getActivity().isDestroyed() && this.isAdded()) {
                mCashPayEditValue.setText("");
                updateNotPayMent();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_union_fragment_new, container, false);
        assignViews(view);
        mPay.setOnClickListener(this);
        mCashPayEditValue.setOnClickListener(this);
        mCashPayDeleteCash.setOnClickListener(this);
        mCashPayEditValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                return true;
            }
        });
        if (mPaymentInfo != null) {
            mAmountEditedEvent.setBusinessType(mPaymentInfo.getTradeBusinessType());
            mNumberKeyBorad = new PayKeyPanel(view);

            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            mNumberKeyBorad.setEnabled(true);
            mNumberKeyBorad.setDefaultValue(true);
            mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
            mCashPayDeleteCash.setVisibility(View.VISIBLE);
            updateNotPayMent();
            mAmountEditedEvent.setAmountValue(mPaymentInfo.getActualAmount());
            EventBus.getDefault().post(mAmountEditedEvent);
            EventBus.getDefault().register(this);
            setVewEvents();
            if (mCurrentPayModeId == PayModeId.MEMBER_CARD)            {
                View emputyView = view.findViewById(R.id.union_pay_errortext);
                emputyView.setVisibility(View.GONE);
            }
        }
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            updateNotPayMent();
            if (mInit && !this.mPaymentInfo.isNeedToPayDeposit() && !isNeedToDeductionEarnest()) {
                verify();
            }
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
                    verify();
                }
            }
        } else if (vId == R.id.cash_pay_edit_value) {
            mNumberKeyBorad.setCurrentInput(mCashPayEditValue);

        } else if (vId == R.id.delete_all_text) {

            mCashPayEditValue.setText("");
        }
    }

    private void verify() {
        if ((mPaymentInfo.isDinner() || mPaymentInfo.isBeauty()) && mCurrentPayModeId != PayModeId.MEMBER_CARD) {
            VerifyHelper.verifyAlert(getActivity(), mPaymentInfo.getCashPermissionCode(),
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            doPay();
                        }
                    });
        } else {
            doPay();
        }
    }

    private void doPay() {
        PayModelItem payModelItem = new PayModelItem(mCurrentPayModeId);
        payModelItem.setUsedValue(BigDecimal.valueOf(getInputValue()));
        if (mCurrentPayModeId == PayModeId.MOBILE_PAY) {            if (MobliePayMenuTool.isSetMobilePay()) {
                MobilePayDialog payDialog = MobilePayDialog.newInstance(mDoPayApi, mPaymentInfo, payModelItem, IPayConstParame.ONLIEN_SCAN_TYPE_ACTIVE);
                payDialog.setOnPayStopListener(this);
                payDialog.show(getFragmentManager(), "MobilePayDialog");
            } else {
                ToastUtil.showShortToast(getString(R.string.please_open_onecode_pay));
            }
        } else {            if (mDoPayApi != null)
                mDoPayApi.showOnlinePayDialog(this.getActivity(), payModelItem, mPaymentInfo, IPayConstParame.ONLIEN_SCAN_TYPE_ACTIVE, this);
        }
    }


    private void setVewEvents() {
                mCashPayEditValue.addTextChangedListener(new CashEditTextWatcher(this, this.mPaymentInfo, mCashPayEditValue, ShopInfoCfg.getInstance().getCurrencySymbol()));
    }


    public void updateNotPayMent() {
        double notpayvalue = mPaymentInfo.getActualAmount();
        String input = mCashPayEditValue.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            if (input.startsWith(".")) {
                input = "0" + input;
            }
            String valueStr = input.replace(ShopInfoCfg.getInstance().getCurrencySymbol(), "").trim();
            double inputvalue = Double.valueOf(valueStr);
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
        } else {
            mCashPayAlerttext.setVisibility(View.VISIBLE);
            mCashPayAlerttext.setText(getString(R.string.pay_rest_payment_text) + CashInfoManager.formatCash(notpayvalue));
            DoPayUtils.updatePayEnable(getActivity(), mPay, false);
        }
    }

    public void inputMoreAlter() {
        ToastUtil.showLongToastCenter(getActivity(), getActivity().getString(R.string.pay_more_input_alter));
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


    public void onEventMainThread(ExemptEventUpdate event) {
        mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
        mNumberKeyBorad.setDefaultValue(true);
        updateNotPayMent();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

        public double getInputValue() {
        String inputValueStr = mCashPayEditValue.getText().toString();
        return DoPayUtils.formatInputCash(inputValueStr);
    }
}
