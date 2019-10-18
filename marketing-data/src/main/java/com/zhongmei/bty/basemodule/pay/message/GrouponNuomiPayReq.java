package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

import java.math.BigDecimal;

public class GrouponNuomiPayReq extends PaymentItemReq {
    private GrouponNuomiPayReqItem paymentItemGroupon;
    public GrouponNuomiPayReq(PaymentItem paymentItem) {
        super(paymentItem);
        GrouponNuomiPayReqItem grouponNuomiPayReqItem = new GrouponNuomiPayReqItem();
        grouponNuomiPayReqItem.setDealTitle(paymentItem.getPaymentItemGroupon().getDealTitle());
        grouponNuomiPayReqItem.setGrouponId(paymentItem.getPaymentItemGroupon().getGrouponId());
        grouponNuomiPayReqItem.setMarketPrice(paymentItem.getPaymentItemGroupon().getMarketPrice());
        grouponNuomiPayReqItem.setPrice(paymentItem.getPaymentItemGroupon().getPrice());
        grouponNuomiPayReqItem.setSerialNo(paymentItem.getPaymentItemGroupon().getSerialNo());
        grouponNuomiPayReqItem.setUseCount(paymentItem.getPaymentItemGroupon().getUseCount());
        paymentItemGroupon = grouponNuomiPayReqItem;
    }

    public GrouponNuomiPayReqItem getPaymentItemGroupon() {
        return paymentItemGroupon;
    }

    public void setPaymentItemGroupon(GrouponNuomiPayReqItem paymentItemGroupon) {
        this.paymentItemGroupon = paymentItemGroupon;
    }

    public class GrouponNuomiPayReqItem {
        private String dealTitle;        private Long grouponId;        private BigDecimal marketPrice;        private BigDecimal price;        private String serialNo;        private Integer useCount;
        public String getDealTitle() {
            return dealTitle;
        }

        public void setDealTitle(String dealTitle) {
            this.dealTitle = dealTitle;
        }

        public Long getGrouponId() {
            return grouponId;
        }

        public void setGrouponId(Long grouponId) {
            this.grouponId = grouponId;
        }

        public BigDecimal getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(BigDecimal marketPrice) {
            this.marketPrice = marketPrice;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public Integer getUseCount() {
            return useCount;
        }

        public void setUseCount(Integer useCount) {
            this.useCount = useCount;
        }
    }
}
