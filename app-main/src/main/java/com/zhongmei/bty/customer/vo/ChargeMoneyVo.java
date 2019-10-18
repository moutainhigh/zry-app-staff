package com.zhongmei.bty.customer.vo;

import com.zhongmei.bty.basemodule.customer.enums.FullSend;
import com.zhongmei.bty.commonmodule.database.enums.SendType;

import java.math.BigDecimal;


public class ChargeMoneyVo {


    private BigDecimal fullMoney;

    private BigDecimal sendMoney;

    private boolean isAutoInput;

    private SendType sendType;

    private BigDecimal sendRate;

    private FullSend isFullSend;

    public FullSend getIsFullSend() {
        return isFullSend;
    }

    public void setIsFullSend(FullSend isFullSend) {
        this.isFullSend = isFullSend;
    }

    public SendType getSendType() {
        return sendType;
    }

    public void setSendType(SendType sendType) {
        this.sendType = sendType;
    }

    public BigDecimal getSendRate() {
        return sendRate;
    }

    public void setSendRate(BigDecimal sendRate) {
        this.sendRate = sendRate;
    }

    public ChargeMoneyVo(boolean isAuto) {
        isAutoInput = isAuto;
    }

    public boolean isAuto() {
        return isAutoInput;
    }

    public BigDecimal getFullMoney() {
        return fullMoney;
    }

    public void setFullMoney(BigDecimal fullMoney) {
        this.fullMoney = fullMoney;
    }

    public BigDecimal getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }

}
