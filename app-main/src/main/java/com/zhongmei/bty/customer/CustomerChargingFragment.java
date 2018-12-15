package com.zhongmei.bty.customer;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.ZMIntent;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.customer.bean.ChargingPrint;
import com.zhongmei.bty.basemodule.customer.bean.PayMethod;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo.RechargeRuleDetailVo;
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
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.VerifyPayResp;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.SendType;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog;
import com.zhongmei.bty.customer.adapter.ChargeMoneyAdapter;
import com.zhongmei.bty.customer.event.EventRefreshBalance;
import com.zhongmei.bty.customer.views.ListConfirmDialogFragment;
import com.zhongmei.bty.customer.views.ListConfirmDialogFragment_;
import com.zhongmei.bty.customer.views.ListPromptDialogFragment;
import com.zhongmei.bty.customer.views.ListPromptDialogFragment_;
import com.zhongmei.bty.customer.views.RechargeRuleDialogFragment;
import com.zhongmei.bty.customer.views.RechargeRuleDialogFragment_;
import com.zhongmei.bty.customer.vo.ChargeMoneyVo;
import com.zhongmei.bty.mobilepay.event.MemberPayChargeEvent;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 充值页面
 */

@EFragment(R.layout.customer_charging_layout)
public class CustomerChargingFragment extends BasicFragment implements OnClickListener {

    public static final String TAG = CustomerChargingFragment.class.getSimpleName();

    public static final String FLAG = "Charging";

    /* 现金 */
    private final static int CASHCHARGE = 0;

    /* 银行卡 */
    private final static int BANKCARDCHARGE = 1;

    @ViewById(R.id.show_value)
    protected TextView mShowValue;

    @ViewById(R.id.rule_money)
    protected TextView mRuleMoneyTx;

    @ViewById(R.id.rule_layout)
    protected RelativeLayout mRuleLayout;

    @ViewById(R.id.rule_btn)
    protected Button mRuleBtn;

    @ViewById(R.id.customer_balance)
    protected TextView mCustomerBalance;

    @ViewById(R.id.customer_name)
    protected TextView mName;

    /**
     * 充值可选金额面板
     */
    @ViewById(R.id.customer_charging_num)
    protected GridView listView;

    private CustomerResp mCustomer;

    private ListConfirmDialogFragment mConfirmDialog;//确定对话框
    /**
     * 实体卡
     */
    private EcCardInfo ecCard;

    // 当前余额
    private String mBalance;

    private String serverId;

    private int chargeType = CASHCHARGE;

    /**
     * 充值规则
     */
    private RechargeRuleVo ruleVo;

    /**
     * 充值可选金额
     */
    private List<ChargeMoneyVo> chargeMoneyList;
    /**
     * 充值金额
     */
    private BigDecimal chargeMoney = BigDecimal.ZERO;

    /**
     * 赠送金额
     */
    private BigDecimal sendMoney = BigDecimal.ZERO;

    private int from;//从哪里跳转到充值界面的 CustomerChargingBalanceActivity
    //两个字段 FROM_MEMBER_PAY       FROM_MEMBER_CUSTOMER

    @Override
    public void onDestroy() {
        this.unregisterEventBus();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerEventBus();
    }

    @AfterViews
    void initView() {
        serverId = SystemUtils.genOnlyIdentifier();
        mShowValue.setTag(FLAG);
        listView.setOnItemClickListener(itemListener);
    }

    /**
     * 传入Customer 与Balance
     */
    public void bindData(int from, CustomerResp customer, String balance, EcCardInfo ecCard) {
        this.mCustomer = customer;
        this.mBalance = balance;
        this.ecCard = ecCard;
        this.from = from;
        refreshInfo();
    }

    @UiThread
    void refreshInfo() {
        if (ecCard != null) {
            mName.setText(String.format(getResources().getString(R.string.card_no_str), ecCard.getCardNum()));
        } else if (mCustomer != null) {
            mName.setText(String.format(getResources().getString(R.string.customer_name_str), mCustomer.getCustomerName(getActivity())));
        }
        mCustomerBalance.setText(ShopInfoCfg.formatCurrencySymbol(mBalance));
        checkRule();
    }

    @Click({R.id.rule_btn, R.id.customer_card_charging, R.id.customer_cash_charging, R.id.show_value})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rule_btn:// 查看充值规则
                RechargeRuleDialogFragment rechargeRuleDialogFragment = new RechargeRuleDialogFragment_();
                rechargeRuleDialogFragment.setDataSource(ruleVo);
                rechargeRuleDialogFragment.show(getFragmentManager(), "RechargeRuleDialogFragment");
                break;
            /*case R.id.customer_card_charging:// 银行卡充值
                serverId = SystemUtilss.genOnlyIdentifier();
				if (!ClickManager.getInstance().isClicked(R.id.customer_charging)) {
					if (TextUtils.isEmpty(serverId)) {
						ToastUtil.showLongToast(R.string.customer_data_error);
						return;
					}
					chargeType = BANKCARDCHARGE;
					PreCharging();
				}
				break;*/
            case R.id.customer_cash_charging:// 现金充值
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

                                    if (TextUtils.isEmpty(mCustomer.mobile)) {
                                        ToastUtil.showLongToast(R.string.customer_bind_pohone);
                                        return;
                                    }

                                    chargeType = CASHCHARGE;
                                    PreCharging();
                                }
                            }
                        });
                break;
            case R.id.show_value:
                // showNumberInputDialog();// add 2016 06 12
                break;

            default:
                break;
        }

    }

    // yutang add 2016 06 12 start
    // 自定义输入回调
    NumberInputdialog.InputOverListener mInputOverListener = new NumberInputdialog.InputOverListener() {
        @Override
        public void afterInputOver(String inputContent) {
            if (inputContent != null) {
                chargeMoney = new BigDecimal(inputContent);
                sendMoney = findSendMoneyByInput(chargeMoney);
                mRuleMoneyTx.setText("");// 自定义不显示赠送
                mShowValue.setText(ShopInfoCfg.formatCurrencySymbol(inputContent));
            }
        }
    };

    //根据输入金额匹配赠送金额
    private BigDecimal findSendMoneyByInput(BigDecimal inputChargeMoney) {
        if (inputChargeMoney == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal _sendMoney = BigDecimal.ZERO;
        if (chargeMoneyList != null && !chargeMoneyList.isEmpty()) {
            for (ChargeMoneyVo vo : chargeMoneyList) {
                if (!vo.isAuto() && vo.getFullMoney() != null && inputChargeMoney.compareTo(vo.getFullMoney()) >= 0) {
                    _sendMoney = vo.getSendMoney();
                }
            }
        }
        return _sendMoney;
    }

    //显示自定义输入框
    private void showNumberInputDialog() {
        double maxValue = 999999d;
        String defaultInput = null;
        NumberInputdialog numberDialog =
                new NumberInputdialog(this.getActivity(), getString(R.string.custom_recharge), getString(R.string.enter_recharge_amount), defaultInput, maxValue, mInputOverListener);
        numberDialog.setNumberType(NumberInputdialog.NUMBER_TYPE_INT).show();
//        numberDialog.setDotType(NumberInputdialog.DotType.ClEAR).show();
    }

    //跳转到充值支付界面
    private void gotoPayRecharge() {
        long customerId = mCustomer.customerId;
        TradeVo tradeVo = null;
        SaleCardDataModel saleCard = new SaleCardDataModel(OperatesFactory.create(CustomerDal.class));
        if (ecCard != null) {// 实体卡充值(需要tradeitem保存卡号)
            List<EcCardInfo> list = new ArrayList<EcCardInfo>();
            list.add(ecCard);
            tradeVo = saleCard.createTradeVo(CustomerApplication.mCustomerBussinessType, list);
            tradeVo.getTradeItemList().get(0).getTradeItem().setAmount(chargeMoney);
            tradeVo.getTrade().setBusinessType(BusinessType.CARD_RECHARGE);
            tradeVo.getTrade().setSaleAmount(chargeMoney);
            tradeVo.getTrade().setTradeAmount(chargeMoney);

        } else {//会员充值(不需要tradeitem)
            tradeVo = saleCard.createMemberRechargeTradeVo(chargeMoney, BusinessType.ONLINE_RECHARGE);
        }
        ZMIntent.payRecharge(getActivity(), tradeVo, customerId, chargeMoney);
       /* Intent chargeIntent = new Intent();
        chargeIntent.setClass(this.getActivity(), PayRechargeActivity_.class);
        chargeIntent.putExtra("tradeVo", tradeVo);
        chargeIntent.putExtra("chargeMoney", chargeMoney);
        chargeIntent.putExtra("customerId", customerId);
        if (this.sendMoney != null)
            chargeIntent.putExtra("sendMoney", sendMoney);
        startActivity(chargeIntent);*/
    }

    //本地计算充值后余额 = 原有余额+充值+赠送（注明;可能不准确）
    private BigDecimal getAfterBalanceValue() {
        BigDecimal value = BigDecimal.ZERO;
        String balanceStr = mCustomerBalance.getText().toString();
        if (!TextUtils.isEmpty(balanceStr)) {
            balanceStr = balanceStr.replace(ShopInfoCfg.getInstance().getCurrencySymbol(), "");
        }
        //充值
        if (chargeMoney != null) {
            value = value.add(chargeMoney);
        }
        //赠送
        if (sendMoney != null) {
            value = value.add(sendMoney);
        }
        //原有余额
        if (!TextUtils.isEmpty(balanceStr)) {
            value = value.add(new BigDecimal(balanceStr));
        }
        return value;
    }

    //充值前余额
    private BigDecimal geBeforeBalanceValue() {
        BigDecimal value = BigDecimal.ZERO;
        String balanceStr = mCustomerBalance.getText().toString();
        if (!TextUtils.isEmpty(balanceStr)) {
            balanceStr = balanceStr.replace(ShopInfoCfg.getInstance().getCurrencySymbol(), "");
        }
        //原有余额
        if (!TextUtils.isEmpty(balanceStr)) {
            value = value.add(new BigDecimal(balanceStr));
        }
        return value;
    }

    private void doChargePrint(final BigDecimal chargeMoney, final BigDecimal valueCard, EventPayResult eventPayResult) {
        ChargingPrint print = new ChargingPrint();
        List<PayMethod> list = new ArrayList<PayMethod>(2);
        print.setCustomerName(mCustomer.customerName);
        print.setCustomerSex(mCustomer.sex + "");
        print.setPhoneNo(mCustomer.mobile);
        if (ecCard != null)
            print.setCardNum(ecCard.getCardNum());
        print.setChargingType(0);
        print.setTrueIncomeValuecard(chargeMoney);
        print.setBeforeValuecard(geBeforeBalanceValue());
        print.setCommercialName(ShopInfoCfg.getInstance().commercialName);
        print.setUserId(Session.getAuthUser().getName());
        print.setChargeValuecard(valueCard.subtract(geBeforeBalanceValue()));
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
                    //print.setCapitalStart(geBeforeBalanceValue().subtract(BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0))));
                    //print.setCapitalEnd(valueCard.subtract(print.getPresentEnd()));
                    //double capitalstart = extendsStr.optDouble("valueCard", 0) - extendsStr.optDouble("currentTotalAdd", 0)- extendsStr.optDouble("beforeSendValue", 0);

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
                    //print.setCapitalStart(geBeforeBalanceValue().subtract(BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0))));
                    //print.setCapitalEnd(valueCard.subtract(print.getPresentEnd()));
                    //double capitalstart = extendsStr.optDouble("valueCard", 0) - extendsStr.optDouble("currentTotalAdd", 0)- extendsStr.optDouble("beforeSendValue", 0);
                    print.setCapitalEnd(BigDecimal.valueOf(extendsStr.optDouble("valueCard", 0)).subtract(print.getPresentEnd()));
                    print.setCapitalStart(print.getCapitalEnd().subtract(chargeMoney));
                    //print.setBeforeValuecard(BigDecimal.valueOf(extendsStr.optDouble("valueCard", 0) - extendsStr.optDouble("currentTotalAdd", 0)));
                    //print.setChargeValuecard(BigDecimal.valueOf(extendsStr.optDouble("currentTotalAdd", 0)));
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
        //      PrintContentQueue.getInstance().printCardOrMemberCharge(print,false,new OnSimplePrintListener(PrintTicketTypeEnum.STORE));
        //PrintTool.printCardOrMemberCharge(print, false, new PRTOnSimplePrintListener(PrintTicketTypeEnum.STORE));
    }

    //清空输入数据
    private void clearInput() {
        chargeMoney = BigDecimal.ZERO;
        sendMoney = BigDecimal.ZERO;
        mShowValue.setText(getString(R.string.zero));
    }

    //充值成功回调处理
    public void onEventMainThread(EventPayResult eventPayResult) {
        if (eventPayResult.getType() == BusinessType.ONLINE_RECHARGE || eventPayResult.getType() == BusinessType.CARD_RECHARGE) {
            dismissConfirmDialog();//关闭核对框
            //显示对话框
            if (eventPayResult.isOnlinePay()) {//如果在线支付不返回余额
                doChargePrint(chargeMoney, getAfterBalanceValue(), eventPayResult);
                showPromptDialog(chargeMoney, getAfterBalanceValue(), eventPayResult.getType());

            } else {//非在线支付有返回余额
                AnonymousCardStoreResp content = (AnonymousCardStoreResp) eventPayResult.getContent();
                //doChargePrint(chargeMoney, content.getValueCard(), eventPayResult);
                showPromptDialog(chargeMoney, content.getValueCard(), eventPayResult.getType());
            }
        }
    }

    private void showPromptDialog(final BigDecimal chargeMoney, final BigDecimal valueCard, final BusinessType businessType) {
        List<String> list = new ArrayList<String>(2);
        list.add(getString(R.string.rechare_money) + CashInfoManager.formatCash(chargeMoney.doubleValue()));
        list.add(getString(R.string.current_balance) + CashInfoManager.formatCash(valueCard.doubleValue()));
        ListPromptDialogFragment dialog = new ListPromptDialogFragment_();
        dialog.setContentList(list).
                setPositiveListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Log.d("DEBUG", "DEBUG----from=" + from);
                        //if如果是收银调转过来，直接关闭充值界面
                        if (from == CustomerChargingDialogFragment.FROM_MEMBER_PAY) {
                            Log.d("DEBUG", "DEBUG----from=FROM_MEMBER_PAY===" + valueCard.toString());
                            MemberPayChargeEvent memberPayChargeEvent = new MemberPayChargeEvent();
                            memberPayChargeEvent.setType(MemberPayChargeEvent.BALANCE_CHANGE_TYPE_CHARGE);//add 20170217
                            memberPayChargeEvent.setmValueCardBalance(valueCard);
                            EventBus.getDefault().post(memberPayChargeEvent);//发送EVENTBUS到会员支付界面MemberPayFragment
                            getActivity().finish();

                            //提示会员重新登录(只有正餐拆弹购物车需要，快餐每次都会去更新，正餐原单不需要展示所以不更新)
                            CustomerResp separatorCustomer = CustomerManager.getInstance().getSeparateLoginCustomer();
                            if (separatorCustomer != null) {
                                //判断充值的会员是当前登录的会员
                                if (mCustomer != null
                                        && TextUtils.equals(mCustomer.mobile, separatorCustomer.mobile)) {
                                    separatorCustomer.needRefresh = (true);
                                } else if (ecCard != null && separatorCustomer.card != null
                                        && TextUtils.equals(ecCard.getCardNum(), separatorCustomer.card.getCardNum())) {
                                    separatorCustomer.needRefresh = (true);
                                }
                            }
                        } else {
                            //清空输入数据
                            clearInput();
                            // 刷新左边列表
                            if (ecCard != null) {
                                EventBus.getDefault().post(new EventRefreshBalance(ecCard.getCardNum()));
                            } else {
                                EventBus.getDefault().post(new EventRefreshBalance(null));
                            }
                            // 刷新余额
                            mCustomerBalance.setText(ShopInfoCfg.getInstance().getCurrencySymbol() + MathDecimal.trimZero(valueCard));
                        }
                    }
                });
        dialog.show(getFragmentManager(), "ListPromptDialogFragment");
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
    // yutang modify 2016 07 18 end


    /**
     * 充值金额点击
     */
    private OnItemClickListener itemListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ChargeMoneyVo chargeItme = chargeMoneyList.get(position);
            if (chargeItme.isAuto()) {
                showNumberInputDialog();
            } else {
                chargeMoney = chargeItme.getFullMoney();
                sendMoney = chargeItme.getSendMoney();
                if (sendMoney != null && sendMoney.compareTo(BigDecimal.ZERO) > 0) {
                    String ruleMoneyText =
                            String.format("%s%s%s%s",
                                    getResources().getString(R.string.full_recharge_money),
                                    ShopInfoCfg.formatCurrencySymbol(chargeMoney),
                                    getResources().getString(R.string.send_money),
                                    ShopInfoCfg.formatCurrencySymbol(sendMoney));
                    mRuleMoneyTx.setText(ruleMoneyText);
                } else {
                    mRuleMoneyTx.setText(R.string.no_send);
                }
                mShowValue.setText(ShopInfoCfg.formatCurrencySymbol(chargeItme.getFullMoney()));
            }
        }
    };

    /**
     * 充值确认页面
     */
    private void PreCharging() {
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
        // 有些卡不允许充值 ，禁止打开充值界面
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
        showConfirmDialog();
        // if (chargeMoney.compareTo(BigDecimal.ZERO)==0) {
        // ToastUtil.showShortToast("请选择金额");
        // return;
        // }
        //  打开钱箱
        /*if (chargeType == CASHCHARGE) {
            PrintContentQueue.getInstance(MainApplication.getInstance()).openMoneyBox();
		}*/
        // 显示名称
       /* String name = mCustomer.get(Customer.NAME_KEY);
        if (ecCard != null) {
            name = ecCard.getCardNum();
        }
        // 充值类型
        String type = getResources().getString(R.string.card_charge);
        if (chargeType == CASHCHARGE) {
            type = getResources().getString(R.string.cash_charge);
        }
        ListConfirmDialogFragment.show(new String[]{name, mCustomer.get(Customer.MOBILE_KEY),
                        chargeMoney.toPlainString(), type},
                getString(R.string.customer_confirm),
                getFragmentManager(),
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        gotoPayRecharge();
                    }
                });*/

    }

    /**
     * 充值
     */
   /* private void charging() {
        CustomerOperates op = OperatesFactory.create(CustomerOperates.class);
        if (ecCard != null) {// 实体卡充值
            final CardRechargeReq req = getCardRechargeReq();
            op.cardRecharge(req, LoadingResponseListener.ensure(new ResponseListener<CardRechargeResp>() {

                                                                    @Override
                                                                    public void onResponse(ResponseObject<CardRechargeResp> response) {
                                                                        if (ResponseObject.isOk(response)) {
                                                                            CardRechargeResp resp = response.getContent();
                                                                            ChargingPrint print = new ChargingPrint();
                                                                            print.setCustomerName(mCustomer.get(Customer.NAME_KEY));
                                                                            print.setCustomerSex(mCustomer.get(Customer.SEX_KEY));
                                                                            print.setPhoneNo(mCustomer.get(Customer.MOBILE_KEY));
                                                                            print.setCardNum(req.getCardNum());
                                                                            print.setChargingType(0);
                                                                            print.setTrueIncomeValuecard(req.getAddValue().floatValue());
                                                                            print.setBeforeValuecard(resp.getValueCard().subtract(resp.getCurrentTotalAdd()).floatValue());
                                                                            print.setCommercialName(MainApplication.getInstance().getShopInfo().get(ShopInfo.NAME_KEY));
                                                                            print.setUserId(AuthUserCache.getAuthUser().getName());
                                                                            print.setChargeValuecard(resp.getCurrentTotalAdd().floatValue());
                                                                            if (resp.getIntegral() != null) {
                                                                                print.setCustomerIntegral(resp.getIntegral() + "");
                                                                            } else {
                                                                                print.setCustomerIntegral(null);
                                                                            }
                                                                            print.setEndValuecard(resp.getValueCard().floatValue());

                                                                            String type = getResources().getString(R.string.card_charge);
                                                                            if (chargeType == CASHCHARGE) {
                                                                                type = getResources().getString(R.string.cash_charge);
                                                                            }
                                                                            PRTPayMethod payMethod = new PRTPayMethod(type, req.getAddValue().floatValue());
                                                                            List<PRTPayMethod> list = new ArrayList<PRTPayMethod>();
                                                                            list.add(payMethod);
                                                                            print.setPayMethods(list);
                                                                            PrintContentQueue.getInstance(MainApplication.getInstance()).printCardOrMemberCharge(print,
                                                                                    false);

                                                                            PromptDialogFragment.show(getFragmentManager(),
                                                                                    getString(R.string.customer_charging_success),
                                                                                    R.drawable.customer_charging_success_icon,
                                                                                    null);
                                                                            // 刷新余额
                                                                            EventBus.getDefault().post(new EventRefreshBalance(ecCard.getCardNum()));
                                                                            mCustomerBalance.setText(getString(R.string.dinner_money_symbol) + MathDecimal.trimZero(response.getContent().getValueCard()));
                                                                        } else if (ResponseObject.isExisted(response)) {
                                                                            ToastUtil.showShortToast("充值单据已存在，不可以重复提交");
                                                                        } else {
                                                                            ToastUtil.showShortToast(response.getMessage());
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(VolleyError error) {
                                                                        ToastUtil.showShortToast(error.getMessage());
                                                                    }

                                                                },
                    getFragmentManager()));
        } else {// 会员充值
            MemberRechargeReq req = getMemberRechargeReq();

            op.memberRecharge(req, LoadingResponseListener.ensure(new ResponseListener<MemberRechargeResp>() {

                                                                      @Override
                                                                      public void onResponse(ResponseObject<MemberRechargeResp> response) {
                                                                          if (response.getStatusCode() == 1000) {
                                                                              JsonObject obj = response.getContent().getMemberValuecardHistory().get(0);
                                                                              List<JsonObject> customerIntegers = response.getContent().getMemberIntegrals();
                                                                              JsonObject customerInteger = null;
                                                                              if (customerIntegers != null && customerIntegers.size() > 0) {
                                                                                  customerInteger = customerIntegers.get(0);
                                                                              }
                                                                              ChargingPrint print = new ChargingPrint();
                                                                              print.setCustomerName(mCustomer.get(Customer.NAME_KEY));
                                                                              print.setCustomerSex(mCustomer.get(Customer.SEX_KEY));
                                                                              print.setPhoneNo(mCustomer.get(Customer.MOBILE_KEY));
                                                                              print.setChargingType(0);
                                                                              print.setTrueIncomeValuecard(obj.get("addValuecard").getAsFloat());
                                                                              print.setBeforeValuecard(obj.get("beforeValuecard").getAsFloat());
                                                                              print.setCommercialName(MainApplication.getInstance().getShopInfo().get(ShopInfo.NAME_KEY));
                                                                              print.setUserId(obj.get("userId").getAsString());
                                                                              print.setChargeValuecard(obj.get("addValuecard").getAsFloat()
                                                                                      + obj.get("sendValuecard").getAsFloat());
                                                                              if (customerInteger != null) {
                                                                                  print.setCustomerIntegral(customerInteger.get("integral") + "");
                                                                              } else {
                                                                                  print.setCustomerIntegral(null);
                                                                              }
                                                                              print.setEndValuecard(obj.get("endValuecard").getAsFloat());

                                                                              Resources res = getResources();
                                                                              JsonElement value = obj.get("cashValuecard");
                                                                              String name = res.getString(R.string.cash_charge);
                                                                              if (value == null) {
                                                                                  value = obj.get("bankValuecard");
                                                                                  name = res.getString(R.string.card_charge);
                                                                              }
                                                                              PRTPayMethod payMethod = new PRTPayMethod(name, value.getAsFloat());
                                                                              List<PRTPayMethod> list = new ArrayList<PRTPayMethod>();
                                                                              list.add(payMethod);
                                                                              print.setPayMethods(list);
                                                                              PrintContentQueue.getInstance(MainApplication.getInstance()).printCardOrMemberCharge(print,
                                                                                      false);
                                                                              PromptDialogFragment.show(getFragmentManager(),
                                                                                      getString(R.string.customer_charging_success),
                                                                                      R.drawable.customer_charging_success_icon,
                                                                                      null);
                                                                              // 刷新余额
                                                                              EventBus.getDefault().post(new EventRefreshBalance(null));
                                                                              loadStatisticData();

                                                                          } else {
                                                                              ToastUtil.showShortToast(response.getMessage());
                                                                          }
                                                                      }

                                                                      @Override
                                                                      public void onError(VolleyError error) {
                                                                          ToastUtil.showShortToast(R.string.customer_timeout);
                                                                      }

                                                                  },
                    getFragmentManager()));
        }

    }*/

    /**
     * 封装会员充值请求数据
     */
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

    /**
     * 封装实体卡充值请求数据
     */
    private CardRechargeReq getCardRechargeReq() {
        CardRechargeReq req = new CardRechargeReq();
        setValueReq(req);
        req.setCardNum(ecCard.getCardNum());
        req.setAddValue(chargeMoney);
        req.setSendValue(sendMoney);
        return req;
    }

    /**
     * 公共字段赋值
     *
     * @return
     */
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

    /**
     * 获取账户余额
     */
    /*private void loadStatisticData() {
        if (ecCard == null) {// 刷新会员充值后的余额
            CustomerRequestManager.getInstance().getMemberStatistics(mCustomer.get(Customer.SERVER_ID_KEY),
                    new Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                CustomerStatistic mCustomerStatistic = new CustomerStatistic();
                                mCustomerStatistic.initFromJson(response);
                                mCustomerBalance.setText(getString(R.string.dinner_money_symbol)
                                        + mCustomerStatistic.get(CustomerStatistic.MEMBER_BALANCE_KEY));
                            } catch (Exception e) {
                                Log.e(TAG, "", e);
                            }
                        }
                    },
                    new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    },
                    getFragmentManager());
        }
    }*/

    /**
     * 查看规则
     */
    public void checkRule() {
        chargeMoneyList = new ArrayList<ChargeMoneyVo>();
        final CustomerDal dal = OperatesFactory.create(CustomerDal.class);
        //modify begin 20170302
        TaskContext.bindExecute(this, new SimpleAsyncTask<RechargeRuleVo>() {


            @Override
            public RechargeRuleVo doInBackground(Void... params) {
                RechargeRuleVo rechargeRuleVo = null;
                try {
                    if (ecCard != null) {
                        rechargeRuleVo = dal.findRechargeRule(Long.valueOf(ecCard.getCardLevelId()));
                    } else if (mCustomer != null) {
                        rechargeRuleVo = dal.findRechargeRule(null);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }

                return rechargeRuleVo;
            }

            public void onPostExecute(RechargeRuleVo result) {
                try {
                    ruleVo = result;
                    // 做一个判断，如果没有满赠，就不显示满赠的布局
                    if (ruleVo != null) {
                        List<RechargeRuleDetailVo> ruleList = ruleVo.getRuleDetailList();
                        // 判断是否是无满增
                        if (ruleVo.getIsFullSend() == FullSend.YES) {
                            mRuleLayout.setVisibility(View.VISIBLE);

                            if (ruleList != null && ruleList.size() > 0) {
                                for (RechargeRuleDetailVo rule : ruleList) {
                                    ChargeMoneyVo chargeMoneyVo = new ChargeMoneyVo(false);
                                    chargeMoneyVo.setFullMoney(rule.getFullValue());
                                    if (ruleVo.getSendType() == SendType.FIXED) {
                                        chargeMoneyVo.setSendMoney(rule.getSendValue());
                                    } else {
                                        chargeMoneyVo.setSendMoney(rule.getSendRate()
                                                .multiply(rule.getFullValue())
                                                .divide(new BigDecimal(100)));
                                    }
                                    chargeMoneyList.add(chargeMoneyVo);
                                }
                            } else {
                                fixAmount();
                            }
                        } else {
                            if (ruleList != null && ruleList.size() > 0) {
                                for (RechargeRuleDetailVo rule : ruleList) {
                                    ChargeMoneyVo chargeMoneyVo = new ChargeMoneyVo(false);
                                    chargeMoneyVo.setFullMoney(rule.getFullValue());
                                    chargeMoneyVo.setSendMoney(BigDecimal.ZERO);
                                    chargeMoneyList.add(chargeMoneyVo);
                                }
                            } else {
                                fixAmount();
                            }
                        }

                    } else {
                        fixAmount();
                    }
                    chargeMoneyList.add(new ChargeMoneyVo(true));
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                // 初始话充值金额界面
                ChargeMoneyAdapter adapter = new ChargeMoneyAdapter(getActivity());
                listView.setAdapter(adapter);
                adapter.setDataSource(chargeMoneyList);
            }
        });
        //modify end 20170302
    }


    /**
     * 显示固定充值金额
     */
    private void fixAmount() {
        mRuleLayout.setVisibility(View.GONE);
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
                // case 4:
                // chargeMoneyVo.setFullMoney(new
                // BigDecimal(1000));
                // break;

                default:
                    break;
            }
            chargeMoneyVo.setSendMoney(BigDecimal.ZERO);
            chargeMoneyList.add(chargeMoneyVo);
        }
    }

}
