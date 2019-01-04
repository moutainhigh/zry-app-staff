package com.zhongmei.bty.mobilepay.manager;

import android.content.Context;
import android.util.Log;

import com.zhongmei.bty.mobilepay.bean.GroupPay;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.bty.basemodule.pay.message.PaymentDeviceReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemUnionpayReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemUnionpayVoReq;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.enums.RefundWay;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.yunfu.db.entity.ErpCommercialRelation;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.bty.commonmodule.database.enums.PosBusinessType;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 记录收银信息, 缓存订单及用户输入的各种支付信息
 * @Version: 1.0
 */
public class CashInfoManager implements IPaymentInfo {
    private static final String TAG = CashInfoManager.class.getSimpleName();

    public static final String CASH_FORMAT_REG = "([0-9]{1,8}[.][0-9]{0,2})|([0-9]{1,8})";// 金额验证正则表达式

    //public static String CASH_FORMAT = BaseApplication.sInstance.getString(R.string.dinner_balace_price);// 金额输入框显示格式

    private int eraseType = 8;// 抹零方式

    private double tradeAmount;// 应收金额

    private double actualAmount;// 实收金额

    private double cashPayMoney; // 记录现金收银金额

    private double unionPayMoney; // 记录银联收银金额

    private double memberPayMoney; // 记录会员收银金额

    private double memberCardBalance;// 会员余额

    private long memberIntegral;// 会员积分

    private GroupPay otherPay;// 其它支付组合

    private boolean isCurrentDefaultInputValue;// 当前选项是否默认值

    private PayModelGroup defaultInputPayGroup;

    private boolean isOrderCenter;// 是否订单中心，判断是否打印下单

    private boolean isOrdered;// 是否已经下单，判断走收银还是下单及收银

    private boolean isQuickPay;// 是否闪结

    private boolean isPrintedOk;// 是否已经打印

    private String memberId;// 会员服务器id

    private CustomerResp customer;// 会员姓名

    private String memberPassword;// 会员支付密码

    private CustomerLoginResp memberResp;// 会员储值信息

    private TradeVo mTradeVo;// 订单数据

    private String paymentItemCashUuid;// 现金UUid

    private String paymentItemUnionUuid;// 银联UUid

    private String paymentItemMemberUuid;// 会员收银UUid

    private String paymentUuid;// 支付uuid

    private String mTradeUUid;// 订单uuid

    private boolean isSplit;// 是否拆单

    private TradeVo sourceTradeVo;// 原单数据(拆单时调用)

    private Reason mReason;// 退货原因(无单退货时调用)

    private boolean isPrintKitchen = true;// 是否打印厨打(无单退货时调用)

    private PosTransLog mPosTransLog;// 银联pos扣款信息

    private EcCard ecCard;

    private BigDecimal chargeMoney;//充值金额

    private BigDecimal chargeSendMoney;//充值赠送金额

    private long customerId;//会员Id

    private boolean reviseStock = true;//是否退回库存

    private int defaultPaymentMenuType = -1;//默認支付方式

    private String id;//当前对象实例ID add 20170708

    private PayScene mPayScene = PayScene.SCENE_CODE_SHOP;//add 20170704

    private PayActionPage mPayActionPage = PayActionPage.COMPAY; //add 20180309  sign pay activity 默认快餐界面

    public boolean getIsReviseStock() {
        return reviseStock;
    }

    public void setReviseStock(boolean reviseStock) {
        this.reviseStock = reviseStock;
    }

    /**
     * 新增是否打印电子发票
     */
    private boolean isPrintInvoice;

    private ElectronicInvoiceVo electronicInvoiceVo;

    //构造方法
    private CashInfoManager() {
        otherPay = new GroupPay(PayModelGroup.OTHER);// 初始化其它支付
        this.id = SystemUtils.genOnlyIdentifier();
    }

    //工厂方法构建器
    public static CashInfoManager getNewInstance() {
        return new CashInfoManager();
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public BigDecimal getChargeSendMoney() {
        return chargeSendMoney;
    }

    public void setChargeSendMoney(BigDecimal chargeSendMoney) {
        this.chargeSendMoney = chargeSendMoney;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }


    public PosTransLog getPosTransLog() {
        return mPosTransLog;
    }

    public void setPosTransLog(PosTransLog posTransLog) {
        this.mPosTransLog = posTransLog;
    }

    /**
     * @Title: clearData
     * @Description: 清理所有数据
     * @Return void 返回类型
     */
    public void clearData() {
        tradeAmount = 0.0;
        actualAmount = 0.0;
        cashPayMoney = 0.0;
        unionPayMoney = 0.0;
        memberPayMoney = 0.0;
        mTradeVo = null;
        eraseType = 8;
        isCurrentDefaultInputValue = false;
        isOrderCenter = false;
        isOrdered = false;
        isQuickPay = false;
        isPrintedOk = false;
        memberId = null;
        customer = null;
        memberPassword = null;
        paymentItemCashUuid = null;
        paymentItemUnionUuid = null;
        paymentItemMemberUuid = null;
        paymentUuid = null;
        otherPay.clear();
        memberResp = null;
        isSplit = false;
        sourceTradeVo = null;
        memberIntegral = 0;
        mReason = null;
        isPrintKitchen = true;
        mPosTransLog = null;
        ecCard = null;

        isPrintInvoice = false;
        electronicInvoiceVo = null;
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
        return 0;
    }

    @Override
    public void setQuickPayType(int quickPayType) {

    }

    @Override
    public void doFinish() {

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
        return false;
    }

    public ElectronicInvoiceVo getElectronicInvoiceVo() {
        return electronicInvoiceVo;
    }

    public void setElectronicInvoiceVo(ElectronicInvoiceVo electronicInvoiceVo) {
        this.electronicInvoiceVo = electronicInvoiceVo;
    }

    public boolean isPrintInvoice() {
        return isPrintInvoice;
    }

    public void setPrintInvoice(boolean printInvoice) {
        isPrintInvoice = printInvoice;
    }

    public void clearPaymentUUids() {
        paymentItemCashUuid = null;
        paymentItemUnionUuid = null;
        paymentItemMemberUuid = null;
        paymentUuid = null;
    }

    /**
     * @Description: 判断是否正餐，true 正餐：false:不是正餐
     * @Return boolean 返回类型
     */
    public boolean isDinner() {
        if (mTradeVo == null) {
            return false;
        }
        return mTradeVo.getTrade().getBusinessType() == BusinessType.DINNER;
    }

    @Override
    public boolean isBeauty() {
        return mTradeVo != null && mTradeVo.isBeauty();
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

    /**
     * @Title: getCashPayMoney
     * @Description: 读取现金输入金额
     * @Return double 返回类型
     */
    public double getCashPayMoney() {
        return cashPayMoney;
    }

    /**
     * @Title: setCashPayMoney
     * @Description: 设置现金输入金额
     * @Param cashPayMoney
     * @Return void 返回类型
     */
    public void setCashPayMoney(double cashPayMoney) {
        this.cashPayMoney = cashPayMoney;
    }

    /**
     * @Title: getUnionPayMoney
     * @Description: 读取银联输入金额
     * @Return double 返回类型
     */
    public double getUnionPayMoney() {
        return unionPayMoney;
    }

    /**
     * @Title: setUnionPayMoney
     * @Description: 设置银联输入金额
     * @Param unionPayMoney 输入金额
     * @Return void 返回类型
     */
    public void setUnionPayMoney(double unionPayMoney) {
        this.unionPayMoney = unionPayMoney;
    }

    /**
     * @Title: getMemberPayMoney
     * @Description:读取会员储值输入金额
     * @Return double 返回类型
     */
    public double getMemberPayMoney() {
        return memberPayMoney;
    }

    /**
     * @Title: setMemberPayMoney
     * @Description: 设置会员储值输入金额
     * @Param membePayMoney 输入金额
     * @Return void 返回类型
     */
    public void setMemberPayMoney(double membePayMoney) {
        this.memberPayMoney = membePayMoney;
    }

    /**
     * @Description: 获取其它支付数据
     * @Return GroupPay 返回类型
     */
    public GroupPay getOtherPay() {
        return otherPay;
    }

    /**
     * @Description: 设计其它支付数据
     * @Param @param otherPay 其它支付
     * @Return void 返回类型
     */
    public void setOtherPay(GroupPay otherPay) {
        this.otherPay = otherPay;
    }

    /**
     * @Description: 获取抹零金额
     * @Return double 返回类型
     */
    public double getExemptAmount() {

        return floatSubtract(this.tradeAmount, this.actualAmount);
    }

    @Override
    public Payment getPaidPayment() {
        return null;
    }

    @Override
    public double getPaidAmount() {
        return 0;
    }

    /**
     * @Description: 获取未付金额(应收 - 所有输入金额之和)
     * @Return double 返回类型
     */
    public double getNotPayMent() {
        return floatSubtract(this.actualAmount, this.getCommonPaySum());
    }

    // 未收小于等于零返回true
    public boolean enablePay() {
        return getNotPayMent() <= 0;
    }

    /**
     * @Description: 获取未付金额字串
     * @Param @return
     * @Return String 返回类型
     */
    public String getNotPayMentStr() {

        return formatCash(getNotPayMent());

    }

    /**
     * @Description: 获取找零、溢收金额字串
     * @Return String 返回类型
     */
    public String getMorePaymentStr() {
        return formatCash(floatSubtract(this.getCommonPaySum(), this.actualAmount));
    }

    /**
     * @Description: 获取找零、溢收金额(不找零金额和-应收)
     * @Return double 返回类型
     */
    public double getMorePaymentAmount() {
        if (new BigDecimal(otherPay.getGroupAmount()).compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        } else {
            return floatSubtract(otherPay.getGroupAmount() + unionPayMoney + memberPayMoney, this.actualAmount);
        }
    }

    /**
     * @Description(各种支付和-找零)
     * @Return BigDecimal 返回类型
     */
    private BigDecimal getActualPaymentAmount() {
        if (new BigDecimal(otherPay.getGroupAmount()).compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(this.actualAmount);
        } else {
            return BigDecimal.valueOf(floatSubtract(otherPay.getGroupActualAmount() + unionPayMoney + memberPayMoney + cashPayMoney, this.getChangeAmount()));
        }
    }

    /**
     * @Description: 获取现金找零金额
     * @Param @return
     * @Return float 返回类型
     */
    public double getChangeAmount() {
        if (getCashNotPayMent() >= 0) {
            return floatSubtract(this.cashPayMoney, this.getCashNotPayMent());
        } else {
            if (this.cashPayMoney >= 0)
                return this.cashPayMoney + this.getCashNotPayMent();
            else
                return floatSubtract(this.cashPayMoney, this.getCashNotPayMent());
        }
    }

    /**
     * @Description: 获取除现金以外已付款金额(银联 + 会员 + 其它 。 。)
     * @Param @return
     * @Return double 返回类型
     */
    public double getNotCashPayMent() {
        return unionPayMoney + memberPayMoney + otherPay.getGroupAmount();

    }

    /**
     * @Description: 获取除现金以外未付金额
     * @Param @return
     * @Return double 返回类型
     */
    public double getCashNotPayMent() {
        return floatSubtract(this.actualAmount, (unionPayMoney + memberPayMoney + otherPay.getGroupAmount()));

    }

    /**
     * @Description: 获取除银联以外未付金额
     * @Return double 返回类型
     */
    public double getUnionNotPayMent() {
        return floatSubtract(this.actualAmount, (cashPayMoney + memberPayMoney + otherPay.getGroupAmount()));
    }

    /**
     * @Description: 获取除会员以外未付金额
     * @Return double 返回类型
     */
    public double getMemberNotPayMent() {
        return floatSubtract(this.actualAmount, (cashPayMoney + unionPayMoney + otherPay.getGroupAmount()));
    }

    /**
     * @Description: 获取某支付方式金额
     * @Return double 返回类型
     */
    public double getInputValueByPayModelGroup(PayModelGroup group) {
        switch (group) {
            case CASH:// 现金
                return cashPayMoney;

            case BANK_CARD:// 银联
                return unionPayMoney;

            case VALUE_CARD:// 会员
                return memberPayMoney;

            case OTHER:// 会员
                return otherPay.getGroupAmount();

            default:
                return 0;
        }
    }

    /**
     * @Description: 获取某其它支付未付金额
     * @Param ModelId 其它支付id
     * @Return double 返回类型
     */
    public double getOtherPayNotPayMentByModelId(Long ModelId) {

        return floatSubtract(this.actualAmount,
                (cashPayMoney + unionPayMoney + memberPayMoney + otherPay.getGroupAmountExceptModel(ModelId)));
    }

    /**
     * @Title: getCommonPaySum
     * @Description: 获取输入总金额
     * @Return double 返回类型
     */
    public double getCommonPaySum() {
        double sum = cashPayMoney + unionPayMoney + memberPayMoney + otherPay.getGroupAmount();
        return MathDecimal.round(sum, 2);
    }

    /**
     * @Description: 获取订单金额
     * @Param @return
     * @Return double 返回类型
     */
    public double getTradeAmount() {
        return tradeAmount;
    }

    public double getSaleAmount() {
        //订单金额:对应UI上的折前
        return this.mTradeVo.getTrade().getSaleAmount().doubleValue();
    }

    public void setTradeAmount(double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    /**
     * @Description: 获取实收金额(抹零后的金额)
     * @Param @return
     * @Return float 返回类型
     */
    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    /**
     * @Description: 获取抹零方式
     * @Param @return
     * @Return int 返回类型
     */
    public int getEraseType() {
        return eraseType;
    }

    public void setEraseType(int eraseType) {
        this.eraseType = eraseType;
    }

    /**
     * @Description: 读取储值卡余额
     * @Return double 返回类型
     */
    public double getMemberCardBalance() {
        return memberCardBalance;
    }

    public void setMemberCardBalance(double memberCardBalance) {
        this.memberCardBalance = memberCardBalance;
    }

    /**
     * @Description: 读取积分余额
     * @Return int 返回类型
     */
    public long getMemberIntegral() {
        return memberIntegral;
    }

    public void setMemberIntegral(long memberIntegral) {
        this.memberIntegral = memberIntegral;
    }

    public TradeVo getTradeVo() {
        return mTradeVo;
    }

    public void setTradeVo(TradeVo tradeVo) {
        this.mTradeVo = tradeVo;
    }

    /**
     * @Description: 判断是否是自动输入金额
     * @Return boolean 返回类型
     */
    public boolean isDefaultValue() {
        return isCurrentDefaultInputValue;
    }

    public boolean isDefaultValue(PayModelGroup payModelGroup) {
        return (defaultInputPayGroup == payModelGroup && isCurrentDefaultInputValue);
    }

    public void setDefaultValue(boolean isDefaultValue, PayModelGroup payModelGroup) {
        this.isCurrentDefaultInputValue = isDefaultValue;
        this.defaultInputPayGroup = payModelGroup;
    }

    /* public void setDefaultValue(boolean isDefaultValue) {
         this.isCurrentDefaultInputValue = isDefaultValue;
     }*/
    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public boolean isOrderCenter() {
        return isOrderCenter;
    }

    public void setOrderCenter(boolean isOrderCenter) {
        this.isOrderCenter = isOrderCenter;
    }

    public CustomerResp getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResp customer) {
        this.customer = customer;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public CustomerLoginResp getMemberResp() {
        return memberResp;
    }

    public void setMemberResp(CustomerLoginResp memberResp) {
        this.memberResp = memberResp;
    }

    public boolean isSplit() {
        return isSplit;
    }

    public void setSplit(boolean isSplit) {
        this.isSplit = isSplit;
    }

    public TradeVo getSourceTradeVo() {
        return sourceTradeVo;
    }

    public void setSourceTradeVo(TradeVo sourceTradeVo) {
        this.sourceTradeVo = sourceTradeVo;
    }

    @Override
    public void setCardCustomer(CustomerResp customer) {

    }

    @Override
    public CustomerResp getCardCustomer() {
        return null;
    }

    /**
     * @Description: 是否已经下单
     * @Param @return
     * @Return boolean 返回类型
     */
    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public Reason getReason() {
        return mReason;
    }

    public void setReason(Reason reason) {
        this.mReason = reason;
    }

    public boolean isPrintKitchen() {
        return isPrintKitchen;
    }

    public void setPrintKitchen(boolean isPrintKitchen) {
        this.isPrintKitchen = isPrintKitchen;
    }

    public boolean isPrintedOk() {
        return isPrintedOk;
    }

    public void setPrintedOk(boolean isPrintedOk) {
        this.isPrintedOk = isPrintedOk;
    }

    public String getPaymentUuid() {
        if (paymentUuid == null) {
            paymentUuid = SystemUtils.genOnlyIdentifier();
        }
        return paymentUuid;
    }

    @Override
    public void setPaidPaymentRecords(List<PaymentVo> paymentVoList) {

    }

    @Override
    public boolean isOpenElectronicInvoice() {
        return false;
    }

    @Override
    public List<PaymentItem> getPaidPaymentItems() {
        return null;
    }

    public String getPaymentItemUnionUuid() {
        if (paymentItemUnionUuid == null) {
            paymentItemUnionUuid = SystemUtils.genOnlyIdentifier();
        }
        return paymentItemUnionUuid;
    }

    //add v8.14
    public void addPaymentRecord(PaymentVo paymentVo) {

    }

    //add v8.14
    public List<PaymentVo> getPaymentRecords() {
        return null;
    }

    //根据登录会员生成打印信息 add v8.4
    public String getPaymentItemMemberUuid() {
        if (paymentItemMemberUuid == null) {
            paymentItemMemberUuid = SystemUtils.genOnlyIdentifier();
        }
        return paymentItemMemberUuid;
    }

    public String getPaymentItemCashUuid() {
        if (paymentItemCashUuid == null) {
            paymentItemCashUuid = SystemUtils.genOnlyIdentifier();
        }
        return paymentItemCashUuid;
    }

    public BusinessType getTradeBusinessType() {
        if (this.mTradeVo != null && this.mTradeVo.getTrade() != null)
            return this.mTradeVo.getTrade().getBusinessType();
        return null;
    }

    /**
     * @Description: 各种支付数据验证(找零不能大于100 ， 用户不能为空 ， 未收不能大于0. 。 。)
     * @Param context
     * @Param @return
     * @Return boolean 返回类型
     */
    public boolean checkInputValue(Context context) {

        if (this.actualAmount < 0) {// 退货不验证
            return true;
        }
        // double oddChange = this.getChangeAmount();// 找零金额
        double commPaySum = this.getCommonPaySum();// 总付金额

        if (this.unionPayMoney > floatSubtract(this.actualAmount, this.memberPayMoney)) {
            ToastUtil.showLongToastCenter(context, context.getString(R.string.pay_more_input_alter));
            return false;

        } else if (this.memberPayMoney > floatSubtract(this.actualAmount, this.unionPayMoney)) {
            ToastUtil.showLongToastCenter(context, context.getString(R.string.pay_more_input_alter));
            return false;
        } else if (this.memberPayMoney > this.memberCardBalance) {
            ToastUtil.showLongToastCenter(context, context.getString(R.string.pay_dict_not_enough));
            return false;
        } else if (commPaySum < this.actualAmount) {
            ToastUtil.showLongToastCenter(context, context.getString(R.string.pay_change_error));
            return false;

        }
        /*else if (oddChange >= 100) {

            PopupManager.showLongToastCenter(context, context.getString(R.string.pay_change_more_error));
            return false;
        } */
        else if (Session.getAuthUser() == null) {
            ToastUtil.showLongToastCenter(context, context.getString(R.string.pay_user_not_login));
            return false;
        }
        return true;
    }

    /**
     * @Description: 构建收银数据
     * @Return PaymentVo 返回类型
     */
    public PaymentVo getTradePaymentVo() {
        PaymentVo paymenVo = null;
        Trade trade = null;
        if ((trade = mTradeVo.getTrade()) != null) {
            paymenVo = new PaymentVo();
            Payment payment = new Payment();
            double restAmount = actualAmount;// 未收金额初始值为实收
            payment.validateCreate();

            final List<PaymentItem> paymentItemList = new ArrayList<PaymentItem>();
            // 收银员
            String _operatorName = Session.getAuthUser() != null
                    ? Session.getAuthUser().getName() : trade.getCreatorName();
            long _operatorId = Session.getAuthUser() != null
                    ? Session.getAuthUser().getId() : trade.getCreatorId();

            // 设置支付信息
            payment.setReceivableAmount(trade.getTradeAmount());// 可收金额

            payment.setBeforePrivilegeAmount(mTradeVo.getBeforePrivilegeAmount());

            double moreAmount = getMorePaymentAmount();
            // 如果有溢收
            if (moreAmount > 0) {
                payment.setActualAmount(BigDecimal.valueOf(this.actualAmount + moreAmount));// 实际收金额
            } else {
                payment.setActualAmount(BigDecimal.valueOf(this.actualAmount));// 实际收金额
            }
            payment.setExemptAmount(BigDecimal.valueOf(this.getExemptAmount()));// 抹零金额
            payment.setRelateUuid(trade.getUuid());// 主单uuid
            payment.setRelateId(trade.getId());// 关联id
            //支付类型
            BusinessType businessType = this.getTradeBusinessType();
            PaymentType paymentType = PaymentType.TRADE_SELL;
            if (businessType != null) {
                switch (businessType) {
                    case ONLINE_RECHARGE://会员虚拟卡充值
                        paymentType = PaymentType.MEMBER_RECHARGE;
                        break;
                    case CARD_RECHARGE://会员实体卡充值
                        paymentType = PaymentType.ENTITY_CARD_RECHARGE;
                        break;
                    case ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE://匿名卡售卡储值
                        paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_SELL_RECHARGE;
                        break;
                    case ANONYMOUS_ENTITY_CARD_RECHARGE://匿名卡储值
                        paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_RECHARGE;
                        break;
                    default:
                        break;
                }
            }
            payment.setPaymentType(paymentType);// 交易支付
            // 生成PaymentUUID并缓存
            payment.setUuid(this.getPaymentUuid());
            payment.setCreatorId(_operatorId);// 收银员id
            payment.setCreatorName(_operatorName);// 收银员名字
            payment.setUpdatorId(_operatorId);// 收银员id
            payment.setUpdatorName(_operatorName);// 收银员名字

            paymenVo.setPayment(payment);// 支付主单
            paymenVo.setPaymentItemList(paymentItemList);// 支付明细列表

            if (this.getUnionPayMoney() > 0) {// 银联方式
                if (mPosTransLog != null && isPosUnionPay(mPosTransLog)) {// 银行刷卡相关信息
                    final List<PaymentItemUnionpayVoReq> paymentCards = new ArrayList<PaymentItemUnionpayVoReq>();
                    paymentCards.add(getPaymentItemUnionpayVoReq(mPosTransLog, _operatorId, _operatorName));
                    paymenVo.setPaymentCards(paymentCards);
                }
                PaymentItem paymentItem = new PaymentItem();
                paymentItem.setPaySource(PaySource.CASHIER);
                paymentItem.validateCreate();

                // 生成UUID并缓存,pos刷卡
                if (mPosTransLog != null && isPosUnionPay(mPosTransLog)) {
                    paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                    paymentItem.setUuid(mPosTransLog.getUuid());
                    paymentItem.setPayModeId(PayModeId.POS_CARD.value());
                } else {
                    paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                    paymentItem.setUuid(this.getPaymentItemUnionUuid());
                    paymentItem.setPayModeId(PayModeId.BANK_CARD.value());
                }
                paymentItem.setPaymentUuid(payment.getUuid());
                paymentItem.setPayModelGroup(PayModelGroup.BANK_CARD);

                // 支付类型名称
                paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));
                paymentItem.setFaceAmount(BigDecimal.valueOf(this.unionPayMoney));// 票面金额
                paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.unionPayMoney));// 实付款
                paymentItem.setCreatorId(_operatorId);// 收银员id
                paymentItem.setCreatorName(_operatorName);// 收银员名字
                paymentItemList.add(paymentItem);

                restAmount = floatSubtract(restAmount, this.unionPayMoney);
            }
            if (this.getMemberPayMoney() > 0) {// 会员方式

                PaymentItem paymentItem = new PaymentItem();
                if (customer != null && ecCard == null) {
                    PaymentItemExtra paymentItemExtra = new PaymentItemExtra();
                    paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                    paymentItemExtra.setCustomerId(Long.valueOf(memberId));//如果是虚拟卡就不传
                    //paymentItemExtra.setEntityNo("");//如果是虚拟卡卡号就不传entityNo

                    paymentItem.setRelateId(memberId);// 会员id
                    paymentItem.setPayModeId(PayModeId.MEMBER_CARD.value());
                    paymentItem.setPaymentItemExtra(paymentItemExtra);

                } else if (customer == null && ecCard != null) {
                    if (ecCard.getCardType() == EntityCardType.CUSTOMER_ENTITY_CARD) {
                        PaymentItemExtra paymentItemExtra = new PaymentItemExtra();
                        paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                        paymentItemExtra.setCustomerId(ecCard.getCustomer().getCustomerid());//传入实体卡绑定的会员ID号
                        paymentItemExtra.setEntityNo(ecCard.getCardNum());//实体卡就传入实体卡卡号

                        paymentItem.setRelateId(ecCard.getCustomer().getCustomerid() + "");// 会员id
                        paymentItem.setPayModeId(PayModeId.ENTITY_CARD.value());
                        paymentItem.setPaymentItemExtra(paymentItemExtra);
                    } else if (ecCard.getCardType() == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                        paymentItem.setRelateId(ecCard.getCardNum());// 临时卡应该传入卡号
                        paymentItem.setPayModeId(PayModeId.ANONYMOUS_ENTITY_CARD.value());
                    }

                }
                paymentItem.setPaySource(PaySource.CASHIER);
                paymentItem.validateCreate();
                // 退款来源
                paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                paymentItem.setUuid(this.getPaymentItemMemberUuid());
                // paymentItem.setRelateId(memberId);// 会员id
                paymentItem.setPaymentUuid(payment.getUuid());
                // paymentItem.setPayModeId(PayModeId.MEMBER_CARD.value());
                paymentItem.setPayModelGroup(PayModelGroup.VALUE_CARD);
                // 支付类型名称
                paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));

                paymentItem.setFaceAmount(BigDecimal.valueOf(this.memberPayMoney));// 票面金额
                paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.memberPayMoney));// 实付款
                paymentItem.setCreatorId(_operatorId);// 收银员id
                paymentItem.setCreatorName(_operatorName);// 收银员名字
                paymentItemList.add(paymentItem);
                restAmount = floatSubtract(restAmount, this.memberPayMoney);
            }

            // 其它支付方式
            if (!otherPay.isEmpty()) {
                // 自定义自由输入选项不考虑找零
                List<PayModelItem> faceValueList = otherPay.getAllPayModelItems();
                boolean isContainsMeituan = false;
                if (faceValueList != null && !faceValueList.isEmpty()) {
                    for (PayModelItem model : faceValueList) {
                        PaymentItem paymentItem = new PaymentItem();
                        paymentItem.setPaySource(PaySource.CASHIER);
                        paymentItem.validateCreate();
                        paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                        paymentItem.setUuid(SystemUtils.genOnlyIdentifier());// 生成UUID
                        paymentItem.setPaymentUuid(payment.getUuid());
                        paymentItem.setPayModelGroup(otherPay.getGroup());
                        // 点评券
                       /* if (model.getPayMode() == PayModeId.DIANPING_COUPON) {
                            paymentItem.setPayModeId(PayModeId.DIANPING_COUPON.value());
                            paymentItem.setPayModeName(model.getTicketInfo().getDealTitle());// 券名
                            paymentItem.setFaceAmount(model.getUsedValue());// 点评券市场价
                            paymentItem.setRelateId(model.getTicketInfo().getSerialNumber());

                        } else */
                        if (model.getPayMode() == PayModeId.MEITUAN_TUANGOU) {//美团点评券
                            paymentItem.setPayModeId(PayModeId.MEITUAN_TUANGOU.value());
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));// 券名
                            paymentItem.setFaceAmount(model.getUsedValue());// 点评券市场价
                            paymentItem.setRelateId(model.getTuanGouCouponDetail().getSerialNumber());
                            //美团点评券详细信息
                            PaymentItemGroupon pig = new PaymentItemGroupon();
                            pig.setGrouponId(model.getTuanGouCouponDetail().getGrouponId());
                            pig.setDealTitle(model.getTuanGouCouponDetail().getDealTitle());
                            pig.setMarketPrice(model.getTuanGouCouponDetail().getMarketPrice());
                            pig.setPrice(model.getTuanGouCouponDetail().getPrice());
                            pig.setUseCount(model.getUsedCount());
                            paymentItem.setPaymentItemGroupon(pig);
                            isContainsMeituan = true;
                        } else {// 其它自定义支付
                            paymentItem.setPayModeId(model.getPayModelId());
                            paymentItem.setPayModeName(model.getPaymentModeShop().getName());
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                        }

                        if (restAmount >= model.getUsedValue().doubleValue()) {
                            // 不满足溢收
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额

                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款

                            restAmount = floatSubtract(restAmount, model.getUsedValue().doubleValue());

                        } else {// 满足溢收
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额

                            paymentItem.setUsefulAmount(BigDecimal.valueOf(restAmount));// 实付款

                            restAmount = 0;
                        }
                        // }
                        paymentItem.setCreatorId(_operatorId);// 收银员id
                        paymentItem.setCreatorName(_operatorName);// 收银员名字
                        paymentItemList.add(paymentItem);
                    }
                }
                //如果有美团券，重新计算实收（用售价计算）
                if (isContainsMeituan) {
                    payment.setShopActualAmount(this.getActualPaymentAmount());//  商户实际收金额
                }
            }
            if (this.getCashPayMoney() > 0) {// 现金方式
                PaymentItem paymentItem = new PaymentItem();
                paymentItem.setPaySource(PaySource.CASHIER);
                paymentItem.validateCreate();
                // 生成UUID并缓存
                paymentItem.setUuid(this.getPaymentItemCashUuid());
                paymentItem.setPaymentUuid(payment.getUuid());
                paymentItem.setPayModeId(PayModeId.CASH.value());
                paymentItem.setPayModelGroup(PayModelGroup.CASH);
                paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                // 支付类型名称
                paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));

                paymentItem.setFaceAmount(BigDecimal.valueOf(this.cashPayMoney));// 票面金额
                paymentItem.setChangeAmount(BigDecimal.valueOf(floatSubtract(this.cashPayMoney, restAmount)));// 找零金额
                paymentItem.setUsefulAmount(BigDecimal.valueOf(restAmount));// 实付款
                paymentItem.setCreatorId(_operatorId);// 收银员id
                paymentItem.setCreatorName(_operatorName);// 收银员名字
                paymentItemList.add(paymentItem);
            }

            // 如果收银明细为空,默认添加零的现金记录
            if (paymentItemList.size() <= 0) {// 快捷支付
                // CashType cashType = null;
                PaymentItem paymentItem = new PaymentItem();
                paymentItem.setPaySource(PaySource.CASHIER);
                paymentItem.validateCreate();
                // 生成UUID并缓存
                paymentItem.setUuid(this.getPaymentItemCashUuid());
                paymentItem.setPaymentUuid(payment.getUuid());
                paymentItem.setPayModeId(PayModeId.CASH.value());
                paymentItem.setPayModelGroup(PayModelGroup.CASH);
                paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                // 支付类型名称
                paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));

                paymentItem.setFaceAmount(BigDecimal.valueOf(this.cashPayMoney));// 票面金额
                if (BigDecimal.valueOf(this.cashPayMoney).compareTo(BigDecimal.ZERO) < 0) {// 无单退货
                    paymentItem.setChangeAmount(BigDecimal.ZERO);
                    paymentItem.setUsefulAmount(BigDecimal.valueOf(this.cashPayMoney));
                } else {
                    paymentItem.setChangeAmount(BigDecimal.valueOf(this.getChangeAmount()));// 找零金额
                    paymentItem.setUsefulAmount(BigDecimal.valueOf(this.cashPayMoney - this.getChangeAmount()));// 实付款
                }
                paymentItem.setCreatorId(_operatorId);// 收银员id
                paymentItem.setCreatorName(_operatorName);// 收银员名字
                paymentItemList.add(paymentItem);
            }
        }
        return paymenVo;
    }

    private PaymentItemUnionpayVoReq getPaymentItemUnionpayVoReq(PosTransLog log, Long operatorId, String operatorName) {
        PaymentItemUnionpayVoReq vo = null;
        if (log != null) {
            vo = new PaymentItemUnionpayVoReq();
            vo.setCreatorId(operatorId);
            vo.setCreatorName(operatorName);
            vo.setCardNumber(log.getCardNumber());
            vo.setCardName(log.getCardName());
            vo.setExpireDate(log.getExpireDate());
            vo.setIssNumber(log.getIssNumber());
            vo.setIssName(log.getIssName());
            vo.setPaymentItemUnionpay(
                    getPaymentItemUnionpayReq(PaySettingCache.getmErpComRel(), log, operatorId, operatorName));
            // 设备终端号
            PaymentDeviceReq device = new PaymentDeviceReq();
            device.setDeviceNumber(log.getTerminalNumber());
            if (PaySettingCache.getmErpComRel() != null)
                device.setPosChannelId(PaySettingCache.getmErpComRel().getBankChannelId());
            vo.setPaymentDevice(device);
        }
        return vo;
    }

    private PaymentItemUnionpayReq getPaymentItemUnionpayReq(ErpCommercialRelation erpComRel, PosTransLog log,
                                                             Long operatorId, String operatorName) {
        PaymentItemUnionpayReq req = new PaymentItemUnionpayReq();
        req.setCreatorId(operatorId);
        req.setCreatorName(operatorName);
        req.setUuid(SystemUtils.genOnlyIdentifier());
        req.setPaymentItemUuid(log.getUuid());
        req.setTransDate(DateTimeUtils.parseStringDateTime(log.getTransDate() + log.getTransTime()));
        req.setTransType(PosBusinessType.EXPENSE);
        if (log.getAmount() != null) {
            req.setAmount(log.getAmount());
            if (erpComRel != null) {
                Double backRates = erpComRel.getBankRates();
                if (backRates != null) {
                    req.setRates(backRates);// 费率
                    Double fee = log.getAmount() * backRates;
                    req.setFee(MathDecimal.round(fee, 4));// 手续费
                } else {
                    req.setFee(0D);
                }
                req.setPosChannelId(erpComRel.getBankChannelId());
            }
        } else {
            req.setAmount(0);
            req.setFee(0D);
            if (erpComRel != null) {
                req.setRates(erpComRel.getBankRates());
                req.setPosChannelId(erpComRel.getBankChannelId());
            }
        }
        req.setHostSerialNumber(log.getHostSerialNumber());
        req.setPosTraceNumber(log.getPosTraceNumber());
        req.setBatchNumber(log.getBatchNumber());
        req.setTerminalNumber(log.getTerminalNumber());// 设备终端号
        req.setAppname(log.getAppName());
        return req;
    }

    public boolean isPosUnionPay(PosTransLog log) {
        boolean isPos = false;
        if (log != null && log.getAmount() != null) {
            Double unionAmount = unionPayMoney * 100;
            if (log.getAmount() == unionAmount.intValue()) {
                isPos = true;
            }
        }
        return isPos;
    }

    /**
     * @Description:浮点数求差公共方法,保留2位小数
     * @Param arg1 被減数
     * @Param arg2 减数
     * @Param @return
     * @Return float 返回类型
     */
    public static double floatSubtract(double arg1, double arg2) {
        BigDecimal bigDecimal = new BigDecimal(arg1);

        BigDecimal bigDecima2 = new BigDecimal(arg2);

        double result = bigDecimal.subtract(bigDecima2).doubleValue();

        return MathDecimal.round(result, 2);
    }

    /**
     * "Y######.00"格式 将金额格式化添加货币符号
     */
    public static String formatCash(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            return ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(value);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return ShopInfoCfg.getInstance().getCurrencySymbol() + value + "";
    }

    /**
     * "######.00"格式 将金额格式化
     */
    public static String formatCashAmount(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            return df.format(value);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return value + "";
    }

    /**
     * @Description: 验证金额输入是否合法
     * @Param value
     * @Param @return
     * @Return boolean 返回类型
     */
    public static boolean isMatchCashFormat(String value) {
        if (value == null) {
            return false;
        }
        return value.matches(CASH_FORMAT_REG);
    }

    public EcCard getEcCard() {
        return ecCard;
    }

    public void setEcCard(EcCard ecCard) {
        this.ecCard = ecCard;
    }

    /**
     * 设置实体卡的信息因为 打印的时候需要 memberResp的值 所以需要设置这个值
     *
     * @Title: setPrintMemeberInfoByCard
     * @Description:
     * @Return void 返回类型
     */
    public void setPrintMemeberInfoByCard() {
        // 如果是有实体卡信息
        CustomerLoginResp loginResp = new CustomerLoginResp();
        if (loginResp != null && ecCard != null) {
            loginResp.setCustomerId(ecCard.getId());
            loginResp.setLevel(ecCard.getCardLevel().getCardLevelName());
            loginResp.setSex(Sex.MALE);

            BigDecimal integralAccount = new BigDecimal(0);// 会员积分
            BigDecimal valueCardAccount = new BigDecimal(memberCardBalance);// 会员余额
            switch (ecCard.getCardType()) {
                case CUSTOMER_ENTITY_CARD:
                    // 得到 integralAccount 的值 valueCardAccount的值
                    if (ecCard.getIntegralAccount() == null) {
                        integralAccount = BigDecimal.valueOf(0);
                    } else {
                        if (ecCard.getIntegralAccount().getIntegral() == null) {
                            integralAccount = BigDecimal.valueOf(0);
                        } else {
                            integralAccount =
                                    MathDecimal.round(BigDecimal.valueOf(ecCard.getIntegralAccount().getIntegral()), 2);
                        }

                    }

               /* if (ecCard.getValueCardAccount() == null) {
                    valueCardAccount = BigDecimal.valueOf(0);
                } else {
                    if (ecCard.getValueCardAccount().getRemainValue() == null) {
                        valueCardAccount = BigDecimal.valueOf(0);
                    } else {
                        valueCardAccount =
                                MathDecimal.round(BigDecimal.valueOf(ecCard.getValueCardAccount().getRemainValue()), 2);
                    }

                }*/
                    loginResp.setCustomerName(ecCard.getCardNum());
                    break;
                case ANONYMOUS_ENTITY_CARD:
                   /* if (ecCard != null && null != ecCard.getEctempAccount() && null != ecCard.getEctempAccount().getRemainValue()) {
                        integralAccount = BigDecimal.valueOf(0);

                   *//* valueCardAccount =
                            MathDecimal.round(BigDecimal.valueOf(ecCard.getEctempAccount().getRemainValue()), 2);*//*

                    } else {
                        integralAccount = BigDecimal.valueOf(0);
                   *//* valueCardAccount = BigDecimal.valueOf(0);*//*
                    }*/
                    integralAccount = BigDecimal.valueOf(0);
                    loginResp.setCustomerName("");//匿名卡不需要传入名字
                    break;
                default:
                    break;

            }

            loginResp.setIntegralBalance(integralAccount);// 会员积分
            loginResp.setValueCardBalance(valueCardAccount);// 会员余额
        }
        setMemberResp(loginResp);
    }
}
