package com.zhongmei.bty.pay.fragment.memberpay;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.constants.SettingConstant;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.PosConnectManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.ReadKeyboardDialogFragment;
import com.zhongmei.bty.basemodule.devices.mispos.enums.WorkStatus;
import com.zhongmei.bty.basemodule.devices.mispos.event.EventReadKeyboard;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment_;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.IPaymentMenuType;
import com.zhongmei.bty.mobilepay.PayKeyPanel;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayMethodItem;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.bean.PaymentMenuTool;
import com.zhongmei.bty.mobilepay.dialog.PayDepositPromptDialog;
import com.zhongmei.bty.mobilepay.event.AmountEditedEvent;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.event.MemberPayChargeEvent;
import com.zhongmei.bty.mobilepay.event.RefreshTradeVoEvent;
import com.zhongmei.bty.mobilepay.fragment.com.BasePayFragment;
import com.zhongmei.bty.mobilepay.fragment.com.PayView;
import com.zhongmei.bty.mobilepay.fragment.onlinepay.OnlinePayDialog;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.mobilepay.utils.PayDisplayUtil;
import com.zhongmei.bty.mobilepay.v1.event.MemberLoginEvent;
import com.zhongmei.bty.snack.customer.ModifyCustomerResp;
import com.zhongmei.bty.snack.customer.TradeCustomerConvert;
import com.zhongmei.bty.snack.customer.TradeModifyOperates;
import com.zhongmei.bty.snack.offline.Snack;
import com.zhongmei.bty.snack.orderdish.data.SDConstant.MobClick;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerLoginReq;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.core.security.Password;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.util.EmptyUtils;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.ValueEnums;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.pay_member_pay_fragment_layout)
public class MemberPayFragment extends BasePayFragment implements PayView {
    private final String TAG = MemberPayFragment.class.getSimpleName();
    @ViewById(R.id.cash_pay_alerttext)
    TextView mPayAlterTV;

    PayKeyPanel mNumberKeyBorad;

    @ViewById(R.id.pay)
    Button mPay;


    @ViewById(R.id.member_pay_user_exit)
    TextView mUserExit;

    @ViewById(R.id.cash_pay_edit_value)
    EditText mCashPayEditValue;
    @ViewById(R.id.member_pay_dict_sum)
    TextView mDictSum;
    @ViewById(R.id.bt_to_charge)
    Button btcharge;
    @ViewById(R.id.member_pay_delete_dict)
    ImageView mCashPayDeleteCash;
    @ViewById(R.id.member_pay_code_pay_bt)
    TextView mCodePayTV;    private double mValueCardBalance = 0;
    private long mIntegralBalance;
    private String dictNotEnough_alter;
    private String dictInputMore_alter;
    private CustomerResp mCustomer;

    private EcCard mecCard;

                private AmountEditedEvent mAmountEditedEvent = new AmountEditedEvent(PayModelGroup.VALUE_CARD, "");

        private PayModeId mCurrentPayMode;


        private boolean isAnoCardSinglePay = false;





        @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNumberKeyBorad = new PayKeyPanel(view);
    }


    @AfterViews
    void init() {
        mAmountEditedEvent.setPayMethodId(IPaymentMenuType.PAY_MENU_TYPE_MEMBER);
        dictNotEnough_alter = getResources().getString(R.string.pay_dict_not_enough);
        dictInputMore_alter = getResources().getString(R.string.pay_more_input_alter);
        boolean ishowCharge = false;        if (this.mPaymentInfo != null) {
            if (!mPaymentInfo.isDinner()) {
                mIsSuportGroupPay = !isNeedMemberPrice();
            }
            mCustomer = mPaymentInfo.getCustomer();
            mecCard = mPaymentInfo.getEcCard();
            if (mCustomer != null) {                String num = mCustomer.mobile;
                if (!TextUtils.isEmpty(num)) {                    ishowCharge = true;
                }
                mCurrentPayMode = PayModeId.MEMBER_CARD;                mValueCardBalance = mPaymentInfo.getMemberCardBalance();
                mIntegralBalance = mPaymentInfo.getMemberIntegral();
                Sex sex = ValueEnums.toEnum(Sex.class, mCustomer.sex);
                String name = mCustomer.customerName;
                showLoginSuccessView(mValueCardBalance, name, sex, ishowCharge);
            } else if (mecCard != null) {

                switch (mecCard.getCardType()) {

                    case CUSTOMER_ENTITY_CARD:
                        if (mecCard.getCardKind() != null && mecCard.getCardKind().getWorkStatus() == WorkStatus.AVAILABLE_AFTER_SALE) {                             ishowCharge = false;
                        } else {
                            ishowCharge = true;                        }
                        mCurrentPayMode = PayModeId.ENTITY_CARD;                        mValueCardBalance = mPaymentInfo.getMemberCardBalance();
                        mIntegralBalance = mPaymentInfo.getMemberIntegral();
                        break;
                    case ANONYMOUS_ENTITY_CARD:
                        ishowCharge = false;                        mCurrentPayMode = PayModeId.ANONYMOUS_ENTITY_CARD;                        mValueCardBalance = mPaymentInfo.getMemberCardBalance();
                        mIntegralBalance = mPaymentInfo.getMemberIntegral();
                                                isAnoCardSinglePay = isAnonCardSinglePay();
                        break;
                    default:
                        break;
                }
                String name = mecCard.getCardKind().getCardKindName();
                showLoginSuccessView(mValueCardBalance, name, null, ishowCharge);
            }
            updateNotPayMent();
        }
        this.registerEventBus();
        setVewEvents();
                if (mCurrentPayMode == PayModeId.MEMBER_CARD && mPaymentInfo.getCustomer() != null && mPaymentInfo.getCustomer().customerLoginType == CustomerLoginType.WECHAT_MEMBERCARD_ID) {
            mCodePayTV.setVisibility(View.INVISIBLE);
        } else if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_WRITEOFF) {            mCodePayTV.setVisibility(View.INVISIBLE);
        }


    }


    public void updateNotPayMent() {
        double restAmout = mPaymentInfo.getActualAmount() - getInputValue();
        if (restAmout == 0) {
            mPayAlterTV.setVisibility(View.INVISIBLE);
        } else {
            if (restAmout > 0) {                mPayAlterTV.setText(this.getActivity().getString(R.string.pay_rest_payment_text)
                        + CashInfoManager.formatCash(restAmout));
            } else {                 mPayAlterTV.setText(String.format(this.getActivity().getString(R.string.more_pay_cash_text),
                        CashInfoManager.formatCash(0 - restAmout)));
            }
            mPayAlterTV.setVisibility(View.VISIBLE);
        }
        DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());        if (isAnoCardSinglePay) {
            DoPayUtils.updatePayEnable(getActivity(), mPay, anonCardPayEnable());
        }
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

        public double getInputValue() {
        String inputValueStr = mCashPayEditValue.getText().toString();
        return DoPayUtils.formatInputCash(inputValueStr);
    }



        private void showInfoViews() {
        mUserExit.setVisibility(View.VISIBLE);
                PayDisplayUtil.updateCashUserInfo(getActivity(), true, mPaymentInfo);
    }


    private void setVewEvents() {
        mCashPayEditValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    boolean ismodify = false;
                    String content = s.toString();
                    String valueStr = null;

                    if (!TextUtils.isEmpty(content)) {
                        mCashPayEditValue.setSelection(content.length());
                        mCashPayDeleteCash.setVisibility(View.VISIBLE);
                        if (content.startsWith(".")) {
                            content = "0" + content;
                            ismodify = true;
                        }
                        String symbol = ShopInfoCfg.getInstance().getCurrencySymbol();
                        if (!content.startsWith(symbol)) {
                            content = ShopInfoCfg.formatCurrencySymbol(content);
                            ismodify = true;
                        }
                        valueStr = content.replace(symbol, "").trim();
                        if (TextUtils.isEmpty(valueStr)) {                            content = "";
                            ismodify = true;
                        } else if (!CashInfoManager.isMatchCashFormat(valueStr)) {
                            content = content.substring(0, content.length() - 1);
                            ismodify = true;
                        } else {
                            double value = Double.valueOf(valueStr);
                            if (value > mValueCardBalance) {                                content = content.substring(0, content.length() - 1);
                                ismodify = true;
                                ToastUtil.showLongToastCenter(getActivity(), dictNotEnough_alter);

                            } else if (value > mPaymentInfo.getActualAmount()) {

                                content = content.substring(0, content.length() - 1);
                                ismodify = true;

                                ToastUtil.showLongToastCenter(getActivity(), dictInputMore_alter);
                            }
                                                    }
                        if (ismodify == true) {
                            mCashPayEditValue.setText(content);
                        } else {
                            updateNotPayMent();                        }
                    } else {
                        mCashPayDeleteCash.setVisibility(View.INVISIBLE);
                        updateNotPayMent();                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        });
    }

        private void autoInputValue() {
        if (mValueCardBalance > mPaymentInfo.getActualAmount()) {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
        } else {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mValueCardBalance));
        }
        mNumberKeyBorad.setDefaultValue(true);
        mNumberKeyBorad.setEnabled(true);
        DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
    }

    @Override
    public void onResume() {
               super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
                        autoInputValue();
            updateNotPayMent();
            refreshMemberPrice();
            batchMemberChargePrivilege();
        } else {
                        clearInputData();
            cleanMemberChargePrivilege();
            notifyRefreshUI();

        }
        super.onHiddenChanged(hidden);
    }

    @Touch({R.id.cash_pay_edit_value})
    public boolean onTouch(View arg0, MotionEvent arg1) {

        switch (arg0.getId()) {

            case R.id.cash_pay_edit_value:
                mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
                arg0.requestFocus();
                return true;
            default:
                break;
        }
        return false;
    }

    IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOK, int statusCode) {
            try {
                if (isOK) {
                    mNumberKeyBorad.setEnabled(true);
                    mCashPayEditValue.setText("");
                                    } else {
                    mNumberKeyBorad.setEnabled(true);
                    DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };


    @Click({R.id.pay, R.id.member_pay_delete_dict,
            R.id.show_value, R.id.member_pay_user_exit, R.id.bt_to_charge, R.id.member_pay_code_pay_bt

    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                if (!ClickManager.getInstance().isClicked()) {




                    if (doPayChecked(false)) {
                        mPay.setEnabled(false);
                        mNumberKeyBorad.setEnabled(false);
                        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_STORE);
                        this.mPaymentInfo.getOtherPay().clear();
                        this.mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItem());                                                preparePay();
                    }
                }
                break;

            case R.id.member_pay_delete_dict:
                clearEditText(mCashPayEditValue);
                break;

            case R.id.member_pay_user_exit:

                mCustomer = null;
                mecCard = null;
                mAmountEditedEvent.setMethodName(getResources().getString(R.string.pay_balance_st));



                if ((mPaymentInfo.getTradeVo().getTrade().getId() == null || mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT) && !mPaymentInfo.isDinner()) {
                    boolean isSelected = SharedPreferenceUtil.getSpUtil().getBoolean(SettingConstant.MEMBER_AUTO_PRIVILEGE, false);
                    if (isSelected || isNeedMemberPrice()) {
                        CustomerManager.getInstance().setLoginCustomer(null);
                        CustomerManager.getInstance().setAccounts(null);
                        ShoppingCart.getInstance().setFastFoodCustomer(null);
                        ShoppingCart.getInstance().removeAllGiftPrivilege();
                                                ShoppingCart.getInstance().removeCouponPrivilege();                        ShoppingCart.getInstance().removeIntegralCash();                        ShoppingCart.getInstance().removeMemberPrivilege();                        ShoppingCart.getInstance().checkMarketActivity();

                        RefreshTradeVoEvent event = new RefreshTradeVoEvent();
                        event.setLogin(false);
                        event.setTradeVo(ShoppingCart.getInstance().createFastFoodOrder(false));
                        EventBus.getDefault().post(event);
                    }
                }


                EventBus.getDefault().post(mAmountEditedEvent);
                EventBus.getDefault().post(new MemberLoginEvent(true));
                break;
            case R.id.bt_to_charge:
                                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_MEMBER_LOGIN_CHARGE);
                showCharingBalance(mPaymentInfo);                break;

            case R.id.member_pay_code_pay_bt:
                if (this.mPaymentInfo.isNeedToPayDeposit()) {
                    PayDepositPromptDialog.start(this.getActivity(), mDoPayApi, this.mPaymentInfo);
                } else if (this.isNeedToDeductionEarnest()) {
                    this.showBookingDeductionDialog();                } else {
                    double inputAmount = getInputValue();
                    if (inputAmount > 0) {
                        PayModelItem payModelItem = new PayModelItem(PayModeId.MEMBER_CARD);                        payModelItem.setUsedValue(BigDecimal.valueOf(inputAmount));
                        OnlinePayDialog payDialog = OnlinePayDialog.newInstance(mDoPayApi, mPaymentInfo, payModelItem, IPayConstParame.ONLIEN_SCAN_TYPE_ACTIVE);
                        payDialog.setOnPayStopListener(null);
                        payDialog.show(getFragmentManager(), "OnlinePayDialog");
                    } else {
                        ToastUtil.showShortToast(R.string.pay_please_input_amount);
                    }
                }
                break;

            default:
                break;
        }
    }



    private void clearEditText(EditText input) {

        if (input != null && !TextUtils.isEmpty(input.getText())) {
            input.setText("");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        this.unregisterEventBus();
        super.onDestroy();
        cleanMemberChargePrivilege();
    }


    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
                        autoInputValue();
            updateNotPayMent();
        }
    }


    private void showUserNameOnMneu(String username, Sex sex) {
        if (username != null && username.length() > 0) {
            if (sex == null) {
                username = username + "";
            } else if (sex == Sex.FEMALE) {                username = username.substring(0, 1) + getResources().getString(R.string.pay_user_female);
            } else {
                username = username.substring(0, 1) + getResources().getString(R.string.pay_user_male);
            }
            mAmountEditedEvent.setMethodName(username);

        } else {
            mAmountEditedEvent.setMethodName(getResources().getString(R.string.pay_user_no_name));

        }
        EventBus.getDefault().post(mAmountEditedEvent);
    }


    private void showCharingBalance(IPaymentInfo paymentInfo) {
        if (paymentInfo != null && paymentInfo.getCustomer() != null && paymentInfo.getEcCard() == null) {            showChargingDialog(mCustomer, null, String.valueOf(mValueCardBalance));
        } else if (paymentInfo != null && paymentInfo.getCustomer() == null && paymentInfo.getEcCard() != null) {            EcCard ecCard = paymentInfo.getEcCard();
            EcCardInfo ecCardInfo = EcCardInfo.toEcCard(ecCard);            showChargingDialog(paymentInfo.getCardCustomer(), ecCardInfo, String.valueOf(mValueCardBalance));
        } else {
            ToastUtil.showLongToast(R.string.customer_get_card_info_fail);
        }
    }


    private void showChargingDialog(CustomerResp customer, EcCardInfo ecCard, String balance) {
        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_MEMBER_PAY);        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        if (ecCard != null) {
            args.putSerializable(CustomerChargingDialogFragment.KEY_ECCARD, ecCard);
        }
        args.putString(CustomerChargingDialogFragment.KEY_BALANCE, balance);
        dialogFragment.setArguments(args);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "ecCardCharging");
    }

    private void gotoChargingBalance() {

    }


    public void onEventMainThread(MemberPayChargeEvent memberPayChargeEvent) {

        mValueCardBalance = memberPayChargeEvent.getmValueCardBalance().doubleValue();
        Log.d("DEBUG", "DEBUG----onEventMainThread-----mValueCardBalance=" + mValueCardBalance);
                if (memberPayChargeEvent.getType() == MemberPayChargeEvent.BALANCE_CHANGE_TYPE_CHARGE) {
            mPaymentInfo.setMemberCardBalance(mValueCardBalance);
        }
        if (mCustomer != null) {
            Sex sex = ValueEnums.toEnum(Sex.class, mCustomer.sex);
            String name = mCustomer.customerName;
            showLoginSuccessView(mValueCardBalance, name, sex, true);
        } else if (mecCard != null) {
            boolean ishowCharge = false;            switch (mecCard.getCardType()) {
                case CUSTOMER_ENTITY_CARD:
                    ishowCharge = true;
                    break;
                case ANONYMOUS_ENTITY_CARD:

                    ishowCharge = false;                    break;
                default:
                    break;
            }
            String name = mecCard.getCardKind().getCardKindName();
            showLoginSuccessView(mValueCardBalance, name, null, ishowCharge);
        }
    }


    private void showLoginSuccessView(double mValueCardBalance, String username, Sex sex, boolean isshow) {
        showInfoViews();

        if (isshow) {
            btcharge.setVisibility(View.GONE);
        } else {
            btcharge.setVisibility(View.GONE);
        }
        if (ServerSettingCache.getInstance().isJinChBusiness()) {
            btcharge.setVisibility(View.GONE);
        }
        String dictStr = String.format(getResources().getString(R.string.pay_member_use_dict_sum),
                CashInfoManager.formatCash(mValueCardBalance));
                if (isNotSufficientFunds()) {
            dictStr = dictStr.concat(" ").concat(getString(R.string.pay_dict_not_enough));
        }
        if (dictStr != null && dictStr.contains(ShopInfoCfg.getInstance().getCurrencySymbol())) {
            SpannableStringBuilder builder =
                    new SpannableStringBuilder(dictStr);
            builder.setSpan(new ForegroundColorSpan(Color.RED),
                    dictStr.indexOf(ShopInfoCfg.getInstance().getCurrencySymbol()),
                    dictStr.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mDictSum.setText(builder);
        }
                autoInputValue();
        mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
        mCashPayDeleteCash.setVisibility(View.VISIBLE);
        showUserNameOnMneu(username, sex);        updateNotPayMent();
    }

    private PayModelItem getPayModelItem() {
        PayModelItem item = new PayModelItem(this.mCurrentPayMode);
        item.setUsedValue(BigDecimal.valueOf(getInputValue()));
        return item;
    }


    private void payClick() {
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
        CustomerResp customerNew = mPaymentInfo.getCustomer() == null ? mPaymentInfo.getCardCustomer() : mPaymentInfo.getCustomer();
        EcCard ecCard = mPaymentInfo.getEcCard();
                if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_WRITEOFF || mPaymentInfo.isDinner() || EmptyUtils.isEmpty(customerNew) || EmptyUtils.isEmpty(tradeVo) || EmptyUtils.isEmpty(tradeVo.getTrade())) {
            mDoPayApi.doPay(MemberPayFragment.this.getActivity(), MemberPayFragment.this.mPaymentInfo, mPayOverCallback);
            return;
        }
                Trade trade = tradeVo.getTrade();

        TradeCustomerConvert customerConvert = TradeCustomerConvert.newInstance();
        List<TradeCustomer> customerList = tradeVo.getTradeCustomerList();
        if (customerList == null) {
            customerList = new ArrayList<>();
        }
        customerList.add(customerConvert.buildTradeCustomer(trade.getId(), trade.getUuid(), customerNew, ecCard, CustomerType.PAY));
        tradeVo.setTradeCustomerList(customerList);

        if (!mPaymentInfo.isOrdered() && !mPaymentInfo.isOrderCenter()) {
            mDoPayApi.doPay(MemberPayFragment.this.getActivity(), MemberPayFragment.this.mPaymentInfo, mPayOverCallback);
            return;
        }
        if (Snack.isOfflineTrade(tradeVo)) {
            mDoPayApi.doPay(MemberPayFragment.this.getActivity(), MemberPayFragment.this.mPaymentInfo, mPayOverCallback);
            return;
        }
                TradeModifyOperates tradeModifyOperates = TradeModifyOperates.newInstance();
        tradeModifyOperates.requestModifyCustomer(trade, customerNew, ecCard, new ResponseListener<ModifyCustomerResp>() {
            @Override
            public void onResponse(ResponseObject<ModifyCustomerResp> response) {
                if (ResponseObject.isOk(response)) {
                    mDoPayApi.doPay(MemberPayFragment.this.getActivity(), MemberPayFragment.this.mPaymentInfo, mPayOverCallback);
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        });
    }

        private void preparePay() {

        if (mCurrentPayMode == PayModeId.MEMBER_CARD) {                        if (mPaymentInfo.getCustomer() != null) {
                String num = mPaymentInfo.getCustomer().mobile;
                Long customerId = mPaymentInfo.getCustomer().customerId;
                if (TextUtils.isEmpty(num) && customerId != null && customerId != 0) {                    mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
                } else {
                                        if (ServerSettingCache.getInstance().isNeedMemberPayPWDVerify()) {
                        showPasswordDialog(num, mPaymentInfo, this.getActivity(), mPayOverCallback);
                    } else {
                        payClick();
                    }
                                    }
            } else {
                ToastUtil.showShortToast(R.string.pay_user_not_login);
            }
        } else if (mCurrentPayMode == PayModeId.ENTITY_CARD) {                        if (mPaymentInfo.getEcCard().getCardKind().getIsNeedPwd() == Bool.YES && mPaymentInfo.getEcCard().getCustomer() != null) {
                String num = mPaymentInfo.getEcCard().getCustomer().getMobile();
                                showPasswordDialog(num, mPaymentInfo, this.getActivity(), mPayOverCallback);
            } else {
                payClick();
            }

        } else if (mCurrentPayMode == PayModeId.ANONYMOUS_ENTITY_CARD) {                        mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
        }
    }

        private void showPasswordDialog(final String phonenum, final IPaymentInfo cashInfo, final FragmentActivity context, final IPayOverCallback callback) {

        final PasswordDialog dialog = new PasswordDialog(context) {
            @Override
            public void close() {
                super.close();
                callback.onFinished(false, 0);
            }
        };
        String membeName;
        if (cashInfo.getCustomer() != null && cashInfo.getEcCard() == null) {
            membeName = cashInfo.getCustomer().customerName;
        } else if (cashInfo.getCustomer() == null && cashInfo.getEcCard() != null) {
            membeName = cashInfo.getEcCard().getCustomer().getName();
        } else {
            membeName = getString(R.string.customer_sex_unknown);
        }

        dialog.setMembeName(membeName);
        dialog.setLisetner(new PasswordDialog.PasswordCheckLisetner() {

            @Override
            public void checkPassWord(String password) {
                password = Password.create().generate(phonenum, password);
                doVerifypassword(phonenum, password, cashInfo, context, callback, dialog, null);
            }

            @Override
            public void showPassWord(String password) {

            }

            @Override
            public void showReadKeyBord() {

                if (!PosConnectManager.isPosConnected()) {
                    ToastUtil.showLongToastCenter(context, context.getString(R.string.pay_pos_connection_closed));
                    return;
                }
                final ReadKeyboardDialogFragment dialogFragment =
                        new ReadKeyboardDialogFragment.ReadKeyboardFragmentBuilder().build();
                ReadKeyboardDialogFragment.CardOvereCallback cardOvereCallback = new ReadKeyboardDialogFragment.CardOvereCallback() {

                    @Override
                    public void onSuccess(String keybord) {

                        String password = keybord.toUpperCase(Locale.getDefault());

                        doVerifypassword(phonenum, password.trim(), cashInfo, context, callback, dialog, dialogFragment);
                    }

                    @Override
                    public void onFail(NewLDResponse ldResponse) {

                    }
                };
                dialogFragment.setPosOvereCallback(cardOvereCallback);

                dialogFragment.show(context.getSupportFragmentManager(), "ReadKeyboardDialog");
            }
        });
        dialog.show();

    }

        private void doVerifypassword(String phonenum, final String password, final IPaymentInfo cashInfo, final FragmentActivity context,
                                  final IPayOverCallback callback, final PasswordDialog dialog, final ReadKeyboardDialogFragment keyboardDialog) {
        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_STORE);
        if (phonenum != null) {
            final String mobile = phonenum;
            CustomerLoginReq loginReq = new CustomerLoginReq();
            loginReq.setLoginType(CustomerLoginType.MOBILE);
                        loginReq.setLoginId(mobile);
            loginReq.setIsNeedPwd(true);
            loginReq.setPassword(password);

            if (mCurrentPayMode == PayModeId.MEMBER_CARD) {
                loginReq.setCountry(mPaymentInfo.getCustomer().country);
                loginReq.setNation(mPaymentInfo.getCustomer().nation);
                loginReq.setNationalTelCode(mPaymentInfo.getCustomer().nationalTelCode);
            } else if (mCurrentPayMode == PayModeId.ENTITY_CARD) {
                loginReq.setCountry(mPaymentInfo.getEcCard().getCustomer().getCountry());
                loginReq.setNation(mPaymentInfo.getEcCard().getCustomer().getNation());
                loginReq.setNationalTelCode(mPaymentInfo.getEcCard().getCustomer().getNationalTelCode());
            }

            CustomerOperates customerOperate = OperatesFactory.create(CustomerOperates.class);
            customerOperate.customerLogin(loginReq, LoadingYFResponseListener.ensure(new YFResponseListener<YFResponse<CustomerLoginResp>>() {

                @Override
                public void onResponse(YFResponse<CustomerLoginResp> response) {
                    if (YFResponse.isOk(response)) {
                        ToastUtil.showShortToast(R.string.veryfy_password_success);
                        CustomerLoginResp content = response.getContent();
                        content.setPhoneNumber(mobile);                        cashInfo.setMemberResp(content);
                                                cashInfo.setMemberPassword(password);                        cashInfo.setCustomerId(content.getCustomerId());
                                                                        payClick();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        EventBus.getDefault().post(new EventReadKeyboard(true, ""));                    } else {
                        String msg = "";

                        msg = response.getMessage();
                        ToastUtil.showLongToastCenter(context, msg);
                                                if (dialog != null) {
                            dialog.clean();                        }

                        EventBus.getDefault().post(new EventReadKeyboard(false, msg));

                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showLongToastCenter(context, error.getMessage());
                    EventBus.getDefault().post(new EventReadKeyboard(false, error.getMessage()));                }

            }, context.getSupportFragmentManager()));
        } else {
            ToastUtil.showShortToast(R.string.pay_member_login_please);
        }
    }


    private boolean isAnonCardSinglePay() {
        PaymentMenuTool menuTool = new PaymentMenuTool(getActivity(), BusinessType.SNACK);
        menuTool.isBuildEmpty(false);
        List<PayMethodItem> payMethodItems = menuTool.initMethodList();
        int count = 0;
        if (payMethodItems != null) {
            count = payMethodItems.size();
        }
        return mCurrentPayMode == PayModeId.ANONYMOUS_ENTITY_CARD && count == 1;
    }


    private boolean anonCardPayEnable() {
        return mValueCardBalance >= mPaymentInfo.getActualAmount();
    }


    private boolean isNotSufficientFunds() {
        return isAnoCardSinglePay && !anonCardPayEnable();
    }


    private boolean isNeedMemberPrice() {
        return CustomerManager.getInstance().isOpenPriceLimit(getCustomerType())
                && ShoppingCart.getInstance().isHaveMemberPrivilege(false);
    }

    private CustomerType getCustomerType() {
        CustomerResp customerNew = CustomerManager.getInstance().getLoginCustomer();
        if (customerNew != null) {
            if (customerNew.card != null) {
                return CustomerType.CARD;
            } else if (customerNew.isMember()) {
                return CustomerType.MEMBER;
            }
        }

        return CustomerType.__UNKNOWN__;
    }


    private void cleanMemberChargePrivilege(){
        if(ServerSettingCache.getInstance().isChargePrivilegeWhenPay()){
            DinnerShoppingCart.getInstance().removeChargePrivilege(true,true);
        }
    }

    private void notifyRefreshUI(){
        RefreshTradeVoEvent event = new RefreshTradeVoEvent();
        event.setLogin(true);
        event.setTradeVo(DinnerShoppingCart.getInstance().getShoppingCartVo().getmTradeVo());
        EventBus.getDefault().post(event);
    }


    private void batchMemberChargePrivilege(){
        DinnerShopManager.getInstance().setLoginCustomer(mCustomer);
        DinnerShoppingCart.getInstance().batchMemberChargePrivilege(true, false);

        notifyRefreshUI();
    }

    private void refreshMemberPrice() {
                boolean isSelect = SharedPreferenceUtil.getSpUtil().getBoolean(SettingConstant.MEMBER_AUTO_PRIVILEGE, false);
        if (isSelect && mCustomer != null) {
            mCustomer.queryLevelRightInfos();
            CustomerManager.getInstance().setLoginCustomer(mCustomer);
            TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(mCustomer);
            if (!mCustomer.isMember()) {
                tradeCustomer.setCustomerType(CustomerType.CUSTOMER);
            } else if (mCustomer.card == null) {
                tradeCustomer.setCustomerType(CustomerType.PAY);
            } else {
                tradeCustomer.setCustomerType(CustomerType.CARD);
                tradeCustomer.setEntitycardNum(mCustomer.card.getCardNum());
            }

            ShoppingCart.getInstance().setFastFoodCustomer(tradeCustomer);
            ShoppingCart.getInstance().memberPrivilege();
            RefreshTradeVoEvent event = new RefreshTradeVoEvent();
            event.setLogin(false);
            event.setTradeVo(ShoppingCart.getInstance().createFastFoodOrder(false));
            EventBus.getDefault().post(event);
        }
    }
}
