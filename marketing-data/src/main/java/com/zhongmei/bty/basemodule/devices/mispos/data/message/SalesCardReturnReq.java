package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import java.util.List;

import com.zhongmei.bty.basemodule.trade.message.TradePaymentReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.ReturnCardDataModel.CardNumber;


public class SalesCardReturnReq {
    private Long refundWay;

    private Long srcTradeId;

    private Long srcServerUpdateTime;

    private Long creatorId;

    private String creatorName;

    private Long reasonId;

    private String reasonContent;

    private TradePaymentReq partRefundContent;

    private List<CardNumber> cardList;

    public List<CardNumber> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardNumber> cardList) {
        this.cardList = cardList;
    }

    public TradePaymentReq getPartRefundContent() {
        return partRefundContent;
    }

    public void setPartRefundContent(TradePaymentReq partRefundContent) {
        this.partRefundContent = partRefundContent;
    }

    public Long getRefundWay() {
        return refundWay;
    }

    public void setRefundWay(Long refundWay) {
        this.refundWay = refundWay;
    }

    public Long getSrcTradeId() {
        return srcTradeId;
    }

    public void setSrcTradeId(Long srcTradeId) {
        this.srcTradeId = srcTradeId;
    }

    public Long getSrcServerUpdateTime() {
        return srcServerUpdateTime;
    }

    public void setSrcServerUpdateTime(Long srcServerUpdateTime) {
        this.srcServerUpdateTime = srcServerUpdateTime;
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
