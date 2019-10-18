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



public class PaymentInfoManager implements IPaymentInfo {
        private boolean isOrderCenter;
        private boolean isOrdered;

    private boolean isSplit;
        private boolean isPrintedOk;
        private int mEraseType = 8;
        private boolean isOpenElectronicInvoice;
        private double mExemptAmount;
        private TradeVo mTradeVo;
        private TradeVo sourceTradeVo;
        private GroupPay mInputPayModels;
        private transient CusomerPayInfo mCusomerPayInfo;
        private PaidPaymentRecord mPaidPaymentRecords;
    private String paymentUuid;    private String id;    private transient IUpdateUICallback mUILitenner;
    private transient IFinishPayCallback mFinishPayCallback;    private int defaultPaymentMenuType = -1;    private PayScene mPayScene = PayScene.SCENE_CODE_SHOP;        private int quickPayType = -1;
    private PayActionPage mPayActionPage = PayActionPage.COMPAY;     private Boolean isGroupPay = true;

        public static synchronized PaymentInfoManager getNewInstance(IUpdateUICallback updateUICallback) {
        return new PaymentInfoManager(updateUICallback);
    }

        private PaymentInfoManager() {
                this.mInputPayModels = new GroupPay();
                this.mCusomerPayInfo = new CusomerPayInfo();
                this.mPaidPaymentRecords = new PaidPaymentRecord();

        this.id = SystemUtils.genOnlyIdentifier();
    }

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
                    mUILitenner.updatePaymentInfo();                                    }
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
                        mUILitenner.updatePaymentInfo();                        EventBus.getDefault().post(new ExemptEventUpdate(getEraseType()));                     }
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
                return CashInfoManager.floatSubtract(getTradeAmount(), getPaidAmount());
    }

    @Override
    public Double getActualAmount() {
                if (this.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING) {
            return CashInfoManager.floatSubtract(mTradeVo.getBakeryBookingAmount().add(mTradeVo.getPayDepositAmount()).doubleValue(), getExemptAmount());
        }

        return CashInfoManager.floatSubtract(getTradeAmount(), (getExemptAmount() + getPaidAmount()));
    }

    @Override
    public double getTradeAmount() {
                if (this.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && this.mTradeVo.isNeedToPayDeposit()) {
            return this.mTradeVo.getTradeDeposit().getDepositPay().doubleValue();
        }

        if (this.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING) {

            return mTradeVo.getBakeryBookingAmount().add(mTradeVo.getUnPayDepositAmount()).doubleValue();
        }


                return this.mTradeVo.getTrade().getTradeAmount().doubleValue();
    }

    public double getSaleAmount() {
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
    public void setEraseType(int eraseType) {        mEraseType = eraseType;
    }

    @Override
    public double getOtherPayNotPayMentByModelId(Long ModelId) {
        return CashInfoManager.floatSubtract(this.getActualAmount(),
                mInputPayModels.getGroupAmountExceptModel(ModelId));
    }

    @Override
    public boolean enablePay() {
        if (DoPayUtils.isSupportGroupPay(this, PaySettingCache.isSupportGroupPay())) {            return this.mInputPayModels.getGroupAmount() > 0 ? true : false;
        } else {
            return this.mInputPayModels.getGroupAmount() >= this.getActualAmount();        }
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
        this.setPrintMemeberInfoByCustomer();    }

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

        public double getPaidAmount() {
        return this.mPaidPaymentRecords.getPaidAmount();
    }

        public boolean enableExempt() {

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

        @Override
    public boolean isNeedToPayDeposit() {
        if (this.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT) {
            return false;
        }
                if (this.getTradeBusinessType() == BusinessType.BOOKING_LIST && this.getTradeAmount() > 0 && this.getTradeVo().isNeedToPayDeposit()) {
            return (!this.getTradeVo().isPaidTradeposit());
        }
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

    public void setFinishPayCallback(IFinishPayCallback finishPayCallback) {        this.mFinishPayCallback = finishPayCallback;
    }

    @Override
    public void doFinish() {        if (this.mFinishPayCallback != null) {
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
                if (this.getCustomer() == null && this.getEcCard() != null) {
            CustomerLoginResp loginResp = new CustomerLoginResp();
            loginResp.setCustomerId(this.getEcCard().getId());

            loginResp.setLevel(this.getEcCard().getCardLevel().getCardLevelName());
            loginResp.setSex(Sex.MALE);

            BigDecimal integralAccount = new BigDecimal(0);            BigDecimal valueCardAccount = new BigDecimal(this.getMemberCardBalance());            switch (this.getEcCard().getCardType()) {
                case CUSTOMER_ENTITY_CARD:
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
                    loginResp.setCustomerName("");
                    break;

                default:
                    break;

            }
            loginResp.setIntegralBalance(integralAccount);            loginResp.setValueCardBalance(valueCardAccount);            this.setMemberResp(loginResp);
        }
    }

        public void addPaymentRecord(PaymentVo paymentVo) {
        this.mPaidPaymentRecords.addPaymentRecord(paymentVo);
        if (this.mUILitenner != null) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUILitenner.updatePaymentInfo();                }
            });
        }
    }

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

        private void setPrintMemeberInfoByCustomer() {
                if (this.getCustomer() != null) {
            CustomerLoginResp loginResp = new CustomerLoginResp();
            loginResp.setCustomerId(this.getCustomer().customerId);
            loginResp.setLevel(this.getCustomer().levelName);
            loginResp.setSex(this.getCustomer().sex);
            BigDecimal integralAccount = new BigDecimal(this.getCustomer().integral == null ? 0 : this.getCustomer().integral);            BigDecimal valueCardAccount = new BigDecimal(this.getCustomer().remainValue == null ? 0 : this.getCustomer().remainValue);
            loginResp.setIntegralBalance(integralAccount);            loginResp.setValueCardBalance(valueCardAccount);            this.setMemberResp(loginResp);
        }
    }
}
