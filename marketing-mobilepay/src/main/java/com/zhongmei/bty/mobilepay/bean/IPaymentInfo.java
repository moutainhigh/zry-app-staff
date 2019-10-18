package com.zhongmei.bty.mobilepay.bean;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.enums.BusinessType;

import java.io.Serializable;
import java.util.List;



public interface IPaymentInfo extends Serializable {

    public TradeVo getTradeVo();
    public void setTradeVo(TradeVo tradeVo);
    public TradeVo getSourceTradeVo();
    public void setSourceTradeVo(TradeVo sourceTradeVo);

    public Double getActualAmount();
    public double getTradeAmount();
    public double getSaleAmount();
    public boolean isDinner();
    public boolean isBeauty();
    public double getNotPayMent();
    public boolean isSplit();
    public void setSplit(boolean isSplit);

    public boolean isOrdered();
    public void setOrdered(boolean isOrdered);
    public int getEraseType();
    public void setEraseType(int eraseType);
    public double getOtherPayNotPayMentByModelId(Long ModelId);
    public boolean enablePay();
    public GroupPay getOtherPay();
    public BusinessType getTradeBusinessType();
    public long getCustomerId();
    public void setCustomerId(long customerId);
    public double getMemberCardBalance();
    public void setMemberCardBalance(double memberCardBalance);
    public long getMemberIntegral();
    public void setMemberIntegral(long memberIntegral);
    public CustomerResp getCustomer();
    public void setCustomer(CustomerResp customer);
    public EcCard getEcCard();
    public void setEcCard(EcCard ecCard);
    public void setCardCustomer(CustomerResp customer);
    public CustomerResp getCardCustomer();
    public String getMemberPassword();
    public void setMemberPassword(String memberPassword);
    public CustomerLoginResp getMemberResp();
    public void setMemberResp(CustomerLoginResp memberResp);
    public double getExemptAmount();
    public Payment getPaidPayment();
    public double getPaidAmount();
    public void setPrintMemeberInfoByCard();
    public boolean isPrintedOk();
    public void setPrintedOk(boolean isPrintedOk);

    public boolean isOrderCenter();

    public PaymentVo getTradePaymentVo();

    public String getPaymentUuid();

    public void clearPaymentUUids();

    public void setPaidPaymentRecords(List<PaymentVo> paymentVoList);
    public boolean isOpenElectronicInvoice();
    public List<PaymentItem> getPaidPaymentItems();
    public void clearData();
    public PayScene getPayScene();
    public void setPayScene(PayScene scene);
    public boolean isNeedToPayDeposit();
    public String getId();
    public int getDefaultPaymentMenuType();

    public void setDefaultPaymentMenuType(int menuType);

        public int getQuickPayType();

    public void setQuickPayType(int quickPayType);

    public void doFinish();
    public PayActionPage getPayActionPage();
    public void setPayActionPage(PayActionPage payActionPage);
    public boolean isGroupPay();
    public void addPaymentRecord(PaymentVo paymentVo);
    public List<PaymentVo> getPaymentRecords();
        public String getCashPermissionCode();
}
