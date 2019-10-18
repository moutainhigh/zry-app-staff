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


public class CashInfoManager implements IPaymentInfo {
    private static final String TAG = CashInfoManager.class.getSimpleName();

    public static final String CASH_FORMAT_REG = "([0-9]{1,8}[.][0-9]{0,2})|([0-9]{1,8})";

    private int eraseType = 8;
    private double tradeAmount;
    private double actualAmount;
    private double cashPayMoney;
    private double unionPayMoney;
    private double memberPayMoney;
    private double memberCardBalance;
    private long memberIntegral;
    private GroupPay otherPay;
    private boolean isCurrentDefaultInputValue;
    private PayModelGroup defaultInputPayGroup;

    private boolean isOrderCenter;
    private boolean isOrdered;
    private boolean isQuickPay;
    private boolean isPrintedOk;
    private String memberId;
    private CustomerResp customer;
    private String memberPassword;
    private CustomerLoginResp memberResp;
    private TradeVo mTradeVo;
    private String paymentItemCashUuid;
    private String paymentItemUnionUuid;
    private String paymentItemMemberUuid;
    private String paymentUuid;
    private String mTradeUUid;
    private boolean isSplit;
    private TradeVo sourceTradeVo;
    private Reason mReason;
    private boolean isPrintKitchen = true;
    private PosTransLog mPosTransLog;
    private EcCard ecCard;

    private BigDecimal chargeMoney;
    private BigDecimal chargeSendMoney;
    private long customerId;
    private boolean reviseStock = true;
    private int defaultPaymentMenuType = -1;
    private String id;
    private PayScene mPayScene = PayScene.SCENE_CODE_SHOP;
    private PayActionPage mPayActionPage = PayActionPage.COMPAY;
    public boolean getIsReviseStock() {
        return reviseStock;
    }

    public void setReviseStock(boolean reviseStock) {
        this.reviseStock = reviseStock;
    }


    private boolean isPrintInvoice;

    private ElectronicInvoiceVo electronicInvoiceVo;

        private CashInfoManager() {
        otherPay = new GroupPay(PayModelGroup.OTHER);        this.id = SystemUtils.genOnlyIdentifier();
    }

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


    public double getCashPayMoney() {
        return cashPayMoney;
    }


    public void setCashPayMoney(double cashPayMoney) {
        this.cashPayMoney = cashPayMoney;
    }


    public double getUnionPayMoney() {
        return unionPayMoney;
    }


    public void setUnionPayMoney(double unionPayMoney) {
        this.unionPayMoney = unionPayMoney;
    }


    public double getMemberPayMoney() {
        return memberPayMoney;
    }


    public void setMemberPayMoney(double membePayMoney) {
        this.memberPayMoney = membePayMoney;
    }


    public GroupPay getOtherPay() {
        return otherPay;
    }


    public void setOtherPay(GroupPay otherPay) {
        this.otherPay = otherPay;
    }


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


    public double getNotPayMent() {
        return floatSubtract(this.actualAmount, this.getCommonPaySum());
    }

        public boolean enablePay() {
        return getNotPayMent() <= 0;
    }


    public String getNotPayMentStr() {

        return formatCash(getNotPayMent());

    }


    public String getMorePaymentStr() {
        return formatCash(floatSubtract(this.getCommonPaySum(), this.actualAmount));
    }


    public double getMorePaymentAmount() {
        if (new BigDecimal(otherPay.getGroupAmount()).compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        } else {
            return floatSubtract(otherPay.getGroupAmount() + unionPayMoney + memberPayMoney, this.actualAmount);
        }
    }


    private BigDecimal getActualPaymentAmount() {
        if (new BigDecimal(otherPay.getGroupAmount()).compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(this.actualAmount);
        } else {
            return BigDecimal.valueOf(floatSubtract(otherPay.getGroupActualAmount() + unionPayMoney + memberPayMoney + cashPayMoney, this.getChangeAmount()));
        }
    }


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


    public double getNotCashPayMent() {
        return unionPayMoney + memberPayMoney + otherPay.getGroupAmount();

    }


    public double getCashNotPayMent() {
        return floatSubtract(this.actualAmount, (unionPayMoney + memberPayMoney + otherPay.getGroupAmount()));

    }


    public double getUnionNotPayMent() {
        return floatSubtract(this.actualAmount, (cashPayMoney + memberPayMoney + otherPay.getGroupAmount()));
    }


    public double getMemberNotPayMent() {
        return floatSubtract(this.actualAmount, (cashPayMoney + unionPayMoney + otherPay.getGroupAmount()));
    }


    public double getInputValueByPayModelGroup(PayModelGroup group) {
        switch (group) {
            case CASH:                return cashPayMoney;

            case BANK_CARD:                return unionPayMoney;

            case VALUE_CARD:                return memberPayMoney;

            case OTHER:                return otherPay.getGroupAmount();

            default:
                return 0;
        }
    }


    public double getOtherPayNotPayMentByModelId(Long ModelId) {

        return floatSubtract(this.actualAmount,
                (cashPayMoney + unionPayMoney + memberPayMoney + otherPay.getGroupAmountExceptModel(ModelId)));
    }


    public double getCommonPaySum() {
        double sum = cashPayMoney + unionPayMoney + memberPayMoney + otherPay.getGroupAmount();
        return MathDecimal.round(sum, 2);
    }


    public double getTradeAmount() {
        return tradeAmount;
    }

    public double getSaleAmount() {
                return this.mTradeVo.getTrade().getSaleAmount().doubleValue();
    }

    public void setTradeAmount(double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }


    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }


    public int getEraseType() {
        return eraseType;
    }

    public void setEraseType(int eraseType) {
        this.eraseType = eraseType;
    }


    public double getMemberCardBalance() {
        return memberCardBalance;
    }

    public void setMemberCardBalance(double memberCardBalance) {
        this.memberCardBalance = memberCardBalance;
    }


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

        public void addPaymentRecord(PaymentVo paymentVo) {

    }

        public List<PaymentVo> getPaymentRecords() {
        return null;
    }

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


    public boolean checkInputValue(Context context) {

        if (this.actualAmount < 0) {            return true;
        }
                double commPaySum = this.getCommonPaySum();
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

        else if (Session.getAuthUser() == null) {
            ToastUtil.showLongToastCenter(context, context.getString(R.string.pay_user_not_login));
            return false;
        }
        return true;
    }


    public PaymentVo getTradePaymentVo() {
        PaymentVo paymenVo = null;
        Trade trade = null;
        if ((trade = mTradeVo.getTrade()) != null) {
            paymenVo = new PaymentVo();
            Payment payment = new Payment();
            double restAmount = actualAmount;            payment.validateCreate();

            final List<PaymentItem> paymentItemList = new ArrayList<PaymentItem>();
                        String _operatorName = Session.getAuthUser() != null
                    ? Session.getAuthUser().getName() : trade.getCreatorName();
            long _operatorId = Session.getAuthUser() != null
                    ? Session.getAuthUser().getId() : trade.getCreatorId();

                        payment.setReceivableAmount(trade.getTradeAmount());
            payment.setBeforePrivilegeAmount(mTradeVo.getBeforePrivilegeAmount());

            double moreAmount = getMorePaymentAmount();
                        if (moreAmount > 0) {
                payment.setActualAmount(BigDecimal.valueOf(this.actualAmount + moreAmount));            } else {
                payment.setActualAmount(BigDecimal.valueOf(this.actualAmount));            }
            payment.setExemptAmount(BigDecimal.valueOf(this.getExemptAmount()));            payment.setRelateUuid(trade.getUuid());            payment.setRelateId(trade.getId());                        BusinessType businessType = this.getTradeBusinessType();
            PaymentType paymentType = PaymentType.TRADE_SELL;
            if (businessType != null) {
                switch (businessType) {
                    case ONLINE_RECHARGE:                        paymentType = PaymentType.MEMBER_RECHARGE;
                        break;
                    case CARD_RECHARGE:                        paymentType = PaymentType.ENTITY_CARD_RECHARGE;
                        break;
                    case ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE:                        paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_SELL_RECHARGE;
                        break;
                    case ANONYMOUS_ENTITY_CARD_RECHARGE:                        paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_RECHARGE;
                        break;
                    default:
                        break;
                }
            }
            payment.setPaymentType(paymentType);                        payment.setUuid(this.getPaymentUuid());
            payment.setCreatorId(_operatorId);            payment.setCreatorName(_operatorName);            payment.setUpdatorId(_operatorId);            payment.setUpdatorName(_operatorName);
            paymenVo.setPayment(payment);            paymenVo.setPaymentItemList(paymentItemList);
            if (this.getUnionPayMoney() > 0) {                if (mPosTransLog != null && isPosUnionPay(mPosTransLog)) {                    final List<PaymentItemUnionpayVoReq> paymentCards = new ArrayList<PaymentItemUnionpayVoReq>();
                    paymentCards.add(getPaymentItemUnionpayVoReq(mPosTransLog, _operatorId, _operatorName));
                    paymenVo.setPaymentCards(paymentCards);
                }
                PaymentItem paymentItem = new PaymentItem();
                paymentItem.setPaySource(PaySource.CASHIER);
                paymentItem.validateCreate();

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

                                paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));
                paymentItem.setFaceAmount(BigDecimal.valueOf(this.unionPayMoney));                paymentItem.setChangeAmount(BigDecimal.valueOf(0));                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.unionPayMoney));                paymentItem.setCreatorId(_operatorId);                paymentItem.setCreatorName(_operatorName);                paymentItemList.add(paymentItem);

                restAmount = floatSubtract(restAmount, this.unionPayMoney);
            }
            if (this.getMemberPayMoney() > 0) {
                PaymentItem paymentItem = new PaymentItem();
                if (customer != null && ecCard == null) {
                    PaymentItemExtra paymentItemExtra = new PaymentItemExtra();
                    paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                    paymentItemExtra.setCustomerId(Long.valueOf(memberId));
                    paymentItem.setRelateId(memberId);                    paymentItem.setPayModeId(PayModeId.MEMBER_CARD.value());
                    paymentItem.setPaymentItemExtra(paymentItemExtra);

                } else if (customer == null && ecCard != null) {
                    if (ecCard.getCardType() == EntityCardType.CUSTOMER_ENTITY_CARD) {
                        PaymentItemExtra paymentItemExtra = new PaymentItemExtra();
                        paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                        paymentItemExtra.setCustomerId(ecCard.getCustomer().getCustomerid());                        paymentItemExtra.setEntityNo(ecCard.getCardNum());
                        paymentItem.setRelateId(ecCard.getCustomer().getCustomerid() + "");                        paymentItem.setPayModeId(PayModeId.ENTITY_CARD.value());
                        paymentItem.setPaymentItemExtra(paymentItemExtra);
                    } else if (ecCard.getCardType() == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                        paymentItem.setRelateId(ecCard.getCardNum());                        paymentItem.setPayModeId(PayModeId.ANONYMOUS_ENTITY_CARD.value());
                    }

                }
                paymentItem.setPaySource(PaySource.CASHIER);
                paymentItem.validateCreate();
                                paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                paymentItem.setUuid(this.getPaymentItemMemberUuid());
                                paymentItem.setPaymentUuid(payment.getUuid());
                                paymentItem.setPayModelGroup(PayModelGroup.VALUE_CARD);
                                paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));

                paymentItem.setFaceAmount(BigDecimal.valueOf(this.memberPayMoney));                paymentItem.setChangeAmount(BigDecimal.valueOf(0));                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.memberPayMoney));                paymentItem.setCreatorId(_operatorId);                paymentItem.setCreatorName(_operatorName);                paymentItemList.add(paymentItem);
                restAmount = floatSubtract(restAmount, this.memberPayMoney);
            }

                        if (!otherPay.isEmpty()) {
                                List<PayModelItem> faceValueList = otherPay.getAllPayModelItems();
                boolean isContainsMeituan = false;
                if (faceValueList != null && !faceValueList.isEmpty()) {
                    for (PayModelItem model : faceValueList) {
                        PaymentItem paymentItem = new PaymentItem();
                        paymentItem.setPaySource(PaySource.CASHIER);
                        paymentItem.validateCreate();
                        paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                        paymentItem.setUuid(SystemUtils.genOnlyIdentifier());                        paymentItem.setPaymentUuid(payment.getUuid());
                        paymentItem.setPayModelGroup(otherPay.getGroup());

                        if (model.getPayMode() == PayModeId.MEITUAN_TUANGOU) {                            paymentItem.setPayModeId(PayModeId.MEITUAN_TUANGOU.value());
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setRelateId(model.getTuanGouCouponDetail().getSerialNumber());
                                                        PaymentItemGroupon pig = new PaymentItemGroupon();
                            pig.setGrouponId(model.getTuanGouCouponDetail().getGrouponId());
                            pig.setDealTitle(model.getTuanGouCouponDetail().getDealTitle());
                            pig.setMarketPrice(model.getTuanGouCouponDetail().getMarketPrice());
                            pig.setPrice(model.getTuanGouCouponDetail().getPrice());
                            pig.setUseCount(model.getUsedCount());
                            paymentItem.setPaymentItemGroupon(pig);
                            isContainsMeituan = true;
                        } else {                            paymentItem.setPayModeId(model.getPayModelId());
                            paymentItem.setPayModeName(model.getPaymentModeShop().getName());
                            paymentItem.setFaceAmount(model.getUsedValue());                        }

                        if (restAmount >= model.getUsedValue().doubleValue()) {
                                                        paymentItem.setChangeAmount(BigDecimal.valueOf(0));
                            paymentItem.setUsefulAmount(model.getUsedValue());
                            restAmount = floatSubtract(restAmount, model.getUsedValue().doubleValue());

                        } else {                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));
                            paymentItem.setUsefulAmount(BigDecimal.valueOf(restAmount));
                            restAmount = 0;
                        }
                                                paymentItem.setCreatorId(_operatorId);                        paymentItem.setCreatorName(_operatorName);                        paymentItemList.add(paymentItem);
                    }
                }
                                if (isContainsMeituan) {
                    payment.setShopActualAmount(this.getActualPaymentAmount());                }
            }
            if (this.getCashPayMoney() > 0) {                PaymentItem paymentItem = new PaymentItem();
                paymentItem.setPaySource(PaySource.CASHIER);
                paymentItem.validateCreate();
                                paymentItem.setUuid(this.getPaymentItemCashUuid());
                paymentItem.setPaymentUuid(payment.getUuid());
                paymentItem.setPayModeId(PayModeId.CASH.value());
                paymentItem.setPayModelGroup(PayModelGroup.CASH);
                paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                                paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));

                paymentItem.setFaceAmount(BigDecimal.valueOf(this.cashPayMoney));                paymentItem.setChangeAmount(BigDecimal.valueOf(floatSubtract(this.cashPayMoney, restAmount)));                paymentItem.setUsefulAmount(BigDecimal.valueOf(restAmount));                paymentItem.setCreatorId(_operatorId);                paymentItem.setCreatorName(_operatorName);                paymentItemList.add(paymentItem);
            }

                        if (paymentItemList.size() <= 0) {                                PaymentItem paymentItem = new PaymentItem();
                paymentItem.setPaySource(PaySource.CASHIER);
                paymentItem.validateCreate();
                                paymentItem.setUuid(this.getPaymentItemCashUuid());
                paymentItem.setPaymentUuid(payment.getUuid());
                paymentItem.setPayModeId(PayModeId.CASH.value());
                paymentItem.setPayModelGroup(PayModelGroup.CASH);
                paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                                paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));

                paymentItem.setFaceAmount(BigDecimal.valueOf(this.cashPayMoney));                if (BigDecimal.valueOf(this.cashPayMoney).compareTo(BigDecimal.ZERO) < 0) {                    paymentItem.setChangeAmount(BigDecimal.ZERO);
                    paymentItem.setUsefulAmount(BigDecimal.valueOf(this.cashPayMoney));
                } else {
                    paymentItem.setChangeAmount(BigDecimal.valueOf(this.getChangeAmount()));                    paymentItem.setUsefulAmount(BigDecimal.valueOf(this.cashPayMoney - this.getChangeAmount()));                }
                paymentItem.setCreatorId(_operatorId);                paymentItem.setCreatorName(_operatorName);                paymentItemList.add(paymentItem);
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
                    req.setRates(backRates);                    Double fee = log.getAmount() * backRates;
                    req.setFee(MathDecimal.round(fee, 4));                } else {
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
        req.setTerminalNumber(log.getTerminalNumber());        req.setAppname(log.getAppName());
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


    public static double floatSubtract(double arg1, double arg2) {
        BigDecimal bigDecimal = new BigDecimal(arg1);

        BigDecimal bigDecima2 = new BigDecimal(arg2);

        double result = bigDecimal.subtract(bigDecima2).doubleValue();

        return MathDecimal.round(result, 2);
    }


    public static String formatCash(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            return ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(value);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return ShopInfoCfg.getInstance().getCurrencySymbol() + value + "";
    }


    public static String formatCashAmount(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            return df.format(value);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return value + "";
    }


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


    public void setPrintMemeberInfoByCard() {
                CustomerLoginResp loginResp = new CustomerLoginResp();
        if (loginResp != null && ecCard != null) {
            loginResp.setCustomerId(ecCard.getId());
            loginResp.setLevel(ecCard.getCardLevel().getCardLevelName());
            loginResp.setSex(Sex.MALE);

            BigDecimal integralAccount = new BigDecimal(0);            BigDecimal valueCardAccount = new BigDecimal(memberCardBalance);            switch (ecCard.getCardType()) {
                case CUSTOMER_ENTITY_CARD:
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


                    loginResp.setCustomerName(ecCard.getCardNum());
                    break;
                case ANONYMOUS_ENTITY_CARD:

                    integralAccount = BigDecimal.valueOf(0);
                    loginResp.setCustomerName("");                    break;
                default:
                    break;

            }

            loginResp.setIntegralBalance(integralAccount);            loginResp.setValueCardBalance(valueCardAccount);        }
        setMemberResp(loginResp);
    }
}
