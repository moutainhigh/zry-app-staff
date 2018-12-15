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

/**
 * 把原来的CashInfoManager 的公共部分抽象成接口
 * 提供支付信息的接口
 */

public interface IPaymentInfo extends Serializable {

    public TradeVo getTradeVo();//获取支付单信息

    public void setTradeVo(TradeVo tradeVo);//设置订单信息

    public TradeVo getSourceTradeVo();//原单信息

    public void setSourceTradeVo(TradeVo sourceTradeVo);

    public Double getActualAmount();//获取实收

    public double getTradeAmount();//获取订单金额

    public double getSaleAmount();//获取订单金额(优惠前)

    public boolean isDinner();//是否是正餐

    public boolean isBeauty();//是否是美业

    public double getNotPayMent();//获取未支付信息

    public boolean isSplit();//是否拆单

    public void setSplit(boolean isSplit);

    public boolean isOrdered();//是否已经保存了或下单了

    public void setOrdered(boolean isOrdered);//是否已经保存了或下单了

    public int getEraseType();//抹零类别

    public void setEraseType(int eraseType);//设置抹零类别

    public double getOtherPayNotPayMentByModelId(Long ModelId);//获取某其它支付未付金额

    public boolean enablePay();//是否可以支付

    public GroupPay getOtherPay();//获取其它支付信息

    public BusinessType getTradeBusinessType();//获取订单类型

    public long getCustomerId();//会员id

    public void setCustomerId(long customerId);//会员id

    public double getMemberCardBalance();//储值余额

    public void setMemberCardBalance(double memberCardBalance);//储值余额

    public long getMemberIntegral();//会员积分

    public void setMemberIntegral(long memberIntegral);//会员积分

    public CustomerResp getCustomer();//会员积分

    public void setCustomer(CustomerResp customer);//会员积分

    public EcCard getEcCard();//储值卡

    public void setEcCard(EcCard ecCard);//储值卡

    public void setCardCustomer(CustomerResp customer);  //临时添加

    public CustomerResp getCardCustomer(); //临时添加

    public String getMemberPassword();//会员密码

    public void setMemberPassword(String memberPassword);//会员密码

    public CustomerLoginResp getMemberResp();//会员信息

    public void setMemberResp(CustomerLoginResp memberResp);//会员信息

    public double getExemptAmount();//抹零金额

    public Payment getPaidPayment();//已支付payment记录

    public double getPaidAmount();//获取已经支付金额

    public void setPrintMemeberInfoByCard();//设置会员打印信息

    public boolean isPrintedOk();//是否已经打印出单

    public void setPrintedOk(boolean isPrintedOk);

    public boolean isOrderCenter();

    public PaymentVo getTradePaymentVo();

    public String getPaymentUuid();

    public void clearPaymentUUids();

    public void setPaidPaymentRecords(List<PaymentVo> paymentVoList);//设置已付款记录

    public boolean isOpenElectronicInvoice();//是否开电子发票

    public List<PaymentItem> getPaidPaymentItems();//获取已支付记录

    public void clearData();//清空输入的数据

    public PayScene getPayScene();//获取支付场景

    public void setPayScene(PayScene scene);//设置支付场景

    public boolean isNeedToPayDeposit();//是否需要付押金

    public String getId();//获取接口对象的ID

    public int getDefaultPaymentMenuType();

    public void setDefaultPaymentMenuType(int menuType);

    // v8.6.0 增加快捷支付
    public int getQuickPayType();

    public void setQuickPayType(int quickPayType);

    public void doFinish();//add 20180123 finish pay to call

    public PayActionPage getPayActionPage();//add 20180309  sign pay activity

    public void setPayActionPage(PayActionPage payActionPage);//add 20180309  sign pay activity

    public boolean isGroupPay();//是否组合支付

    public void addPaymentRecord(PaymentVo paymentVo);//add v8.14

    public List<PaymentVo> getPaymentRecords();//add v8.14

    //获取收银权限码
    public String getCashPermissionCode();
}
