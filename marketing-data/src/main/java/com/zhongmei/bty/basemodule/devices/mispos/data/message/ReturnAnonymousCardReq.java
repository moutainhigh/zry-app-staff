package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.trade.message.TradePaymentReq;


public class ReturnAnonymousCardReq {

    private TradePaymentReq refundContent;
    private Long creatorId;

    private String creatorName;

    private String cardNo;

    private Long srcTradeItemId;

    private String refundTradeItemUuid;


    public TradePaymentReq getRefundContent() {
        return refundContent;
    }

    public void setRefundContent(TradePaymentReq refundContent) {
        this.refundContent = refundContent;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRefundTradeItemUuid() {
        return refundTradeItemUuid;
    }

    public void setRefundTradeItemUuid(String refundTradeItemUuid) {
        this.refundTradeItemUuid = refundTradeItemUuid;
    }

    public Long getSrcTradeItemId() {
        return srcTradeItemId;
    }

    public void setSrcTradeItemId(Long srcTradeItemId) {
        this.srcTradeItemId = srcTradeItemId;
    }
}
