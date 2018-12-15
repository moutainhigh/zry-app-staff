package com.zhongmei.bty.mobilepay.manager;

import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.IFinishPayCallback;
import com.zhongmei.bty.mobilepay.IUpdateUICallback;
import com.zhongmei.bty.mobilepay.bean.CusomerPayInfo;
import com.zhongmei.bty.mobilepay.bean.GroupPay;
import com.zhongmei.bty.basemodule.pay.bean.PaidPaymentRecord;
import com.zhongmei.bty.mobilepay.bean.PayModelTool;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;

import java.math.BigDecimal;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 保存支付相关的信息.
 */

public class PaymentInfoManager implements IPaymentInfo {
    // 是否订单中心，判断是否打印下单
    private boolean isOrderCenter;
    // 是否已经下单，判断走收银还是下单及收银
    private boolean isOrdered;

    private boolean isSplit;
    //是否已经打印
    private boolean isPrintedOk;
    // 抹零方式
    private int mEraseType = 8;
    //是否开发票
    private boolean isOpenElectronicInvoice;
    //抹零金额
    private double mExemptAmount;
    //当前要支付的订单
    private TradeVo mTradeVo;
    // 原单数据(拆单时调用)
    private TradeVo sourceTradeVo;
    //当前输入的支付金额
    private GroupPay mInputPayModels;
    //保存会员相关信息
    private transient CusomerPayInfo mCusomerPayInfo;
    //已经支付信息
    private PaidPaymentRecord mPaidPaymentRecords;
    private String paymentUuid;// 支付uuid
    private String id;//当前对象实例ID add 20170708
    private transient IUpdateUICallback mUILitenner;
    private transient IFinishPayCallback mFinishPayCallback;//add 20180123
    private int defaultPaymentMenuType = -1;//默認支付方式
    private PayScene mPayScene = PayScene.SCENE_CODE_SHOP;//add 20170704
    // v8.6.0 快捷支付增加字段
    private int quickPayType = -1;
    private PayActionPage mPayActionPage = PayActionPage.COMPAY; //add 20180309  sign pay activity 默认快餐界面
    private Boolean isGroupPay = true;

    //工厂方法构建器
    public static synchronized PaymentInfoManager getNewInstance(IUpdateUICallback updateUICallback) {
        return new PaymentInfoManager(updateUICallback);
    }

    //构造方法1
    private PaymentInfoManager() {
        // 初始化输入的支付数据
        this.mInputPayModels = new GroupPay();
        //初始化会员相关信息
        this.mCusomerPayInfo = new CusomerPayInfo();
        //初始化已经付款记录
        this.mPaidPaymentRecords = new PaidPaymentRecord();

        this.id = SystemUtils.genOnlyIdentifier();
    }

    //构造方法2
    private PaymentInfoManager(IUpdateUICallback updateUICallback) {
        this();
        this.mUILitenner = updateUICallback;
    }

    public void setPaidPaymentRecords(List<PaymentVo> paymentVoList) {
        this.mPaidPaymentRecords.setPaidPayment(paymentVoList);
        if (this.mUILitenner != null) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUILitenner.updatePaymentInfo();//刷新应收
                    // EventBus.getDefault().post(new ExemptEventUpdate(getEraseType())); //刷新输入框 modify v8.5
                }
            });
        }
    }

    @Override
    public boolean isOpenElectronicInvoice() {
        return this.isOpenElectronicInvoice;
    }

    public void setOpenElectronicInvoice(boolean openElectronicInvoice) {
        this.isOpenElectronicInvoice = openElectronicInvoice;
    }

    public void setTradeVo(TradeVo tradeVo) {
        if (isOrdered && this.mTradeVo != null && mTradeVo.getTrade().getTradeAmount().compareTo(tradeVo.getTrade().getTradeAmount()) != 0) {
            this.mTradeVo = tradeVo;
            if (this.mUILitenner != null) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUILitenner.updatePaymentInfo();//刷新应收
                        EventBus.getDefault().post(new ExemptEventUpdate(getEraseType())); //刷新输入框 modify v8.5
                    }
                });
            }
        } else {
            this.mTradeVo = tradeVo;
        }
    }

    @Override
    public TradeVo getTradeVo() {
        return this.mTradeVo;
    }

    public TradeVo getSourceTradeVo() {
        return sourceTradeVo;
    }

    public void setSourceTradeVo(TradeVo sourceTradeVo) {
        this.sourceTradeVo = sourceTradeVo;
    }

    public double getReceivableAmount() {
        //当前应支付的金额=（应收-已收）//抹零是在该基础上抹
        return CashInfoManager.floatSubtract(getTradeAmount(), getPaidAmount());
    }

    @Override
    public Double getActualAmount() {
        //当前要支付的金额=（应收-已收-抹零）//对应UI上的未收
        if (this.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING) {
            return CashInfoManager.floatSubtract(mTradeVo.getBakeryBookingAmount().add(mTradeVo.getPayDepositAmount()).doubleValue(), getExemptAmount());
        }

        return CashInfoManager.floatSubtract(getTradeAmount(), (getExemptAmount() + getPaidAmount()));
    }

    @Override
    public double getTradeAmount() {
        //如果是付押金，获取押金金额 add 20170706
        if (this.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && this.mTradeVo.isNeedToPayDeposit()) {
            return this.mTradeVo.getTradeDeposit().getDepositPay().doubleValue();
        }

        if (this.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING) {

            return mTradeVo.getBakeryBookingAmount().add(mTradeVo.getUnPayDepositAmount()).doubleValue();
        }


        //订单金额:对应UI上的应收
        return this.mTradeVo.getTrade().getTradeAmount().doubleValue();
    }

    public double getSaleAmount() {
        //订单金额:对应UI上的折前
        return this.mTradeVo.getTrade().getSaleAmount().doubleValue();
    }

    @Override
    public boolean isDinner() {
        return BusinessType.DINNER.equals(getTradeBusinessType()) || BusinessType.GROUP.equals(getTradeBusinessType()) || BusinessType.BUFFET.equals(getTradeBusinessType());
    }

    @Override
    public boolean isBeauty() {
        return this.mTradeVo != null && mTradeVo.isBeauty();
    }

    @Override
    public double getNotPayMent() {
        //还剩金额=（应收-抹零-已收-已输入金额）***其它支付要用改方法
        return CashInfoManager.floatSubtract(getTradeAmount(), (getExemptAmount() + getPaidAmount() + mInputPayModels.getGroupAmount()));
    }

    @Override
    public boolean isSplit() {
        return isSplit;
    }

    @Override
    public void setSplit(boolean isSplit) {
        this.isSplit = isSplit;
    }

    @Override
    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public boolean isPrintedOk() {
        return isPrintedOk;
    }

    public void setPrintedOk(boolean isPrintedOk) {
        this.isPrintedOk = isPrintedOk;
    }

    @Override
    public int getEraseType() {
        return mEraseType;
    }

    @Override
    public void setEraseType(int eraseType) {//设置抹零方式
        mEraseType = eraseType;
    }

    @Override
    public double getOtherPayNotPayMentByModelId(Long ModelId) {
        return CashInfoManager.floatSubtract(this.getActualAmount(),
                mInputPayModels.getGroupAmountExceptModel(ModelId));
    }

    @Override
    public boolean enablePay() {
        if (DoPayUtils.isSupportGroupPay(this, PaySettingCache.isSupportGroupPay())) {//如果支持组合支付
            return this.mInputPayModels.getGroupAmount() > 0 ? true : false;
        } else {
            return this.mInputPayModels.getGroupAmount() >= this.getActualAmount();//不分步支付，输入金额必须大于等于应付金额
        }
    }

    @Override
    public GroupPay getOtherPay() {
        return this.mInputPayModels;
    }

    @Override
    public BusinessType getTradeBusinessType() {
        if (this.mTradeVo != null && this.mTradeVo.getTrade() != null)
            return this.mTradeVo.getTrade().getBusinessType();
        return null;
    }

    @Override
    public long getCustomerId() {
        return this.mCusomerPayInfo.getCustomerId();
    }

    @Override
    public void setCustomerId(long customerId) {
        this.mCusomerPayInfo.setCustomerId(customerId);
    }

    @Override
    public double getMemberCardBalance() {
        return this.mCusomerPayInfo.getMemberCardBalance();
    }

    @Override
    public void setMemberCardBalance(double memberCardBalance) {
        this.mCusomerPayInfo.setMemberCardBalance(memberCardBalance);
    }

    @Override
    public long getMemberIntegral() {
        return this.mCusomerPayInfo.getMemberIntegral();
    }

    @Override
    public void setMemberIntegral(long memberIntegral) {
        this.mCusomerPayInfo.setMemberIntegral(memberIntegral);
    }

    @Override
    public CustomerResp getCustomer() {
        return this.mCusomerPayInfo.getCustomer();
    }

    @Override
    public void setCustomer(CustomerResp customer) {
        this.mCusomerPayInfo.setCustomer(customer);
        this.setPrintMemeberInfoByCustomer();//add v8.4
    }

    @Override
    public void setCardCustomer(CustomerResp customer) {
        mCusomerPayInfo.cardCustomer = customer;
    }

    @Override
    public CustomerResp getCardCustomer() {
        return mCusomerPayInfo.cardCustomer;
    }

    @Override
    public EcCard getEcCard() {
        return this.mCusomerPayInfo.getEcCard();
    }

    @Override
    public void setEcCard(EcCard ecCard) {
        this.mCusomerPayInfo.setEcCard(ecCard);
    }

    @Override
    public String getMemberPassword() {
        return this.mCusomerPayInfo.getMemberPassword();
    }

    @Override
    public void setMemberPassword(String memberPassword) {
        this.mCusomerPayInfo.setMemberPassword(memberPassword);
    }

    @Override
    public CustomerLoginResp getMemberResp() {
        return this.mCusomerPayInfo.getMemberResp();
    }

    @Override
    public void setMemberResp(CustomerLoginResp memberResp) {
        this.mCusomerPayInfo.setMemberResp(memberResp);
    }

    //已支付金额（已经到账）
    public double getPaidAmount() {
        return this.mPaidPaymentRecords.getPaidAmount();
    }

    //是否可以抹零（如果已经抹零，不能再抹零）
    public boolean enableExempt() {
        /*if (mPayScene == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPayScene == PayScene.SCENE_CODE_WRITEOFF || mPayScene == PayScene.SCENE_CODE_BOOKING_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)//付押金不支持抹零,销账不支持
            return false;
        if (this.isNeedToPayDeposit()) {//付押金前不支持抹零
            return false;
        }
        return (mPaidPaymentRecords.getPaiExemptAmount() > 0 && mPaidPaymentRecords.getPaidAmount() > 0) ? false : true;*/
        return false;
    }

    public double getExemptAmount() {
        if (enableExempt())
            return mExemptAmount;
        else
            return this.mPaidPaymentRecords.getPaiExemptAmount();
    }

    public void setExemptAmount(double exemptAmount) {
        this.mExemptAmount = exemptAmount;
    }

    public void setOrderCenter(boolean isOrderCenter) {
        this.isOrderCenter = isOrderCenter;
    }

    public boolean isOrderCenter() {
        return isOrderCenter;
    }

    @Override
    public PaymentVo getTradePaymentVo() {
        PayModelTool payModelTool = new PayModelTool(this);
        PaymentVo paymentVo = payModelTool.creatTradePaymentVo();
        return paymentVo;
    }

    public String getPaymentUuid() {
        if (this.paymentUuid == null) {
            this.paymentUuid = SystemUtils.genOnlyIdentifier();
        }
        return this.paymentUuid;
    }

    @Override
    public void clearPaymentUUids() {
        this.paymentUuid = null;
    }

    public List<PaymentItem> getPaidPaymentItems() {
        return this.mPaidPaymentRecords.getPaidPaymentItems();
    }

    @Override
    public void clearData() {
        getOtherPay().clear();
        setPaidPaymentRecords(null);
        clearPaymentUUids();
        setPrintedOk(false);
        setEraseType(-1);
        mExemptAmount = 0;
    }

    @Override
    public PayScene getPayScene() {
        return this.mPayScene;
    }

    @Override
    public void setPayScene(PayScene scene) {
        this.mPayScene = scene;
    }

    //是否需要付押金
    @Override
    public boolean isNeedToPayDeposit() {
        if (this.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT) {
            return false;
        }
        //押金有效，大于零，且未支付，需要交押金
        if (this.getTradeBusinessType() == BusinessType.BOOKING_LIST && this.getTradeAmount() > 0 && this.getTradeVo().isNeedToPayDeposit()) {
            return (!this.getTradeVo().isPaidTradeposit());
        }
        //押金有效，大于零，且未支付，需要交押金
        if (this.getTradeBusinessType() == BusinessType.BUFFET && this.getTradeAmount() > 0 && this.getTradeVo().isNeedToPayDeposit()) {
            return (!this.getTradeVo().isPaidTradeposit());
        }
        return false;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getDefaultPaymentMenuType() {
        return defaultPaymentMenuType;
    }

    @Override
    public void setDefaultPaymentMenuType(int menuType) {
        defaultPaymentMenuType = menuType;
    }

    @Override
    public int getQuickPayType() {
        return quickPayType;
    }

    @Override
    public void setQuickPayType(int quickPayType) {
        this.quickPayType = quickPayType;
    }

    public void setFinishPayCallback(IFinishPayCallback finishPayCallback) {//add 20180123
        this.mFinishPayCallback = finishPayCallback;
    }

    @Override
    public void doFinish() {//add 20180123
        if (this.mFinishPayCallback != null) {
            this.mFinishPayCallback.finishPay();
        }
    }

    @Override
    public PayActionPage getPayActionPage() {
        return mPayActionPage;
    }

    @Override
    public void setPayActionPage(PayActionPage payActionPage) {
        mPayActionPage = payActionPage;
    }

    @Override
    public boolean isGroupPay() {
        return this.isGroupPay;
    }

    public boolean setIsGroupPay(boolean isSuportGroupPay) {
        return this.isGroupPay = isSuportGroupPay;
    }

    public Payment getPaidPayment() {
        return this.mPaidPaymentRecords.getPaidPayment();
    }

    public void setPrintMemeberInfoByCard() {
        // 如果是有实体卡信息
        if (this.getCustomer() == null && this.getEcCard() != null) {
            CustomerLoginResp loginResp = new CustomerLoginResp();
            loginResp.setCustomerId(this.getEcCard().getId());

            loginResp.setLevel(this.getEcCard().getCardLevel().getCardLevelName());
            loginResp.setSex(Sex.MALE);

            BigDecimal integralAccount = new BigDecimal(0);// 会员积分
            BigDecimal valueCardAccount = new BigDecimal(this.getMemberCardBalance());// 会员余额
            switch (this.getEcCard().getCardType()) {
                case CUSTOMER_ENTITY_CARD:
                    // 得到 integralAccount 的值 valueCardAccount的值
                    if (this.getEcCard().getIntegralAccount() == null) {
                        integralAccount = BigDecimal.valueOf(0);
                    } else {
                        if (this.getEcCard().getIntegralAccount().getIntegral() == null) {
                            integralAccount = BigDecimal.valueOf(0);
                        } else {
                            integralAccount =
                                    MathDecimal.round(BigDecimal.valueOf(this.getEcCard().getIntegralAccount().getIntegral()), 2);
                        }
                    }
                    loginResp.setCustomerName(this.getEcCard().getCardNum());

                    break;
                case ANONYMOUS_ENTITY_CARD:
                    integralAccount = BigDecimal.valueOf(0);
                    loginResp.setCustomerName("");//匿名卡不需要传入名字

                    break;

                default:
                    break;

            }
            loginResp.setIntegralBalance(integralAccount);// 会员积分
            loginResp.setValueCardBalance(valueCardAccount);// 会员余额
            this.setMemberResp(loginResp);
        }
    }

    //add v8.14
    public void addPaymentRecord(PaymentVo paymentVo) {
        this.mPaidPaymentRecords.addPaymentRecord(paymentVo);
        if (this.mUILitenner != null) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUILitenner.updatePaymentInfo();//刷新应收
                }
            });
        }
    }

    //add v8.14
    public List<PaymentVo> getPaymentRecords() {
        return this.mPaidPaymentRecords.getPaidPayments();
    }

    @Override
    public String getCashPermissionCode() {
        String permissionCode;
        if (isDinner()) {
            permissionCode = DinnerApplication.PERMISSION_DINNER_CASH;
        } else {
            permissionCode = BeautyApplication.PERMISSION_BEAUTY_CASH;
        }
        return permissionCode;
    }

    //根据登录会员生成打印信息 add v8.4
    private void setPrintMemeberInfoByCustomer() {
        // 如果是有实体卡信息
        if (this.getCustomer() != null) {
            CustomerLoginResp loginResp = new CustomerLoginResp();
            loginResp.setCustomerId(this.getCustomer().customerId);
            loginResp.setLevel(this.getCustomer().levelName);
            loginResp.setSex(this.getCustomer().sex);
            BigDecimal integralAccount = new BigDecimal(this.getCustomer().integral == null ? 0 : this.getCustomer().integral);// 会员积分
            BigDecimal valueCardAccount = new BigDecimal(this.getCustomer().remainValue == null ? 0 : this.getCustomer().remainValue);// 会员余额

            loginResp.setIntegralBalance(integralAccount);// 会员积分
            loginResp.setValueCardBalance(valueCardAccount);// 会员余额
            this.setMemberResp(loginResp);
        }
    }
}
