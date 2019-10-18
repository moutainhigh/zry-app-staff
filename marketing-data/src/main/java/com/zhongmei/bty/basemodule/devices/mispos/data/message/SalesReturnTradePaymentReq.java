package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.trade.message.TradePaymentReq;


public class SalesReturnTradePaymentReq extends TradePaymentReq {
    private Long reasonId;

    private String reasonContent;

    private boolean reviseStock;
    public boolean isReviseStock() {
        return reviseStock;
    }

    public void setReviseStock(boolean reviseStock) {
        this.reviseStock = reviseStock;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }
}
