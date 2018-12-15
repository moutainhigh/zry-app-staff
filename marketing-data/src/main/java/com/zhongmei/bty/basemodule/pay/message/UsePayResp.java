package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;

import java.math.BigDecimal;
import java.util.List;

/**
 * 接口相应体
 */

public class UsePayResp {
    private List<PaymentItemResult> paymentItemResults;//支付明细结果列表

    public List<PaymentItemResult> getPaymentItemResults() {
        return paymentItemResults;
    }

    public void setPaymentItemResults(List<PaymentItemResult> paymentItemResults) {
        this.paymentItemResults = paymentItemResults;
    }

    public class PaymentItemResult {
        private PaymentItemAddition addition;
        private String paymentItemUuid;//支付明细uuid
        private String resultMsg;//结果信息
        private Integer resultStatus;//结果状态码

        public PaymentItemAddition getAddition() {
            return addition;
        }

        public void setAddition(PaymentItemAddition addition) {
            this.addition = addition;
        }

        public String getPaymentItemUuid() {
            return paymentItemUuid;
        }

        public void setPaymentItemUuid(String paymentItemUuid) {
            this.paymentItemUuid = paymentItemUuid;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public Integer getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(Integer resultStatus) {
            this.resultStatus = resultStatus;
        }
    }

    public class PaymentItemAddition {
        private String codeUrl;

        private PaymentItem paymentItem;
        private PaymentItemGroupon paymentItemGroupon;
        private TradePromotion tradePromotion;

        private BigDecimal subValue;
        private BigDecimal beforeActualvalue;
        private BigDecimal beforeSendValue;
        private BigDecimal currentReduceActualValue;
        private BigDecimal currentReduceSendValue;
        private BigDecimal beforeIntegralValue;
        private BigDecimal currentIntegralReduceValue;
        private BigDecimal afterIntegralValue;

        public String getCodeUrl() {
            return codeUrl;
        }

        public void setCodeUrl(String codeUrl) {
            this.codeUrl = codeUrl;
        }

        public PaymentItem getPaymentItem() {
            return paymentItem;
        }

        public void setPaymentItem(PaymentItem paymentItem) {
            this.paymentItem = paymentItem;
        }

        public PaymentItemGroupon getPaymentItemGroupon() {
            return paymentItemGroupon;
        }

        public void setPaymentItemGroupon(PaymentItemGroupon paymentItemGroupon) {
            this.paymentItemGroupon = paymentItemGroupon;
        }

        public TradePromotion getTradePromotion() {
            return tradePromotion;
        }

        public void setTradePromotion(TradePromotion tradePromotion) {
            this.tradePromotion = tradePromotion;
        }

        public BigDecimal getSubValue() {
            return subValue;
        }

        public void setSubValue(BigDecimal subValue) {
            this.subValue = subValue;
        }

        public BigDecimal getBeforeActualvalue() {
            return beforeActualvalue;
        }

        public void setBeforeActualvalue(BigDecimal beforeActualvalue) {
            this.beforeActualvalue = beforeActualvalue;
        }

        public BigDecimal getBeforeSendValue() {
            return beforeSendValue;
        }

        public void setBeforeSendValue(BigDecimal beforeSendValue) {
            this.beforeSendValue = beforeSendValue;
        }

        public BigDecimal getCurrentReduceActualValue() {
            return currentReduceActualValue;
        }

        public void setCurrentReduceActualValue(BigDecimal currentReduceActualValue) {
            this.currentReduceActualValue = currentReduceActualValue;
        }

        public BigDecimal getCurrentReduceSendValue() {
            return currentReduceSendValue;
        }

        public void setCurrentReduceSendValue(BigDecimal currentReduceSendValue) {
            this.currentReduceSendValue = currentReduceSendValue;
        }

        public BigDecimal getBeforeIntegralValue() {
            return beforeIntegralValue;
        }

        public void setBeforeIntegralValue(BigDecimal beforeIntegralValue) {
            this.beforeIntegralValue = beforeIntegralValue;
        }

        public BigDecimal getCurrentIntegralReduceValue() {
            return currentIntegralReduceValue;
        }

        public void setCurrentIntegralReduceValue(BigDecimal currentIntegralReduceValue) {
            this.currentIntegralReduceValue = currentIntegralReduceValue;
        }

        public BigDecimal getAfterIntegralValue() {
            return afterIntegralValue;
        }

        public void setAfterIntegralValue(BigDecimal afterIntegralValue) {
            this.afterIntegralValue = afterIntegralValue;
        }
    }
}
