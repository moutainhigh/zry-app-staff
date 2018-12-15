package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.trade.message.TradePaymentReq;

/**
 * @Date：2015年12月16日 下午6:49:37
 * @Description: 封装无单退货的数据
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class SalesReturnTradePaymentReq extends TradePaymentReq {
    private Long reasonId;

    private String reasonContent;

    private boolean reviseStock;//是否需要恢复库存

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
