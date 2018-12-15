package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

import java.math.BigDecimal;

public class GrouponNuomiPayReq extends PaymentItemReq {
    private GrouponNuomiPayReqItem paymentItemGroupon;//券信息

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
        private String dealTitle;//团单名称
        private Long grouponId;//团购groupon.id
        private BigDecimal marketPrice;//市场价(元)
        private BigDecimal price;//销售价(元)
        private String serialNo;//团购券码
        private Integer useCount;//准备用掉的团购券张数

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
