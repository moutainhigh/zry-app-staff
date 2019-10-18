package com.zhongmei.bty.basemodule.trade.bean;

import java.io.Serializable;
import java.math.BigDecimal;


public class OutTimeInfo implements Serializable {

    private long limitTimeLine;
    private long outTimeUnit;
    private BigDecimal outTimeFee;
    public long getOutTimeUnit() {
        return outTimeUnit;
    }

    public void setOutTimeUnit(long outTimeUnit) {
        this.outTimeUnit = outTimeUnit;
    }

    public BigDecimal getOutTimeFee() {
        return outTimeFee;
    }

    public void setOutTimeFee(BigDecimal outTimeFee) {
        this.outTimeFee = outTimeFee;
    }

    public long getLimitTimeLine() {
        return limitTimeLine;
    }

    public void setLimitTimeLine(long limitTimeLine) {
        this.limitTimeLine = limitTimeLine;
    }

}
