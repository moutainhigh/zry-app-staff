package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class PrePayRefundReq {

    private Long tradeId;

    private Long clientUpdateTime;

    private Long serverUpdateTime;

    private Long updatorId;

    private String updatorName;

    private BigDecimal refundAmount;//退款金额

    private BigDecimal deductionAmount;//抵扣金额

    private BigDecimal exemptAmount;//抹零金额

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public void setExemptAmount(BigDecimal exemptAmount) {
        this.exemptAmount = exemptAmount;
    }
}
