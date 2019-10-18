package com.zhongmei.bty.customer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.zhongmei.ZMIntent;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.beauty.customer.BeautyNumberInputdialog;
import com.zhongmei.beauty.dialog.BeautyCustomerWaiterDialog;
import com.zhongmei.beauty.entity.UserVo;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.customer.bean.ChargingPrint;
import com.zhongmei.bty.basemodule.customer.bean.PayMethod;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo.RechargeRuleDetailVo;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.customer.enums.FullSend;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.MemberRechargeReq;
import com.zhongmei.bty.basemodule.customer.message.RechargeReq;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.customer.util.CustomerUtil;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardKind;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.SaleCardDataModel;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.AnonymousCardStoreResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardRechargeReq;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardKindStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.VerifyPayResp;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.CardRechagingStatus;
import com.zhongmei.bty.commonmodule.database.enums.SendType;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog.DotType;
import com.zhongmei.bty.customer.adapter.ChargeMoneyToDialogAdapter;
import com.zhongmei.bty.customer.event.EventDinnerCustomerRegisterRecharge;
import com.zhongmei.bty.customer.event.EventRefreshBalance;
import com.zhongmei.bty.customer.event.EventSaleCardCharging;
import com.zhongmei.bty.customer.views.ListConfirmDialogFragment;
import com.zhongmei.bty.customer.views.ListConfirmDialogFragment_;
import com.zhongmei.bty.customer.vo.ChargeMoneyVo;
import com.zhongmei.bty.mobilepay.event.MemberPayChargeEvent;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;
import com.zhongmei.bty.pay.utils.PayUtils;
import com.zhongmei.bty.splash.login.DoubleUserDialog;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.ValueEnums;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;



@EFragment(R.layout.customer_charging_dialog)
public class CustomerChargingDialogFragment extends BasicDialogFragment implements OnClickListener {

    public static final String TAG = CustomerChargingDialogFragment.class.getSimpleName();

    public static final String FLAG = "Charging";


    private final static int CASHCHARGE = 0;


    private final static int BANKCARDCHARGE = 1;

    @ViewById(R.id.customer_cash_charging)
    protected Button mBtnCharging;

    @ViewById(R.id.show_value)
    protected TextView mShowValue;

    @ViewById(R.id.rule_money)
    protected TextView mRuleMoneyTx;

    @ViewById(R.id.customer_balance)
    protected TextView mCustomerBalance;
    @ViewById(R.id.customer_balance_title)
    protected TextView mCustomerBalanceTitle;

    @ViewById(R.id.customer_name)
    protected TextView mName;


    @ViewById(R.id.customer_charging_num)
    protected GridView mRuleListView;

    @ViewById(R.id.salesman_name_et)
    protected EditText etSalesman;
    private CustomerResp mCustomer;

    private ListConfirmDialogFragment mConfirmDialog;
    private EcCardInfo ecCard;

        private String mBalance;

    private String serverId;

    private int chargeType = CASHCHARGE;


    private RechargeRuleVo ruleVo;


    private List<ChargeMoneyVo> chargeMoneyList;

    private BigDecimal chargeMoney = BigDecimal.ZERO;


    private BigDecimal sendMoney = BigDecimal.ZERO;

    private int from;
    public interface OnChargingClickListener {
        void onClickClose();
    }

    public static final int FROM_MEMBER_PAY = 0;    public static final int FROM_MEMBER_CUSTOMER = 1;    public static final int FROM_CREATE_CUSTOMER = 2;    public static final int FROM_CARD_SALE = 3;    public static final int FROM_CARD_BEATY_MAIN = 4;

    public static final String KEY_CUSTOMER = "key_customer";
    public static final String KEY_FROM = "key_form";
    public static final String KEY_ECCARD = "key_ecCard";
    public static final String KEY_BALANCE = "key_balance";

    public void show(FragmentManager manager, String tag) {
        if (manager != null && !manager.isDestroyed()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    private OnChargingClickListener mOnChargingClickListener;

    public void setOnClickListener(OnChargingClickListener onChargingClickListener) {
        this.mOnChargingClickListener = onChargingClickListener;
    }

    @AfterViews
    void initView() {
        setupView();
        setupWindow();
        serverId = SystemUtils.genOnlyIdentifier();
        mShowValue.setTag(FLAG);
        mRuleListView.setOnItemClickListener(null);
        getArgumentsData();
        refreshInfo();
        registerEventBus();
    }


    private void setupView() {
        if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
            mBtnCharging.setBackgroundResource(R.drawable.beauty_customer_charging_pay_cash_selector);
        } else {
            mBtnCharging.setBackgroundResource(R.drawable.customer_charging_pay_cash_selector);
        }
    }

    private void setupWindow() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
    }


    public void getArgumentsData() {
        this.from = getArguments().getInt(KEY_FROM);
        this.mCustomer = (CustomerResp) getArguments().getSerializable(KEY_CUSTOMER);
        this.mBalance = getArguments().getString(KEY_BALANCE);
        this.ecCard = (EcCardInfo) getArguments().getSerializable(KEY_ECCARD);
    }

    public String getBalance() {
        if (TextUtils.isEmpty(mBalance) || "null".equals(mBalance)) {
            return "0";
        }
        return mBalance;
    }

    @UiThread
    void refreshInfo() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        if (ecCard != null && ecCard.getCardType() == EntityCardType.CUSTOMER_ENTITY_CARD.value()) {             mName.setText(String.format(getResources().getString(R.string.customer_charging_name), ecCard.getCardNum()));
        } else {             if (mCustomer != null) {
                mName.setText(String.format(getResources().getString(R.string.customer_charging_name), mCustomer.getCustomerName(getActivity())));
            }
        }

        mCustomerBalance.setText(ShopInfoCfg.formatCurrencySymbol(getBalance()));
        if (ecCard != null) {
            if (ecCard.getRightStatus() == CardRechagingStatus.EFFECTIVE) {
                checkRule();
            } else {
                mRuleListView.setVisibility(View.INVISIBLE);
            }
        } else {
            checkRule();
        }
    }

    @Click({R.id.ivClose_CCharging, R.id.customer_cash_charging, R.id.show_value, R.id.salesman_iv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose_CCharging:
                MobclickAgentEvent.onEvent(UserActionCode.GK010020);
                if (mOnChargingClickListener != null) {
                    mOnChargingClickListener.onClickClose();
                }
                dismissAllowingStateLoss();
                break;
            case R.id.customer_cash_charging:                MobclickAgentEvent.onEvent(UserActionCode.GK010023);
                VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_STORE,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                serverId = SystemUtils.genOnlyIdentifier();
                                if (!ClickManager.getInstance().isClicked(R.id.customer_cash_charging)) {
                                    if (TextUtils.isEmpty(serverId)) {
                                        ToastUtil.showLongToast(R.string.customer_data_error);
                                        return;
                                    }
                                    if (ecCard == null || ecCard.getCardType() == EntityCardType.GENERAL_CUSTOMER_CARD.value()) {                                         if (TextUtils.isEmpty(mCustomer.mobile)) {
                                            ToastUtil.showLongToast(R.string.customer_bind_pohone);
                                            return;
                                        }
                                    }
                                    chargeType = CASHCHARGE;
                                    PreCharging();
                                }
                            }
                        });
                break;
            case R.id.show_value:
                showNumberInputDialog();
                break;
            case R.id.salesman_iv:                 if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                    showBeautyWaiterDialog();
                } else {
                    List<User> allUserList = Session.getFunc(UserFunc.class).getUsers();
                    DoubleUserDialog userDialog = new DoubleUserDialog(getContext(), R.string.salesman_select_hint2, allUserList, mSalesmans, mSelectedListener);
                    userDialog.show();
                }
                break;
            default:
                break;
        }
    }

        private List<AuthUser> mSalesmans = new ArrayList<>();
    DoubleUserDialog.DoubleUserListener mSelectedListener = new DoubleUserDialog.DoubleUserListener() {

        @Override
        public void onUserSelect(List<AuthUser> authUsers) {
            MobclickAgentEvent.onEvent(UserActionCode.GK010022);
            if (mSalesmans.isEmpty()) {
                mSalesmans.clear();
                etSalesman.setText("");
            } else {
                String names = "";
                for (AuthUser user : mSalesmans) {
                    if (!TextUtils.isEmpty(names))
                        names += ",";
                    names += user.getName();
                }
                etSalesman.setText(names);
            }
        }
    };

    NumberInputdialog.InputOverListener mInputOverListener = new NumberInputdialog.InputOverListener() {
        @Override
        public void afterInputOver(String inputContent) {
            if (inputContent != null) {
                chargeMoney = new BigDecimal(inputContent);
                sendMoney = findSendMoneyByInput(chargeMoney);
                if (ruleVo != null && ruleVo.getIsFullSend() == FullSend.YES && sendMoney.compareTo(BigDecimal.ZERO) > 0) {
                    mRuleMoneyTx.setText(getString(R.string.customer_charging_rule_money, sendMoney));
                } else {
                    mRuleMoneyTx.setText("");                }
                mShowValue.setText(ShopInfoCfg.formatCurrencySymbol(inputContent));
            }
        }
    };


    private BigDecimal findSendMoneyByInput(BigDecimal inputChargeMoney) {
        if (inputChargeMoney == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal _sendMoney = BigDecimal.ZERO;
        if (chargeMoneyList != null && !chargeMoneyList.isEmpty()) {
            for (ChargeMoneyVo vo : chargeMoneyList) {
                if (!vo.isAuto() && vo.getFullMoney() != null && inputChargeMoney.compareTo(vo.getFullMoney()) >= 0) {
                    if (vo.getSendType() == SendType.RATIO) {
                        _sendMoney = vo.getSendRate().multiply(chargeMoney).divide(new BigDecimal(100));
                    } else {
                        _sendMoney = vo.getSendMoney();
                    }
                }
            }
        }
        return _sendMoney;
    }


    private void showNumberInputDialog() {
        double maxValue = 999999d;
        String defaultInput = null;
        NumberInputdialog numberDialog;
        if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
            numberDialog = new BeautyNumberInputdialog(this.getActivity(), getString(R.string.custom_recharge), getString(R.string.enter_recharge_amount), defaultInput, maxValue, mInputOverListener);
        } else {
            numberDialog = new NumberInputdialog(this.getActivity(), getString(R.string.custom_recharge), getString(R.string.enter_recharge_amount), defaultInput, maxValue, mInputOverListener);
        }
        numberDialog.setNumberType(NumberInputdialog.NUMBER_TYPE_FLOAT).setDotType(DotType.DOT).show();
    }


    private void gotoPayRecharge() {
        long customerId = mCustomer.customerId;
        TradeVo tradeVo = null;
        SaleCardDataModel saleCard = new SaleCardDataModel(OperatesFactory.create(CustomerDal.class));
                tradeVo = saleCard.createMemberRechargeTradeVo(chargeMoney, BusinessType.ONLINE_RECHARGE, CustomerApplication.mCustomerBussinessType);

        Trade trade = tradeVo.getTrade();
        TradeCustomer tradeCustomer = new TradeCustomer();
        tradeCustomer.validateUpdate();
        tradeCustomer.setUuid(SystemUtils.genOnlyIdentifier());
        tradeCustomer.setTradeId(trade.getId());
        tradeCustomer.setTradeUuid(trade.getUuid());
        tradeCustomer.setCustomerType(CustomerType.MEMBER);
        tradeCustomer.setCustomerId(customerId);
        tradeCustomer.setCustomerName(mCustomer.customerName);
        tradeCustomer.setCustomerSex(ValueEnums.toEnum(Sex.class, mCustomer.sex));
        tradeCustomer.setCustomerPhone(mCustomer.mobile);
        tradeVo.setTradeCustomerList(Arrays.asList(tradeCustomer));
                List<TradeUser> tradeUsers = new ArrayList<>();
        if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
            for (UserVo vo : mUserVos) {
                tradeUsers.add(PayUtils.creatTradeUser(tradeVo.getTrade(), vo));
            }
        } else {
            for (AuthUser user : mSalesmans) {
                tradeUsers.add(PayUtils.creatTradeUser(tradeVo.getTrade(), user));
            }
        }
        tradeVo.setTradeUsers(tradeUsers);
        ZMIntent.payRecharge(getActivity(), tradeVo, customerId, chargeMoney);
    }

        private BigDecimal getAfterBalanceValue() {
        BigDecimal value = BigDecimal.ZERO;
        String balanceStr = mCustomerBalance.getText().toString();
        if (!TextUtils.isEmpty(balanceStr)) {
            balanceStr = balanceStr.replace(ShopInfoCfg.getInstance().getCurrencySymbol(), "");
        }
                if (chargeMoney != null) {
            value = value.add(chargeMoney);
        }
                if (sendMoney != null) {
            value = value.add(sendMoney);
        }
                if (!TextUtils.isEmpty(balanceStr)) {
            value = value.add(new BigDecimal(balanceStr));
        }
        return value;
    }

        private BigDecimal geBeforeBalanceValue() {
        BigDecimal value = BigDecimal.ZERO;
        String balanceStr = mCustomerBalance.getText().toString();
        if (!TextUtils.isEmpty(balanceStr)) {
            balanceStr = balanceStr.replace(ShopInfoCfg.getInstance().getCurrencySymbol(), "");
        }
                if (!TextUtils.isEmpty(balanceStr)) {
            value = value.add(new BigDecimal(balanceStr));
        }
        return value;
    }

    private void doChargePrint(final BigDecimal chargeMoney, final BigDecimal valueCard, EventPayResult eventPayResult) {
        printChargeOrder(mCustomer, ecCard, new BigDecimal(getBalance()), chargeMoney, valueCard, eventPayResult);
    }

    public static void printChargeOrder(CustomerResp mCustomer, EcCardInfo ecCard, BigDecimal beforeValuecard, BigDecimal chargeMoney, BigDecimal valueCard, EventPayResult eventPayResult) {
        ChargingPrint print = new ChargingPrint();
        List<PayMethod> list = new ArrayList<>(2);
        print.setCustomerName(mCustomer.customerName);
        print.setCustomerSex(mCustomer.sex + "");
        print.setPhoneNo(mCustomer.mobile);
        if (ecCard != null)
            print.setCardNum(ecCard.getCardNum());
        print.setChargingType(0);
        print.setTrueIncomeValuecard(chargeMoney);
        print.setBeforeValuecard(beforeValuecard);
        print.setCommercialName(ShopInfoCfg.getInstance().commercialName);
        print.setUserId(Session.getAuthUser().getName());
        print.setChargeValuecard(valueCard.subtract(beforeValuecard));
        print.setCustomerIntegral(null);
        print.setEndValuecard(valueCard);

        if (eventPayResult.isOnlinePay()) {
            VerifyPayResp content = (VerifyPayResp) eventPayResult.getContent();
            try {
                List<PrintOperation> printOperationsListAll = content.getPrintOperations();
                if (printOperationsListAll != null && !printOperationsListAll.isEmpty()) {
                    String tmp = printOperationsListAll.get(0).getExtendsStr();
                    JSONObject extendsStr = new JSONObject(tmp);
                    print.setPresentStart(BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0)));
                    print.setPresentEnd((BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0) + extendsStr.optDouble("currentSendValue", 0))));
                    print.setCapitalEnd(BigDecimal.valueOf(extendsStr.optDouble("valueCard", 0)).subtract(print.getPresentEnd()));
                    print.setCapitalStart(print.getCapitalEnd().subtract(chargeMoney));
                    print.setChargeValuecard(BigDecimal.valueOf(extendsStr.optDouble("currentSendValue", 0)).add(chargeMoney));
                    print.setBeforeValuecard(BigDecimal.valueOf(extendsStr.optDouble("valueCard", 0)).subtract(print.getChargeValuecard()));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            print.setChargingTime(content.getTrades().get(0).getServerUpdateTime());
            if (content.getPaymentItems() != null && !content.getPaymentItems().isEmpty()) {
                for (PaymentItem item : content.getPaymentItems()) {
                    if (item.getStatusFlag() == StatusFlag.VALID && TradePayStatus.PAID.equals(item.getPayStatus())) {
                        String name = TextUtils.isEmpty(item.getPayModeName()) ? PaySettingCache.getPayModeNameByModeId(item.getPayModeId()) : item.getPayModeName();
                        PayMethod payMethod = new PayMethod(name, item.getUsefulAmount());
                        payMethod.setChangeAmount(item.getChangeAmount());
                        payMethod.setFaceAmount(item.getFaceAmount());
                        payMethod.setUsefulAmount(item.getUsefulAmount());
                        payMethod.setPaymentType(item.getPayModeId());
                        list.add(payMethod);
                    }
                }
            }
        } else {
            AnonymousCardStoreResp content = (AnonymousCardStoreResp) eventPayResult.getContent();
            try {
                List<PrintOperation> printOperationsListAll = content.getPrintOperations();
                if (printOperationsListAll != null && !printOperationsListAll.isEmpty()) {
                    String tmp = printOperationsListAll.get(0).getExtendsStr();
                    JSONObject extendsStr = new JSONObject(tmp);
                    print.setPresentStart(BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0)));
                    print.setPresentEnd((BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0) + extendsStr.optDouble("currentSendValue", 0))));
                    print.setCapitalEnd(BigDecimal.valueOf(extendsStr.optDouble("valueCard", 0)).subtract(print.getPresentEnd()));
                    print.setCapitalStart(print.getCapitalEnd().subtract(chargeMoney));
                    print.setChargeValuecard(BigDecimal.valueOf(extendsStr.optDouble("currentSendValue", 0)).add(chargeMoney));
                    print.setBeforeValuecard(BigDecimal.valueOf(extendsStr.optDouble("valueCard", 0)).subtract(print.getChargeValuecard()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            print.setChargingTime(content.getTrade().getServerUpdateTime());
            if (content.getPaymentItems() != null && !content.getPaymentItems().isEmpty()) {
                for (PaymentItem item : content.getPaymentItems()) {
                    if (item.getStatusFlag() == StatusFlag.VALID && TradePayStatus.PAID.equals(item.getPayStatus())) {
                        String name = TextUtils.isEmpty(item.getPayModeName()) ? PaySettingCache.getPayModeNameByModeId(item.getPayModeId()) : item.getPayModeName();
                        PayMethod payMethod = new PayMethod(name, item.getUsefulAmount());
                        payMethod.setChangeAmount(item.getChangeAmount());
                        payMethod.setFaceAmount(item.getFaceAmount());
                        payMethod.setUsefulAmount(item.getUsefulAmount());
                        payMethod.setPaymentType(item.getPayModeId());
                        list.add(payMethod);
                    }
                }
            }
        }
        print.setPayMethods(list);
            }

        private void clearInput() {
        chargeMoney = BigDecimal.ZERO;
        sendMoney = BigDecimal.ZERO;
        mShowValue.setText(getString(R.string.zero));
    }

        public void onEventMainThread(EventPayResult eventPayResult) {
        this.dismissAllowingStateLoss();
    }

    private void showPromptDialog(final BigDecimal chargeMoney, final BigDecimal valueCard, final BusinessType businessType) {
        String money = CashInfoManager.formatCashAmount(chargeMoney.doubleValue());         String currentBalance = CashInfoManager.formatCashAmount(valueCard.doubleValue());         Bundle data = new Bundle();
        data.putString("money", money);
        data.putString("currentBalance", currentBalance);
        if (sendMoney != null && sendMoney.compareTo(BigDecimal.ZERO) > 0) {
            data.putString("sendMoney", sendMoney.toPlainString());
        }
        data.putSerializable("customer", mCustomer);
        CustomerChargingSuccessDialog dialog = new CustomerChargingSuccessDialog_();
        dialog.setArguments(data);
        dialog.setOnPositiveListener(new CustomerChargingSuccessDialog.OnPositiveListener() {
            @Override
            public void onClickSubmit() {
                Log.d("DEBUG", "DEBUG----from=" + from);
                                if (from == FROM_MEMBER_PAY) {
                    Log.d("DEBUG", "DEBUG----from=FROM_MEMBER_PAY===" + valueCard.toString());
                    MemberPayChargeEvent memberPayChargeEvent = new MemberPayChargeEvent();
                    memberPayChargeEvent.setType(MemberPayChargeEvent.BALANCE_CHANGE_TYPE_CHARGE);                    memberPayChargeEvent.setmValueCardBalance(valueCard);
                    EventBus.getDefault().post(memberPayChargeEvent);                                        CustomerResp separatorCustomer = CustomerManager.getInstance().getSeparateLoginCustomer();
                    if (separatorCustomer != null) {
                                                if (mCustomer != null && TextUtils.equals(mCustomer.mobile, separatorCustomer.mobile)) {
                            separatorCustomer.needRefresh = (true);
                        } else if (ecCard != null && separatorCustomer.card != null && TextUtils.equals(ecCard.getCardNum(), separatorCustomer.card.getCardNum())) {
                            separatorCustomer.needRefresh = (true);
                        }
                    }
                } else if (from == FROM_CREATE_CUSTOMER) {
                    EventBus.getDefault().post(new EventDinnerCustomerRegisterRecharge(mCustomer));
                } else if (from == FROM_CARD_SALE) {
                                    } else {
                                        if (ecCard != null) {
                        EventBus.getDefault().post(new EventRefreshBalance(ecCard.getCardNum()));
                    } else {
                        EventBus.getDefault().post(new EventRefreshBalance(null));
                    }
                }
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), "customerChargingSussessDialog");
    }


    private void showConfirmDialog() {
        if (mConfirmDialog == null) {
            mConfirmDialog = ListConfirmDialogFragment_.builder().mTitleString(getString(R.string.customer_confirm)).build();
            String name = mCustomer.customerName;
            if (ecCard != null) {
                name = ecCard.getCardNum();
            }
            String phoneNumber = mCustomer.mobile;
            mConfirmDialog.setPhoneNumber(phoneNumber);
            mConfirmDialog.setAccount(name);
            mConfirmDialog.setOnConfirmClick(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    gotoPayRecharge();
                }
            });
        }
        mConfirmDialog.setChargeAmonut(chargeMoney);
        mConfirmDialog.show(getFragmentManager(), "ListConfirmDialogFragment");
    }


    private void dismissConfirmDialog() {
        if (mConfirmDialog != null)
            mConfirmDialog.dismissAllowingStateLoss();
    }

    private ChargeMoneyToDialogAdapter.OnRuleClick mOnRuleClick = new ChargeMoneyToDialogAdapter.OnRuleClick() {
        @Override
        public void OnClickListener(ChargeMoneyVo vo) {
            MobclickAgentEvent.onEvent(UserActionCode.GK010021);
            if (vo.isAuto()) {
                showNumberInputDialog();
            } else {
                chargeMoney = vo.getFullMoney();
                sendMoney = vo.getSendMoney();
                if (sendMoney != null && sendMoney.compareTo(BigDecimal.ZERO) > 0) {
                    mRuleMoneyTx.setText(getString(R.string.customer_charging_rule_money, sendMoney));
                } else {
                    mRuleMoneyTx.setText("");
                }
                mShowValue.setText(ShopInfoCfg.formatCurrencySymbol(vo.getFullMoney()));
            }
        }
    };


    private void PreCharging() {
        if (TextUtils.isEmpty(mShowValue.getText())) {
            ToastUtil.showShortToast(getString(R.string.select_recharge_amount));
            return;
        }
        if (chargeMoney.compareTo(BigDecimal.ZERO) < 0 && sendMoney.compareTo(BigDecimal.ZERO) == 0) {
            ToastUtil.showShortToast(getString(R.string.select_recharge_amount));
            return;
        }
        if (mCustomer != null && mCustomer.isDisabled()) {
            ToastUtil.showLongToast(getString(R.string.member_disable));
            return;
        }
        if (ecCard != null && ecCard.getCardStatus() != CardStatus.ACTIVATED) {
            ToastUtil.showShortToast(getString(R.string.entity_card) + CustomerUtil.getStatusName(ecCard.getCardStatus()) + getString(R.string.can_not_execute));
            return;
        }
                CustomerDal dal = OperatesFactory.create(CustomerDal.class);
        EcCardKind cardKind = null;
        try {
            if (ecCard != null)
                cardKind = dal.findEcCardKindById(ecCard.getCardKindId());
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        if (cardKind != null && cardKind.getIsValueCard() == Bool.NO) {
            ToastUtil.showShortToast(getString(R.string.card_cannot_have_the_right));
            return;
        }
        if (cardKind != null && cardKind.getCardKindStatus() != CardKindStatus.ISSUED) {
            ToastUtil.showShortToast(getString(R.string.card_not_issued_cannot_use));
            return;
        }
        gotoPayRecharge();
    }


    private MemberRechargeReq getMemberRechargeReq() {
        MemberRechargeReq req = new MemberRechargeReq();
        setValueReq(req);
        if (chargeType == CASHCHARGE) {
            req.setCashValueCard(chargeMoney);
            req.setBankValueCard(null);
        } else if (chargeType == BANKCARDCHARGE) {
            req.setBankValueCard(chargeMoney);
            req.setCashValueCard(null);
        }
        req.setSource(1);
        req.setPaymentTime(System.currentTimeMillis());
        req.setTotalValueCard(chargeMoney);
        return req;
    }


    private CardRechargeReq getCardRechargeReq() {
        CardRechargeReq req = new CardRechargeReq();
        setValueReq(req);
        req.setCardNum(ecCard.getCardNum());
        req.setAddValue(chargeMoney);
        req.setSendValue(sendMoney);
        return req;
    }


    private RechargeReq setValueReq(RechargeReq req) {
        req.setClientCreateTime(System.currentTimeMillis());
        req.setClientUpdateTime(System.currentTimeMillis());
        req.setCreatorId(Session.getAuthUser().getId());
        req.setCreatorName(Session.getAuthUser().getName());
        req.setCustomerId(mCustomer.customerId);
        req.setUpdatorId(Session.getAuthUser().getId());
        req.setUpdatorName(Session.getAuthUser().getName());
        req.setUuid(serverId);
        String tradeNo = SystemUtils.getBillNumber();
        req.setTradeNo(tradeNo);
        PaymentItem item = new PaymentItem();
        if (chargeType == CASHCHARGE) {
            item.setPayModeId(PayModeId.CASH.value());
            item.setPayModelGroup(PayModelGroup.CASH);
        } else if (chargeType == BANKCARDCHARGE) {
            item.setPayModeId(PayModeId.BANK_CARD.value());
            item.setPayModelGroup(PayModelGroup.BANK_CARD);
        }

        item.setFaceAmount(chargeMoney);
        item.setUsefulAmount(chargeMoney);
        item.setChangeAmount(BigDecimal.ZERO);
        item.setUuid(serverId);
        List<PaymentItem> paymentItems = new ArrayList<PaymentItem>();
        paymentItems.add(item);
        req.setPaymentItems(paymentItems);
        return req;
    }


    public void checkRule() {
        chargeMoneyList = new ArrayList<ChargeMoneyVo>();
        final CustomerDal dal = OperatesFactory.create(CustomerDal.class);
                TaskContext.bindExecute(this, new SimpleAsyncTask<RechargeRuleVo>() {
            @Override
            public RechargeRuleVo doInBackground(Void... params) {
                RechargeRuleVo rechargeRuleVo = null;
                try {
                    rechargeRuleVo = dal.findRechargeRule();
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return rechargeRuleVo;
            }

            public void onPostExecute(RechargeRuleVo result) {
                try {
                    ruleVo = result;
                                        if (ruleVo != null) {
                        List<RechargeRuleDetailVo> ruleList = ruleVo.getRuleDetailList();
                                                if (ruleVo.getIsFullSend() == FullSend.YES) {
                            if (ruleList != null && ruleList.size() > 0) {
                                for (RechargeRuleDetailVo rule : ruleList) {
                                    if (rule.getFullValue() != null) {
                                        ChargeMoneyVo chargeMoneyVo = new ChargeMoneyVo(false);
                                        chargeMoneyVo.setFullMoney(rule.getFullValue());
                                        if (ruleVo.getSendType() == SendType.FIXED) {
                                            chargeMoneyVo.setSendMoney(rule.getSendValue());
                                        } else {
                                            chargeMoneyVo.setSendMoney(rule.getSendRate().multiply(rule.getFullValue()).divide(new BigDecimal(100)));
                                        }
                                        chargeMoneyVo.setIsFullSend(result.getIsFullSend());                                         chargeMoneyVo.setSendType(result.getSendType());                                         chargeMoneyVo.setSendRate(rule.getSendRate());                                         chargeMoneyList.add(chargeMoneyVo);
                                    }
                                }
                            } else {
                                fixAmount();
                            }
                        } else {
                            if (ruleList != null && ruleList.size() > 0) {
                                for (RechargeRuleDetailVo rule : ruleList) {
                                    if (rule.getFullValue() != null) {
                                        ChargeMoneyVo chargeMoneyVo = new ChargeMoneyVo(false);
                                        chargeMoneyVo.setFullMoney(rule.getFullValue());
                                        chargeMoneyVo.setSendMoney(BigDecimal.ZERO);
                                        chargeMoneyList.add(chargeMoneyVo);
                                    }
                                }
                            } else {
                                fixAmount();
                            }
                        }
                    } else {
                        fixAmount();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                                ChargeMoneyToDialogAdapter adapter = new ChargeMoneyToDialogAdapter(getActivity());
                adapter.setOnRuleClick(mOnRuleClick);
                mRuleListView.setAdapter(adapter);
                adapter.setDataSource(chargeMoneyList);
            }
        });
            }


    private void fixAmount() {
        for (int i = 0; i < 4; i++) {
            ChargeMoneyVo chargeMoneyVo = new ChargeMoneyVo(false);
            switch (i) {
                case 0:
                    chargeMoneyVo.setFullMoney(new BigDecimal(50));
                    break;
                case 1:
                    chargeMoneyVo.setFullMoney(new BigDecimal(100));
                    break;
                case 2:
                    chargeMoneyVo.setFullMoney(new BigDecimal(200));
                    break;
                case 3:
                    chargeMoneyVo.setFullMoney(new BigDecimal(500));
                    break;
                default:
                    break;
            }
            chargeMoneyVo.setSendMoney(BigDecimal.ZERO);
            chargeMoneyList.add(chargeMoneyVo);
        }
    }


    private List<UserVo> mUserVos = new ArrayList<>();
    private BeautyCustomerWaiterDialog mBeautyCustomerWaiterDialog;

    private void showBeautyWaiterDialog() {
        if (mBeautyCustomerWaiterDialog == null) {
            mBeautyCustomerWaiterDialog = new BeautyCustomerWaiterDialog();
            mBeautyCustomerWaiterDialog.setOnBeautyWaiterListener(new BeautyCustomerWaiterDialog.OnBeautyWaiterListener() {
                @Override
                public void onChoiceUserListener(@Nullable List<? extends UserVo> userVos) {
                    MobclickAgentEvent.onEvent(UserActionCode.GK010022);
                    if (userVos.size() > 0) {
                        mUserVos.clear();
                        mUserVos.addAll(userVos);
                        StringBuffer buffer = new StringBuffer();
                        for (int i = 0; i < mUserVos.size(); i++) {
                            UserVo vo = mUserVos.get(i);
                            if (i == mUserVos.size() - 1) {
                                buffer.append(vo.getUser().getName());
                            } else {
                                buffer.append(vo.getUser().getName() + ",");
                            }
                        }
                        etSalesman.setText(buffer);
                    } else {
                        mUserVos.clear();
                        etSalesman.setText("");
                    }
                }
            });
        }
        mBeautyCustomerWaiterDialog.show(getChildFragmentManager(), "BeautyWaiterDialog");
    }



    private void cleaUserVo() {
        mUserVos.clear();         etSalesman.setText("");
    }
}
