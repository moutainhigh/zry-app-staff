package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import java.math.BigDecimal;

/**
 * 会员充值接口回复数据封装
 *
 * @version: 1.0
 * @date 2015年5月8日
 */
public class CardRechargeResp {

    private BigDecimal valueCard;

    private BigDecimal currentSendValue;

    private BigDecimal currentTotalAdd;

    private Integer integral;

    public BigDecimal getValueCard() {
        return valueCard;
    }

    public void setValueCard(BigDecimal valueCard) {
        this.valueCard = valueCard;
    }

    public BigDecimal getCurrentSendValue() {
        return currentSendValue;
    }

    public void setCurrentSendValue(BigDecimal currentSendValue) {
        this.currentSendValue = currentSendValue;
    }

    public BigDecimal getCurrentTotalAdd() {
        return currentTotalAdd;
    }

    public void setCurrentTotalAdd(BigDecimal currentTotalAdd) {
        this.currentTotalAdd = currentTotalAdd;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

}
