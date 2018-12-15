package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;

import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.yunfu.db.enums.RefundWay;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * 修改银联退款状态请求类
 *
 * @Date：2016-2-17 下午4:37:15
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class RefundStatusReq {

    private long tradeId;

    private long updatorId;

    private String updatorName;

    private long serverUpdateTime;

    private Integer tradePayStatus;

    private List<RefundPaymentItem> refundPaymentItems;

    private List<PaymentItemUnionpay> refundPaymentItemUnionpays;

    public RefundStatusReq(long tradeId, long updatorId, String updatorName, long serverUpdateTime,
                           TradePayStatus tradePayStatus, List<RefundPaymentItem> refundPaymentItems,
                           List<PaymentItemUnionpay> refundPaymentItemUnionpays) {
        this.tradeId = tradeId;
//		this.updatorId = updatorId;
//		this.updatorName = updatorName;
        this.serverUpdateTime = serverUpdateTime;
//		this.tradePayStatus = ValueEnums.toValue(tradePayStatus);
        this.refundPaymentItems = refundPaymentItems;
        this.refundPaymentItemUnionpays = refundPaymentItemUnionpays;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public TradePayStatus getTradePayStatus() {
        return ValueEnums.toEnum(TradePayStatus.class, tradePayStatus);
    }

    public void setTradePayStatus(TradePayStatus tradePayStatus) {
        this.tradePayStatus = ValueEnums.toValue(tradePayStatus);
    }

    public List<RefundPaymentItem> getRefundPaymentItems() {
        return refundPaymentItems;
    }

    public void setRefundPaymentItems(List<RefundPaymentItem> refundPaymentItems) {
        this.refundPaymentItems = refundPaymentItems;
    }

    public List<PaymentItemUnionpay> getRefundPaymentItemUnionpays() {
        return refundPaymentItemUnionpays;
    }

    public void setRefundPaymentItemUnionpays(List<PaymentItemUnionpay> refundPaymentItemUnionpays) {
        this.refundPaymentItemUnionpays = refundPaymentItemUnionpays;
    }

    public static class RefundPaymentItem {
        private long paymentItemId;

        private Integer refundWay;

        private Integer payStatus;

        public RefundPaymentItem(long paymentItemId, RefundWay refundWay, TradePayStatus payStatus) {
            this.paymentItemId = paymentItemId;
            this.refundWay = ValueEnums.toValue(refundWay);
            this.payStatus = ValueEnums.toValue(payStatus);
        }

        public long getPaymentItemId() {
            return paymentItemId;
        }

        public void setPaymentItemId(long paymentItemId) {
            this.paymentItemId = paymentItemId;
        }

        public RefundWay getRefundWay() {
            return ValueEnums.toEnum(RefundWay.class, refundWay);
        }

        public void setRefundWay(RefundWay refundWay) {
            this.refundWay = ValueEnums.toValue(refundWay);
        }

        public TradePayStatus getPayStatus() {
            return ValueEnums.toEnum(TradePayStatus.class, payStatus);
        }

        public void setPayStatus(TradePayStatus payStatus) {
            this.payStatus = ValueEnums.toValue(payStatus);
        }

    }

}
