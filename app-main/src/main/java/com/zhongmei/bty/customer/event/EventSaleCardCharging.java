package com.zhongmei.bty.customer.event;

import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;

import java.math.BigDecimal;


public class EventSaleCardCharging {


    public enum ChargingType {
        CUSTOMER,         CARD     }

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


    public BigDecimal chargeMoney;


    public EventPayResult payResult;


    public BigDecimal sendMoney;


    public ChargingType chargingType;


    public EcCardInfo ecCardInfo;
}
