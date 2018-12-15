package com.zhongmei.bty.customer.event;

import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;

import java.math.BigDecimal;

/**
 * 售卡处  充值 成功 event
 * <p>
 * Created by demo on 2018/12/15
 */
public class EventSaleCardCharging {

    /**
     * 标记储值类型
     */
    public enum ChargingType {
        CUSTOMER, // 虚拟会员
        CARD // 卡
    }

    public EventSaleCardCharging(BigDecimal chargeMoney, BigDecimal sendMoney, EventPayResult payResult) {
        this.chargeMoney = chargeMoney;
        this.payResult = payResult;
        this.sendMoney = sendMoney;
    }

    public EventSaleCardCharging(BigDecimal chargeMoney, BigDecimal sendMoney, EventPayResult payResult, ChargingType type, EcCardInfo ecCardInfo) {
        this.chargeMoney = chargeMoney;
        this.payResult = payResult;
        this.sendMoney = sendMoney;
        this.chargingType = type;
        this.ecCardInfo = ecCardInfo;
    }

    /**
     * 充值金额
     */
    public BigDecimal chargeMoney;

    /**
     * 充值返回结果
     */
    public EventPayResult payResult;

    /**
     * 赠送金额
     */
    public BigDecimal sendMoney;

    /**
     * 储值类型
     */
    public ChargingType chargingType;

    /**
     * 卡信息
     */
    public EcCardInfo ecCardInfo;
}
