package com.zhongmei.bty.basemodule.trade.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Date： 2016/7/19
 * @Description:押金内容
 * @Version: 1.0
 */
public class OutTimeInfo implements Serializable {

    private long limitTimeLine;//超时限制

    private long outTimeUnit;//超时单位 例如：30分钟

    private BigDecimal outTimeFee;//超时费用 例如：50元

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
