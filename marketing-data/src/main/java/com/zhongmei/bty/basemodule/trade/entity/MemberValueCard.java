package com.zhongmei.bty.basemodule.trade.entity;

import java.math.BigDecimal;

/**
 * 储值及支付返回值
 *
 * @version v8.5.0
 * @since 2018.01.11.
 */
public class MemberValueCard {

    // beforeRealValue	BigDecimal	是	操作前余额实储金额
    // beforeSendValue	BigDecimal	是	操作前储值赠送余额
    // beforePrepareValue	BigDecimal	是	操作前预储金额
    // currentRealValue	BigDecimal	是	本次操作实储金额
    // currentSendValue	BigDecimal	是	本次操作储值赠送金额
    // currentPrepareValue	BigDecimal	是	本次操作预储金额
    // endRealValue	BigDecimal	是	操作后余额实储金额
    // endSendValue	BigDecimal	是	操作后储值赠送余额
    // endPrepareValue	BigDecimal	是	操作后预储金额

    private BigDecimal beforeRealValue = BigDecimal.ZERO;
    private BigDecimal beforeSendValue = BigDecimal.ZERO;
    private BigDecimal beforePrepareValue = BigDecimal.ZERO;
    private BigDecimal currentRealValue = BigDecimal.ZERO;
    private BigDecimal currentSendValue = BigDecimal.ZERO;
    private BigDecimal currentPrepareValue = BigDecimal.ZERO;
    private BigDecimal endRealValue = BigDecimal.ZERO;
    private BigDecimal endSendValue = BigDecimal.ZERO;
    private BigDecimal endPrepareValue = BigDecimal.ZERO;

    public BigDecimal getBeforeRealValue() {
        return beforeRealValue;
    }

    public void setBeforeRealValue(BigDecimal beforeRealValue) {
        this.beforeRealValue = beforeRealValue;
    }

    public BigDecimal getBeforeSendValue() {
        return beforeSendValue;
    }

    public void setBeforeSendValue(BigDecimal beforeSendValue) {
        this.beforeSendValue = beforeSendValue;
    }

    public BigDecimal getBeforePrepareValue() {
        return beforePrepareValue;
    }

    public void setBeforePrepareValue(BigDecimal beforePrepareValue) {
        this.beforePrepareValue = beforePrepareValue;
    }

    public BigDecimal getCurrentRealValue() {
        return currentRealValue;
    }

    public void setCurrentRealValue(BigDecimal currentRealValue) {
        this.currentRealValue = currentRealValue;
    }

    public BigDecimal getCurrentSendValue() {
        return currentSendValue;
    }

    public void setCurrentSendValue(BigDecimal currentSendValue) {
        this.currentSendValue = currentSendValue;
    }

    public BigDecimal getCurrentPrepareValue() {
        return currentPrepareValue;
    }

    public void setCurrentPrepareValue(BigDecimal currentPrepareValue) {
        this.currentPrepareValue = currentPrepareValue;
    }

    public BigDecimal getEndRealValue() {
        return endRealValue;
    }

    public void setEndRealValue(BigDecimal endRealValue) {
        this.endRealValue = endRealValue;
    }

    public BigDecimal getEndSendValue() {
        return endSendValue;
    }

    public void setEndSendValue(BigDecimal endSendValue) {
        this.endSendValue = endSendValue;
    }

    public BigDecimal getEndPrepareValue() {
        return endPrepareValue;
    }

    public void setEndPrepareValue(BigDecimal endPrepareValue) {
        this.endPrepareValue = endPrepareValue;
    }
}
