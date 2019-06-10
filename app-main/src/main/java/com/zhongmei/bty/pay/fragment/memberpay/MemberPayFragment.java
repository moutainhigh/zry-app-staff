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
    TextView mPayAlterTV;//还剩多少钱

    /* @Bean(PayKeyBorad.class)*/
    PayKeyPanel mNumberKeyBorad;

    @ViewById(R.id.pay)
    Button mPay;

    //@StringRes(R.string.pay_input_cash_text)
    //String mAmountFormat;// 金额格式化字符串

    @ViewById(R.id.member_pay_user_exit)
    TextView mUserExit;

    @ViewById(R.id.cash_pay_edit_value)
    EditText mCashPayEditValue;// 余额编辑框

    @ViewById(R.id.member_pay_dict_sum)
    TextView mDictSum;// 余额

    @ViewById(R.id.bt_to_charge)
    Button btcharge;//去充值

    @ViewById(R.id.member_pay_delete_dict)
    ImageView mCashPayDeleteCash;// 删除余额

    @ViewById(R.id.member_pay_code_pay_bt)
    TextView mCodePayTV;//扫码支付按钮add v8.5.6
    private double mValueCardBalance = 0;// 会员余额

    private long mIntegralBalance;// 会员积分

    private String dictNotEnough_alter;// 余额不足提示

    private String dictInputMore_alter;// 输入余额大于未付金额提示
    /**
     * 会员
     */
    private CustomerResp mCustomer;
    /**
     * 会员卡
     */
    private EcCard mecCard;

    //add 20160714 start
    // private IPaymentInfo mPaymentInfo;
    //刷新支付菜单文本
    private AmountEditedEvent mAmountEditedEvent = new AmountEditedEvent(PayModelGroup.VALUE_CARD, "");

    //当前字符方式
    private PayModeId mCurrentPayMode;

    //private boolean mIsSuportGroupPay = true;//默认支持分步支付

    // v8.3 判断是否美食城，匿名卡单支付方式
    private boolean isAnoCardSinglePay = false;

    // private DoPayApi mDoPayApi;//add v8.11

   /* public void setIsSuportGroupPay(boolean isSuportGroupPay) {
        mIsSuportGroupPay = isSuportGroupPay;
    }
*/
   /* public void setCashInfoManager(IPaymentInfo paymentInfo) {
        this.mPaymentInfo = paymentInfo;
    }

    public void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }*/

    //and 20160714 end
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
        boolean ishowCharge = false;//是否展示去充值按钮
        if (this.mPaymentInfo != null) {
            if (!mPaymentInfo.isDinner()) {
                mIsSuportGroupPay = !isNeedMemberPrice();
            }
            mCustomer = mPaymentInfo.getCustomer();
            mecCard = mPaymentInfo.getEcCard();
            if (mCustomer != null) {// 标识已登录会员 直接显示会员余额信息
                String num = mCustomer.mobile;
                if (!TextUtils.isEmpty(num)) {//手机号码不为空显示去充值按钮
                    ishowCharge = true;
                }
                mCurrentPayMode = PayModeId.MEMBER_CARD;//会员虚拟卡
                mValueCardBalance = mPaymentInfo.getMemberCardBalance();
                mIntegralBalance = mPaymentInfo.getMemberIntegral();
                Sex sex = ValueEnums.toEnum(Sex.class, mCustomer.sex);
                String name = mCustomer.customerName;
                showLoginSuccessView(mValueCardBalance, name, sex, ishowCharge);//会员传true展示（去充值按钮）

            } else if (mecCard != null) {

                switch (mecCard.getCardType()) {

                    case CUSTOMER_ENTITY_CARD:
                        if (mecCard.getCardKind() != null && mecCard.getCardKind().getWorkStatus() == WorkStatus.AVAILABLE_AFTER_SALE) { // 8.3 新增 匿名权益卡顾客判断 ， 匿名权益卡顾客屏蔽充值按钮
                            ishowCharge = false;
                        } else {
                            ishowCharge = true;//会员实体卡显示去充值按钮
                        }
                        mCurrentPayMode = PayModeId.ENTITY_CARD;//会员实体卡
                        mValueCardBalance = mPaymentInfo.getMemberCardBalance();
                        mIntegralBalance = mPaymentInfo.getMemberIntegral();
                        break;
                    case ANONYMOUS_ENTITY_CARD:
                        ishowCharge = false;//匿名卡不显示去充值按钮
                        mCurrentPayMode = PayModeId.ANONYMOUS_ENTITY_CARD;//匿名卡支付
                        mValueCardBalance = mPaymentInfo.getMemberCardBalance();
                        mIntegralBalance = mPaymentInfo.getMemberIntegral();
                        // v8.3 美食城匿名卡支付余额不够不能分步支付
                        isAnoCardSinglePay = isAnonCardSinglePay();
                        break;
                    default:
                        break;
                }
                String name = mecCard.getCardKind().getCardKindName();
                showLoginSuccessView(mValueCardBalance, name, null, ishowCharge);//传入金额，名称，性别，当性别为null左菜单显示只显示name

            }
            updateNotPayMent();
        }
        this.registerEventBus();
        setVewEvents();
        //add v8.5.6 如果是token登录不要显示扫码支付按钮
        if (mCurrentPayMode == PayModeId.MEMBER_CARD && mPaymentInfo.getCustomer() != null && mPaymentInfo.getCustomer().customerLoginType == CustomerLoginType.WECHAT_MEMBERCARD_ID) {
            mCodePayTV.setVisibility(View.INVISIBLE);
        } else if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_WRITEOFF) {//add v8.11 销账不支持扫码支付
            mCodePayTV.setVisibility(View.INVISIBLE);
        }

        // 大客户屏蔽充值按钮
        /*if (KeyAt.getKeyAtType() == KeyAtType.YAZUO) {
            btcharge.setVisibility(View.GONE);
        }*/
    }

    /**
     * @Title: updateNotPayMent
     * @Description: 刷新未支付或找零、溢收
     * @Return void 返回类型
     */
    public void updateNotPayMent() {
        double restAmout = mPaymentInfo.getActualAmount() - getInputValue();
        if (restAmout == 0) {
            mPayAlterTV.setVisibility(View.INVISIBLE);
        } else {
            if (restAmout > 0) {//还差
                mPayAlterTV.setText(this.getActivity().getString(R.string.pay_rest_payment_text)
                        + CashInfoManager.formatCash(restAmout));
            } else { //溢收
                mPayAlterTV.setText(String.format(this.getActivity().getString(R.string.more_pay_cash_text),
                        CashInfoManager.formatCash(0 - restAmout)));
            }
            mPayAlterTV.setVisibility(View.VISIBLE);
        }
        DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());//刷新支付按钮
        if (isAnoCardSinglePay) {
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

    //获取输入金额
    public double getInputValue() {
        String inputValueStr = mCashPayEditValue.getText().toString();
        return DoPayUtils.formatInputCash(inputValueStr);
    }

  /*  //add begin 20170424 for 组合支付开关
    private boolean enablePay() {
        if (this.mIsSuportGroupPay && DoPayUtils.isSupportGroupPay(mPaymentInfo, PaySettingCache.isSupportGroupPay())) {//如果支持组合支付
            return getInputValue() > 0;
        } else {
            return getInputValue() >= mPaymentInfo.getActualAmount();//不分步支付，输入金额必须大于等于应付金额
        }
    }*/

    //add end 20170424 for 组合支付开关
    private void showInfoViews() {
        mUserExit.setVisibility(View.VISIBLE);
        // 登录成功后第二屏显示用户信息
        PayDisplayUtil.updateCashUserInfo(getActivity(), true, mPaymentInfo);
    }

    /**
     * 设置编辑框改变事件
     */
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
                        if (TextUtils.isEmpty(valueStr)) {//modify v8.11
                            content = "";
                            ismodify = true;
                        } else if (!CashInfoManager.isMatchCashFormat(valueStr)) {
                            content = content.substring(0, content.length() - 1);
                            ismodify = true;
                        } else {
                            double value = Double.valueOf(valueStr);
                            if (value > mValueCardBalance) {// 如果大于会员余额
                                content = content.substring(0, content.length() - 1);
                                ismodify = true;
                                ToastUtil.showLongToastCenter(getActivity(), dictNotEnough_alter);

                            } else if (value > mPaymentInfo.getActualAmount()) {

                                content = content.substring(0, content.length() - 1);
                                ismodify = true;

                                ToastUtil.showLongToastCenter(getActivity(), dictInputMore_alter);
                            }
                            //updateNotPayMent();//刷新余额*//* //modify 20170329
                        }
                        if (ismodify == true) {
                            mCashPayEditValue.setText(content);
                        } else {
                            updateNotPayMent();//刷新余额
                        }
                    } else {
                        mCashPayDeleteCash.setVisibility(View.INVISIBLE);
                        updateNotPayMent();//刷新余额
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        });
    }

    // 自带数据
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
       /* // 自带数据
        autoInputValue();
        updateNotPayMent();*///modify 20170329
        super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            // 自带数据
            autoInputValue();
            updateNotPayMent();
            refreshMemberPrice();
            batchMemberChargePrivilege();
        } else {
            //隐藏时清空已输入金额
            clearInputData();
            cleanMemberChargePrivilege();
          /*  DisplayServiceManager.updateDisplay(getActivity().getApplicationContext(),
                    DisplayServiceManager.buildPayMessage(DisplayUserInfo.COMMAND_CACEL, ""));*/
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
                    // DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
                } else {
                    mNumberKeyBorad.setEnabled(true);
                    DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };

    /**
     * 设置各种单击事件
     */
    @Click({R.id.pay, R.id.member_pay_delete_dict,
            R.id.show_value, R.id.member_pay_user_exit, R.id.bt_to_charge, R.id.member_pay_code_pay_bt

    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                if (!ClickManager.getInstance().isClicked()) {
                    /*if (mPaymentInfo.getActualAmount() == 0) {
                        ToastUtil.showShortToast(R.string.pay_zero_cannot_use);
                        return;
                    }*/
                    //同一订单不允许相同账号重复支付
                    //add 20170310 如果有储值支付记录，就不能清空
                    /*if (PayUtils.isMemberPay(mPaymentInfo.getPaidPaymentItems())) {
                        ToastUtil.showShortToast(R.string.pay_member_pay_repay_alter);
                        return;
                    }*/

                   /* if (this.mPaymentInfo.isNeedToPayDeposit()) {
                        PayDepositPromptDialog.start(this.getActivity(), this.mPaymentInfo);
                    } else*/
                    if (doPayChecked(false)) {
                        mPay.setEnabled(false);
                        mNumberKeyBorad.setEnabled(false);
                        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_STORE);
                        this.mPaymentInfo.getOtherPay().clear();
                        this.mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItem());//添加一种支付方式
                        //调用支付接口
                        preparePay();
                    }
                }
                break;

            case R.id.member_pay_delete_dict:
                clearEditText(mCashPayEditValue);
                break;

            case R.id.member_pay_user_exit:
                //add 20170310 如果有储值支付记录，就不能清空
                /*
                if (!PayUtils.isMemberPay(mPaymentInfo.getPaidPaymentItems())) {
                    mPaymentInfo.setEcCard(null);// 清空实体卡会员
                    mPaymentInfo.setCustomer(null);// 清空会员
                }
                */
                mCustomer = null;
                mecCard = null;
                mAmountEditedEvent.setMethodName(getResources().getString(R.string.pay_balance_st));


//没有下单和反结账都要带会员权益

                if ((mPaymentInfo.getTradeVo().getTrade().getId() == null || mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT) && !mPaymentInfo.isDinner()) {
                    boolean isSelected = SharedPreferenceUtil.getSpUtil().getBoolean(SettingConstant.MEMBER_AUTO_PRIVILEGE, false);
                    if (isSelected || isNeedMemberPrice()) {
                        CustomerManager.getInstance().setLoginCustomer(null);
                        CustomerManager.getInstance().setAccounts(null);
                        ShoppingCart.getInstance().setFastFoodCustomer(null);
                        ShoppingCart.getInstance().removeAllGiftPrivilege();
                        // 从购物车移除优惠券和积分抵现
                        ShoppingCart.getInstance().removeCouponPrivilege();// 删除优惠劵
                        ShoppingCart.getInstance().removeIntegralCash();// 删除积分
                        ShoppingCart.getInstance().removeMemberPrivilege();//
                        ShoppingCart.getInstance().checkMarketActivity();

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
                // 收银台会员充值
                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_MEMBER_LOGIN_CHARGE);
                showCharingBalance(mPaymentInfo);//跳转到充值支付界面
                break;

            case R.id.member_pay_code_pay_bt:
                if (this.mPaymentInfo.isNeedToPayDeposit()) {
                    PayDepositPromptDialog.start(this.getActivity(), mDoPayApi, this.mPaymentInfo);
                } else if (this.isNeedToDeductionEarnest()) {
                    this.showBookingDeductionDialog();//先判断是否抵扣预付金 add 8.14
                } else {
                    double inputAmount = getInputValue();
                    if (inputAmount > 0) {
                        PayModelItem payModelItem = new PayModelItem(PayModeId.MEMBER_CARD);//会员卡余额
                        payModelItem.setUsedValue(BigDecimal.valueOf(inputAmount));
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

    /**
     * @param input
     */

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
    }

    /***
     * 监听抹零事件
     *
     * @param event
     */
    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
            // 自带数据
            autoInputValue();
            updateNotPayMent();
        }
    }

    /**
     * 登录成功之后更新左菜单
     *
     * @param username
     * @param sex
     */
    private void showUserNameOnMneu(String username, Sex sex) {
        if (username != null && username.length() > 0) {
            if (sex == null) {
                username = username + "";
            } else if (sex == Sex.FEMALE) {// 如果是女性
                username = username.substring(0, 1) + getResources().getString(R.string.pay_user_female);
            } else {
                username = username.substring(0, 1) + getResources().getString(R.string.pay_user_male);
            }
            mAmountEditedEvent.setMethodName(username);

        } else {
            mAmountEditedEvent.setMethodName(getResources().getString(R.string.pay_user_no_name));

        }
        EventBus.getDefault().post(mAmountEditedEvent);
    }

    /**
     * 跳转充值支付界面
     *
     * @param paymentInfo 会员跳转
     */
    private void showCharingBalance(IPaymentInfo paymentInfo) {
        if (paymentInfo != null && paymentInfo.getCustomer() != null && paymentInfo.getEcCard() == null) {// 会员跳转
            showChargingDialog(mCustomer, null, String.valueOf(mValueCardBalance));
        } else if (paymentInfo != null && paymentInfo.getCustomer() == null && paymentInfo.getEcCard() != null) {// 实体卡跳转
            EcCard ecCard = paymentInfo.getEcCard();
            EcCardInfo ecCardInfo = EcCardInfo.toEcCard(ecCard);//转换为支付成功
//            ecCardInfo.setRemainValue(BigDecimal.valueOf(paymentInfo.getMemberCardBalance()));
            showChargingDialog(paymentInfo.getCardCustomer(), ecCardInfo, String.valueOf(mValueCardBalance));
        } else {
            ToastUtil.showLongToast(R.string.customer_get_card_info_fail);
        }
    }

    /**
     * 实体卡会员充值界面
     *
     * @param customer 顾客信息
     * @param ecCard   实体卡信息 , 会员充值 传 null
     * @param balance  余额，实体卡充值 传 null
     */
    private void showChargingDialog(CustomerResp customer, EcCardInfo ecCard, String balance) {
        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_MEMBER_PAY);//来自顾客界面
        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        if (ecCard != null) {
            args.putSerializable(CustomerChargingDialogFragment.KEY_ECCARD, ecCard);
        }
        args.putString(CustomerChargingDialogFragment.KEY_BALANCE, balance);
        dialogFragment.setArguments(args);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "ecCardCharging");
    }

    private void gotoChargingBalance() {

    }

    /**
     * 用来接收会员充值界面返回的充值后的金额
     */
    public void onEventMainThread(MemberPayChargeEvent memberPayChargeEvent) {

        mValueCardBalance = memberPayChargeEvent.getmValueCardBalance().doubleValue();
        Log.d("DEBUG", "DEBUG----onEventMainThread-----mValueCardBalance=" + mValueCardBalance);
        //add 20170217 如果是充值导致余额变动，更新消费前余额，如果是消费就不变
        if (memberPayChargeEvent.getType() == MemberPayChargeEvent.BALANCE_CHANGE_TYPE_CHARGE) {
            mPaymentInfo.setMemberCardBalance(mValueCardBalance);
        }
        if (mCustomer != null) {// 标识已登录会员 直接显示会员余额信息

            Sex sex = ValueEnums.toEnum(Sex.class, mCustomer.sex);
            String name = mCustomer.customerName;
            showLoginSuccessView(mValueCardBalance, name, sex, true);//会员展示（去充值按钮）

        } else if (mecCard != null) {
            boolean ishowCharge = false;//是否展示（去充值按钮）
            switch (mecCard.getCardType()) {
                case CUSTOMER_ENTITY_CARD:
                    ishowCharge = true;
                    break;
                case ANONYMOUS_ENTITY_CARD:

                    ishowCharge = false;//匿名卡不展示（去充值按钮）
                    break;
                default:
                    break;
            }
            String name = mecCard.getCardKind().getCardKindName();
            showLoginSuccessView(mValueCardBalance, name, null, ishowCharge);//传入金额，名称，性别，当性别为null左菜单显示只显示name

        }
    }

    /**
     * 显示登录成功后的展示的会员信息
     * isshow 是否展示去充值按钮
     */
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
        // v8.3 添加余额不足的显示
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
        //自动带入未收金额
        autoInputValue();
        mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
        mCashPayDeleteCash.setVisibility(View.VISIBLE);
        showUserNameOnMneu(username, sex);// 左菜单先生用户
        updateNotPayMent();//刷新余额

    }

    private PayModelItem getPayModelItem() {
        PayModelItem item = new PayModelItem(this.mCurrentPayMode);
        item.setUsedValue(BigDecimal.valueOf(getInputValue()));
        return item;
    }

    /**
     * 快餐更新TradeCustomer
     * v8.11.0
     */
    private void payClick() {
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
        CustomerResp customerNew = mPaymentInfo.getCustomer() == null ? mPaymentInfo.getCardCustomer() : mPaymentInfo.getCustomer();
        EcCard ecCard = mPaymentInfo.getEcCard();
        //如果是销账
        if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_WRITEOFF || mPaymentInfo.isDinner() || EmptyUtils.isEmpty(customerNew) || EmptyUtils.isEmpty(tradeVo) || EmptyUtils.isEmpty(tradeVo.getTrade())) {
            mDoPayApi.doPay(MemberPayFragment.this.getActivity(), MemberPayFragment.this.mPaymentInfo, mPayOverCallback);
            return;
        }
        // 未下单的数据组装TradeCustomer
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
        // 已下单的数据修改TradeCustomer
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

    //点击结账按钮调用
    private void preparePay() {
        /*if (KeyAt.getKeyAtType() == KeyAtType.YAZUO) {
            payClick();
            return;
        }*/
        if (mCurrentPayMode == PayModeId.MEMBER_CARD) {//虚拟会员
            // 当前会员登录是否为空
            if (mPaymentInfo.getCustomer() != null) {
                String num = mPaymentInfo.getCustomer().mobile;
                Long customerId = mPaymentInfo.getCustomer().customerId;
                if (TextUtils.isEmpty(num) && customerId != null && customerId != 0) {//此为openID登录没有支付密码，直接收银
                    mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
                } else {
                    //modify  20170913 start
//                    if (mPaymentInfo.getCustomer().isNeedPassword(BigDecimal.valueOf(getInputValue()))) {
                    if (ServerSettingCache.getInstance().isNeedMemberPayPWDVerify()) {
                        showPasswordDialog(num, mPaymentInfo, this.getActivity(), mPayOverCallback);
                    } else {
                        payClick();
                    }
                    //modify  20170913 end
                }
            } else {
                ToastUtil.showShortToast(R.string.pay_user_not_login);
            }
        } else if (mCurrentPayMode == PayModeId.ENTITY_CARD) {//会员权益卡
            // 判断会员实体卡(权益卡)是否需要密码
            if (mPaymentInfo.getEcCard().getCardKind().getIsNeedPwd() == Bool.YES && mPaymentInfo.getEcCard().getCustomer() != null) {
                String num = mPaymentInfo.getEcCard().getCustomer().getMobile();
                // 会员实体卡要密码验证
                showPasswordDialog(num, mPaymentInfo, this.getActivity(), mPayOverCallback);
            } else {
                payClick();
            }

        } else if (mCurrentPayMode == PayModeId.ANONYMOUS_ENTITY_CARD) {//匿名卡
            // 实体卡不需要密码
            mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
        }
    }

    //显示支付密码
    private void showPasswordDialog(final String phonenum, final IPaymentInfo cashInfo, final FragmentActivity context, final IPayOverCallback callback) {

        final PasswordDialog dialog = new PasswordDialog(context) {
            @Override
            public void close() {
                super.close();
                callback.onFinished(false, 0);// 失败回调
//                DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,
//                        cashInfo.getCustomer(),
//                        cashInfo.getMemberIntegral(),
//                        true, cashInfo.getActualAmount());
                /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,
                        cashInfo.getCustomer(),
                        cashInfo.getMemberIntegral(),
                        true, cashInfo.getActualAmount());
                DisplayServiceManager.updateDisplay(context.getApplicationContext(), dUserInfo);*/
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
                /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT,
                        password,
                        null,
                        0,
                        false, cashInfo.getActualAmount());
                DisplayServiceManager.updateDisplay(context.getApplicationContext(), dUserInfo);*/
            }

            @Override
            public void showReadKeyBord() {
                // TODO Auto-generated method stub

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
                        // TODO Auto-generated method stub

                    }
                };
                dialogFragment.setPosOvereCallback(cardOvereCallback);

                dialogFragment.show(context.getSupportFragmentManager(), "ReadKeyboardDialog");
            }
        });
        dialog.show();
        /*DisplayUserInfo dUserInfo =
                DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT, "", null, 0, true, cashInfo.getActualAmount());
        DisplayServiceManager.updateDisplay(context.getApplicationContext(), dUserInfo);*/
    }

    //验证支付密码
    private void doVerifypassword(String phonenum, final String password, final IPaymentInfo cashInfo, final FragmentActivity context,
                                  final IPayOverCallback callback, final PasswordDialog dialog, final ReadKeyboardDialogFragment keyboardDialog) {
        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_STORE);
        if (phonenum != null) {
            final String mobile = phonenum;
            CustomerLoginReq loginReq = new CustomerLoginReq();
            loginReq.setLoginType(CustomerLoginType.MOBILE);
            //loginReq.setMobile(mobile);
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
                        content.setPhoneNumber(mobile);//add 20161111
                        cashInfo.setMemberResp(content);
                        // 缓存密码
                        cashInfo.setMemberPassword(password);//
                        cashInfo.setCustomerId(content.getCustomerId());
                        // cashInfo.setMemberId(content.getCustomerId() + "");// 缓存会员id
                        // 调用付款方法
                        payClick();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment
                    } else {
                        String msg = "";
                        /*if (response.getStatus() == 1126) {
                            msg = context.getString(R.string.order_dish_member_disabled);
                            ToastUtil.showLongToastCenter(context, msg);
                        } else {*/
                        msg = response.getMessage();
                        ToastUtil.showLongToastCenter(context, msg);
                        //}
                        if (dialog != null) {
                            dialog.clean();// 清空输入框
                        }

                        EventBus.getDefault().post(new EventReadKeyboard(false, msg));// 发送失败到ReadKeyboardDialogFragment

                        /*DisplayUserInfo dUserInfo = DisplayServiceManager
                                .buildDUserInfo(DisplayUserInfo.COMMAND_VALIDATE_PASS_FAIL, "", null, 0, true, cashInfo.getActualAmount());
                        DisplayServiceManager.updateDisplay(context, dUserInfo);*/
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showLongToastCenter(context, error.getMessage());
                    EventBus.getDefault().post(new EventReadKeyboard(false, error.getMessage()));// 发送失败到ReadKeyboardDialogFragment
                }

            }, context.getSupportFragmentManager()));
        } else {
            ToastUtil.showShortToast(R.string.pay_member_login_please);
        }
    }

    /**
     * v8.3 判断是否美食城，单独匿名卡支付方式
     */
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

    /**
     * v8.3 判断匿名卡卡内余额是否大于实际付款金额
     */
    private boolean anonCardPayEnable() {
        return mValueCardBalance >= mPaymentInfo.getActualAmount();
    }

    /**
     * v8.3 判断是否显示余额不足信息
     */
    private boolean isNotSufficientFunds() {
        return isAnoCardSinglePay && !anonCardPayEnable();
    }

    /**
     * v8.10.0
     */
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

    /**
     * 清除会员储值优惠信息
     */
    private void cleanMemberChargePrivilege(){
        DinnerShoppingCart.getInstance().removeChargePrivilege(true,true);
        RefreshTradeVoEvent event = new RefreshTradeVoEvent();
        event.setLogin(true);
        event.setTradeVo(DinnerShoppingCart.getInstance().getShoppingCartVo().getmTradeVo());
        EventBus.getDefault().post(event);
    }

    /**
     * 添加会员储值优惠信息
     */
    private void batchMemberChargePrivilege(){
        DinnerShopManager.getInstance().setLoginCustomer(mCustomer);
        DinnerShoppingCart.getInstance().batchMemberChargePrivilege(true, false);

        RefreshTradeVoEvent event = new RefreshTradeVoEvent();
        event.setLogin(true);
        event.setTradeVo(DinnerShoppingCart.getInstance().getShoppingCartVo().getmTradeVo());
        EventBus.getDefault().post(event);
    }

    private void refreshMemberPrice() {
        // v8.10.0 重新带入会员价
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
            ShoppingCart.getInstance().memberPrivilege();// 设置购物车会员折扣

            RefreshTradeVoEvent event = new RefreshTradeVoEvent();
            event.setLogin(false);
            event.setTradeVo(ShoppingCart.getInstance().createFastFoodOrder(false));
            EventBus.getDefault().post(event);
        }
    }
}
